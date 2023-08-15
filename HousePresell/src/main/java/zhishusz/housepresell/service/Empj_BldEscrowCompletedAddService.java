package zhishusz.housepresell.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.StrUtil;
import zhishusz.housepresell.controller.form.Empj_BldEscrowCompletedForm;
import zhishusz.housepresell.controller.form.Empj_BldEscrowCompleted_DtlForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.pdf.ExportPdfForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BldEscrowCompletedDao;
import zhishusz.housepresell.database.dao.Empj_BuildingExtendInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted_Dtl;
import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_ButtonType;
import zhishusz.housepresell.database.po.state.S_EscrowState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.pdf.ExportPdfByWordService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;

/*
 * Service添加操作：申请表-项目托管终止（审批）-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BldEscrowCompletedAddService
{
	private static final String BUSI_CODE = "03030102";//具体业务编码参看SVN文
	@Autowired
	private Empj_BldEscrowCompletedDao empj_BldEscrowCompletedDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private Empj_BldEscrowCompleted_DtlAddService bldEscrowCompleted_DtlAddService;
	@Autowired
	private Tgpj_BuildingAccountLogCalculateService calculateService;
	@Autowired
	private Empj_BldAccountGetLimitAmountVerService bldAccountGetLimitAmountVerService;
	@Autowired
	private Empj_BuildingExtendInfoDao empj_buildingExtendInfoDao;
	@Autowired
	private Tgpj_BuildingAccountLimitedUpdateService tgpj_BuildingAccountLimitedUpdateService;
	@Autowired
	private AttachmentJudgeExistUtil attachmentJudgeExistUtil;
	@Autowired
	private ExportPdfByWordService exportPdfByWordService;//生成PDF
	@Autowired
	private Sm_AttachmentDao attacmentDao;
	@Autowired
	private Sm_AttachmentCfgDao attacmentcfgDao;
		
	public Properties execute(Empj_BldEscrowCompletedForm model)
	{
		Properties properties = new MyProperties();
		
		Serializable s_tableId;		

		Integer theState = S_TheState.Normal;
//		String busiState = "未备案"; //model.getBusiState()
		String eCode = sm_BusinessCodeGetService.execute(BUSI_CODE); //自动编号：TGZZ+YY+MM+DD+四位流水号（按年度流水）
		Long createTimeStamp = System.currentTimeMillis();
		Long developCompanyId = model.getDevelopCompanyId();
		Long projectId = model.getProjectId();
		String eCodeFromDRAD = model.geteCodeFromDRAD();
		String remark = model.getRemark();
		
		String hasFormula = model.getHasFormula();
		String webSite = model.getWebSite();
		if(StrUtil.isBlank(hasFormula)){
			return MyBackInfo.fail(properties, "请选择是否已公示！");
		}
		
		if("1".equals(hasFormula) && StrUtil.isBlank(webSite)){
			return MyBackInfo.fail(properties, "请输入公示网址！");
		}


		if(developCompanyId == null || developCompanyId < 1)
		{
			return MyBackInfo.fail(properties, "开发企业不能为空");
		}
		
		if(projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "项目不能为空");
		}		
		if(eCodeFromDRAD == null || eCodeFromDRAD.length() == 0)
		{
			return MyBackInfo.fail(properties, "交付备案批准文件号不能为空");
		}
//		if(remark == null || remark.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "备注不能为空");
//		}
		if (remark != null && remark.length() > 200)
		{
			return MyBackInfo.fail(properties, "备注长度不能超过200字");
		}

		if (model.getEmpj_BldEscrowCompletedAddDtltab() == null) {
			return MyBackInfo.fail(properties, "请选择托管终止楼幢");
		}
		if (model.getEmpj_BldEscrowCompletedAddDtltab().length <= 0) {
			return MyBackInfo.fail(properties, "请选择托管终止楼幢");
		}

		Sm_User userStart = model.getUser();
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "操作人不存在，请重新登录");
		}
		Emmp_CompanyInfo developCompany = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(developCompanyId);
		if(developCompany == null)
		{
			return MyBackInfo.fail(properties, "开发企业不存在");
		}
		Empj_ProjectInfo project = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(projectId);
		if(project == null)
		{
			return MyBackInfo.fail(properties, "项目不存在");
		}
	
		MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);
		if(!msgInfo.isSuccess())
		{
			return MyBackInfo.fail(properties, msgInfo.getInfo());
		}
		
		//托管终止明细表
		List<Empj_BldEscrowCompleted_Dtl> empj_BldEscrowCompleted_Dtls = new ArrayList<Empj_BldEscrowCompleted_Dtl>();
		for (Empj_BldEscrowCompleted_DtlForm empj_BldEscrowCompleted_Dtl : model.getEmpj_BldEscrowCompletedAddDtltab())
		{
			if (empj_BldEscrowCompleted_Dtl == null)
			{
				return MyBackInfo.fail(properties, "托管终止楼幢信息不能为空");				
			}
			
			Properties detailProperties = bldEscrowCompleted_DtlAddService.execute(empj_BldEscrowCompleted_Dtl);
			if (S_NormalFlag.success.equals(detailProperties.getProperty(S_NormalFlag.result)))
			{
				empj_BldEscrowCompleted_Dtls.add((Empj_BldEscrowCompleted_Dtl)detailProperties.get("empj_BldEscrowCompleted_Dtl"));				
			}
			else
			{
				return detailProperties;			
			}
		}

		//托管终止主表
		Empj_BldEscrowCompleted empj_BldEscrowCompleted = new Empj_BldEscrowCompleted();
		empj_BldEscrowCompleted.setTheState(theState);
//		empj_BldEscrowCompleted.setBusiState(busiState);
		empj_BldEscrowCompleted.seteCode(eCode);
		empj_BldEscrowCompleted.setUserStart(userStart);
		empj_BldEscrowCompleted.setCreateTimeStamp(createTimeStamp);
		empj_BldEscrowCompleted.setDevelopCompany(developCompany);		
		empj_BldEscrowCompleted.seteCodeOfDevelopCompany(developCompany.geteCode());
		empj_BldEscrowCompleted.setProject(project);
		empj_BldEscrowCompleted.setTheNameOfProject(project.getTheName());
		empj_BldEscrowCompleted.seteCodeOfProject(project.geteCode());
		empj_BldEscrowCompleted.seteCodeFromDRAD(eCodeFromDRAD);
		empj_BldEscrowCompleted.setRemark(remark);	
		
		empj_BldEscrowCompleted.setHasFormula(hasFormula);
		empj_BldEscrowCompleted.setWebSite(webSite);
		empj_BldEscrowCompleted.setHasPush("0");
		empj_BldEscrowCompleted.setEmpj_BldEscrowCompleted_DtlList(empj_BldEscrowCompleted_Dtls);


		//审批流
		properties = sm_ApprovalProcessGetService.execute(BUSI_CODE, model.getUserId());
		//没有配置审批流程无需走审批流直接保存数据库
		if("noApproval".equals(properties.getProperty(S_NormalFlag.info)))
		{
			empj_BldEscrowCompleted.setUserRecord(userStart);
			empj_BldEscrowCompleted.setRecordTimeStamp(createTimeStamp); //已备案的添加备案人、备案日期
			empj_BldEscrowCompleted.setBusiState(S_BusiState.HaveRecord); //已备案
			empj_BldEscrowCompleted.setApprovalState(S_ApprovalState.Completed); //已完结
			s_tableId=empj_BldEscrowCompletedDao.save(empj_BldEscrowCompleted);
			
			sm_AttachmentBatchAddService.execute(model, empj_BldEscrowCompleted.getTableId());

			/**
			 * TODO
			 * 1、更改楼幢信息的托管状态-> 托管终止
			 * 2、已备案：更新"当前形象进度"为"交付"，"当前受限比例（%）"为0，"当前受限额度（元）"为0，并保存
			 * 3、推送消息给财务部门，需包含终止楼幢、托管余额等新（审批流模块已包含此功能）
			 */
			for (Empj_BldEscrowCompleted_Dtl empjBldEscrowCompletedDtl : empj_BldEscrowCompleted_Dtls)
			{
				Empj_BuildingInfo buildingInfo = empjBldEscrowCompletedDtl.getBuilding();

				//1、更改托管状态
				Empj_BuildingExtendInfo buildingExtendInfo = buildingInfo.getExtendInfo();
				if (buildingExtendInfo != null)
				{
					buildingExtendInfo.setEscrowState(S_EscrowState.EndEscrowState);
					empj_buildingExtendInfoDao.save(buildingExtendInfo);
				}

				//2、保存变化的受限额度、比例等信息
				Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();
				Long bldEscrowCompletedDtlableId = empjBldEscrowCompletedDtl.getTableId();
				Tgpj_BuildingAccountLog tgpj_BuildingAccountLog = changeBuildingAccountAttribute(buildingAccount ,
						bldEscrowCompletedDtlableId);
				if (tgpj_BuildingAccountLog != null)
				{
					Empj_BuildingInfoForm buildingInfoForm = new Empj_BuildingInfoForm();
					buildingInfoForm.setTableId(buildingInfo.getTableId());
					Properties laProperties =
							bldAccountGetLimitAmountVerService.executeZeroLimitAmountNode(buildingInfoForm);
					if (!MyBackInfo.isSuccess(laProperties))
					{
						return MyBackInfo.fail(properties, laProperties.getProperty(S_NormalFlag.info));
					}
					Tgpj_BldLimitAmountVer_AFDtl bldLimitAmountVer_afDtl =
							(Tgpj_BldLimitAmountVer_AFDtl)laProperties.get("limitAmountNode");
					if (bldLimitAmountVer_afDtl == null)
					{
						return MyBackInfo.fail(properties, "该楼幢不存在受限额度版本节点");
					}

					// 修改产生了变更的字段
					tgpj_BuildingAccountLog.setCurrentFigureProgress(bldLimitAmountVer_afDtl.getStageName()); //当前形象进度
					tgpj_BuildingAccountLog.setCurrentLimitedRatio(0.0); //当前受限比例
					tgpj_BuildingAccountLog.setNodeLimitedAmount(bldLimitAmountVer_afDtl.getLimitedAmount()); //当前受限额度
					tgpj_BuildingAccountLog.setBldLimitAmountVerDtl(bldLimitAmountVer_afDtl);

					calculateService.execute(tgpj_BuildingAccountLog);	//重新计算
					tgpj_BuildingAccountLimitedUpdateService.execute(tgpj_BuildingAccountLog);  //保存
				}
			}

		}
		else
		{
			//判断是否满足审批条件（有审批角色，单审批流程）
			if ("fail".equals(properties.getProperty(S_NormalFlag.result)))
			{
				return properties;
			}

			empj_BldEscrowCompleted.setBusiState(S_BusiState.NoRecord); //未备案
			s_tableId=empj_BldEscrowCompletedDao.save(empj_BldEscrowCompleted);
			
			sm_AttachmentBatchAddService.execute(model, empj_BldEscrowCompleted.getTableId());

			//审批操作
			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");
			sm_approvalProcessService.execute(empj_BldEscrowCompleted, model, sm_approvalProcess_cfg);

			/**
			 * TODO
			 * 点击提交按钮 1、更改楼幢信息的托管状态-> 申请托管终止
			 * 2、未备案：更新"当前形象进度"为"交付"，"当前受限比例（%）"为0，"当前受限额度（元）"为0
			 */
			if (S_ButtonType.Submit.equals(model.getButtonType()))
			{
				for (Empj_BldEscrowCompleted_Dtl empjBldEscrowCompletedDtl : empj_BldEscrowCompleted_Dtls)
				{
					Empj_BuildingInfo buildingInfo = empjBldEscrowCompletedDtl.getBuilding();

					//1、更改托管状态
					Empj_BuildingExtendInfo buildingExtendInfo = buildingInfo.getExtendInfo();
					if (buildingExtendInfo != null)
					{
						buildingExtendInfo.setEscrowState(S_EscrowState.ApprovalEscrowState);
						empj_buildingExtendInfoDao.save(buildingExtendInfo);
					}

					//2、保存变化的受限额度、比例等信息
					Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();
					Long bldEscrowCompletedDtlableId = empjBldEscrowCompletedDtl.getTableId();
					Tgpj_BuildingAccountLog tgpj_BuildingAccountLog = changeBuildingAccountAttribute(buildingAccount ,
							bldEscrowCompletedDtlableId);
					if (tgpj_BuildingAccountLog != null)
					{
						Empj_BuildingInfoForm buildingInfoForm = new Empj_BuildingInfoForm();
						buildingInfoForm.setTableId(buildingInfo.getTableId());
						Properties laProperties =
								bldAccountGetLimitAmountVerService.executeZeroLimitAmountNode(buildingInfoForm);
						if (!MyBackInfo.isSuccess(laProperties))
						{
							return MyBackInfo.fail(properties, laProperties.getProperty(S_NormalFlag.info));
						}
						Tgpj_BldLimitAmountVer_AFDtl bldLimitAmountVer_afDtl =
								(Tgpj_BldLimitAmountVer_AFDtl)laProperties.get("limitAmountNode");
						if (bldLimitAmountVer_afDtl == null)
						{
							return MyBackInfo.fail(properties, "该楼幢不存在受限额度版本节点");
						}

						// 修改产生了变更的字段
						tgpj_BuildingAccountLog.setCurrentFigureProgress(bldLimitAmountVer_afDtl.getStageName()); //当前形象进度
						tgpj_BuildingAccountLog.setCurrentLimitedRatio(0.0); //当前受限比例
						tgpj_BuildingAccountLog.setNodeLimitedAmount(bldLimitAmountVer_afDtl.getLimitedAmount()); //当前受限额度
						tgpj_BuildingAccountLog.setBldLimitAmountVerDtl(bldLimitAmountVer_afDtl);

						calculateService.execute(tgpj_BuildingAccountLog);	//重新计算
					}
				}
				
				/*
				 * dcg by time 提交结束后调用生成PDF方法
				 * 并将生成PDF后上传值OSS路径返回给前端
				 * 
				 * 参数：
				 * sourceBusiCode：业务编码
				 * sourceId：单据ID
				 * 
				 * xsz by time 2019-3-18 15:45:10
				 * ·.0
				 * 每次点击提交时，重新生成新的协议pdf
				 */
				Sm_User user = model.getUser();				
				String busiCode = model.getBusiCode();
				Long tableId=new Long(s_tableId.toString());
				
				if(null!=user.getIsSignature()&&"1".equals(user.getIsSignature()))
				{
					
					// 查询是否已经存在PDF附件
					Sm_Attachment attachment = isSaveAttachment(busiCode, String.valueOf(tableId));
					if (null != attachment)
					{
						//如果存在附件，置为删除态重新生成
						attachment.setTheState(S_TheState.Deleted);
						attacmentDao.save(attachment);
						
					}
					
					ExportPdfForm pdfModel = new ExportPdfForm();
					pdfModel.setSourceBusiCode(busiCode);
					pdfModel.setSourceId(String.valueOf(tableId));
					Properties executeProperties = exportPdfByWordService.execute(pdfModel);
					String pdfUrl = (String) executeProperties.get("pdfUrl");
					
					Map<String, String> signatureMap = new HashMap<>();
					
					signatureMap.put("signaturePath", pdfUrl);
					//TODO 此配置后期做成配置
					signatureMap.put("signatureKeyword", "开发企业（盖章）");
					signatureMap.put("ukeyNumber", model.getUser().getUkeyNumber());
					
					properties.put("signatureMap", signatureMap);
					
				}
			}
		}

		properties.put("tableId", empj_BldEscrowCompleted.getTableId());
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	public Tgpj_BuildingAccountLog changeBuildingAccountAttribute(Tgpj_BuildingAccount buildingAccount, Long tableId)
	{
		if (buildingAccount != null)
		{
			//保存楼幢账户log表
			// 不发生修改的字段
			Tgpj_BuildingAccountLog tgpj_BuildingAccountLog = new Tgpj_BuildingAccountLog();
			tgpj_BuildingAccountLog.setTheState(S_TheState.Normal);
			tgpj_BuildingAccountLog.setBusiState("0");
			tgpj_BuildingAccountLog.seteCode(buildingAccount.geteCode());
			tgpj_BuildingAccountLog.setUserStart(buildingAccount.getUserStart());
			tgpj_BuildingAccountLog.setCreateTimeStamp(buildingAccount.getCreateTimeStamp());
			tgpj_BuildingAccountLog.setUserUpdate(buildingAccount.getUserUpdate());
			tgpj_BuildingAccountLog.setLastUpdateTimeStamp(System.currentTimeMillis());
			tgpj_BuildingAccountLog.setUserRecord(buildingAccount.getUserRecord());
			tgpj_BuildingAccountLog.setRecordTimeStamp(buildingAccount.getRecordTimeStamp());
			tgpj_BuildingAccountLog.setDevelopCompany(buildingAccount.getDevelopCompany());
			tgpj_BuildingAccountLog.seteCodeOfDevelopCompany(buildingAccount.geteCodeOfDevelopCompany());
			tgpj_BuildingAccountLog.setProject(buildingAccount.getProject());
			tgpj_BuildingAccountLog.setTheNameOfProject(buildingAccount.getTheNameOfProject());
			tgpj_BuildingAccountLog.setBuilding(buildingAccount.getBuilding());
			tgpj_BuildingAccountLog.setPayment(buildingAccount.getPayment());
			tgpj_BuildingAccountLog.seteCodeOfBuilding(buildingAccount.geteCodeOfBuilding());
			tgpj_BuildingAccountLog.setEscrowStandard(buildingAccount.getEscrowStandard());
			tgpj_BuildingAccountLog.setEscrowArea(buildingAccount.getEscrowArea());
			tgpj_BuildingAccountLog.setBuildingArea(buildingAccount.getBuildingArea());
			tgpj_BuildingAccountLog.setOrgLimitedAmount(buildingAccount.getOrgLimitedAmount());
			tgpj_BuildingAccountLog.setTotalGuaranteeAmount(buildingAccount.getTotalGuaranteeAmount());
			tgpj_BuildingAccountLog.setCashLimitedAmount(buildingAccount.getCashLimitedAmount());
			tgpj_BuildingAccountLog.setTotalAccountAmount(buildingAccount.getTotalAccountAmount());
			tgpj_BuildingAccountLog.setPayoutAmount(buildingAccount.getPayoutAmount());
			tgpj_BuildingAccountLog.setAppliedNoPayoutAmount(buildingAccount.getAppliedNoPayoutAmount());
			tgpj_BuildingAccountLog.setApplyRefundPayoutAmount(buildingAccount.getApplyRefundPayoutAmount());
			tgpj_BuildingAccountLog.setRefundAmount(buildingAccount.getRefundAmount());
			tgpj_BuildingAccountLog.setCurrentEscrowFund(buildingAccount.getCurrentEscrowFund());
			tgpj_BuildingAccountLog.setAppropriateFrozenAmount(buildingAccount.getAppropriateFrozenAmount());
			tgpj_BuildingAccountLog.setRecordAvgPriceOfBuildingFromPresellSystem(buildingAccount.getRecordAvgPriceOfBuildingFromPresellSystem());

			tgpj_BuildingAccountLog.setLogId(buildingAccount.getLogId());
			tgpj_BuildingAccountLog.setActualAmount(buildingAccount.getActualAmount());
			tgpj_BuildingAccountLog.setPaymentLines(buildingAccount.getPaymentLines());
			tgpj_BuildingAccountLog.setRelatedBusiCode(BUSI_CODE);
			tgpj_BuildingAccountLog.setRelatedBusiTableId(tableId);
			tgpj_BuildingAccountLog.setTgpj_BuildingAccount(buildingAccount);
			tgpj_BuildingAccountLog.setVersionNo(buildingAccount.getVersionNo());
			tgpj_BuildingAccountLog.setPaymentProportion(buildingAccount.getPaymentProportion());
			tgpj_BuildingAccountLog.setBuildAmountPaid(buildingAccount.getBuildAmountPaid());
			tgpj_BuildingAccountLog.setBuildAmountPay(buildingAccount.getBuildAmountPay());
			tgpj_BuildingAccountLog.setTotalAmountGuaranteed(buildingAccount.getTotalAmountGuaranteed());
			tgpj_BuildingAccountLog.setCashLimitedAmount(buildingAccount.getCashLimitedAmount());
			tgpj_BuildingAccountLog.setEffectiveLimitedAmount(buildingAccount.getEffectiveLimitedAmount());
			tgpj_BuildingAccountLog.setSpilloverAmount(buildingAccount.getSpilloverAmount());
			tgpj_BuildingAccountLog.setAllocableAmount(buildingAccount.getAllocableAmount());
			tgpj_BuildingAccountLog.setRecordAvgPriceOfBuilding(buildingAccount.getRecordAvgPriceOfBuilding());

			// 修改产生了变更的字段
			tgpj_BuildingAccountLog.setCurrentFigureProgress("交付");   //当前形象进度
			tgpj_BuildingAccountLog.setCurrentLimitedRatio(0.0);	   //当前受限比例
			tgpj_BuildingAccountLog.setNodeLimitedAmount(0.0);		   //当前受限额度

			return tgpj_BuildingAccountLog;
		}
		return null;
	}
	
	/**
	 * 是否存在PDF
	 * 
	 * @param sourceBusiCode
	 *            业务编码
	 * @param sourceId
	 *            业务数据Id
	 * @return
	 */
	Sm_Attachment isSaveAttachment(String sourceBusiCode, String sourceId)
	{
		//合作协议打印编码
		String attacmentcfg = "240104";
		
		Sm_AttachmentCfg sm_AttachmentCfg = isSaveAttachmentCfg(attacmentcfg);
		
		if (null == sm_AttachmentCfg)
		{
			return null;
		}
		
		Sm_AttachmentForm form = new Sm_AttachmentForm();
		form.setSourceId(sourceId);
		form.setBusiType(sourceBusiCode);
		form.setSourceType(sm_AttachmentCfg.geteCode());
		form.setTheState(S_TheState.Normal);

		Sm_Attachment attachment = attacmentDao
				.findOneByQuery_T(attacmentDao.getQuery(attacmentDao.getBasicHQL(), form));

		if (null == attachment)
		{
			return null;
		}
		return attachment;
	}
	

	/**
	 * 是否进行档案配置
	 * 
	 * @param attacmentcfg
	 *            档案类型编码
	 * @return
	 */
	Sm_AttachmentCfg isSaveAttachmentCfg(String attacmentcfg)
	{
		// 根据业务编号查询配置文件
		Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
		form.setTheState(S_TheState.Normal);
		form.setBusiType(attacmentcfg);

		Sm_AttachmentCfg sm_AttachmentCfg = attacmentcfgDao
				.findOneByQuery_T(attacmentcfgDao.getQuery(attacmentcfgDao.getBasicHQL(), form));

		if (null == sm_AttachmentCfg)
		{
			return null;
		}
		return sm_AttachmentCfg;
	}
	
}


