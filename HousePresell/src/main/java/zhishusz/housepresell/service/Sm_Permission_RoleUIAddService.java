package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleUIForm;
import zhishusz.housepresell.controller.form.Sm_Permission_UIResourceForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
import zhishusz.housepresell.database.dao.Sm_Permission_UIResourceDao;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_RoleBusiType;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：角色与UI权限对应关系
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Permission_RoleUIAddService
{
	@Autowired
	private Sm_Permission_RoleDao sm_Permission_RoleDao;
	@Autowired
	private Sm_Permission_UIResourceDao sm_Permission_UIResourceDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_Permission_RoleUIForm model)
	{
		//MyString myString = MyString.getInstance();
		Properties properties = new MyProperties();

		//角色Id
		Long tableId = model.getTableId();
		String busiType = model.getBusiType();//是否启用 S_RoleBusiType
		Long[] btnCheckArr = model.getBtnCheckArr();//用户勾选的菜单Id数组
		Long[] menuCheckArr = model.getMenuCheckArr();//用户勾选的功能按钮Id数组
		
		if(tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "请选择角色");
		}
		if(busiType == null || busiType.length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择状态");
		}
		
		Sm_Permission_Role sm_Permission_Role = sm_Permission_RoleDao.findById(tableId);
		if(sm_Permission_Role == null)
		{
			return MyBackInfo.fail(properties, "该角色不存在");
		}
		
		List<Sm_Permission_UIResource> menuCheckList = new ArrayList<Sm_Permission_UIResource>();
		List<Sm_Permission_UIResource> btnCheckList = new ArrayList<Sm_Permission_UIResource>();
		if(btnCheckArr != null && btnCheckArr.length > 0)
		{
			Sm_Permission_UIResourceForm sm_Permission_UIResourceForm = new Sm_Permission_UIResourceForm();
			sm_Permission_UIResourceForm.setTheState(S_TheState.Normal);
			//String inIdArrStr = myString.getSqlIdIn(btnCheckArr);
			sm_Permission_UIResourceForm.setInIdArr(btnCheckArr);
			btnCheckList = sm_Permission_UIResourceDao.findByPage(sm_Permission_UIResourceDao.getQuery(sm_Permission_UIResourceDao.getHQLForRoleUIAddOrUpdate(), sm_Permission_UIResourceForm));
		}
		if(menuCheckArr != null && menuCheckArr.length > 0)
		{
			Sm_Permission_UIResourceForm sm_Permission_UIResourceForm = new Sm_Permission_UIResourceForm();
			sm_Permission_UIResourceForm.setTheState(S_TheState.Normal);
			//String inIdArrStr = myString.getSqlIdIn(menuCheckArr);
			sm_Permission_UIResourceForm.setInIdArr(menuCheckArr);
			menuCheckList = sm_Permission_UIResourceDao.findByPage(sm_Permission_UIResourceDao.getQuery(sm_Permission_UIResourceDao.getHQLForRoleUIAddOrUpdate(), sm_Permission_UIResourceForm));
		}
		
		//合并List
		menuCheckList.addAll(btnCheckList);
		sm_Permission_Role.setUiResourceList(menuCheckList);
		
		//是否启用
		if(S_RoleBusiType.Valid.equals(busiType))
		{
			sm_Permission_Role.setEnableTimeStamp(System.currentTimeMillis());
			sm_Permission_Role.setBusiType(busiType);
		}
		else if(S_RoleBusiType.InValid.equals(busiType))
		{
			sm_Permission_Role.setDownTimeStamp(System.currentTimeMillis());
			sm_Permission_Role.setBusiType(busiType);
		}
		
		sm_Permission_RoleDao.save(sm_Permission_Role);
		
		properties.put("sm_Permission_Role", sm_Permission_Role);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
