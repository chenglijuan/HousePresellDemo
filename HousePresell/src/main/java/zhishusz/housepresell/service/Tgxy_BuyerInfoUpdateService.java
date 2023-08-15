package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgxy_BuyerInfoForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_BuyerInfoDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_BuyerInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：买受人信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_BuyerInfoUpdateService
{
	@Autowired
	private Tgxy_BuyerInfoDao tgxy_BuyerInfoDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Tgxy_BuyerInfoForm model)
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
		String buyerName = model.getBuyerName();
		String buyerType = model.getBuyerType();
		String certificateType = model.getCertificateType();
		String eCodeOfcertificate = model.geteCodeOfcertificate();
		String contactPhone = model.getContactPhone();
		String contactAdress = model.getContactAdress();
		String agentName = model.getAgentName();
		String agentCertType = model.getAgentCertType();
		String agentCertNumber = model.getAgentCertNumber();
		String agentPhone = model.getAgentPhone();
		String agentAddress = model.getAgentAddress();
		String eCodeOfContract = model.geteCodeOfContract();
		
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
		if(buyerName == null || buyerName.length() == 0)
		{
			return MyBackInfo.fail(properties, "'buyerName'不能为空");
		}
		if(buyerType == null || buyerType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'buyerType'不能为空");
		}
		if(certificateType == null || certificateType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'certificateType'不能为空");
		}
		if(eCodeOfcertificate == null || eCodeOfcertificate.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfcertificate'不能为空");
		}
		if(contactPhone == null || contactPhone.length() == 0)
		{
			return MyBackInfo.fail(properties, "'contactPhone'不能为空");
		}
		if(contactAdress == null || contactAdress.length() == 0)
		{
			return MyBackInfo.fail(properties, "'contactAdress'不能为空");
		}
		if(agentName == null || agentName.length() == 0)
		{
			return MyBackInfo.fail(properties, "'agentName'不能为空");
		}
		if(agentCertType == null || agentCertType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'agentCertType'不能为空");
		}
		if(agentCertNumber == null || agentCertNumber.length() == 0)
		{
			return MyBackInfo.fail(properties, "'agentCertNumber'不能为空");
		}
		if(agentPhone == null || agentPhone.length() == 0)
		{
			return MyBackInfo.fail(properties, "'agentPhone'不能为空");
		}
		if(agentAddress == null || agentAddress.length() == 0)
		{
			return MyBackInfo.fail(properties, "'agentAddress'不能为空");
		}
		if(eCodeOfContract == null || eCodeOfContract.length() == 0)
		{
			return MyBackInfo.fail(properties, "'contractno'不能为空");
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
	
		Long tgxy_BuyerInfoId = model.getTableId();
		Tgxy_BuyerInfo tgxy_BuyerInfo = (Tgxy_BuyerInfo)tgxy_BuyerInfoDao.findById(tgxy_BuyerInfoId);
		if(tgxy_BuyerInfo == null)
		{
			return MyBackInfo.fail(properties, "'Tgxy_BuyerInfo(Id:" + tgxy_BuyerInfoId + ")'不存在");
		}
		
		tgxy_BuyerInfo.setTheState(theState);
		tgxy_BuyerInfo.setBusiState(busiState);
		tgxy_BuyerInfo.seteCode(eCode);
		tgxy_BuyerInfo.setUserStart(userStart);
		tgxy_BuyerInfo.setCreateTimeStamp(createTimeStamp);
		tgxy_BuyerInfo.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgxy_BuyerInfo.setUserRecord(userRecord);
		tgxy_BuyerInfo.setRecordTimeStamp(recordTimeStamp);
		tgxy_BuyerInfo.setBuyerName(buyerName);
		tgxy_BuyerInfo.setBuyerType(buyerType);
		tgxy_BuyerInfo.setCertificateType(certificateType);
		tgxy_BuyerInfo.seteCodeOfcertificate(eCodeOfcertificate);
		tgxy_BuyerInfo.setContactPhone(contactPhone);
		tgxy_BuyerInfo.setContactAdress(contactAdress);
		tgxy_BuyerInfo.setAgentName(agentName);
		tgxy_BuyerInfo.setAgentCertType(agentCertType);
		tgxy_BuyerInfo.setAgentCertNumber(agentCertNumber);
		tgxy_BuyerInfo.setAgentPhone(agentPhone);
		tgxy_BuyerInfo.setAgentAddress(agentAddress);
		tgxy_BuyerInfo.seteCodeOfContract(eCodeOfContract);
	
		tgxy_BuyerInfoDao.save(tgxy_BuyerInfo);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
