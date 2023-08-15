package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Empj_BuildingInfo_AFForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfo_AFDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo_AF;
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
 * Service更新操作：申请表-楼幢变更
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BuildingInfo_AFUpdateService
{
	@Autowired
	private Empj_BuildingInfo_AFDao empj_BuildingInfo_AFDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	public Properties execute(Empj_BuildingInfo_AFForm model)
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
		String eCodeOfProject = model.geteCodeOfProject();
		Long buildingId = model.getBuildingId();
		String eCodeOfBuilding = model.geteCodeOfBuilding();
		Double buildingArea = model.getBuildingArea();
		Double escrowArea = model.getEscrowArea();
		String deliveryType = model.getDeliveryType();
		Double upfloorNumber = model.getUpfloorNumber();
		Double downfloorNumber = model.getDownfloorNumber();
		Integer landMortgageState = model.getLandMortgageState();
		String landMortgagor = model.getLandMortgagor();
		Double landMortgageAmount = model.getLandMortgageAmount();
		String remark = model.getRemark();
		
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
		if(buildingArea == null || buildingArea < 1)
		{
			return MyBackInfo.fail(properties, "'buildingArea'不能为空");
		}
		if(escrowArea == null || escrowArea < 1)
		{
			return MyBackInfo.fail(properties, "'escrowArea'不能为空");
		}
		if(deliveryType == null || deliveryType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'deliveryType'不能为空");
		}
		if(upfloorNumber == null || upfloorNumber < 1)
		{
			return MyBackInfo.fail(properties, "'upfloorNumber'不能为空");
		}
		if(downfloorNumber == null || downfloorNumber < 1)
		{
			return MyBackInfo.fail(properties, "'downfloorNumber'不能为空");
		}
		if(landMortgageState == null || landMortgageState < 1)
		{
			return MyBackInfo.fail(properties, "'landMortgageState'不能为空");
		}
		if(landMortgagor == null || landMortgagor.length() == 0)
		{
			return MyBackInfo.fail(properties, "'landMortgagor'不能为空");
		}
		if(landMortgageAmount == null || landMortgageAmount < 1)
		{
			return MyBackInfo.fail(properties, "'landMortgageAmount'不能为空");
		}
		if(remark == null || remark.length() == 0)
		{
			return MyBackInfo.fail(properties, "'remark'不能为空");
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
	
		Long empj_BuildingInfo_AFId = model.getTableId();
		Empj_BuildingInfo_AF empj_BuildingInfo_AF = (Empj_BuildingInfo_AF)empj_BuildingInfo_AFDao.findById(empj_BuildingInfo_AFId);
		if(empj_BuildingInfo_AF == null)
		{
			return MyBackInfo.fail(properties, "'Empj_BuildingInfo_AF(Id:" + empj_BuildingInfo_AFId + ")'不存在");
		}
		
		empj_BuildingInfo_AF.setTheState(theState);
		empj_BuildingInfo_AF.setBusiState(busiState);
		empj_BuildingInfo_AF.seteCode(eCode);
		empj_BuildingInfo_AF.setUserStart(userStart);
		empj_BuildingInfo_AF.setCreateTimeStamp(createTimeStamp);
		empj_BuildingInfo_AF.setUserUpdate(userUpdate);
		empj_BuildingInfo_AF.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		empj_BuildingInfo_AF.setUserRecord(userRecord);
		empj_BuildingInfo_AF.setRecordTimeStamp(recordTimeStamp);
		empj_BuildingInfo_AF.setDevelopCompany(developCompany);
		empj_BuildingInfo_AF.seteCodeOfDevelopCompany(eCodeOfDevelopCompany);
		empj_BuildingInfo_AF.setProject(project);
		empj_BuildingInfo_AF.setTheNameOfProject(theNameOfProject);
		empj_BuildingInfo_AF.seteCodeOfProject(eCodeOfProject);
		empj_BuildingInfo_AF.setBuilding(building);
		empj_BuildingInfo_AF.seteCodeOfBuilding(eCodeOfBuilding);
		empj_BuildingInfo_AF.setBuildingArea(buildingArea);
		empj_BuildingInfo_AF.setEscrowArea(escrowArea);
		empj_BuildingInfo_AF.setDeliveryType(deliveryType);
		empj_BuildingInfo_AF.setUpfloorNumber(upfloorNumber);
		empj_BuildingInfo_AF.setDownfloorNumber(downfloorNumber);
		empj_BuildingInfo_AF.setLandMortgageState(landMortgageState);
		empj_BuildingInfo_AF.setLandMortgagor(landMortgagor);
		empj_BuildingInfo_AF.setLandMortgageAmount(landMortgageAmount);
		empj_BuildingInfo_AF.setRemark(remark);
	
		empj_BuildingInfo_AFDao.save(empj_BuildingInfo_AF);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
