package test.initPermission;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_Permission_UIResourceForm;
import zhishusz.housepresell.database.dao.Sm_Permission_UIResourceDao;
import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UIResourceType;
import zhishusz.housepresell.initialize.Sm_Permission_UIResourceTemplate;
import zhishusz.housepresell.initialize.Sm_Permission_UIResourceTemplateList;
import zhishusz.housepresell.util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitSm_Permission_UIResource extends BaseJunitTest
{
	@Autowired
	private Sm_Permission_UIResourceDao sm_Permission_UIResourceDao;
	
	/**
	 * 权限资源(UIResource)初始化，执行方法 Start
	 */
	@Test
	public void execute() throws Exception 
	{
		//Step1-->持久化，每一个URL链接，以及对应链接下的所有按钮
		saveResourseFromJsonFile("UrlAndBtnRelationConfig.json");
		//Step2-->持久化，每一个权限资源对象（关联关系暂时不保存）
		saveResourseFromJsonFile("PermissionConfig.json");
		//Step3-->持久化，每一个菜单之前的关联关系
		saveRelation_Step3();
		//Step4-->持久化，每一个实体菜单权限所关联的URL链接对象
		saveRealityMenuAndUrlRelation_Step4();
	}
	
	/**
	 * 	读取 指定 Json 配置文件信息
	 *  该方法用于初始化，“权限资源” 表-->Sm_Permission_UIResource
	 */
	public void saveResourseFromJsonFile(String jsonFileName)
	{
		//设置权限配置文件路径
		String jsonFilePath = System.getProperty("user.dir")+"/src/main/test/test/initPermission/"+jsonFileName;
		
		//根据指定文件路径，读取权限配置文件内容
		Gson gson = new GsonBuilder().create();
		Sm_Permission_UIResourceTemplateList listObj = gson.fromJson(new JsonUtil().getJsonString(jsonFilePath), new TypeToken<Sm_Permission_UIResourceTemplateList>(){}.getType());
		
		//根据读取到的数据，持久化权限信息
		List<Sm_Permission_UIResourceTemplate> templateList = listObj.getList();
		for(Sm_Permission_UIResourceTemplate template : templateList)
		{
			//根据 theName 的值在数据库中查询theOriginalName是否和theName的值相等，查不到就新增
			//String theOriginalName = template.getTheName();
			//template.setTheOriginalName(theOriginalName);
			//递归增加UIResource权限
			addUIResource(template);
		}
		
		//================= 输入从权限资源json文件，读取到的数据 Start ===============//
		/*Properties properties = new MyProperties();
		properties.put("listObj", listObj);
		
		String jsonStr = new JsonUtil().propertiesToJson(properties,20);
		System.out.println(jsonStr);*/
		//================= 输入从权限资源json文件，读取到的数据 End ===============//
	}
	
	/**
	 * Step3-->持久化权限之间的关联关系
	 */
	@SuppressWarnings("unchecked")
	public void saveRelation_Step3()
	{
		Sm_Permission_UIResourceForm sm_Permission_UIResourceForm = new Sm_Permission_UIResourceForm();	
		sm_Permission_UIResourceForm.setTheState(S_TheState.Normal);
		List<Sm_Permission_UIResource> uiResourceList = sm_Permission_UIResourceDao.findByPage(sm_Permission_UIResourceDao.getQuery(sm_Permission_UIResourceDao.getBasicHQL(), sm_Permission_UIResourceForm));
		
		for(Sm_Permission_UIResource uiResource : uiResourceList)
		{
			//持久化：父级权限关系
			String parentLevelNumber = uiResource.getParentLevelNumber();
			if(parentLevelNumber != null && parentLevelNumber.length() > 0)
			{
				Sm_Permission_UIResourceForm sm_Permission_UIResourceForm_Parent = new Sm_Permission_UIResourceForm();	
				sm_Permission_UIResourceForm_Parent.setTheState(S_TheState.Normal);
				sm_Permission_UIResourceForm_Parent.setLevelNumber(parentLevelNumber);
				Sm_Permission_UIResource uiResource_Parent = sm_Permission_UIResourceDao.findOneByQuery_T(sm_Permission_UIResourceDao.getQuery(sm_Permission_UIResourceDao.getBasicHQL(), sm_Permission_UIResourceForm_Parent));
				if(uiResource_Parent != null) 
				{
					uiResource.setParentUI(uiResource_Parent);
				}
			}
			
			//持久化：子级权限关系
			String levelNumber = uiResource.getLevelNumber();
			if(levelNumber != null && levelNumber.length() > 0)
			{
				Sm_Permission_UIResourceForm sm_Permission_UIResourceForm_Children = new Sm_Permission_UIResourceForm();	
				sm_Permission_UIResourceForm_Children.setTheState(S_TheState.Normal);
				sm_Permission_UIResourceForm_Children.setParentLevelNumber(levelNumber);
				List<Sm_Permission_UIResource> childrenUIList = sm_Permission_UIResourceDao.findByPage(sm_Permission_UIResourceDao.getQuery(sm_Permission_UIResourceDao.getBasicHQL(), sm_Permission_UIResourceForm_Children));
				if(childrenUIList != null)
				{
					uiResource.setChildrenUIList(childrenUIList);
				}
			}
			
			sm_Permission_UIResourceDao.save(uiResource);
		}
	}
	
	/**
	 * Step4-->持久化，每一个实体菜单权限所关联的URL链接对象
	 */
	@SuppressWarnings("unchecked")
	public void saveRealityMenuAndUrlRelation_Step4()
	{
		Sm_Permission_UIResourceForm sm_Permission_UIResourceForm = new Sm_Permission_UIResourceForm();	
		sm_Permission_UIResourceForm.setTheState(S_TheState.Normal);
		sm_Permission_UIResourceForm.setTheType(S_UIResourceType.RealityMenu);
		List<Sm_Permission_UIResource> menuUiResourceList = sm_Permission_UIResourceDao.findByPage(sm_Permission_UIResourceDao.getQuery(sm_Permission_UIResourceDao.getBasicHQL(), sm_Permission_UIResourceForm));
		
		for(Sm_Permission_UIResource menu_UIResource : menuUiResourceList)
		{
			//该实体菜单，未预先关联URL，则跳过
			String theResource = menu_UIResource.getTheResource();
			if(theResource == null || theResource.length() == 0) continue;
			
			Sm_Permission_UIResourceForm urlForm = new Sm_Permission_UIResourceForm();	
			urlForm.setTheState(S_TheState.Normal);
			urlForm.setTheType(S_UIResourceType.TheResource);
			urlForm.setTheResource(theResource);
			Sm_Permission_UIResource url_UIResource = sm_Permission_UIResourceDao.findOneByQuery_T(sm_Permission_UIResourceDao.getQuery(sm_Permission_UIResourceDao.getBasicHQL(), urlForm));
			menu_UIResource.setResourceUI(url_UIResource);
			
			sm_Permission_UIResourceDao.save(menu_UIResource);
		}
	}
	
	/**
	 * 递归增加UIResource权限
	 * @param template
	 */
	public void addUIResource(Sm_Permission_UIResourceTemplate template)
	{
		if(template == null) return;
			
		Sm_Permission_UIResourceForm sm_Permission_UIResourceForm = new Sm_Permission_UIResourceForm();	
		sm_Permission_UIResourceForm.setTheOriginalName(template.getTheName());
		sm_Permission_UIResourceForm.setTheState(S_TheState.Normal);
		sm_Permission_UIResourceForm.setTheType(S_UIResourceType.StrToIntVal.get(template.getTheType()));
		if(S_UIResourceType.Button.equals(sm_Permission_UIResourceForm.getTheType()))
		{
			//初始化按钮的时候需要，另外多传参数
			sm_Permission_UIResourceForm.setParentLevelNumber(template.getParentLevelNumber());
		}
		Sm_Permission_UIResource sm_Permission_UIResource = sm_Permission_UIResourceDao.findOneByQuery_T(sm_Permission_UIResourceDao.getQuery(sm_Permission_UIResourceDao.getBasicHQL(), sm_Permission_UIResourceForm));
		//已经添加完毕的，则不再重复添加
		if(sm_Permission_UIResource != null) return;
		
		//==================== 持久化当前菜单 Start =========================//
		sm_Permission_UIResource = new Sm_Permission_UIResource();
		saveUIResource(sm_Permission_UIResource, template);
		//==================== 持久化当前菜单 End ===========================//
		
		List<Sm_Permission_UIResourceTemplate> childrenUIList = template.getChildrenUIList();
		if(childrenUIList != null && childrenUIList.size() > 0)
		{
			for(Sm_Permission_UIResourceTemplate children_Template : childrenUIList)
			{
				addUIResource(children_Template); 
			}
		}
	}
	
	/**
	 * 持久化UIResource权限资源
	 */
	public void saveUIResource(Sm_Permission_UIResource sm_Permission_UIResource, Sm_Permission_UIResourceTemplate template)
	{
		if(template == null) return;
		if(sm_Permission_UIResource == null)
		{
			sm_Permission_UIResource = new Sm_Permission_UIResource();
		}
		
		String theName = template.getTheName();
		sm_Permission_UIResource.setTheState(S_TheState.Normal);
		sm_Permission_UIResource.setBusiState(null);
		sm_Permission_UIResource.setBusiCode(template.getBusiCode());
		sm_Permission_UIResource.setCreateTimeStamp(System.currentTimeMillis());
		sm_Permission_UIResource.setTheName(theName);
		sm_Permission_UIResource.setTheOriginalName(theName);
		sm_Permission_UIResource.setLevelNumber(template.getLevelNumber());
		sm_Permission_UIResource.setParentLevelNumber(template.getParentLevelNumber());
		sm_Permission_UIResource.setTheIndex(template.getTheIndex());
		sm_Permission_UIResource.setTheResource(template.getTheResource());
		sm_Permission_UIResource.setTheType(S_UIResourceType.StrToIntVal.get(template.getTheType()));
		sm_Permission_UIResource.setIsDefault(template.getIsDefault());//是否是默认权限 S_YesNo
		sm_Permission_UIResource.setRemark(template.getRemark());
		sm_Permission_UIResource.setEditNum(template.getEditNum());
		
		sm_Permission_UIResourceDao.save(sm_Permission_UIResource);
	}
}
