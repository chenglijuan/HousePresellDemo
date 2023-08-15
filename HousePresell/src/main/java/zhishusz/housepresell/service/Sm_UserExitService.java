package zhishusz.housepresell.service;

import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Sm_UserSignInLogDao;
import zhishusz.housepresell.database.po.Sm_UserSignInLog;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;

/**
 * 用户退出 1.清除登录的session 2.将用户登录状态置为未登录
 * 
 * @ClassName: Sm_UserExitService
 * @author: xushizhong
 * @date: 2018年12月19日13:51:32
 * @version V1.0
 *
 */
@Service
@Transactional
public class Sm_UserExitService {
	@Autowired
	private Sm_UserDao sm_UserDao;

	@Autowired
	private Sm_UserSignInLogDao sm_userSignInLogDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Sm_UserForm model, HttpServletRequest request) {
		Properties properties = new MyProperties();

		Long tableId = model.getTableId();

		HttpSession session = request.getSession();
		if (session != null) {
			ServletContext application = session.getServletContext();
			Map<String, Object> loginMap = (Map<String, Object>) application.getAttribute("loginMap");
			
			if(null != loginMap)
			{
				loginMap.remove(String.valueOf(tableId));
				application.setAttribute("loginMap", loginMap);
			}
			
			session.invalidate();
		}

		Sm_UserSignInLog sm_userSignInLog = new Sm_UserSignInLog();
		sm_userSignInLog.setCreateTimeStamp(new Date().getTime());
		sm_userSignInLog.setRealName("");
		sm_userSignInLog.setTheAccount("");
		sm_userSignInLog.setTheState(0);
		sm_userSignInLog.setTheType(2);
		sm_userSignInLog.setUserId(tableId+"");
		sm_userSignInLogDao.save(sm_userSignInLog);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
		
}
