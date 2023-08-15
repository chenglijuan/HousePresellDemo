package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementVerMngForm;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementVerMngDao;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreementVerMng;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
	
/*
 * Service添加操作：三方协议版本管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_TripleAgreementVerMngAddService
{
	@Autowired
	private Tgxy_TripleAgreementVerMngDao tgxy_TripleAgreementVerMngDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Tgxy_TripleAgreementVerMngForm model)
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
		String eCodeOfCooperationAgreement = model.geteCodeOfCooperationAgreement();
		String theNameOfCooperationAgreement = model.getTheNameOfCooperationAgreement();
		Long enableTimeStamp = model.getEnableTimeStamp();
		Long downTimeStamp = model.getDownTimeStamp();
		String templateContentStyle = model.getTemplateContentStyle();
		
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
		if(eCodeOfCooperationAgreement == null || eCodeOfCooperationAgreement.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfCooperationAgreement'不能为空");
		}
		if(theNameOfCooperationAgreement == null || theNameOfCooperationAgreement.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfCooperationAgreement'不能为空");
		}
		if(enableTimeStamp == null || enableTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'enableTimeStamp'不能为空");
		}
		if(downTimeStamp == null || downTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'downTimeStamp'不能为空");
		}
		if(templateContentStyle == null || templateContentStyle.length() == 0)
		{
			return MyBackInfo.fail(properties, "'templateContentStyle'不能为空");
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
	
		Tgxy_TripleAgreementVerMng tgxy_TripleAgreementVerMng = new Tgxy_TripleAgreementVerMng();
		tgxy_TripleAgreementVerMng.setTheState(theState);
		tgxy_TripleAgreementVerMng.setBusiState(busiState);
		tgxy_TripleAgreementVerMng.seteCode(eCode);
		tgxy_TripleAgreementVerMng.setUserStart(userStart);
		tgxy_TripleAgreementVerMng.setCreateTimeStamp(createTimeStamp);
		tgxy_TripleAgreementVerMng.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgxy_TripleAgreementVerMng.setUserRecord(userRecord);
		tgxy_TripleAgreementVerMng.setRecordTimeStamp(recordTimeStamp);
		tgxy_TripleAgreementVerMng.setTheName(theName);
		tgxy_TripleAgreementVerMng.setTheVersion(theVersion);
		tgxy_TripleAgreementVerMng.setTheType(theType);
		tgxy_TripleAgreementVerMng.seteCodeOfCooperationAgreement(eCodeOfCooperationAgreement);
		tgxy_TripleAgreementVerMng.setTheNameOfCooperationAgreement(theNameOfCooperationAgreement);
		tgxy_TripleAgreementVerMng.setEnableTimeStamp(enableTimeStamp);
		tgxy_TripleAgreementVerMng.setDownTimeStamp(downTimeStamp);
		tgxy_TripleAgreementVerMng.setTemplateContentStyle(templateContentStyle);
		tgxy_TripleAgreementVerMngDao.save(tgxy_TripleAgreementVerMng);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
