package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Sm_StreetInfoForm;
import zhishusz.housepresell.database.dao.Sm_StreetInfoDao;
import zhishusz.housepresell.database.po.Sm_StreetInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;

/*
 * Service更新操作：Sm_StreetInfo
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_StreetInfoUpdateService
{
	@Autowired
	private Sm_StreetInfoDao sm_StreetInfoDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	
	public Properties execute(Sm_StreetInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Integer theState = model.getTheState();
		String busiState = model.getBusiState();
		String eCode = model.geteCode();
		Long userStartId = model.getUserStartId();
		Long createTimeStamp = model.getCreateTimeStamp();
		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
		Long userRecordId = model.getUserRecordId();
		Long recordTimeStamp = model.getRecordTimeStamp();
		Long cityRegionId = model.getCityRegionId();
		String theName = model.getTheName();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(busiState == null || busiState.length()< 1)
		{
			return MyBackInfo.fail(properties, "'busiState'不能为空");
		}
		if(eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCode'不能为空");
		}
		if(userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(createTimeStamp == null || createTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'createTimeStamp'不能为空");
		}
		if(lastUpdateTimeStamp == null || lastUpdateTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'lastUpdateTimeStamp'不能为空");
		}
		if(userRecordId == null || userRecordId < 1)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if(recordTimeStamp == null || recordTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'recordTimeStamp'不能为空");
		}
		if(cityRegionId == null || cityRegionId < 1)
		{
			return MyBackInfo.fail(properties, "'cityRegion'不能为空");
		}
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theName'不能为空");
		}
		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart(Id:" + userStartId + ")'不存在");
		}
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord(Id:" + userRecordId + ")'不存在");
		}
		Sm_CityRegionInfo cityRegion = (Sm_CityRegionInfo)sm_CityRegionInfoDao.findById(cityRegionId);
		if(cityRegion == null)
		{
			return MyBackInfo.fail(properties, "'cityRegion(Id:" + cityRegionId + ")'不存在");
		}
	
		Long sm_StreetInfoId = model.getTableId();
		Sm_StreetInfo sm_StreetInfo = (Sm_StreetInfo)sm_StreetInfoDao.findById(sm_StreetInfoId);
		if(sm_StreetInfo == null)
		{
			return MyBackInfo.fail(properties, "'Sm_StreetInfo(Id:" + sm_StreetInfoId + ")'不存在");
		}
		
		sm_StreetInfo.setTheState(theState);
		sm_StreetInfo.setBusiState(busiState);
		sm_StreetInfo.seteCode(eCode);
		sm_StreetInfo.setUserStart(userStart);
		sm_StreetInfo.setCreateTimeStamp(createTimeStamp);
		sm_StreetInfo.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		sm_StreetInfo.setUserRecord(userRecord);
		sm_StreetInfo.setRecordTimeStamp(recordTimeStamp);
		sm_StreetInfo.setCityRegion(cityRegion);
		sm_StreetInfo.setTheName(theName);
	
		sm_StreetInfoDao.save(sm_StreetInfo);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
