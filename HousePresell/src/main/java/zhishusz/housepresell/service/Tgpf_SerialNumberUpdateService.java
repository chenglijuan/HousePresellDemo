package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Tgpf_SerialNumberForm;
import zhishusz.housepresell.database.dao.Tgpf_SerialNumberDao;
import zhishusz.housepresell.database.po.Tgpf_SerialNumber;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;

/*
 * Service更新操作：流水号
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_SerialNumberUpdateService
{
	@Autowired
	private Tgpf_SerialNumberDao tgpf_SerialNumberDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Tgpf_SerialNumberForm model)
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
		String businessType = model.getBusinessType();
		Integer serialNumber = model.getSerialNumber();
		String serialDate = model.getSerialDate();
		
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
		if(businessType == null || businessType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'businessType'不能为空");
		}
		if(serialNumber == null || serialNumber < 1)
		{
			return MyBackInfo.fail(properties, "'serialNumber'不能为空");
		}
		if(serialDate == null || serialDate.length() == 0)
		{
			return MyBackInfo.fail(properties, "'serialDate'不能为空");
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
	
		Long tgpf_SerialNumberId = model.getTableId();
		Tgpf_SerialNumber tgpf_SerialNumber = (Tgpf_SerialNumber)tgpf_SerialNumberDao.findById(tgpf_SerialNumberId);
		if(tgpf_SerialNumber == null)
		{
			return MyBackInfo.fail(properties, "'Tgpf_SerialNumber(Id:" + tgpf_SerialNumberId + ")'不存在");
		}
		
		tgpf_SerialNumber.setTheState(theState);
		tgpf_SerialNumber.setBusiState(busiState);
		tgpf_SerialNumber.seteCode(eCode);
		tgpf_SerialNumber.setUserStart(userStart);
		tgpf_SerialNumber.setCreateTimeStamp(createTimeStamp);
		tgpf_SerialNumber.setUserUpdate(userUpdate);
		tgpf_SerialNumber.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgpf_SerialNumber.setUserRecord(userRecord);
		tgpf_SerialNumber.setRecordTimeStamp(recordTimeStamp);
		tgpf_SerialNumber.setBusinessType(businessType);
		tgpf_SerialNumber.setSerialNumber(serialNumber);
		tgpf_SerialNumber.setSerialDate(serialDate);
	
		tgpf_SerialNumberDao.save(tgpf_SerialNumber);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
