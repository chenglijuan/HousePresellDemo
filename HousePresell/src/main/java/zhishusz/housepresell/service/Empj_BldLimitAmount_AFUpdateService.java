package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.controller.form.pdf.ExportPdfForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDtlDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_User;
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
import zhishusz.housepresell.util.ObjectCopier;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;
import zhishusz.housepresell.util.project.BuildingAccountLogUtil;
import zhishusz.housepresell.util.project.EscrowStandardUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/*
 * Service更新操作：申请表-受限额度变更
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BldLimitAmount_AFUpdateService {
    private static final String BUSI_CODE = "03030101";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
    //	private static final String BUSI_CODE = "03030102";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
    @Autowired
    private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;
    @Autowired
    private Sm_BusiState_LogAddService logAddService;
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
    private Sm_ApprovalProcessService sm_approvalProcessService;
    @Autowired
    private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
    @Autowired
    private Sm_PoCompareResult sm_PoCompareResult;
    @Autowired
    private Sm_AttachmentDao sm_AttachmentDao;
    @Autowired
    private Tgpj_BuildingAccountLogCalculateService calculateService;
    @Autowired
    private Tgpj_BuildingAvgPriceDao avgPriceDao;
    @Autowired
    private EscrowStandardUtil escrowStandardUtil;
    @Autowired
    private BuildingAccountLogUtil buildingAccountLogUtil;
    @Autowired
    private AttachmentJudgeExistUtil attachmentJudgeExistUtil;
    @Autowired
	private ExportPdfByWordService exportPdfByWordService;//生成PDF


    //附件相关
    @Autowired
    private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;

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
        Long tableId = model.getTableId();
        Integer theState = model.getTheState();
        String busiState = model.getBusiState();
        Long userUpdateId = model.getUserUpdateId();
        Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
        Long projectId = model.getProjectId();
        Long buildingId = model.getBuildingId();
        String eCodeFromConstruction = model.geteCodeFromConstruction();
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
        String buttonType = model.getButtonType();//提交保存按钮
        String remark = model.getRemark();
        Sm_User userStart = model.getUserStart();
        if (orgLimitedAmount == null) {
            return MyBackInfo.fail(properties, "初始受限额度不能为空");
        }
        if (orgLimitedAmount == 0) {
            return MyBackInfo.fail(properties, "该楼幢没有初始受限额度，请先添加备案均价");
        }
        boolean uniqueLimitAmount = empj_BldLimitAmount_AFDao.isUniqueLimitAmount(model,false);
        if (!uniqueLimitAmount) {
            return MyBackInfo.fail(properties, "该楼幢有申请单正在申请中，无法添加");
        }
        //		if(theState == null || theState < 1)
//		{
//			return MyBackInfo.fail(properties, "状态 S_TheState 初始为Normal不能为空");
//		}
//		if(busiState == null || busiState.length()< 1)
//		{
//			return MyBackInfo.fail(properties, "业务状态不能为空");
//		}
//		if(eCode == null || eCode.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "编号不能为空");
//		}
//		if(userStartId == null || userStartId < 1)
//		{
//			return MyBackInfo.fail(properties, "创建人不能为空");
//		}
//		if(createTimeStamp == null || createTimeStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "创建时间不能为空");
//		}
//		if(lastUpdateTimeStamp == null || lastUpdateTimeStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "最后修改日期不能为空");
//		}
//		if(userRecordId == null || userRecordId < 1)
//		{
//			return MyBackInfo.fail(properties, "备案人不能为空");
//		}
//		if(recordTimeStamp == null || recordTimeStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "备案日期不能为空");
//		}
//		if(developCompanyId == null || developCompanyId < 1)
//		{
//			return MyBackInfo.fail(properties, "关联开发企业不能为空");
//		}
//		if(eCodeOfDevelopCompany == null || eCodeOfDevelopCompany.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "开发企业编号不能为空");
//		}
        if (projectId == null || projectId < 1) {
            return MyBackInfo.fail(properties, "关联项目不能为空");
        }
//		if(theNameOfProject == null || theNameOfProject.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "项目名称-冗余不能为空");
//		}
//		if(eCodeOfProject == null || eCodeOfProject.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "项目编号不能为空");
//		}
        if (buildingId == null || buildingId < 1) {
            return MyBackInfo.fail(properties, "关联楼幢不能为空");
        }
//		if(eCodeOfBuilding == null || eCodeOfBuilding.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "楼幢编号不能为空");
//		}
//		if(upfloorNumber == null || upfloorNumber < 1)
//		{
//			return MyBackInfo.fail(properties, "地上楼层数不能为空");
//		}
        if (eCodeFromConstruction == null || eCodeFromConstruction.length() == 0) {
            return MyBackInfo.fail(properties, "施工编号不能为空");
        }
//		if(eCodeFromPublicSecurity == null || eCodeFromPublicSecurity.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "公安编号不能为空");
//		}
//		if(recordAveragePriceOfBuilding == null || recordAveragePriceOfBuilding < 1)
//		{
//			return MyBackInfo.fail(properties, "当前楼幢住宅备案均价（元/㎡）不能为空");
//		}
//		if(escrowStandard == null || escrowStandard.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "托管标准不能为空");
//		}
//		if(deliveryType == null || deliveryType.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "交付类型不能为空");
//		}
//		if(orgLimitedAmount == null || orgLimitedAmount < 1)
//		{
//			return MyBackInfo.fail(properties, "初始受限额度不能为空");
//		}
//		if(currentFigureProgress == null || currentFigureProgress < 1)
//		{
//			return MyBackInfo.fail(properties, "当前形象进度不能为空");
//		}
//		if(currentLimitedRatio == null || currentLimitedRatio < 1)
//		{
//			return MyBackInfo.fail(properties, "当前受限比例不能为空");
//		}
//		if(nodeLimitedAmount == null || nodeLimitedAmount < 1)
//		{
//			return MyBackInfo.fail(properties, "节点受限额度不能为空");
//		}
//		if(totalGuaranteeAmount == null || totalGuaranteeAmount < 1)
//		{
//			return MyBackInfo.fail(properties, "累计可计入保证金额不能为空");
//		}
//		if(cashLimitedAmount == null || cashLimitedAmount < 1)
//		{
//			return MyBackInfo.fail(properties, "现金受限额度不能为空");
//		}
//		if(effectiveLimitedAmount == null || effectiveLimitedAmount < 1)
//		{
//			return MyBackInfo.fail(properties, "有效受限额度不能为空");
//		}
        if (expectFigureProgressId == null) {
            return MyBackInfo.fail(properties, "请选择拟变更形象进度");
        }
//		if(expectLimitedRatio == null || expectLimitedRatio < 1)
//		{
//			return MyBackInfo.fail(properties, "拟变更受限比例不能为空");
//		}
//		if(expectLimitedAmount == null || expectLimitedAmount < 1)
//		{
//			return MyBackInfo.fail(properties, "拟变更受限额度不能为空");
//		}
//		if(expectEffectLimitedAmount == null || expectEffectLimitedAmount < 1)
//		{
//			return MyBackInfo.fail(properties, "拟变更有效受限额度不能为空");
//		}

//		Sm_User userUpdate = (Sm_User)sm_UserDao.findById(userUpdateId);
//		if(userUpdate == null)
//		{
//			return MyBackInfo.fail(properties, "'userUpdate(Id:" + userUpdate + ")'不存在");
//		}
        Empj_ProjectInfo project = (Empj_ProjectInfo) empj_ProjectInfoDao.findById(projectId);
        if (project == null) {
            return MyBackInfo.fail(properties, "'project(Id:" + projectId + ")'不存在");
        }
        Empj_BuildingInfo building = (Empj_BuildingInfo) empj_BuildingInfoDao.findById(buildingId);
        if (building == null) {
            return MyBackInfo.fail(properties, "'building(Id:" + buildingId + ")'不存在");
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
        Tgpj_BuildingAvgPriceForm tgpj_buildingAvgPriceForm = new Tgpj_BuildingAvgPriceForm();
        tgpj_buildingAvgPriceForm.setTheState(S_TheState.Normal);
        tgpj_buildingAvgPriceForm.setBuildingInfoId(buildingId);
        List<Tgpj_BuildingAvgPrice> byPage = avgPriceDao
                .findByPage(avgPriceDao.getQuery(avgPriceDao.getBasicHQL(), tgpj_buildingAvgPriceForm));
        if (byPage.size() > 0) {
            Tgpj_BuildingAvgPrice buildingAvgPrice = byPage.get(0);
            recordAveragePriceOfBuilding = buildingAvgPrice.getRecordAveragePrice();
        }
//		Double limitedAmount = versionAfDtl.getLimitedAmount();
//		if (limitedAmount != null)
//		{
//			currentLimitedRatio=limitedAmount;
//		}
        model.setExpectFigureProgress(versionAfDtl);

        Long empj_BldLimitAmount_AFId = model.getTableId();
        Empj_BldLimitAmount_AF empj_BldLimitAmount_AF = (Empj_BldLimitAmount_AF) empj_BldLimitAmount_AFDao.findById(empj_BldLimitAmount_AFId);
        Empj_BldLimitAmount_AF empj_BldLimitAmount_AFOld = ObjectCopier.copy(empj_BldLimitAmount_AF);
        if (empj_BldLimitAmount_AF == null) {
            return MyBackInfo.fail(properties, "'Empj_BldLimitAmount_AF(Id:" + empj_BldLimitAmount_AFId + ")'不存在");
        }


        String currentFigureProgressChange = versionAfDtl.getStageName();
        Double currentLimitedRatioChange = versionAfDtl.getLimitedAmount();
        if (buildingAccount != null && model.getButtonType().equals(S_ButtonType.Submit)) {
            buildingAccountLogUtil.changeAndCaculate(building, model.getTableId(), BUSI_CODE, tgpj_BuildingAccountLog -> {
                setChanges(versionAfDtl, tgpj_BuildingAccountLog);
            });
        }

        lastUpdateTimeStamp = System.currentTimeMillis();

        empj_BldLimitAmount_AF.setTheState(theState);
        empj_BldLimitAmount_AF.setBusiState(busiState);
//		empj_BldLimitAmount_AF.seteCode(eCode);
//		empj_BldLimitAmount_AF.setUserStart(userStart);
//		empj_BldLimitAmount_AF.setCreateTimeStamp(createTimeStamp);
//		empj_BldLimitAmount_AF.setUserUpdate(userUpdate);
        empj_BldLimitAmount_AF.setUserUpdate(model.getUser());
        empj_BldLimitAmount_AF.setLastUpdateTimeStamp(System.currentTimeMillis());
//		empj_BldLimitAmount_AF.setUserRecord(userRecord);
//		empj_BldLimitAmount_AF.setRecordTimeStamp(recordTimeStamp);
//		empj_BldLimitAmount_AF.setDevelopCompany(developCompany);
//		empj_BldLimitAmount_AF.seteCodeOfDevelopCompany(eCodeOfDevelopCompany);
        empj_BldLimitAmount_AF.setProject(project);
//		empj_BldLimitAmount_AF.setTheNameOfProject(theNameOfProject);
//		empj_BldLimitAmount_AF.seteCodeOfProject(eCodeOfProject);
        empj_BldLimitAmount_AF.setBuilding(building);
//		empj_BldLimitAmount_AF.seteCodeOfBuilding(eCodeOfBuilding);
//		empj_BldLimitAmount_AF.setUpfloorNumber(upfloorNumber);
        empj_BldLimitAmount_AF.seteCodeFromConstruction(eCodeFromConstruction);
//		empj_BldLimitAmount_AF.seteCodeFromPublicSecurity(eCodeFromPublicSecurity);
//		empj_BldLimitAmount_AF.setRecordAveragePriceOfBuilding(recordAveragePriceOfBuilding);
//		empj_BldLimitAmount_AF.setEscrowStandard(escrowStandard);
//		empj_BldLimitAmount_AF.setDeliveryType(deliveryType);
        empj_BldLimitAmount_AF.setOrgLimitedAmount(orgLimitedAmount);
        empj_BldLimitAmount_AF.setCurrentFigureProgress(currentFigureProgress);
        empj_BldLimitAmount_AF.setCurrentLimitedRatio(currentLimitedRatio);
        empj_BldLimitAmount_AF.setNodeLimitedAmount(nodeLimitedAmount);
        empj_BldLimitAmount_AF.setTotalGuaranteeAmount(totalGuaranteeAmount);
        empj_BldLimitAmount_AF.setCashLimitedAmount(cashLimitedAmount);
        empj_BldLimitAmount_AF.setEffectiveLimitedAmount(effectiveLimitedAmount);
//		empj_BldLimitAmount_AF.setExpectFigureProgress(expectFigureProgress);
        empj_BldLimitAmount_AF.setExpectFigureProgress(versionAfDtl);
        empj_BldLimitAmount_AF.setExpectLimitedRatio(expectLimitedRatio);
        empj_BldLimitAmount_AF.setExpectLimitedAmount(expectLimitedAmount);
        empj_BldLimitAmount_AF.setExpectEffectLimitedAmount(expectEffectLimitedAmount);
        empj_BldLimitAmount_AF.setRemark(remark);

        sm_AttachmentBatchAddService.execute(model, model.getTableId());

        //没有配置审批流程无需走审批流直接保存数据库
        if (!"noApproval".equals(properties.getProperty(S_NormalFlag.info))) {
        	
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
            
        } else {
            buildingAccountLogUtil.calculateWithoutApproval(building, tableId, BUSI_CODE, tgpj_BuildingAccountLog -> {
                setChanges(versionAfDtl, tgpj_BuildingAccountLog);
            });
            //备案人，备案日期，备案状态，审批状态
            empj_BldLimitAmount_AF.setApprovalState(S_ApprovalState.Completed);
            empj_BldLimitAmount_AF.setBusiState(S_BusiState.HaveRecord);
            empj_BldLimitAmount_AF.setUserRecord(model.getUser());
            empj_BldLimitAmount_AF.setRecordTimeStamp(System.currentTimeMillis());
            empj_BldLimitAmount_AFDao.save(empj_BldLimitAmount_AF);
        }
////		empj_BldLimitAmount_AFDao.save(empj_BldLimitAmount_AF);
//		Empj_BldLimitAmount_AF empj_BldLimitAmount_AFNew = ObjectCopier.copy(empj_BldLimitAmount_AF);
////		logAddService.addLog(model, empj_BldLimitAmount_AFId, empj_BldLimitAmount_AFOld, empj_BldLimitAmount_AFNew);
////		sm_AttachmentBatchAddService.execute(model,model.getTableId());
//
////		审批流
//		Boolean flag = sm_PoCompareResult.execute(empj_BldLimitAmount_AFOld, empj_BldLimitAmount_AFNew);
//
//		System.out.println("结果："+flag);
//
//		if (flag)
//		{
//			for (Sm_AttachmentForm formOSS : model.getGeneralAttachmentList())
//			{
//				//如果有form没有tableId，说明有新增
//				if (formOSS.getTableId() == null || formOSS.getTableId() == 0)
//				{
//					flag = false;//有新增不一样
//					break;
//				}
//			}
//			if (flag) //如果没有新增再看有没有删除
//			{
//				Integer totalCountNew = model.getGeneralAttachmentList().length;
//
//				Sm_AttachmentForm theForm = new Sm_AttachmentForm();
//				theForm.setTheState(S_TheState.Normal);
//				theForm.setBusiType(BUSI_CODE);
//				theForm.setSourceId(MyString.getInstance().parse(model.getTableId()));
//
//				Integer totalCount = sm_AttachmentDao.findByPage_Size(sm_AttachmentDao.getQuery_Size(sm_AttachmentDao.getBasicHQL(), theForm));
//
//				if (totalCountNew < totalCount)
//				{
//					flag = false;//有删除不一样
//				}
//			}
//		}else{
//			//判断是否是未备案
//			//如果是未备案则先保存到数据库然后根据是提交按钮还是保存按钮判断是否走新增的审批流
//			if(S_BusiState.NoRecord.equals(empj_BldLimitAmount_AF.getBusiState()))
//			{
//				empj_BldLimitAmount_AFDao.update(empj_BldLimitAmount_AF);
//				//				empj_BuildingExtendInfoDao.update(empj_BuildingExtendInfo);
//
//				sm_AttachmentBatchAddService.execute(model, empj_BldLimitAmount_AF.getTableId());
//				//如果是提交按钮则需要走新增的审批流
//				if(S_ButtonType.Submit.equals(buttonType))
//				{
//					properties = sm_ApprovalProcessGetService.execute(BUSI_CODE, model.getUserId());
//					if("fail".equals(properties.getProperty(S_NormalFlag.result)))
//					{
//						return properties;
//					}
//
//					//没有配置审批流程无需走审批流直接保存数据库
//					if(!"noApproval".equals(properties.getProperty(S_NormalFlag.info)))
//					{
//						//有相应的审批流程配置才走审批流程
//						Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");
//
//						//审批操作
//						sm_approvalProcessService.execute(empj_BldLimitAmount_AF, model, sm_approvalProcess_cfg);
//					}
//				}
//			}
//
//			//已备案怎样都要走编辑的审批流
//			if(S_BusiState.HaveRecord.equals(empj_BldLimitAmount_AF.getBusiState()))
//			{
//				properties = sm_ApprovalProcessGetService.execute(BUSI_CODE, model.getUserId());
//				if("fail".equals(properties.getProperty(S_NormalFlag.result)))
//				{
//					return properties;
//				}
//
//				//没有配置审批流程无需走审批流直接保存数据库
//				if("noApproval".equals(properties.getProperty(S_NormalFlag.info)))
//				{
//					empj_BldLimitAmount_AFDao.update(empj_BldLimitAmount_AF);
//					//					empj_BuildingExtendInfoDao.update(empj_BuildingExtendInfo);
//
//					sm_AttachmentBatchAddService.execute(model, empj_BldLimitAmount_AF.getTableId());
//				}
//				else
//				{
//					//做一个还原操作
//
//					empj_BldLimitAmount_AF = ObjectCopier.copy(empj_BldLimitAmount_AFOld);
//					empj_BldLimitAmount_AF.setUserStart(userStart);
//					empj_BldLimitAmount_AF.setUserUpdate(model.getUser());
//
//					//						PropertyUtils.copyProperties(empj_BuildingInfo, empj_BuildingInfoTemplateOld);
//					//						PropertyUtils.copyProperties(empj_BuildingExtendInfo, empj_BuildingInfoTemplateOld);
//					//有相应的审批流程配置才走审批流程
//					Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");
//
//					//审批操作
//					sm_approvalProcessService.execute(empj_BldLimitAmount_AF, model, sm_approvalProcess_cfg);
//				}
//			}
//		}
//        String escrowStandardTypeName = escrowStandardUtil.getEscrowStandardTypeName(building);
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        properties.put("tableId", empj_BldLimitAmount_AF.getTableId());
//        properties.put("trusteeshipContent", escrowStandardTypeName);

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
