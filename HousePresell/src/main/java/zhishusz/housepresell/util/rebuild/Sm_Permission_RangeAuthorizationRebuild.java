package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_Permission_Range;
import zhishusz.housepresell.database.po.Sm_Permission_RangeAuthorization;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_RangeAuthType;
import zhishusz.housepresell.service.Sm_BaseParameterGetService;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：角色授权列表操作
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_Permission_RangeAuthorizationRebuild extends RebuilderBase<Sm_Permission_RangeAuthorization>
{
	@Autowired
	private Sm_BaseParameterGetService sm_BaseParameterGetService;
	private MyDatetime myDatetime = MyDatetime.getInstance(); 
	
	@Override
	public Properties getSimpleInfo(Sm_Permission_RangeAuthorization object)
	{
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Sm_Permission_RangeAuthorization object)
	{
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		Sm_User userInfo = object.getUserInfo();
		if(userInfo != null)
		{
			properties.put("userInfoId", userInfo.getTableId());
			properties.put("userInfoName", userInfo.getTheName());
		}
		
		Sm_User userStart = object.getUserStart();
		if(userStart != null)
		{
			properties.put("userStartId", userStart.getTableId());
			properties.put("userStartName", userStart.getTheName());
		}
		properties.put("createDateTime", myDatetime.dateToString(object.getCreateTimeStamp(), "yyyy-MM-dd HH:mm:ss"));
		
		Emmp_CompanyInfo emmp_CompanyInfo = object.getEmmp_CompanyInfo();
		if(emmp_CompanyInfo != null)
		{
			properties.put("companyInfoId", emmp_CompanyInfo.getTableId());
			properties.put("companyInfoName", emmp_CompanyInfo.getTheName());
			properties.put("companyInfoType", emmp_CompanyInfo.getTheType());
			Sm_BaseParameter sm_BaseParameter = sm_BaseParameterGetService.getParameter("8", emmp_CompanyInfo.getTheType());
			if(sm_BaseParameter != null)
			{
				properties.put("sm_BaseParameter", sm_BaseParameter);
				properties.put("parameterName", sm_BaseParameter.getTheName());
			}
		}
		
		properties.put("authTimePeriod", myDatetime.dateToString(object.getAuthStartTimeStamp(),"yyyy-MM-dd") +" - "+ myDatetime.dateToString(object.getAuthEndTimeStamp(),"yyyy-MM-dd"));
		properties.put("rangeAuthType", object.getRangeAuthType());
		
		
		//根据 授权类别 找出相关IdList
		List<Long> idSelectList = new ArrayList<Long>(); 
		Integer rangeAuthType = object.getRangeAuthType();
		if(S_RangeAuthType.Area.equals(rangeAuthType))
		{
			List<Sm_Permission_Range> rangeInfoList = object.getRangeInfoList();
			if(rangeInfoList != null)
			{
				for(Sm_Permission_Range rangeInfo : rangeInfoList)
				{
					Sm_CityRegionInfo cityRegionInfo = rangeInfo.getCityRegionInfo();
					if(cityRegionInfo != null)
					{
						idSelectList.add(cityRegionInfo.getTableId());
					}
				}
			}
		}
		else if(S_RangeAuthType.Project.equals(rangeAuthType))
		{
			List<Sm_Permission_Range> rangeInfoList = object.getRangeInfoList();
			if(rangeInfoList != null)
			{
				for(Sm_Permission_Range rangeInfo : rangeInfoList)
				{
					Empj_ProjectInfo projectInfo = rangeInfo.getProjectInfo();
					if(projectInfo != null)
					{
						idSelectList.add(projectInfo.getTableId());
					}
				}
			}
		}
		else if(S_RangeAuthType.Building.equals(rangeAuthType))
		{
			List<Sm_Permission_Range> rangeInfoList = object.getRangeInfoList();
			if(rangeInfoList != null)
			{
				for(Sm_Permission_Range rangeInfo : rangeInfoList)
				{
					Empj_BuildingInfo buildingInfo = rangeInfo.getBuildingInfo();
					if(buildingInfo != null)
					{
						idSelectList.add(buildingInfo.getTableId());
					}
				}
			}
		}
		properties.put("idSelectList", idSelectList);
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_Permission_RangeAuthorization> objectList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(objectList != null)
		{
			for(Sm_Permission_RangeAuthorization object : objectList)
			{
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				properties.put("forCompanyType", object.getForCompanyType());
				properties.put("emmp_CompanyInfo", object.getEmmp_CompanyInfo());
				properties.put("authStartTimeStamp", myDatetime.dateToString(object.getAuthStartTimeStamp(),"yyyy-MM-dd"));
				properties.put("authEndTimeStamp", myDatetime.dateToString(object.getAuthEndTimeStamp(),"yyyy-MM-dd"));
				properties.put("authTimePeriod", myDatetime.dateToString(object.getAuthStartTimeStamp(),"yyyy-MM-dd") +" - "+ myDatetime.dateToString(object.getAuthEndTimeStamp(),"yyyy-MM-dd"));
				
				//机构类型
				if(object.getEmmp_CompanyInfo() != null)
				{
					Sm_BaseParameter sm_BaseParameter = sm_BaseParameterGetService.getParameter("8", object.getEmmp_CompanyInfo().getTheType());
					if(sm_BaseParameter != null)
					{
						properties.put("sm_BaseParameter", sm_BaseParameter);
						properties.put("parameterName", sm_BaseParameter.getTheName());
					}
				}
				
				Integer rangeAuthType = object.getRangeAuthType();
				properties.put("rangeAuthType", rangeAuthType);
				
				Sm_User userInfo = object.getUserInfo();
				if(userInfo != null)
				{
					properties.put("userInfoId", userInfo.getTableId());
					properties.put("userInfoName", userInfo.getTheName());
				}
				
				//区域/项目名称/楼幢
				List<Properties> rangeInfoList_Pro = new ArrayList<Properties>();
				List<Sm_Permission_Range> rangeInfoList = object.getRangeInfoList();
				if(rangeInfoList != null)
				{
					for(Sm_Permission_Range rangeInfo : rangeInfoList)
					{
						Properties rangeInfo_Pro = new MyProperties();
						
						if(S_RangeAuthType.Area.equals(rangeAuthType))
						{
							Sm_CityRegionInfo cityRegionInfo = rangeInfo.getCityRegionInfo();
							rangeInfo_Pro.put("theName", cityRegionInfo.getTheName());
						}
						else if(S_RangeAuthType.Project.equals(rangeAuthType))
						{
							Empj_ProjectInfo projectInfo = rangeInfo.getProjectInfo();
							rangeInfo_Pro.put("theName", projectInfo.getTheName());
						}
						else if(S_RangeAuthType.Building.equals(rangeAuthType))
						{
							Empj_BuildingInfo buildingInfo = rangeInfo.getBuildingInfo();
							rangeInfo_Pro.put("theName", buildingInfo.geteCodeFromConstruction());
						}
						rangeInfoList_Pro.add(rangeInfo_Pro);
					}
				}
				properties.put("rangeInfoList", rangeInfoList_Pro);
				
				list.add(properties);
			}
		}
		return list;
	}
	
	public String getRangeInfoListStr(Sm_Permission_RangeAuthorization object)
	{
		String rangeInfoListStr = "";
		if(object == null || object.getRangeAuthType() == null)
		{
			return rangeInfoListStr;
		}
		
		//区域/项目名称/楼幢
		Integer rangeAuthType = object.getRangeAuthType();
		List<String> rangeInfoList_Str = new ArrayList<String>();
		List<Sm_Permission_Range> rangeInfoList = object.getRangeInfoList();
		if(rangeInfoList != null)
		{
			for(Sm_Permission_Range rangeInfo : rangeInfoList)
			{
				if(S_RangeAuthType.Area.equals(rangeAuthType))
				{
					Sm_CityRegionInfo cityRegionInfo = rangeInfo.getCityRegionInfo();
					rangeInfoList_Str.add(cityRegionInfo.getTheName());
				}
				else if(S_RangeAuthType.Project.equals(rangeAuthType))
				{
					Empj_ProjectInfo projectInfo = rangeInfo.getProjectInfo();
					rangeInfoList_Str.add(projectInfo.getTheName());
				}
				else if(S_RangeAuthType.Building.equals(rangeAuthType))
				{
					Empj_BuildingInfo buildingInfo = rangeInfo.getBuildingInfo();
					rangeInfoList_Str.add(buildingInfo.geteCodeFromConstruction());
				}
			}
		}
		
		return StringUtils.join(rangeInfoList_Str, ",");
	}
}
