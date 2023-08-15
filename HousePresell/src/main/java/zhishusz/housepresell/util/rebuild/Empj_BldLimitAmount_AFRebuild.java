package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.Sm_BaseParameterGetService;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：申请表-受限额度变更
 * Company：ZhiShuSZ
 * */
@Service
public class Empj_BldLimitAmount_AFRebuild extends RebuilderBase<Empj_BldLimitAmount_AF> {
    @Autowired
    private Sm_BaseParameterGetService baseParameterGetService;
    @Autowired
    private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
    private MyDouble myDouble = MyDouble.getInstance();

    @Override
    public Properties getSimpleInfo(Empj_BldLimitAmount_AF object) {
        if (object == null) return null;
        Properties properties = new MyProperties();

        properties.put("tableId", object.getTableId());

        return properties;
    }

    @SuppressWarnings("unchecked")
	@Override
    public Properties getDetail(Empj_BldLimitAmount_AF object) {
        MyDatetime myDatetime = MyDatetime.getInstance();
        if (object == null) return null;
        Properties properties = new MyProperties();
        properties.put("tableId", object.getTableId());
        properties.put("theState", object.getTheState());
        properties.put("busiState", object.getBusiState());
        properties.put("eCode", object.geteCode());
        Sm_User userStart = object.getUserStart();
        if (userStart != null) {
            properties.put("userStartId", object.getUserStart().getTableId());
            properties.put("userStartName", object.getUserStart().getTheName());
        }
        properties.put("createTimeStamp", object.getCreateTimeStamp());
        properties.put("createTimeStampString", myDatetime.dateToString2(object.getCreateTimeStamp()));
        properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
        properties.put("lastUpdateTimeStampString", MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));
        Sm_User userUpdate = object.getUserUpdate();
        if (userUpdate != null) {
            properties.put("userUpdateId", userUpdate.getTableId());
            properties.put("userUpdateName", userUpdate.getTheName());
        }
        Sm_User userRecord = object.getUserRecord();
        if (userRecord != null) {
            properties.put("userRecordId", userRecord.getTableId());
            properties.put("userRecordName", userRecord.getTheName());
        }
        properties.put("recordTimeStamp", object.getRecordTimeStamp());
        properties.put("recordTimeStampString", myDatetime.dateToString2(object.getRecordTimeStamp()));
        Emmp_CompanyInfo developCompany = object.getDevelopCompany();
        if (developCompany != null) {
            properties.put("developCompanyId", developCompany.getTableId());
            properties.put("developCompanyName", developCompany.getTheName());
        }
        properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
        Empj_ProjectInfo project = object.getProject();
        if (project != null) {
            properties.put("projectId", project.getTableId());
            properties.put("projectName", project.getTheName());
        }
        properties.put("theNameOfProject", object.getTheNameOfProject());
        properties.put("eCodeOfProject", object.geteCodeOfProject());
        Empj_BuildingInfo building = object.getBuilding();
        if (building != null) {
            properties.put("buildingId", building.getTableId());
            properties.put("buildingEcode", building.geteCode());
            properties.put("buildingArea", building.getBuildingArea());
            properties.put("escrowArea", building.getEscrowArea());
            properties.put("escrowStandard", building.getEscrowStandard());
            Sm_BaseParameter parameter = baseParameterGetService.getParameter("5", building.getDeliveryType());
            properties.put("deliveryTypeName", parameter.getTheName());
            properties.put("deliveryTypeId", object.getDeliveryType());
//			properties.put("eCodeOfBuilding", building.geteCodeFromPresellSystem());
            properties.put("eCodeFromConstruction", building.geteCodeFromConstruction());
            properties.put("eCodeFromPresellSystem", building.geteCodeFromPresellSystem());
            properties.put("eCodeFromPublicSecurity", building.geteCodeFromPublicSecurity());
            properties.put("upfloorNumber", building.getUpfloorNumber());
            properties.put("eCodeOfBuilding", building.geteCode());
            
            // 受限额度版本
        	Tgpj_BldLimitAmountVer_AFDtl bldLimitAmountVerDtl = building.getBuildingAccount().getBldLimitAmountVerDtl();
        	if (null != bldLimitAmountVerDtl) {
        		Tgpj_BldLimitAmountVer_AF bldLimitAmountVer_AF = bldLimitAmountVerDtl.getBldLimitAmountVerMng();
        		properties.put("bldLimitAmountName", bldLimitAmountVer_AF.getTheName());
        		properties.put("bldLimitAmountId", bldLimitAmountVer_AF.getTableId());
        	}
        }
        properties.put("recordAveragePriceOfBuilding", object.getRecordAveragePriceOfBuilding());
        properties.put("orgLimitedAmount", object.getOrgLimitedAmount());
        properties.put("currentFigureProgress", object.getCurrentFigureProgress());
        properties.put("currentLimitedRatio", object.getCurrentLimitedRatio());
        properties.put("nodeLimitedAmount", object.getNodeLimitedAmount());
        properties.put("totalGuaranteeAmount", object.getTotalGuaranteeAmount());
        properties.put("cashLimitedAmount", object.getCashLimitedAmount());
        properties.put("effectiveLimitedAmount", object.getEffectiveLimitedAmount());
        Tgpj_BldLimitAmountVer_AFDtl expectFigureProgress = object.getExpectFigureProgress();
        if (expectFigureProgress != null) {
            properties.put("expectFigureProgressId", expectFigureProgress.getTableId());
            properties.put("expectFigureProgressStageName", expectFigureProgress.getStageName());
            properties.put("expectFigureProgressLimitAmount", expectFigureProgress.getLimitedAmount());
            properties.put("stageName", expectFigureProgress.getStageName());
            properties.put("expectFigureProgressStageLimit", expectFigureProgress.getStageName() + "-" + myDouble.noDecimal(expectFigureProgress.getLimitedAmount())+"%");
        
        }
        properties.put("expectLimitedRatio", object.getExpectLimitedRatio());
        properties.put("expectLimitedAmount", object.getExpectLimitedAmount());
        properties.put("expectEffectLimitedAmount", object.getExpectEffectLimitedAmount());
        properties.put("approvalState", object.getApprovalState());
        properties.put("remark", object.getRemark());
        properties.put("acceptExplain", object.getAcceptExplain());
        properties.put("appointExplain", object.getAppointExplain());
        properties.put("sceneInvestigationExplain", object.getSceneInvestigationExplain());
        properties.put("acceptTimeString", myDatetime.dateToStringMinute(object.getAcceptTimeStamp()));
        properties.put("appointTimeString", myDatetime.dateToStringMinute(object.getAppointTimeStamp()));
        properties.put("sceneInvestigationTimeString", myDatetime.dateToStringMinute(object.getSceneInvestigationTimeStamp()));

        /**
         * xsz by time 2019-4-30 16:22:49
         * 操作时间取申请单的提交时间
         * 
         */
        Sm_ApprovalProcess_AFForm afModel = new Sm_ApprovalProcess_AFForm();
        afModel.setTheState(S_TheState.Normal);
        afModel.setBusiCode("03030101");
        afModel.setSourceId(object.getTableId());
        afModel.setOrderBy("createTimeStamp");
        List<Sm_ApprovalProcess_AF> afList = new ArrayList<>();
        afList = sm_ApprovalProcess_AFDao.findByPage(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), afModel));
        if(null == afList || afList.size() == 0)
        {
        	properties.put("lastUpdateTimeStamp", "-");
            properties.put("lastUpdateTimeStampString", "-");
        }
        else
        {
        	Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = afList.get(0);
        	properties.put("lastUpdateTimeStamp", sm_ApprovalProcess_AF.getStartTimeStamp());
            properties.put("lastUpdateTimeStampString", MyDatetime.getInstance().dateToString2(sm_ApprovalProcess_AF.getStartTimeStamp()));
        }
        

        return properties;
    }
    
    public Properties getDetail2(Empj_BldLimitAmount_AF object) {
        MyDatetime myDatetime = MyDatetime.getInstance();
        if (object == null) return null;
        Properties properties = new MyProperties();
        properties.put("tableId", object.getTableId());
        properties.put("theState", object.getTheState());
        properties.put("busiState", object.getBusiState());
        properties.put("eCode", object.geteCode());
        Sm_User userStart = object.getUserStart();
        if (userStart != null) {
            properties.put("userStartId", object.getUserStart().getTableId());
            properties.put("userStartName", object.getUserStart().getTheName());
        }
        properties.put("createTimeStamp", object.getCreateTimeStamp());
        properties.put("createTimeStampString", myDatetime.dateToString2(object.getCreateTimeStamp()));
        properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
        properties.put("lastUpdateTimeStampString", MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));
        Sm_User userUpdate = object.getUserUpdate();
        if (userUpdate != null) {
            properties.put("userUpdateId", userUpdate.getTableId());
            properties.put("userUpdateName", userUpdate.getTheName());
        }
        Sm_User userRecord = object.getUserRecord();
        if (userRecord != null) {
            properties.put("userRecordId", userRecord.getTableId());
            properties.put("userRecordName", userRecord.getTheName());
        }
        properties.put("recordTimeStamp", object.getRecordTimeStamp());
        properties.put("recordTimeStampString", myDatetime.dateToString2(object.getRecordTimeStamp()));
        Emmp_CompanyInfo developCompany = object.getDevelopCompany();
        if (developCompany != null) {
            properties.put("developCompanyId", developCompany.getTableId());
            properties.put("developCompanyName", developCompany.getTheName());
        }
        properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
        Empj_ProjectInfo project = object.getProject();
        if (project != null) {
            properties.put("projectId", project.getTableId());
            properties.put("projectName", project.getTheName());
        }
        properties.put("theNameOfProject", object.getTheNameOfProject());
        properties.put("eCodeOfProject", object.geteCodeOfProject());
        
        
        Empj_BuildingInfo building = object.getBuilding();
        if (building != null) {
        	
                
            properties.put("buildingId", building.getTableId());
            properties.put("buildingEcode", building.geteCode());
            properties.put("buildingArea", building.getBuildingArea());
            properties.put("escrowArea", building.getEscrowArea());
            properties.put("escrowStandard", building.getEscrowStandard());
            Sm_BaseParameter parameter = baseParameterGetService.getParameter("5", building.getDeliveryType());
            properties.put("deliveryTypeName", parameter.getTheName());
            properties.put("deliveryTypeId", object.getDeliveryType());
            properties.put("eCodeFromConstruction", building.geteCodeFromConstruction());
            properties.put("eCodeFromPresellSystem", building.geteCodeFromPresellSystem());
            properties.put("eCodeFromPublicSecurity", building.geteCodeFromPublicSecurity());
            properties.put("upfloorNumber", building.getUpfloorNumber());
            properties.put("eCodeOfBuilding", building.geteCode());
            
            // 受限额度版本
        	properties.put("buildingId", building.getTableId());
            properties.put("buildingEcode", building.geteCode());
            properties.put("buildingArea", building.getBuildingArea());
            properties.put("escrowArea", building.getEscrowArea());
            properties.put("eCodeOfBuilding", building.geteCodeFromPresellSystem());
            properties.put("eCodeFromConstruction", building.geteCodeFromConstruction());
            
            Tgpj_BldLimitAmountVer_AFDtl expectFigureProgress = object.getExpectFigureProgress();
            
            Tgpj_BldLimitAmountVer_AF bldLimitAmountVer_AF = expectFigureProgress.getBldLimitAmountVerMng();
    		properties.put("bldLimitAmountName", bldLimitAmountVer_AF.getTheName());
    		properties.put("bldLimitAmountId", bldLimitAmountVer_AF.getTableId());
            
            properties.put("currentFigureProgress", expectFigureProgress.getStageName() +"-"+myDouble.noDecimal(expectFigureProgress.getLimitedAmount())+"%");
            
            Tgpj_BuildingAccount buildingAccount = building.getBuildingAccount();
            properties.put("nodeLimitedAmount", buildingAccount.getNodeLimitedAmount());//节点受限额度
            properties.put("recordAveragePriceOfBuilding", buildingAccount.getRecordAvgPriceOfBuilding());//备案均价
            properties.put("orgLimitedAmount", buildingAccount.getOrgLimitedAmount());//初始受限额度
            
            properties.put("currentFigureProgress", expectFigureProgress.getStageName());//当前形象进度
            properties.put("currentLimitedRatio", expectFigureProgress.getLimitedAmount());//当前受限比例
            properties.put("cashLimitedAmount", buildingAccount.getCashLimitedAmount());//现金受限额度
            properties.put("effectiveLimitedAmount", buildingAccount.getEffectiveLimitedAmount());//有效受限额度
            
            
        }
        
        
        /*properties.put("recordAveragePriceOfBuilding", object.getRecordAveragePriceOfBuilding());
        properties.put("orgLimitedAmount", object.getOrgLimitedAmount());
        properties.put("currentFigureProgress", object.getCurrentFigureProgress());
        properties.put("currentLimitedRatio", object.getCurrentLimitedRatio());
        properties.put("nodeLimitedAmount", object.getNodeLimitedAmount());
        properties.put("totalGuaranteeAmount", object.getTotalGuaranteeAmount());
        properties.put("cashLimitedAmount", object.getCashLimitedAmount());
        properties.put("effectiveLimitedAmount", object.getEffectiveLimitedAmount());
        Tgpj_BldLimitAmountVer_AFDtl expectFigureProgress = object.getExpectFigureProgress();
        if (expectFigureProgress != null) {
            properties.put("expectFigureProgressId", expectFigureProgress.getTableId());
            properties.put("expectFigureProgressStageName", expectFigureProgress.getStageName());
            properties.put("expectFigureProgressLimitAmount", expectFigureProgress.getLimitedAmount());
            properties.put("stageName", expectFigureProgress.getStageName());
            properties.put("expectFigureProgressStageLimit", expectFigureProgress.getStageName() + "-" + myDouble.noDecimal(expectFigureProgress.getLimitedAmount())+"%");
        
        }
        properties.put("expectLimitedRatio", object.getExpectLimitedRatio());
        properties.put("expectLimitedAmount", object.getExpectLimitedAmount());
        properties.put("expectEffectLimitedAmount", object.getExpectEffectLimitedAmount());
        properties.put("approvalState", object.getApprovalState());
        properties.put("remark", object.getRemark());
        properties.put("acceptExplain", object.getAcceptExplain());
        properties.put("appointExplain", object.getAppointExplain());
        properties.put("sceneInvestigationExplain", object.getSceneInvestigationExplain());
        properties.put("acceptTimeString", myDatetime.dateToStringMinute(object.getAcceptTimeStamp()));
        properties.put("appointTimeString", myDatetime.dateToStringMinute(object.getAppointTimeStamp()));
        properties.put("sceneInvestigationTimeString", myDatetime.dateToStringMinute(object.getSceneInvestigationTimeStamp()));*/


        return properties;
    }

    @SuppressWarnings({
			"rawtypes", "unchecked"
	})
    public List getDetailForAdmin(List<Empj_BldLimitAmount_AF> empj_BldLimitAmount_AFList) {
        List<Properties> list = new ArrayList<Properties>();
        if (empj_BldLimitAmount_AFList != null) {
            for (Empj_BldLimitAmount_AF object : empj_BldLimitAmount_AFList) {
                Properties properties = new MyProperties();

                properties.put("theState", object.getTheState());
                properties.put("tableId", object.getTableId());
                properties.put("busiState", object.getBusiState());
                properties.put("eCode", object.geteCode());
                Sm_User userStart = object.getUserStart();
                if (userStart != null) {
                    properties.put("userStartId", userStart.getTableId());
                }
                Emmp_CompanyInfo developCompany = object.getDevelopCompany();
                if (developCompany != null) {
                    properties.put("developCompanyId", developCompany.getTableId());
                    properties.put("developCompanyName", developCompany.getTheName());
                }

                properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
                Empj_ProjectInfo project = object.getProject();
                if (project != null) {
                    properties.put("projectId", project.getTableId());
                    properties.put("projectName", project.getTheName());
                }

                properties.put("theNameOfProject", object.getTheNameOfProject());
                properties.put("eCodeOfProject", object.geteCodeOfProject());
                Empj_BuildingInfo building = object.getBuilding();
                if (building != null) {
                    properties.put("buildingId", building.getTableId());
                    properties.put("buildingEcode", building.geteCode());
                    properties.put("buildingArea", building.getBuildingArea());
                    properties.put("escrowArea", building.getEscrowArea());
                    properties.put("eCodeOfBuilding", building.geteCodeFromPresellSystem());
                    properties.put("eCodeFromConstruction", building.geteCodeFromConstruction());
                }
                properties.put("currentFigureProgress", object.getCurrentFigureProgress()+"-"+myDouble.noDecimal(object.getCurrentLimitedRatio())+"%");
                properties.put("expectLimitedAmount", object.getExpectLimitedAmount());
                properties.put("recordAveragePriceOfBuilding", object.getRecordAveragePriceOfBuilding());


                properties.put("nodeLimitedAmount", object.getNodeLimitedAmount());
//				properties.put("expectFigureProgress", object.getExpectFigureProgress());
                Tgpj_BldLimitAmountVer_AFDtl expectFigureProgress = object.getExpectFigureProgress();
                if (expectFigureProgress != null) {
                    properties.put("expectFigureProgressId", expectFigureProgress.getTableId());
                    properties.put("stageName", expectFigureProgress.getStageName());
                    properties.put("expectFigureProgressLimitAmount", expectFigureProgress.getLimitedAmount());
                    properties.put("expectFigureProgressStageLimit", expectFigureProgress.getStageName() + "-" + myDouble.noDecimal(expectFigureProgress.getLimitedAmount())+"%");
                }
                properties.put("expectEffectLimitedAmount", object.getExpectEffectLimitedAmount());
                properties.put("approvalState", object.getApprovalState());


                //查找申请单，给审批情况用的
				Sm_ApprovalProcess_AFForm sm_approvalProcess_afForm = new Sm_ApprovalProcess_AFForm();
				sm_approvalProcess_afForm.setTheState(S_TheState.Normal);
				sm_approvalProcess_afForm.setBusiCode(S_BusiCode.busiCode_03030101);
				sm_approvalProcess_afForm.setSourceId(object.getTableId());
				
				List<Sm_ApprovalProcess_AF> afList = sm_ApprovalProcess_AFDao.findByPage(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_approvalProcess_afForm));
//				Sm_ApprovalProcess_AF sm_approvalProcess_af = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_approvalProcess_afForm));
				if(null != afList && afList.size()>0)
				{
					Sm_ApprovalProcess_AF sm_approvalProcess_af = afList.get(0);
					if (sm_approvalProcess_af != null) {
					    properties.put("afId", sm_approvalProcess_af.getTableId()); // 申请单id
						properties.put("workflowId", sm_approvalProcess_af.getCurrentIndex());//当前结点Id
						properties.put("busiType", sm_approvalProcess_af.getBusiType());  //业务类型
						properties.put("busiCode", sm_approvalProcess_af.getBusiCode()); //业务编码
					}
				}
				
//				properties.put("upfloorNumber", object.getUpfloorNumber());
//				properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
//				properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());
//
//				properties.put("escrowStandard", object.getEscrowStandard());
//				properties.put("deliveryType", object.getDeliveryType());
//				properties.put("orgLimitedAmount", object.getOrgLimitedAmount());
//
//				properties.put("currentLimitedRatio", object.getCurrentLimitedRatio());

//				properties.put("totalGuaranteeAmount", object.getTotalGuaranteeAmount());
//				properties.put("cashLimitedAmount", object.getCashLimitedAmount());
//				properties.put("effectiveLimitedAmount", object.getEffectiveLimitedAmount());

//				properties.put("expectLimitedRatio", object.getExpectLimitedRatio());


                list.add(properties);
            }
        }
        return list;
    }
    
    public List getDetailForAdmin2(List<Empj_BldLimitAmount_AF> empj_BldLimitAmount_AFList) {
        List<Properties> list = new ArrayList<Properties>();
        if (empj_BldLimitAmount_AFList != null) {
            for (Empj_BldLimitAmount_AF object : empj_BldLimitAmount_AFList) {
                Properties properties = new MyProperties();

                properties.put("theState", object.getTheState());
                properties.put("tableId", object.getTableId());
                properties.put("busiState", object.getBusiState());
                properties.put("eCode", object.geteCode());
                Sm_User userStart = object.getUserStart();
                if (userStart != null) {
                    properties.put("userStartId", userStart.getTableId());
                }
                Emmp_CompanyInfo developCompany = object.getDevelopCompany();
                if (developCompany != null) {
                    properties.put("developCompanyId", developCompany.getTableId());
                    properties.put("developCompanyName", developCompany.getTheName());
                }

                properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
                Empj_ProjectInfo project = object.getProject();
                if (project != null) {
                    properties.put("projectId", project.getTableId());
                    properties.put("projectName", project.getTheName());
                }

                properties.put("theNameOfProject", object.getTheNameOfProject());
                properties.put("eCodeOfProject", object.geteCodeOfProject());
                Empj_BuildingInfo building = object.getBuilding();
                if (building != null) {
                    properties.put("buildingId", building.getTableId());
                    properties.put("buildingEcode", building.geteCode());
                    properties.put("buildingArea", building.getBuildingArea());
                    properties.put("escrowArea", building.getEscrowArea());
                    properties.put("eCodeOfBuilding", building.geteCodeFromPresellSystem());
                    properties.put("eCodeFromConstruction", building.geteCodeFromConstruction());
                    
                    Tgpj_BldLimitAmountVer_AFDtl expectFigureProgress = object.getExpectFigureProgress();
                    
                    properties.put("currentFigureProgress", expectFigureProgress.getStageName() +"-"+myDouble.noDecimal(expectFigureProgress.getLimitedAmount())+"%");
                
                
                    Tgpj_BuildingAccount buildingAccount = building.getBuildingAccount();
                    properties.put("nodeLimitedAmount", buildingAccount.getNodeLimitedAmount());
                    properties.put("recordAveragePriceOfBuilding", buildingAccount.getRecordAvgPriceOfBuilding());
                    
                }
                
                
                /*properties.put("currentFigureProgress", object.getCurrentFigureProgress()+"-"+myDouble.noDecimal(object.getCurrentLimitedRatio())+"%");
                properties.put("expectLimitedAmount", object.getExpectLimitedAmount());
                properties.put("recordAveragePriceOfBuilding", object.getRecordAveragePriceOfBuilding());


                properties.put("nodeLimitedAmount", object.getNodeLimitedAmount());
                Tgpj_BldLimitAmountVer_AFDtl expectFigureProgress = object.getExpectFigureProgress();
                if (expectFigureProgress != null) {
                    properties.put("expectFigureProgressId", expectFigureProgress.getTableId());
                    properties.put("stageName", expectFigureProgress.getStageName());
                    properties.put("expectFigureProgressLimitAmount", expectFigureProgress.getLimitedAmount());
                    properties.put("expectFigureProgressStageLimit", expectFigureProgress.getStageName() + "-" + myDouble.noDecimal(expectFigureProgress.getLimitedAmount())+"%");
                }
                properties.put("expectEffectLimitedAmount", object.getExpectEffectLimitedAmount());*/
                properties.put("approvalState", object.getApprovalState());


                //查找申请单，给审批情况用的
				Sm_ApprovalProcess_AFForm sm_approvalProcess_afForm = new Sm_ApprovalProcess_AFForm();
				sm_approvalProcess_afForm.setTheState(S_TheState.Normal);
				sm_approvalProcess_afForm.setBusiCode(S_BusiCode.busiCode_03030101);
				sm_approvalProcess_afForm.setSourceId(object.getTableId());
				
				List<Sm_ApprovalProcess_AF> afList = sm_ApprovalProcess_AFDao.findByPage(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_approvalProcess_afForm));
				if(null != afList && afList.size()>0)
				{
					Sm_ApprovalProcess_AF sm_approvalProcess_af = afList.get(0);
					if (sm_approvalProcess_af != null) {
					    properties.put("afId", sm_approvalProcess_af.getTableId()); // 申请单id
						properties.put("workflowId", sm_approvalProcess_af.getCurrentIndex());//当前结点Id
						properties.put("busiType", sm_approvalProcess_af.getBusiType());  //业务类型
						properties.put("busiCode", sm_approvalProcess_af.getBusiCode()); //业务编码
					}
				}

                list.add(properties);
            }
        }
        return list;
    }
}
