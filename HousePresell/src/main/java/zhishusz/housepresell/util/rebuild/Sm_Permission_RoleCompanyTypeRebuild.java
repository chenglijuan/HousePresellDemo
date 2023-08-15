package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Sm_Permission_RoleCompanyType;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：角色与机构类型对应关系
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_Permission_RoleCompanyTypeRebuild extends RebuilderBase<Sm_Permission_RoleCompanyType>
{
	@Override
	public Properties getSimpleInfo(Sm_Permission_RoleCompanyType object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Sm_Permission_RoleCompanyType object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("sm_Permission_Role", object.getSm_Permission_Role());
		properties.put("sm_Permission_RoleId", object.getSm_Permission_Role().getTableId());
		properties.put("forCompanyType", object.getForCompanyType());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_Permission_RoleCompanyType> sm_Permission_RoleCompanyTypeList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_Permission_RoleCompanyTypeList != null)
		{
			for(Sm_Permission_RoleCompanyType object:sm_Permission_RoleCompanyTypeList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("sm_Permission_Role", object.getSm_Permission_Role());
				properties.put("sm_Permission_RoleId", object.getSm_Permission_Role().getTableId());
				properties.put("forCompanyType", object.getForCompanyType());
				
				list.add(properties);
			}
		}
		return list;
	}
}
