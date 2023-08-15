package zhishusz.housepresell.database.po.state;

import java.util.HashMap;

//授权类别
public class S_RangeAuthType
{
	public static final Integer Area = 1;//区域
	public static final Integer Project = 2;//项目
	public static final Integer Building = 3;//楼幢
	
	@SuppressWarnings("serial")
	public static final HashMap<Integer, String> IntKeyToVal = new HashMap<Integer, String>(){{
		put(Area, "区域");
		put(Project, "项目");
		put(Building, "楼幢");
	}};
}
