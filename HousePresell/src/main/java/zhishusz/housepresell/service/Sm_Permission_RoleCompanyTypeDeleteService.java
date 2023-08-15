package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleCompanyTypeForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleCompanyTypeDao;
import zhishusz.housepresell.database.po.Sm_Permission_RoleCompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：角色与机构类型对应关系
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Permission_RoleCompanyTypeDeleteService
{
	@Autowired
	private Sm_Permission_RoleCompanyTypeDao sm_Permission_RoleCompanyTypeDao;

	public Properties execute(Sm_Permission_RoleCompanyTypeForm model)
	{
		Properties properties = new MyProperties();

		Long sm_Permission_RoleCompanyTypeId = model.getTableId();
		Sm_Permission_RoleCompanyType sm_Permission_RoleCompanyType = (Sm_Permission_RoleCompanyType)sm_Permission_RoleCompanyTypeDao.findById(sm_Permission_RoleCompanyTypeId);
		if(sm_Permission_RoleCompanyType == null)
		{
			return MyBackInfo.fail(properties, "'Sm_Permission_RoleCompanyType(Id:" + sm_Permission_RoleCompanyTypeId + ")'不存在");
		}
		
		sm_Permission_RoleCompanyType.setTheState(S_TheState.Deleted);
		sm_Permission_RoleCompanyTypeDao.save(sm_Permission_RoleCompanyType);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
