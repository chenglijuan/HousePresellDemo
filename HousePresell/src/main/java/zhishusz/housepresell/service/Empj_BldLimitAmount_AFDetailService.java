package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDtlDao;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.extra.FrontEdgeControlModel;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_IsNeedBackup;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.ApprovalProcessGetWorkFlowUtil;
import zhishusz.housepresell.util.project.EscrowStandardUtil;
import zhishusz.housepresell.util.project.IsNeedBackupUtil;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service详情：申请表-受限额度变更
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BldLimitAmount_AFDetailService {
    @Autowired
    private Empj_BldLimitAmount_AFDao dao;
    @Autowired
    private Tgpj_BldLimitAmountVer_AFDtlDao tgpj_bldLimitAmountVer_afDtlDao;
    @Autowired
    private Gson gson;
    @Autowired
    private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;
    @Autowired
    private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
    @Autowired
    private Sm_BaseParameterGetService baseParameterGetService;
    @Autowired
    private EscrowStandardUtil escrowStandardUtil;
    @Autowired
    private Empj_BldGetLimitAmountVerService bldGetLimitAmountVerService;
    @Autowired
    private ApprovalProcessGetWorkFlowUtil approvalProcessGetWorkFlowUtil;
    @Autowired
    private Empj_BldAccountGetLimitAmountVerService versionListService;
    @Autowired
    private IsNeedBackupUtil isNeedBackupUtil;
    private MyDatetime myDatetime = MyDatetime.getInstance();
    //    private String isNeedAcceptExplain = "isNeedAcceptExplain";
//    private String isNeedAppointExplain = "isNeedAppointExplain";
//    private String isNeedSceneInvestigationExplain = "isNeedSceneInvestigationExplain";
//    private String isNeedUploadMaterial = "isNeedUploadMaterial";
    private FrontEdgeControlModel acceptExplainControlModel = new FrontEdgeControlModel();
    private FrontEdgeControlModel appointExplainControlModel = new FrontEdgeControlModel();
    private FrontEdgeControlModel sceneInvestigationExplainControlModel = new FrontEdgeControlModel();

    @SuppressWarnings("unchecked")
	public Properties execute(Empj_BldLimitAmount_AFForm model) {
        model.setTheState(S_TheState.Normal);
        Properties properties = new MyProperties();

        Long empj_BldLimitAmount_AFId = model.getTableId();
        Empj_BldLimitAmount_AF object = (Empj_BldLimitAmount_AF) dao.findById(empj_BldLimitAmount_AFId);
        if (object == null) {
            return MyBackInfo.fail(properties, "'Empj_BldLimitAmount_AF(Id:" + empj_BldLimitAmount_AFId + ")'不存在");
        }
//		List<Empj_BldLimitAmount_AF> byPage = dao.findByPage(dao.getQuery(dao.getBasicHQL(), model));
//		if(byPage.size()==0 || byPage.get(0)==null){
//			return MyBackInfo.fail(properties, "'Empj_BldLimitAmount_AF(Id:" + empj_BldLimitAmount_AFId + ")'不存在");
//		}
//		if(empj_BldLimitAmount_AF == null || S_TheState.Deleted.equals(empj_BldLimitAmount_AF.getTheState()))
//		{
//			return MyBackInfo.fail(properties, "'Empj_BldLimitAmount_AF(Id:" + empj_BldLimitAmount_AFId + ")'不存在");
//		}
//		Empj_BldLimitAmount_AF object = byPage.get(0);
//		System.out.println("object DeliveryType is "+ object.getDeliveryType());
//		Sm_BaseParameter parameter = baseParameterGetService.getParameter("5", object.getDeliveryType());
//		object.setDeliveryType(parameter.getTheName());
        Empj_BuildingInfo building = object.getBuilding();
        MyProperties newProperties = new MyProperties();
        Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = null;
        if (!"详情".equals(model.getReqSource())) {
            //查待提交的申请单
            Sm_ApprovalProcess_AFForm sm_ApprovalProcess_AFForm = new Sm_ApprovalProcess_AFForm();
            sm_ApprovalProcess_AFForm.setTheState(S_TheState.Normal);
            sm_ApprovalProcess_AFForm.setBusiState("待提交");
            sm_ApprovalProcess_AFForm.setBusiCode(S_BusiCode.busiCode_03030101);
            sm_ApprovalProcess_AFForm.setSourceId(object.getTableId());
            
            List<Sm_ApprovalProcess_AF> afList = sm_ApprovalProcess_AFDao.findByPage(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));
//            sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));
            boolean isHasApproveProcess = true;
            if(null != afList && afList.size()>0)
			{
            	Long afId = model.getAfId();
            	
            	if(null != afId)
            	{
            		for ( int i = 0;i<afList.size();i++)
    				{
            			if(afList.get(i).getTableId().equals(afId))
            			{
            				sm_ApprovalProcess_AF = afList.get(i);
            				break;
            			}
    				}
            	}
            	
            	if(null == sm_ApprovalProcess_AF)
            	{
            		isHasApproveProcess = false;
            	}
            	
//            	sm_ApprovalProcess_AF = afList.get(0);
//				if (sm_ApprovalProcess_AF != null) {
//				    properties.put("afId", sm_approvalProcess_af.getTableId()); // 申请单id
//					properties.put("workflowId", sm_approvalProcess_af.getCurrentIndex());//当前结点Id
//					properties.put("busiType", sm_approvalProcess_af.getBusiType());  //业务类型
//					properties.put("busiCode", sm_approvalProcess_af.getBusiCode()); //业务编码
//				}
			}
			else
			{
				isHasApproveProcess = false;
			}
            
//            if (sm_ApprovalProcess_AF == null) {
//            	isHasApproveProcess = false;
////				sm_ApprovalProcess_AFForm.setBusiState("审核中");
//                //				sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));
//                //
//                //				if(sm_ApprovalProcess_AF == null)
//                //				{
//                //					return properties;
//                //				}
//            }
            if (isHasApproveProcess) {
                Long currentNode = sm_ApprovalProcess_AF.getCurrentIndex();
                Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = sm_approvalProcess_workflowDao
                        .findById(currentNode);
//                Boolean isNeedBackup = null;
                if (sm_approvalProcess_workflow.getNextWorkFlow() == null) {
                    if (S_IsNeedBackup.Yes.equals(sm_ApprovalProcess_AF.getIsNeedBackup())) {
//                        isNeedBackup = true;
                    }
                } else {
//                    isNeedBackup = false;
                }
//                properties.put("isNeedBackup", isNeedBackup);//是否显示备案按钮
                String jsonNewStr = sm_ApprovalProcess_AF.getExpectObjJson();
                if (jsonNewStr != null && jsonNewStr.length() > 0) {
                    Empj_BldLimitAmount_AFForm form = gson.fromJson(jsonNewStr, Empj_BldLimitAmount_AFForm.class);
                    Long expectFigureProgressId = form.getExpectFigureProgressId();
                    newProperties.put("expectFigureProgressId", expectFigureProgressId);
                    Tgpj_BldLimitAmountVer_AFDtl afDtl = tgpj_bldLimitAmountVer_afDtlDao
                            .findById(expectFigureProgressId);
                    if (afDtl != null) {
                        String stageName = afDtl.getStageName();
                        Double limitedAmount = afDtl.getLimitedAmount();
                        String expectFigureProgressStageLimit = stageName + "-" + limitedAmount;
                        newProperties.put("expectFigureProgressStageLimit", expectFigureProgressStageLimit);
                    }
                    newProperties.put("remark", form.getRemark());
                }
            }
        }
        Tgpj_BuildingAccount buildingAccount = building.getBuildingAccount();
        if (buildingAccount != null) {
            Double currentLimitedRatio = buildingAccount.getCurrentLimitedRatio();
            
            if (currentLimitedRatio != null) {
                Empj_BuildingInfoForm empj_buildingInfoForm = new Empj_BuildingInfoForm();
                empj_buildingInfoForm.setTheState(S_TheState.Normal);
                empj_buildingInfoForm.setTableId(building.getTableId());
                empj_buildingInfoForm.setNowLimitRatio(buildingAccount.getCurrentLimitedRatio() + "");
//                Properties execute = bldGetLimitAmountVerService.execute(empj_buildingInfoForm);
                Properties execute = versionListService.execute(empj_buildingInfoForm);
                properties.put("versionList", execute.get("versionList"));
            }
        }
        String escrowStandardTypeName = escrowStandardUtil.getEscrowStandardTypeName(building);
        if (model.getSourcePage() != null) {
            if (model.getSourcePage().equals(1)) {
                System.out.println("代办流程");
                judgeWhichNeedInput(object, properties);
            } else {
                System.out.println("我发起的/已办");
                setDisableCase(properties, true, true);
            }
        }
        if (model.getAfId() != null) {
            sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findById(model.getAfId());
            isNeedBackupUtil.setIsNeedBackup(properties, sm_ApprovalProcess_AF);
        }

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        properties.put("empj_BldLimitAmount_AF", object);
        properties.put("empj_BldLimitAmount_AFNew", newProperties);
        properties.put("trusteeshipContent", escrowStandardTypeName);

        return properties;
    }

    /**
     * 用于返回给前端，哪些框是必须输入的，哪些是禁用的
     *
     * @param object
     * @param properties
     */
    private void judgeWhichNeedInput(Empj_BldLimitAmount_AF object, Properties properties) {
        Sm_ApprovalProcess_AFForm sm_ApprovalProcess_AFForm = new Sm_ApprovalProcess_AFForm();
        sm_ApprovalProcess_AFForm.setTheState(S_TheState.Normal);
        sm_ApprovalProcess_AFForm.setBusiState(S_ApprovalState.Examining);
        sm_ApprovalProcess_AFForm.setBusiCode(S_BusiCode.busiCode_03030101);
        sm_ApprovalProcess_AFForm.setSourceId(object.getTableId());
        Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = null;
        List<Sm_ApprovalProcess_AF> afList = sm_ApprovalProcess_AFDao.findByPage(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));
//        Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));
	       if(null == afList || afList.size() == 0)
	       {
	    	   return;
	       }
	       else
	       {
	    	   sm_ApprovalProcess_AF =  afList.get(0);
	    	   if (sm_ApprovalProcess_AF == null) {
	               return;
	           }
	       }
        
        Long startTimeStamp = sm_ApprovalProcess_AF.getStartTimeStamp();
        System.out.println("startTimeStamp is " + startTimeStamp + " nowTime is " + System.currentTimeMillis());
//        Integer workFlowNowIndex = approvalProcessGetWorkFlowUtil.getWorkFlowNowIndex(sm_ApprovalProcess_AF);
//        if (workFlowNowIndex == null) {
//            return;
//        }
        String nowWorkFlowName = approvalProcessGetWorkFlowUtil.getNowWorkFlowName(sm_ApprovalProcess_AF);
        if(nowWorkFlowName==null){
            return;
        }
        System.out.println("nowWorkFlowName is "+nowWorkFlowName);


//        int index = workFlowNowIndex;
        long interval = System.currentTimeMillis() - startTimeStamp;//与提交时间相减的间隔时间
        System.out.println("interval time is " + interval);
//        properties.put(isNeedUploadMaterial, false);
        switch (nowWorkFlowName) {
//            case 0://我发起的
//                System.out.println("in case 0");
//                setDisableCase(properties, true, true);
//                properties.put(isNeedAcceptExplain, false);
//                properties.put(isNeedAppointExplain, false);
//                properties.put(isNeedAppointExplain, false);
//                break;
            case "CS"://初审，受理和预约
                System.out.println("in case 1");
                setDisableCase(properties, false, true);
                break;
            case "TK"://探勘，现场勘查
                System.out.println("in case 2");
                setDisableCase(properties, true, false);
                break;
            case "FH"://复合
//                System.out.println("in case 3");
                setDisableCase(properties, true, true);
                break;
            default:
                System.out.println("in default");
                setDisableCase(properties, true, true);
                break;
        }

    }

    private void setDisableCase(Properties properties, boolean acceptDisable, boolean sceneInvestigationDisable) {
        acceptExplainControlModel.setNeedDisable(acceptDisable);
        acceptExplainControlModel.setNeedInput(false);
        properties.put("acceptExplainControlModel", acceptExplainControlModel);
        appointExplainControlModel.setNeedDisable(acceptDisable);
        appointExplainControlModel.setNeedInput(false);
        properties.put("appointExplainControlModel", appointExplainControlModel);
        sceneInvestigationExplainControlModel.setNeedDisable(sceneInvestigationDisable);
        sceneInvestigationExplainControlModel.setNeedInput(false);
        properties.put("sceneInvestigationExplainControlModel", sceneInvestigationExplainControlModel);
    }
}
