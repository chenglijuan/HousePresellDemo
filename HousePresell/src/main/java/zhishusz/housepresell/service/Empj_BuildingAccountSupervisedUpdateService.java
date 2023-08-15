package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_BuildingAccountSupervisedForm;
import zhishusz.housepresell.database.dao.Empj_BuildingAccountSupervisedDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpj_BankAccountSupervisedDao;
import zhishusz.housepresell.database.po.Empj_BuildingAccountSupervised;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.ObjectCopier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

/*
 * Service更新操作：楼幢与楼幢监管账号关联表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BuildingAccountSupervisedUpdateService
{
	@Autowired
	private Empj_BuildingAccountSupervisedDao empj_BuildingAccountSupervisedDao;
	@Autowired
	private Sm_BusiState_LogAddService logAddService;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Tgpj_BankAccountSupervisedDao tgpj_BankAccountSupervisedDao;
	
	public Properties execute(Empj_BuildingAccountSupervisedForm model)
	{
		Properties properties = new MyProperties();
		MyDatetime myDatetime = MyDatetime.getInstance();
		String busiState = model.getBusiState();
		String eCode = model.geteCode();
//		Long userStartId = model.getUserStartId();
//		Long createTimeStamp = model.getCreateTimeStamp();
		Long userUpdateId = model.getUserUpdateId();
		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
//		Long userRecordId = model.getUserRecordId();
//		Long recordTimeStamp = model.getRecordTimeStamp();
		Long buildingInfoId = model.getBuildingInfoId();
		Long bankAccountSupervisedId = model.getBankAccountSupervisedId();
//		Long beginTimeStamp = model.getBeginTimeStamp();
//		String beginTimeStampString = model.getBeginTimeStampString();
//		Long endTimeStamp = model.getEndTimeStamp();
//		String endTimeStampString = model.getEndTimeStampString();
		Integer isUsing=model.getIsUsing();
		
//		if(theState == null || theState < 1)
//		{
//			return MyBackInfo.fail(properties, "'theState'不能为空");
//		}
//		if(busiState == null || busiState.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'busiState'不能为空");
//		}
//		if(eCode == null || eCode.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'eCode'不能为空");
//		}
//		if(userStartId == null || userStartId < 1)
//		{
//			return MyBackInfo.fail(properties, "'userStart'不能为空");
//		}
//		if(createTimeStamp == null || createTimeStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "'createTimeStamp'不能为空");
//		}
//		if(userUpdateId == null || userUpdateId < 1)
//		{
//			return MyBackInfo.fail(properties, "'userUpdate'不能为空");
//		}
//		if(lastUpdateTimeStamp == null || lastUpdateTimeStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "'lastUpdateTimeStamp'不能为空");
//		}
//		if(userRecordId == null || userRecordId < 1)
//		{
//			return MyBackInfo.fail(properties, "'userRecord'不能为空");
//		}
//		if(recordTimeStamp == null || recordTimeStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "'recordTimeStamp'不能为空");
//		}

		if(buildingInfoId == null || buildingInfoId < 1)
		{
			return MyBackInfo.fail(properties, "'buildingInfo'不能为空");
		}
		if(bankAccountSupervisedId == null || bankAccountSupervisedId < 1)
		{
			return MyBackInfo.fail(properties, "'bankAccountSupervised'不能为空");
		}
		if(isUsing==null){
			return MyBackInfo.fail(properties, "请选择是否启用");
		}
//		if(beginTimeStampString == null || beginTimeStampString.length() < 1)
//		{
//			return MyBackInfo.fail(properties, "'beginTimeStamp'不能为空");
//		}
//		if(endTimeStampString == null || endTimeStampString.length() < 1)
//		{
//			return MyBackInfo.fail(properties, "'endTimeStamp'不能为空");
//		}
//		beginTimeStamp = myDatetime.stringToLong(beginTimeStampString);
//		endTimeStamp = myDatetime.stringToLong(endTimeStampString);

		//		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
//		if(userStart == null)
//		{
//			return MyBackInfo.fail(properties, "'userStart(Id:" + userStartId + ")'不存在");
//		}
		Sm_User userUpdate = (Sm_User)sm_UserDao.findById(userUpdateId);
//		if(userUpdate == null)
//		{
//			return MyBackInfo.fail(properties, "'userUpdate(Id:" + userUpdateId + ")'不存在");
//		}
//		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
//		if(userRecord == null)
//		{
//			return MyBackInfo.fail(properties, "'userRecord(Id:" + userRecordId + ")'不存在");
//		}
		Empj_BuildingInfo buildingInfo = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(buildingInfoId);
		if(buildingInfo == null)
		{
			return MyBackInfo.fail(properties, "'buildingInfo(Id:" + buildingInfoId + ")'不存在");
		}
		Tgpj_BankAccountSupervised bankAccountSupervised = (Tgpj_BankAccountSupervised)tgpj_BankAccountSupervisedDao.findById(bankAccountSupervisedId);
		if(bankAccountSupervised == null)
		{
			return MyBackInfo.fail(properties, "'bankAccountSupervised(Id:" + bankAccountSupervisedId + ")'不存在");
		}
	
		Long empj_BuildingAccountSupervisedId = model.getTableId();
		Empj_BuildingAccountSupervised empj_BuildingAccountSupervised = (Empj_BuildingAccountSupervised)empj_BuildingAccountSupervisedDao.findById(empj_BuildingAccountSupervisedId);
		Empj_BuildingAccountSupervised empj_BuildingAccountSupervisedOld = ObjectCopier.copy(empj_BuildingAccountSupervised);
		if(empj_BuildingAccountSupervised == null)
		{
			return MyBackInfo.fail(properties, "'Empj_BuildingAccountSupervised(Id:" + empj_BuildingAccountSupervisedId + ")'不存在");
		}
		
//		empj_BuildingAccountSupervised.setBusiState(busiState);
		empj_BuildingAccountSupervised.seteCode(eCode);
//		empj_BuildingAccountSupervised.setUserStart(userStart);
//		empj_BuildingAccountSupervised.setCreateTimeStamp(createTimeStamp);
		empj_BuildingAccountSupervised.setUserUpdate(userUpdate);
		empj_BuildingAccountSupervised.setUserUpdate(model.getUser());
		empj_BuildingAccountSupervised.setLastUpdateTimeStamp(System.currentTimeMillis());
//		empj_BuildingAccountSupervised.setUserRecord(userRecord);
//		empj_BuildingAccountSupervised.setRecordTimeStamp(recordTimeStamp);
		empj_BuildingAccountSupervised.setBuildingInfo(buildingInfo);
		empj_BuildingAccountSupervised.setBankAccountSupervised(bankAccountSupervised);
//		empj_BuildingAccountSupervised.setBeginTimeStamp(beginTimeStamp);
//		empj_BuildingAccountSupervised.setEndTimeStamp(endTimeStamp);
		empj_BuildingAccountSupervised.setIsUsing(isUsing);
	
		empj_BuildingAccountSupervisedDao.save(empj_BuildingAccountSupervised);
		logAddService.addLog(model, empj_BuildingAccountSupervisedId, empj_BuildingAccountSupervisedOld, empj_BuildingAccountSupervised);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tableId", empj_BuildingAccountSupervised.getTableId());
		
		return properties;
	}
}
