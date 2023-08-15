package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_PresellDocumentInfoForm;
import zhishusz.housepresell.database.dao.Empj_PresellDocumentInfoDao;
import zhishusz.housepresell.database.po.Empj_PresellDocumentInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import java.util.Set;
	
/*
 * Service添加操作：预售证信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_PresellDocumentInfoAddService
{
	@Autowired
	private Empj_PresellDocumentInfoDao empj_PresellDocumentInfoDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	
	public Properties execute(Empj_PresellDocumentInfoForm model)
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
		Long developCompanyId = model.getDevelopCompanyId();
		String eCodeOfDevelopCompany = model.geteCodeOfDevelopCompany();
		Long projectId = model.getProjectId();
		String theNameOfProject = model.getTheNameOfProject();
		String addressOfProject = model.getAddressOfProject();
		Set buildingInfoSet = model.getBuildingInfoSet();
		
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
		if(projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "'project'不能为空");
		}
		if(theNameOfProject == null || theNameOfProject.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfProject'不能为空");
		}
		if(addressOfProject == null || addressOfProject.length() == 0)
		{
			return MyBackInfo.fail(properties, "'addressOfProject'不能为空");
		}
		if(buildingInfoSet == null || buildingInfoSet.size() < 1)
		{
			return MyBackInfo.fail(properties, "'buildingInfoSet'不能为空");
		}

		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		Sm_User userUpdate = (Sm_User)sm_UserDao.findById(userUpdateId);
		Emmp_CompanyInfo developCompany = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(developCompanyId);
		Empj_ProjectInfo project = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(projectId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if(userUpdate == null)
		{
			return MyBackInfo.fail(properties, "'userUpdate(Id:" + userUpdate + ")'不存在");
		}
		if(developCompany == null)
		{
			return MyBackInfo.fail(properties, "'developCompany'不能为空");
		}
		if(project == null)
		{
			return MyBackInfo.fail(properties, "'project'不能为空");
		}
	
		Empj_PresellDocumentInfo empj_PresellDocumentInfo = new Empj_PresellDocumentInfo();
		empj_PresellDocumentInfo.setTheState(theState);
		empj_PresellDocumentInfo.setBusiState(busiState);
		empj_PresellDocumentInfo.seteCode(eCode);
		empj_PresellDocumentInfo.setUserStart(userStart);
		empj_PresellDocumentInfo.setCreateTimeStamp(createTimeStamp);
		empj_PresellDocumentInfo.setUserUpdate(userUpdate);
		empj_PresellDocumentInfo.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		empj_PresellDocumentInfo.setUserRecord(userRecord);
		empj_PresellDocumentInfo.setRecordTimeStamp(recordTimeStamp);
		empj_PresellDocumentInfo.setDevelopCompany(developCompany);
		empj_PresellDocumentInfo.seteCodeOfDevelopCompany(eCodeOfDevelopCompany);
		empj_PresellDocumentInfo.setProject(project);
		empj_PresellDocumentInfo.setTheNameOfProject(theNameOfProject);
		empj_PresellDocumentInfo.setAddressOfProject(addressOfProject);
		empj_PresellDocumentInfo.setBuildingInfoSet(buildingInfoSet);
		empj_PresellDocumentInfoDao.save(empj_PresellDocumentInfo);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
