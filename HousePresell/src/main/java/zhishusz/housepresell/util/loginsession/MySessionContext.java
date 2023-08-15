package zhishusz.housepresell.util.loginsession;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

/**
 * session操作
 * 
 * @author XSZ
 * @since 2018-12-19 10:59:21
 * @version 1.0
 *
 */
public class MySessionContext {

	@SuppressWarnings("rawtypes")
	private static HashMap sessionMap = new HashMap();

	@SuppressWarnings("unchecked")
	public static synchronized void AddSession(HttpSession session) {
		if (session != null) {
			sessionMap.put(session.getId(), session);
		}
	}

	public static synchronized void DelSession(HttpSession session) {
		if (session != null) {
			sessionMap.remove(session.getId());
		}
	}

	public static synchronized HttpSession getSession(String session_id) {
		if (session_id == null)
			return null;
		return (HttpSession) sessionMap.get(session_id);
	}

}
