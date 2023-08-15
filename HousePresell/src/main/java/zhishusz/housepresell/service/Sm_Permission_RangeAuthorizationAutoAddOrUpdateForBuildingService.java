package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_Permission_RangeAuthorizationForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RangeAuthorizationDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RangeDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_Permission_Range;
import zhishusz.housepresell.database.po.Sm_Permission_RangeAuthorization;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_RangeAuthType;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：添加项目且审批通过时，调用该业务，
 * 系统将自动添加或追加范围授权
 * 	2018-11-06
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Permission_RangeAuthorizationAutoAddOrUpdateForBuildingService
{
	private MyDatetime myDatetime = MyDatetime.getInstance();
	
	@Autowired
	private Sm_Permission_RangeAuthorizationDao sm_Permission_RangeAuthorizationDao;
	@Autowired
	private Sm_Permission_RangeDao sm_Permission_RangeDao;
	/*@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;*/
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	/**
	 * 调用该业务必传参数：
	 * sponsorId 审批发起人Id
	 * buildingInfoId 楼幢Id
	 * @param model
	 * @return
	 */
	public Properties execute(Sm_Permission_RangeAuthorizationForm model)
	{
		Properties properties = new MyProperties();
		Integer rangeAuthType = S_RangeAuthType.Building;
		
		Long sponsorId = model.getSponsorId();
		if(sponsorId == null || sponsorId < 1)
		{
			return MyBackInfo.fail(properties, "请传入发起人Id");
		}
		
		Sm_User sponsor = sm_UserDao.findById(sponsorId);
		if(sponsor == null)
		{
			return MyBackInfo.fail(properties, "该发起人不存在");
		}
		
		Long buildingInfoId = model.getBuildingInfoId();
		Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findById(buildingInfoId);
		if(empj_BuildingInfo == null)
		{
			return MyBackInfo.fail(properties, "该楼幢不存在");
		}
		
		Emmp_CompanyInfo emmp_CompanyInfo = empj_BuildingInfo.getDevelopCompany();
		if(emmp_CompanyInfo == null)
		{
			return MyBackInfo.fail(properties, "该楼幢未关联企业信息");
		}
		
		Empj_ProjectInfo empj_ProjectInfo = empj_BuildingInfo.getProject();
		if(empj_ProjectInfo == null)
		{
			return MyBackInfo.fail(properties, "该楼幢未关联项目");
		}
		
		List<Sm_Permission_Range> rangeInfoList = new ArrayList<Sm_Permission_Range>();
		List<Long> idList = new ArrayList<Long>();
		idList.add(buildingInfoId);
		Long[] idArr = idList.toArray(new Long[idList.size()]);
		
		model.setRangeAuthType(rangeAuthType);
		model.setEmmp_CompanyInfo(emmp_CompanyInfo);
		model.setTheState(S_TheState.Normal);
		Sm_Permission_RangeAuthorization sm_Permission_RangeAuthorization = sm_Permission_RangeAuthorizationDao.findOne(sm_Permission_RangeAuthorizationDao.createCriteriaForList(model, null));
		if(sm_Permission_RangeAuthorization == null)
		{
			//============== 自动添加的范围授权，默认1年的有效期 Start ===============================//
			Calendar nowCal = Calendar.getInstance();
			Long nowTimeStamp = nowCal.getTimeInMillis();
			String startDateStr = myDatetime.dateToString(nowTimeStamp, "yyyy-MM-dd");
			
			nowCal.add(Calendar.YEAR, 1);
			Long afterTimeStamp = nowCal.getTimeInMillis();
			String endDateStr = myDatetime.dateToString(afterTimeStamp, "yyyy-MM-dd");
			
			String authTimeStampRange = startDateStr +" - "+endDateStr;
			//============== 自动添加的范围授权，默认1年的有效期 End ===============================//
			
			sm_Permission_RangeAuthorization = new Sm_Permission_RangeAuthorization();
			sm_Permission_RangeAuthorization.setTheState(S_TheState.Normal);
			sm_Permission_RangeAuthorization.setUserStart(sponsor);
			sm_Permission_RangeAuthorization.setCreateTimeStamp(nowTimeStamp);
			sm_Permission_RangeAuthorization.setForCompanyType(emmp_CompanyInfo.getTheType());
			sm_Permission_RangeAuthorization.setEmmp_CompanyInfo(emmp_CompanyInfo);
			sm_Permission_RangeAuthorization.setAuthStartTimeStamp(myDatetime.getDateTimeStampMin(authTimeStampRange));
			sm_Permission_RangeAuthorization.setAuthEndTimeStamp(myDatetime.getDateTimeStampMax(authTimeStampRange));
			sm_Permission_RangeAuthorization.setRangeAuthType(rangeAuthType);

			sm_Permission_RangeAuthorizationDao.save(sm_Permission_RangeAuthorization);
			
			//删除该机构原先所有的已经分配的数据
			sm_Permission_RangeDao.deleteBatch(null, emmp_CompanyInfo);
			
			//根据 授权类型 和 idArr 分别查询该模型信息，然后新增数据
			rangeInfoList = new ArrayList<Sm_Permission_Range>();
		}
		else
		{
			rangeInfoList = sm_Permission_RangeAuthorization.getRangeInfoList();
			if(rangeInfoList == null) rangeInfoList = new ArrayList<Sm_Permission_Range>();
		}
		
		Long authStartTimeStamp = sm_Permission_RangeAuthorization.getAuthStartTimeStamp();
		Long authEndTimeStamp = sm_Permission_RangeAuthorization.getAuthEndTimeStamp();
		
		if(S_RangeAuthType.Area.equals(rangeAuthType) && idArr != null)
		{
			for(Long id : idArr)
			{
				Sm_CityRegionInfo cityRegionInfo = sm_CityRegionInfoDao.findById(id);
				if(cityRegionInfo == null) continue;
				
				Sm_Permission_Range sm_Permission_Range = new Sm_Permission_Range();
				sm_Permission_Range.setTheState(S_TheState.Normal);
				sm_Permission_Range.setTheType(S_RangeAuthType.Area);
				sm_Permission_Range.setCityRegionInfo(cityRegionInfo);
				sm_Permission_Range.setCompanyInfo(emmp_CompanyInfo);
				sm_Permission_Range.setRangeAuth(sm_Permission_RangeAuthorization);
				sm_Permission_Range.setAuthStartTimeStamp(authStartTimeStamp);
				sm_Permission_Range.setAuthEndTimeStamp(authEndTimeStamp);
				sm_Permission_RangeDao.save(sm_Permission_Range);
				
				rangeInfoList.add(sm_Permission_Range);
			}
		}
		else if(S_RangeAuthType.Project.equals(rangeAuthType) && idArr != null)
		{
			for(Long id : idArr)
			{
				Empj_ProjectInfo projectInfo = empj_ProjectInfoDao.findById(id);
				if(projectInfo == null) continue;
				
				Sm_Permission_Range sm_Permission_Range = new Sm_Permission_Range();
				sm_Permission_Range.setTheState(S_TheState.Normal);
				sm_Permission_Range.setTheType(S_RangeAuthType.Project);
				sm_Permission_Range.setProjectInfo(projectInfo);
				sm_Permission_Range.setCompanyInfo(emmp_CompanyInfo);
				sm_Permission_Range.setRangeAuth(sm_Permission_RangeAuthorization);
				sm_Permission_Range.setAuthStartTimeStamp(authStartTimeStamp);
				sm_Permission_Range.setAuthEndTimeStamp(authEndTimeStamp);
				sm_Permission_RangeDao.save(sm_Permission_Range);
				
				rangeInfoList.add(sm_Permission_Range);
			}
		}
		else if(S_RangeAuthType.Building.equals(rangeAuthType) && idArr != null)
		{
			for(Long id : idArr)
			{
				Empj_BuildingInfo buildingInfo = empj_BuildingInfoDao.findById(id);
				if(buildingInfo == null) continue;
				
				Sm_Permission_Range sm_Permission_Range = new Sm_Permission_Range();
				sm_Permission_Range.setTheState(S_TheState.Normal);
				sm_Permission_Range.setTheType(S_RangeAuthType.Building);
				sm_Permission_Range.setBuildingInfo(buildingInfo);
				sm_Permission_Range.setCompanyInfo(emmp_CompanyInfo);
				sm_Permission_Range.setRangeAuth(sm_Permission_RangeAuthorization);
				sm_Permission_Range.setAuthStartTimeStamp(authStartTimeStamp);
				sm_Permission_Range.setAuthEndTimeStamp(authEndTimeStamp);
				sm_Permission_RangeDao.save(sm_Permission_Range);
				
				rangeInfoList.add(sm_Permission_Range);
			}
		}
		
		sm_Permission_RangeAuthorization.setRangeInfoList(rangeInfoList);
		sm_Permission_RangeAuthorizationDao.save(sm_Permission_RangeAuthorization);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
