package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_Permission_UIResourceForm;
import zhishusz.housepresell.database.dao.Sm_Permission_UIResourceDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UIResourceType;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service删除：删除菜单项
 * Company：ZhiShuSZ
 * 
 */
@Service
@Transactional
public class Sm_Management_MenuDeleteService
{
	@Autowired
	private Sm_Permission_UIResourceDao sm_Permission_UIResourceDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Sm_Permission_UIResourceForm model)
	{
		Properties properties = new MyProperties();
//		MyString myString = MyString.getInstance();
		
		Long tableId = model.getTableId();
		
		if(tableId == null)
		{
			return MyBackInfo.fail(properties, "请选择菜单");
		}
		
		Sm_User sm_User = sm_UserDao.findById(model.getUserId());
		Sm_Permission_UIResource sm_Permission_UIResource = sm_Permission_UIResourceDao.findById(tableId);
		if(sm_Permission_UIResource == null)
		{
			return MyBackInfo.fail(properties, "该菜单不存在");
		}
		
		Integer theType = sm_Permission_UIResource.getTheType();
		if(!S_UIResourceType.RealityMenu.equals(theType) && 
				!S_UIResourceType.VirtualMenu.equals(theType))
		{
			return MyBackInfo.fail(properties, "菜单类型错误");
		}
		
		Integer theState = sm_Permission_UIResource.getTheState();
		if(S_TheState.Deleted.equals(theState))
		{
			return MyBackInfo.fail(properties, "菜单状态错误");
		}
		
		//将所有父菜单为当前菜单的子菜单，批量删除
		Sm_Permission_UIResourceForm childrenUI_model = new Sm_Permission_UIResourceForm();
		childrenUI_model.setTheState(S_TheState.Normal);
		childrenUI_model.setParentUI(sm_Permission_UIResource);
		childrenUI_model.setTheType(S_UIResourceType.Menu);
		Integer childrenUIList_Size = sm_Permission_UIResourceDao.findByPage_Size(sm_Permission_UIResourceDao.getQuery_Size(sm_Permission_UIResourceDao.getBasicHQL(), childrenUI_model));
		if(childrenUIList_Size != null && childrenUIList_Size > 0)
		{
			return MyBackInfo.fail(properties, "删除菜单失败，请先删除该菜单下的所有子菜单");
		}
		
		sm_Permission_UIResource.setTheState(S_TheState.Deleted);
		sm_Permission_UIResource.setChildrenUIList(null);
		sm_Permission_UIResource.setUserUpdate(sm_User);
		sm_Permission_UIResource.setLastUpdateTimeStamp(System.currentTimeMillis());
		sm_Permission_UIResourceDao.save(sm_Permission_UIResource);
		
		//若该菜单有关联父级菜单，则需要删除，父级菜单和当前菜单的关联关系
		List<Sm_Permission_UIResource> childrenList_NeedSave = new ArrayList<Sm_Permission_UIResource>();
		List<Sm_Permission_UIResource> childrenList = new ArrayList<Sm_Permission_UIResource>();
		Sm_Permission_UIResource parentUI = sm_Permission_UIResource.getParentUI();
		if(parentUI != null)
		{
			childrenList = parentUI.getChildrenUIList();
			for(Sm_Permission_UIResource child : childrenList)
			{
				if(!sm_Permission_UIResource.getTableId().equals(child.getTableId()))
				{
					childrenList_NeedSave.add(child);
				}
			}
			
			parentUI.setChildrenUIList(childrenList_NeedSave);
			sm_Permission_UIResourceDao.save(parentUI);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
