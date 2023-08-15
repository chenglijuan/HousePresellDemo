package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_BldEscrowCompleted_DtlForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFDtlForm;
import zhishusz.housepresell.controller.form.pdf.ExportPdfForm;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.*;
import zhishusz.housepresell.service.pdf.ExportPdfByWordService;
import zhishusz.housepresell.util.ObjectCopier;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Empj_BldEscrowCompletedForm;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.objectdiffer.model.Empj_BldEscrowCompletedTemplate;

/*
 * Service更新操作：申请表-项目托管终止（审批）-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BldEscrowCompletedApprovalProcessService
{
	private static final String BUSI_CODE = "03030102";//具体业务编码参看SVN文
	@Autowired
	private Empj_BldEscrowCompletedDao empj_BldEscrowCompletedDao;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
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
	@Autowired
    private Tgpf_FundAppropriated_AFDtlDao tgpf_FundAppropriated_AFDtlDao;//用款申请子表
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_BldEscrowCompletedForm model)
	{
		Properties properties = new MyProperties();

		Long mainTableId = model.getTableId();
		Empj_BldEscrowCompleted empj_BldEscrowCompleted = (Empj_BldEscrowCompleted)empj_BldEscrowCompletedDao.findById(mainTableId);
		if(empj_BldEscrowCompleted == null)
		{
			return MyBackInfo.fail(properties, "托管终止信息不存在");
		}
		
		
		/*
		 * 校验附件信息
		 * “是否已公式”字段选择“否”时企业新增时不需要维护“公式网址字段”及“公式截图”附件，此时需要维护“其他附件”进行说明
		 */
		Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
        sm_AttachmentForm.setSourceId(mainTableId.toString());
        sm_AttachmentForm.setBusiType("03030102");
        sm_AttachmentForm.setTheState(S_TheState.Normal);

        // 加载所有楼幢下的相关附件信息
        List<Sm_Attachment> sm_AttachmentList = attacmentDao
            .findByPage(attacmentDao.getQuery(attacmentDao.getBasicHQL2(), sm_AttachmentForm));
        if (null == sm_AttachmentList || sm_AttachmentList.size() == 0) {
        	System.out.println("未查询到有效的附件信息！");
            return MyBackInfo.fail(properties, "未查询到有效的附件信息！");
        }

        boolean hasSubmit = true;
		String hasFormula = empj_BldEscrowCompleted.getHasFormula();
		if("0".equals(hasFormula)){
			//否-校验其他附件
			
			
			for (int i = 0; i < sm_AttachmentList.size(); i++) {
	            if (sm_AttachmentList.get(i).getAttachmentCfg().getTheName().contains("其他附件")) {
	            	hasSubmit = false;
	            }
	        }
			
			if(hasSubmit){
				return MyBackInfo.fail(properties, "请上传‘其他附件’材料！");
			}
			
		}
		
		if("1".equals(hasFormula)){
			//是-校验公示截图
			
			for (int i = 0; i < sm_AttachmentList.size(); i++) {
	            if (sm_AttachmentList.get(i).getAttachmentCfg().getTheName().contains("公示截图")) {
	            	hasSubmit = false;
	            }
	        }
			
			if(hasSubmit){
				return MyBackInfo.fail(properties, "请上传‘公示截图’材料！");
			}
			
		}
		
		
		//校验是否生成签章文件
		Sm_Attachment attachmentBak = isSaveAttachment2("240180", String.valueOf(mainTableId));
		if(null == attachmentBak){
			return MyBackInfo.fail(properties, "请先进行‘公安-施工编号签章’后再提交！");
		}
		
		model.setButtonType(S_ButtonType.Submit);
		//拷贝对象，用于查看变更日志
//		Empj_BldEscrowCompleted empj_bldEscrowCompletedOld = ObjectCopier.copy(empj_BldEscrowCompleted);
//		Empj_BldEscrowCompletedTemplate empj_bldEscrowCompletedTemplateOld = new Empj_BldEscrowCompletedTemplate();
//		empj_bldEscrowCompletedTemplateOld.setBldEscrowCompleted(empj_bldEscrowCompletedOld);
//		empj_bldEscrowCompletedTemplateOld.createSpecialLogFieldWithDtlList(empj_bldEscrowCompletedOld.getEmpj_BldEscrowCompleted_DtlList());

		/**
		 * 新增明细表
		 * 修改明细表删除状态thestate=0
		 * 解除主表与明细表直接的关系
		 * 设置主表与新明细表一对多关联关系
		 */
		//托管终止明细表操作
		Tgpf_FundAppropriated_AFDtlForm AFDtlModel;
		List<Tgpf_FundAppropriated_AFDtl> afDtlList;
		List<Empj_BldEscrowCompleted_Dtl> empj_BldEscrowCompleted_Dtls = empj_BldEscrowCompleted.getEmpj_BldEscrowCompleted_DtlList();
		for (Empj_BldEscrowCompleted_Dtl empj_BldEscrowCompleted_Dtl : empj_BldEscrowCompleted_Dtls) {
			
			AFDtlModel = new Tgpf_FundAppropriated_AFDtlForm();
			AFDtlModel.setTheState(S_TheState.Normal);
			AFDtlModel.setBuilding(empj_BldEscrowCompleted_Dtl.getBuilding());
			AFDtlModel.setBuildingId(empj_BldEscrowCompleted_Dtl.getBuilding().getTableId());
			
			afDtlList = new ArrayList<>();
			afDtlList = tgpf_FundAppropriated_AFDtlDao.findByPage(tgpf_FundAppropriated_AFDtlDao.getQuery(tgpf_FundAppropriated_AFDtlDao.getBasicHQL(), AFDtlModel));
			if(!afDtlList.isEmpty())
			{
				Tgpf_FundAppropriated_AF af = afDtlList.get(0).getMainTable();
				if(!S_ApprovalState.Completed.equals(af.getApprovalState()))
				{
					return MyBackInfo.fail(properties, "楼幢："+empj_BldEscrowCompleted_Dtl.getBuilding().geteCodeFromConstruction()+"已发起用款申请流程，请待流程结束后重新申请！");
				}
			}
		}

		if(!"待提交".equals(empj_BldEscrowCompleted.getApprovalState())){
		    return MyBackInfo.fail(properties, "该单据已处于：'"+empj_BldEscrowCompleted.getApprovalState()+"'状态！");
		}
		
		//审批流
		/** TODO
		 * 已备案：不允许修改和删除
		 * 未备案：点击保存，直接保存到数据，不需要走审批流，因为当前审批流程状态是待提交（审核中不允许编辑，已完结审批状态是已备案）
		 * 点击提交，走新增审批流程（BUSI_CODE = xxxx01），其他和新增一样
		 * 更新时已备案的项目做日志相关操作
		 */
//		empj_BldEscrowCompletedDao.save(empj_BldEscrowCompleted);
		
		//如果是提交按钮则需要走新增的审批流
		properties = sm_ApprovalProcessGetService.execute(BUSI_CODE, model.getUserId());
		//判断是否满足审批条件（有审批角色，单审批流程）
		if("fail".equals(properties.getProperty(S_NormalFlag.result)))
		{
			return properties;
		}
		//配置审批流程需走审批流
		if(!"noApproval".equals(properties.getProperty(S_NormalFlag.info)))
		{
			
			/*
        	 * xsz by time 2019-4-11 16:17:07
        	 * 正式提交前校验签章
        	 */
        	String isSign = model.getIsSign();
        	System.out.println("isSign:" + isSign);
        	if(null == isSign)
        	{
        		isSign = "0";
        	}

        	if(!"1".equals(isSign))
        	{
        		System.out.println("签章分支---");
        		/*
    			 * xsz by time 提交结束后调用生成PDF方法
    			 * 并将生成PDF后上传值OSS路径返回给前端
    			 * 
    			 * 参数：
    			 * sourceBusiCode：业务编码
    			 * sourceId：单据ID
    			 * 
    			 * xsz by time 2019-3-11 19:28:10
    			 * 2.0
    			 * 每次点击提交时，重新生成新的协议pdf
    			 */
    			Sm_User user = model.getUser();
    			String busiCode = model.getBusiCode();
    			Long tableId = model.getTableId();
    			
    			System.out.println("user.getIsSignature():" + user.getIsSignature());
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
    				System.out.println(signatureMap.get("signatureKeyword"));
    				signatureMap.put("ukeyNumber", model.getUser().getUkeyNumber());
    				
    				Properties signatureProperties = new MyProperties();
    				signatureProperties.put("signatureMap", signatureMap);
    				signatureProperties.put(S_NormalFlag.result, S_NormalFlag.success);
    				signatureProperties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
    				
    				return signatureProperties;
    			}else{
    				return MyBackInfo.fail(properties, "生成签章失败！");
    			}
        	}
			
        	
        	System.out.println("正常分支---");
        	/*//生成公安-施工编号对照表
        	// 查询是否已经存在PDF附件
			Sm_Attachment attachment = isSaveAttachment2("240180", String.valueOf(mainTableId));
			if (null != attachment)
			{
				//如果存在附件，置为删除态重新生成
				attachment.setTheState(S_TheState.Deleted);
				attacmentDao.save(attachment);
				
			}
			
			ExportPdfForm pdfModel = new ExportPdfForm();
			pdfModel.setSourceBusiCode("240180");
			pdfModel.setSourceId(String.valueOf(mainTableId));
			Properties executeProperties = exportPdfByWordService.execute(pdfModel);
			if(!S_NormalFlag.success.equals(executeProperties.get(S_NormalFlag.result))){
				return executeProperties;
			}
			//生成公安-施工编号对照表*/        	
			//审批操作
			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");
			sm_approvalProcessService.execute(empj_BldEscrowCompleted, model, sm_approvalProcess_cfg);

			/**
			 * TODO
			 * 点击提交按钮 1、更改楼幢信息的托管状态-> 申请托管终止
			 * 2、未备案：更新"当前形象进度"为"交付"，"当前受限比例（%）"为0，"当前受限额度（元）"为0
			 */
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
			
		}
		else
		{
			empj_BldEscrowCompleted.setUserRecord(model.getUser());
			empj_BldEscrowCompleted.setRecordTimeStamp(System.currentTimeMillis()); //已备案的添加备案人、备案日期
			empj_BldEscrowCompleted.setBusiState(S_BusiState.HaveRecord); //已备案
			empj_BldEscrowCompleted.setApprovalState(S_ApprovalState.Completed); //已完结
			empj_BldEscrowCompletedDao.update(empj_BldEscrowCompleted);

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
//					tgpj_BuildingAccountLog.setNodeLimitedAmount(bldLimitAmountVer_afDtl.getLimitedAmount()); //当前受限额度
					tgpj_BuildingAccountLog.setNodeLimitedAmount(0.00); //当前受限额度
					tgpj_BuildingAccountLog.setBldLimitAmountVerDtl(bldLimitAmountVer_afDtl);
					tgpj_BuildingAccountLog.setCashLimitedAmount(0.00);

					calculateService.execute(tgpj_BuildingAccountLog);	//重新计算
					tgpj_BuildingAccountLimitedUpdateService.execute(tgpj_BuildingAccountLog);  //保存
				}
			}
		}				
				

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
		//托管终止打印编码
		String attacmentcfg = "240108";
		
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
	 * 是否存在PDF
	 * 
	 * @param sourceBusiCode
	 *            业务编码
	 * @param sourceId
	 *            业务数据Id
	 * @return
	 */
	Sm_Attachment isSaveAttachment2(String sourceBusiCode, String sourceId)
	{
		//托管终止打印编码
		String attacmentcfg = "240180";
		
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
