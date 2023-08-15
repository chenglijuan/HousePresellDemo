package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Condition;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_CfgForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_CfgDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：审批流-流程配置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_CfgDeleteService
{
	@Autowired
	private Sm_ApprovalProcess_CfgDao sm_ApprovalProcess_CfgDao;

	public Properties execute(Sm_ApprovalProcess_CfgForm model)
	{
		Properties properties = new MyProperties();

		Long sm_ApprovalProcess_CfgId = model.getTableId();
		Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = (Sm_ApprovalProcess_Cfg)sm_ApprovalProcess_CfgDao.findById(sm_ApprovalProcess_CfgId);
		if(sm_ApprovalProcess_Cfg == null)
		{
			return MyBackInfo.fail(properties, "'Sm_ApprovalProcess_Cfg(Id:" + sm_ApprovalProcess_CfgId + ")'不存在");
		}

		if(sm_ApprovalProcess_Cfg.getNodeList().size() > 0)
		{
			for(Sm_ApprovalProcess_Node sm_approvalProcess_node : sm_ApprovalProcess_Cfg.getNodeList())
			{
				sm_approvalProcess_node.setTheState(S_TheState.Deleted);
				if(sm_approvalProcess_node.getApprovalProcess_conditionList().size() > 0)
				{
					for (Sm_ApprovalProcess_Condition sm_approvalProcess_condition : sm_approvalProcess_node.getApprovalProcess_conditionList())
					{
						sm_approvalProcess_condition.setTheState(S_TheState.Deleted);
					}
					sm_approvalProcess_node.getApprovalProcess_conditionList().clear();
				}
			}
			sm_ApprovalProcess_Cfg.getNodeList().clear();
		}

		
		sm_ApprovalProcess_Cfg.setTheState(S_TheState.Deleted);
		sm_ApprovalProcess_CfgDao.save(sm_ApprovalProcess_Cfg);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
