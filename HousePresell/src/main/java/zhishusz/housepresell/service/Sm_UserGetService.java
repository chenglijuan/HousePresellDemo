package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/**
 * 获取ID为1的用户
 * 
 * @ClassName: Sm_UserLoginService
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月11日 下午14:04:59
 * @version V1.0
 *
 */
@Service
@Transactional
public class Sm_UserGetService
{
	@Autowired
	private Sm_UserDao sm_UserDao;

	public Properties execute(Sm_UserForm model)
	{
		Properties properties = new MyProperties();
		String pageHomeStr = "home";
		
		if(null == model.getUserId())
		{
			return MyBackInfo.fail(properties, "登录信息已失效，请重新登录！");
		}
		
		Sm_User sm_User = sm_UserDao.findById(model.getUserId());
		
		if(null == sm_User)
		{
			return MyBackInfo.fail(properties, "登录信息已失效，请重新登录！");
		}
		
		if(null != sm_User.getCompany())
		{
			String theType = sm_User.getCompany().getTheType();
			if("21".equals(theType))
			{
				pageHomeStr = "homeCooperativeAgency";
			}
		}

//		sm_User.setLoginPassword(null);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("sm_User", sm_User);
		properties.put("pageHomeStr", pageHomeStr);
		return properties;
	}
}
