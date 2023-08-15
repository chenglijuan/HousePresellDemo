package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_Permission_RangeAuthorizationForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RangeAuthorizationDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RangeDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
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
import zhishusz.housepresell.util.objectdiffer.model.Sm_Permission_RangeAuthorizationTemplate;
import zhishusz.housepresell.util.project.RangeAuthorizationLogUtil;
import zhishusz.housepresell.util.rebuild.Sm_Permission_RangeAuthorizationRebuild;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：正泰机构范围授权编辑
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Permission_RangeAuthorizationForZTEditService
{
	private MyDatetime myDatetime = MyDatetime.getInstance();

	@Autowired
	private Sm_Permission_RangeAuthorizationDao sm_Permission_RangeAuthorizationDao;
	@Autowired
	private Sm_Permission_RangeDao sm_Permission_RangeDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	@Autowired
	private Sm_BusiState_LogAddService logAddService;
	@Autowired
	private Sm_Permission_RangeAuthorizationRebuild sm_Permission_RangeAuthorizationRebuild;
	@Autowired
	private RangeAuthorizationLogUtil rangeAuthorizationLogUtil;

	public Properties execute(Sm_Permission_RangeAuthorizationForm model)
	{
		Properties properties = new MyProperties();

		Long tableId = model.getTableId();
		Long userInfoId = model.getUserInfoId();
		Integer rangeAuthType = model.getRangeAuthType();
		Long[] idArr = model.getIdArr();
		String authTimeStampRange = model.getAuthTimeStampRange();
		Sm_User login_User = model.getUser();

		if(tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "请选择范围授权信息");
		}
		if(userInfoId == null || userInfoId < 1)
		{
			return MyBackInfo.fail(properties, "请选择正泰用户");
		}
		if(rangeAuthType == null)
		{
			return MyBackInfo.fail(properties, "请选择授权类别");
		}
		if(authTimeStampRange == null || authTimeStampRange.length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择授权日期");
		}

		Sm_Permission_RangeAuthorization sm_Permission_RangeAuthorization = sm_Permission_RangeAuthorizationDao.findById(tableId);
		Sm_Permission_RangeAuthorizationTemplate rangeTemplate = new Sm_Permission_RangeAuthorizationTemplate();
		Sm_Permission_RangeAuthorizationTemplate rangeTemplateOld = rangeAuthorizationLogUtil.getCopyTemplate(sm_Permission_RangeAuthorization, rangeTemplate);
//		Sm_Permission_RangeAuthorizationForZTTemplate sm_Permission_RangeAuthorizationTemplate = new Sm_Permission_RangeAuthorizationForZTTemplate();
//		sm_Permission_RangeAuthorizationTemplate.setRangeAuthorization(sm_Permission_RangeAuthorization);
//		Sm_Permission_RangeAuthorizationForZTTemplate oldTemplate = ObjectCopier.copy(sm_Permission_RangeAuthorizationTemplate);
		if(sm_Permission_RangeAuthorization == null)
		{
			return MyBackInfo.fail(properties, "该范围授权信息不存在");
		}
//		oldTemplate.setRangeInfoListString(sm_Permission_RangeAuthorizationRebuild.getRangeInfoListStr(sm_Permission_RangeAuthorization));

		Sm_User userInfo = sm_UserDao.findById(userInfoId);
		if(userInfo == null)
		{
			return MyBackInfo.fail(properties, "该正泰用户不存在");
		}

		model.setExceptTableId(tableId);
		model.setUserInfo(userInfo);
		model.setTheState(S_TheState.Normal);
		Integer findByCheck_Size = sm_Permission_RangeAuthorizationDao.findByPage_Size(sm_Permission_RangeAuthorizationDao.createCriteriaForList_ForZT(model, null));
		if(findByCheck_Size != null && findByCheck_Size > 0)
		{
			return MyBackInfo.fail(properties, "该正泰用户已经添加范围授权，请勿重复添加");
		}

		sm_Permission_RangeAuthorization.setUserUpdate(login_User);
		sm_Permission_RangeAuthorization.setLastUpdateTimeStamp(System.currentTimeMillis());
		sm_Permission_RangeAuthorization.setUserInfo(userInfo);
		sm_Permission_RangeAuthorization.setAuthStartTimeStamp(myDatetime.getDateTimeStampMin(authTimeStampRange));
		sm_Permission_RangeAuthorization.setAuthEndTimeStamp(myDatetime.getDateTimeStampMax(authTimeStampRange));
		sm_Permission_RangeAuthorization.setRangeAuthType(rangeAuthType);

		sm_Permission_RangeAuthorizationDao.save(sm_Permission_RangeAuthorization);

		//删除该正泰用户原先所有的已经分配的数据
		sm_Permission_RangeDao.deleteBatch(userInfo, null);
		//根据 授权类型 和 idArr 分别查询该模型信息，然后新增数据
		List<Sm_Permission_Range> rangeInfoList = new ArrayList<Sm_Permission_Range>();
		if(S_RangeAuthType.Area.equals(rangeAuthType) && idArr != null)
		{
			for(Long id : idArr)
			{
				Sm_CityRegionInfo cityRegionInfo = sm_CityRegionInfoDao.findById(id);
				if(cityRegionInfo == null) continue;

				Sm_Permission_Range sm_Permission_Range = new Sm_Permission_Range();
				sm_Permission_Range.setUserInfo(userInfo);
				sm_Permission_Range.setTheState(S_TheState.Normal);
				sm_Permission_Range.setTheType(S_RangeAuthType.Area);
				sm_Permission_Range.setCityRegionInfo(cityRegionInfo);
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
				sm_Permission_Range.setUserInfo(userInfo);
				sm_Permission_Range.setTheState(S_TheState.Normal);
				sm_Permission_Range.setTheType(S_RangeAuthType.Project);
				sm_Permission_Range.setProjectInfo(projectInfo);
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
				sm_Permission_Range.setUserInfo(userInfo);
				sm_Permission_Range.setTheState(S_TheState.Normal);
				sm_Permission_Range.setTheType(S_RangeAuthType.Building);
				sm_Permission_Range.setBuildingInfo(buildingInfo);
				sm_Permission_Range.setRangeAuth(sm_Permission_RangeAuthorization);
				sm_Permission_Range.setAuthStartTimeStamp(myDatetime.getDateTimeStampMin(authTimeStampRange));
				sm_Permission_Range.setAuthEndTimeStamp(myDatetime.getDateTimeStampMax(authTimeStampRange));
				sm_Permission_RangeDao.save(sm_Permission_Range);

				rangeInfoList.add(sm_Permission_Range);
			}
		}

		sm_Permission_RangeAuthorization.setRangeInfoList(rangeInfoList);
		sm_Permission_RangeAuthorizationDao.save(sm_Permission_RangeAuthorization);
		Sm_Permission_RangeAuthorizationTemplate rangeTemplateNew = rangeAuthorizationLogUtil.getCopyTemplate(sm_Permission_RangeAuthorization, rangeTemplate);

//		Sm_Permission_RangeAuthorizationForZTTemplate newTemplate = ObjectCopier.copy(sm_Permission_RangeAuthorizationTemplate);
//		newTemplate.setRangeInfoListString(sm_Permission_RangeAuthorizationRebuild.getRangeInfoListStr(sm_Permission_RangeAuthorization));
		logAddService.addLog(model, tableId, rangeTemplateOld, rangeTemplateNew);

		properties.put("sm_Permission_RangeAuthorization", sm_Permission_RangeAuthorization);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
