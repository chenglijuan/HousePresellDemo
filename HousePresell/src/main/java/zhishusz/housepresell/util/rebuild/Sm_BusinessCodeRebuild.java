package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Sm_BusinessCode;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：业务编号
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_BusinessCodeRebuild extends RebuilderBase<Sm_BusinessCode>
{
	@Override
	public Properties getSimpleInfo(Sm_BusinessCode object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Sm_BusinessCode object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("userUpdate", object.getUserUpdate());
		properties.put("userUpdateId", object.getUserUpdate().getTableId());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("userRecord", object.getUserRecord());
		properties.put("userRecordId", object.getUserRecord().getTableId());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("busiCode", object.getBusiCode());
		properties.put("theYear", object.getTheYear());
		properties.put("theMonth", object.getTheMonth());
		properties.put("theDay", object.getTheDay());
		properties.put("ticketCount", object.getTicketCount());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_BusinessCode> sm_BusinessCodeList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_BusinessCodeList != null)
		{
			for(Sm_BusinessCode object:sm_BusinessCodeList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("userUpdate", object.getUserUpdate());
				properties.put("userUpdateId", object.getUserUpdate().getTableId());
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				properties.put("userRecord", object.getUserRecord());
				properties.put("userRecordId", object.getUserRecord().getTableId());
				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				properties.put("busiCode", object.getBusiCode());
				properties.put("theYear", object.getTheYear());
				properties.put("theMonth", object.getTheMonth());
				properties.put("theDay", object.getTheDay());
				properties.put("ticketCount", object.getTicketCount());
				
				list.add(properties);
			}
		}
		return list;
	}
}
