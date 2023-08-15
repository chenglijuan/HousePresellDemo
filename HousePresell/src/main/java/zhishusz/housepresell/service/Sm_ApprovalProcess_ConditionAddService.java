package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_ConditionForm;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_NodeForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_ConditionDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_NodeDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Condition;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Node;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service添加操作：审批流-节点
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_ConditionAddService
{
	@Autowired
	private Sm_ApprovalProcess_NodeDao sm_approvalProcess_nodeDao;
	@Autowired
	private Sm_ApprovalProcess_ConditionDao sm_approvalProcess_conditionDao;
	@Autowired
	private Sm_UserDao sm_userDao;

	public Properties execute(Sm_ApprovalProcess_ConditionForm model)
	{
		Properties properties = new MyProperties();


		String theContent = model.getTheContent(); //条件内容
		Long nextStep = model.getNextStep(); //下一步骤

		if(theContent == null || theContent.length() == 0)
		{
			return MyBackInfo.fail(properties, "'条件内容'不能为空");
		}
		if(nextStep == null || nextStep  < 1)
		{
			return MyBackInfo.fail(properties, "'下一步骤'不能为空");
		}

		Sm_ApprovalProcess_Node sm_approvalProcess_node = sm_approvalProcess_nodeDao.findById(nextStep);
		if(sm_approvalProcess_node == null)
		{
			return MyBackInfo.fail(properties, "'结点'不能为空");
		}
		String nextStepName = sm_approvalProcess_node.getTheName();
		if(nextStepName == null || nextStepName.length() == 0)
		{
			return MyBackInfo.fail(properties, "'结点名称'不能为空");
		}

		Sm_ApprovalProcess_Condition sm_approvalProcess_condition = new Sm_ApprovalProcess_Condition();
		sm_approvalProcess_condition.setTheState(S_TheState.Normal);
		sm_approvalProcess_condition.setTemporaryState(S_TheState.Deleted);
		sm_approvalProcess_condition.setTheContent(theContent); //条件内容
		sm_approvalProcess_condition.setNextStep(nextStep); //下一步骤
		sm_approvalProcess_condition.setNextStepName(nextStepName);//下一步骤名称
		sm_approvalProcess_condition.setCreateTimeStamp(System.currentTimeMillis());
		sm_approvalProcess_condition.setLastUpdateTimeStamp(System.currentTimeMillis());

		properties.put("sm_approvalProcess_condition",sm_approvalProcess_condition);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
