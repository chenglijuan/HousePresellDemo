package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_ConditionForm;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_TheState;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_NodeForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：审批流-节点
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_NodeUpdateService
{
	@Autowired
	private Sm_ApprovalProcess_NodeDao sm_ApprovalProcess_NodeDao;
	@Autowired
	private Sm_Permission_RoleDao sm_permission_roleDao;
	@Autowired
	private Sm_ApprovalProcess_ConditionDao sm_approvalProcess_conditionDao;
	@Autowired
	private Sm_ApprovalProcess_ConditionAddService sm_approvalProcess_conditionAddService;
	@Autowired
	private Sm_MessageTemplate_CfgDao sm_messageTemplate_cfgDao;
	@Autowired
	private Sm_UserDao sm_userDao;
	
	public Properties execute(Sm_ApprovalProcess_NodeForm model)
	{
		Properties properties = new MyProperties();

		Long sm_ApprovalProcess_NodeId = model.getTableId(); //结点Id
		Integer nodeType = model.getNodeType();//结点类型
		String theName = model.getTheName(); //步骤名称
		String eCode = model.geteCode(); //结点编号
		Long roleId = model.getRoleId(); //角色Id
		Integer approvalModel = model.getApprovalModel() ; //审批模式
		Integer finishPercentage = model.getFinishPercentage();//会签完成阀值
		Integer passPercentage = model.getPassPercentage(); //会签通过阀值
		Integer rejectModel = model.getRejectModel(); //驳回模式
		Long[] checkedMessageTemplateId = model.getCheckedMessageTemplateId();
		Long userId = model.getUserId();

		if(userId == null || userId < 1)
		{
			return MyBackInfo.fail(properties, "'登录用户'不能为空");
		}
		Sm_User userUpdate = sm_userDao.findById(userId);
		if(userUpdate == null)
		{
			return MyBackInfo.fail(properties, "登录用户不能为空");
		}
		if(sm_ApprovalProcess_NodeId == null || sm_ApprovalProcess_NodeId  < 1)
		{
			return MyBackInfo.fail(properties, "'节点'不能为空");
		}

		Sm_ApprovalProcess_Node sm_ApprovalProcess_Node = (Sm_ApprovalProcess_Node)sm_ApprovalProcess_NodeDao.findById(sm_ApprovalProcess_NodeId);
		if(sm_ApprovalProcess_Node == null)
		{
			return MyBackInfo.fail(properties, "'节点(Id:" + sm_ApprovalProcess_NodeId + ")'不存在");
		}

		if(nodeType == null || nodeType  < 0)
		{
			return MyBackInfo.fail(properties, "'节点类型'不能为空");
		}
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "'步骤名称'不能为空");
		}
		if(eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'节点编号'不能为空");
		}
		if(approvalModel == null || approvalModel  < 0)
		{
			return MyBackInfo.fail(properties, "'审批模式'不能为空");
		}
		if(approvalModel == 1)
		{
			if (finishPercentage == null || finishPercentage < 1) {
				return MyBackInfo.fail(properties, "'会签完成阀值'不能为空");
			}
			if (passPercentage == null || passPercentage  < 1) {
				return MyBackInfo.fail(properties, "'会签通过阀值'不能为空");
			}
		}
		if(rejectModel == null || rejectModel < 0)
		{
			return MyBackInfo.fail(properties, "'驳回模式'不能为空");
		}

		if(roleId == null || roleId  < 1)
		{
			return MyBackInfo.fail(properties, "'审批角色'不能为空");
		}

		Sm_Permission_Role sm_permission_role = sm_permission_roleDao.findById(roleId);
		if(sm_permission_role == null)
		{
			return MyBackInfo.fail(properties, "'审批角色'不能为空");
		}

		if(sm_ApprovalProcess_Node.getApprovalProcess_conditionList().size() > 0)
		{
			for(Sm_ApprovalProcess_Condition sm_approvalProcess_condition : sm_ApprovalProcess_Node.getApprovalProcess_conditionList())
			{
				sm_approvalProcess_condition.setTheState(S_TheState.Deleted);
			}
			sm_ApprovalProcess_Node.getApprovalProcess_conditionList().clear();
		}

		//审批条件
		List<Sm_ApprovalProcess_Condition> sm_approvalProcess_conditionList = new ArrayList<Sm_ApprovalProcess_Condition>();

		for (Sm_ApprovalProcess_ConditionForm sm_approvalProcess_conditionForm : model.getSm_ApprovalProcess_ConditionList())
		{
			if(sm_approvalProcess_conditionForm != null)
			{
				Properties propertiesSaveDetail = sm_approvalProcess_conditionAddService.execute(sm_approvalProcess_conditionForm);
				if (S_NormalFlag.success.equals(propertiesSaveDetail.getProperty(S_NormalFlag.result)))
				{
					sm_approvalProcess_conditionList.add((Sm_ApprovalProcess_Condition) propertiesSaveDetail.get("sm_approvalProcess_condition"));
				}
				else
				{
					return propertiesSaveDetail;
				}
			}
		}

		//消息模板
		List<Sm_MessageTemplate_Cfg> sm_messageTemplate_cfgList = new ArrayList<>();
		if(checkedMessageTemplateId !=null && checkedMessageTemplateId.length > 0)
		{
			for(Long messageId : checkedMessageTemplateId)
			{
				Sm_MessageTemplate_Cfg sm_messageTemplate_cfg = sm_messageTemplate_cfgDao.findById(messageId);
				if(sm_messageTemplate_cfg == null)
				{
					return MyBackInfo.fail(properties, "'消息模板'不能为空");
				}
				sm_messageTemplate_cfgList.add(sm_messageTemplate_cfg);
			}
		}

		sm_ApprovalProcess_Node.setTheName(theName); //名称
		sm_ApprovalProcess_Node.seteCode(eCode);
		sm_ApprovalProcess_Node.setRole(sm_permission_role); //角色
		sm_ApprovalProcess_Node.setApprovalModel(approvalModel);//审批模式
		sm_ApprovalProcess_Node.setFinishPercentage(finishPercentage); //会签完成阀值
		sm_ApprovalProcess_Node.setPassPercentage(passPercentage);//会签通过阀值
		sm_ApprovalProcess_Node.setRejectModel(rejectModel);//驳回模式
		sm_ApprovalProcess_Node.setLastUpdateTimeStamp(System.currentTimeMillis());
		sm_ApprovalProcess_Node.setApprovalProcess_conditionList(sm_approvalProcess_conditionList); //审批条件
		sm_ApprovalProcess_Node.setSm_messageTemplate_cfgList(sm_messageTemplate_cfgList);
		sm_ApprovalProcess_Node.setUserUpdate(userUpdate);
		sm_ApprovalProcess_Node.setLastUpdateTimeStamp(System.currentTimeMillis());
	
		sm_ApprovalProcess_NodeDao.save(sm_ApprovalProcess_Node);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
