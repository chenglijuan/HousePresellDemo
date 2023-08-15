package zhishusz.housepresell.util.rebuild;

import cn.hutool.core.date.DateUtil;
import zhishusz.housepresell.database.po.Tg_HandleTimeLimitConfig;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：办理时限配置表
 * Company：ZhiShuSZ
 * */
@Service
public class Tg_HandleTimeLimitConfigRebuild extends RebuilderBase<Tg_HandleTimeLimitConfig>
{
	@Override
	public Properties getSimpleInfo(Tg_HandleTimeLimitConfig object)
	{
		return getDetail(object);
	}

	@Override
	public Properties getDetail(Tg_HandleTimeLimitConfig object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("theType", object.getTheType());
		properties.put("completionStandard", object.getCompletionStandard());
		properties.put("limitDayNumber", object.getLimitDayNumber());
		if(object.getRole() != null)
		{
			properties.put("roleName", object.getRole().getTheName());
			properties.put("roleId", object.getRole().getTableId());
		}
		properties.put("lastCfgUser", object.getLastCfgUser());
		if (object.getLastCfgTimeStamp() != null) {
			properties.put("lastCfgTimeStamp", object.getLastCfgTimeStamp());
			properties.put("lastCfgTime", MyDatetime.getInstance().dateToString2(object.getLastCfgTimeStamp()));
		}
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_HandleTimeLimitConfig> tg_HandleTimeLimitConfigList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_HandleTimeLimitConfigList != null)
		{
			for(Tg_HandleTimeLimitConfig object:tg_HandleTimeLimitConfigList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				properties.put("theType", object.getTheType());
				properties.put("completionStandard", object.getCompletionStandard());
				properties.put("limitDayNumber", object.getLimitDayNumber());
				if(object.getRole() != null)
				{
					properties.put("roleName", object.getRole().getTheName());
					properties.put("roleId", object.getRole().getTableId());
				}
				properties.put("lastCfgUser", object.getLastCfgUser());
				if (object.getLastCfgTimeStamp() != null) {
					properties.put("lastCfgTimeStamp", object.getLastCfgTimeStamp());
					properties.put("lastCfgTime", DateUtil.formatDate(new Date(object.getLastCfgTimeStamp())));
				}
				
				list.add(properties);
			}
		}
		return list;
	}
}
