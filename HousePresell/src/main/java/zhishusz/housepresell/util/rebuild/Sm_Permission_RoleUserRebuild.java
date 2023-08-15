package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：角色与用户对应关系
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_Permission_RoleUserRebuild extends RebuilderBase<Sm_Permission_RoleUser>
{
	@Override
	public Properties getSimpleInfo(Sm_Permission_RoleUser object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Sm_Permission_RoleUser object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("sm_Permission_Role", object.getSm_Permission_Role());
		properties.put("sm_Permission_RoleId", object.getSm_Permission_Role().getTableId());
		properties.put("sm_User", object.getSm_User());
		properties.put("sm_UserId", object.getSm_User().getTableId());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_Permission_RoleUser> sm_Permission_RoleUserList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_Permission_RoleUserList != null)
		{
			for(Sm_Permission_RoleUser object:sm_Permission_RoleUserList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("sm_Permission_Role", object.getSm_Permission_Role());
				properties.put("sm_Permission_RoleId", object.getSm_Permission_Role().getTableId());
				properties.put("sm_User", object.getSm_User());
				properties.put("sm_UserId", object.getSm_User().getTableId());
				
				list.add(properties);
			}
		}
		return list;
	}
}
