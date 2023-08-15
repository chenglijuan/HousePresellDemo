package zhishusz.housepresell.approvalprocess;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountLogDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.service.Sm_AttachmentBatchAddService;
import zhishusz.housepresell.service.Sm_BusiState_LogAddService;
import zhishusz.housepresell.service.Tgpj_BuildingAccountLimitedUpdateService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.ObjectCopier;
import zhishusz.housepresell.util.project.BuildingAccountLogUtil;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

/**
 * 备案均价：
 * 审批过后-业务逻辑处理
 */
@Transactional
public class ApprovalProcessCallBack_03010302 implements IApprovalProcessCallback {
    @Autowired
    private Tgpj_BuildingAvgPriceDao tgpj_BuildingAvgPriceDao;
    @Autowired
    private Gson gson;
    @Autowired
    private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
    @Autowired
    private Sm_BusiState_LogAddService logAddService;
    @Autowired
    private Tgpj_BuildingAccountLogDao tgpj_BuildingAccountLogDao;
    @Autowired
    private Tgpj_BuildingAccountLimitedUpdateService tgpj_BuildingAccountLimitedUpdateService;
    @Autowired
    private BuildingAccountLogUtil buildingAccountLogUtil;
    private String busiCode = "03010302";

    public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm) {
        Properties properties = new MyProperties();

        try {
            String workflowEcode = approvalProcessWorkflow.geteCode();//节点编码

            // 获取正在处理的申请单
            Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();

            // 获取正在处理的申请单所属的流程配置
            Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = sm_ApprovalProcess_AF.getConfiguration();
            String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;//流程编码加节点编码

            // 获取正在审批的楼幢
            Long tgpj_BuildingAvgPriceId = sm_ApprovalProcess_AF.getSourceId();
            Tgpj_BuildingAvgPrice tgpj_BuildingAvgPrice = tgpj_BuildingAvgPriceDao.findById(tgpj_BuildingAvgPriceId);

//			tgpj_BuildingAvgPriceDao.clear(tgpj_BuildingAvgPrice);

            if (tgpj_BuildingAvgPrice == null) {
                return MyBackInfo.fail(properties, "审批的楼幢不存在");
            }

            Tgpj_BuildingAvgPrice tgpj_BuildingAvgPriceOld = ObjectCopier.copy(tgpj_BuildingAvgPrice);

            switch (approvalProcessWork) {
                case "03010302001_ZS":
                    if (S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState()) && S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState())) {

                        String jsonStr = sm_ApprovalProcess_AF.getExpectObjJson();
                        if (jsonStr != null && jsonStr.length() > 0) {
                            Tgpj_BuildingAvgPriceForm tgpj_BuildingAvgPriceForm = gson.fromJson(jsonStr, Tgpj_BuildingAvgPriceForm.class);

                            tgpj_BuildingAvgPrice.setApprovalState(S_ApprovalState.Completed);
                            tgpj_BuildingAvgPrice.setUserUpdate(sm_ApprovalProcess_AF.getUserStart());
                            tgpj_BuildingAvgPrice.setLastUpdateTimeStamp(System.currentTimeMillis());
                            tgpj_BuildingAvgPrice.setUserRecord(baseForm.getUser());
                            tgpj_BuildingAvgPrice.setRecordTimeStamp(System.currentTimeMillis());
                            tgpj_BuildingAvgPrice.setRecordAveragePrice(tgpj_BuildingAvgPriceForm.getRecordAveragePrice());
                            tgpj_BuildingAvgPrice.setRemark(tgpj_BuildingAvgPriceForm.getRemark());
                            tgpj_BuildingAvgPrice.setBusiState(S_BusiState.HaveRecord);
                            tgpj_BuildingAvgPriceDao.save(tgpj_BuildingAvgPrice);
                            tgpj_BuildingAvgPriceForm.setBusiType(busiCode);
                            sm_AttachmentBatchAddService.execute(tgpj_BuildingAvgPriceForm, tgpj_BuildingAvgPriceId);


                            Tgpj_BuildingAvgPrice tgpj_BuildingAvgPriceNew = ObjectCopier.copy(tgpj_BuildingAvgPrice);
                            if (baseForm != null) {
                                baseForm.setUser(sm_ApprovalProcess_AF.getUserStart());
                                logAddService.addLog(baseForm, tgpj_BuildingAvgPriceId, tgpj_BuildingAvgPriceOld, tgpj_BuildingAvgPriceNew);
                            }
//
                            //回显数据到楼幢账户表 开始
                            Empj_BuildingInfo buildingInfo = tgpj_BuildingAvgPrice.getBuildingInfo();
                            if (buildingInfo != null) {
                                buildingAccountLogUtil.callBackChange(buildingInfo,properties,tgpj_BuildingAvgPrice.getTableId(),busiCode,(tgpj_BuildingAccountLog, accountLogForm) -> {
                                    tgpj_BuildingAccountLog.setRecordAvgPriceOfBuilding(tgpj_BuildingAvgPriceForm.getRecordAveragePrice());
                                });
                            }
                        }
                    }break;
                default:
                    properties.put(S_NormalFlag.result, S_NormalFlag.success);
                    properties.put(S_NormalFlag.info, "没有需要处理的回调");
                    ;
            }
        } catch (Exception e) {
        	
        	e.printStackTrace();
        	
            properties.put(S_NormalFlag.result, S_NormalFlag.fail);
            properties.put(S_NormalFlag.info, S_NormalFlag.info_BusiError);
            
        }

        return properties;
    }
}
