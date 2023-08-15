package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_PjRiskRating;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：项目风险评级
 * Company：ZhiShuSZ
 * */
@Service
public class Tg_PjRiskRatingRebuild extends RebuilderBase<Tg_PjRiskRating>
{
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	@Override
	public Properties getSimpleInfo(Tg_PjRiskRating object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_PjRiskRating object)
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
//		properties.put("userRecord", object.getUserRecord());
//		properties.put("userRecordId", object.getUserRecord().getTableId());
//		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		
		Sm_User userUpdate = object.getUserUpdate();
		if (userUpdate != null)
		{					
			properties.put("userUpdate", userUpdate.getTheName());
		}
		properties.put("lastUpdateTimeStamp",myDatetime.dateToSimpleString(object.getLastUpdateTimeStamp()) );


		Empj_ProjectInfo project = object.getProject();
		if(object.getProject()!=null){
			properties.put("theName", project.getTheName());
		}
		Sm_CityRegionInfo sm_CityRegionInfo = object.getCityRegion();
		if(object.getCityRegion()!=null){
			properties.put("cityRegionName", sm_CityRegionInfo.getTheName());
		}
		Emmp_CompanyInfo emmp_CompanyInfo = object.getDevelopCompany();
		if(object.getDevelopCompany()!=null){
			properties.put("developCompanyName", emmp_CompanyInfo.getTheName());
		}
		
//		properties.put("cityRegion", object.getCityRegion());
//		properties.put("cityRegionId", object.getCityRegion().getTableId());
//		properties.put("theNameOfCityRegion", object.getTheNameOfCityRegion());
//		properties.put("developCompany", object.getDevelopCompany());
//		properties.put("developCompanyId", object.getDevelopCompany().getTableId());
//		properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
//		properties.put("theName", object.getProject());
//		properties.put("projectId", object.getProject().getTableId());
//		properties.put("theNameOfProject", object.getTheNameOfProject());
		
		properties.put("operateDate", object.getOperateDate());
		properties.put("theLevel", object.getTheLevel());
		properties.put("riskNotification", object.getRiskNotification());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_PjRiskRating> tg_PjRiskRatingList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_PjRiskRatingList != null)
		{
			for(Tg_PjRiskRating object:tg_PjRiskRatingList)
			{
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());
//				properties.put("theState", object.getTheState());
//				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
//				properties.put("userStart", object.getUserStart());
//				properties.put("userStartId", object.getUserStart().getTableId());
//				properties.put("createTimeStamp", object.getCreateTimeStamp());
//				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
//				properties.put("userRecord", object.getUserRecord());
//				properties.put("userRecordId", object.getUserRecord().getTableId());
//				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				
				Sm_User userUpdate = object.getUserUpdate();
				if (userUpdate != null)
				{					
					properties.put("userUpdate", userUpdate.getTheName());
				}
				properties.put("lastUpdateTimeStamp",myDatetime.dateToSimpleString(object.getLastUpdateTimeStamp()) );
				
				Empj_ProjectInfo project = object.getProject();
				if(object.getProject()!=null){
					properties.put("theName", project.getTheName());
				}
				Sm_CityRegionInfo sm_CityRegionInfo = object.getCityRegion();
				if(object.getCityRegion()!=null){
					properties.put("cityRegionName", sm_CityRegionInfo.getTheName());
				}
				Emmp_CompanyInfo emmp_CompanyInfo = object.getDevelopCompany();
				if(object.getDevelopCompany()!=null){
					properties.put("developCompanyName", emmp_CompanyInfo.getTheName());
				}
				
//				properties.put("cityRegion", object.getCityRegion());
//				properties.put("cityRegionId", object.getCityRegion().getTableId());
//				properties.put("theNameOfCityRegion", object.getTheNameOfCityRegion());
//				properties.put("developCompany", object.getDevelopCompany());
//				properties.put("developCompanyId", object.getDevelopCompany().getTableId());
//				properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
//				properties.put("project", object.getProject());
//				properties.put("projectId", object.getProject().getTableId());
//				properties.put("theNameOfProject", object.getTheNameOfProject());
				
				properties.put("operateDate", object.getOperateDate());
				properties.put("theLevel", object.getTheLevel());
//				properties.put("riskNotification", object.getRiskNotification());
				
				list.add(properties);
			}
		}
		return list;
	}

	/**
	 * xsz by time 2018-10-16 10:09:54
	 * 用于项目风险日志选择项目时加载评级
	 * @param object
	 * @return
	 */
	public Properties getPreForRiskInfo(Tg_PjRiskRating object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("theLevel", object.getTheLevel());
		
		return properties;
	}
}
