package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：参数定义
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_BaseParameterRebuild extends RebuilderBase<Sm_BaseParameter>
{
	@Override
	public Properties getSimpleInfo(Sm_BaseParameter object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("theName", object.getTheName());
		properties.put("theValue", object.getTheValue());
		
		return properties;
	}

	@Override
	public Properties getDetail(Sm_BaseParameter object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("userStart", object.getUserStart());
		if(object.getUserStart() != null)
		{
			properties.put("userStartId", object.getUserStart().getTableId());
		}
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("parentParameter", object.getParentParameter());
		if(object.getParentParameter() != null)
		{
			properties.put("parentParameterId", object.getParentParameter().getTableId());
		}
		properties.put("theName", object.getTheName());
		properties.put("theValue", object.getTheValue());
		properties.put("validDateFrom", object.getValidDateFrom());
		properties.put("validDateTo", object.getValidDateTo());
		properties.put("theVersion", object.getTheVersion());
		properties.put("parametertype", object.getParametertype());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_BaseParameter> sm_BaseParameterList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_BaseParameterList != null)
		{
			for(Sm_BaseParameter object:sm_BaseParameterList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				properties.put("parentParameter", object.getParentParameter());
				properties.put("parentParameterId", object.getParentParameter().getTableId());
				properties.put("theName", object.getTheName());
				properties.put("theValue", object.getTheValue());
				properties.put("validDateFrom", object.getValidDateFrom());
				properties.put("validDateTo", object.getValidDateTo());
				properties.put("theVersion", object.getTheVersion());
				
				list.add(properties);
			}
		}
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List executeForSelectList(List<Sm_BaseParameter> sm_BaseParameterList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (sm_BaseParameterList != null)
		{
			for (Sm_BaseParameter object : sm_BaseParameterList)
			{
				Properties properties = new MyProperties();

				properties.put("theName", object.getTheName());
				properties.put("tableId", object.getTableId());
				properties.put("busiCode",object.getTheValue());
				properties.put("busiType", object.getTheName());
				properties.put("theValue",object.getTheValue());
				
				list.add(properties);
			}
		}
		return list;
	}
}
