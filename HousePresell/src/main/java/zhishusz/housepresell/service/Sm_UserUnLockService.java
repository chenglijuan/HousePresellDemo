package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service用户解锁
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_UserUnLockService
{
	@Autowired
	private Sm_UserDao sm_UserDao;

	public Properties execute(Sm_UserForm model)
	{
		Properties properties = new MyProperties();

		Long tableId = model.getTableId();

		if(tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "请选择需要解锁的用户");
		}

		Sm_User sm_User = sm_UserDao.findById(tableId);
		if(sm_User == null)
		{
			return MyBackInfo.fail(properties, "选择解锁的用户不存在");
		}
		
		sm_User.setLockUntil(System.currentTimeMillis());
		sm_UserDao.save(sm_User);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
