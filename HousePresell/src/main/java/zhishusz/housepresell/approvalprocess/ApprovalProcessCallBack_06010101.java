package zhishusz.housepresell.approvalprocess;

import java.util.Properties;

import zhishusz.housepresell.database.po.state.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Tgpj_EscrowStandardVerMngForm;
import zhishusz.housepresell.database.dao.Tgpj_EscrowStandardVerMngDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;
import zhishusz.housepresell.service.Sm_AttachmentBatchAddService;
import zhishusz.housepresell.service.Sm_BusiState_LogAddService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import com.google.gson.Gson;

/**
 * 托管标准版本管理：
 * 审批过后-业务逻辑处理
 */
@Transactional
public class ApprovalProcessCallBack_06010101 implements IApprovalProcessCallback
{
    private static final String BUSI_CODE = "06010101";//具体业务编码参看SVN文

    @Autowired
    private Tgpj_EscrowStandardVerMngDao tgpj_escrowStandardVerMngDao;
    @Autowired
    private Gson gson;
    @Autowired
    private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
    @Autowired
    private Sm_BusiState_LogAddService logAddService;

    @Override
    public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm)
    {
        Properties properties = new MyProperties();

        try {
            String workflowEcode = approvalProcessWorkflow.geteCode();

            // 获取正在处理的申请单
            Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();

            // 获取正在处理的申请单所属的流程配置
            Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = sm_ApprovalProcess_AF.getConfiguration();
            String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;

            // 获取正在审批的项目
            Long tgpj_escrowStandardVerMngId = sm_ApprovalProcess_AF.getSourceId();
            Tgpj_EscrowStandardVerMng tgpj_escrowStandardVerMng = tgpj_escrowStandardVerMngDao.findById(tgpj_escrowStandardVerMngId);

            if (tgpj_escrowStandardVerMng == null) {
                return MyBackInfo.fail(properties, "审批的托管标准版本不存在");
            }
//            Empj_BldEscrowCompleted empj_bldEscrowCompletedOld = ObjectCopier.copy(empj_bldEscrowCompleted);

            switch (approvalProcessWork) {
                case S_BusiCode.busiCode_06010101+"001_ZS":
                    if (S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState()) && S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState())) {
                        String jsonStr = sm_ApprovalProcess_AF.getExpectObjJson();
                        if (jsonStr != null && jsonStr.length() > 0) {
                            Tgpj_EscrowStandardVerMngForm tgpj_escrowStandardVerMngForm = gson.fromJson(jsonStr,
                                    Tgpj_EscrowStandardVerMngForm.class);

                            tgpj_escrowStandardVerMng.setTheState(S_TheState.Normal);
                            tgpj_escrowStandardVerMng.setUserRecord(approvalProcessWorkflow.getUserUpdate());
                            tgpj_escrowStandardVerMng.setRecordTimeStamp(System.currentTimeMillis());
                            tgpj_escrowStandardVerMng.setBusiState(S_BusiState.HaveRecord); //已备案
                            tgpj_escrowStandardVerMng.setApprovalState(S_ApprovalState.Completed); //已完结
                            tgpj_escrowStandardVerMngDao.save(tgpj_escrowStandardVerMng);

                            tgpj_escrowStandardVerMngForm.setBusiType(BUSI_CODE);
                            sm_AttachmentBatchAddService.execute(tgpj_escrowStandardVerMngForm, tgpj_escrowStandardVerMngId);

//                            if(baseForm != null)
//                            {
//                                baseForm.setUser(sm_ApprovalProcess_AF.getUserStart());
//                                Empj_BldEscrowCompleted empj_bldEscrowCompletedNew = ObjectCopier.copy(empj_bldEscrowCompleted);
//                                logAddService.addLog(baseForm, empj_BldEscrowCompletedId, empj_bldEscrowCompletedOld, empj_bldEscrowCompletedNew);
//                            }

                        }
                    }break;
                default:
                    properties.put(S_NormalFlag.result, S_NormalFlag.success);
                    properties.put(S_NormalFlag.info, "没有需要处理的回调");
            }
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            properties.put(S_NormalFlag.result, S_NormalFlag.fail);
            properties.put(S_NormalFlag.info, S_NormalFlag.info_BusiError);
        }

        return properties;
    }
}
