package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.state.S_UIResourceType;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：UI权限资源
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_Permission_UIResourceRebuild extends RebuilderBase<Sm_Permission_UIResource>
{
//	private MyDatetime myDatetime = MyDatetime.getInstance();

	@Override
	public Properties getSimpleInfo(Sm_Permission_UIResource object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		//列表页面
		properties.put("tableId", object.getTableId());
		properties.put("busiCode", object.getBusiCode());
		properties.put("theName", object.getTheName());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("theType", object.getTheType());
		properties.put("isDefault", object.getIsDefault());
		properties.put("remark", object.getRemark());
		properties.put("theResource", object.getTheResource());
		properties.put("theState", object.getTheState());
		
		List<Sm_Permission_UIResource> childrenBtnList = object.getChildrenUIList();
		if(childrenBtnList != null)
		{
			List<Properties> childrenBtnList_Pro = new ArrayList<Properties>();
			for(Sm_Permission_UIResource childrenBtn : childrenBtnList)
			{
				Properties childrenBtn_Pro = new MyProperties();
				childrenBtn_Pro.put("theResource", childrenBtn.getTheResource());
				childrenBtn_Pro.put("theName", childrenBtn.getTheName());
				childrenBtn_Pro.put("tableId", childrenBtn.getTableId());
				childrenBtn_Pro.put("editNum", childrenBtn.getEditNum());
				childrenBtnList_Pro.add(childrenBtn_Pro);
			}
			properties.put("childrenBtnList", childrenBtnList_Pro);
		}
		
		return properties;
	}

	@Override
	public Properties getDetail(Sm_Permission_UIResource object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		//详情页面
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("busiCode", object.getBusiCode());
//		properties.put("userStart", object.getUserStart());
		//properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("createTimeStamp",  object.getCreateTimeStamp());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
//		properties.put("userRecord", object.getUserRecord());
//		properties.put("userRecordId", object.getUserRecord().getTableId());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("theName", object.getTheName());
		properties.put("theIndex", object.getTheIndex());
		properties.put("theResource", object.getTheResource());
		properties.put("theType", object.getTheType());
		properties.put("isDefault", object.getIsDefault());
		properties.put("remark", object.getRemark());
//		properties.put("parentUI", object.getParentUI());
//		properties.put("parentUIId", object.getParentUI().getTableId());
//		properties.put("childrenUIList", object.getChildrenUIList());
		
		return properties;
	}
	
	//供SideBar使用
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_Permission_UIResource> sm_Permission_UIResourceList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_Permission_UIResourceList != null)
		{
			for(Sm_Permission_UIResource object:sm_Permission_UIResourceList)
			{
				Properties properties = new MyProperties();
				
				Long tableId = object.getTableId();
				String theName = object.getTheName();
				Double theIndex = object.getTheIndex();
				String theResource = object.getTheResource();
				Integer theType = object.getTheType();
				
				properties.put("tableId", tableId);
				properties.put("theName", theName);
				properties.put("theIndex", theIndex);
				properties.put("theResource", theResource);
				properties.put("theType", theType);
				
				/*List<Properties> childrenUIList_Pro = new ArrayList<Properties>();
				getChildrenUIListPro_Next(properties, childrenUIList_Pro, object);*/
				
				list.add(properties);
			}
		}
		return list;
	}
	
	//递归获取子菜单
	/*@SuppressWarnings("unchecked")
	public void getChildrenUIListPro_Next(Properties properties, Sm_Permission_UIResource childrenUI)
	{
		List<Properties> childrenUIList_Pro = (List<Properties>) properties.get("childrenUIList");
		
		Properties childrenUI_Pro = new MyProperties();
		childrenUI_Pro.put("tableId", childrenUI.getTableId());
		childrenUI_Pro.put("theName", childrenUI.getTheName());
		childrenUI_Pro.put("theIndex", childrenUI.getTheIndex());
		childrenUI_Pro.put("theResource", childrenUI.getTheResource());
		childrenUI_Pro.put("theType", childrenUI.getTheType());
		
		if(childrenUI.getChildrenUIList() != null)
		{
			if(childrenUIList == null) childrenUIList = new ArrayList<Properties>(); 

			List<Sm_Permission_UIResource> childrenUIList = childrenUI.getChildrenUIList();
			for(Sm_Permission_UIResource childrenUI_Temp : childrenUIList)
			{
				getChildrenUIListPro_Next(childrenUIList_Pro_Temp, childrenUI);
			}
			childrenUI_Pro.put("childrenUIList", childrenUIList_Pro_Temp);
		}
	}*/
	
	@SuppressWarnings("rawtypes")
	public List getSimpleDetailForManagement_Menu(List<Sm_Permission_UIResource> sm_Permission_UIResourceList)
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
	
	public Properties getSimpleDetailForManagement_Menu_Add(Sm_Permission_UIResource object)
	{
		if(object == null) return null;
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
			
		return properties;
	}
	
	public Properties getSimpleDetailForManagement_Menu(Sm_Permission_UIResource object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		Sm_Permission_UIResource parentUI = object.getParentUI();
		Sm_Permission_UIResource resourceUI = object.getResourceUI();
		
		//---------公共字段-Start---------//
		Long tableId = object.getTableId();
		String theName = object.getTheName();//UI权限名称，用于显示
		Integer theType = object.getTheType();
		String busiCode = object.getBusiCode();
		Double theIndex = object.getTheIndex();
		String theResource = object.getTheResource();
		String iconPath = object.getIconPath();
		String remark = object.getRemark();
		
		Properties parentUI_Pro = new MyProperties();
		if(parentUI != null)
		{
			parentUI_Pro.put("tableId", parentUI.getTableId());
			parentUI_Pro.put("theName", parentUI.getTheName());
		}
		else
		{
			parentUI_Pro.put("tableId", "");
			parentUI_Pro.put("theName", "");
		}
		properties.put("parentUI", parentUI_Pro);
		
		if(resourceUI != null)
		{
			//菜单，关联的“链接”对象Id
			properties.put("resourceUIId", resourceUI.getTableId());
			List<Properties> childrenBtnList_Pro = new ArrayList<Properties>();
			List<Sm_Permission_UIResource> childrenBtnList = resourceUI.getChildrenUIList();
			if(childrenBtnList != null)
			{
				for(Sm_Permission_UIResource childrenBtn : childrenBtnList)
				{
					Properties childrenBtn_Pro = new MyProperties();
					childrenBtn_Pro.put("tableId", childrenBtn.getTableId());
					childrenBtn_Pro.put("theName", childrenBtn.getTheName());
					childrenBtnList_Pro.add(childrenBtn_Pro);
				}
			}
			properties.put("childrenBtnList", childrenBtnList_Pro);
		}
		
		List<Sm_Permission_UIResource> childrenUIList = object.getChildrenUIList();
		Boolean canChangeTheType = false;
		if(S_UIResourceType.VirtualMenu.equals(theType) && 
				(childrenUIList == null || childrenUIList.size() == 0))
		{
			canChangeTheType = true;
		}
		properties.put("canChangeTheType", canChangeTheType);
		
		if(tableId != null && tableId > 0)
		{
			properties.put("tableId", tableId);
		}
		if(theName != null)
		{
			properties.put("theName", theName);
		}
		if(theType != null)
		{
			properties.put("theType", theType);
		}
		if(busiCode != null)
		{
			properties.put("busiCode", busiCode);
		}
		if(theIndex != null)
		{
			properties.put("theIndex", theIndex);
		}
		if(theResource != null)
		{
			properties.put("theResource", theResource);
		}
		if(iconPath != null)
		{
			properties.put("iconPath", iconPath);
		}
		if(remark != null)
		{
			properties.put("remark", remark);
		}
		
		return properties;
	}
}
