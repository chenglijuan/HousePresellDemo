package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_PjRiskLetter;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：项目风险函
 * Company：ZhiShuSZ
 * */
@Service
public class Tg_PjRiskLetterRebuild extends RebuilderBase<Tg_PjRiskLetter>
{
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	@Override
	public Properties getSimpleInfo(Tg_PjRiskLetter object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
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
		properties.put("releaseDate", object.getReleaseDate());
		properties.put("deliveryCompany", object.getDeliveryCompany());
		properties.put("riskNotification", object.getRiskNotification());
		properties.put("basicSituation", object.getBasicSituation());
		properties.put("riskAssessment", object.getRiskAssessment());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_PjRiskLetter object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
//		properties.put("theState", object.getTheState());
//		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
//		properties.put("userStart", object.getUserStart());
//		properties.put("userStartId", object.getUserStart().getTableId());
//		properties.put("createTimeStamp", object.getCreateTimeStamp());
//		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
//		properties.put("userRecord", object.getUserRecord());
//		properties.put("userRecordId", object.getUserRecord().getTableId());
//		properties.put("recordTimeStamp", object.getRecordTimeStamp());
//		properties.put("cityRegion", object.getCityRegion());
//		properties.put("cityRegionId", object.getCityRegion().getTableId());
//		properties.put("theNameOfCityRegion", object.getTheNameOfCityRegion());
//		properties.put("developCompany", object.getDevelopCompany());
//		properties.put("developCompanyId", object.getDevelopCompany().getTableId());
//		properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
//		properties.put("project", object.getProject());
//		properties.put("projectId", object.getProject().getTableId());
//		properties.put("theNameOfProject", object.getTheNameOfProject());
		
		Sm_User userUpdate = object.getUserUpdate();
		if (userUpdate != null)
		{					
			properties.put("userUpdate", userUpdate.getTheName());
		}
		properties.put("lastUpdateTimeStamp",myDatetime.dateToString2(object.getLastUpdateTimeStamp()) );	
		
		Emmp_CompanyInfo company = object.getDevelopCompany();
		if(object.getDevelopCompany()!=null){
			properties.put("companyName", company.getTheName());
		}
		
		Empj_ProjectInfo project = object.getProject();
		if(object.getProject()!=null){
			properties.put("projectName", project.getTheName());
		}
		
		Sm_CityRegionInfo sm_CityRegionInfo = object.getCityRegion();
		if(object.getProject()!=null){
			properties.put("cityRegionName", sm_CityRegionInfo.getTheName());
		}
		
		properties.put("releaseDate", object.getReleaseDate());
		properties.put("deliveryCompany", object.getDeliveryCompany());
		properties.put("riskNotification", object.getRiskNotification());
		properties.put("basicSituation", object.getBasicSituation());
		properties.put("riskAssessment", object.getRiskAssessment());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_PjRiskLetter> tg_PjRiskLetterList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_PjRiskLetterList != null)
		{
			for(Tg_PjRiskLetter object:tg_PjRiskLetterList)
			{
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());
				properties.put("eCode", object.geteCode());

				Sm_User userUpdate = object.getUserUpdate();
				if (userUpdate != null)
				{					
					properties.put("userUpdate", userUpdate.getTheName());
				}
				properties.put("lastUpdateTimeStamp",myDatetime.dateToString2(object.getLastUpdateTimeStamp()) );

				
				
				Emmp_CompanyInfo company = object.getDevelopCompany();
				if(object.getDevelopCompany()!=null){
					properties.put("companyName", company.getTheName());
				}
				
				Empj_ProjectInfo project = object.getProject();
				if(object.getProject()!=null){
					properties.put("projectName", project.getTheName());
				}
				
				Sm_CityRegionInfo sm_CityRegionInfo = object.getCityRegion();
				if(object.getProject()!=null){
					properties.put("cityRegionName", sm_CityRegionInfo.getTheName());
				}
				
				properties.put("releaseDate", object.getReleaseDate());
				properties.put("deliveryCompany", object.getDeliveryCompany());
				
				list.add(properties);
			}
		}
		return list;
	}
}
