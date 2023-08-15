package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tg_RiskLogInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：风险日志管理
 * Company：ZhiShuSZ
 */
@Service
public class Tg_RiskLogInfoRebuild extends RebuilderBase<Tg_RiskLogInfo>
{
	@Override
	public Properties getSimpleInfo(Tg_RiskLogInfo object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());

		properties.put("eCode", object.geteCode());
		properties.put("theNameOfProject", object.getTheNameOfProject());

		if (null != object.getDevelopCompany() && null != object.getDevelopCompany().getTheName())
		{
			properties.put("theNameOfDevelopCompany", object.getDevelopCompany().getTheName());
		}
		else
		{
			properties.put("theNameOfDevelopCompany", "");
		}
		
		properties.put("theNameOfCityRegion", object.getTheNameOfCityRegion());
		
		properties.put("eCodeOfPjRiskRating", object.geteCodeOfPjRiskRating());
		properties.put("riskRating", object.getRiskRating());
		properties.put("logDate", object.getLogDate());
		
		properties.put("userStart", object.getUserStart());
		properties.put("userUpdate", object.getUserUpdate());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("userUpdateId", object.getUserUpdate().getTableId());

		properties.put("createTimeStamp", MyDatetime.getInstance().dateToString2(object.getCreateTimeStamp()));
		properties.put("lastUpdateTimeStamp",
				MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));

		return properties;
	}

	@Override
	public Properties getDetail(Tg_RiskLogInfo object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());

		properties.put("eCode", object.geteCode());
		properties.put("theNameOfProject", object.getTheNameOfProject());
		properties.put("projectId", object.getProject().getTableId());

		if (null != object.getDevelopCompany() && null != object.getDevelopCompany().getTheName())
		{
			properties.put("theNameOfDevelopCompany", object.getDevelopCompany().getTheName());
		}
		else
		{
			properties.put("theNameOfDevelopCompany", "");
		}
		
		properties.put("theNameOfCityRegion", object.getTheNameOfCityRegion());
		
		properties.put("eCodeOfPjRiskRating", object.geteCodeOfPjRiskRating());
		properties.put("riskRating", object.getRiskRating());
		properties.put("logDate", object.getLogDate());
		properties.put("riskLog", object.getRiskLog());
		
		properties.put("userStart", object.getUserStart());
		properties.put("userUpdate", object.getUserUpdate());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("userUpdateId", object.getUserUpdate().getTableId());

		properties.put("createTimeStamp", MyDatetime.getInstance().dateToString2(object.getCreateTimeStamp()));
		properties.put("lastUpdateTimeStamp",
				MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_RiskLogInfo> tg_RiskLogInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (tg_RiskLogInfoList != null)
		{
			for (Tg_RiskLogInfo object : tg_RiskLogInfoList)
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
				properties.put("developCompany", object.getDevelopCompany());
				properties.put("developCompanyId", object.getDevelopCompany().getTableId());
				properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
				properties.put("project", object.getProject());
				properties.put("projectId", object.getProject().getTableId());
				properties.put("theNameOfProject", object.getTheNameOfProject());
				properties.put("cityRegion", object.getCityRegion());
				properties.put("cityRegionId", object.getCityRegion().getTableId());
				properties.put("theNameOfCityRegion", object.getTheNameOfCityRegion());
				properties.put("riskRating", object.getRiskRating());
				properties.put("riskLog", object.getRiskLog());

				list.add(properties);
			}
		}
		return list;
	}
}
