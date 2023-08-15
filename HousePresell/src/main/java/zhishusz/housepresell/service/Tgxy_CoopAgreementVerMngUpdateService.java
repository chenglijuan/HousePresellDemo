package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementVerMngForm;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementVerMngDao;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementVerMng;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;

/*
 * Service更新操作：合作协议版本管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_CoopAgreementVerMngUpdateService
{
	@Autowired
	private Tgxy_CoopAgreementVerMngDao tgxy_CoopAgreementVerMngDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Tgxy_CoopAgreementVerMngForm model)
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
		String theName = model.getTheName();
		String theVersion = model.getTheVersion();
		String theType = model.getTheType();
//		Long enableTimeStamp = model.getEnableTimeStamp();
//		Long downTimeStamp = model.getDownTimeStamp();
		String templateFilePath = model.getTemplateFilePath();
		
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
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theName'不能为空");
		}
		if(theVersion == null || theVersion.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theVersion'不能为空");
		}
		if(theType == null || theType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theType'不能为空");
		}
//		if(enableTimeStamp == null || enableTimeStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "'enableTimeStamp'不能为空");
//		}
//		if(downTimeStamp == null || downTimeStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "'downTimeStamp'不能为空");
//		}
		if(templateFilePath == null || templateFilePath.length() == 0)
		{
			return MyBackInfo.fail(properties, "'templateFilePath'不能为空");
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
	
		Long tgxy_CoopAgreementVerMngId = model.getTableId();
		Tgxy_CoopAgreementVerMng tgxy_CoopAgreementVerMng = (Tgxy_CoopAgreementVerMng)tgxy_CoopAgreementVerMngDao.findById(tgxy_CoopAgreementVerMngId);
		if(tgxy_CoopAgreementVerMng == null)
		{
			return MyBackInfo.fail(properties, "'Tgxy_CoopAgreementVerMng(Id:" + tgxy_CoopAgreementVerMngId + ")'不存在");
		}
		
		tgxy_CoopAgreementVerMng.setTheState(theState);
		tgxy_CoopAgreementVerMng.setBusiState(busiState);
		tgxy_CoopAgreementVerMng.seteCode(eCode);
		tgxy_CoopAgreementVerMng.setUserStart(userStart);
		tgxy_CoopAgreementVerMng.setCreateTimeStamp(createTimeStamp);
		tgxy_CoopAgreementVerMng.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgxy_CoopAgreementVerMng.setUserRecord(userRecord);
		tgxy_CoopAgreementVerMng.setRecordTimeStamp(recordTimeStamp);
		tgxy_CoopAgreementVerMng.setTheName(theName);
		tgxy_CoopAgreementVerMng.setTheVersion(theVersion);
		tgxy_CoopAgreementVerMng.setTheType(theType);
//		tgxy_CoopAgreementVerMng.setEnableTimeStamp(enableTimeStamp);
//		tgxy_CoopAgreementVerMng.setDownTimeStamp(downTimeStamp);
		tgxy_CoopAgreementVerMng.setTemplateFilePath(templateFilePath);
	
		tgxy_CoopAgreementVerMngDao.save(tgxy_CoopAgreementVerMng);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
