package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import zhishusz.housepresell.util.MyDatetime;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tg_ExpForecast;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：支出预测
 * Company：ZhiShuSZ
 * */
@Service
public class Tg_ExpForecastRebuild extends RebuilderBase<Tg_ExpForecast>
{
	private MyDatetime myDatetime = MyDatetime.getInstance();

	private MyDouble myDouble = MyDouble.getInstance();

	@Override
	public Properties getSimpleInfo(Tg_ExpForecast object)
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
		properties.put("payTrendForecast", myDouble.getShort(object.getPayTrendForecast(), 2));
		properties.put("applyAmount", myDouble.getShort(object.getApplyAmount(),2));
		properties.put("payableFund", myDouble.getShort(object.getPayableFund(), 2));
		properties.put("nodeChangePayForecast", myDouble.getShort(object.getNodeChangePayForecast(), 2));
		properties.put("handlingFixedDeposit", myDouble.getShort(object.getHandlingFixedDeposit(), 2));
		properties.put("payForecast1", myDouble.getShort(object.getPayForecast1(), 2));
		properties.put("payForecast2", myDouble.getShort(object.getPayForecast2(), 2));
		properties.put("payForecast3", myDouble.getShort(object.getPayForecast3(), 2));

		return properties;
	}

	@Override
	public Properties getDetail(Tg_ExpForecast object)
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
		properties.put("payTrendForecast", object.getPayTrendForecast());
		properties.put("applyAmount", object.getApplyAmount());
		properties.put("payableFund", object.getPayableFund());
		properties.put("nodeChangePayForecast", object.getNodeChangePayForecast());
		properties.put("handlingFixedDeposit", object.getHandlingFixedDeposit());
		properties.put("payForecast1", object.getPayForecast1());
		properties.put("payForecast2", object.getPayForecast2());
		properties.put("payForecast3", object.getPayForecast3());
		properties.put("payTotal", object.getPayTotal());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_ExpForecast> tg_ExpForecastList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_ExpForecastList != null)
		{
			for(Tg_ExpForecast object:tg_ExpForecastList)
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
				properties.put("payTrendForecast", object.getPayTrendForecast());
				properties.put("applyAmount", object.getApplyAmount());
				properties.put("payableFund", object.getPayableFund());
				properties.put("nodeChangePayForecast", object.getNodeChangePayForecast());
				properties.put("handlingFixedDeposit", object.getHandlingFixedDeposit());
				properties.put("payForecast1", object.getPayForecast1());
				properties.put("payForecast2", object.getPayForecast2());
				properties.put("payForecast3", object.getPayForecast3());
				properties.put("payTotal", object.getPayTotal());
				
				list.add(properties);
			}
		}
		return list;
	}
}
