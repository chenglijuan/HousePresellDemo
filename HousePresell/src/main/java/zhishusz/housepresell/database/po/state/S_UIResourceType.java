package zhishusz.housepresell.database.po.state;

import java.util.HashMap;

public class S_UIResourceType {
	public static final Integer RealityMenu = 1;	//实体菜单
	public static final Integer VirtualMenu = 2;	//虚拟菜单
	public static final Integer Button = 3;			//按钮
	public static final Integer TheResource = 4;	//链接
	
	public static final Integer Menu = 20;			//菜单（虚拟菜单|实体菜单）
	
	@SuppressWarnings("serial")
	public static final HashMap<String, Integer> StrToIntVal = new HashMap<String, Integer>(){{
		put("实体菜单",RealityMenu);
		put("虚拟菜单",VirtualMenu);
		put("按钮",Button);
		put("链接",TheResource);
	}};
}
