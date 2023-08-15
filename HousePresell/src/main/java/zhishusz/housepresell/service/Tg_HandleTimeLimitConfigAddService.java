package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tg_HandleTimeLimitConfigForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tg_HandleTimeLimitConfigDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_HandleTimeLimitConfig;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Properties;
	
/*
 * Service添加操作：办理时限配置表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_HandleTimeLimitConfigAddService
{
	@Autowired
	private Tg_HandleTimeLimitConfigDao tg_HandleTimeLimitConfigDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Sm_Permission_RoleDao sm_Permission_RoleDao;
	
	public Properties execute(Tg_HandleTimeLimitConfigForm model)
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
		String theType = model.getTheType();
		String completionStandard = model.getCompletionStandard();
		Integer limitDayNumber = model.getLimitDayNumber();
		Long roleId = model.getRoleId();
		String lastCfgUser = model.getLastCfgUser();
		Long lastCfgTimeStamp = model.getLastCfgTimeStamp();
		
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
		if(theType == null || theType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theType'不能为空");
		}
		if(completionStandard == null || completionStandard.length() == 0)
		{
			return MyBackInfo.fail(properties, "'completionStandard'不能为空");
		}
		if(limitDayNumber == null || limitDayNumber < 1)
		{
			return MyBackInfo.fail(properties, "'limitDayNumber'不能为空");
		}
		if(roleId == null || roleId == 0)
		{
			return MyBackInfo.fail(properties, "'roleId'不能为空");
		}
		if(lastCfgUser == null || lastCfgUser.length() == 0)
		{
			return MyBackInfo.fail(properties, "'lastCfgUser'不能为空");
		}
		if(lastCfgTimeStamp == null || lastCfgTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'lastCfgTimeStamp'不能为空");
		}

		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
	
		Tg_HandleTimeLimitConfig tg_HandleTimeLimitConfig = new Tg_HandleTimeLimitConfig();
		tg_HandleTimeLimitConfig.setTheState(theState);
		tg_HandleTimeLimitConfig.setBusiState(busiState);
		tg_HandleTimeLimitConfig.seteCode(eCode);
		tg_HandleTimeLimitConfig.setUserStart(userStart);
		tg_HandleTimeLimitConfig.setCreateTimeStamp(createTimeStamp);
		tg_HandleTimeLimitConfig.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tg_HandleTimeLimitConfig.setUserRecord(userRecord);
		tg_HandleTimeLimitConfig.setRecordTimeStamp(recordTimeStamp);
		tg_HandleTimeLimitConfig.setTheType(theType);
		tg_HandleTimeLimitConfig.setCompletionStandard(completionStandard);
		tg_HandleTimeLimitConfig.setLimitDayNumber(limitDayNumber);
		tg_HandleTimeLimitConfig.setRole(sm_Permission_RoleDao.findById(model.getRoleId()));
		tg_HandleTimeLimitConfig.setLastCfgUser(lastCfgUser);
		tg_HandleTimeLimitConfig.setLastCfgTimeStamp(lastCfgTimeStamp);
		tg_HandleTimeLimitConfigDao.save(tg_HandleTimeLimitConfig);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
