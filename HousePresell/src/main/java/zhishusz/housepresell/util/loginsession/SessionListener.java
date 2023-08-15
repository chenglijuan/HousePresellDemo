package zhishusz.housepresell.util.loginsession;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import zhishusz.housepresell.database.po.Sm_User;

/**
 * session监听
 * @author XSZ
 *
 */
public class SessionListener implements HttpSessionListener
{

	@Override
	public void sessionCreated(HttpSessionEvent se)
	{

	}

	@SuppressWarnings("unchecked")
	@Override
	public void sessionDestroyed(HttpSessionEvent se)
	{
		HttpSession session = se.getSession();
		
		Sm_User user = (Sm_User) session.getAttribute("user");
		if (user != null) 
		{
			ServletContext application = session.getServletContext();
			Map<String, Object> loginMap = (Map<String, Object>) application.getAttribute("loginMap");
			loginMap.remove(String.valueOf(user.getTableId()));
			application.setAttribute("loginMap", loginMap);
			session.removeAttribute("user");
		}
	}

}
