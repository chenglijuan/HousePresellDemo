package zhishusz.housepresell.approvalprocess;

import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Empj_BldEscrowCompletedForm;
import zhishusz.housepresell.controller.form.Empj_BldEscrowCompleted_DtlForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Empj_BldEscrowCompletedDao;
import zhishusz.housepresell.database.dao.Empj_BldEscrowCompleted_DtlDao;
import zhishusz.housepresell.database.dao.Empj_BuildingExtendInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tgpf_SocketMsgDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted_Dtl;
import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_SocketMsg;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_EscrowState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.database.po.toInterface.To_building;
import zhishusz.housepresell.service.CommonService;
import zhishusz.housepresell.service.Empj_BldAccountGetLimitAmountVerService;
import zhishusz.housepresell.service.Sm_AttachmentBatchAddService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.ToInterface;

/**
 * 托管终止： 审批过后-业务逻辑处理
 */
public class ApprovalProcessCallBack_03030102 implements IApprovalProcessCallback {
	private static final String BUSI_CODE = "03030102";// 具体业务编码参看SVN文

	@Autowired
	private Tgpf_SocketMsgDao tgpf_SocketMsgDao;// 接口报文表
	@Autowired
	private Empj_BldEscrowCompletedDao empj_bldEscrowCompletedDao;
	@Autowired
	private Empj_BldEscrowCompleted_DtlDao empj_bldEscrowCompleted_DtlDao;
	@Autowired
	private Gson gson;
	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
	@Autowired
	private Empj_BldAccountGetLimitAmountVerService bldAccountGetLimitAmountVerService;
	@Autowired
	private Empj_BuildingExtendInfoDao empj_buildingExtendInfoDao;
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;
	@Autowired
	private CommonService commonService;

	@Autowired
	private Sm_AttachmentCfgDao attacmentcfgDao;
	@Autowired
	private Sm_AttachmentDao attacmentDao;

	@SuppressWarnings("unchecked")
	@Override
	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm) {
		Properties properties = new MyProperties();

		// 获取当前操作人（备案人）
		Sm_User user = baseForm.getUser();
		try {
			String workflowEcode = approvalProcessWorkflow.geteCode();

			// 获取正在处理的申请单
			Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();

			// 获取正在处理的申请单所属的流程配置
			Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = sm_ApprovalProcess_AF.getConfiguration();
			String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;

			// 获取正在审批的项目
			Long empj_BldEscrowCompletedId = sm_ApprovalProcess_AF.getSourceId();
			Empj_BldEscrowCompleted empj_bldEscrowCompleted = empj_bldEscrowCompletedDao
					.findById(empj_BldEscrowCompletedId);

			if (empj_bldEscrowCompleted == null) {
				return MyBackInfo.fail(properties, "审批的托管终止不存在");
			}
			// Empj_BldEscrowCompleted empj_bldEscrowCompletedOld =
			// ObjectCopier.copy(empj_bldEscrowCompleted);

			// 驳回到发起人，发起人撤回，不通过 --还原楼幢托管状态、受限额度等
			if (S_ApprovalState.WaitSubmit.equals(sm_ApprovalProcess_AF.getBusiState())
					|| S_ApprovalState.NoPass.equals(sm_ApprovalProcess_AF.getBusiState())) {
				empj_bldEscrowCompleted.setApprovalState(S_ApprovalState.WaitSubmit); // 已完结
				empj_bldEscrowCompletedDao.save(empj_bldEscrowCompleted);

				/**
				 * TODO 1、更改楼幢信息的托管状态-> 已托管
				 * 2、已备案：更新"当前形象进度"为"交付"，"当前受限比例（%）"为0，"当前受限额度（元）"为0，都有做还原处理
				 */
				List<Empj_BldEscrowCompleted_Dtl> empj_BldEscrowCompleted_Dtls = empj_bldEscrowCompleted
						.getEmpj_BldEscrowCompleted_DtlList();
				if (empj_BldEscrowCompleted_Dtls != null) {
					for (Empj_BldEscrowCompleted_Dtl empjBldEscrowCompletedDtl : empj_BldEscrowCompleted_Dtls) {
						Empj_BuildingInfo buildingInfo = empjBldEscrowCompletedDtl.getBuilding();

						// 1、更改托管状态
						Empj_BuildingExtendInfo buildingExtendInfo = buildingInfo.getExtendInfo();
						if (buildingExtendInfo != null) {
							buildingExtendInfo.setEscrowState(S_EscrowState.HasEscrowState);
							empj_buildingExtendInfoDao.save(buildingExtendInfo);
						}
					}
				}

				/*
				 * 还原公安-施工编号对照表
				 */
				// 根据业务编号查询配置文件
				Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
				form.setTheState(S_TheState.Normal);
				form.setBusiType("240180");

				Sm_AttachmentCfg sm_AttachmentCfg = attacmentcfgDao
						.findOneByQuery_T(attacmentcfgDao.getQuery(attacmentcfgDao.getBasicHQL(), form));

				if (null != sm_AttachmentCfg) {
					Sm_AttachmentForm form1 = new Sm_AttachmentForm();
					form1.setSourceId(empj_BldEscrowCompletedId.toString());
					form1.setBusiType("240180");
					form1.setSourceType(sm_AttachmentCfg.geteCode());
					form1.setTheState(S_TheState.Normal);
					form1.setAttachmentCfg(sm_AttachmentCfg);

					Sm_Attachment attachment = attacmentDao
							.findOneByQuery_T(attacmentDao.getQuery(attacmentDao.getBasicHQL(), form1));

					if (null != attachment) {
						attachment.setTheState(S_TheState.Deleted);
						attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
						attachment.setUserUpdate(user);
						attacmentDao.update(attachment);

					}
				}

			}

			switch (approvalProcessWork) {
			case S_BusiCode.busiCode_03030102 + "001_ZS":
				if (S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState())
						&& S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState())) {
					String jsonStr = sm_ApprovalProcess_AF.getExpectObjJson();
					if (jsonStr != null && jsonStr.length() > 0) {

						// 查询开关
						Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
						baseParameterForm0.setTheState(S_TheState.Normal);
						baseParameterForm0.setTheValue("710000");
						baseParameterForm0.setParametertype("71");
						Sm_BaseParameter baseParameter0 = sm_BaseParameterDao.findOneByQuery_T(
								sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));

						Empj_BldEscrowCompletedForm empj_bldEscrowCompletedForm = gson.fromJson(jsonStr,
								Empj_BldEscrowCompletedForm.class);

						empj_bldEscrowCompleted.setTheState(S_TheState.Normal);
						empj_bldEscrowCompleted.setUserRecord(approvalProcessWorkflow.getUserUpdate());
						empj_bldEscrowCompleted.setRecordTimeStamp(System.currentTimeMillis());
						empj_bldEscrowCompleted.setBusiState(S_BusiState.HaveRecord); // 已备案
						empj_bldEscrowCompleted.setApprovalState(S_ApprovalState.Completed); // 已完结
						empj_bldEscrowCompletedDao.save(empj_bldEscrowCompleted);

						empj_bldEscrowCompletedForm.setBusiType(BUSI_CODE);
						sm_AttachmentBatchAddService.execute(empj_bldEscrowCompletedForm, empj_BldEscrowCompletedId);

						/**
						 * TODO 1、更改楼幢信息的托管状态-> 托管终止
						 * 2、已备案：更新"当前形象进度"为"交付"，"当前受限比例（%）"为0，"当前受限额度（元）"为0，并保存
						 * 3、推送消息给财务部门，需包含终止楼幢、托管余额等新（审批流模块已包含此功能）
						 */

						Empj_BldEscrowCompleted_DtlForm dtlForm = new Empj_BldEscrowCompleted_DtlForm();
						dtlForm.setTheState(S_TheState.Normal);
						dtlForm.setMainTable(empj_bldEscrowCompleted);
						dtlForm.setMainTableId(empj_BldEscrowCompletedId);
						List<Empj_BldEscrowCompleted_Dtl> empj_BldEscrowCompleted_Dtls = empj_bldEscrowCompleted_DtlDao
								.findByPage(empj_bldEscrowCompleted_DtlDao.listByCriteria(dtlForm));

						if (null == empj_BldEscrowCompleted_Dtls) {
							empj_BldEscrowCompleted_Dtls = empj_bldEscrowCompleted.getEmpj_BldEscrowCompleted_DtlList();
						}

						if (empj_BldEscrowCompleted_Dtls != null) {
							for (Empj_BldEscrowCompleted_Dtl empjBldEscrowCompletedDtl : empj_BldEscrowCompleted_Dtls) {
								Empj_BuildingInfo buildingInfo = empjBldEscrowCompletedDtl.getBuilding();

								// 1、更改托管状态
								Empj_BuildingExtendInfo buildingExtendInfo = buildingInfo.getExtendInfo();
								if (buildingExtendInfo != null) {
									buildingExtendInfo.setEscrowState(S_EscrowState.EndEscrowState);
									empj_buildingExtendInfoDao.save(buildingExtendInfo);
								}

								// 查询需要修改的楼幢账户
								Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();
								if (buildingAccount != null) {
									Empj_BuildingInfoForm buildingInfoForm = new Empj_BuildingInfoForm();
									buildingInfoForm.setTableId(buildingInfo.getTableId());
									Properties laProperties = bldAccountGetLimitAmountVerService
											.executeZeroLimitAmountNode(buildingInfoForm);
									if (!MyBackInfo.isSuccess(laProperties)) {
										return MyBackInfo.fail(properties, laProperties.getProperty(S_NormalFlag.info));
									}
									Tgpj_BldLimitAmountVer_AFDtl bldLimitAmountVer_afDtl = (Tgpj_BldLimitAmountVer_AFDtl) laProperties
											.get("limitAmountNode");
									if (bldLimitAmountVer_afDtl == null) {
										return MyBackInfo.fail(properties, "该楼幢不存在受限额度版本节点");
									}

									// 更新
									buildingAccount.setCurrentFigureProgress(bldLimitAmountVer_afDtl.getStageName());
									buildingAccount.setCurrentLimitedRatio(0.0);
									buildingAccount.setNodeLimitedAmount(0.0);
									buildingAccount.setBldLimitAmountVerDtl(bldLimitAmountVer_afDtl);
									buildingAccount.setCashLimitedAmount(0.0);
									buildingAccount.setSpilloverAmount(buildingAccount.getCurrentEscrowFund());
									buildingAccount.setAllocableAmount(buildingAccount.getCurrentEscrowFund());
									buildingAccount.setEffectiveLimitedAmount(0.0);
									buildingAccount.setLastUpdateTimeStamp(System.currentTimeMillis());
									buildingAccount.setUserUpdate(user);
									tgpj_BuildingAccountDao.update(buildingAccount);

									/*
									 * Long accountVersion =
									 * buildingAccount.getVersionNo(); if (null
									 * == buildingAccount.getVersionNo() ||
									 * buildingAccount.getVersionNo() < 0) {
									 * accountVersion = 1l; }
									 * 
									 * // 根据楼幢账户查询版本号最大的楼幢账户log表 // 查询条件：1.业务编码
									 * 2.楼幢账户 3.关联主键 4.根据版本号大小排序
									 * Tgpj_BuildingAccountLogForm
									 * tgpj_BuildingAccountLogForm = new
									 * Tgpj_BuildingAccountLogForm();
									 * tgpj_BuildingAccountLogForm.setTheState(
									 * S_TheState.Normal);
									 * tgpj_BuildingAccountLogForm.
									 * setRelatedBusiCode(BUSI_CODE);
									 * tgpj_BuildingAccountLogForm.
									 * setTgpj_BuildingAccount(buildingAccount);
									 * 
									 * 
									 * Integer logCount =
									 * tgpj_BuildingAccountLogDao
									 * .findByPage_Size(
									 * tgpj_BuildingAccountLogDao.getQuery_Size(
									 * tgpj_BuildingAccountLogDao.getSpecialHQL(
									 * ), tgpj_BuildingAccountLogForm));
									 * 
									 * List<Tgpj_BuildingAccountLog>
									 * tgpj_BuildingAccountLogList; if (logCount
									 * > 0) { tgpj_BuildingAccountLogList =
									 * tgpj_BuildingAccountLogDao
									 * .findByPage(tgpj_BuildingAccountLogDao.
									 * getQuery(
									 * tgpj_BuildingAccountLogDao.getSpecialHQL(
									 * ), tgpj_BuildingAccountLogForm));
									 * 
									 * Tgpj_BuildingAccountLog
									 * buildingAccountLog =
									 * tgpj_BuildingAccountLogList.get(0); //
									 * 获取日志表的版本号 Long logVersionNo =
									 * buildingAccountLog.getVersionNo(); if
									 * (null ==
									 * buildingAccountLog.getVersionNo() ||
									 * buildingAccountLog.getVersionNo() < 0) {
									 * logVersionNo = 1l; }
									 * 
									 * if (logVersionNo == accountVersion) {
									 * tgpj_BuildingAccountLimitedUpdateService.
									 * execute(buildingAccountLog); } else if
									 * (logVersionNo < accountVersion) { //
									 * 保存楼幢账户log表 // 不发生修改的字段
									 * Tgpj_BuildingAccountLog
									 * tgpj_BuildingAccountLog = new
									 * Tgpj_BuildingAccountLog();
									 * tgpj_BuildingAccountLog.setTheState(
									 * S_TheState.Normal);
									 * tgpj_BuildingAccountLog.setBusiState("0")
									 * ; tgpj_BuildingAccountLog.seteCode(
									 * buildingAccount.geteCode());
									 * tgpj_BuildingAccountLog.setUserStart(
									 * buildingAccount.getUserStart());
									 * tgpj_BuildingAccountLog
									 * .setCreateTimeStamp(buildingAccount.
									 * getCreateTimeStamp());
									 * tgpj_BuildingAccountLog.setUserUpdate(
									 * buildingAccount.getUserUpdate());
									 * tgpj_BuildingAccountLog.
									 * setLastUpdateTimeStamp(System.
									 * currentTimeMillis());
									 * tgpj_BuildingAccountLog.setUserRecord(
									 * buildingAccount.getUserRecord());
									 * tgpj_BuildingAccountLog
									 * .setRecordTimeStamp(buildingAccount.
									 * getRecordTimeStamp());
									 * tgpj_BuildingAccountLog
									 * .setDevelopCompany(buildingAccount.
									 * getDevelopCompany());
									 * tgpj_BuildingAccountLog.
									 * seteCodeOfDevelopCompany(
									 * buildingAccount.geteCodeOfDevelopCompany(
									 * )); tgpj_BuildingAccountLog.setProject(
									 * buildingAccount.getProject());
									 * tgpj_BuildingAccountLog
									 * .setTheNameOfProject(buildingAccount.
									 * getTheNameOfProject());
									 * tgpj_BuildingAccountLog.setBuilding(
									 * buildingAccount.getBuilding());
									 * tgpj_BuildingAccountLog.setPayment(
									 * buildingAccount.getPayment());
									 * tgpj_BuildingAccountLog
									 * .seteCodeOfBuilding(buildingAccount.
									 * geteCodeOfBuilding());
									 * tgpj_BuildingAccountLog
									 * .setEscrowStandard(buildingAccount.
									 * getEscrowStandard());
									 * tgpj_BuildingAccountLog.setEscrowArea(
									 * buildingAccount.getEscrowArea());
									 * tgpj_BuildingAccountLog.setBuildingArea(
									 * buildingAccount.getBuildingArea());
									 * tgpj_BuildingAccountLog
									 * .setOrgLimitedAmount(buildingAccount.
									 * getOrgLimitedAmount());
									 * tgpj_BuildingAccountLog
									 * .setTotalGuaranteeAmount(buildingAccount.
									 * getTotalGuaranteeAmount());
									 * tgpj_BuildingAccountLog
									 * .setCashLimitedAmount(buildingAccount.
									 * getCashLimitedAmount());
									 * tgpj_BuildingAccountLog
									 * .setTotalAccountAmount(buildingAccount.
									 * getTotalAccountAmount());
									 * tgpj_BuildingAccountLog.setPayoutAmount(
									 * buildingAccount.getPayoutAmount());
									 * tgpj_BuildingAccountLog.
									 * setAppliedNoPayoutAmount(
									 * buildingAccount.getAppliedNoPayoutAmount(
									 * )); tgpj_BuildingAccountLog.
									 * setApplyRefundPayoutAmount(
									 * buildingAccount.
									 * getApplyRefundPayoutAmount());
									 * tgpj_BuildingAccountLog.setRefundAmount(
									 * buildingAccount.getRefundAmount());
									 * tgpj_BuildingAccountLog
									 * .setCurrentEscrowFund(buildingAccount.
									 * getCurrentEscrowFund());
									 * tgpj_BuildingAccountLog.
									 * setAppropriateFrozenAmount(
									 * buildingAccount.
									 * getAppropriateFrozenAmount());
									 * tgpj_BuildingAccountLog.
									 * setRecordAvgPriceOfBuildingFromPresellSystem(
									 * buildingAccount.
									 * getRecordAvgPriceOfBuildingFromPresellSystem
									 * ());
									 * 
									 * tgpj_BuildingAccountLog.setLogId(
									 * buildingAccount.getLogId());
									 * tgpj_BuildingAccountLog.setActualAmount(
									 * buildingAccount.getActualAmount());
									 * tgpj_BuildingAccountLog.setPaymentLines(
									 * buildingAccount.getPaymentLines());
									 * tgpj_BuildingAccountLog.
									 * setRelatedBusiCode(BUSI_CODE);
									 * tgpj_BuildingAccountLog
									 * .setRelatedBusiTableId(
									 * empjBldEscrowCompletedDtl.getTableId());
									 * tgpj_BuildingAccountLog.
									 * setTgpj_BuildingAccount(buildingAccount);
									 * tgpj_BuildingAccountLog.setVersionNo(
									 * buildingAccount.getVersionNo());
									 * tgpj_BuildingAccountLog
									 * .setPaymentProportion(buildingAccount.
									 * getPaymentProportion());
									 * tgpj_BuildingAccountLog
									 * .setBuildAmountPaid(buildingAccount.
									 * getBuildAmountPaid());
									 * tgpj_BuildingAccountLog
									 * .setBuildAmountPay(buildingAccount.
									 * getBuildAmountPay());
									 * tgpj_BuildingAccountLog.
									 * setTotalAmountGuaranteed(
									 * buildingAccount.getTotalAmountGuaranteed(
									 * )); tgpj_BuildingAccountLog
									 * .setCashLimitedAmount(buildingAccount.
									 * getCashLimitedAmount());
									 * tgpj_BuildingAccountLog.
									 * setEffectiveLimitedAmount(
									 * buildingAccount.getEffectiveLimitedAmount
									 * ()); tgpj_BuildingAccountLog
									 * .setSpilloverAmount(buildingAccount.
									 * getSpilloverAmount());
									 * tgpj_BuildingAccountLog
									 * .setAllocableAmount(buildingAccount.
									 * getAllocableAmount());
									 * tgpj_BuildingAccountLog.
									 * setRecordAvgPriceOfBuilding(
									 * buildingAccount.
									 * getRecordAvgPriceOfBuilding());
									 * 
									 * // 修改产生了变更的字段 tgpj_BuildingAccountLog
									 * .setCurrentFigureProgress(
									 * bldLimitAmountVer_afDtl.getStageName());
									 * // 当前形象进度 tgpj_BuildingAccountLog.
									 * setCurrentLimitedRatio(0.0); // 当前受限比例
									 * tgpj_BuildingAccountLog.
									 * setNodeLimitedAmount(0.0); // 当前受限额度
									 * tgpj_BuildingAccountLog.
									 * setBldLimitAmountVerDtl(
									 * bldLimitAmountVer_afDtl);
									 * tgpj_BuildingAccountLog.
									 * setCashLimitedAmount(0.0);
									 * 
									 * tgpj_BuildingAccountLogDao.save(
									 * tgpj_BuildingAccountLog);
									 * 
									 * tgpj_BuildingAccountLimitedUpdateService.
									 * execute(tgpj_BuildingAccountLog); } else
									 * if (logVersionNo > accountVersion) {
									 * return MyBackInfo.fail(properties,
									 * "备案版本存在回档，请核实后重新发起！"); }
									 * 
									 * }
									 */
								}

								/**
								 * xsz by 2019-10-11 19:05:12 判断是否需要推送
								 * 
								 * START
								 */

								if (null != baseParameter0 && "1".equals(baseParameter0.getTheName())) {
									Boolean interFaceAction = toInterFaceAction(buildingInfo,
											String.valueOf(buildingInfo.getTableId()),
											MyDatetime.getInstance().dateToString(System.currentTimeMillis()));
									/*
									 * if (!interFaceAction) {
									 * properties.put(S_NormalFlag.result,
									 * S_NormalFlag.fail);
									 * properties.put(S_NormalFlag.info,
									 * "消息推送门户网站失败！"); }
									 */

								}

								/**
								 * xsz by 2019-10-11 19:05:12 判断是否需要推送
								 * 
								 * END
								 */

							}
						}

						// if(baseForm != null)
						// {
						// baseForm.setUser(sm_ApprovalProcess_AF.getUserStart());
						// Empj_BldEscrowCompleted empj_bldEscrowCompletedNew =
						// ObjectCopier.copy(empj_bldEscrowCompleted);
						// logAddService.addLog(baseForm,
						// empj_BldEscrowCompletedId,
						// empj_bldEscrowCompletedOld,
						// empj_bldEscrowCompletedNew);
						// }

						/*
						 * xsz by time 提交结束后调用生成PDF方法 并将生成PDF后上传值OSS路径返回给前端
						 * 
						 * 参数： sourceBusiCode：业务编码 sourceId：单据ID
						 * 
						 * xsz by time 2019-1-21 08:54:03 首先判断提交人是否具有签章
						 */

						/*
						 * String isSignature =
						 * sm_ApprovalProcess_AF.getUserStart().getIsSignature()
						 * ; if(null != isSignature && "1".equals(isSignature))
						 * { if(null!=user.getIsSignature()&&"1".equals(user.
						 * getIsSignature())) {
						 * 
						 * ExportPdfForm pdfModel = new ExportPdfForm();
						 * pdfModel.setSourceBusiCode(BUSI_CODE);
						 * pdfModel.setSourceId(String.valueOf(
						 * empj_BldEscrowCompletedId)); Properties
						 * executeProperties =
						 * exportPdfByWordService.execute(pdfModel); String
						 * pdfUrl = (String) executeProperties.get("pdfUrl");
						 * 
						 * Map<String, String> signatureMap = new HashMap<>();
						 * 
						 * signatureMap.put("signaturePath", pdfUrl); //TODO
						 * 此配置后期做成配置 signatureMap.put("signatureKeyword",
						 * "常州正泰房产居间服务有限公司（盖章）"); signatureMap.put("ukeyNumber",
						 * user.getUkeyNumber());
						 * 
						 * properties.put("signatureMap", signatureMap);
						 * 
						 * } }
						 */

						if (null != baseParameter0 && "1".equals(baseParameter0.getTheName())) {
							// 推送门户网站
							if ("1".equals(empj_bldEscrowCompleted.getHasFormula())
									&& StrUtil.isNotBlank(empj_bldEscrowCompleted.getWebSite())) {
								boolean pushBldEscrowCompleted = commonService
										.pushBldEscrowCompleted(empj_bldEscrowCompleted);
								if (pushBldEscrowCompleted) {
									empj_bldEscrowCompleted.setHasPush("1");
									empj_bldEscrowCompletedDao.update(empj_bldEscrowCompleted);
								}
							}
						}
					}
				}
				break;
			default:
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "没有需要处理的回调");
			}
		} catch (Exception e) {
			e.printStackTrace();
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_BusiError);
		}

		return properties;
	}

	private Log log = LogFactory.getCurrentLogFactory().createLog(ApprovalProcessCallBack_03030102.class);

	/**
	 * 系统推送数据到门户网站
	 * 
	 * @param model
	 * @param eCode
	 * @param qysj
	 */
	public Boolean toInterFaceAction(Empj_BuildingInfo buildingInfo, String eCode, String qysj) {

		// 查询地址
		Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
		baseParameterForm0.setTheState(S_TheState.Normal);
		baseParameterForm0.setTheValue("69004");
		baseParameterForm0.setParametertype("69");
		Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));

		if (null == baseParameter0) {
			log.equals("未查询到配置路径！");

			return false;
		}

		To_building building = new To_building();
		building.setAction("del");
		building.setCate("bld");
		building.setPj_title(buildingInfo.getProject().getTheName());
		building.setTs_pj_id(String.valueOf(buildingInfo.getProject().getTableId()));
		building.setBld_hname(buildingInfo.geteCodeFromConstruction());
		building.setBld_hmane1(
				null == buildingInfo.geteCodeFromPublicSecurity() ? " " : buildingInfo.geteCodeFromPublicSecurity());
		building.setTs_bld_id(eCode);
		building.setBld_mj(Double.toString(null == buildingInfo.getEscrowArea() ? 0.00 : buildingInfo.getEscrowArea()));
		building.setBld_lc(Double.toString(buildingInfo.getUpfloorNumber()));

		String deliveryType = buildingInfo.getDeliveryType();
		if (null != deliveryType && deliveryType.trim().equals("1")) {
			building.setBld_type("0");
		} else if (null != deliveryType && deliveryType.trim().equals("2")) {
			building.setBld_type("1");
		}

		String sql = "select * from Tgxy_EscrowAgreement where theState = 0 and businessProcessState ='7' and tableId=(select A.TGXY_ESCROWAGREEMENT from Rel_EscrowAgreement_Building A where A.EMPJ_BUILDINGINFO="
				+ buildingInfo.getTableId() + ")";

		Tgxy_EscrowAgreement tgxy_EscrowAgreement = sessionFactory.getCurrentSession()
				.createNativeQuery(sql, Tgxy_EscrowAgreement.class).uniqueResult();

		if (null != tgxy_EscrowAgreement) {
			building.setBld_tgtime(tgxy_EscrowAgreement.getContractApplicationDate());
		} else {
			building.setBld_tgtime("");
		}

		building.setBld_endtime(MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));

		// building.setBld_jfbatime(MyDatetime.getInstance().dateToString2(System.currentTimeMillis()));

		Gson gson = new Gson();

		String jsonMap = gson.toJson(building);

		System.out.println(jsonMap);

		String decodeStr = Base64Encoder.encode(jsonMap);

		System.out.println(decodeStr);

		ToInterface toFace = new ToInterface();

		// 记录接口交互信息
		Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
		tgpf_SocketMsg.setTheState(S_TheState.Normal);// 状态：正常
		tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
		tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
		tgpf_SocketMsg.setMsgStatus(1);// 发送状态
		tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());// 发生时间

		tgpf_SocketMsg.setMsgDirection("TGZZ_MHTS");// 报文方向
		tgpf_SocketMsg.setMsgContentArchives(jsonMap);// 报文内容
		tgpf_SocketMsg.setReturnCode("200");// 返回码
		tgpf_SocketMsgDao.save(tgpf_SocketMsg);

		return toFace.interfaceUtil(decodeStr, baseParameter0.getTheName());
	}

}
