package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_WorkflowForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：审批流-审批流程
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_WorkflowAddService
{
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_ApprovalProcess_WorkflowDao;
	
	public Properties execute(Sm_ApprovalProcess_WorkflowForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = model.getTheState();
		String busiState = model.getBusiState();
		Integer orderNumber = model.getOrderNumber();
		Integer lastAction = model.getLastAction();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(orderNumber == null || orderNumber < 1)
		{
			return MyBackInfo.fail(properties, "'orderNumber'不能为空");
		}
		if(lastAction == null || lastAction < 0)
		{
			return MyBackInfo.fail(properties, "'lastAction'不能为空");
		}

	
		Sm_ApprovalProcess_Workflow sm_ApprovalProcess_Workflow = new Sm_ApprovalProcess_Workflow();
		sm_ApprovalProcess_Workflow.setTheState(theState);
		sm_ApprovalProcess_Workflow.setBusiState(busiState);
		sm_ApprovalProcess_Workflow.setLastAction(lastAction);
		sm_ApprovalProcess_WorkflowDao.save(sm_ApprovalProcess_Workflow);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
