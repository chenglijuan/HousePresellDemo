package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleCompanyTypeForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleCompanyTypeDao;
import zhishusz.housepresell.database.po.Sm_Permission_RoleCompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
	
/*
 * Service添加操作：角色与机构类型对应关系
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Permission_RoleCompanyTypeAddService
{
	@Autowired
	private Sm_Permission_RoleCompanyTypeDao sm_Permission_RoleCompanyTypeDao;
	@Autowired
	private Sm_Permission_RoleDao sm_Permission_RoleDao;
	
	public Properties execute(Sm_Permission_RoleCompanyTypeForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = model.getTheState();
		Long sm_Permission_RoleId = model.getSm_Permission_RoleId();
		String forCompanyType = model.getForCompanyType();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(sm_Permission_RoleId == null || sm_Permission_RoleId < 1)
		{
			return MyBackInfo.fail(properties, "'sm_Permission_Role'不能为空");
		}
		if(forCompanyType == null || forCompanyType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'forCompanyType'不能为空");
		}

		Sm_Permission_Role sm_Permission_Role = (Sm_Permission_Role)sm_Permission_RoleDao.findById(sm_Permission_RoleId);
		if(sm_Permission_Role == null)
		{
			return MyBackInfo.fail(properties, "'sm_Permission_Role'不能为空");
		}
	
		Sm_Permission_RoleCompanyType sm_Permission_RoleCompanyType = new Sm_Permission_RoleCompanyType();
		sm_Permission_RoleCompanyType.setTheState(theState);
		sm_Permission_RoleCompanyType.setSm_Permission_Role(sm_Permission_Role);
		sm_Permission_RoleCompanyType.setForCompanyType(forCompanyType);
		sm_Permission_RoleCompanyTypeDao.save(sm_Permission_RoleCompanyType);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
