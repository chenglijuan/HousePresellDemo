package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
	
/*
 * Service添加操作：角色与用户对应关系
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Permission_RoleUserAddService
{
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;
	@Autowired
	private Sm_Permission_RoleDao sm_Permission_RoleDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Sm_Permission_RoleUserForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = model.getTheState();
		Long sm_Permission_RoleId = model.getSm_Permission_RoleId();
		Long sm_UserId = model.getSm_UserId();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(sm_Permission_RoleId == null || sm_Permission_RoleId < 1)
		{
			return MyBackInfo.fail(properties, "'sm_Permission_Role'不能为空");
		}
		if(sm_UserId == null || sm_UserId < 1)
		{
			return MyBackInfo.fail(properties, "'sm_User'不能为空");
		}

		Sm_Permission_Role sm_Permission_Role = (Sm_Permission_Role)sm_Permission_RoleDao.findById(sm_Permission_RoleId);
		Sm_User sm_User = (Sm_User)sm_UserDao.findById(sm_UserId);
		if(sm_Permission_Role == null)
		{
			return MyBackInfo.fail(properties, "'sm_Permission_Role'不能为空");
		}
		if(sm_User == null)
		{
			return MyBackInfo.fail(properties, "'sm_User'不能为空");
		}
	
		Sm_Permission_RoleUser sm_Permission_RoleUser = new Sm_Permission_RoleUser();
		sm_Permission_RoleUser.setTheState(theState);
		sm_Permission_RoleUser.setSm_Permission_Role(sm_Permission_Role);
		sm_Permission_RoleUser.setSm_User(sm_User);
		sm_Permission_RoleUserDao.save(sm_Permission_RoleUser);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
