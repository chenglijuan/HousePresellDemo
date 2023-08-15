package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_WorkflowForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：审批流-审批流程
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_WorkflowBatchDeleteService
{
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_ApprovalProcess_WorkflowDao;

	public Properties execute(Sm_ApprovalProcess_WorkflowForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Sm_ApprovalProcess_Workflow sm_ApprovalProcess_Workflow = (Sm_ApprovalProcess_Workflow)sm_ApprovalProcess_WorkflowDao.findById(tableId);
			if(sm_ApprovalProcess_Workflow == null)
			{
				return MyBackInfo.fail(properties, "'Sm_ApprovalProcess_Workflow(Id:" + tableId + ")'不存在");
			}
		
			sm_ApprovalProcess_Workflow.setTheState(S_TheState.Deleted);
			sm_ApprovalProcess_WorkflowDao.save(sm_ApprovalProcess_Workflow);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
