package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：系统用户+机构用户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_UserDetailService
{
	@Autowired
	private Sm_UserDao sm_UserDao;

	public Properties execute(Sm_UserForm model)
	{
		Properties properties = new MyProperties();

		Long sm_UserId = model.getTableId();
		Sm_User sm_User = (Sm_User)sm_UserDao.findById(sm_UserId);
		if(sm_User == null || S_TheState.Deleted.equals(sm_User.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Sm_User(Id:" + sm_UserId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("sm_User", sm_User);

		return properties;
	}
}
