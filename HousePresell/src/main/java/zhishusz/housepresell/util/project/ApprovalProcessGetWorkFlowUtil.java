package zhishusz.housepresell.util.project;

import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dechert on 2018/11/29.
 * Company: zhishusz
 * Usage:
 */
@Service
public class ApprovalProcessGetWorkFlowUtil {
    @Autowired
    private Sm_ApprovalProcess_WorkflowDao workflowDao;

    public Integer getWorkFlowNowIndex(Sm_ApprovalProcess_AF approvalProcess_af){
        Long currentIndex = approvalProcess_af.getCurrentIndex();
        List<Sm_ApprovalProcess_Workflow> workFlowList = approvalProcess_af.getWorkFlowList();
        int index=-1;
        for (int i = 0; i < workFlowList.size(); i++) {
            Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = workFlowList.get(i);
            if(sm_approvalProcess_workflow.getTableId().equals(currentIndex)){
                index=i;
                break;
            }
        }
        if(index==-1){
            return null;
        }
        return index;
    }

    public Sm_ApprovalProcess_Workflow getNowWorkFlow(Sm_ApprovalProcess_AF approvalProcess_af){
        Long currentIndex = approvalProcess_af.getCurrentIndex();
        Sm_ApprovalProcess_Workflow nowWorkflow = workflowDao.findById(currentIndex);
        return nowWorkflow;
    }

    public String getNowWorkFlowName(Sm_ApprovalProcess_AF approvalProcess_af){
        Sm_ApprovalProcess_Workflow nowWorkFlow = getNowWorkFlow(approvalProcess_af);
        if (nowWorkFlow != null) {
            return nowWorkFlow.geteCode();
        }else{
            return null;
        }
    }


}
