package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_NodeDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_TheState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_CfgForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_CfgDao;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import java.util.Set;
import java.util.List;
	
/*
 * Service添加操作：审批流-流程配置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_CfgAddService
{
	@Autowired
	private Sm_ApprovalProcess_CfgDao sm_ApprovalProcess_CfgDao;
	@Autowired
	private Sm_ApprovalProcess_NodeDao sm_approvalProcess_nodeDao;
	@Autowired
	private Sm_ApprovalProcess_NodeDao sm_ApprovalProcess_NodeDao;
	@Autowired
	private Sm_UserDao sm_userDao;
	@Autowired
	private Sm_BaseParameterDao sm_baseParameterDao;

	public Properties execute(Sm_ApprovalProcess_CfgForm model)
	{
		Properties properties = new MyProperties();

		Long userId = model.getUserId();
		Long busiId = model.getBusiId();
		String eCode = model.geteCode(); //流程编码
		String theName = model.getTheName(); //流程名称
		String remark = model.getRemark(); //备注
		Integer isNeedBackup = model.getIsNeedBackup();
		Long[] idArr = model.getIdArr(); //  结点id

		if(userId == null || userId < 1)
		{
			return MyBackInfo.fail(properties, "'登录用户'不能为空");
		}
		Sm_User userStart = sm_userDao.findById(userId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "登录用户不能为空");
		}
		if(busiId == null || busiId < 1)
		{
			return MyBackInfo.fail(properties, "'业务编码'不能为空");
		}
		Sm_BaseParameter sm_baseParameter = sm_baseParameterDao.findById(busiId);
		if(sm_baseParameter == null )
		{
			return MyBackInfo.fail(properties, "'业务编码'不能为空");
		}
		String busiCode = sm_baseParameter.getTheValue();//业务编码
		String busiType = sm_baseParameter.getTheName();//业务类型
		if(busiCode == null || busiCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'业务编码'不能为空");
		}
		if(busiType == null || busiType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'业务类型'不能为空");
		}
		if(eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'流程编码'不能为空");
		}
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "'流程名称'不能为空");
		}
		if(isNeedBackup == null || isNeedBackup < 0)
		{
			return MyBackInfo.fail(properties, "'是否需要备案'不能为空");
		}
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "请添加审批流程");
		}

		List<Sm_ApprovalProcess_Node> sm_approvalProcess_nodeList = new ArrayList<>();
		for(Long tableId : idArr)
		{
			Sm_ApprovalProcess_Node  sm_approvalProcess_node = (Sm_ApprovalProcess_Node)sm_approvalProcess_nodeDao.findById(tableId);
			if(sm_approvalProcess_node == null)
			{
				return MyBackInfo.fail(properties, "'Sm_ApprovalProcess_Node(Id:" + tableId + ")'不存在");
			}
			sm_approvalProcess_node.setTemporaryState(S_TheState.Normal);
			if(sm_approvalProcess_node.getApprovalProcess_conditionList() != null)
			{
				for(Sm_ApprovalProcess_Condition sm_approvalProcess_condition : sm_approvalProcess_node.getApprovalProcess_conditionList())
				{
					sm_approvalProcess_condition.setTemporaryState(S_TheState.Normal);
				}
			}
			sm_approvalProcess_nodeList.add(sm_approvalProcess_node);
		}
		Sm_Permission_Role sm_permission_role = sm_approvalProcess_nodeList.get(0).getRole();
	
		Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = new Sm_ApprovalProcess_Cfg();
		sm_ApprovalProcess_Cfg.setTheState(S_TheState.Normal);
		sm_ApprovalProcess_Cfg.setUserStart(userStart);
		sm_ApprovalProcess_Cfg.setTheName(theName);
		sm_ApprovalProcess_Cfg.setBusiCode(busiCode);//业务编码
		sm_ApprovalProcess_Cfg.setBusiType(busiType);//业务类型
		sm_ApprovalProcess_Cfg.seteCode(eCode);
		sm_ApprovalProcess_Cfg.setIsNeedBackup(isNeedBackup);
		sm_ApprovalProcess_Cfg.setRemark(remark);
		sm_ApprovalProcess_Cfg.setNodeList(sm_approvalProcess_nodeList);
		sm_ApprovalProcess_Cfg.setCreateTimeStamp(System.currentTimeMillis());
		sm_ApprovalProcess_Cfg.setLastUpdateTimeStamp(System.currentTimeMillis());
		sm_ApprovalProcess_CfgDao.save(sm_ApprovalProcess_Cfg);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tableId", sm_ApprovalProcess_Cfg.getTableId());
		return properties;
	}
}
