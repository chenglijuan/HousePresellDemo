package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_BusinessCodeForm;
import zhishusz.housepresell.database.dao.Sm_BusinessCodeDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_BusinessCode;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：业务编号
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_BusinessCodeUpdateService
{
	@Autowired
	private Sm_BusinessCodeDao sm_BusinessCodeDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Sm_BusinessCodeForm model)
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
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart(Id:" + userStartId + ")'不存在");
		}
		Sm_User userUpdate = (Sm_User)sm_UserDao.findById(userUpdateId);
		if(userUpdate == null)
		{
			return MyBackInfo.fail(properties, "'userUpdate(Id:" + userUpdateId + ")'不存在");
		}
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord(Id:" + userRecordId + ")'不存在");
		}
	
		Long sm_BusinessCodeId = model.getTableId();
		Sm_BusinessCode sm_BusinessCode = (Sm_BusinessCode)sm_BusinessCodeDao.findById(sm_BusinessCodeId);
		if(sm_BusinessCode == null)
		{
			return MyBackInfo.fail(properties, "'Sm_BusinessCode(Id:" + sm_BusinessCodeId + ")'不存在");
		}
		
		sm_BusinessCode.setTheState(theState);
		sm_BusinessCode.setBusiState(busiState);
		sm_BusinessCode.seteCode(eCode);
		sm_BusinessCode.setUserStart(userStart);
		sm_BusinessCode.setCreateTimeStamp(createTimeStamp);
		sm_BusinessCode.setUserUpdate(userUpdate);
		sm_BusinessCode.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		sm_BusinessCode.setUserRecord(userRecord);
		sm_BusinessCode.setRecordTimeStamp(recordTimeStamp);
		sm_BusinessCode.setBusiCode(busiCode);
		sm_BusinessCode.setTheYear(theYear);
		sm_BusinessCode.setTheMonth(theMonth);
		sm_BusinessCode.setTheDay(theDay);
		sm_BusinessCode.setTicketCount(ticketCount);
	
		sm_BusinessCodeDao.save(sm_BusinessCode);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
