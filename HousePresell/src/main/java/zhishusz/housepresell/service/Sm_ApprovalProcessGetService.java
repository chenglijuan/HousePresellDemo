package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_CfgForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_CfgDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Node;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

/**
 * 获取登录用户的审批流程
 * 
 * @author Glad.Wang
 *
 */
@Service
@Transactional
public class Sm_ApprovalProcessGetService
{
	@Autowired
	private Sm_ApprovalProcess_CfgDao sm_ApprovalProcess_CfgDao;
	@Autowired
	private Sm_Permission_RoleUserDao sm_permission_roleUserDao;
	@Autowired
	private Sm_UserDao sm_userDao;

	@SuppressWarnings({"unchecked"})
	public Properties execute(String busiCode, Long userStartId)
	{
		Properties properties = new MyProperties();

		Sm_User userStart = sm_userDao.findById(userStartId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "登录用户不能为空");
		}

		if(userStart.getCompany() == null)
		{
			return MyBackInfo.fail(properties, "用户所属机构不能为空");
		}
		Emmp_CompanyInfo emmp_companyInfo = userStart.getCompany();

		if(emmp_companyInfo.getTheName() == null || emmp_companyInfo.getTheName().length() == 0)
		{
			return MyBackInfo.fail(properties, "机构名称不能为空");
		}


		Sm_Permission_RoleUserForm roleUserForm = new Sm_Permission_RoleUserForm();
		roleUserForm.setTheState(S_TheState.Normal);
		roleUserForm.setSm_UserId(userStartId);
		List<Sm_Permission_RoleUser> sm_permission_roleUserList = sm_permission_roleUserDao.findByPage(sm_permission_roleUserDao.getQuery(sm_permission_roleUserDao.getBasicHQL(), roleUserForm));

		Sm_ApprovalProcess_CfgForm cfgFormModel = new Sm_ApprovalProcess_CfgForm();
		cfgFormModel.setTheState(S_TheState.Normal);
		cfgFormModel.setBusiCode(busiCode);
		List<Sm_ApprovalProcess_Cfg> sm_ApprovalProcess_CfgList = sm_ApprovalProcess_CfgDao.findByPage(sm_ApprovalProcess_CfgDao.getQuery(sm_ApprovalProcess_CfgDao.getBasicHQL(), cfgFormModel));

		if(sm_ApprovalProcess_CfgList == null || sm_ApprovalProcess_CfgList.isEmpty())
		{
			properties.put(S_NormalFlag.info,"noApproval");
			return properties;
		}

        List<Sm_ApprovalProcess_Cfg> sm_approvalProcess_cfgList2 = new ArrayList<>();

        for(Sm_Permission_RoleUser sm_Permission_RoleUser : sm_permission_roleUserList)
		{
			if(sm_Permission_RoleUser.getSm_Permission_Role() == null)
			{
				return MyBackInfo.fail(properties, "角色不能为空");
			}
			Sm_Permission_Role sm_permission_role1 = sm_Permission_RoleUser.getSm_Permission_Role(); //登录用户角色
			Long userRoleId = sm_permission_role1.getTableId(); //登录用户角色Id
			List<Long> cfgRoleIdList = new ArrayList<Long>();//流程配置第一个节点（发起人节点）角色ID列表
			
			int count = 0;
			for(Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg : sm_ApprovalProcess_CfgList)
			{
				if(sm_ApprovalProcess_Cfg.getNodeList() == null)
				{
					return MyBackInfo.fail(properties, "审批流程结点信息不能为空");
				}
				Sm_ApprovalProcess_Node sm_approvalProcess_node = sm_ApprovalProcess_Cfg.getNodeList().get(0);
				if(sm_approvalProcess_node.getRole() == null)
				{
					return MyBackInfo.fail(properties, "审批结点角色不能为空");
				}

				Sm_Permission_Role sm_permission_role2 = sm_approvalProcess_node.getRole(); //流程配置第一个结点 角色信息
				Long cfgRoleId = sm_permission_role2.getTableId(); //流程配置第一个结点 角色Id
				cfgRoleIdList.add(cfgRoleId);
				if(userRoleId.equals(cfgRoleId))
				{
					count++;
				}
			}
			if(count == 1)
			{
				sm_approvalProcess_cfgList2.add(sm_ApprovalProcess_CfgList.get(cfgRoleIdList.indexOf(userRoleId)));
			}
		}

		if(sm_approvalProcess_cfgList2.isEmpty())
		{
			return MyBackInfo.fail(properties, "没有权限发起审批");
		}
		if(sm_approvalProcess_cfgList2.size() > 1 )
		{
			return MyBackInfo.fail(properties, "发现"+sm_approvalProcess_cfgList2.size()+"个审批流程");
		}

		Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = sm_approvalProcess_cfgList2.get(0);

		if(sm_approvalProcess_cfg.getBusiType() == null || sm_approvalProcess_cfg.getBusiType().length()==0)
		{
			return MyBackInfo.fail(properties, "业务类型不能为空");
		}

		properties.put("sm_approvalProcess_cfg", sm_approvalProcess_cfg);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
