package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UIResourceType;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service操作：获取用户所拥有的按钮权限
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Permission_UserBtnListService
{
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_Permission_RoleForm model)
	{
		Properties properties = new MyProperties();
		
		Long userLogInId = model.getUserId();
		Sm_User sm_User = sm_UserDao.findById(userLogInId);
		
		List<String> needShowBtnList = new ArrayList<String>();
		if(userLogInId != null)
		{
			Long nowTimeStamp = System.currentTimeMillis();
			
			Set<Sm_Permission_UIResource> uiResourceSet = new HashSet<Sm_Permission_UIResource>();
			
			Sm_Permission_RoleUserForm sm_Permission_RoleUserForm = new Sm_Permission_RoleUserForm();
			sm_Permission_RoleUserForm.setTheState(S_TheState.Normal);
			sm_Permission_RoleUserForm.setSm_User(sm_User);
			sm_Permission_RoleUserForm.setLeEnableTimeStamp(nowTimeStamp);
			sm_Permission_RoleUserForm.setGtDownTimeStamp(nowTimeStamp);
			
			List<Sm_Permission_RoleUser> sm_Permission_RoleUserList = sm_Permission_RoleUserDao.findByPage(sm_Permission_RoleUserDao.createCriteriaForListService(sm_Permission_RoleUserForm));
			for(Sm_Permission_RoleUser sm_Permission_RoleUser : sm_Permission_RoleUserList)
			{
				if(sm_Permission_RoleUser.getSm_Permission_Role() == null) continue;
				
				List<Sm_Permission_UIResource> uiResourceList = sm_Permission_RoleUser.getSm_Permission_Role().getUiResourceList();
				uiResourceSet.addAll(uiResourceList);
			}
			
			for(Sm_Permission_UIResource uiResource : uiResourceSet)
			{
				if(!S_UIResourceType.Button.equals(uiResource.getTheType()))
				{
					continue;
				}
				
				String editNum = uiResource.getEditNum();
				if(editNum == null)
				{
					continue;
				}
				
				if(!needShowBtnList.contains(editNum))
				{
					needShowBtnList.add(editNum);
				}
			}
		}

		properties.put("needShowBtnList", needShowBtnList);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
