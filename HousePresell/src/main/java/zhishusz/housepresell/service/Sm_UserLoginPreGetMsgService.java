package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_Permission_UIResourceForm;
import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_ValidState;
import zhishusz.housepresell.util.AesUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/**
 * 用户登录
 * 
 * @ClassName: Sm_UserLoginService
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月3日 下午1:58:33
 * @version V1.0
 *
 */
@Service
@Transactional
public class Sm_UserLoginPreGetMsgService
{
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Sm_UserLoginService sm_UserLoginService;	
	@Autowired
	private Sm_Permission_UIResourceLoginForSideBarInitService sm_Permission_UIResourceLoginForSideBarInitService;

	@SuppressWarnings({
			"unchecked", "static-access"
	})
	public Properties execute(Sm_UserForm model,HttpServletRequest request)
	{
		Properties properties = new MyProperties();

		/*
		 * xsz by time 2018-9-3 14:06:25
		 * 用户登录信息控制：
		 * 瞎做的
		 * 
		 */
		String theName = model.getTheName();// 获取登录用户名
		String loginPassword = model.getLoginPassword();// 登录密码
		String ukeyNumber = model.getUkeyNumber();//UKey序列号
		String requestTime = model.getRequestTime();// 请求次数
		

		if (null == theName || theName.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请输入用户名");
		}
		if (null == loginPassword || loginPassword.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请输入密码");
		}
		

		// 设置查询用户名
		Sm_UserForm loginModel = new Sm_UserForm();
		loginModel.setTheAccount(theName);
		loginModel.setTheState(S_TheState.Normal);
		Integer isEncrypt = null;
		
		Sm_User sm_User = new Sm_User();
		// 根据用户名查询是否 存在相关信息
		Integer totalCount = sm_UserDao.findByPage_Size(sm_UserDao.getQuery_Size(sm_UserDao.getBasicHQL(), loginModel));

		List<Sm_User> sm_UserList = new ArrayList<Sm_User>();
		if (totalCount == 1)
		{
			/*
			 * 有且只有一条用户信息匹配
			 * 进行登录相关操作
			 */
			sm_UserList = sm_UserDao.findByPage(sm_UserDao.getQuery(sm_UserDao.getBasicHQL(), loginModel));

			if (null == sm_UserList || sm_UserList.size() == 0)
			{
				// 未查询到相关信息
				return MyBackInfo.fail(properties, "用户名 '" + theName + "' 不存在");
			}
			else
			{
				// 根据查询到的用户信息进行密码校验，需要用户状态为启用状态
				sm_User = sm_UserList.get(0);

				isEncrypt = sm_User.getIsEncrypt();
				
				if( null != requestTime && "1".equals(requestTime))
				{
					//用户加密但是无加密设备，返回前端进行二次读取加密设备登录
					if(( isEncrypt == 1 && (ukeyNumber == null || ukeyNumber.length() < 1)))
					{
						properties.put("isEncrypt", isEncrypt);
						properties.put(S_NormalFlag.result, S_NormalFlag.success);
						properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
						return properties;
					}
					else if((isEncrypt == 1 && ukeyNumber != null) || isEncrypt == 0)
					{
						//用户加密且序列号不为空或者未加密，进行登录操作
						properties = sm_UserLoginService.execute(model,request);
						return properties;
					}
					else
					{
						return MyBackInfo.fail(properties, "校验参数不正确！");
					}
				}
				else if( null != requestTime && "2".equals(requestTime))
				{
					//用户加密且序列号不为空或者未加密，进行登录操作
					if( (isEncrypt == 1 && ukeyNumber != null) || isEncrypt == 0)
					{
						properties = sm_UserLoginService.execute(model,request);
						return properties;
					}
					else if( isEncrypt == 1 && (ukeyNumber == null || ukeyNumber.length() < 1) )
					{
						//用户加密且序列号为空或者未加密，提醒用户插入加密设备
						return MyBackInfo.fail(properties, "请插入加密设备！");
					}
					else
					{
						return MyBackInfo.fail(properties, "校验参数不正确！");
					}
					
				}
				else
				{
					return MyBackInfo.fail(properties, "校验参数不正确！");
				}
			}
		}
		else if (totalCount == 0)
		{
			// 未查询到相关信息
			return MyBackInfo.fail(properties, "用户名 '" + theName + "' 不存在");
		}
		else
		{
			// 存在多条用户信息
			return MyBackInfo.fail(properties, "用户名 '" + theName + "' 存在多条信息");
		}
	}
}
