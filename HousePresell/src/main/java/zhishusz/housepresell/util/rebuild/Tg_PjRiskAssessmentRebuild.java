package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tg_PjRiskAssessment;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：项目风险评估
 * Company：ZhiShuSZ
 * */
@Service
public class Tg_PjRiskAssessmentRebuild extends RebuilderBase<Tg_PjRiskAssessment>
{
	@Override
	public Properties getSimpleInfo(Tg_PjRiskAssessment object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("eCode", object.geteCode());
		properties.put("theNameOfCityRegion", object.getTheNameOfCityRegion());
		properties.put("theCompanyName", object.getDevelopCompany().getTheName());
		properties.put("theNameOfProject", object.getTheNameOfProject());
		properties.put("assessDate", object.getAssessDate());
		properties.put("riskAssessment", object.getRiskAssessment());
		properties.put("createUserName", object.getUserStart().getTheName());
		properties.put("createTimeStamp", MyDatetime.getInstance().dateToSimpleString(object.getCreateTimeStamp()));
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_PjRiskAssessment object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("eCode", object.geteCode());
		
		properties.put("userStart", object.getUserStart());
		
		properties.put("userName", object.getUserStart().getTheName());
		properties.put("createTime", MyDatetime.getInstance().dateToSimpleString(object.getCreateTimeStamp()));
		
		properties.put("cityRegionId", object.getCityRegion().getTableId());
		properties.put("theNameOfCityRegion", object.getTheNameOfCityRegion());
		properties.put("theCompanyName", object.getDevelopCompany().getTheName());
		properties.put("developCompanyId", object.getDevelopCompany().getTableId());
		properties.put("projectId", object.getProject().getTableId());
		properties.put("theNameOfProject", object.getTheNameOfProject());
		properties.put("assessDate", object.getAssessDate());
		properties.put("riskAssessment", object.getRiskAssessment());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_PjRiskAssessment> tg_PjRiskAssessmentList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_PjRiskAssessmentList != null)
		{
			for(Tg_PjRiskAssessment object:tg_PjRiskAssessmentList)
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
				properties.put("cityRegion", object.getCityRegion());
				properties.put("cityRegionId", object.getCityRegion().getTableId());
				properties.put("theNameOfCityRegion", object.getTheNameOfCityRegion());
				properties.put("developCompany", object.getDevelopCompany());
				properties.put("developCompanyId", object.getDevelopCompany().getTableId());
				properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
				properties.put("project", object.getProject());
				properties.put("projectId", object.getProject().getTableId());
				properties.put("theNameOfProject", object.getTheNameOfProject());
				properties.put("assessDate", object.getAssessDate());
				properties.put("riskAssessment", object.getRiskAssessment());
				
				list.add(properties);
			}
		}
		return list;
	}
}
