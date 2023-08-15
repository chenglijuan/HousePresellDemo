package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_MessageTemplate_Cfg;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：审批流-消息模板配置
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_MessageTemplate_CfgRebuild extends RebuilderBase<Sm_MessageTemplate_Cfg>
{
	private MyDatetime myDatetime = MyDatetime.getInstance();

	@Override
	public Properties getSimpleInfo(Sm_MessageTemplate_Cfg object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		//列表页面
		properties.put("tableId", object.getTableId());

		if(object.getSm_baseParameter()!= null)
		{
			properties.put("busiCode",object.getSm_baseParameter().getTheValue()+"-"+object.getSm_baseParameter().getTheName());
		}
		properties.put("eCode", object.geteCode());  //消息模板编码
		properties.put("theName", object.getTheName()); //消息模板名称
		if(object.getUserUpdate() != null)
		{
			properties.put("lastUpdateUser",object.getUserUpdate().getTheName()); //最后维护人
		}
		properties.put("lastUpdateTimeStamp",myDatetime.dateToString2(object.getLastUpdateTimeStamp())); //最后维护时间

		return properties;
	}

	@Override
	public Properties getDetail(Sm_MessageTemplate_Cfg object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		//详情页面
		properties.put("tableId", object.getTableId());
		if(object.getSm_baseParameter()!= null)
		{
			Sm_BaseParameter sm_baseParameter = object.getSm_baseParameter();
			properties.put("busiId",sm_baseParameter.getTableId());
			properties.put("codeType",sm_baseParameter.getTheName()+"-"+sm_baseParameter.getTheValue());
		}
		properties.put("eCode", object.geteCode());  //消息模板编码
		properties.put("theName", object.getTheName()); //消息模板名称
		properties.put("theDescribe",object.getTheDescribe()); //模板描述
		properties.put("theTitle",object.getTheTitle()); //消息标题
		properties.put("theContent",object.getTheContent()); //消息内容

		if(object.getSm_permission_roleList() != null)
		{
			List<Properties> propertiesList = new ArrayList<Properties>();
			List<Sm_Permission_Role> sm_permission_roleList = object.getSm_permission_roleList();
			for (Sm_Permission_Role sm_permission_role : sm_permission_roleList)
			{
				Properties properties1 = new MyProperties();
				properties1.put("roleId",sm_permission_role.getTableId());
				properties1.put("roleName",sm_permission_role.getTheName());
				propertiesList.add(properties1);
			}
			properties.put("roleList",propertiesList);
		}
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_MessageTemplate_Cfg> sm_MessageTemplate_CfgList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_MessageTemplate_CfgList != null)
		{
			for(Sm_MessageTemplate_Cfg object:sm_MessageTemplate_CfgList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());
				properties.put("busiCode", object.getBusiCode()); //业务编码
				properties.put("eCode", object.geteCode());  //消息模板编码
				properties.put("theName", object.getTheName()); //消息模板名称
				properties.put("theDescribe",object.getTheDescribe()); //模板描述
				properties.put("theTitle",object.getTheTitle()); //消息标题
				properties.put("theContent",object.getTheContent()); //消息内容
				
				list.add(properties);
			}
		}
		return list;
	}
}
