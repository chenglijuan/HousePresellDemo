package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Tg_ProInspectionSchedule_View;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：项目巡查预测表
 * Company：ZhiShuSZ
 * */
@Service
public class Tg_ProInspectionSchedule_ViewRebuild  extends RebuilderBase<Tg_ProInspectionSchedule_View>
{

	@Override
	public Properties getSimpleInfo(Tg_ProInspectionSchedule_View object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_ProInspectionSchedule_View object)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_ProInspectionSchedule_View> tg_ProInspectionSchedule_ViewList)
	{
		List<Properties> list = new ArrayList<Properties>();
		
		for (Tg_ProInspectionSchedule_View object : tg_ProInspectionSchedule_ViewList)
		{
			Properties properties = new MyProperties();
			
			properties.put("cityRegion", object.getCityRegion());
			properties.put("companyName", object.getCompanyName());
			properties.put("projectName", object.getProjectName());
			properties.put("upTotalFloorNumber", object.getUpTotalFloorNumber());
			properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
			properties.put("currentLimitedNote", object.getCurrentLimitedNote());
			properties.put("currentBuildProgress", object.getCurrentBuildProgress());
			properties.put("progressOfUpdateTime", object.getProgressOfUpdateTime());
			properties.put("nextChangeNode", object.getNextChangeNode());
			properties.put("forecastNextChangeTime", object.getForecastNextChangeTime());
			properties.put("preSalePermits", object.getPreSalePermits());
			
			list.add(properties);
		}
		return list;
	}
}
