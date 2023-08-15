package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_Permission_RangeAuthorizationForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RangeAuthorizationDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RangeDao;
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
 * Service列表查询：范围授权添加
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Permission_RangeAuthorizationAddService
{
	private MyDatetime myDatetime = MyDatetime.getInstance();
	
	@Autowired
	private Sm_Permission_RangeAuthorizationDao sm_Permission_RangeAuthorizationDao;
	@Autowired
	private Sm_Permission_RangeDao sm_Permission_RangeDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	
	public Properties execute(Sm_Permission_RangeAuthorizationForm model)
	{
		Properties properties = new MyProperties();
		
		Long emmp_CompanyInfoId = model.getEmmp_CompanyInfoId();
		Integer rangeAuthType = model.getRangeAuthType();
		Long[] idArr = model.getIdArr();
		String authTimeStampRange = model.getAuthTimeStampRange();
		Sm_User login_User = model.getUser();
		
		if(emmp_CompanyInfoId == null || emmp_CompanyInfoId < 1)
		{
			return MyBackInfo.fail(properties, "请选择机构");
		}
		if(rangeAuthType == null)
		{
			return MyBackInfo.fail(properties, "请选择授权类别");
		}
		if(authTimeStampRange == null || authTimeStampRange.length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择授权日期");
		}
		
		Emmp_CompanyInfo emmp_CompanyInfo = emmp_CompanyInfoDao.findById(emmp_CompanyInfoId);
		if(emmp_CompanyInfo == null)
		{
			return MyBackInfo.fail(properties, "该机构不存在");
		}
		
		model.setEmmp_CompanyInfo(emmp_CompanyInfo);
		model.setTheState(S_TheState.Normal);
		Integer findByCheck_Size = sm_Permission_RangeAuthorizationDao.findByPage_Size(sm_Permission_RangeAuthorizationDao.createCriteriaForList(model, null));
		if(findByCheck_Size != null && findByCheck_Size > 0)
		{
			return MyBackInfo.fail(properties, "该机构已经添加范围授权，请勿重复添加");
		}
		
		Sm_Permission_RangeAuthorization sm_Permission_RangeAuthorization = new Sm_Permission_RangeAuthorization();
		
		sm_Permission_RangeAuthorization.setTheState(S_TheState.Normal);
		sm_Permission_RangeAuthorization.setUserStart(login_User);
		sm_Permission_RangeAuthorization.setCreateTimeStamp(System.currentTimeMillis());
		sm_Permission_RangeAuthorization.setForCompanyType(emmp_CompanyInfo.getTheType());
		sm_Permission_RangeAuthorization.setEmmp_CompanyInfo(emmp_CompanyInfo);
		sm_Permission_RangeAuthorization.setAuthStartTimeStamp(myDatetime.getDateTimeStampMin(authTimeStampRange));
		sm_Permission_RangeAuthorization.setAuthEndTimeStamp(myDatetime.getDateTimeStampMax(authTimeStampRange));
		sm_Permission_RangeAuthorization.setRangeAuthType(rangeAuthType);
		
		sm_Permission_RangeAuthorizationDao.save(sm_Permission_RangeAuthorization);
		
		//删除该机构原先所有的已经分配的数据
		sm_Permission_RangeDao.deleteBatch(null, emmp_CompanyInfo);
		//根据 授权类型 和 idArr 分别查询该模型信息，然后新增数据
		List<Sm_Permission_Range> rangeInfoList = new ArrayList<Sm_Permission_Range>();
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
				sm_Permission_Range.setAuthStartTimeStamp(myDatetime.getDateTimeStampMin(authTimeStampRange));
				sm_Permission_Range.setAuthEndTimeStamp(myDatetime.getDateTimeStampMax(authTimeStampRange));
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
				sm_Permission_Range.setAuthStartTimeStamp(myDatetime.getDateTimeStampMin(authTimeStampRange));
				sm_Permission_Range.setAuthEndTimeStamp(myDatetime.getDateTimeStampMax(authTimeStampRange));
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
				sm_Permission_Range.setAuthStartTimeStamp(myDatetime.getDateTimeStampMin(authTimeStampRange));
				sm_Permission_Range.setAuthEndTimeStamp(myDatetime.getDateTimeStampMax(authTimeStampRange));
				sm_Permission_RangeDao.save(sm_Permission_Range);
				
				rangeInfoList.add(sm_Permission_Range);
			}
		}
		
		sm_Permission_RangeAuthorization.setRangeInfoList(rangeInfoList);
		sm_Permission_RangeAuthorizationDao.save(sm_Permission_RangeAuthorization);
		
		properties.put("sm_Permission_RangeAuthorization", sm_Permission_RangeAuthorization);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
