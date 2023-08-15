package zhishusz.housepresell.util.loginsession;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

/**
 * Session监听
 * 
 * @author XSZ
 * @version 1.0
 *
 */
public class MySessionListener {

	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		MySessionContext.AddSession(httpSessionEvent.getSession());
	}

	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		HttpSession session = httpSessionEvent.getSession();
		MySessionContext.DelSession(session);
	}

}
