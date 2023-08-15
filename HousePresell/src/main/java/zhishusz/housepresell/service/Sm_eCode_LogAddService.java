package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_eCode_LogForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Sm_eCode_LogDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_eCode_Log;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：eCode记录
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_eCode_LogAddService
{
	@Autowired
	private Sm_eCode_LogDao sm_eCode_LogDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Sm_eCode_LogForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = model.getTheState();
		String busiState = model.getBusiState();
		String eCode = model.geteCode();
		Long userStartId = model.getUserStartId();
		Long createTimeStamp = model.getCreateTimeStamp();
		Long userUpdateId = model.getUserUpdateId();
		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
		Long userRecordId = model.getUserRecordId();
		Long recordTimeStamp = model.getRecordTimeStamp();
		Integer recordState = model.getRecordState();
		String recordRejectReason = model.getRecordRejectReason();
		String busiCode = model.getBusiCode();
		Integer theYear = model.getTheYear();
		Integer theMonth = model.getTheMonth();
		Integer theDay = model.getTheDay();
		Integer ticketCount = model.getTicketCount();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(busiState == null || busiState.length() == 0)
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
		if(userUpdateId == null || userUpdateId < 1)
		{
			return MyBackInfo.fail(properties, "'userUpdate'不能为空");
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
		if(recordState == null || recordState < 1)
		{
			return MyBackInfo.fail(properties, "'recordState'不能为空");
		}
		if(recordRejectReason == null || recordRejectReason.length() == 0)
		{
			return MyBackInfo.fail(properties, "'recordRejectReason'不能为空");
		}
		if(busiCode == null || busiCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'busiCode'不能为空");
		}
		if(theYear == null || theYear < 1)
		{
			return MyBackInfo.fail(properties, "'theYear'不能为空");
		}
		if(theMonth == null || theMonth < 1)
		{
			return MyBackInfo.fail(properties, "'theMonth'不能为空");
		}
		if(theDay == null || theDay < 1)
		{
			return MyBackInfo.fail(properties, "'theDay'不能为空");
		}
		if(ticketCount == null || ticketCount < 1)
		{
			return MyBackInfo.fail(properties, "'ticketCount'不能为空");
		}

		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		Sm_User userUpdate = (Sm_User)sm_UserDao.findById(userUpdateId);
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(userUpdate == null)
		{
			return MyBackInfo.fail(properties, "'userUpdate'不能为空");
		}
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
	
		Sm_eCode_Log sm_eCode_Log = new Sm_eCode_Log();
		sm_eCode_Log.setTheState(theState);
		sm_eCode_Log.setBusiState(busiState);
		sm_eCode_Log.seteCode(eCode);
		sm_eCode_Log.setUserStart(userStart);
		sm_eCode_Log.setCreateTimeStamp(createTimeStamp);
		sm_eCode_Log.setUserUpdate(userUpdate);
		sm_eCode_Log.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		sm_eCode_Log.setUserRecord(userRecord);
		sm_eCode_Log.setRecordTimeStamp(recordTimeStamp);
		sm_eCode_Log.setRecordState(recordState);
		sm_eCode_Log.setRecordRejectReason(recordRejectReason);
		sm_eCode_Log.setBusiCode(busiCode);
		sm_eCode_Log.setTheYear(theYear);
		sm_eCode_Log.setTheMonth(theMonth);
		sm_eCode_Log.setTheDay(theDay);
		sm_eCode_Log.setTicketCount(ticketCount);
		sm_eCode_LogDao.save(sm_eCode_Log);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
