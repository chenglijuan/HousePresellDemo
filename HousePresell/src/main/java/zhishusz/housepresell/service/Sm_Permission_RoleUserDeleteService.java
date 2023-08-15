package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：角色与用户对应关系
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Permission_RoleUserDeleteService
{
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;

	public Properties execute(Sm_Permission_RoleUserForm model)
	{
		Properties properties = new MyProperties();

		Long sm_Permission_RoleUserId = model.getTableId();
		Sm_Permission_RoleUser sm_Permission_RoleUser = (Sm_Permission_RoleUser)sm_Permission_RoleUserDao.findById(sm_Permission_RoleUserId);
		if(sm_Permission_RoleUser == null)
		{
			return MyBackInfo.fail(properties, "'Sm_Permission_RoleUser(Id:" + sm_Permission_RoleUserId + ")'不存在");
		}
		
		sm_Permission_RoleUser.setTheState(S_TheState.Deleted);
		sm_Permission_RoleUserDao.save(sm_Permission_RoleUser);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
