package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Sm_eCode_Log;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：eCode记录
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_eCode_LogRebuild extends RebuilderBase<Sm_eCode_Log>
{
	@Override
	public Properties getSimpleInfo(Sm_eCode_Log object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Sm_eCode_Log object)
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
		properties.put("recordState", object.getRecordState());
		properties.put("recordRejectReason", object.getRecordRejectReason());
		properties.put("busiCode", object.getBusiCode());
		properties.put("theYear", object.getTheYear());
		properties.put("theMonth", object.getTheMonth());
		properties.put("theDay", object.getTheDay());
		properties.put("ticketCount", object.getTicketCount());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_eCode_Log> sm_eCode_LogList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_eCode_LogList != null)
		{
			for(Sm_eCode_Log object:sm_eCode_LogList)
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
				properties.put("recordState", object.getRecordState());
				properties.put("recordRejectReason", object.getRecordRejectReason());
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
