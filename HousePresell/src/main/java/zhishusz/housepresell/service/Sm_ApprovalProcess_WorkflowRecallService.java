package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_RecordForm;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_WorkflowForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_RecordDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Record;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Properties;

/**
 * 审批流程 --- 审核：撤回
 * 
 * @author Glad.Wang
 *
 */
@Service
@Transactional
public class Sm_ApprovalProcess_WorkflowRecallService
{
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;
	@Autowired
	private Sm_ApprovalProcess_RecordDao sm_ApprovalProcess_RecordDao;
	@Autowired
	private Sm_UserDao sm_userDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Sm_ApprovalProcess_WorkflowForm model)
	{
		Properties properties = new MyProperties();

		Long userStartId = model.getUserId();
		if(userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "登录用户不能为空");
		}
		Sm_User sm_user = sm_userDao.findById(userStartId);
		if(sm_user == null)
		{
			return MyBackInfo.fail(properties, "登录用户不能为空");
		}

		Long workflowId = model.getTableId();
		if(workflowId == null || workflowId < 1)
		{
			return MyBackInfo.fail(properties, "结点不存在");
		}
		Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = sm_approvalProcess_workflowDao.findById(workflowId);//当前结点
		if(sm_approvalProcess_workflow == null)
		{
			return MyBackInfo.fail(properties, "结点不存在");
		}

		if(sm_approvalProcess_workflow.getApprovalProcess_AF() == null)
		{
			return MyBackInfo.fail(properties, "申请单不能为空");
		}
		Sm_ApprovalProcess_AF sm_approvalProcess_af = sm_approvalProcess_workflow.getApprovalProcess_AF();

		if(sm_approvalProcess_af.getUserUpdate() == null)
		{
			return MyBackInfo.fail(properties, "发起人不能为空");
		}

		Long sendId = sm_approvalProcess_workflow.getSendId(); //发送结点

		Sm_ApprovalProcess_Workflow sendWorkflow = sm_approvalProcess_workflowDao.findById(sendId);//下一个环节

		if(sendWorkflow == null)
		{
			return MyBackInfo.fail(properties, "该节点无可撤回节点");
		}
		
		Sm_ApprovalProcess_RecordForm recordForm = new Sm_ApprovalProcess_RecordForm();
		recordForm.setTheState(S_TheState.Normal);
		recordForm.setApprovalProcessId(sendId);
		if(sm_approvalProcess_workflow.getLastUpdateTimeStamp() !=null)//当前节点的通过时间
		{
			recordForm.setWorkflowTime(sm_approvalProcess_workflow.getLastUpdateTimeStamp());
		}
		//查找的是当前节点通过后，针对下一个环节操作的记录
		List<Sm_ApprovalProcess_Record> sm_approvalProcess_recordList = sm_ApprovalProcess_RecordDao.findByPage(sm_ApprovalProcess_RecordDao.getQuery(sm_ApprovalProcess_RecordDao.getBasicHQL(), recordForm));

		//下一个环节是审核中并且没有审批操作记录可以做撤回操作
		if(S_ApprovalState.Examining.equals(sendWorkflow.getBusiState()) && (sm_approvalProcess_recordList == null || sm_approvalProcess_recordList.isEmpty()))
		{
			sm_approvalProcess_workflow.setBusiState(S_WorkflowBusiState.Examining);
			sm_approvalProcess_workflow.setSendId(null);
			sendWorkflow.setBusiState(S_WorkflowBusiState.WaitSubmit);
			sendWorkflow.setSourceId(null);
			sm_approvalProcess_af.setCurrentIndex(workflowId);
		}
		else
		{
			return MyBackInfo.fail(properties, "下一个结点已经审核，不可撤回");
		}

		sm_approvalProcess_workflowDao.save(sm_approvalProcess_workflow);
		sm_approvalProcess_workflowDao.save(sendWorkflow);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
