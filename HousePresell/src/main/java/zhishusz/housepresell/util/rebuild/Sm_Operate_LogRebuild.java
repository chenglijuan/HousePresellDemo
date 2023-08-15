package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Sm_Operate_Log;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：日志-关键操作
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_Operate_LogRebuild extends RebuilderBase<Sm_Operate_Log>
{
	@Override
	public Properties getSimpleInfo(Sm_Operate_Log object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Sm_Operate_Log object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("userOperate", object.getUserOperate());
		properties.put("userOperateId", object.getUserOperate().getTableId());
		properties.put("remoteAddress", object.getRemoteAddress());
		properties.put("operate", object.getOperate());
		properties.put("inputForm", object.getInputForm());
		properties.put("result", object.getResult());
		properties.put("info", object.getInfo());
		properties.put("returnJson", object.getReturnJson());
		properties.put("startTimeStamp", object.getStartTimeStamp());
		properties.put("endTimeStamp", object.getEndTimeStamp());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_Operate_Log> sm_Operate_LogList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_Operate_LogList != null)
		{
			for(Sm_Operate_Log object:sm_Operate_LogList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("userOperate", object.getUserOperate());
				properties.put("userOperateId", object.getUserOperate().getTableId());
				properties.put("remoteAddress", object.getRemoteAddress());
				properties.put("operate", object.getOperate());
				properties.put("inputForm", object.getInputForm());
				properties.put("result", object.getResult());
				properties.put("info", object.getInfo());
				properties.put("returnJson", object.getReturnJson());
				properties.put("startTimeStamp", object.getStartTimeStamp());
				properties.put("endTimeStamp", object.getEndTimeStamp());
				
				list.add(properties);
			}
		}
		return list;
	}
}
