package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.database.po.Sm_BusiState_Log;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：日志-业务状态
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_BusiState_LogRebuild extends RebuilderBase<Sm_BusiState_Log>
{
	@Override
	public Properties getSimpleInfo(Sm_BusiState_Log object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Sm_BusiState_Log object)
	{
		MyDatetime myDatetime = MyDatetime.getInstance();
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
//		properties.put("userOperate", object.getUserOperate());
		Sm_User userOperate = object.getUserOperate();
		if (userOperate != null) {
			properties.put("userOperateId",userOperate.getTableId());
			properties.put("userOperateName",userOperate.getTheName());
//			properties.put("userOperateName",userOperate.getTheName());
		}
		properties.put("remoteAddress", object.getRemoteAddress());
		properties.put("operateTimeStamp", object.getOperateTimeStamp());
		properties.put("updateTimeString", myDatetime.dateToString2(object.getOperateTimeStamp()));
		properties.put("sourceId", object.getSourceId());
		properties.put("sourceType", object.getSourceType());
		properties.put("orgObjJsonFilePath", object.getOrgObjJsonFilePath());
		properties.put("newObjJsonFilePath", object.getNewObjJsonFilePath());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_BusiState_Log> sm_BusiState_LogList)
	{
		List<Properties> list = new ArrayList<Properties>();
		MyDatetime myDatetime = MyDatetime.getInstance();
		if(sm_BusiState_LogList != null)
		{
			for(Sm_BusiState_Log object:sm_BusiState_LogList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("tableId", object.getTableId());

				Sm_User userOperate = object.getUserOperate();
				if (userOperate != null) {
					properties.put("updateUserName", userOperate.getTheName());
				}

//				properties.put("userOperate", object.getUserOperate());
//				properties.put("userOperateId", object.getUserOperate().getTableId());
				properties.put("remoteAddress", object.getRemoteAddress());
				properties.put("operateTimeStamp", object.getOperateTimeStamp());
				properties.put("updateTimeString", myDatetime.dateToString2(object.getOperateTimeStamp()));
//				properties.put("sourceId", object.getSourceId());
//				properties.put("sourceType", object.getSourceType());
//				properties.put("orgObjJsonFilePath", object.getOrgObjJsonFilePath());
//				properties.put("newObjJsonFilePath", object.getNewObjJsonFilePath());
				
				list.add(properties);
			}
		}
		return list;
	}
}
