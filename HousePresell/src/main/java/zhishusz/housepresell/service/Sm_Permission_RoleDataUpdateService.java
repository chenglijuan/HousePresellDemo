package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleDataForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDataDao;
import zhishusz.housepresell.database.po.Sm_Permission_RoleData;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;

/*
 * Service更新操作：角色与数据权限对应关系
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Permission_RoleDataUpdateService
{
	@Autowired
	private Sm_Permission_RoleDataDao sm_Permission_RoleDataDao;
	@Autowired
	private Sm_Permission_RoleDao sm_Permission_RoleDao;
	
	public Properties execute(Sm_Permission_RoleDataForm model)
	{
		Properties properties = new MyProperties();
		
		Integer theState = model.getTheState();
		Long sm_Permission_RoleId = model.getSm_Permission_RoleId();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(sm_Permission_RoleId == null || sm_Permission_RoleId < 1)
		{
			return MyBackInfo.fail(properties, "'sm_Permission_Role'不能为空");
		}
		Sm_Permission_Role sm_Permission_Role = (Sm_Permission_Role)sm_Permission_RoleDao.findById(sm_Permission_RoleId);
		if(sm_Permission_Role == null)
		{
			return MyBackInfo.fail(properties, "'sm_Permission_Role(Id:" + sm_Permission_RoleId + ")'不存在");
		}
	
		Long sm_Permission_RoleDataId = model.getTableId();
		Sm_Permission_RoleData sm_Permission_RoleData = (Sm_Permission_RoleData)sm_Permission_RoleDataDao.findById(sm_Permission_RoleDataId);
		if(sm_Permission_RoleData == null)
		{
			return MyBackInfo.fail(properties, "'Sm_Permission_RoleData(Id:" + sm_Permission_RoleDataId + ")'不存在");
		}
		
		sm_Permission_RoleData.setTheState(theState);
		sm_Permission_RoleData.setSm_Permission_Role(sm_Permission_Role);
	
		sm_Permission_RoleDataDao.save(sm_Permission_RoleData);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
