package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tg_RiskCheckMonthSum;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：风控例行抽查表
 * Company：ZhiShuSZ
 * */
@Service
public class Tg_RiskCheckMonthSumRebuild extends RebuilderBase<Tg_RiskCheckMonthSum>
{
	@Override
	public Properties getSimpleInfo(Tg_RiskCheckMonthSum object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("checkNumber", object.getCheckNumber());//风控例行抽查单号
		properties.put("spotTimeStamp", MyDatetime.getInstance().dateToString(object.getSpotTimeStamp(), "yyyy-MM"));//抽查所在月
		properties.put("sumCheckCount", object.getSumCheckCount());//业务
		properties.put("qualifiedCount", object.getQualifiedCount());//合格
		properties.put("unqualifiedCount", object.getUnqualifiedCount());//不合格
		properties.put("pushCount", object.getPushCount());//推送整改
		properties.put("feedbackCount", object.getFeedbackCount());//整改反馈
		properties.put("handleCount", object.getHandleCount());//完成整改
		properties.put("createTimeStamp", MyDatetime.getInstance().dateToSimpleString(object.getCreateTimeStamp()));//抽查日期
		properties.put("rectificationState", object.getRectificationState());//整改状态
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_RiskCheckMonthSum object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("spotTimeStamp", MyDatetime.getInstance().dateToString(object.getSpotTimeStamp(), "yyyy-MM"));//抽查所在月
		properties.put("sumCheckCount", object.getSumCheckCount());//业务
		properties.put("qualifiedCount", object.getQualifiedCount());//合格
		properties.put("unqualifiedCount", object.getUnqualifiedCount());//不合格
		properties.put("pushCount", object.getPushCount());//推送整改
		properties.put("feedbackCount", object.getFeedbackCount());//整改反馈
		properties.put("handleCount", object.getHandleCount());//完成整改
		properties.put("createTimeStamp", MyDatetime.getInstance().dateToSimpleString(object.getCreateTimeStamp()));//抽查日期
		properties.put("rectificationState", object.getRectificationState());//整改状态
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_RiskCheckMonthSum> tg_RiskCheckMonthSumList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_RiskCheckMonthSumList != null)
		{
			for(Tg_RiskCheckMonthSum object:tg_RiskCheckMonthSumList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				
				list.add(properties);
			}
		}
		return list;
	}
}
