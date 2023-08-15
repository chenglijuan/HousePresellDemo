package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.dao.Sm_Permission_UIResourceDao;
import zhishusz.housepresell.database.po.Sm_FastNavigate;
import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.state.S_UIResourceType;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：快捷导航信息
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_FastNavigateRebuild extends RebuilderBase<Sm_FastNavigate>
{
	@Autowired
	private Sm_Permission_UIResourceDao sm_Permission_UIResourceDao;
	@Override
	public Properties getSimpleInfo(Sm_FastNavigate object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("menuTableId", object.getMenuTableId());
		properties.put("theNameOfMenu", object.getTheNameOfMenu());
		properties.put("theLinkOfMenu", object.getTheLinkOfMenu());
		
		return properties;
	}

	@Override
	public Properties getDetail(Sm_FastNavigate object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("userUpdate", object.getUserUpdate());
		properties.put("userUpdateId", object.getUserUpdate().getTableId());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("userTableId", object.getUserTableId());
		properties.put("menuTableId", object.getMenuTableId());
		properties.put("theNameOfMenu", object.getTheNameOfMenu());
		properties.put("theLinkOfMenu", object.getTheLinkOfMenu());
		properties.put("orderNumber", object.getOrderNumber());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_FastNavigate> sm_FastNavigateList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_FastNavigateList != null)
		{
			for(Sm_FastNavigate object:sm_FastNavigateList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("userUpdate", object.getUserUpdate());
				properties.put("userUpdateId", object.getUserUpdate().getTableId());
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				properties.put("userTableId", object.getUserTableId());
				properties.put("menuTableId", object.getMenuTableId());
				properties.put("theNameOfMenu", object.getTheNameOfMenu());
				properties.put("theLinkOfMenu", object.getTheLinkOfMenu());
				properties.put("orderNumber", object.getOrderNumber());
				
				list.add(properties);
			}
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public List getSimpleDetailForFastNavigate(List<Sm_Permission_UIResource> sm_Permission_UIResourceList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_Permission_UIResourceList != null)
		{
			for(Sm_Permission_UIResource object:sm_Permission_UIResourceList)
			{
				Properties properties = new MyProperties();
				
				Sm_Permission_UIResource parentUI = object.getParentUI();
				
				//---------公共字段-Start---------//
				Long tableId = object.getTableId();
				//自身的层级编码，最高一级编码为：1，次一级为1_1，再次一级为1_1_1
				String levelNumber = object.getLevelNumber();
				//父级的层级编码，最高一级编码为：1，次一级为1_1，再次一级为1_1_1
				String parentLevelNumber = object.getParentLevelNumber();
				String	theName = object.getTheName();//UI权限名称，用于显示
				Integer	theType = object.getTheType();//类型：虚拟菜单、实体菜单、按钮、链接 S_UIResourceType
				Boolean isParent = false;
				
				properties.put("theType", theType);
				if(S_UIResourceType.VirtualMenu.equals(theType))
				{
					isParent = true;
				}
				
				if(parentUI != null)
				{
					properties.put("parentUIId", parentUI.getTableId());
				}
				
				if(tableId != null && tableId > 0)
				{
					properties.put("tableId", tableId);
				}
				if(levelNumber != null)
				{
					properties.put("id", levelNumber);
				}
				if(parentLevelNumber != null)
				{
					properties.put("pId", parentLevelNumber);
				}
				else
				{
					properties.put("pId", 0);
					properties.put("open", false);
				}
				if(theName != null)
				{
					properties.put("name", theName);
				}
				properties.put("isParent", isParent);
				
				list.add(properties);
			}
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public List getFastNavigateList(List<Sm_FastNavigate> sm_FastNavigateList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_FastNavigateList != null)
		{			
			for(Sm_FastNavigate object:sm_FastNavigateList)
			{
				if(object.getTheLinkOfMenu()!=null){
					Properties properties = new MyProperties();
					if(object.getTheNameOfMenu()!=null){
						properties.put("theNameOfMenu", object.getTheNameOfMenu());
					}
					if(object.getTheLinkOfMenu()!=null){
						properties.put("theLinkOfMenu", object.getTheLinkOfMenu());
					}
					if(object.getMenuTableId()!=null){
						Sm_Permission_UIResource sm_Permission_UIResource=sm_Permission_UIResourceDao.findById(object.getMenuTableId());
						properties.put("iconPath", sm_Permission_UIResource.getIconPath());
					}				
					list.add(properties);
				}
				
			}
		}
		return list;
	}
}
