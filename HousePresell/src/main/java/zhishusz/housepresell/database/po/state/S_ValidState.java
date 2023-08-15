package zhishusz.housepresell.database.po.state;

import java.util.HashMap;

/**
 * 启用状态 
 * @author http://zhishusz
 */
public class S_ValidState
{
	public static final String InValid = "0"; //停用
	public static final String Valid = "1";	 //启用
	
	@SuppressWarnings("serial")
	public static final HashMap<String, String> ValToStrVal = new HashMap<String, String>(){{
		put(InValid, "停用");
		put(Valid, "启用");
	}};
}
