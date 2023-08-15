package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service更新操作：机构信息备案
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_CompanyInfoRecordService
{
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Emmp_CompanyInfoForm model)
	{
		Properties properties = new MyProperties();

		String busiState = model.getBusiState();
		Long userRecordId = model.getUserRecordId();
		Long recordTimeStamp = model.getRecordTimeStamp();
		Integer recordState = model.getRecordState();
		String recordRejectReason = model.getRecordRejectReason();
		
		if(userRecordId == null || userRecordId < 0) 
		{ 
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		
		Emmp_CompanyInfo emmp_CompanyInfo = new Emmp_CompanyInfo();
		
		emmp_CompanyInfo.setBusiState(busiState);
		emmp_CompanyInfo.setUserRecord(userRecord);
		emmp_CompanyInfo.setRecordTimeStamp(recordTimeStamp);
		emmp_CompanyInfo.setRecordState(recordState);
		emmp_CompanyInfo.setRecordRejectReason(recordRejectReason);
		
		emmp_CompanyInfoDao.save(emmp_CompanyInfo);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
