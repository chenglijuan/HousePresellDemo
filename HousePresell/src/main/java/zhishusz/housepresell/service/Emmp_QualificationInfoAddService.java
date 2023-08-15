package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Emmp_QualificationInfoForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Emmp_QualificationInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Emmp_QualificationInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：资质认证信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_QualificationInfoAddService
{
	@Autowired
	private Emmp_QualificationInfoDao emmp_QualificationInfoDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	
	public Properties execute(Emmp_QualificationInfoForm model)
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
		Long companyId = model.getCompanyId();
		String theType = model.getTheType();
		Integer theLevel = model.getTheLevel();
		String issuanceDate = model.getIssuanceDate();
		String expiryDate = model.getExpiryDate();
		
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
		if(companyId == null || companyId < 1)
		{
			return MyBackInfo.fail(properties, "'company'不能为空");
		}
		if(theType == null || theType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theType'不能为空");
		}
		if(theLevel == null || theLevel < 1)
		{
			return MyBackInfo.fail(properties, "'theLevel'不能为空");
		}
		if(issuanceDate == null || issuanceDate.length() == 0)
		{
			return MyBackInfo.fail(properties, "'issuanceDate'不能为空");
		}
		if(expiryDate == null || expiryDate.length() == 0)
		{
			return MyBackInfo.fail(properties, "'expiryDate'不能为空");
		}

		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		Emmp_CompanyInfo company = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(companyId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if(company == null)
		{
			return MyBackInfo.fail(properties, "'company'不能为空");
		}
	
		Emmp_QualificationInfo emmp_QualificationInfo = new Emmp_QualificationInfo();
		emmp_QualificationInfo.setTheState(theState);
		emmp_QualificationInfo.setBusiState(busiState);
		emmp_QualificationInfo.seteCode(eCode);
		emmp_QualificationInfo.setUserStart(userStart);
		emmp_QualificationInfo.setCreateTimeStamp(createTimeStamp);
		emmp_QualificationInfo.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		emmp_QualificationInfo.setUserRecord(userRecord);
		emmp_QualificationInfo.setRecordTimeStamp(recordTimeStamp);
		emmp_QualificationInfo.setCompany(company);
		emmp_QualificationInfo.setTheType(theType);
		emmp_QualificationInfo.setTheLevel(theLevel);
		emmp_QualificationInfo.setIssuanceDate(issuanceDate);
		emmp_QualificationInfo.setExpiryDate(expiryDate);
		emmp_QualificationInfoDao.save(emmp_QualificationInfo);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
