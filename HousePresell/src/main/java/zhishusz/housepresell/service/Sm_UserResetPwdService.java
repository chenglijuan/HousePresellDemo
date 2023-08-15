package zhishusz.housepresell.service;

import java.util.Date;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.AesUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.PwdUtil;

/*
 * Service用户重置密码
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_UserResetPwdService
{
	@Autowired
	private Sm_UserDao sm_UserDao;

	public Properties execute(Sm_UserForm model)
	{
		Properties properties = new MyProperties();

		Long tableId = model.getTableId();

		if(tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "请选择需要重置密码的用户");
		}
		Sm_User sm_User = sm_UserDao.findById(tableId);
		if(sm_User == null)
		{
			return MyBackInfo.fail(properties, "选择重置密码的用户不存在");
		}

		//sm_User.setLoginPassword(AesUtil.getInstance().encrypt("000000"));
		sm_User.setLoginPassword(AesUtil.getInstance().encrypt(PwdUtil.DEFAULTPWD));
		//设置密码过期时间为注册后的90天
		long time = new Date().getTime() + 24 * 3600 * 1000l * 90;
		sm_User.setPwdExpireTimeStamp(time);
		
		sm_UserDao.save(sm_User);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
