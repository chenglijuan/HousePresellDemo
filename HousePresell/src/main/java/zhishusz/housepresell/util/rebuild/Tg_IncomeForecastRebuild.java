package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import zhishusz.housepresell.util.MyDatetime;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tg_IncomeForecast;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：收入预测
 * Company：ZhiShuSZ
 * */
@Service
public class Tg_IncomeForecastRebuild extends RebuilderBase<Tg_IncomeForecast>
{
	private MyDatetime myDatetime = MyDatetime.getInstance();

	private MyDouble myDouble = MyDouble.getInstance();

	@Override
	public Properties getSimpleInfo(Tg_IncomeForecast object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		if (object.getTheDay() != null && object.getTheDay() > 0) {
			properties.put("theDayStr", myDatetime.dateToSimpleString(object.getTheDay()));
		}
		if (object.getTheWeek() != null) {
			switch (object.getTheWeek()) {
				case 1 :
					properties.put("theWeekStr", "星期一");
					break;
				case 2 :
					properties.put("theWeekStr", "星期二");
					break;
				case 3 :
					properties.put("theWeekStr", "星期三");
					break;
				case 4 :
					properties.put("theWeekStr", "星期四");
					break;
				case 5 :
					properties.put("theWeekStr", "星期五");
					break;
				case 6 :
					properties.put("theWeekStr", "星期六");
					break;
				case 0 :
					properties.put("theWeekStr", "星期日");
					break;
				default :
					properties.put("theWeekStr", "星期日");
					break;
			}
		}
		properties.put("incomeTrendForecast", myDouble.getShort(object.getIncomeTrendForecast(), 2));
		properties.put("fixedExpire", myDouble.getShort(object.getFixedExpire(), 2));
		properties.put("bankLending", myDouble.getShort(object.getBankLending(), 2));
		properties.put("incomeForecast1", myDouble.getShort(object.getIncomeForecast1(), 2));
		properties.put("incomeForecast2", myDouble.getShort(object.getIncomeForecast2(), 2));
		properties.put("incomeForecast3", myDouble.getShort(object.getIncomeForecast3(), 2));
		properties.put("incomeTotal", object.getIncomeTotal());

		return properties;
	}

	@Override
	public Properties getDetail(Tg_IncomeForecast object)
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
		properties.put("theDay", object.getTheDay());
		properties.put("theWeek", object.getTheWeek());
		properties.put("incomeTrendForecast", object.getIncomeTrendForecast());
		properties.put("fixedExpire", object.getFixedExpire());
		properties.put("bankLending", object.getBankLending());
		properties.put("incomeForecast1", object.getIncomeForecast1());
		properties.put("incomeForecast2", object.getIncomeForecast2());
		properties.put("incomeForecast3", object.getIncomeForecast3());
		properties.put("incomeTotal", object.getIncomeTotal());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_IncomeForecast> tg_IncomeForecastList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_IncomeForecastList != null)
		{
			for(Tg_IncomeForecast object:tg_IncomeForecastList)
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
				properties.put("theDay", object.getTheDay());
				properties.put("theWeek", object.getTheWeek());
				properties.put("incomeTrendForecast", object.getIncomeTrendForecast());
				properties.put("fixedExpire", object.getFixedExpire());
				properties.put("bankLending", object.getBankLending());
				properties.put("incomeForecast1", object.getIncomeForecast1());
				properties.put("incomeForecast2", object.getIncomeForecast2());
				properties.put("incomeForecast3", object.getIncomeForecast3());
				properties.put("incomeTotal", object.getIncomeTotal());
				
				list.add(properties);
			}
		}
		return list;
	}
}
