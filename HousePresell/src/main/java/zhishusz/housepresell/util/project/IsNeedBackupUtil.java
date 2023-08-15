package zhishusz.housepresell.util.project;

import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.state.S_IsNeedBackup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * Created by Dechert on 2018/12/5.
 * Company: www.chisalsoft.
 * Usage: 是否需要备案的方法
 */
@Service
public class IsNeedBackupUtil {
    @Autowired
    private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
    @Autowired
    private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;

    private boolean isNeedBackup(Sm_ApprovalProcess_AF sm_ApprovalProcess_AF) {
//        Sm_ApprovalProcess_AFForm sm_ApprovalProcess_AFForm = new Sm_ApprovalProcess_AFForm();
//        sm_ApprovalProcess_AFForm.setTheState(S_TheState.Normal);
////        sm_ApprovalProcess_AFForm.setBusiState("待提交");
//        sm_ApprovalProcess_AFForm.setBusiCode(busiCode);
//        sm_ApprovalProcess_AFForm.setSourceId(tableId);
//        Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));
        if (sm_ApprovalProcess_AF == null) {
            return false;
        }
        Long currentNode = sm_ApprovalProcess_AF.getCurrentIndex();
        Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = sm_approvalProcess_workflowDao
                .findById(currentNode);
        boolean isNeedBackup = false;
        if (sm_approvalProcess_workflow.getNextWorkFlow() == null) {
            if (S_IsNeedBackup.Yes.equals(sm_ApprovalProcess_AF.getIsNeedBackup())) {
                isNeedBackup = true;
            } else {
                isNeedBackup = false;
            }
        } else {
            isNeedBackup = false;
        }
        return isNeedBackup;
    }

    public void setIsNeedBackup(Properties properties, Sm_ApprovalProcess_AF sm_ApprovalProcess_AF) {
        if (isNeedBackup(sm_ApprovalProcess_AF)) {
            properties.put("isNeedBackup", true);
        } else {
            properties.put("isNeedBackup", false);
        }
    }


}
