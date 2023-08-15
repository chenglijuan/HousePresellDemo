package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Empj_BldEscrowCompleted_DtlForm;
import zhishusz.housepresell.database.dao.Empj_BldEscrowCompleted_DtlDao;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted_Dtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted;
import zhishusz.housepresell.database.dao.Empj_BldEscrowCompletedDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;

/*
 * Service更新操作：申请表-项目托管终止（审批）-明细表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BldEscrowCompleted_DtlUpdateService
{
	@Autowired
	private Empj_BldEscrowCompleted_DtlDao empj_BldEscrowCompleted_DtlDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Empj_BldEscrowCompletedDao empj_BldEscrowCompletedDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	public Properties execute(Empj_BldEscrowCompleted_DtlForm model)
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
		String eCodeOfMainTable = model.geteCodeOfMainTable();
		Long mainTableId = model.getMainTableId();
		Long developCompanyId = model.getDevelopCompanyId();
		String eCodeOfDevelopCompany = model.geteCodeOfDevelopCompany();
		Long projectId = model.getProjectId();
		String theNameOfProject = model.getTheNameOfProject();
		String eCodeOfProject = model.geteCodeOfProject();
		Long buildingId = model.getBuildingId();
		String eCodeOfBuilding = model.geteCodeOfBuilding();
		String eCodeFromPublicSecurity = model.geteCodeFromPublicSecurity();
		String eCodeFromConstruction = model.geteCodeFromConstruction();
		
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
		if(eCodeOfMainTable == null || eCodeOfMainTable.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfMainTable'不能为空");
		}
		if(mainTableId == null || mainTableId < 1)
		{
			return MyBackInfo.fail(properties, "'mainTable'不能为空");
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
		if(eCodeOfProject == null || eCodeOfProject.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfProject'不能为空");
		}
		if(buildingId == null || buildingId < 1)
		{
			return MyBackInfo.fail(properties, "'building'不能为空");
		}
		if(eCodeOfBuilding == null || eCodeOfBuilding.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfBuilding'不能为空");
		}
		if(eCodeFromPublicSecurity == null || eCodeFromPublicSecurity.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeFromPublicSecurity'不能为空");
		}
		if(eCodeFromConstruction == null || eCodeFromConstruction.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeFromConstruction'不能为空");
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
		Sm_User userUpdate = (Sm_User)sm_UserDao.findById(userUpdateId);
		if(userUpdate == null)
		{
			return MyBackInfo.fail(properties, "'userUpdate(Id:" + userUpdate + ")'不存在");
		}
		Empj_BldEscrowCompleted mainTable = (Empj_BldEscrowCompleted)empj_BldEscrowCompletedDao.findById(mainTableId);
		if(mainTable == null)
		{
			return MyBackInfo.fail(properties, "'mainTable(Id:" + mainTableId + ")'不存在");
		}
		Emmp_CompanyInfo developCompany = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(developCompanyId);
		if(developCompany == null)
		{
			return MyBackInfo.fail(properties, "'developCompany(Id:" + developCompanyId + ")'不存在");
		}
		Empj_ProjectInfo project = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(projectId);
		if(project == null)
		{
			return MyBackInfo.fail(properties, "'project(Id:" + projectId + ")'不存在");
		}
		Empj_BuildingInfo building = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(buildingId);
		if(building == null)
		{
			return MyBackInfo.fail(properties, "'building(Id:" + buildingId + ")'不存在");
		}
	
		Long empj_BldEscrowCompleted_DtlId = model.getTableId();
		Empj_BldEscrowCompleted_Dtl empj_BldEscrowCompleted_Dtl = (Empj_BldEscrowCompleted_Dtl)empj_BldEscrowCompleted_DtlDao.findById(empj_BldEscrowCompleted_DtlId);
		if(empj_BldEscrowCompleted_Dtl == null)
		{
			return MyBackInfo.fail(properties, "'Empj_BldEscrowCompleted_Dtl(Id:" + empj_BldEscrowCompleted_DtlId + ")'不存在");
		}
		
		empj_BldEscrowCompleted_Dtl.setTheState(theState);
		empj_BldEscrowCompleted_Dtl.setBusiState(busiState);
		empj_BldEscrowCompleted_Dtl.seteCode(eCode);
		empj_BldEscrowCompleted_Dtl.setUserStart(userStart);
		empj_BldEscrowCompleted_Dtl.setCreateTimeStamp(createTimeStamp);
		empj_BldEscrowCompleted_Dtl.setUserUpdate(userUpdate);
		empj_BldEscrowCompleted_Dtl.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		empj_BldEscrowCompleted_Dtl.setUserRecord(userRecord);
		empj_BldEscrowCompleted_Dtl.setRecordTimeStamp(recordTimeStamp);
		empj_BldEscrowCompleted_Dtl.seteCodeOfMainTable(eCodeOfMainTable);
		empj_BldEscrowCompleted_Dtl.setMainTable(mainTable);
		empj_BldEscrowCompleted_Dtl.setDevelopCompany(developCompany);
		empj_BldEscrowCompleted_Dtl.seteCodeOfDevelopCompany(eCodeOfDevelopCompany);
		empj_BldEscrowCompleted_Dtl.setProject(project);
		empj_BldEscrowCompleted_Dtl.setTheNameOfProject(theNameOfProject);
		empj_BldEscrowCompleted_Dtl.seteCodeOfProject(eCodeOfProject);
		empj_BldEscrowCompleted_Dtl.setBuilding(building);
		empj_BldEscrowCompleted_Dtl.seteCodeOfBuilding(eCodeOfBuilding);
		empj_BldEscrowCompleted_Dtl.seteCodeFromPublicSecurity(eCodeFromPublicSecurity);
		empj_BldEscrowCompleted_Dtl.seteCodeFromConstruction(eCodeFromConstruction);
	
		empj_BldEscrowCompleted_DtlDao.save(empj_BldEscrowCompleted_Dtl);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
