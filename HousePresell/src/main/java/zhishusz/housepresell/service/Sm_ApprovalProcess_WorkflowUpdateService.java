package zhishusz.housepresell.service;

import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_WorkflowForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：审批流-审批流程
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_WorkflowUpdateService
{
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_ApprovalProcess_WorkflowDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Sm_ApprovalProcess_WorkflowForm model)
	{
		Properties properties = new MyProperties();


		Long userStartId = model.getUserId();
		if (userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "'登录用户'不能为空");
		}
		Sm_User userStart = (Sm_User) sm_UserDao.findById(userStartId);
		if (userStart == null)
		{
			return MyBackInfo.fail(properties, "'登录用户'不能为空");
		}

		Long sm_ApprovalProcess_WorkflowId = model.getTableId();
		Sm_ApprovalProcess_Workflow sm_ApprovalProcess_Workflow = (Sm_ApprovalProcess_Workflow)sm_ApprovalProcess_WorkflowDao.findById(sm_ApprovalProcess_WorkflowId);
		if(sm_ApprovalProcess_Workflow == null)
		{
			return MyBackInfo.fail(properties, "'审批节点(Id:" + sm_ApprovalProcess_WorkflowId + ")'不存在");
		}

		//上传附件更新结点信息，在待办流程列表页面显示当前处理人信息
		sm_ApprovalProcess_Workflow.setUserOperate(userStart);
		sm_ApprovalProcess_Workflow.setOperateTimeStamp(System.currentTimeMillis());

		sm_ApprovalProcess_WorkflowDao.save(sm_ApprovalProcess_Workflow);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
