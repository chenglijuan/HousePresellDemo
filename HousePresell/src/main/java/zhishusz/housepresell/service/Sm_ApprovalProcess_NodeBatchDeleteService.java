package zhishusz.housepresell.service;

import java.util.ArrayList;
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

import  java.util.List;

/*
 * Service批量删除：审批流-节点
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_NodeBatchDeleteService
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
		
		Long[] idArr = model.getIdArr();
		Long configurationId = model.getConfigurationId();//流程配置Id
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		List<Sm_ApprovalProcess_Node> sm_approvalProcess_nodeList;
		Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = null;

		for(Long tableId : idArr)
		{
			Sm_ApprovalProcess_Node sm_ApprovalProcess_Node = (Sm_ApprovalProcess_Node)sm_ApprovalProcess_NodeDao.findById(tableId);
			if(sm_ApprovalProcess_Node == null)
			{
				return MyBackInfo.fail(properties, "'Sm_ApprovalProcess_Node(Id:" + tableId + ")'不存在");
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
			if(configurationId !=null && configurationId > 0)
			{
				sm_approvalProcess_cfg = sm_approvalProcess_cfgDao.findById(configurationId);

				if(sm_approvalProcess_cfg != null)
				{
					sm_approvalProcess_nodeList = sm_approvalProcess_cfg.getNodeList();
					sm_approvalProcess_nodeList.remove(sm_ApprovalProcess_Node);
					sm_approvalProcess_cfgDao.save(sm_approvalProcess_cfg);
				}
			}
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
