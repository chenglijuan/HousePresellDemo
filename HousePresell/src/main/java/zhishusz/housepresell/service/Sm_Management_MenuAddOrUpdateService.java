package zhishusz.housepresell.service;

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
import zhishusz.housepresell.database.po.state.S_YesNo;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;

/*
 * Service列表查询：添加或编辑菜单项
 * Company：ZhiShuSZ
 * 
 */
@Service
@Transactional
public class Sm_Management_MenuAddOrUpdateService
{
	@Autowired
	private Sm_Permission_UIResourceDao sm_Permission_UIResourceDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Sm_Permission_UIResourceForm model)
	{
		Properties properties = new MyProperties();
		MyString myString = MyString.getInstance();
		
		Long tableId = model.getTableId();
		
		if(tableId == null)
		{
			properties = executeForAdd(model);
		}
		else
		{
			properties = executeForUpdate(model);
		}
		
		if(!MyBackInfo.isSuccess(properties))
		{
			return MyBackInfo.fail(properties, myString.parse(properties.get(S_NormalFlag.info)));
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
	
	//添加
	@SuppressWarnings("unchecked")
	public Properties executeForAdd(Sm_Permission_UIResourceForm model)
	{
		Properties properties = new MyProperties();
		MyString myString = MyString.getInstance();
		MyDouble myDouble = MyDouble.getInstance();
		model.setOrderBy("theIndex,tableId asc");
		
		Integer theType = model.getTheType();//菜单类型 S_UIResourceType
		String theName = model.getTheName();//菜单名称
		String busiCode = model.getBusiCode();//业务编码
		Long parentUIIdForNowMenu = model.getParentUIIdForNowMenu();//上级菜单Id
		String theIndexStr = model.getTheIndexStr();
		Long resourceUIId = model.getResourceUIId();//（实体菜单必须传该参数）资源URL Id
		String iconPath = model.getIconPath();
		String remark = model.getRemark();//备注
		
		if(theType == null)
		{
			return MyBackInfo.fail(properties, "请选择菜单类型");
		}
		if(!S_UIResourceType.RealityMenu.equals(theType) && 
				!S_UIResourceType.VirtualMenu.equals(theType))
		{
			return MyBackInfo.fail(properties, "菜单类型不正确");
		}
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "请填写菜单名称");
		}
		if(busiCode == null || busiCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择业务编码");
		}
		//上级菜单可以不选
		Sm_Permission_UIResource parentUI = null; 
		if(parentUIIdForNowMenu != null && parentUIIdForNowMenu > 0)
		{
			parentUI = sm_Permission_UIResourceDao.findById(parentUIIdForNowMenu);
			if(parentUI == null)
			{
				return MyBackInfo.fail(properties, "该上级菜单不存在");
			}
		}
		if(theIndexStr == null || theIndexStr.length() == 0)
		{
			return MyBackInfo.fail(properties, "请填写排序编号");
		}
		if(!myString.checkNumber(theIndexStr, null, 10))
		{
			return MyBackInfo.fail(properties, "排序编号格式不正确");
		}
		Double theIndex = myDouble.parse(theIndexStr);
		if(theIndex == null || theIndex <= 0.0)
		{
			return MyBackInfo.fail(properties, "排序编号只能为从1开始的正数，例如：1或1.1等");
		}
		if(S_UIResourceType.RealityMenu.equals(theType) &&
				(resourceUIId == null || resourceUIId < 1))
		{
			return MyBackInfo.fail(properties, "请选择资源URL");
		}
		
		//===========================================================================================//
		Sm_User sm_User = sm_UserDao.findById(model.getUserId());
		Sm_Permission_UIResource url_resourceUI = sm_Permission_UIResourceDao.findById(resourceUIId); 
		if(S_UIResourceType.RealityMenu.equals(theType) &&
				url_resourceUI == null)
		{
			return MyBackInfo.fail(properties, "该资源URL不存在");
		}
		
		//业务编码重复应校验
		Sm_Permission_UIResourceForm checkBusiCodeModel = new Sm_Permission_UIResourceForm();
		checkBusiCodeModel.setBusiCode(busiCode);
		checkBusiCodeModel.setTheState(S_TheState.Normal);
		checkBusiCodeModel.setTheType(S_UIResourceType.Menu);
		Integer findByBusiCodeCheck_Size = sm_Permission_UIResourceDao.findByPage_Size(sm_Permission_UIResourceDao.getQuery_Size(sm_Permission_UIResourceDao.getBasicHQL(), checkBusiCodeModel));
		if(findByBusiCodeCheck_Size != null && findByBusiCodeCheck_Size > 0)
		{
			return MyBackInfo.fail(properties, "业务编码重复");
		}
		
		//根据“菜单名称”、“上级菜单”查询是否有重复项
		Sm_Permission_UIResourceForm checkModel = new Sm_Permission_UIResourceForm();
		checkModel.setTheName(theName);
		checkModel.setParentUI(parentUI);
		checkModel.setTheState(S_TheState.Normal);
		checkModel.setTheType(theType);
		Sm_Permission_UIResource sm_Permission_UIResource = sm_Permission_UIResourceDao.findOneByQuery_T(sm_Permission_UIResourceDao.getQuery(sm_Permission_UIResourceDao.getBasicHQL(), checkModel));
		if(sm_Permission_UIResource != null)
		{
			return MyBackInfo.fail(properties, "菜单名称重复");
		}
		
		String theResource = null;
		if(S_UIResourceType.RealityMenu.equals(theType) && url_resourceUI != null)
		{
			//实体菜单
			//获取链接下的按钮
			theResource = url_resourceUI.getTheResource();
		}
		
		//菜单项
		String parentLevelNumber = null;
		if(parentUI != null)
		{
			parentLevelNumber = parentUI.getLevelNumber();
		}
		else
		{
			parentLevelNumber = "0";//所有最高级目录，其父级LevelNumber设置为0；
		}
		
		sm_Permission_UIResource = new Sm_Permission_UIResource();

		sm_Permission_UIResource.setTheState(S_TheState.Normal);
		sm_Permission_UIResource.setBusiState(null);
		sm_Permission_UIResource.setBusiCode(busiCode);
		sm_Permission_UIResource.setUserStart(sm_User);
		sm_Permission_UIResource.setCreateTimeStamp(System.currentTimeMillis());
		sm_Permission_UIResource.setTheName(theName);
		sm_Permission_UIResource.setParentLevelNumber(parentLevelNumber);
		sm_Permission_UIResource.setTheIndex(theIndex);
		sm_Permission_UIResource.setTheResource(theResource);
		sm_Permission_UIResource.setResourceUI(url_resourceUI);
		sm_Permission_UIResource.setTheType(theType);
		sm_Permission_UIResource.setIsDefault(S_YesNo.No);
		sm_Permission_UIResource.setRemark(remark);
		sm_Permission_UIResource.setIconPath(iconPath);
		sm_Permission_UIResource.setParentUI(parentUI);
		
		sm_Permission_UIResourceDao.save(sm_Permission_UIResource);
		
		//若该菜单有父菜单，则需要根据 排序号（数字小的在前面，排序号一致则根据tableId数字小的在前面）排序，并重新生成：levelNumber("自身的层级编码，最高一级编码为：1，次一级为1_1，再次一级为1_1_1,")
		if(parentUI != null)
		{
			Sm_Permission_UIResourceForm childrenUIModel = new Sm_Permission_UIResourceForm();
			childrenUIModel.setParentUI(parentUI);
			childrenUIModel.setTheState(S_TheState.Normal);
			childrenUIModel.setTheType(S_UIResourceType.Menu);
			childrenUIModel.setOrderBy("tableId asc");//根据index排序要放在list中，放在此处会影响父子菜单的关联关系
			List<Sm_Permission_UIResource> childrenUIList = sm_Permission_UIResourceDao.findByPage(sm_Permission_UIResourceDao.getQuery(sm_Permission_UIResourceDao.getBasicHQL(), childrenUIModel));
			
			Integer i = 1;
			for(Sm_Permission_UIResource childrenUI : childrenUIList)
			{
				String levelNumber = parentLevelNumber + "_" + i;
				childrenUI.setLevelNumber(levelNumber);
				sm_Permission_UIResourceDao.save(childrenUI);
				i++;
			}
			
			parentUI.setChildrenUIList(childrenUIList);
			sm_Permission_UIResourceDao.save(parentUI);
		}
		else
		{
			//找出一级菜单的个数，在此基础上加1
			Sm_Permission_UIResourceForm levelOneUIModel = new Sm_Permission_UIResourceForm();
			levelOneUIModel.setTheState(S_TheState.Normal);
			levelOneUIModel.setTheType(S_UIResourceType.VirtualMenu);
			levelOneUIModel.setParentLevelNumber(parentLevelNumber);
			
			Integer levelOneMenu_size = sm_Permission_UIResourceDao.findByPage_Size(sm_Permission_UIResourceDao.getQuery_Size(sm_Permission_UIResourceDao.getBasicHQL(), levelOneUIModel));
			if(levelOneMenu_size == null) levelOneMenu_size = 0;
			
			sm_Permission_UIResource.setLevelNumber((levelOneMenu_size+1)+"");
			sm_Permission_UIResourceDao.save(sm_Permission_UIResource);
		}
		
		properties.put("sm_Permission_UIResource_New", sm_Permission_UIResource);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}

	//编辑
	@SuppressWarnings("unchecked")
	public Properties executeForUpdate(Sm_Permission_UIResourceForm model)
	{
		Properties properties = new MyProperties();
		MyString myString = MyString.getInstance();
		MyDouble myDouble = MyDouble.getInstance();
		model.setOrderBy("theIndex,tableId asc");
		
		Long tableId = model.getTableId();
		Integer theType = model.getTheType();//菜单类型
		String theName = model.getTheName();//菜单名称
		String busiCode = model.getBusiCode();//业务编码
		String theIndexStr = model.getTheIndexStr();
		Long resourceUIId = model.getResourceUIId();
		String iconPath = model.getIconPath();
		String remark = model.getRemark();//备注
		
		if(tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "请选择菜单");
		}
		if(theType == null)
		{
			return MyBackInfo.fail(properties, "请选择菜单类型");
		}
		if(!S_UIResourceType.RealityMenu.equals(theType) && 
				!S_UIResourceType.VirtualMenu.equals(theType))
		{
			return MyBackInfo.fail(properties, "菜单类型不正确");
		}
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "请填写菜单名称");
		}
		if(busiCode == null || busiCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择业务编码");
		}
		if(theIndexStr == null || theIndexStr.length() == 0)
		{
			return MyBackInfo.fail(properties, "请填写排序编号");
		}
		if(!myString.checkNumber(theIndexStr, null, 10))
		{
			return MyBackInfo.fail(properties, "排序编号格式不正确");
		}
		Double theIndex = myDouble.parse(theIndexStr);
		if(theIndex == null || theIndex <= 0.0)
		{
			return MyBackInfo.fail(properties, "排序编号只能为从1开始的正数，例如：1或1.1等");
		}
		if(S_UIResourceType.RealityMenu.equals(theType) &&
				(resourceUIId == null || resourceUIId < 1))
		{
			return MyBackInfo.fail(properties, "请选择资源URL");
		}
		
		//===========================================================================================//
		Sm_User sm_User = sm_UserDao.findById(model.getUserId());
		Sm_Permission_UIResource url_resourceUI = sm_Permission_UIResourceDao.findById(resourceUIId); 
		if(S_UIResourceType.RealityMenu.equals(theType) &&
				url_resourceUI == null)
		{
			return MyBackInfo.fail(properties, "该资源URL不存在");
		}
		
		Sm_Permission_UIResource sm_Permission_UIResource = sm_Permission_UIResourceDao.findById(tableId);
		if(sm_Permission_UIResource == null)
		{
			return MyBackInfo.fail(properties, "该菜单不存在");
		}
		
		//业务编码重复应校验
		Sm_Permission_UIResourceForm checkBusiCodeModel = new Sm_Permission_UIResourceForm();
		checkBusiCodeModel.setExceptTableId(sm_Permission_UIResource.getTableId());
		checkBusiCodeModel.setBusiCode(busiCode);
		checkBusiCodeModel.setTheState(S_TheState.Normal);
		checkBusiCodeModel.setTheType(S_UIResourceType.Menu);
		Integer findByBusiCodeCheck_Size = sm_Permission_UIResourceDao.findByPage_Size(sm_Permission_UIResourceDao.getQuery_Size(sm_Permission_UIResourceDao.getBasicHQL(), checkBusiCodeModel));
		if(findByBusiCodeCheck_Size != null && findByBusiCodeCheck_Size > 0)
		{
			return MyBackInfo.fail(properties, "业务编码重复");
		}
		
		//获取当前菜单的上级菜单
		Sm_Permission_UIResource parentUI = sm_Permission_UIResource.getParentUI();
		
		//根据“菜单名称”、“上级菜单”查询是否有重复项
		Sm_Permission_UIResourceForm checkModel = new Sm_Permission_UIResourceForm();
		checkModel.setTheName(theName);
		checkModel.setParentUI(parentUI);
		checkModel.setTheState(S_TheState.Normal);
		checkModel.setExceptTableId(sm_Permission_UIResource.getTableId());
		checkModel.setTheType(theType);
		Integer findByCheck_Size = sm_Permission_UIResourceDao.findByPage_Size(sm_Permission_UIResourceDao.getQuery_Size(sm_Permission_UIResourceDao.getBasicHQL(), checkModel));
		if(findByCheck_Size != null && findByCheck_Size > 0)
		{
			return MyBackInfo.fail(properties, "菜单名称重复");
		}
		
		String theResource = null;
		if(S_UIResourceType.RealityMenu.equals(theType) && url_resourceUI != null)
		{
			//实体菜单
			//获取链接下的按钮
			theResource = url_resourceUI.getTheResource();
		}
		
		//菜单项
		String parentLevelNumber = null;
		if(parentUI != null)
		{
			parentLevelNumber = parentUI.getLevelNumber();
		}
		else
		{
			parentLevelNumber = "0";//所有最高级目录，其父级LevelNumber设置为0；
		}
		
		sm_Permission_UIResource.setTheState(S_TheState.Normal);
		sm_Permission_UIResource.setBusiState(null);
		sm_Permission_UIResource.setBusiCode(busiCode);
		sm_Permission_UIResource.setUserStart(sm_User);
		sm_Permission_UIResource.setTheName(theName);
		sm_Permission_UIResource.setParentLevelNumber(parentLevelNumber);
		sm_Permission_UIResource.setTheIndex(theIndex);
		sm_Permission_UIResource.setTheResource(theResource);
		sm_Permission_UIResource.setResourceUI(url_resourceUI);
		sm_Permission_UIResource.setTheType(theType);
		sm_Permission_UIResource.setIsDefault(S_YesNo.No);
		sm_Permission_UIResource.setRemark(remark);
		sm_Permission_UIResource.setIconPath(iconPath);
		sm_Permission_UIResource.setParentUI(parentUI);
		//记录修改人信息
		sm_Permission_UIResource.setUserUpdate(sm_User);
		sm_Permission_UIResource.setLastUpdateTimeStamp(System.currentTimeMillis());
		
		sm_Permission_UIResourceDao.save(sm_Permission_UIResource);
		
		//若该菜单有父菜单，则需要根据 排序号（数字小的在前面，排序号一致则根据tableId数字小的在前面）排序，并重新生成：levelNumber("自身的层级编码，最高一级编码为：1，次一级为1_1，再次一级为1_1_1,")
		if(parentUI != null)
		{
			Sm_Permission_UIResourceForm childrenUIModel = new Sm_Permission_UIResourceForm();
			childrenUIModel.setParentUI(parentUI);
			childrenUIModel.setTheState(S_TheState.Normal);
			childrenUIModel.setTheType(S_UIResourceType.Menu);
			childrenUIModel.setOrderBy("tableId asc");//根据index排序要放在list中，放在此处会影响父子菜单的关联关系
			List<Sm_Permission_UIResource> childrenUIList = sm_Permission_UIResourceDao.findByPage(sm_Permission_UIResourceDao.getQuery(sm_Permission_UIResourceDao.getBasicHQL(), childrenUIModel));
			
			Integer i = 1;
			for(Sm_Permission_UIResource childrenUI : childrenUIList)
			{
				String levelNumber = parentLevelNumber + "_" + i;
				childrenUI.setLevelNumber(levelNumber);
				sm_Permission_UIResourceDao.save(childrenUI);
				i++;
			}
			
			parentUI.setChildrenUIList(childrenUIList);
			sm_Permission_UIResourceDao.save(parentUI);
		}
		
		properties.put("sm_Permission_UIResource_New", sm_Permission_UIResource);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
