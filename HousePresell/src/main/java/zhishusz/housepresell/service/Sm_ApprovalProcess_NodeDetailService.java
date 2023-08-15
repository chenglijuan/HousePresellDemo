package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_NodeForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_NodeDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Node;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

import java.util.List;

/*
 * Service详情：审批流-节点
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_NodeDetailService
{
	@Autowired
	private Sm_ApprovalProcess_NodeDao sm_ApprovalProcess_NodeDao;

	public Properties execute(Sm_ApprovalProcess_NodeForm model)
	{
		Properties properties = new MyProperties();

		Long sm_ApprovalProcess_NodeId = model.getTableId();
		Sm_ApprovalProcess_Node sm_ApprovalProcess_Node = (Sm_ApprovalProcess_Node)sm_ApprovalProcess_NodeDao.findById(sm_ApprovalProcess_NodeId);
		if(sm_ApprovalProcess_Node == null || S_TheState.Deleted.equals(sm_ApprovalProcess_Node.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Sm_ApprovalProcess_Node(Id:" + sm_ApprovalProcess_NodeId + ")'不存在");
		}

		List<Sm_ApprovalProcess_Condition> sm_approvalProcess_conditionList;
		if(sm_ApprovalProcess_Node.getApprovalProcess_conditionList() !=null)
		{
			sm_approvalProcess_conditionList = sm_ApprovalProcess_Node.getApprovalProcess_conditionList();
		}
		else
		{
			sm_approvalProcess_conditionList = new ArrayList<>();
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("sm_ApprovalProcess_Node", sm_ApprovalProcess_Node);
		properties.put("sm_approvalProcess_conditionList", sm_approvalProcess_conditionList);

		return properties;
	}
}
