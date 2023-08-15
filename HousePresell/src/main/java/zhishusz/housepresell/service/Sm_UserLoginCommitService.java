package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
import zhishusz.housepresell.database.dao.Sm_UserStateDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_LoginState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.loginsession.MySessionContext;

/**
 * 用户登录 校验用户是否处于未登录状态
 * 
 * @ClassName: Sm_UserLoginPreCommitGetMsgService
 * @author: xushizhong
 * @date: 2018年12月19日09:56:20
 * @version V1.0
 *
 */
@Service
@Transactional
public class Sm_UserLoginCommitService {
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Sm_Permission_UIResourceLoginForSideBarInitService sm_Permission_UIResourceLoginForSideBarInitService;// 静态化菜单
//	@Autowired
//	private Sm_UserStateDao sm_UserStateDao;// 用户登录状态

	@SuppressWarnings("unchecked")
	public Properties execute(Sm_UserForm model, HttpServletRequest request) {
		Properties properties = new MyProperties();

		String theName = model.getTheName();// 获取登录用户名
		Integer isEncrypt = model.getIsEncrypt();

		// 设置查询用户名
		Sm_UserForm loginModel = new Sm_UserForm();
		loginModel.setTheAccount(theName);
		loginModel.setTheState(S_TheState.Normal);

		Sm_User sm_User = new Sm_User();
		// 根据用户名查询是否 存在相关信息
		List<Sm_User> sm_UserList = new ArrayList<Sm_User>();
		sm_UserList = sm_UserDao.findByPage(sm_UserDao.getQuery(sm_UserDao.getBasicHQL(), loginModel));

		// 获取登录用户
		sm_User = sm_UserList.get(0);
		
		
		try
		{
			//强制登录
			Long tableId = sm_User.getTableId();
			String user_id = String.valueOf(tableId);
			
			HttpSession session = request.getSession();
			ServletContext application = session.getServletContext();
			
			Map<String, Object> loginMap = (Map<String, Object>) application.getAttribute("loginMap");
			if (loginMap == null) {
			    loginMap = new HashMap<String, Object>();
			}
			
			for (String key : loginMap.keySet()) {
			    if (user_id.equals(key)) {
			    	
			    	String sessionId = (String) loginMap.get(key);
			    	HttpSession session1 = MySessionContext.getSession(sessionId);
					if (null != session1)
						session1.invalidate();
					MySessionContext.DelSession(session1);
					
					//同时清除loginMap中的用户信息
					loginMap.remove(key);
			    }
			}
			
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

			// 登录成功，更新最后登录时间,清除登录错误次数
			sm_User.setLastLoginTimeStamp(System.currentTimeMillis());
			sm_User.setErrPwdCount(0);
			
			sm_UserDao.save(sm_User);
			
		}
		catch (Exception e)
		{
			properties.put("loginState", e.getMessage());
		}

		sm_UserDao.save(sm_User);

		// ======== 动态获取 当前用户的左侧菜单，并且实现，左侧菜单的动态生成 Start ========================
		// 根据当前用户，加载其所属的左侧菜单数据，并静态化左侧菜单栏==============//
		Sm_Permission_UIResourceForm uiModel = new Sm_Permission_UIResourceForm();
		uiModel.setUser(sm_User);
		sm_Permission_UIResourceLoginForSideBarInitService.executeForSideBarInit(uiModel);
		// ======== 动态获取 当前用户的左侧菜单，并且实现，左侧菜单的动态生成 End ========================//
		
//		properties.put("isEncrypt", isEncrypt);
		properties.put("sm_User", sm_User);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		

		return properties;
	}
}
