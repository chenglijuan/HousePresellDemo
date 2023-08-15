package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RangeForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RangeDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_Permission_Range;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UserType;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：获取用户的权限范围
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_UserGetRangeService
{
	@Autowired
	private Sm_Permission_RangeDao sm_Permission_RangeDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Sm_User sm_User)
	{
		Properties properties = new MyProperties();

		Integer userType = sm_User.getTheType();//用户类型
		Emmp_CompanyInfo companyInfo = null;
		
		Long nowTimeStamp = System.currentTimeMillis();
		List<Sm_Permission_Range> sm_Permission_RangeList = new ArrayList<Sm_Permission_Range>();
		Sm_Permission_RangeForm sm_Permission_RangeForm = new Sm_Permission_RangeForm();
		sm_Permission_RangeForm.setTheState(S_TheState.Normal);
		sm_Permission_RangeForm.setLeAuthStartTimeStamp(nowTimeStamp);
		sm_Permission_RangeForm.setGtAuthEndTimeStamp(nowTimeStamp);
		
		if(S_UserType.ZhengtaiUser.equals(userType))
		{
			//在角色授权数据表，根据用户，查询，获取该用户拥有的区域IdArr
			sm_Permission_RangeForm.setUser(sm_User);
			sm_Permission_RangeList = sm_Permission_RangeDao.findByPage(sm_Permission_RangeDao.getQuery(sm_Permission_RangeDao.getBasicHQL(), sm_Permission_RangeForm));
		}
		else if(S_UserType.CommonUser.equals(userType))
		{
			//根据该用户所在的机构，查询出该机构所拥有的区域IdArr,项目IdArr,楼栋IdArr
			companyInfo = sm_User.getCompany();
			sm_Permission_RangeForm.setCompanyInfo(companyInfo);
			sm_Permission_RangeList = sm_Permission_RangeDao.findByPage(sm_Permission_RangeDao.getQuery(sm_Permission_RangeDao.getBasicHQL(), sm_Permission_RangeForm));
		}

		Set<Long> buildingIdSet_Temp = new HashSet<Long>();
		Set<Long> projectIdSet_Temp = new HashSet<Long>();
		Set<Long> cityReagionIdSet_Temp = new HashSet<Long>();
		Set<Long> developCompanyInfoIdSet_Temp = new HashSet<Long>();
		
		List<Long> logInCityRegionInfoIdArr = new ArrayList<Long>();
		List<Long> logInProjectInfoIdArr = new ArrayList<Long>();
		List<Long> logInBuildingInfoIdArr = new ArrayList<Long>();
		for(Sm_Permission_Range sm_Permission_Range_Temp : sm_Permission_RangeList)
		{
			Sm_CityRegionInfo cityRegionInfo = sm_Permission_Range_Temp.getCityRegionInfo();
			Empj_ProjectInfo projectInfo = sm_Permission_Range_Temp.getProjectInfo();
			Empj_BuildingInfo buildingInfo = sm_Permission_Range_Temp.getBuildingInfo();
			
			Long cityRegionInfoId = cityRegionInfo == null ? null : cityRegionInfo.getTableId();
			Long projectInfoId = projectInfo == null ? null : projectInfo.getTableId();
			Long buildingInfoIdId = buildingInfo == null ? null : buildingInfo.getTableId(); 
			
			if(cityRegionInfoId != null && !logInCityRegionInfoIdArr.contains(cityRegionInfoId))
			{
				logInCityRegionInfoIdArr.add(cityRegionInfoId);
			}
			if(projectInfoId != null && !logInProjectInfoIdArr.contains(projectInfoId))
			{
				logInProjectInfoIdArr.add(projectInfoId);
			}
			if(buildingInfoIdId != null && !logInBuildingInfoIdArr.contains(buildingInfoIdId))
			{
				logInBuildingInfoIdArr.add(buildingInfoIdId);
			}
		}
		
		if(logInCityRegionInfoIdArr.size() > 0)
		{
			cityReagionIdSet_Temp = new HashSet<Long>(logInCityRegionInfoIdArr);
			
			Empj_ProjectInfoForm projectInfo_TempModel = new Empj_ProjectInfoForm();
			projectInfo_TempModel.setCityRegionInfoIdArr(logInCityRegionInfoIdArr.toArray(new Long[logInCityRegionInfoIdArr.size()]));
			List<Empj_ProjectInfo> empj_ProjectInfoList = empj_ProjectInfoDao.findByPage(empj_ProjectInfoDao.getQuery(empj_ProjectInfoDao.getListByIdArrHQL(), projectInfo_TempModel));
			for(Empj_ProjectInfo empj_ProjectInfo : empj_ProjectInfoList)
			{
				if(empj_ProjectInfo == null) continue;
				projectIdSet_Temp.add(empj_ProjectInfo.getTableId());
			}
			
			Empj_BuildingInfoForm tempModel = new Empj_BuildingInfoForm();
			tempModel.setCityRegionInfoIdArr(logInCityRegionInfoIdArr.toArray(new Long[logInCityRegionInfoIdArr.size()]));
			List<Empj_BuildingInfo> empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getListByIdArrHQL(), tempModel));
			for(Empj_BuildingInfo empj_BuildingInfo : empj_BuildingInfoList)
			{
				if(empj_BuildingInfo == null) continue;
				buildingIdSet_Temp.add(empj_BuildingInfo.getTableId());
			}
			
			//查区域下面的开发企业
//			Emmp_CompanyInfoForm developCompanyForm = new Emmp_CompanyInfoForm();
//			developCompanyForm.setCityRegionInfoIdArr(logInCityRegionInfoIdArr.toArray(new Long[logInCityRegionInfoIdArr.size()]));
//			List<Emmp_CompanyInfo> developCompanyInfoList = emmp_CompanyInfoDao.findByPage(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getListByIdArrHQLForDevelopCompany(), developCompanyForm));
//			for(Emmp_CompanyInfo developCompanyInfo : developCompanyInfoList)
//			{
//				if(developCompanyInfo == null) continue;
//				developCompanyInfoIdSet_Temp.add(developCompanyInfo.getTableId());
//			}
			
			properties.put("cityRegionInfoIdArr", cityReagionIdSet_Temp.toArray(new Long[cityReagionIdSet_Temp.size()]));
			properties.put("developCompanyInfoIdArr", developCompanyInfoIdSet_Temp.toArray(new Long[developCompanyInfoIdSet_Temp.size()]));
			properties.put("projectInfoIdArr", projectIdSet_Temp.toArray(new Long[projectIdSet_Temp.size()]));
			properties.put("buildingInfoIdIdArr", buildingIdSet_Temp.toArray(new Long[buildingIdSet_Temp.size()]));
			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

			return properties;
		}
		
		if(logInProjectInfoIdArr.size() > 0)
		{
			Empj_ProjectInfoForm projectInfo_TempModel = new Empj_ProjectInfoForm();
			projectInfo_TempModel.setProjectInfoIdArr(logInProjectInfoIdArr.toArray(new Long[logInProjectInfoIdArr.size()]));
			List<Empj_ProjectInfo> empj_ProjectInfoList = empj_ProjectInfoDao.findByPage(empj_ProjectInfoDao.getQuery(empj_ProjectInfoDao.getListByIdArrHQL(), projectInfo_TempModel));
			for(Empj_ProjectInfo empj_ProjectInfo : empj_ProjectInfoList)
			{
				if(empj_ProjectInfo == null) continue;
				projectIdSet_Temp.add(empj_ProjectInfo.getTableId());
				
				Sm_CityRegionInfo sm_CityRegionInfo = empj_ProjectInfo.getCityRegion();
				if(sm_CityRegionInfo != null)
				{
					cityReagionIdSet_Temp.add(sm_CityRegionInfo.getTableId());
				}
				
				//关联项目下的开发企业
				Emmp_CompanyInfo developCompanyInfo = empj_ProjectInfo.getDevelopCompany();
				if(developCompanyInfo != null)
				{
					developCompanyInfoIdSet_Temp.add(developCompanyInfo.getTableId());
				}
			}
			
			Empj_BuildingInfoForm tempModel = new Empj_BuildingInfoForm();
			tempModel.setProjectInfoIdArr(logInProjectInfoIdArr.toArray(new Long[logInProjectInfoIdArr.size()]));
			List<Empj_BuildingInfo> empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getListByIdArrHQL(), tempModel));
			for(Empj_BuildingInfo empj_BuildingInfo : empj_BuildingInfoList)
			{
				if(empj_BuildingInfo == null) continue;
				buildingIdSet_Temp.add(empj_BuildingInfo.getTableId());
			}
			
			properties.put("cityRegionInfoIdArr", cityReagionIdSet_Temp.toArray(new Long[cityReagionIdSet_Temp.size()]));
			properties.put("developCompanyInfoIdArr", developCompanyInfoIdSet_Temp.toArray(new Long[developCompanyInfoIdSet_Temp.size()]));
			properties.put("projectInfoIdArr", projectIdSet_Temp.toArray(new Long[projectIdSet_Temp.size()]));
			properties.put("buildingInfoIdIdArr", buildingIdSet_Temp.toArray(new Long[buildingIdSet_Temp.size()]));
			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

			return properties;
		}
		
		if(logInBuildingInfoIdArr.size() > 0)
		{
			Empj_BuildingInfoForm tempModel = new Empj_BuildingInfoForm();
			tempModel.setBuildingInfoIdIdArr(logInBuildingInfoIdArr.toArray(new Long[logInBuildingInfoIdArr.size()]));
			List<Empj_BuildingInfo> empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getListByIdArrHQL(), tempModel));
			for(Empj_BuildingInfo empj_BuildingInfo : empj_BuildingInfoList)
			{
				if(empj_BuildingInfo == null) continue;
				buildingIdSet_Temp.add(empj_BuildingInfo.getTableId());
				
				Sm_CityRegionInfo city_Temp = empj_BuildingInfo.getCityRegion(); 
				Empj_ProjectInfo project_Temp = empj_BuildingInfo.getProject(); 
				Emmp_CompanyInfo developCompanyInfo = empj_BuildingInfo.getDevelopCompany();
				
				if(city_Temp != null)
				{
					cityReagionIdSet_Temp.add(city_Temp.getTableId());
				}
				
				if(project_Temp != null)
				{
					projectIdSet_Temp.add(project_Temp.getTableId());
				}
				
				//关联楼幢下面的开发企业
				if(developCompanyInfo != null)
				{
					developCompanyInfoIdSet_Temp.add(developCompanyInfo.getTableId());
				}
			}
			
			properties.put("cityRegionInfoIdArr", cityReagionIdSet_Temp.toArray(new Long[cityReagionIdSet_Temp.size()]));
			properties.put("developCompanyInfoIdArr", developCompanyInfoIdSet_Temp.toArray(new Long[developCompanyInfoIdSet_Temp.size()]));
			properties.put("projectInfoIdArr", projectIdSet_Temp.toArray(new Long[projectIdSet_Temp.size()]));
			properties.put("buildingInfoIdIdArr", buildingIdSet_Temp.toArray(new Long[buildingIdSet_Temp.size()]));
			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

			return properties;
		}
		
		//普通用户，且未分配范围授权
		if(S_UserType.CommonUser.equals(userType))
		{
			/**
			 * 20181113和苏经理沟通后的处理方案：
			 * 	原先不分配范围授权的时候，若是机构用户则只能看到自己创建的项目，楼幢信息。
			 * 	现在要求看到和登录用户机构类型一致即可以看到
			 */
			Emmp_CompanyInfo userCompany = sm_User.getCompany();
			if(userCompany != null)
			{
				//传机构下的项目
				Empj_ProjectInfoForm projectInfo_TempModel = new Empj_ProjectInfoForm();
				projectInfo_TempModel.setDevelopCompanyId(userCompany.getTableId());
				List<Empj_ProjectInfo> empj_ProjectInfoList = empj_ProjectInfoDao.findByPage(empj_ProjectInfoDao.getQuery(empj_ProjectInfoDao.getListByIdArrHQL(), projectInfo_TempModel));
				for(Empj_ProjectInfo empj_ProjectInfo : empj_ProjectInfoList)
				{
					if(empj_ProjectInfo == null) continue;
					projectIdSet_Temp.add(empj_ProjectInfo.getTableId());
					
					Sm_CityRegionInfo sm_CityRegionInfo = empj_ProjectInfo.getCityRegion();
					if(sm_CityRegionInfo != null)
					{
						cityReagionIdSet_Temp.add(sm_CityRegionInfo.getTableId());
					}
					
					Emmp_CompanyInfo developCompanyInfo = empj_ProjectInfo.getDevelopCompany();
					if(developCompanyInfo != null)
					{
						developCompanyInfoIdSet_Temp.add(developCompanyInfo.getTableId());
					}
				}
				
				//机构下未分配项目
				if(projectIdSet_Temp == null || projectIdSet_Temp.size() == 0)
				{
					projectIdSet_Temp = new HashSet<Long>();
					projectIdSet_Temp.add(-1L);

					buildingIdSet_Temp = new HashSet<Long>();
					buildingIdSet_Temp.add(-1L);
					
					//是开发企业用户
					if(companyInfo != null && S_CompanyType.Development.equals(companyInfo.getTheType()))
					{
						developCompanyInfoIdSet_Temp.add(companyInfo.getTableId());
					}
					else
					{
						developCompanyInfoIdSet_Temp.add(-1L);
					}
					
					properties.put("cityRegionInfoIdArr", cityReagionIdSet_Temp.toArray(new Long[cityReagionIdSet_Temp.size()]));
					properties.put("developCompanyInfoIdArr", developCompanyInfoIdSet_Temp.toArray(new Long[developCompanyInfoIdSet_Temp.size()]));
					properties.put("projectInfoIdArr", projectIdSet_Temp.toArray(new Long[projectIdSet_Temp.size()]));
					properties.put("buildingInfoIdIdArr", buildingIdSet_Temp.toArray(new Long[buildingIdSet_Temp.size()]));
					properties.put(S_NormalFlag.result, S_NormalFlag.success);
					properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
					
					return properties;
				}
				
				//传机构下的楼幢
				Empj_BuildingInfoForm tempModel = new Empj_BuildingInfoForm();
				tempModel.setProjectInfoIdArr(projectIdSet_Temp.toArray(new Long[projectIdSet_Temp.size()]));
				List<Empj_BuildingInfo> empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getListByIdArrHQL(), tempModel));
				for(Empj_BuildingInfo empj_BuildingInfo : empj_BuildingInfoList)
				{
					if(empj_BuildingInfo == null) continue;
					buildingIdSet_Temp.add(empj_BuildingInfo.getTableId());
				}
			}
			else
			{
				//过滤脏数据，登录用户，未关联机构信息
				buildingIdSet_Temp.add(-1L);
				projectIdSet_Temp.add(-1L);
				cityReagionIdSet_Temp.add(-1L);
				developCompanyInfoIdSet_Temp.add(-1L);
			}
		}
		
		properties.put("cityRegionInfoIdArr", cityReagionIdSet_Temp.toArray(new Long[cityReagionIdSet_Temp.size()]));
		properties.put("developCompanyInfoIdArr", developCompanyInfoIdSet_Temp.toArray(new Long[developCompanyInfoIdSet_Temp.size()]));
		properties.put("projectInfoIdArr", projectIdSet_Temp.toArray(new Long[projectIdSet_Temp.size()]));
		properties.put("buildingInfoIdIdArr", buildingIdSet_Temp.toArray(new Long[buildingIdSet_Temp.size()]));
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
