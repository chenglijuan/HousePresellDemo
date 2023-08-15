package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleDtlForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDtlDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettle;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettleDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：三方协议结算-子表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_CoopAgreementSettleDtlAddService
{
	@Autowired
	private Tgxy_CoopAgreementSettleDtlDao tgxy_CoopAgreementSettleDtlDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Tgxy_CoopAgreementSettleDao tgxy_CoopAgreementSettleDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	
	public Properties execute(Tgxy_CoopAgreementSettleDtlForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = model.getTheState();
		String busiState = model.getBusiState();
		Long userStartId = model.getUserStartId();
		Long createTimeStamp = model.getCreateTimeStamp();
		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
		Long userRecordId = model.getUserRecordId();
		Long recordTimeStamp = model.getRecordTimeStamp();
		Long mainTableId = model.getMainTableId();
		String eCode = model.geteCode();
		String agreementDate = model.getAgreementDate();
		String seller = model.getSeller();
		String buyer = model.getBuyer();
		Long projectId = model.getProjectId();
		String theNameOfProject = model.getTheNameOfProject();
		Long buildingInfoId = model.getBuildingInfoId();
		String eCodeOfBuilding = model.geteCodeOfBuilding();
		String eCodeFromConstruction = model.geteCodeFromConstruction();
		Long unitInfoId = model.getUnitInfoId();
		Long houseInfoId = model.getHouseInfoId();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(busiState == null || busiState.length()< 1)
		{
			return MyBackInfo.fail(properties, "'busiState'不能为空");
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
		if(mainTableId == null || mainTableId < 1)
		{
			return MyBackInfo.fail(properties, "'mainTable'不能为空");
		}
		if(eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCode'不能为空");
		}
		if(agreementDate == null || agreementDate.length() == 0)
		{
			return MyBackInfo.fail(properties, "'agreementDate'不能为空");
		}
		if(seller == null || seller.length() == 0)
		{
			return MyBackInfo.fail(properties, "'seller'不能为空");
		}
		if(buyer == null || buyer.length() == 0)
		{
			return MyBackInfo.fail(properties, "'buyer'不能为空");
		}
		if(projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "'project'不能为空");
		}
		if(theNameOfProject == null || theNameOfProject.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfProject'不能为空");
		}
		if(buildingInfoId == null || buildingInfoId < 1)
		{
			return MyBackInfo.fail(properties, "'buildingInfo'不能为空");
		}
		if(eCodeOfBuilding == null || eCodeOfBuilding.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfBuilding'不能为空");
		}
		if(eCodeFromConstruction == null || eCodeFromConstruction.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeFromConstruction'不能为空");
		}
		if(unitInfoId == null || unitInfoId < 1)
		{
			return MyBackInfo.fail(properties, "'unitInfo'不能为空");
		}
		if(houseInfoId == null || houseInfoId < 1)
		{
			return MyBackInfo.fail(properties, "'houseInfo'不能为空");
		}

		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		Tgxy_CoopAgreementSettle mainTable = (Tgxy_CoopAgreementSettle)tgxy_CoopAgreementSettleDao.findById(mainTableId);
		Empj_ProjectInfo project = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(projectId);
		Empj_BuildingInfo buildingInfo = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(buildingInfoId);
		Empj_UnitInfo unitInfo = (Empj_UnitInfo)empj_UnitInfoDao.findById(unitInfoId);
		Empj_HouseInfo houseInfo = (Empj_HouseInfo)empj_HouseInfoDao.findById(houseInfoId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if(mainTable == null)
		{
			return MyBackInfo.fail(properties, "'mainTable'不能为空");
		}
		if(project == null)
		{
			return MyBackInfo.fail(properties, "'project'不能为空");
		}
		if(buildingInfo == null)
		{
			return MyBackInfo.fail(properties, "'buildingInfo'不能为空");
		}
		if(unitInfo == null)
		{
			return MyBackInfo.fail(properties, "'unitInfo'不能为空");
		}
		if(houseInfo == null)
		{
			return MyBackInfo.fail(properties, "'houseInfo'不能为空");
		}
	
		Tgxy_CoopAgreementSettleDtl tgxy_CoopAgreementSettleDtl = new Tgxy_CoopAgreementSettleDtl();
		tgxy_CoopAgreementSettleDtl.setTheState(theState);
		tgxy_CoopAgreementSettleDtl.setBusiState(busiState);
		tgxy_CoopAgreementSettleDtl.setUserStart(userStart);
		tgxy_CoopAgreementSettleDtl.setCreateTimeStamp(createTimeStamp);
		tgxy_CoopAgreementSettleDtl.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgxy_CoopAgreementSettleDtl.setUserRecord(userRecord);
		tgxy_CoopAgreementSettleDtl.setRecordTimeStamp(recordTimeStamp);
		tgxy_CoopAgreementSettleDtl.setMainTable(mainTable);
		tgxy_CoopAgreementSettleDtl.seteCode(eCode);
		tgxy_CoopAgreementSettleDtl.setAgreementDate(agreementDate);
		tgxy_CoopAgreementSettleDtl.setSeller(seller);
		tgxy_CoopAgreementSettleDtl.setBuyer(buyer);
		tgxy_CoopAgreementSettleDtl.setProject(project);
		tgxy_CoopAgreementSettleDtl.setTheNameOfProject(theNameOfProject);
		tgxy_CoopAgreementSettleDtl.setBuildingInfo(buildingInfo);
		tgxy_CoopAgreementSettleDtl.seteCodeOfBuilding(eCodeOfBuilding);
		tgxy_CoopAgreementSettleDtl.seteCodeFromConstruction(eCodeFromConstruction);
		tgxy_CoopAgreementSettleDtl.setUnitInfo(unitInfo);
		tgxy_CoopAgreementSettleDtl.setHouseInfo(houseInfo);
		tgxy_CoopAgreementSettleDtlDao.save(tgxy_CoopAgreementSettleDtl);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
