package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.database.po.Tg_IncomeExpDepositForecast;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：收入预测
 * Company：ZhiShuSZ
 * */
@Service
public class Tg_IncomeExpDepositForecastRebuild extends RebuilderBase<Tg_IncomeExpDepositForecast>
{
	private MyDatetime myDatetime = MyDatetime.getInstance();

	private MyDouble myDouble = MyDouble.getInstance();

	@Override
	public Properties getSimpleInfo(Tg_IncomeExpDepositForecast object)
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
				default :
					properties.put("theWeekStr", "星期日");
					break;
			}
		}
		properties.put("lastDaySurplus", myDouble.getShort(object.getLastDaySurplus(), 2));
		properties.put("incomeTotal", myDouble.getShort(object.getIncomeTotal(), 2));
		properties.put("expTotal", myDouble.getShort(object.getExpTotal(), 2));
		properties.put("todaySurplus", myDouble.getShort(object.getTodaySurplus(), 2));
		properties.put("collocationReference", myDouble.getShort(object.getCollocationReference(), 2));
		properties.put("collocationBalance", myDouble.getShort(object.getCollocationBalance(), 2));
		properties.put("canDepositReference1", myDouble.getShort(object.getCanDepositReference1(), 2));
		properties.put("canDepositReference2", myDouble.getShort(object.getCanDepositReference2(), 2));


		return properties;
	}

	@Override
	public Properties getDetail(Tg_IncomeExpDepositForecast object)
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
		properties.put("lastDaySurplus", myDouble.getShort(object.getLastDaySurplus(), 2));
		properties.put("incomeTotal", myDouble.getShort(object.getIncomeTotal(), 2));
		properties.put("expTotal", myDouble.getShort(object.getExpTotal(), 2));
		properties.put("todaySurplus", myDouble.getShort(object.getTodaySurplus(), 2));
		properties.put("collocationReference", myDouble.getShort(object.getCollocationReference(), 2));
		properties.put("collocationBalance", myDouble.getShort(object.getCollocationBalance(), 2));
		properties.put("canDepositReference1", myDouble.getShort(object.getCanDepositReference1(), 2));
		properties.put("canDepositReference2", myDouble.getShort(object.getCanDepositReference2(), 2));

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_IncomeExpDepositForecast> tg_IncomeExpDepositForecastList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_IncomeExpDepositForecastList != null)
		{
			for(Tg_IncomeExpDepositForecast object:tg_IncomeExpDepositForecastList)
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
				properties.put("lastDaySurplus", myDouble.getShort(object.getLastDaySurplus(), 2));
				properties.put("incomeTotal", myDouble.getShort(object.getIncomeTotal(), 2));
				properties.put("expTotal", myDouble.getShort(object.getExpTotal(), 2));
				properties.put("todaySurplus", myDouble.getShort(object.getTodaySurplus(), 2));
				properties.put("collocationReference", myDouble.getShort(object.getCollocationReference(), 2));
				properties.put("collocationBalance", myDouble.getShort(object.getCollocationBalance(), 2));
				properties.put("canDepositReference1", myDouble.getShort(object.getCanDepositReference1(), 2));
				properties.put("canDepositReference2", myDouble.getShort(object.getCanDepositReference2(), 2));

				list.add(properties);
			}
		}
		return list;
	}
}
