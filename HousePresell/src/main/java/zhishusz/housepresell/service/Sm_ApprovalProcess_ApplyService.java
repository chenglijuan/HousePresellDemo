package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaominfo.oss.sdk.ReceiveMessage;

import cn.hutool.core.util.StrUtil;
import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeChildForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BldEscrowCompletedDao;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmountDao;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_DtlDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeChildDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgForcast_AFDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgForcast_ManageDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tg_DepositManagementDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_FundOverallPlanDao;
import zhishusz.housepresell.database.dao.Tgpf_RefundInfoDao;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.dao.Tgxy_EscrowAgreementDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted_Dtl;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Empj_PaymentGuaranteeChild;
import zhishusz.housepresell.database.po.Empj_ProjProgForcast_AF;
import zhishusz.housepresell.database.po.Empj_ProjProgForcast_Manage;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Condition;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Node;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Record;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_WorkflowCondition;
import zhishusz.housepresell.database.po.Sm_MessageTemplate_Cfg;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_DepositManagement;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlan;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_ButtonType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.exception.RoolBackException;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;
import zhishusz.housepresell.util.fileupload.OssServerUtil;

/**
 * 审批流程 --- 添加：申请按钮
 *
 * @author Glad.Wang
 *
 */
@Service
@Transactional
public class Sm_ApprovalProcess_ApplyService {
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;

	@Autowired
	private Sm_UserDao sm_UserDao;

	@Autowired
	private OssServerUtil ossServerUtil;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Sm_ApprovalProcess_MessagePushletService messagePushletService;
	@Autowired
	private Sm_BusinessRecordCommAddService sm_businessRecordCommAddService;

	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;// 开发企业
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;// 项目
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;// 楼幢信息
	@Autowired
	private Tgxy_EscrowAgreementDao tgxy_EscrowAgreementDao;// 合作协议
	@Autowired
	private Tgpj_BuildingAvgPriceDao tgpj_BuildingAvgPriceDao;// 备案价格
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;// 三方协议
	@Autowired
	private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;// 受限额度变更
	@Autowired
	private Empj_BldEscrowCompletedDao empj_BldEscrowCompletedDao;// 托管终止
	@Autowired
	private Empj_PaymentGuaranteeDao empj_PaymentGuaranteeDao;// 支付保证
	@Autowired
	private Tg_DepositManagementDao tg_DepositManagementDao;// 存单
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;// 用款申请
	@Autowired
	private Tgpf_FundOverallPlanDao tgpf_FundOverallPlanDao;// 统筹
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDao tgpf_SpecialFundAppropriated_AFDao;// 特殊拨付
	@Autowired
	private Tgpf_RefundInfoDao tgpf_RefundInfoDao;// 退房退款
	@Autowired
	private Empj_PaymentGuaranteeChildDao empj_PaymentGuaranteeChildDao;// 支付保证子表
	@Autowired
	private Empj_BldLimitAmountDao empj_BldLimitAmountDao;
	@Autowired
	private Empj_BldLimitAmount_DtlDao empj_BldLimitAmount_DtlDao;
	@Autowired
	private Empj_ProjProgForcast_AFDao empj_ProjProgForcast_AFDao;
	@Autowired
	private Empj_ProjProgForcast_ManageDao manageDao;

	/**
	 *
	 * @param modelPo
	 *            审批对象
	 * @param orgObjJson
	 *            原数据
	 * @param expectObjJson
	 *            修改后数据
	 * @param userStartId
	 *            登录用户ID
	 * @param buttonType
	 *            按钮来源
	 * @param sm_approvalProcess_cfg
	 *            审批对象与登录用户匹配成功的审批流程
	 * @return
	 */
	public Properties execute(IApprovable modelPo, String orgObjJson, String expectObjJson, Long userStartId,
			String buttonType, Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg) {
		Properties properties = new MyProperties();

		System.out.println("Sm_ApprovalProcess_ApplyService START");

		String busiCode_01030203 = S_BusiCode.busiCode_01030203; // 我发起的业务编码
		Sm_User loginUser = sm_UserDao.findById(userStartId);
		String loginUserName = loginUser.getTheName();// 申请人
		Emmp_CompanyInfo emmp_companyInfo = loginUser.getCompany(); // 登录用户所属机构
		String theNameOfCompanyInfo = emmp_companyInfo.getTheName();// 机构名称
		String busiCode = sm_approvalProcess_cfg.getBusiCode();
		String busiType = sm_approvalProcess_cfg.getBusiType();

		// 修改前数据文件路径
		String orgObjJsonFilePath = null;
		// 修改后数据文件路径
		String expectObjJsonFilePath = null;
		
		if(!"03030206".equals(busiCode)){
			if (orgObjJson != null && orgObjJson.length() > 100) {
				ReceiveMessage uploadOrgObjJson = ossServerUtil.stringUpload(orgObjJson);// 上传可变更的旧数据
				if (uploadOrgObjJson.getData() != null) {
					if (uploadOrgObjJson.getData().get(0).getUrl() != null) {
						orgObjJsonFilePath = uploadOrgObjJson.getData().get(0).getUrl();// 获取文件路径
					}
				}
			}

			System.out.println("orgObjJsonFilePath = " + orgObjJsonFilePath);

			if (expectObjJson != null && expectObjJson.length() > 100) {
				ReceiveMessage uploadExpectObjJson = ossServerUtil.stringUpload(expectObjJson);// 上传可变更的新数据数据
				if (uploadExpectObjJson.getData() != null) {
					if (uploadExpectObjJson.getData().get(0).getUrl() != null) {
						expectObjJsonFilePath = uploadExpectObjJson.getData().get(0).getUrl();// 获取文件路径
					}
				}
			}

			System.out.println("expectObjJsonFilePath = " + expectObjJsonFilePath);	
		}

		// 1.通过配置信息(cfg)得到结点
		List<Sm_ApprovalProcess_Node> nodeList = sm_approvalProcess_cfg.getNodeList();// 将要走的审批流程的节点列表

		Sm_Permission_Role cfgRole = nodeList.get(0).getRole();// 第一个节点（发起人）的角色

		// 2.通过结点创建审批流程
		List<Sm_ApprovalProcess_Workflow> workflowList = new ArrayList<>();
		for (int i = 0; i < nodeList.size(); i++) {
			Sm_ApprovalProcess_Node sm_approvalProcess_node = nodeList.get(i);
			Sm_ApprovalProcess_Workflow sm_ApprovalProcess_Workflow = new Sm_ApprovalProcess_Workflow(
					sm_approvalProcess_node);
			sm_approvalProcess_workflowDao.save(sm_ApprovalProcess_Workflow);

			// --------------------------------------结点
			// ====》消息模板start---------------------------------------------//
			if (sm_approvalProcess_node.getSm_messageTemplate_cfgList() != null
					&& sm_approvalProcess_node.getSm_messageTemplate_cfgList().size() > 0) {
				List<Sm_MessageTemplate_Cfg> sm_messageTemplate_cfgs = new ArrayList<>();
				sm_messageTemplate_cfgs.addAll(sm_approvalProcess_node.getSm_messageTemplate_cfgList());
				sm_ApprovalProcess_Workflow.setSm_messageTemplate_cfgList(sm_messageTemplate_cfgs);
			}
			// --------------------------------------结点
			// ====》消息模板end---------------------------------------------//
			workflowList.add(sm_ApprovalProcess_Workflow);

			// --------------------------------------结点与结点之间关联start--------------------------------------------//
			if (i > 0) {
				Sm_ApprovalProcess_Workflow preWorkflow = workflowList.get(i - 1);
				preWorkflow.setNextWorkFlow(sm_ApprovalProcess_Workflow);
				sm_ApprovalProcess_Workflow.setLastWorkFlow(preWorkflow);
			}
			// --------------------------------------结点与结点之间关联end---------------------------------------------//
		}
		// -------------------------------------------结点
		// ====》条件start---------------------------------------------//
		for (int nodeIndex = 0; nodeIndex < nodeList.size(); nodeIndex++) {
			Sm_ApprovalProcess_Node sm_approvalProcess_node = nodeList.get(nodeIndex);
			Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = workflowList.get(nodeIndex);
			List<Sm_ApprovalProcess_WorkflowCondition> workflowConditionList = new ArrayList<>();
			if (sm_approvalProcess_node.getApprovalProcess_conditionList() != null
					&& sm_approvalProcess_node.getApprovalProcess_conditionList().size() > 0) {
				for (Sm_ApprovalProcess_Condition sm_approvalProcess_condition : sm_approvalProcess_node
						.getApprovalProcess_conditionList()) {
					Long getNextStepWorkflowId = getNextStepWorkflowId(sm_approvalProcess_condition, nodeList,
							workflowList);

					Sm_ApprovalProcess_WorkflowCondition workflowCondition = new Sm_ApprovalProcess_WorkflowCondition();
					workflowCondition.setTheState(S_TheState.Normal);
					workflowCondition.setTheContent(sm_approvalProcess_condition.getTheContent());
					workflowCondition.setNextStep(getNextStepWorkflowId);
					workflowCondition.setUserStart(loginUser);
					workflowCondition.setCreateTimeStamp(System.currentTimeMillis());
					workflowConditionList.add(workflowCondition);
				}
				sm_approvalProcess_workflow.setWorkflowConditionList(workflowConditionList);
			}
		}
		// -------------------------------------------------结点====》条件end------------------------------------------//

		
		// 提交按钮 设置第一个结点状态为通过
		if (S_ButtonType.Submit.equals(buttonType)) {
			System.out.println("S_ButtonType.Submit.equals(buttonType)");
			workflowList.get(0).setBusiState(S_WorkflowBusiState.Pass);// 发起结点

			if (workflowList.size() > 1) {
				workflowList.get(1).setBusiState(S_WorkflowBusiState.Examining);
			}
		}

		// 3.创建申请单
		System.out.println("3.创建申请单");
		Sm_ApprovalProcess_AF af = new Sm_ApprovalProcess_AF();
		af.setApplicant(loginUserName); // 申请人
		af.setTheNameOfCompanyInfo(theNameOfCompanyInfo); // 申请机构
		af.setConfiguration(sm_approvalProcess_cfg); // 流程配置
		af.setIsNeedBackup(sm_approvalProcess_cfg.getIsNeedBackup());// 是否备案
		af.setCompanyInfo(emmp_companyInfo); // 归属企业
		af.seteCode(sm_BusinessCodeGetService.execute(busiCode_01030203)); // 业务编码
		af.setTheState(S_TheState.Normal);
		af.setPermissionRole(cfgRole);
		af.setBusiCode(busiCode);// 业务编码
		af.setBusiType(busiType);// 业务类型

		/*
		 * xsz by time 2019-3-27 15:40:05 根据业务类型添加不同的主题
		 */
		System.out.println("根据业务类型添加不同的主题");
		Long sourceId = modelPo.getSourceId();// 单据主键
		switch (busiCode) {
		case "020101":
			// 开发企业注册
			Emmp_CompanyInfo companyInfo = emmp_CompanyInfoDao.findById(sourceId);
			if (null != companyInfo) {
				af.setTheme("企业名称：" + companyInfo.getTheName());// 企业名称：XXX
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}
			break;

		case "020102":
			// 开发企业变更
			Emmp_CompanyInfo companyInfo1 = emmp_CompanyInfoDao.findById(sourceId);
			if (null != companyInfo1) {
				af.setTheme("企业名称：" + companyInfo1.getTheName());// 企业名称：XXX
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}
			break;

		case "03010101":
			// 项目新增
			Empj_ProjectInfo projectInfo = empj_ProjectInfoDao.findById(sourceId);
			if (null != projectInfo) {
				af.setTheme("项目名称：" + projectInfo.getTheName());// 项目名称：XXX
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}
			break;

		case "03010102":
			// 项目变更
			Empj_ProjectInfo projectInfo1 = empj_ProjectInfoDao.findById(sourceId);
			if (null != projectInfo1) {
				af.setTheme("项目名称：" + projectInfo1.getTheName());// 项目名称：XXX
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}
			break;

		case "03010201":
			// 楼幢新增
			Empj_BuildingInfo buildingInfo = empj_BuildingInfoDao.findById(sourceId);
			if (null != buildingInfo) {
				af.setTheme(buildingInfo.getProject().getCityRegion().getTheName() + " 项目名称："
						+ buildingInfo.getProject().getTheName() + " 施工编号：" + buildingInfo.geteCodeFromConstruction());// 项目名称：XXX
																														// 施工编号：XXX幢
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}
			break;

		case "03010202":
			// 楼幢变更
			Empj_BuildingInfo buildingInfo1 = empj_BuildingInfoDao.findById(sourceId);
			if (null != buildingInfo1) {
				af.setTheme(buildingInfo1.getProject().getCityRegion().getTheName() + " 项目名称："
						+ buildingInfo1.getProject().getTheName() + " 施工编号："
						+ buildingInfo1.geteCodeFromConstruction());// 项目名称：XXX
																	// 施工编号：XXX幢
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}
			break;

		case "06110201":
			// 合作协议签署
			Tgxy_EscrowAgreement escrowAgreement = tgxy_EscrowAgreementDao.findById(sourceId);
			if (null != escrowAgreement) {
				af.setTheme(escrowAgreement.getProject().getCityRegion().getTheName() + " 项目名称："
						+ escrowAgreement.getProject().getTheName() + " 施工编号："
						+ escrowAgreement.getBuildingInfoCodeList());// 项目名称：XXX
																		// 施工编号：XXX幢
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}
			break;

		case "03010301":
			// 物价备案均价
			Tgpj_BuildingAvgPrice buildingAvgPrice = tgpj_BuildingAvgPriceDao.findById(sourceId);
			if (null != buildingAvgPrice) {
				af.setTheme(buildingAvgPrice.getBuildingInfo().getProject().getCityRegion().getTheName() + "项目名称："
						+ buildingAvgPrice.getBuildingInfo().getProject().getTheName() + " 施工编号："
						+ buildingAvgPrice.getBuildingInfo().geteCodeFromConstruction() + " 备案价格均价："
						+ buildingAvgPrice.getRecordAveragePrice() + "元");// 项目名称：XXX
																			// 施工编号：XXX幢
																			// 备案价格均价:XXX元
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}
			break;

		case "06110301":
			// 三方协议签署
			Tgxy_TripleAgreement tripleAgreement = tgxy_TripleAgreementDao.findById(sourceId);
			if (null != tripleAgreement) {
				af.setTheme(tripleAgreement.getProject().getCityRegion().getTheName() + " 坐落："
						+ tripleAgreement.getProject().getTheName() + " "
						+ tripleAgreement.getBuildingInfo().geteCodeFromConstruction() + " "
						+ tripleAgreement.getUnitRoom() + "户 协议编号：" + tripleAgreement.geteCodeOfTripleAgreement()
						+ " 买受人：" + tripleAgreement.getBuyerName());// 坐落：XXX项目XXX幢XXX户
																	// 协议编号：XXX
																	// 买受人：XXX
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}
			break;

		case "03030101":
			// 进度节点更新
			Empj_BldLimitAmount_AF bldLimitAmount_AF = empj_BldLimitAmount_AFDao.findById(sourceId);
			if (null != bldLimitAmount_AF) {
				Tgpj_BldLimitAmountVer_AFDtl expectFigureProgress = bldLimitAmount_AF.getExpectFigureProgress();
				af.setTheme(bldLimitAmount_AF.getProject().getCityRegion().getTheName() + " 项目名称："
						+ bldLimitAmount_AF.getProject().getTheName() + " 施工编号："
						+ bldLimitAmount_AF.getBuilding().geteCodeFromConstruction() + " 拟变更节点："
						+ expectFigureProgress.getStageName());// 项目名称：XXX
																// 施工编号：XXX 幢
																// 拟变更节点：XXX
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}
			break;

		case "03030102":
			// 项目交付备案
			Empj_BldEscrowCompleted bldEscrowCompleted = empj_BldEscrowCompletedDao.findById(sourceId);
			if (null != bldEscrowCompleted) {
				String sgbh = "";
				List<Empj_BldEscrowCompleted_Dtl> empj_BldEscrowCompleted_DtlList = bldEscrowCompleted
						.getEmpj_BldEscrowCompleted_DtlList();
				if (null != empj_BldEscrowCompleted_DtlList && empj_BldEscrowCompleted_DtlList.size() > 0) {
					for (int i = 0; i < empj_BldEscrowCompleted_DtlList.size(); i++) {
						if (i == 0) {
							sgbh = empj_BldEscrowCompleted_DtlList.get(i).geteCodeFromConstruction();
						} else {
							sgbh = sgbh + "、" + empj_BldEscrowCompleted_DtlList.get(i).geteCodeFromConstruction();
						}
					}
				}

				af.setTheme(bldEscrowCompleted.getProject().getCityRegion().getTheName() + " 项目名称："
						+ bldEscrowCompleted.getProject().getTheName() + " 施工编号：" + sgbh);// 项目名称：XXX
																							// 施工编号：XXX幢、XXX幢
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}
			break;

		case "06120401":
			// 支付保证申请
			Empj_PaymentGuarantee paymentGuarantee = empj_PaymentGuaranteeDao.findById(sourceId);
			if (null != paymentGuarantee) {
				Empj_PaymentGuaranteeChildForm childForm = new Empj_PaymentGuaranteeChildForm();
				childForm.setTheState(S_TheState.Normal);
				childForm.setEmpj_PaymentGuarantee(paymentGuarantee);
				List<Empj_PaymentGuaranteeChild> listChild;
				listChild = empj_PaymentGuaranteeChildDao.findByPage(
						empj_PaymentGuaranteeChildDao.getQuery(empj_PaymentGuaranteeChildDao.getBasicHQL(), childForm));

				StringBuffer sb = new StringBuffer();
				if (null == listChild && listChild.size() > 0) {
					for (int i = 0; i < listChild.size(); i++) {
						if (i == 0) {
							sb.append(null == listChild.get(i).geteCodeFromConstruction() ? ""
									: listChild.get(i).geteCodeFromConstruction());
						}
						sb.append(null == listChild.get(i).geteCodeFromConstruction() ? ""
								: listChild.get(i).geteCodeFromConstruction());

					}
				}

				af.setTheme(paymentGuarantee.getProject().getCityRegion().getTheName() + " 项目名称："
						+ paymentGuarantee.getProject().getTheName() + " 施工编号：" + sb.toString());// 项目名称：XXX
																									// 施工编号：XXX幢、XXX幢
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}

			break;

		case "06120403":
			// 支付保证撤销
			Empj_PaymentGuarantee paymentGuarantee1 = empj_PaymentGuaranteeDao.findById(sourceId);
			if (null != paymentGuarantee1) {
				af.setTheme(paymentGuarantee1.getProject().getCityRegion().getTheName() + " 项目名称："
						+ paymentGuarantee1.getProject().getTheName() + " 施工编号："
						+ paymentGuarantee1.geteCodeFromConstruction());// 项目名称：XXX
																		// 施工编号：XXX幢、XXX幢
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}

			break;

		case "210104":
			// 存单存入
			Tg_DepositManagement tg_DepositManagement = tg_DepositManagementDao.findById(sourceId);

			if (null != tg_DepositManagement && null != tg_DepositManagement.getBank()) {

				String depositProperty = tg_DepositManagement.getDepositProperty();// 存款性质
				if (null == depositProperty) {
					depositProperty = "大额存单";
				} else if ("01".equals(depositProperty)) {
					depositProperty = "大额存单";
				} else if ("02".equals(depositProperty)) {
					depositProperty = "结构性存款";
				} else if ("03".equals(depositProperty)) {
					depositProperty = "保本理财";
				} else {
					depositProperty = "大额存单";
				}

				Double principalAmount = tg_DepositManagement.getPrincipalAmount();// 本金金额

				MyDouble.getInstance();
				af.setTheme(tg_DepositManagement.getBank().getTheName() + " " + depositProperty + " 存入"
						+ MyDouble.pointTOThousandths(principalAmount) + "元");// xx银行
																				// 结构性存款/大额存单/理财
																				// 存入xx元
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}
			break;

		case "210105":
			// 存单提取
			Tg_DepositManagement tg_DepositManagement1 = tg_DepositManagementDao.findById(sourceId);

			if (null != tg_DepositManagement1 && null != tg_DepositManagement1.getBank()) {

				String depositProperty = tg_DepositManagement1.getDepositProperty();// 存款性质
				if (null == depositProperty) {
					depositProperty = "大额存单";
				} else if ("01".equals(depositProperty)) {
					depositProperty = "大额存单";
				} else if ("02".equals(depositProperty)) {
					depositProperty = "结构性存款";
				} else if ("03".equals(depositProperty)) {
					depositProperty = "保本理财";
				} else {
					depositProperty = "大额存单";
				}

				Double principalAmount = tg_DepositManagement1.getPrincipalAmount();// 本金金额

				MyDouble.getInstance();
				af.setTheme(tg_DepositManagement1.getBank().getTheName() + " " + depositProperty + " 提取"
						+ MyDouble.pointTOThousandths(principalAmount) + "元");// xx银行
																				// 结构性存款/大额存单/理财
																				// 存入xx元
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}
			break;

		case "06120301":
			// 用款申请
			Tgpf_FundAppropriated_AF fundAppropriated_AF = tgpf_FundAppropriated_AFDao.findById(sourceId);
			if (null != fundAppropriated_AF && null != fundAppropriated_AF.getProject()) {
				af.setTheme("用款申请与复核：" + fundAppropriated_AF.getProject().getTheName() + " 金额："
						+ MyDouble.pointTOThousandths(fundAppropriated_AF.getTotalApplyAmount()) + "元");// 用款申请与复核：xx项目
																										// 金额：xx元
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}
			break;

		case "06120302":
			// 统筹
			Tgpf_FundOverallPlan tgpf_FundOverallPlan = tgpf_FundOverallPlanDao.findById(sourceId);
			if (null != tgpf_FundOverallPlan && null != tgpf_FundOverallPlan.getFundAppropriated_AFList()) {
				List<Tgpf_FundAppropriated_AF> fundAppropriated_AFList = tgpf_FundOverallPlan
						.getFundAppropriated_AFList();// 用款申请
				StringBuffer sf = new StringBuffer();
				for (int i = 0; i < fundAppropriated_AFList.size(); i++) {
					if (i == 0) {
						sf.append("项目：" + fundAppropriated_AFList.get(i).getProject().getTheName() + " 金额："
								+ MyDouble.pointTOThousandths(fundAppropriated_AFList.get(i).getTotalApplyAmount())
								+ "元");
					} else {
						sf.append("、项目" + fundAppropriated_AFList.get(i).getProject().getTheName() + " 金额："
								+ MyDouble.pointTOThousandths(fundAppropriated_AFList.get(i).getTotalApplyAmount())
								+ "元");
					}
				}

				if ("1".equals(tgpf_FundOverallPlan.getApplyType())) {
					af.setTheme("保函统筹：" + sf.toString());
				} else {
					af.setTheme("统筹：" + sf.toString());
				}

				// 默认主题 机构名称 + 业务类型//统筹：xx项目
				// 金额：xx元、xx项目 金额：xx元等
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}
			break;

		case "061206":
			// 特殊拨付
			Tgpf_SpecialFundAppropriated_AF appropriated_AF = tgpf_SpecialFundAppropriated_AFDao.findById(sourceId);
			if (null != appropriated_AF && null != appropriated_AF.getDevelopCompany()
					&& null != appropriated_AF.getProject() && null != appropriated_AF.getBuilding()) {
				af.setTheme("特殊拨付：开发企业：" + appropriated_AF.getDevelopCompany().getTheName() + " 项目："
						+ appropriated_AF.getProject().getTheName() + " 楼幢："
						+ appropriated_AF.getBuilding().geteCodeFromConstruction());// 特殊拨付：xx开发企业xx项目xx楼幢xx单元xx室xx客户
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}

			break;

		case "06120201":
			// 退房退款（未结清）
			Tgpf_RefundInfo tgpf_RefundInfo = tgpf_RefundInfoDao.findById(sourceId);
			if (null != tgpf_RefundInfo && null != tgpf_RefundInfo.getProject()
					&& null != tgpf_RefundInfo.getBuilding()) {
				af.setTheme("退房退款：开发企业：" + tgpf_RefundInfo.getProject().getDevelopCompany().getTheName() + " 项目："
						+ tgpf_RefundInfo.getProject().getTheName() + " 楼幢："
						+ tgpf_RefundInfo.getBuilding().geteCodeFromConstruction() + " 室号："
						+ tgpf_RefundInfo.getTripleAgreement().getUnitRoom() + " 客户："
						+ tgpf_RefundInfo.getTheNameOfBuyer());// 退房退款：xx开发企业xx项目xx楼幢xx单元xx室xx客户
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}
			break;

		case "06120202":
			// 退房退款（已结清）
			Tgpf_RefundInfo tgpf_RefundInfo1 = tgpf_RefundInfoDao.findById(sourceId);
			if (null != tgpf_RefundInfo1 && null != tgpf_RefundInfo1.getProject()
					&& null != tgpf_RefundInfo1.getBuilding()) {
				af.setTheme("退房退款：开发企业：" + tgpf_RefundInfo1.getProject().getDevelopCompany().getTheName() + " 项目："
						+ tgpf_RefundInfo1.getProject().getTheName() + " 楼幢："
						+ tgpf_RefundInfo1.getBuilding().geteCodeFromConstruction() + " 室号："
						+ tgpf_RefundInfo1.getTripleAgreement().getUnitRoom() + " 客户："
						+ tgpf_RefundInfo1.getTheNameOfBuyer());// 退房退款：xx开发企业xx项目xx楼幢xx单元xx室xx客户
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
																	// + 业务类型
			}
			break;

		case "03030100":

			Empj_BldLimitAmount bldLimitAmount = empj_BldLimitAmountDao.findById(sourceId);
			if (null != bldLimitAmount) {

				af.setTheme(bldLimitAmount.getProject().getCityRegion().getTheName() + " 项目名称："
						+ bldLimitAmount.getProject().getTheName() + "批量申请进度节点更新");

			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
			}

			break;

		case "03030202":

			Empj_ProjProgForcast_AF progForcast_AF = empj_ProjProgForcast_AFDao.findById(sourceId);

			if (null != progForcast_AF) {
				af.setTheme(progForcast_AF.getAreaName() + " 项目名称：" + progForcast_AF.getProjectName() + "工程进度巡查申请");
			} else {
				af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称
			}

			break;

		case "03030206":
			// 工程进度巡查人工审核
			Empj_ProjProgForcast_Manage manage = manageDao.findById(sourceId);
			if (StrUtil.isBlank(manage.getBuildCode())) {
				af.setTheme("工程进度巡查业务部门复核：" + manage.getAreaName() + " " + manage.getProjectName());
			} else {
				af.setTheme("工程进度巡查业务部门复核：" + manage.getAreaName() + " " + manage.getProjectName() + " "
						+ manage.getBuildCode());
			}

			break;

		case "06120501":
			af.setTheme(theNameOfCompanyInfo +sm_approvalProcess_cfg.getKeyword()+" " + busiType);// 默认主题 机构名称 +
			// 业务类型

			break;

		default:

			af.setTheme(theNameOfCompanyInfo + " " + busiType);// 默认主题 机构名称 +
																// 业务类型

			break;
		}

		// 根据按钮类型 设置流程状态 1 保存按钮 ： 待提交 ； 2 提交按钮 ： 审核中
		
		System.out.println("根据按钮类型 设置流程状态 1 保存按钮 ： 待提交 ； 2 提交按钮 ： 审核中");
		if (S_ButtonType.Save.equals(buttonType)) {
			af.setBusiState(S_ApprovalState.WaitSubmit); // 流程状态 ：待提交

		} else if (S_ButtonType.Submit.equals(buttonType)) {
			af.setBusiState(S_ApprovalState.Examining); // 流程状态 ：审核中
		}
		af.setSourceId(modelPo.getSourceId());
		af.setSourceType(modelPo.getSourceType());
		af.setOrgObjJsonFilePath(orgObjJsonFilePath);
		af.setExpectObjJsonFilePath(expectObjJsonFilePath);
		af.setCurrentIndex(workflowList.get(1).getTableId());
		af.setWorkFlowList(workflowList);
		af.setUserStart(loginUser);
		af.setStartTimeStamp(System.currentTimeMillis());
		af.setCreateTimeStamp(System.currentTimeMillis());
		sm_ApprovalProcess_AFDao.save(af);

		// 第一个结点
		Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = workflowList.get(0);

		// --------------------------------------------------------生成发起记录start-----------------------------------//
		List recordList = new ArrayList();
		Sm_ApprovalProcess_Record sm_ApprovalProcess_Record = new Sm_ApprovalProcess_Record();
		sm_ApprovalProcess_Record.setTheState(S_TheState.Normal);
		sm_ApprovalProcess_Record.setConfiguration(sm_approvalProcess_cfg); // 流程配置
		sm_ApprovalProcess_Record.setCreateTimeStamp(System.currentTimeMillis());
		sm_ApprovalProcess_Record.setLastUpdateTimeStamp(System.currentTimeMillis());
		sm_ApprovalProcess_Record.setUserStart(loginUser);
		sm_ApprovalProcess_Record.setUserUpdate(loginUser);
		sm_ApprovalProcess_Record.setUserOperate(loginUser); // 发起人
		sm_ApprovalProcess_Record.setOperateTimeStamp(System.currentTimeMillis()); // 操作时间点

		recordList.add(sm_ApprovalProcess_Record);
		sm_approvalProcess_workflow.setApprovalProcess_recordList(recordList);
		sm_approvalProcess_workflowDao.save(sm_approvalProcess_workflow);
		// --------------------------------------------------------生成发起记录end-------------------------------------//

		// -------------------------------------------------------业务关联记录表start----------------------------------//
		BaseForm baseForm = new BaseForm();
		baseForm.setUserId(userStartId);
		baseForm.setUser(loginUser);
		
		System.out.println("sm_businessRecordCommAddService.addBusinessRecord START");
		
		properties = sm_businessRecordCommAddService.addBusinessRecord(busiCode, modelPo.getEcodeOfBusiness(),
				af.getTableId(), baseForm);
		
		System.out.println("sm_businessRecordCommAddService.addBusinessRecord END");
		if (!MyBackInfo.isSuccess(properties)) {
			throw new RoolBackException(MyString.getInstance().parse(properties.get(S_NormalFlag.info)));
		}
		// -------------------------------------------------------业务关联记录表end------------------------------------//

		// -----------------------------------------------------------消息推送-----------------------------------------//
		if (S_ButtonType.Submit.equals(buttonType)) {
			sm_approvalProcess_workflow.setApprovalProcess_AF(af);
			sm_approvalProcess_workflow.setLastAction(3);
			messagePushletService.execute(sm_approvalProcess_workflow, loginUser);
		}
		// -----------------------------------------------------------消息推送end-----------------------------------------//

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	// 获取下一个结点索引
	public Long getNextStepWorkflowId(Sm_ApprovalProcess_Condition condition, List<Sm_ApprovalProcess_Node> nodeList,
			List<Sm_ApprovalProcess_Workflow> workflowList) {
		Long nextStep = condition.getNextStep(); // 下一步骤 结点Id
		Long nextStepWorkflowId = null;
		for (int index = 0; index < nodeList.size(); index++) {
			if (nextStep.equals(nodeList.get(index).getTableId())) {
				nextStepWorkflowId = workflowList.get(index).getTableId();
				break;
			}
		}
		return nextStepWorkflowId;
	}
}
