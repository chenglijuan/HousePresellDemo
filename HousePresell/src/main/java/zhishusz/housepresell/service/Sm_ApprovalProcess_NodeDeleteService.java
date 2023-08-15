package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_CfgDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_ConditionDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
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

/*
 * Service单个删除：审批流-节点
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_NodeDeleteService
{
	@Autowired
	private Sm_ApprovalProcess_CfgDao sm_approvalProcess_cfgDao;
	@Autowired
	private Sm_ApprovalProcess_NodeDao sm_ApprovalProcess_NodeDao;
	@Autowired
	private Sm_ApprovalProcess_ConditionDao sm_approvalProcess_conditionDao;

	public Properties execute(Sm_ApprovalProcess_NodeForm model)
	{
		Properties properties = new MyProperties();

		Long sm_ApprovalProcess_NodeId = model.getTableId();
		Sm_ApprovalProcess_Node sm_ApprovalProcess_Node = (Sm_ApprovalProcess_Node)sm_ApprovalProcess_NodeDao.findById(sm_ApprovalProcess_NodeId);
		if(sm_ApprovalProcess_Node == null)
		{
			return MyBackInfo.fail(properties, "节点不存在");
		}

		if(sm_ApprovalProcess_Node.getApprovalProcess_conditionList().size() > 0)
		{
			for(Sm_ApprovalProcess_Condition sm_approvalProcess_condition : sm_ApprovalProcess_Node.getApprovalProcess_conditionList())
			{
				sm_approvalProcess_condition.setTheState(S_TheState.Deleted);
			}
			sm_ApprovalProcess_Node.getApprovalProcess_conditionList().clear();
		}

		sm_ApprovalProcess_Node.setTheState(S_TheState.Deleted);

		if(sm_ApprovalProcess_Node.getLastNode()!=null && sm_ApprovalProcess_Node.getNextNode()!=null)  //中间结点
		{
			sm_ApprovalProcess_Node.getLastNode().setNextNode(sm_ApprovalProcess_Node.getNextNode());
			sm_ApprovalProcess_Node.getNextNode().setLastNode(sm_ApprovalProcess_Node.getLastNode());
		}
		else if(sm_ApprovalProcess_Node.getLastNode() ==null && sm_ApprovalProcess_Node.getNextNode()!=null)  //第一个结点
		{
			sm_ApprovalProcess_Node.getNextNode().setLastNode(null);
		}
		else if(sm_ApprovalProcess_Node.getLastNode() !=null && sm_ApprovalProcess_Node.getNextNode()==null)  //最后一个结点
		{
			sm_ApprovalProcess_Node.getLastNode().setNextNode(null);
		}

		sm_ApprovalProcess_Node.setLastNode(null);
		sm_ApprovalProcess_Node.setNextNode(null);

		sm_ApprovalProcess_NodeDao.save(sm_ApprovalProcess_Node);
		//修改页面删除结点--解除流程配置和结点关联关系
		if(sm_ApprovalProcess_Node.getConfiguration() !=null)
		{
			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = sm_ApprovalProcess_Node.getConfiguration();
			sm_approvalProcess_cfg.getNodeList().remove(sm_ApprovalProcess_Node);
			sm_approvalProcess_cfgDao.save(sm_approvalProcess_cfg);
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
