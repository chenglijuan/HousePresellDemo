package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.Properties;

import javax.transaction.Transactional;

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

import  java.util.List;

/*
 * Service详情：审批流-流程配置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_CfgDetailService
{
	@Autowired
	private Sm_ApprovalProcess_CfgDao sm_ApprovalProcess_CfgDao;

	public Properties execute(Sm_ApprovalProcess_CfgForm model)
	{
		Properties properties = new MyProperties();

		Long sm_ApprovalProcess_CfgId = model.getTableId();
		Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = (Sm_ApprovalProcess_Cfg)sm_ApprovalProcess_CfgDao.findById(sm_ApprovalProcess_CfgId);
		if(sm_ApprovalProcess_Cfg == null || S_TheState.Deleted.equals(sm_ApprovalProcess_Cfg.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Sm_ApprovalProcess_Cfg(Id:" + sm_ApprovalProcess_CfgId + ")'不存在");
		}

		List<Sm_ApprovalProcess_Node> sm_ApprovalProcess_NodeList;
		if(sm_ApprovalProcess_Cfg.getNodeList() != null)
		{
			sm_ApprovalProcess_NodeList = sm_ApprovalProcess_Cfg.getNodeList();
			List<Sm_ApprovalProcess_Node> sm_approvalProcess_nodeList;
		}
		else
		{
			sm_ApprovalProcess_NodeList = new ArrayList<Sm_ApprovalProcess_Node>();
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("sm_ApprovalProcess_Cfg", sm_ApprovalProcess_Cfg);
		properties.put("sm_ApprovalProcess_NodeList", sm_ApprovalProcess_NodeList);
		properties.put("sm_ApprovalProcess_ModalNodeList", sm_ApprovalProcess_NodeList);

		return properties;
	}
}
