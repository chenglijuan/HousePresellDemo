package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_PjRiskLetterReceiver;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：项目风险函
 * Company：ZhiShuSZ
 * */
@Service
public class Tg_PjRiskLetterReceiverRebuild extends RebuilderBase<Tg_PjRiskLetterReceiver>
{
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	@Override
	public Properties getSimpleInfo(Tg_PjRiskLetterReceiver object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
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

		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_PjRiskLetterReceiver object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		Sm_User userUpdate = object.getUserUpdate();
		if (userUpdate != null)
		{					
			properties.put("userUpdate", userUpdate.getTheName());
		}
		properties.put("lastUpdateTimeStamp",myDatetime.dateToSimpleString(object.getLastUpdateTimeStamp()) );	
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_PjRiskLetterReceiver> tg_PjRiskLetterList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_PjRiskLetterList != null)
		{
			for(Tg_PjRiskLetterReceiver object:tg_PjRiskLetterList)
			{
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());
				
				properties.put("theNameOfDepartment", object.getTheNameOfDepartment());
				properties.put("theName", object.getTheName());
				properties.put("positionName", object.getPositionName());
				
				
				list.add(properties);
			}
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public List getListForAdmin(List<Tg_PjRiskLetterReceiver> tg_PjRiskLetterList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_PjRiskLetterList != null)
		{
			for(Tg_PjRiskLetterReceiver object:tg_PjRiskLetterList)
			{
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());
				
				properties.put("theName", object.getTheName());
				properties.put("email", object.getEmail());
				properties.put("sendStatement", object.getSendStatement());
				properties.put("sendTimeStamp", object.getSendTimeStamp());
				
				
				list.add(properties);
			}
		}
		return list;
	}
}
