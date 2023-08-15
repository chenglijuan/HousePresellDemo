package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Sm_Permission_RoleData;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：角色与数据权限对应关系
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_Permission_RoleDataRebuild extends RebuilderBase<Sm_Permission_RoleData>
{
	@Override
	public Properties getSimpleInfo(Sm_Permission_RoleData object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Sm_Permission_RoleData object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("sm_Permission_Role", object.getSm_Permission_Role());
		properties.put("sm_Permission_RoleId", object.getSm_Permission_Role().getTableId());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_Permission_RoleData> sm_Permission_RoleDataList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_Permission_RoleDataList != null)
		{
			for(Sm_Permission_RoleData object:sm_Permission_RoleDataList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("sm_Permission_Role", object.getSm_Permission_Role());
				properties.put("sm_Permission_RoleId", object.getSm_Permission_Role().getTableId());
				
				list.add(properties);
			}
		}
		return list;
	}
}
