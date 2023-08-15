package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_TheState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_CfgForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：审批流-流程配置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_CfgUpdateService
{
	@Autowired
	private Sm_ApprovalProcess_CfgDao sm_ApprovalProcess_CfgDao;
	@Autowired
	private Sm_ApprovalProcess_NodeDao sm_approvalProcess_nodeDao;
	@Autowired
	private Sm_ApprovalProcess_ConditionDao sm_approvalProcess_conditionDao;
	@Autowired
	private Sm_UserDao sm_userDao;
	@Autowired
	private Sm_BaseParameterDao sm_baseParameterDao;
	
	public Properties execute(Sm_ApprovalProcess_CfgForm model)
	{
		Properties properties = new MyProperties();

		Long userId = model.getUserId();
		Long[] idArr = model.getIdArr(); //  新增结点id
		String theName = model.getTheName();
		String eCode = model.geteCode();
		Integer isNeedBackup = model.getIsNeedBackup();
		String remark = model.getRemark();

		if(userId == null || userId < 1)
		{
			return MyBackInfo.fail(properties, "'登录用户'不能为空");
		}
		Sm_User userUpdate = sm_userDao.findById(userId);
		if(userUpdate == null)
		{
			return MyBackInfo.fail(properties, "登录用户不能为空");
		}

		if(eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'流程编码'不能为空");
		}
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "'流程名称'不能为空");
		}

		Long sm_ApprovalProcess_CfgId = model.getTableId();
		Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = (Sm_ApprovalProcess_Cfg)sm_ApprovalProcess_CfgDao.findById(sm_ApprovalProcess_CfgId);
		if(sm_ApprovalProcess_Cfg == null)
		{
			return MyBackInfo.fail(properties, "'流程配置(Id:" + sm_ApprovalProcess_CfgId + ")'不存在");
		}


		List<Sm_ApprovalProcess_Node> sm_approvalProcess_nodeList = sm_ApprovalProcess_Cfg.getNodeList();
		for(Sm_ApprovalProcess_Node sm_approvalProcess_node : sm_approvalProcess_nodeList)
		{
			sm_approvalProcess_node.setTemporaryState(S_TheState.Normal);
			if(sm_approvalProcess_node.getApprovalProcess_conditionList() != null)
			{
				for(Sm_ApprovalProcess_Condition sm_approvalProcess_condition : sm_approvalProcess_node.getApprovalProcess_conditionList())
				{
					sm_approvalProcess_condition.setTemporaryState(S_TheState.Normal);
					sm_approvalProcess_conditionDao.save(sm_approvalProcess_condition);
				}
			}
		}

		sm_ApprovalProcess_Cfg.setUserUpdate(userUpdate);
		sm_ApprovalProcess_Cfg.setTheName(theName);
		sm_ApprovalProcess_Cfg.seteCode(eCode);
		sm_ApprovalProcess_Cfg.setIsNeedBackup(isNeedBackup);
		sm_ApprovalProcess_Cfg.setRemark(remark);
		sm_ApprovalProcess_Cfg.setLastUpdateTimeStamp(System.currentTimeMillis());
	
		sm_ApprovalProcess_CfgDao.save(sm_ApprovalProcess_Cfg);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
