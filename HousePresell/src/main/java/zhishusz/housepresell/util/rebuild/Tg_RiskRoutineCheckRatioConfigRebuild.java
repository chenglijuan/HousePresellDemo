package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckRatioConfig;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：风控例行抽查比例配置表
 * Company：ZhiShuSZ
 * */
@Service
public class Tg_RiskRoutineCheckRatioConfigRebuild extends RebuilderBase<Tg_RiskRoutineCheckRatioConfig>
{
	@Override
	public Properties getSimpleInfo(Tg_RiskRoutineCheckRatioConfig object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("largeBusinessName", object.getLargeBusinessName());
		properties.put("subBusinessName", object.getSubBusinessName());
		if(object.getTheRatio() != null)
		{
			properties.put("theRatio", object.getTheRatio());
		}
		else
		{
			properties.put("theRatio", "");
		}
		
		if(object.getRole() != null)
		{
			properties.put("roleName", object.getRole().getTheName());
			properties.put("roleId", object.getRole().getTableId());
		}
		else
		{
			properties.put("roleId", "");
		}
		
		if(object.getUserUpdate() != null)
		{
			properties.put("userUpdateName", object.getUserUpdate().getTheName());
		}
		
		properties.put("lastUpdateTimeStamp", MyDatetime.getInstance().dateToString(object.getLastUpdateTimeStamp()));
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_RiskRoutineCheckRatioConfig object)
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
		properties.put("theRatio", object.getTheRatio());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_RiskRoutineCheckRatioConfig> tg_RiskRoutineCheckRatioConfigList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_RiskRoutineCheckRatioConfigList != null)
		{
			for(Tg_RiskRoutineCheckRatioConfig object:tg_RiskRoutineCheckRatioConfigList)
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
				properties.put("theRatio", object.getTheRatio());
				
				list.add(properties);
			}
		}
		return list;
	}
}
