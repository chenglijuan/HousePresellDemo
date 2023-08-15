package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_FundAccountInfoForm;
import zhishusz.housepresell.database.dao.Tgpf_FundAccountInfoDao;
import zhishusz.housepresell.database.po.Tgpf_FundAccountInfo;
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
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
	
/*
 * Service添加操作：推送给财务系统-设置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundAccountInfoAddService
{
	@Autowired
	private Tgpf_FundAccountInfoDao tgpf_FundAccountInfoDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	public Properties execute(Tgpf_FundAccountInfoForm model)
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
		Long companyInfoId = model.getCompanyInfoId();
		String theNameOfCompany = model.getTheNameOfCompany();
		String eCodeOfCompany = model.geteCodeOfCompany();
		String fullNameOfCompanyFromFinanceSystem = model.getFullNameOfCompanyFromFinanceSystem();
		String shortNameOfCompanyFromFinanceSystem = model.getShortNameOfCompanyFromFinanceSystem();
		Long projectId = model.getProjectId();
		String theNameOfProject = model.getTheNameOfProject();
		String eCodeOfProject = model.geteCodeOfProject();
		String fullNameOfProjectFromFinanceSystem = model.getFullNameOfProjectFromFinanceSystem();
		String shortNameOfProjectFromFinanceSystem = model.getShortNameOfProjectFromFinanceSystem();
		Long buildingId = model.getBuildingId();
		String eCodeFromConstruction = model.geteCodeFromConstruction();
		String eCodeOfBuilding = model.geteCodeOfBuilding();
		String fullNameOfBuildingFromFinanceSystem = model.getFullNameOfBuildingFromFinanceSystem();
		String shortNameOfBuildingFromFinanceSystem = model.getShortNameOfBuildingFromFinanceSystem();
		Integer operateType = model.getOperateType();
		
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
		if(companyInfoId == null || companyInfoId < 1)
		{
			return MyBackInfo.fail(properties, "'companyInfo'不能为空");
		}
		if(theNameOfCompany == null || theNameOfCompany.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfCompany'不能为空");
		}
		if(eCodeOfCompany == null || eCodeOfCompany.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfCompany'不能为空");
		}
		if(fullNameOfCompanyFromFinanceSystem == null || fullNameOfCompanyFromFinanceSystem.length() == 0)
		{
			return MyBackInfo.fail(properties, "'fullNameOfCompanyFromFinanceSystem'不能为空");
		}
		if(shortNameOfCompanyFromFinanceSystem == null || shortNameOfCompanyFromFinanceSystem.length() == 0)
		{
			return MyBackInfo.fail(properties, "'shortNameOfCompanyFromFinanceSystem'不能为空");
		}
		if(projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "'project'不能为空");
		}
		if(theNameOfProject == null || theNameOfProject.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfProject'不能为空");
		}
		if(eCodeOfProject == null || eCodeOfProject.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfProject'不能为空");
		}
		if(fullNameOfProjectFromFinanceSystem == null || fullNameOfProjectFromFinanceSystem.length() == 0)
		{
			return MyBackInfo.fail(properties, "'fullNameOfProjectFromFinanceSystem'不能为空");
		}
		if(shortNameOfProjectFromFinanceSystem == null || shortNameOfProjectFromFinanceSystem.length() == 0)
		{
			return MyBackInfo.fail(properties, "'shortNameOfProjectFromFinanceSystem'不能为空");
		}
		if(buildingId == null || buildingId < 1)
		{
			return MyBackInfo.fail(properties, "'building'不能为空");
		}
		if(eCodeFromConstruction == null || eCodeFromConstruction.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeFromConstruction'不能为空");
		}
		if(eCodeOfBuilding == null || eCodeOfBuilding.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfBuilding'不能为空");
		}
		if(fullNameOfBuildingFromFinanceSystem == null || fullNameOfBuildingFromFinanceSystem.length() == 0)
		{
			return MyBackInfo.fail(properties, "'fullNameOfBuildingFromFinanceSystem'不能为空");
		}
		if(shortNameOfBuildingFromFinanceSystem == null || shortNameOfBuildingFromFinanceSystem.length() == 0)
		{
			return MyBackInfo.fail(properties, "'shortNameOfBuildingFromFinanceSystem'不能为空");
		}
		if(operateType == null || operateType < 1)
		{
			return MyBackInfo.fail(properties, "'operateType'不能为空");
		}

		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		Emmp_CompanyInfo companyInfo = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(companyInfoId);
		Empj_ProjectInfo project = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(projectId);
		Empj_BuildingInfo building = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(buildingId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if(companyInfo == null)
		{
			return MyBackInfo.fail(properties, "'companyInfo'不能为空");
		}
		if(project == null)
		{
			return MyBackInfo.fail(properties, "'project'不能为空");
		}
		if(building == null)
		{
			return MyBackInfo.fail(properties, "'building'不能为空");
		}
	
		Tgpf_FundAccountInfo tgpf_FundAccountInfo = new Tgpf_FundAccountInfo();
		tgpf_FundAccountInfo.setTheState(theState);
		tgpf_FundAccountInfo.setBusiState(busiState);
		tgpf_FundAccountInfo.seteCode(eCode);
		tgpf_FundAccountInfo.setUserStart(userStart);
		tgpf_FundAccountInfo.setCreateTimeStamp(createTimeStamp);
		tgpf_FundAccountInfo.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgpf_FundAccountInfo.setUserRecord(userRecord);
		tgpf_FundAccountInfo.setRecordTimeStamp(recordTimeStamp);
		tgpf_FundAccountInfo.setCompanyInfo(companyInfo);
		tgpf_FundAccountInfo.setTheNameOfCompany(theNameOfCompany);
		tgpf_FundAccountInfo.seteCodeOfCompany(eCodeOfCompany);
		tgpf_FundAccountInfo.setFullNameOfCompanyFromFinanceSystem(fullNameOfCompanyFromFinanceSystem);
		tgpf_FundAccountInfo.setShortNameOfCompanyFromFinanceSystem(shortNameOfCompanyFromFinanceSystem);
		tgpf_FundAccountInfo.setProject(project);
		tgpf_FundAccountInfo.setTheNameOfProject(theNameOfProject);
		tgpf_FundAccountInfo.seteCodeOfProject(eCodeOfProject);
		tgpf_FundAccountInfo.setFullNameOfProjectFromFinanceSystem(fullNameOfProjectFromFinanceSystem);
		tgpf_FundAccountInfo.setShortNameOfProjectFromFinanceSystem(shortNameOfProjectFromFinanceSystem);
		tgpf_FundAccountInfo.setBuilding(building);
		tgpf_FundAccountInfo.seteCodeFromConstruction(eCodeFromConstruction);
		tgpf_FundAccountInfo.seteCodeOfBuilding(eCodeOfBuilding);
		tgpf_FundAccountInfo.setFullNameOfBuildingFromFinanceSystem(fullNameOfBuildingFromFinanceSystem);
		tgpf_FundAccountInfo.setShortNameOfBuildingFromFinanceSystem(shortNameOfBuildingFromFinanceSystem);
		tgpf_FundAccountInfo.setOperateType(operateType);
		tgpf_FundAccountInfoDao.save(tgpf_FundAccountInfo);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
