package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_CfgForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_CfgDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_NodeDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Node;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：待办流程中业务类型查询
 * 根据登录用户所属角色查询所具有的审批流业务类型
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_BaseParameterForDaiBanListService
{
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;//用户角色
	@Autowired
	private Sm_ApprovalProcess_NodeDao sm_ApprovalProcess_NodeDao;//审批节点
	@Autowired
	private Sm_ApprovalProcess_CfgDao sm_ApprovalProcess_CfgDao;//审批流配置
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_BaseParameterForm model)
	{
		Properties properties = new MyProperties();
		
		Sm_User user = model.getUser();
		if(null==user)
		{
			return MyBackInfo.fail(properties, "登录信息已失效，请重新登录！");
		}
		
		//根据登录用户查询角色信息
		Sm_Permission_RoleUserForm roleUserModel = new Sm_Permission_RoleUserForm();
		roleUserModel.setTheState(S_TheState.Normal);
		roleUserModel.setSm_UserId(user.getTableId());
		List<Sm_Permission_RoleUser> roleUserList = new ArrayList<>();
		roleUserList = sm_Permission_RoleUserDao.findByPage(sm_Permission_RoleUserDao.getQuery(sm_Permission_RoleUserDao.getBasicHQL(), roleUserModel));
		if(null==roleUserList||roleUserList.size()==0)
		{
			return MyBackInfo.fail(properties, "未查询到登录用户所属角色信息！");
		}
		
		//查询所有审批流配置信息
		Sm_ApprovalProcess_CfgForm cfgModel = new Sm_ApprovalProcess_CfgForm();
		cfgModel.setTheState(S_TheState.Normal);
		List<Sm_ApprovalProcess_Cfg> cfgList = new ArrayList<>();
		cfgList = sm_ApprovalProcess_CfgDao.findByPage(sm_ApprovalProcess_CfgDao.getQuery(sm_ApprovalProcess_CfgDao.getBasicHQL(), cfgModel));
		if(null==cfgList||cfgList.size()==0)
		{
			return MyBackInfo.fail(properties, "未查询到有效的审批流程！");
		}
		
		List<Sm_BaseParameter> baseParameterList = new ArrayList<>();
		/*
		 * xsz by time 2018-12-20 15:34:56
		 * 审批流配置查询审批节点信息，
		 * 通过审批节点信息与用户角色匹配，
		 * 将满足用户角色的业务类型添加入信息返回
		 */
		List<Sm_ApprovalProcess_Node> nodeList;
		boolean isAdd = false;
		for (Sm_ApprovalProcess_Cfg cfg : cfgList) {
			nodeList = new ArrayList<Sm_ApprovalProcess_Node>();
			nodeList = cfg.getNodeList();
			
			isAdd = false;
			for (Sm_ApprovalProcess_Node node : nodeList) {
				
				for (Sm_Permission_RoleUser roleUser : roleUserList) {
					//如果有角色匹配
					if(roleUser.getSm_Permission_Role().getTableId() == node.getRole().getTableId())
					{
						isAdd = true;
						break;
					}
					
				}
				
				if(isAdd)
				{
					break;
				}
				
			}
			
			if(isAdd)
			{
				Sm_BaseParameterForm baseParameterModel = new Sm_BaseParameterForm();
				baseParameterModel.setTheState(S_TheState.Normal);
				baseParameterModel.setParametertype("1");
				baseParameterModel.setTheValue(cfg.getBusiCode());
				Sm_BaseParameter baseParameter = (Sm_BaseParameter) sm_BaseParameterDao.findOneByQuery(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterModel));
				
				baseParameterList.add(baseParameter);
				
			}
			
		}
		
		properties.put("sm_BaseParameterList", baseParameterList);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
