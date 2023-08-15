package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Tgpf_RemainRightForm;
import zhishusz.housepresell.database.dao.Tgpf_RemainRightDao;
import zhishusz.housepresell.database.po.Tgpf_RemainRight;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;

/*
 * Service更新操作：留存权益
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_RemainRightUpdateService
{
	@Autowired
	private Tgpf_RemainRightDao tgpf_RemainRightDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;
	@Autowired
	private Emmp_BankInfoDao emmp_BankInfoDao;
	
	public Properties execute(Tgpf_RemainRightForm model)
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
		Long enterTimeStamp = model.getEnterTimeStamp();
		String buyer = model.getBuyer();
		String theNameOfCreditor = model.getTheNameOfCreditor();
		String idNumberOfCreditor = model.getIdNumberOfCreditor();
		String eCodeOfContractRecord = model.geteCodeOfContractRecord();
		String eCodeOfTripleAgreement = model.geteCodeOfTripleAgreement();
		String srcBusiType = model.getSrcBusiType();
		Long projectId = model.getProjectId();
		String theNameOfProject = model.getTheNameOfProject();
		Long buildingId = model.getBuildingId();
		String eCodeOfBuilding = model.geteCodeOfBuilding();
		Long buildingUnitId = model.getBuildingUnitId();
		String eCodeOfBuildingUnit = model.geteCodeOfBuildingUnit();
		String eCodeFromRoom = model.geteCodeFromRoom();
		Double actualDepositAmount = model.getActualDepositAmount();
		Double depositAmountFromLoan = model.getDepositAmountFromLoan();
		String theAccountFromLoan = model.getTheAccountFromLoan();
		Integer fundProperty = model.getFundProperty();
		Long bankId = model.getBankId();
		String theNameOfBankPayedIn = model.getTheNameOfBankPayedIn();
		Double theRatio = model.getTheRatio();
		Double theAmount = model.getTheAmount();
		Double limitedRetainRight = model.getLimitedRetainRight();
		Double withdrawableRetainRight = model.getWithdrawableRetainRight();
		Double currentDividedAmout = model.getCurrentDividedAmout();
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
		if(enterTimeStamp == null || enterTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'enterTimeStamp'不能为空");
		}
		if(buyer == null || buyer.length() == 0)
		{
			return MyBackInfo.fail(properties, "'buyer'不能为空");
		}
		if(theNameOfCreditor == null || theNameOfCreditor.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfCreditor'不能为空");
		}
		if(idNumberOfCreditor == null || idNumberOfCreditor.length() == 0)
		{
			return MyBackInfo.fail(properties, "'idNumberOfCreditor'不能为空");
		}
		if(eCodeOfContractRecord == null || eCodeOfContractRecord.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfContractRecord'不能为空");
		}
		if(eCodeOfTripleAgreement == null || eCodeOfTripleAgreement.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfTripleAgreement'不能为空");
		}
		if(srcBusiType == null || srcBusiType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'srcBusiType'不能为空");
		}
		if(projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "'project'不能为空");
		}
		if(theNameOfProject == null || theNameOfProject.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfProject'不能为空");
		}
		if(buildingId == null || buildingId < 1)
		{
			return MyBackInfo.fail(properties, "'building'不能为空");
		}
		if(eCodeOfBuilding == null || eCodeOfBuilding.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfBuilding'不能为空");
		}
		if(buildingUnitId == null || buildingUnitId < 1)
		{
			return MyBackInfo.fail(properties, "'buildingUnit'不能为空");
		}
		if(eCodeOfBuildingUnit == null || eCodeOfBuildingUnit.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfBuildingUnit'不能为空");
		}
		if(eCodeFromRoom == null || eCodeFromRoom.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeFromRoom'不能为空");
		}
		if(actualDepositAmount == null || actualDepositAmount < 1)
		{
			return MyBackInfo.fail(properties, "'actualDepositAmount'不能为空");
		}
		if(depositAmountFromLoan == null || depositAmountFromLoan < 1)
		{
			return MyBackInfo.fail(properties, "'depositAmountFromLoan'不能为空");
		}
		if(theAccountFromLoan == null || theAccountFromLoan.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theAccountFromLoan'不能为空");
		}
		if(fundProperty == null || fundProperty < 1)
		{
			return MyBackInfo.fail(properties, "'fundProperty'不能为空");
		}
		if(bankId == null || bankId < 1)
		{
			return MyBackInfo.fail(properties, "'bank'不能为空");
		}
		if(theNameOfBankPayedIn == null || theNameOfBankPayedIn.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfBankPayedIn'不能为空");
		}
		if(theRatio == null || theRatio < 1)
		{
			return MyBackInfo.fail(properties, "'theRatio'不能为空");
		}
		if(theAmount == null || theAmount < 1)
		{
			return MyBackInfo.fail(properties, "'theAmount'不能为空");
		}
		if(limitedRetainRight == null || limitedRetainRight < 1)
		{
			return MyBackInfo.fail(properties, "'limitedRetainRight'不能为空");
		}
		if(withdrawableRetainRight == null || withdrawableRetainRight < 1)
		{
			return MyBackInfo.fail(properties, "'withdrawableRetainRight'不能为空");
		}
		if(currentDividedAmout == null || currentDividedAmout < 1)
		{
			return MyBackInfo.fail(properties, "'currentDividedAmout'不能为空");
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
		Empj_UnitInfo buildingUnit = (Empj_UnitInfo)empj_UnitInfoDao.findById(buildingUnitId);
		if(buildingUnit == null)
		{
			return MyBackInfo.fail(properties, "'buildingUnit(Id:" + buildingUnitId + ")'不存在");
		}
		Emmp_BankInfo bank = (Emmp_BankInfo)emmp_BankInfoDao.findById(bankId);
		if(bank == null)
		{
			return MyBackInfo.fail(properties, "'bank(Id:" + bankId + ")'不存在");
		}
	
		Long tgpf_RemainRightId = model.getTableId();
		Tgpf_RemainRight tgpf_RemainRight = (Tgpf_RemainRight)tgpf_RemainRightDao.findById(tgpf_RemainRightId);
		if(tgpf_RemainRight == null)
		{
			return MyBackInfo.fail(properties, "'Tgpf_RemainRight(Id:" + tgpf_RemainRightId + ")'不存在");
		}
		
		tgpf_RemainRight.setTheState(theState);
		tgpf_RemainRight.setBusiState(busiState);
		tgpf_RemainRight.seteCode(eCode);
		tgpf_RemainRight.setUserStart(userStart);
		tgpf_RemainRight.setCreateTimeStamp(createTimeStamp);
		tgpf_RemainRight.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgpf_RemainRight.setUserRecord(userRecord);
		tgpf_RemainRight.setRecordTimeStamp(recordTimeStamp);
		tgpf_RemainRight.setEnterTimeStamp(enterTimeStamp);
		tgpf_RemainRight.setBuyer(buyer);
		tgpf_RemainRight.setTheNameOfCreditor(theNameOfCreditor);
		tgpf_RemainRight.setIdNumberOfCreditor(idNumberOfCreditor);
		tgpf_RemainRight.seteCodeOfContractRecord(eCodeOfContractRecord);
		tgpf_RemainRight.seteCodeOfTripleAgreement(eCodeOfTripleAgreement);
		tgpf_RemainRight.setSrcBusiType(srcBusiType);
		tgpf_RemainRight.setProject(project);
		tgpf_RemainRight.setTheNameOfProject(theNameOfProject);
		tgpf_RemainRight.setBuilding(building);
		tgpf_RemainRight.seteCodeOfBuilding(eCodeOfBuilding);
		tgpf_RemainRight.setBuildingUnit(buildingUnit);
		tgpf_RemainRight.seteCodeOfBuildingUnit(eCodeOfBuildingUnit);
		tgpf_RemainRight.seteCodeFromRoom(eCodeFromRoom);
		tgpf_RemainRight.setActualDepositAmount(actualDepositAmount);
		tgpf_RemainRight.setDepositAmountFromLoan(depositAmountFromLoan);
		tgpf_RemainRight.setTheAccountFromLoan(theAccountFromLoan);
		tgpf_RemainRight.setFundProperty(fundProperty);
		tgpf_RemainRight.setBank(bank);
		tgpf_RemainRight.setTheNameOfBankPayedIn(theNameOfBankPayedIn);
		tgpf_RemainRight.setTheRatio(theRatio);
		tgpf_RemainRight.setTheAmount(theAmount);
		tgpf_RemainRight.setLimitedRetainRight(limitedRetainRight);
		tgpf_RemainRight.setWithdrawableRetainRight(withdrawableRetainRight);
		tgpf_RemainRight.setCurrentDividedAmout(currentDividedAmout);
		tgpf_RemainRight.setRemark(remark);
	
		tgpf_RemainRightDao.save(tgpf_RemainRight);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
