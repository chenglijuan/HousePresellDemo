package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleUIForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
import zhishusz.housepresell.database.dao.Sm_Permission_UIResourceDao;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：角色与UI权限对应关系
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Permission_RoleUIUpdateService
{
	@Autowired
	private Sm_Permission_RoleDao sm_Permission_RoleDao;
	@Autowired
	private Sm_Permission_UIResourceDao sm_Permission_UIResourceDao;
	
	public Properties execute(Sm_Permission_RoleUIForm model)
	{
		Properties properties = new MyProperties();
		
		Integer theState = model.getTheState();
		Long sm_Permission_RoleId = model.getSm_Permission_RoleId();
		Long sm_Permission_UIResourceId = model.getSm_Permission_UIResourceId();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(sm_Permission_RoleId == null || sm_Permission_RoleId < 1)
		{
			return MyBackInfo.fail(properties, "'sm_Permission_Role'不能为空");
		}
		if(sm_Permission_UIResourceId == null || sm_Permission_UIResourceId < 1)
		{
			return MyBackInfo.fail(properties, "'sm_Permission_UIResource'不能为空");
		}
		Sm_Permission_Role sm_Permission_Role = (Sm_Permission_Role)sm_Permission_RoleDao.findById(sm_Permission_RoleId);
		if(sm_Permission_Role == null)
		{
			return MyBackInfo.fail(properties, "'sm_Permission_Role(Id:" + sm_Permission_RoleId + ")'不存在");
		}
		Sm_Permission_UIResource sm_Permission_UIResource = (Sm_Permission_UIResource)sm_Permission_UIResourceDao.findById(sm_Permission_UIResourceId);
		if(sm_Permission_UIResource == null)
		{
			return MyBackInfo.fail(properties, "'sm_Permission_UIResource(Id:" + sm_Permission_UIResourceId + ")'不存在");
		}
	
		Long sm_Permission_RoleUIId = model.getTableId();
		Sm_Permission_Role sm_Permission_RoleUI = (Sm_Permission_Role)sm_Permission_RoleDao.findById(sm_Permission_RoleUIId);
		if(sm_Permission_RoleUI == null)
		{
			return MyBackInfo.fail(properties, "'Sm_Permission_RoleUI(Id:" + sm_Permission_RoleUIId + ")'不存在");
		}
		
		//TODO 
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
