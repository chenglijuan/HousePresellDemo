package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Emmp_DepartmentForm;
import zhishusz.housepresell.database.dao.Emmp_DepartmentDao;
import zhishusz.housepresell.database.po.Emmp_Department;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;

/*
 * Service更新操作：部门
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_DepartmentUpdateService
{
	@Autowired
	private Emmp_DepartmentDao emmp_DepartmentDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	
	public Properties execute(Emmp_DepartmentForm model)
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
		Long developCompanyId = model.getDevelopCompanyId();
		String eCodeOfDevelopCompany = model.geteCodeOfDevelopCompany();
		String theName = model.getTheName();
		
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
		if(developCompanyId == null || developCompanyId < 1)
		{
			return MyBackInfo.fail(properties, "'developCompany'不能为空");
		}
		if(eCodeOfDevelopCompany == null || eCodeOfDevelopCompany.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfDevelopCompany'不能为空");
		}
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theName'不能为空");
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
		Emmp_CompanyInfo developCompany = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(developCompanyId);
		if(developCompany == null)
		{
			return MyBackInfo.fail(properties, "'developCompany(Id:" + developCompanyId + ")'不存在");
		}
	
		Long emmp_DepartmentId = model.getTableId();
		Emmp_Department emmp_Department = (Emmp_Department)emmp_DepartmentDao.findById(emmp_DepartmentId);
		if(emmp_Department == null)
		{
			return MyBackInfo.fail(properties, "'Emmp_Department(Id:" + emmp_DepartmentId + ")'不存在");
		}
		
		emmp_Department.setTheState(theState);
		emmp_Department.setBusiState(busiState);
		emmp_Department.seteCode(eCode);
		emmp_Department.setUserStart(userStart);
		emmp_Department.setCreateTimeStamp(createTimeStamp);
		emmp_Department.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		emmp_Department.setUserRecord(userRecord);
		emmp_Department.setRecordTimeStamp(recordTimeStamp);
		emmp_Department.setDevelopCompany(developCompany);
		emmp_Department.seteCodeOfDevelopCompany(eCodeOfDevelopCompany);
		emmp_Department.setTheName(theName);
	
		emmp_DepartmentDao.save(emmp_Department);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
