package zhishusz.housepresell.service;

import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_Permission_UIResourceForm;
import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.controller.form.Sm_UserStateForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Sm_UserSignInLogDao;
import zhishusz.housepresell.database.dao.Sm_UserStateDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_UserSignInLog;
import zhishusz.housepresell.database.po.state.S_LoginState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_ValidState;
import zhishusz.housepresell.util.AesUtil;
import zhishusz.housepresell.util.EhCacheUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.loginsession.MySessionContext;

/**
 * 用户登录
 * 
 * @ClassName: Sm_UserLoginService
 * @Description:
 * @author: xushizhong
 * @date: 2018年9月3日 下午1:58:33
 * @version V1.0
 *
 */
@Service
@Transactional
public class Sm_UserLoginService
{
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Sm_UserStateDao sm_UserStateDao;//用户登录状态

	@Autowired
	private Sm_UserSignInLogDao sm_userSignInLogDao;
	@Autowired
	private Sm_Permission_UIResourceLoginForSideBarInitService sm_Permission_UIResourceLoginForSideBarInitService;

	@SuppressWarnings({
			"unchecked", "static-access"
	})
	public Properties execute(Sm_UserForm model,HttpServletRequest request)
	{
		Properties properties = new MyProperties();
		
		//登录时清除所有缓存信息
//		EhCacheUtil.getInstance().removeAll();
		/*
		 * xsz by time 2018-9-3 14:06:25
		 * 用户登录信息控制：
		 * ==》先根据用户名查询出指定用户信息，再匹配密码
		 * ==》密码错误规则，登录错误时（密码错误次数）errPwdCount加一
		 * ==》登录成功后更新：
		 * 1.密码错误次数清零
		 * 2.更新最后登录时间lastLoginTimeStamp
		 * 
		 */
		String theName = model.getTheName();// 获取登录用户名
		String loginPassword = model.getLoginPassword();// 登录密码
		String ukeyNumber = model.getUkeyNumber();//UKey序列号

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

				// 判断是使用ukey
				if( 1 ==sm_User.getIsEncrypt())
				{
					if (null == ukeyNumber || ukeyNumber.trim().isEmpty())
					{
						return MyBackInfo.fail(properties, "未读取到设备码，请重试！");
					}
					if (!ukeyNumber.equals(sm_User.getUkeyNumber()))
					{
						return MyBackInfo.fail(properties, "设备码有误，请重试！");
					}
				}
				
				if (sm_User.getLockUntil() == null || sm_User.getLockUntil() <= System.currentTimeMillis())
				{
					if (!S_ValidState.Valid.equals(sm_User.getBusiState()))
					{
						return MyBackInfo.fail(properties, "用户名 '" + theName + "' 未启用，请联系管理员");
					}

					if (AesUtil.getInstance().encrypt(loginPassword).equals(sm_User.getLoginPassword())
							|| loginPassword.equals(sm_User.getLoginPassword()))
					{
						/*
						 * 登录成功，更新最后登录时间,清除登录错误次数
						 * 同时将登录成功后的用户信息存入session中
						 * 
						 * xsz by time 2018-12-19 10:07:40
						 * 校验该用户是否处于登录状态:
						 * 如果处于登录状态，则返回提示信息：该用户已处于登录状态，是否继续登录？
						 * 如果未登录，则直接登录成功
						 */
						Long tableId = sm_User.getTableId();
						String user_id = String.valueOf(tableId);
						HttpSession session = request.getSession();
						ServletContext application = session.getServletContext();
						Map<String, Object> loginMap = (Map<String, Object>) application.getAttribute("loginMap");
						if (loginMap == null) {
						    loginMap = new HashMap<String, Object>();
						}
						
						//强制登录
//						String isCommit = model.getIsCommit();
//						if(!"1".equals(isCommit))
//						{
						/*
						 * xsz by time 2019-3-12 19:07:08
						 * 将用户唯一性登录校验去除
						 */
						/*for (String key : loginMap.keySet()) {
						    if (user_id.equals(key)) {
						    	
						    	properties.put("isMsg", "1");//设置用户登录在线提示标记（如果无，则直接提示错误信息；如果有，则弹出选择对话框）
								properties.put(S_NormalFlag.result, S_NormalFlag.fail);
								properties.put(S_NormalFlag.info, "用户"+theName+"已处于登录状态，是否继续登录？");
	
								return properties;
						    }
						}*/
//						}
						
						loginMap.put(user_id,session.getId());
						
						// 将用户保存在session当中
						session.setAttribute(S_NormalFlag.user, sm_User);
						session.setAttribute(S_NormalFlag.userId, sm_User.getTableId());
						session.setAttribute(S_NormalFlag.userName, sm_User.getTheName());
						
						// session 销毁时间
						session.setMaxInactiveInterval(10*60);
						
						// 存入session
						MySessionContext.AddSession(session);
						application.setAttribute("loginMap", loginMap);
						
						sm_User.setLastLoginTimeStamp(System.currentTimeMillis());
						sm_User.setErrPwdCount(0);
						sm_UserDao.save(sm_User);

						Sm_UserSignInLog sm_userSignInLog = new Sm_UserSignInLog();
						sm_userSignInLog.setCreateTimeStamp(new Date().getTime());
						sm_userSignInLog.setRealName(sm_User.getTheName());
						sm_userSignInLog.setTheAccount(sm_User.getTheAccount());
						sm_userSignInLog.setTheState(0);
						sm_userSignInLog.setTheType(1);
						sm_userSignInLog.setUserId(sm_User.getTableId()+"");
						sm_userSignInLogDao.save(sm_userSignInLog);
					}
					else
					{
						/*
						 * 登录密码错误，密码错误次数加一
						 * 登录次数超过十次，则将此用户信息设置为停用状态
						 */
						sm_User.setErrPwdCount(null == sm_User.getErrPwdCount()?0:sm_User.getErrPwdCount() + 1);

						if (sm_User.getErrPwdCount() == 10)
						{
							// sm_User.setBusiState(S_ValidState.InValid);

							// 用户锁定
							// MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis());

							String dayAfter = MyDatetime.getInstance().getSpecifiedDayAfter(
									MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));

							Long timestamp = MyDatetime.getInstance().parseTimestamp(dayAfter, "00:00:00");

							sm_User.setLockUntil(timestamp);

						}

						sm_UserDao.save(sm_User);
						return MyBackInfo.fail(properties, "密码错误");
					}
				}
				else
				{
					// 停用状态
					return MyBackInfo.fail(properties, "用户名 '" + theName + "' 该用户已锁定");
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

		// ======== 动态获取 当前用户的左侧菜单，并且实现，左侧菜单的动态生成 Start
		// ========================//
		// 根据当前用户，加载其所属的左侧菜单数据，并静态化左侧菜单栏
		Sm_Permission_UIResourceForm uiModel = new Sm_Permission_UIResourceForm();
		uiModel.setUser(sm_User);
		sm_Permission_UIResourceLoginForSideBarInitService.executeForSideBarInit(uiModel);
		// ======== 动态获取 当前用户的左侧菜单，并且实现，左侧菜单的动态生成 End ========================//

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("sm_User", sm_User);
		properties.put("isEncrypt", isEncrypt);
//		properties.put("companyType", sm_User.getCompany().getTheType());

		return properties;
	}
}
