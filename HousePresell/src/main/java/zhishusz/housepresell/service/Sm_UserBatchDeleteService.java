package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_ValidState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：系统用户+机构用户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_UserBatchDeleteService
{
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Sm_UserForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Sm_User sm_User = (Sm_User)sm_UserDao.findById(tableId);
			if(sm_User == null)
			{
				return MyBackInfo.fail(properties, "'Sm_User(Id:" + tableId + ")'不存在");
			}
		
			if(S_ValidState.Valid.equals(sm_User.getBusiState()))
			{
				return MyBackInfo.fail(properties, "用户"+sm_User.getTheAccount()+"已启用不能删除");
			}
			
			sm_User.setTheState(S_TheState.Deleted);
			sm_UserDao.save(sm_User);
			
			Sm_Permission_RoleUserForm sm_Permission_RoleUserForm = new Sm_Permission_RoleUserForm();
			sm_Permission_RoleUserForm.setSm_UserId(tableId);
			sm_Permission_RoleUserForm.setTheState(S_TheState.Normal);
			List<Sm_Permission_RoleUser> sm_Permission_RoleUserList = sm_Permission_RoleUserDao.findByPage(sm_Permission_RoleUserDao.getQuery(sm_Permission_RoleUserDao.getBasicHQL(), sm_Permission_RoleUserForm));
			
			if(sm_Permission_RoleUserList != null && !sm_Permission_RoleUserList.isEmpty())
			{
				for(Sm_Permission_RoleUser sm_Permission_RoleUser : sm_Permission_RoleUserList)
				{
					sm_Permission_RoleUser.setTheState(S_TheState.Deleted);
					sm_Permission_RoleUserDao.save(sm_Permission_RoleUser);
				}
			}
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
	
//	public Properties executeDeleteAll(Long companyId)
//	{
//		Properties properties = new MyProperties();
//		
//		Sm_User sm_User = (Sm_User)sm_UserDao.findById(id);
//		if(sm_User == null)
//		{
//			return MyBackInfo.fail(properties, "'Sm_User(Id:" + tableId + ")'不存在");
//		}
//	
//		sm_User.setTheState(S_TheState.Deleted);
//		sm_UserDao.save(sm_User);
//		
//		properties.put(S_NormalFlag.result, S_NormalFlag.success);
//		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
//
//		return properties;
//	}
}
