package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgxy_ContractInfoForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_ContractInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_ContractInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：预售系统买卖合同
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_ContractInfoAddService
{
	@Autowired
	private Tgxy_ContractInfoDao tgxy_ContractInfoDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;

	public Properties execute(Tgxy_ContractInfoForm model)
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
		String eCodeOfContractRecord = model.geteCodeOfContractRecord();
		Long companyId = model.getCompanyId();
		String theNameFormCompany = model.getTheNameFormCompany();
		String theNameOfProject = model.getTheNameOfProject();
		String eCodeFromConstruction = model.geteCodeFromConstruction();
		Long houseInfoId = model.getHouseInfoId();
		String eCodeOfHouseInfo = model.geteCodeOfHouseInfo();
		String roomIdOfHouseInfo = model.getRoomIdOfHouseInfo();
		Double contractSumPrice = model.getContractSumPrice();
		Double buildingArea = model.getBuildingArea();
		String position = model.getPosition();
		Long contractSignDate = model.getContractSignDate();
		String paymentMethod = model.getPaymentMethod();
		String loanBank = model.getLoanBank();
		Double firstPaymentAmount = model.getFirstPaymentAmount();
		Double loanAmount = model.getLoanAmount();
		String escrowState = model.getEscrowState();
		Long payDate = model.getPayDate();
		String eCodeOfBuilding = model.geteCodeOfBuilding();
		String eCodeFromPublicSecurity = model.geteCodeFromPublicSecurity();
		Long contractRecordDate = model.getContractRecordDate();
		String syncPerson = model.getSyncPerson();
		Long syncDate = model.getSyncDate();

		if (theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if (busiState == null || busiState.length() < 1)
		{
			return MyBackInfo.fail(properties, "'busiState'不能为空");
		}
		if (eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCode'不能为空");
		}
		if (userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if (createTimeStamp == null || createTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'createTimeStamp'不能为空");
		}
		if (lastUpdateTimeStamp == null || lastUpdateTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'lastUpdateTimeStamp'不能为空");
		}
		if (userRecordId == null || userRecordId < 1)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if (recordTimeStamp == null || recordTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'recordTimeStamp'不能为空");
		}
		if (eCodeOfContractRecord == null || eCodeOfContractRecord.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfContractRecord'不能为空");
		}
		if (companyId == null || companyId < 1)
		{
			return MyBackInfo.fail(properties, "'company'不能为空");
		}
		if (theNameFormCompany == null || theNameFormCompany.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameFormCompany'不能为空");
		}
		if (theNameOfProject == null || theNameOfProject.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfProject'不能为空");
		}
		if (eCodeFromConstruction == null || eCodeFromConstruction.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeFromConstruction'不能为空");
		}
		if (houseInfoId == null || houseInfoId < 1)
		{
			return MyBackInfo.fail(properties, "'houseInfo'不能为空");
		}
		if (eCodeOfHouseInfo == null || eCodeOfHouseInfo.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfHouseInfo'不能为空");
		}
		if (roomIdOfHouseInfo == null || roomIdOfHouseInfo.length() == 0)
		{
			return MyBackInfo.fail(properties, "'roomIdOfHouseInfo'不能为空");
		}
		if (contractSumPrice == null || contractSumPrice < 1)
		{
			return MyBackInfo.fail(properties, "'contractSumPrice'不能为空");
		}
		if (buildingArea == null || buildingArea < 1)
		{
			return MyBackInfo.fail(properties, "'buildingArea'不能为空");
		}
		if (position == null || position.length() == 0)
		{
			return MyBackInfo.fail(properties, "'position'不能为空");
		}
		if (contractSignDate == null || contractSignDate < 1)
		{
			return MyBackInfo.fail(properties, "'contractSignDate'不能为空");
		}
		if (paymentMethod == null || paymentMethod.length() == 0)
		{
			return MyBackInfo.fail(properties, "'paymentMethod'不能为空");
		}
		if (loanBank == null || loanBank.length() == 0)
		{
			return MyBackInfo.fail(properties, "'loanBank'不能为空");
		}
		if (firstPaymentAmount == null || firstPaymentAmount < 1)
		{
			return MyBackInfo.fail(properties, "'firstPaymentAmount'不能为空");
		}
		if (loanAmount == null || loanAmount < 1)
		{
			return MyBackInfo.fail(properties, "'loanAmount'不能为空");
		}
		if (escrowState == null || escrowState.length() == 0)
		{
			return MyBackInfo.fail(properties, "'escrowState'不能为空");
		}
		if (payDate == null || payDate < 1)
		{
			return MyBackInfo.fail(properties, "'payDate'不能为空");
		}
		if (eCodeOfBuilding == null || eCodeOfBuilding.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfBuilding'不能为空");
		}
		if (eCodeFromPublicSecurity == null || eCodeFromPublicSecurity.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeFromPublicSecurity'不能为空");
		}
		if (contractRecordDate == null || contractRecordDate < 1)
		{
			return MyBackInfo.fail(properties, "'contractRecordDate'不能为空");
		}
		if (syncPerson == null || syncPerson.length() == 0)
		{
			return MyBackInfo.fail(properties, "'syncPerson'不能为空");
		}
		if (syncDate == null || syncDate < 1)
		{
			return MyBackInfo.fail(properties, "'syncDate'不能为空");
		}

		Sm_User userStart = (Sm_User) sm_UserDao.findById(userStartId);
		Sm_User userRecord = (Sm_User) sm_UserDao.findById(userRecordId);
		Emmp_CompanyInfo company = (Emmp_CompanyInfo) emmp_CompanyInfoDao.findById(companyId);
		Empj_HouseInfo houseInfo = (Empj_HouseInfo) empj_HouseInfoDao.findById(houseInfoId);
		if (userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if (userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if (company == null)
		{
			return MyBackInfo.fail(properties, "'company'不能为空");
		}
		if (houseInfo == null)
		{
			return MyBackInfo.fail(properties, "'houseInfo'不能为空");
		}

		Tgxy_ContractInfo tgxy_ContractInfo = new Tgxy_ContractInfo();
		tgxy_ContractInfo.setTheState(theState);
		tgxy_ContractInfo.setBusiState(busiState);
		tgxy_ContractInfo.seteCode(eCode);
		tgxy_ContractInfo.setUserStart(userStart);
		tgxy_ContractInfo.setCreateTimeStamp(createTimeStamp);
		tgxy_ContractInfo.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgxy_ContractInfo.setUserRecord(userRecord);
		tgxy_ContractInfo.setRecordTimeStamp(recordTimeStamp);
		tgxy_ContractInfo.seteCodeOfContractRecord(eCodeOfContractRecord);
		tgxy_ContractInfo.setCompany(company);
		tgxy_ContractInfo.setTheNameFormCompany(theNameFormCompany);
		tgxy_ContractInfo.setTheNameOfProject(theNameOfProject);
		tgxy_ContractInfo.seteCodeFromConstruction(eCodeFromConstruction);
		tgxy_ContractInfo.setHouseInfo(houseInfo);
		tgxy_ContractInfo.seteCodeOfHouseInfo(eCodeOfHouseInfo);
		tgxy_ContractInfo.setRoomIdOfHouseInfo(roomIdOfHouseInfo);
		tgxy_ContractInfo.setContractSumPrice(contractSumPrice);
		tgxy_ContractInfo.setBuildingArea(buildingArea);
		tgxy_ContractInfo.setPosition(position);
		// tgxy_ContractInfo.setContractSignDate(contractSignDate);
		tgxy_ContractInfo.setPaymentMethod(paymentMethod);
		tgxy_ContractInfo.setLoanBank(loanBank);
		tgxy_ContractInfo.setFirstPaymentAmount(firstPaymentAmount);
		tgxy_ContractInfo.setLoanAmount(loanAmount);
		tgxy_ContractInfo.setEscrowState(escrowState);
		// tgxy_ContractInfo.setPayDate(payDate);
		tgxy_ContractInfo.seteCodeOfBuilding(eCodeOfBuilding);
		tgxy_ContractInfo.seteCodeFromPublicSecurity(eCodeFromPublicSecurity);
		// tgxy_ContractInfo.setContractRecordDate(contractRecordDate);
		tgxy_ContractInfo.setSyncPerson(syncPerson);
		tgxy_ContractInfo.setSyncDate(syncDate);
		tgxy_ContractInfoDao.save(tgxy_ContractInfo);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
