package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import zhishusz.housepresell.util.MyDatetime;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tg_TotalEscrowAmountForecast;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：托管总资金的预测结存
 * Company：ZhiShuSZ
 * */
@Service
public class Tg_TotalEscrowAmountForecastRebuild extends RebuilderBase<Tg_TotalEscrowAmountForecast>
{
	private MyDatetime myDatetime = MyDatetime.getInstance();

	@Override
	public Properties getSimpleInfo(Tg_TotalEscrowAmountForecast object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		if (object.getTheDay() != null && object.getTheDay() > 0) {
			properties.put("theDayStr", myDatetime.dateToSimpleString(object.getTheDay()));
		}
		if (object.getTheWeek() != null && object.getTheWeek() > 0) {
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
		properties.put("lastAmount", object.getLastAmount());
		properties.put("incomeTotal", object.getIncomeTotal());
		properties.put("payTotal", object.getPayTotal());
		properties.put("currentAmount", object.getCurrentAmount());
		properties.put("escrowAmountReferenceValue", object.getEscrowAmountReferenceValue());
		properties.put("escrowAmount", object.getEscrowAmount());
		properties.put("referenceValue1", object.getReferenceValue1());
		properties.put("referenceValue2", object.getReferenceValue2());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_TotalEscrowAmountForecast object)
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
		properties.put("lastAmount", object.getLastAmount());
		properties.put("incomeTotal", object.getIncomeTotal());
		properties.put("payTotal", object.getPayTotal());
		properties.put("currentAmount", object.getCurrentAmount());
		properties.put("escrowAmountReferenceValue", object.getEscrowAmountReferenceValue());
		properties.put("escrowAmount", object.getEscrowAmount());
		properties.put("referenceValue1", object.getReferenceValue1());
		properties.put("referenceValue2", object.getReferenceValue2());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_TotalEscrowAmountForecast> tg_TotalEscrowAmountForecastList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_TotalEscrowAmountForecastList != null)
		{
			for(Tg_TotalEscrowAmountForecast object:tg_TotalEscrowAmountForecastList)
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
				properties.put("lastAmount", object.getLastAmount());
				properties.put("incomeTotal", object.getIncomeTotal());
				properties.put("payTotal", object.getPayTotal());
				properties.put("currentAmount", object.getCurrentAmount());
				properties.put("escrowAmountReferenceValue", object.getEscrowAmountReferenceValue());
				properties.put("escrowAmount", object.getEscrowAmount());
				properties.put("referenceValue1", object.getReferenceValue1());
				properties.put("referenceValue2", object.getReferenceValue2());
				
				list.add(properties);
			}
		}
		return list;
	}
}
