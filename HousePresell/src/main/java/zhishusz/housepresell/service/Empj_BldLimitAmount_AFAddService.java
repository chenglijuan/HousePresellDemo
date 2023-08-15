
package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFDtlForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.controller.form.pdf.ExportPdfForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDtlDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_ButtonType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.pdf.ExportPdfByWordService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;
import zhishusz.housepresell.util.project.BuildingAccountLogUtil;
import zhishusz.housepresell.util.project.EscrowStandardUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service添加操作：申请表-受限额度变更
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BldLimitAmount_AFAddService {
    private static final String BUSI_CODE = "03030101";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
    @Autowired
    private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;
    @Autowired
    private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
    @Autowired
    private Sm_UserDao sm_UserDao;
    @Autowired
    private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
    @Autowired
    private Empj_ProjectInfoDao empj_ProjectInfoDao;
    @Autowired
    private Empj_BuildingInfoDao empj_BuildingInfoDao;
    @Autowired
    private Tgpj_BldLimitAmountVer_AFDtlDao tgpj_bldLimitAmountVer_afDtlDao;
    @Autowired
    private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
    @Autowired
    private Sm_ApprovalProcessService sm_approvalProcessService;
    @Autowired
    private Sm_ApprovalProcess_AFDao processAfDao;
    @Autowired
    private Tgpj_BuildingAvgPriceDao avgPriceDao;
    @Autowired
    private Tgpj_BuildingAccountLogCalculateService calculateService;
    @Autowired
    private EscrowStandardUtil escrowStandardUtil;
    @Autowired
    private BuildingAccountLogUtil buildingAccountLogUtil;
    @Autowired
    private AttachmentJudgeExistUtil attachmentJudgeExistUtil;
    @Autowired
	private ExportPdfByWordService exportPdfByWordService;//生成PDF
    @Autowired
    private Tgpf_FundAppropriated_AFDtlDao tgpf_FundAppropriated_AFDtlDao;//用款申请子表
	@Autowired
    private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;//用款申请主表

    //附件相关
    @Autowired
    private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
    @Autowired
	private CheckMutexService checkMutexService;

    @SuppressWarnings("unchecked")
	public Properties execute(Empj_BldLimitAmount_AFForm model) {
        Properties properties = new MyProperties();
        //判断是否有对应的审批流程配置
        properties = sm_ApprovalProcessGetService.execute(BUSI_CODE, model.getUserId());
        if ("fail".equals(properties.getProperty(S_NormalFlag.result))) {
            if (properties.getProperty(S_NormalFlag.info).equals("noApproval")) {

            } else {
                return properties;
            }
        }
        Integer theState = S_TheState.Normal;
        String busiState = model.getBusiState();
        String eCode = model.geteCode();
        Long userStartId = model.getUserStartId();
        Long createTimeStamp = System.currentTimeMillis();
        Long developCompanyId = model.getDevelopCompanyId();
        String eCodeOfDevelopCompany = model.geteCodeOfDevelopCompany();
        Long projectId = model.getProjectId();
        String theNameOfProject = model.getTheNameOfProject();
        String eCodeOfProject = model.geteCodeOfProject();
        Long buildingId = model.getBuildingId();
        String eCodeOfBuilding = model.geteCodeOfBuilding();
        Double upfloorNumber = model.getUpfloorNumber();
        String eCodeFromConstruction = model.geteCodeFromConstruction();
        String eCodeFromPublicSecurity = model.geteCodeFromPublicSecurity();
//		Double recordAveragePriceOfBuilding = model.getRecordAveragePriceOfBuilding();
        String escrowStandard = model.getEscrowStandard();
        String deliveryType = model.getDeliveryType();
        Double orgLimitedAmount = model.getOrgLimitedAmount();
        String currentFigureProgress = model.getCurrentFigureProgress();
        Double currentLimitedRatio = model.getCurrentLimitedRatio();
        Double nodeLimitedAmount = model.getNodeLimitedAmount();
        Double totalGuaranteeAmount = model.getTotalGuaranteeAmount();
        Double cashLimitedAmount = model.getCashLimitedAmount();
        Double effectiveLimitedAmount = model.getEffectiveLimitedAmount();
        //		Double expectFigureProgress = model.getExpectFigureProgress();
        Long expectFigureProgressId = model.getExpectFigureProgressId();
        Double expectLimitedRatio = model.getExpectLimitedRatio();
        Double expectLimitedAmount = model.getExpectLimitedAmount();
        Double expectEffectLimitedAmount = model.getExpectEffectLimitedAmount();
        String remark = model.getRemark();
        if (orgLimitedAmount == null) {
            return MyBackInfo.fail(properties, "初始受限额度不能为空");
        }
        if (orgLimitedAmount == 0) {
            return MyBackInfo.fail(properties, "该楼幢没有初始受限额度，请先添加备案均价");
        }
        boolean uniqueLimitAmount = empj_BldLimitAmount_AFDao.isUniqueLimitAmount(model,true);
        if (!uniqueLimitAmount) {
            return MyBackInfo.fail(properties, "该楼幢有申请单正在申请中，无法添加");
        }
        //		Empj_BldLimitAmount_AFForm searchBuildingForm = new Empj_BldLimitAmount_AFForm();
//		searchBuildingForm.setTheState(S_TheState.Normal);
//		searchBuildingForm.setBuildingId(buildingId);
//		searchBuildingForm.setApprovalState(S_ApprovalState.Examining);
        //		Sm_ApprovalProcess_AFForm processAfForm = new Sm_ApprovalProcess_AFForm();
//		processAfForm.setTheState(S_TheState.Normal);
//		processAfForm.setBusiState("审核中");
//		processAfForm.setBusiCode(BUSI_CODE);
//		processAfForm.setSourceId(buildingId);
//		Integer byPage_size = processAfDao
//				.findByPage_Size(processAfDao.getQuery_Size(processAfDao.getBasicHQL(), processAfForm));
//		if(byPage_size>0){
//			return MyBackInfo.fail(properties, "该楼幢有申请单正在申请中，无法添加");
//		}

        if (projectId == null || projectId < 1) {
            return MyBackInfo.fail(properties, "关联项目不能为空");
        }
        if (buildingId == null || buildingId < 1) {
            return MyBackInfo.fail(properties, "关联楼幢不能为空");
        }
        if (expectFigureProgressId == null) {
            return MyBackInfo.fail(properties, "请选择拟变更形象进度");
        }

        Emmp_CompanyInfo developCompany = (Emmp_CompanyInfo) emmp_CompanyInfoDao.findById(developCompanyId);
        Empj_ProjectInfo project = (Empj_ProjectInfo) empj_ProjectInfoDao.findById(projectId);
        Empj_BuildingInfo building = (Empj_BuildingInfo) empj_BuildingInfoDao.findById(buildingId);
        
        /**
		 * BUG#4080 互斥业务
		 */
		/*Empj_BuildingInfoForm buildingInfoForm = new Empj_BuildingInfoForm();
		buildingInfoForm.setTableId(buildingId);
		Properties properties1 = checkMutexService.checkPaymentGuaranteeApply(buildingInfoForm);
		if(!S_NormalFlag.success.equals(properties1.get(S_NormalFlag.result)))
		{
			return properties1;
		}*/
		/**
		 * BUG#4080 互斥业务
		 */
        
        /**
		 * BUG#4080 互斥业务
		 */
		Empj_BuildingInfoForm buildingInfoForm = new Empj_BuildingInfoForm();
		buildingInfoForm.setTableId(buildingId);
		Properties properties1 = checkMutexService.checkSpecialFundAppropriated(buildingInfoForm);
		if(!S_NormalFlag.success.equals(properties1.get(S_NormalFlag.result)))
		{
			return properties1;
		}
		/**
		 * BUG#4080 互斥业务
		 */
        
        /**
         * xsz by time 2019-7-15 10:42:52
         * 校验楼幢是否已发起用款申请
         */
        Tgpf_FundAppropriated_AFDtlForm AFDtlModel = new Tgpf_FundAppropriated_AFDtlForm();
		AFDtlModel.setTheState(S_TheState.Normal);
		AFDtlModel.setBuilding(building);
		AFDtlModel.setBuildingId(building.getTableId());
		
		List<Tgpf_FundAppropriated_AFDtl> afDtlList = new ArrayList<>();
		afDtlList = tgpf_FundAppropriated_AFDtlDao.findByPage(tgpf_FundAppropriated_AFDtlDao.getQuery(tgpf_FundAppropriated_AFDtlDao.getBasicHQL(), AFDtlModel));
		if(!afDtlList.isEmpty())
		{
			Tgpf_FundAppropriated_AF af = afDtlList.get(0).getMainTable();
			if(!S_ApprovalState.Completed.equals(af.getApprovalState()))
			{
				return MyBackInfo.fail(properties, "该楼幢已发起用款申请流程，请待流程结束后重新申请！");
			}
		}

        if (developCompany == null) {
            return MyBackInfo.fail(properties, "关联开发企业不能为空");
        }
        if (project == null) {
            return MyBackInfo.fail(properties, "关联项目不能为空");
        }
        if (building == null) {
            return MyBackInfo.fail(properties, "关联楼幢不能为空");
        }
        MsgInfo buildingInEscrow = escrowStandardUtil.isBuildingInEscrow(building);
        if (!buildingInEscrow.isSuccess()) {
            return MyBackInfo.fail(properties, buildingInEscrow.getInfo());
        }
        MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);
        if(!msgInfo.isSuccess()){
            return MyBackInfo.fail(properties, msgInfo.getInfo());
        }
//		//托管终止判断 开始
//		Empj_BuildingExtendInfo extendInfo = building.getExtendInfo();
//		if (extendInfo != null)
//		{
//			String escrowState = extendInfo.getEscrowState();
//			if(escrowState!=null){
//				if(escrowState.equals(S_EscrowState.EndEscrowState)){
//					return MyBackInfo.fail(properties, "该楼幢正在申请托管终止，无法变更受限额度");
//				}else if(escrowState.equals(S_EscrowState.EndEscrowState)){
//					return MyBackInfo.fail(properties, "该楼幢已经托管终止，无法变更受限额度");
//				}
//			}
//		}
//		//托管终止判断 结束
        Tgpj_BuildingAccount buildingAccount = building.getBuildingAccount();
        if (buildingAccount == null) {
            return MyBackInfo.fail(properties, "该楼幢没有楼幢账户");
        }
        Double orgLimitedAmountInTable = buildingAccount.getOrgLimitedAmount();
        if (orgLimitedAmountInTable == null) {
            return MyBackInfo.fail(properties, "该楼幢没有初始受限额度，请先添加备案均价");
        }
        if (orgLimitedAmountInTable == 0) {
            return MyBackInfo.fail(properties, "该楼幢没有初始受限额度，请先添加备案均价");
        }
        Tgpj_BldLimitAmountVer_AFDtl versionAfDtl = tgpj_bldLimitAmountVer_afDtlDao.findById(expectFigureProgressId);
        if (versionAfDtl == null) {
            return MyBackInfo.fail(properties, "变更额度不存在");
        }
        Double recordAveragePriceOfBuilding = 0d;
        recordAveragePriceOfBuilding = buildingAccount.getRecordAvgPriceOfBuilding();
        /*Tgpj_BuildingAvgPriceForm tgpj_buildingAvgPriceForm = new Tgpj_BuildingAvgPriceForm();
        tgpj_buildingAvgPriceForm.setTheState(S_TheState.Normal);
        tgpj_buildingAvgPriceForm.setBuildingInfoId(buildingId);
        List<Tgpj_BuildingAvgPrice> byPage = avgPriceDao
                .findByPage(avgPriceDao.getQuery(avgPriceDao.getBasicHQL(), tgpj_buildingAvgPriceForm));
        if (byPage.size() > 0) {
            Tgpj_BuildingAvgPrice buildingAvgPrice = byPage.get(0);
            recordAveragePriceOfBuilding = buildingAvgPrice.getRecordAveragePrice();
        }*/
        //		boolean uniqueBuilding = empj_BldLimitAmount_AFDao.isUniqueBuilding(model);
//		if (!uniqueBuilding)
//		{
//			return MyBackInfo.fail(properties, "该楼幢对应的受限额度已存在，无法重复添加");
//		}
//		currentFigureProgress = versionAfDtl.getStageName();
//		currentLimitedRatio = versionAfDtl.getLimitedAmount();

        Empj_BldLimitAmount_AF empj_BldLimitAmount_AF = new Empj_BldLimitAmount_AF();
        empj_BldLimitAmount_AF = new Empj_BldLimitAmount_AF();
        empj_BldLimitAmount_AF.setTheState(theState);
//		empj_BldLimitAmount_AF.setBusiState(busiState);
        empj_BldLimitAmount_AF.setBusiState(S_BusiState.NoRecord);
        empj_BldLimitAmount_AF.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
        //		empj_BldLimitAmount_AF.setUserStart(userStart);
        empj_BldLimitAmount_AF.setCreateTimeStamp(createTimeStamp);
        empj_BldLimitAmount_AF.setUserStart(model.getUser());
        empj_BldLimitAmount_AF.setUserUpdate(model.getUser());
        empj_BldLimitAmount_AF.setLastUpdateTimeStamp(System.currentTimeMillis());
        empj_BldLimitAmount_AF.setDevelopCompany(developCompany);
        empj_BldLimitAmount_AF.seteCodeOfDevelopCompany(eCodeOfDevelopCompany);
        empj_BldLimitAmount_AF.setProject(project);
        empj_BldLimitAmount_AF.setTheNameOfProject(theNameOfProject);
        empj_BldLimitAmount_AF.seteCodeOfProject(eCodeOfProject);
        empj_BldLimitAmount_AF.setBuilding(building);
        empj_BldLimitAmount_AF.seteCodeOfBuilding(eCodeOfBuilding);
        empj_BldLimitAmount_AF.setUpfloorNumber(upfloorNumber);
        empj_BldLimitAmount_AF.seteCodeFromConstruction(eCodeFromConstruction);
        empj_BldLimitAmount_AF.seteCodeFromPublicSecurity(eCodeFromPublicSecurity);
        empj_BldLimitAmount_AF.setRecordAveragePriceOfBuilding(recordAveragePriceOfBuilding);
        empj_BldLimitAmount_AF.setEscrowStandard(escrowStandard);
        empj_BldLimitAmount_AF.setDeliveryType(deliveryType);
        empj_BldLimitAmount_AF.setOrgLimitedAmount(orgLimitedAmount);
        empj_BldLimitAmount_AF.setCurrentFigureProgress(currentFigureProgress);
        empj_BldLimitAmount_AF.setCurrentLimitedRatio(currentLimitedRatio);
        empj_BldLimitAmount_AF.setNodeLimitedAmount(nodeLimitedAmount);
        empj_BldLimitAmount_AF.setTotalGuaranteeAmount(totalGuaranteeAmount);
        empj_BldLimitAmount_AF.setCashLimitedAmount(cashLimitedAmount);
        empj_BldLimitAmount_AF.setEffectiveLimitedAmount(effectiveLimitedAmount);
//				empj_BldLimitAmount_AF.setExpectFigureProgress(expectFigureProgress);
        empj_BldLimitAmount_AF.setExpectFigureProgress(versionAfDtl);
        empj_BldLimitAmount_AF.setExpectLimitedRatio(expectLimitedRatio);
        empj_BldLimitAmount_AF.setExpectLimitedAmount(expectLimitedAmount);
        empj_BldLimitAmount_AF.setExpectEffectLimitedAmount(expectEffectLimitedAmount);
        empj_BldLimitAmount_AF.setRemark(remark);
        empj_BldLimitAmount_AFDao.save(empj_BldLimitAmount_AF);
        Long tableId = empj_BldLimitAmount_AF.getTableId();

        sm_AttachmentBatchAddService.execute(model, empj_BldLimitAmount_AF.getTableId());

        String currentFigureProgressChange = versionAfDtl.getStageName();
        Double currentLimitedRatioChange = versionAfDtl.getLimitedAmount();
        if (buildingAccount != null && model.getButtonType().equals(S_ButtonType.Submit)) {
            buildingAccountLogUtil.changeAndCaculate(building,tableId,BUSI_CODE,tgpj_BuildingAccountLog -> {
                setChanges(versionAfDtl, tgpj_BuildingAccountLog);
            });
        }

        properties.put("tableId", empj_BldLimitAmount_AF.getTableId());

        //没有配置审批流程无需走审批流直接保存数据库
        if (!"noApproval".equals(properties.getProperty(S_NormalFlag.info))) {
        	
        	String buttonType = model.getButtonType();
        	/*
        	 * xsz by time 2019-4-11 16:17:07
        	 * 正式提交前校验签章
        	 */
        	String isSign = model.getIsSign();
        	if(null == isSign)
        	{
        		isSign = "0";
        	}

        	if(!"1".equals(isSign)&&"2".equals(buttonType))
        	{
        		/*
                 * xsz by time 2019-1-19 10:44:26
                 * 审批操作结束后，生成发对应的pdf并检查是否有签章权限
                 */
                /*
    			 * 并将生成PDF后上传值OSS路径返回给前端
    			 * 
    			 * 参数：
    			 * sourceBusiCode：业务编码
    			 * sourceId：单据ID
    			 */
    			Sm_User user = model.getUser();
    			//登录人是否具有签章
    			if(null!=user.getIsSignature()&&"1".equals(user.getIsSignature()))
    			{
    				
    				ExportPdfForm pdfModel = new ExportPdfForm();
    				pdfModel.setSourceBusiCode(BUSI_CODE);
    				pdfModel.setSourceId(String.valueOf(tableId));
    				Properties executeProperties = exportPdfByWordService.execute(pdfModel);
    				String pdfUrl = (String) executeProperties.get("pdfUrl");
    				
    				Map<String, String> signatureMap = new HashMap<>();
    				
    				signatureMap.put("signaturePath", pdfUrl);
    				//TODO 此配置后期做成配置
    				signatureMap.put("signatureKeyword", "开发企业：");
    				signatureMap.put("ukeyNumber", model.getUser().getUkeyNumber());
    				
    				properties.put("signatureMap", signatureMap);
    				properties.put(S_NormalFlag.result, S_NormalFlag.success);
    			    properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
    				return properties;
    				
    			}
        	}
        	
            Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties
                    .get("sm_approvalProcess_cfg");

            //审批操作
            sm_approvalProcessService.execute(empj_BldLimitAmount_AF, model, sm_approvalProcess_cfg);
            
        }else{
            buildingAccountLogUtil.calculateWithoutApproval(building,tableId,BUSI_CODE,tgpj_BuildingAccountLog -> {
                setChanges(versionAfDtl, tgpj_BuildingAccountLog);
            });
            //备案人，备案日期，备案状态，审批状态
            empj_BldLimitAmount_AF.setApprovalState(S_ApprovalState.Completed);
            empj_BldLimitAmount_AF.setBusiState(S_BusiState.HaveRecord);
            empj_BldLimitAmount_AF.setUserRecord(model.getUser());
            empj_BldLimitAmount_AF.setRecordTimeStamp(System.currentTimeMillis());
            empj_BldLimitAmount_AFDao.save(empj_BldLimitAmount_AF);
        }

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        return properties;
    }

    private void setChanges(Tgpj_BldLimitAmountVer_AFDtl versionAfDtl, Tgpj_BuildingAccountLog tgpj_buildingAccountLog) {
        tgpj_buildingAccountLog.setCurrentFigureProgress(versionAfDtl.getStageName());
        tgpj_buildingAccountLog.setCurrentLimitedRatio(versionAfDtl.getLimitedAmount());
        tgpj_buildingAccountLog.setBldLimitAmountVerDtl(versionAfDtl);
    }

//    private void setChanges(String currentFigureProgressChange, Double currentLimitedRatioChange, Tgpj_BuildingAccountLog tgpj_BuildingAccountLog) {
//        tgpj_BuildingAccountLog.setCurrentFigureProgress(currentFigureProgressChange);
//        tgpj_BuildingAccountLog.setCurrentLimitedRatio(currentLimitedRatioChange);
//    }
}
