package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.database.po.Sm_StreetInfo;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：Sm_StreetInfo
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_StreetInfoRebuild extends RebuilderBase<Sm_StreetInfo>
{
	@Override
	public Properties getSimpleInfo(Sm_StreetInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("theName", object.getTheName());
		
		return properties;
	}

	@Override
	public Properties getDetail(Sm_StreetInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("userRecord", object.getUserRecord());
		properties.put("userRecordId", object.getUserRecord().getTableId());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("cityRegion", object.getCityRegion());
		properties.put("cityRegionId", object.getCityRegion().getTableId());
		properties.put("theName", object.getTheName());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_StreetInfo> sm_StreetInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_StreetInfoList != null)
		{
			for(Sm_StreetInfo object:sm_StreetInfoList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				properties.put("userRecord", object.getUserRecord());
				properties.put("userRecordId", object.getUserRecord().getTableId());
				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				properties.put("cityRegion", object.getCityRegion());
				properties.put("cityRegionId", object.getCityRegion().getTableId());
				properties.put("theName", object.getTheName());
				
				list.add(properties);
			}
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public List executeForSelect(List<Sm_StreetInfo> sm_StreetInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_StreetInfoList != null)
		{
			for(Sm_StreetInfo object:sm_StreetInfoList)
			{
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());
				properties.put("theName", object.getTheName());
				
				list.add(properties);
			}
		}
		return list;
	}

	@Override
	public List<Properties> executeForSelectList(List<Sm_StreetInfo> sm_StreetInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_StreetInfoList != null)
		{
			for(Sm_StreetInfo object:sm_StreetInfoList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());
				properties.put("theName", object.getTheName());

				list.add(properties);
			}
		}
		return list;
	}
}
