package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.controller.form.Sm_Permission_UIResourceForm;
import zhishusz.housepresell.controller.form.TemplateStaticModel;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UIResourceType;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;
import zhishusz.housepresell.util.comparator.MyUIResourceComparator;
import zhishusz.housepresell.util.templateStatic.JsonUtil;
import zhishusz.housepresell.util.templateStatic.SideBarMenu;

/*
 * Service列表查询：左侧菜单静态化
 * 登录人所属左侧菜单静态化
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Sm_Permission_UIResourceLoginForSideBarInitService
{
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;// 用户角色对应关系
	@Autowired
	private Sm_TemplateStaticService sm_TemplateStaticService;
	
	MyString myString = MyString.getInstance();
	
	/**
	 * 根据登录者，获取左侧菜单静态化所需的数据
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Properties executeForSideBarInit(Sm_Permission_UIResourceForm model)
	{
		Properties properties = new MyProperties();

		Sm_User user = model.getUser();
		if (user == null)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NeedLogin);
		}

		// 根据用户id查询用户所属角色信息
		Sm_Permission_RoleUserForm form = new Sm_Permission_RoleUserForm();
		form.setSm_UserId(user.getTableId());
		form.setTheState(S_TheState.Normal);
		List<Sm_Permission_RoleUser> sm_Permission_RoleUserList = sm_Permission_RoleUserDao.findByPage(sm_Permission_RoleUserDao.getQuery(sm_Permission_RoleUserDao.getBasicHQL(), form));
		
		// 获取用户所属角色所关联的所有菜单和按钮信息 
		List<Sm_Permission_UIResource> uiResourceList = new ArrayList<Sm_Permission_UIResource>();
		for(Sm_Permission_RoleUser sm_Permission_RoleUser : sm_Permission_RoleUserList)
		{
			Sm_Permission_Role sm_Permission_Role = sm_Permission_RoleUser.getSm_Permission_Role();
			if(sm_Permission_Role == null) continue;
			
			uiResourceList.removeAll(sm_Permission_Role.getUiResourceList());
			uiResourceList.addAll(sm_Permission_Role.getUiResourceList());
		}
		
		//排序（根据 theIndex 和 tableId排序）
		//先按theIndex排序（小的在前面）
		//若值一样，按照tableId小的排上面
		Collections.sort(uiResourceList, new MyUIResourceComparator());
		
		// 菜单列表
		List<Sm_Permission_UIResource> menuList = new ArrayList<Sm_Permission_UIResource>();
		//List<Sm_Permission_UIResource> btnList = new ArrayList<Sm_Permission_UIResource>();
		for(Sm_Permission_UIResource uiResource : uiResourceList)
		{
			if(S_UIResourceType.VirtualMenu.equals(uiResource.getTheType()) || 
					S_UIResourceType.RealityMenu.equals(uiResource.getTheType()))
			{
				menuList.add(uiResource);
			}
		}
		
		//一级菜单列表
		List<Sm_Permission_UIResource> firstLevelMenuList = new ArrayList<Sm_Permission_UIResource>();
		Map<String, List<Sm_Permission_UIResource>> pidAndChildrenListMap = new HashMap<String, List<Sm_Permission_UIResource>>();
		for(Sm_Permission_UIResource menu : menuList)
		{
			String pid = menu.getParentLevelNumber();
			List<Sm_Permission_UIResource> childrenList = pidAndChildrenListMap.get(pid);
			if(childrenList == null) childrenList = new ArrayList<Sm_Permission_UIResource>();
			
			childrenList.add(menu);
			pidAndChildrenListMap.put(pid, childrenList);
			
			if("0".equals(pid))
			{
				firstLevelMenuList.add(menu);
			}
		}
		
		//================== 获取JsonStr格式的数据 Start ===============================//
		Properties menuList_Pro = new MyProperties();
		List<SideBarMenu> menuList_AfterRebuild = treeMenuList(pidAndChildrenListMap, firstLevelMenuList, "0");
		menuList_Pro.put("treeMenu", menuList_AfterRebuild);
		String dataJsonStr = new JsonUtil().propertiesToJson(menuList_Pro, 17);
		System.out.println(dataJsonStr);
		//================== 获取JsonStr格式的数据 End ===============================//
		
		//================== 静态化业务 开始 ===================================================//
		TemplateStaticModel templateStaticModel = new TemplateStaticModel();
		templateStaticModel.setDataJsonStr(dataJsonStr);
		
		DirectoryUtil directoryUtil = new DirectoryUtil();
		String templatePath = directoryUtil.getProjectRoot()+"resources/templateStatic/SideBarTemplate.java.jstl"; 
		templateStaticModel.setTemplatePath(templatePath);
		templateStaticModel.setFileOutputPath("userNavigationMenu_"+user.getTableId()+".html");
		sm_TemplateStaticService.execute(templateStaticModel);
		//=================== 静态化业务 结束  =================================================//
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		return properties;
	}
	
	
    /**
     * 菜单树形结构
     * 
     * @param menuList 菜单数据
     * @param parentId 父级ID
     * @return
     */
    public List<SideBarMenu> treeMenuList(Map<String, List<Sm_Permission_UIResource>> pidAndChildrenListMap, 
    		List<Sm_Permission_UIResource> menuList, String parentId) 
    {
    	List<SideBarMenu> childMenu = new ArrayList<SideBarMenu>();
        
        for (Sm_Permission_UIResource menu : menuList) 
        {
        	if(menu == null || menu.getLevelNumber() == null || menu.getParentLevelNumber() == null) continue;
        	//System.out.println(menu.getTheName()+"-"+menu.getParentLevelNumber());
        	
            if (parentId.equals(menu.getParentLevelNumber()))
            {
            	SideBarMenu jo = new SideBarMenu();
            	jo.setParentLevelNumber(myString.parseForExport(menu.getParentLevelNumber()));
                jo.setLevelNumber(myString.parseForExport(menu.getLevelNumber()));
                jo.setTheName(myString.parseForExport(menu.getTheName()));
                jo.setTheResource(myString.parseForExport(menu.getTheResource()));
                jo.setIconPath(myString.parseForExport(menu.getIconPath()));
                jo.setBusiCode(myString.parseForExport(menu.getBusiCode()));
                
                List<Sm_Permission_UIResource> childrenMenuList = pidAndChildrenListMap.get(menu.getLevelNumber());
                if(childrenMenuList == null)
                {
                	childrenMenuList = new ArrayList<Sm_Permission_UIResource>();
                }
                List<SideBarMenu> c_node = treeMenuList(pidAndChildrenListMap, childrenMenuList, menu.getLevelNumber());
                c_node = c_node == null ? new ArrayList<SideBarMenu>() : c_node;
                jo.setChildrenUIList(c_node);
                childMenu.add(jo);
            }
        }
        
        return childMenu;
    }
}
