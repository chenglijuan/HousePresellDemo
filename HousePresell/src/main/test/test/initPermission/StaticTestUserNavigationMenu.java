package test.initPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_Permission_UIResourceForm;
import zhishusz.housepresell.database.dao.Sm_Permission_UIResourceDao;
import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UIResourceType;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;
import zhishusz.housepresell.util.templateStatic.FreemarkerStaticUtil;
import zhishusz.housepresell.util.templateStatic.JsonUtil;
import zhishusz.housepresell.util.templateStatic.SideBarMenu;
import com.google.gson.JsonObject;

import test.api.BaseJunitTest;

/**
 * （静态化）左侧菜单栏测试方法 
 * @author http://zhishusz
 */
@Transactional(transactionManager="transactionManager")
public class StaticTestUserNavigationMenu extends BaseJunitTest
{
	@Autowired
	private Sm_Permission_UIResourceDao sm_Permission_UIResourceDao;
	MyString myString = MyString.getInstance();
	
	@SuppressWarnings("rawtypes")
	@Test
	@Override
	public void execute() throws Exception
	{
		//================== 获取JsonStr格式的数据 Start ===============================//
		Properties properties = new MyProperties();
		List<SideBarMenu> menuList = getTreeMenu();
		properties.put("treeMenu", menuList);
		String dataJsonStr = new JsonUtil().propertiesToJson(properties, 17);
		System.out.println(dataJsonStr);
		//================== 获取JsonStr格式的数据 End ===============================//
		
		//================== 设置静态化相关文件的路径 Start ===============================//
		//String templatePath = "http://localhost/HousePresell/resources/templateStatic/SideBarTemplate.java.jstl"; 
		//设置权限配置文件路径(支持物理路径和网络路径)
		String templatePath = System.getProperty("user.dir")+"/src/main/webapp/resources/templateStatic/SideBarTemplate.java.jstl"; 
		//静态化文件的根路径（支持自定义配置）
		String fileRootPath = System.getProperty("user.dir")+"/src/main/test/test/initPermission/";
		String fileOutputPath = "test20181008.html";
		//================== 设置静态化相关文件的路径 End ================================//
		
		//================== 静态化业务 开始 ===================================================//
		JsonUtil jsonUtil = JsonUtil.getInstance();
		FreemarkerStaticUtil freemarkerStaticUtil = FreemarkerStaticUtil.getInstance();
		freemarkerStaticUtil.setFileRootPath(fileRootPath);
		JsonObject dataJsonObj = jsonUtil.jsonStrToJsonObj(dataJsonStr);
		
		//执行静态化
		Map orgInputMap = new HashMap();
		Map currentInputMap = new HashMap();
		freemarkerStaticUtil.createFile(templatePath, fileOutputPath,
				dataJsonObj, orgInputMap, currentInputMap);
		//=================== 静态化业务 结束  =================================================//
		
		System.out.println(S_NormalFlag.info_Success);
	}
	
	/**
	 * 从数据库获取 菜单数据列表 信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Sm_Permission_UIResource> getModules()
	{
		//===================== 查询数据库所有 “正常状态”的“一级菜单”列表数据 Start =================================//
		Sm_Permission_UIResourceForm sm_Permission_UIResourceForm = new Sm_Permission_UIResourceForm();
		sm_Permission_UIResourceForm.setTheState(S_TheState.Normal);
		sm_Permission_UIResourceForm.setTheType(S_UIResourceType.Menu);
		sm_Permission_UIResourceForm.setParentLevelNumber("0");
		List<Sm_Permission_UIResource> firstLevelMenuList = sm_Permission_UIResourceDao.findByPage(sm_Permission_UIResourceDao.getQuery(sm_Permission_UIResourceDao.getBasicHQL(), sm_Permission_UIResourceForm));
		//===================== 查询数据库所有 “正常状态”的“一级菜单”列表数据 End ===================================//
		
		return firstLevelMenuList;
	}
	
	/**
	 * 返回树形导航菜单(Rebuild)后的数据
	 * 
	 * @return
	 */
    public List<SideBarMenu> getTreeMenu()
    {
        return treeMenuList(getModules(), "0");
    }

    /**
     * 菜单树形结构
     * 
     * @param menuList 菜单数据
     * @param parentId 父级ID
     * @return
     */
    public List<SideBarMenu> treeMenuList(List<Sm_Permission_UIResource> menuList, String parentId) 
    {
    	List<SideBarMenu> childMenu = new ArrayList<SideBarMenu>();
        
        for (Sm_Permission_UIResource menu : menuList) 
        {
            if (parentId.equals(menu.getParentLevelNumber()))
            {
            	SideBarMenu jo = new SideBarMenu();
            	jo.setParentLevelNumber(myString.parseForExport(menu.getParentLevelNumber()));
                jo.setLevelNumber(myString.parseForExport(menu.getLevelNumber()));
                jo.setTheName(myString.parseForExport(menu.getTheName()));
                jo.setTheResource(myString.parseForExport(menu.getTheResource()));
                jo.setIconPath(myString.parseForExport(menu.getIconPath()));
                jo.setBusiCode(myString.parseForExport(menu.getBusiCode()));
                
                List<SideBarMenu> c_node = treeMenuList(menu.getChildrenUIList(), menu.getLevelNumber());
                c_node = c_node == null ? new ArrayList<SideBarMenu>() : c_node;
                jo.setChildrenUIList(c_node);
                childMenu.add(jo);
            }
        }
        
        return childMenu;
    }
}
