package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFDtlForm;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.dao.Tgpj_BankAccountSupervisedDao;

/*
 * Service更新操作：申请-用款-明细
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundAppropriated_AFDtlUpdateService
{
	@Autowired
	private Tgpf_FundAppropriated_AFDtlDao tgpf_FundAppropriated_AFDtlDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;
	@Autowired
	private Tgpj_BankAccountSupervisedDao tgpj_BankAccountSupervisedDao;
	
	public Properties execute(Tgpf_FundAppropriated_AFDtlForm model)
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
		Long buildingId = model.getBuildingId();
		String eCodeOfBuilding = model.geteCodeOfBuilding();
		Long mainTableId = model.getMainTableId();
		Long bankAccountSupervisedId = model.getBankAccountSupervisedId();
		String supervisedBankAccount = model.getSupervisedBankAccount();
		Double allocableAmount = model.getAllocableAmount();
		Double appliedAmount = model.getAppliedAmount();
		String escrowStandard = model.getEscrowStandard();
		Double orgLimitedAmount = model.getOrgLimitedAmount();
		String currentFigureProgress = model.getCurrentFigureProgress();
		Double currentLimitedRatio = model.getCurrentLimitedRatio();
		Double currentLimitedAmount = model.getCurrentLimitedAmount();
		Double totalAccountAmount = model.getTotalAccountAmount();
		Double appliedPayoutAmount = model.getAppliedPayoutAmount();
		Double currentEscrowFund = model.getCurrentEscrowFund();
		Double refundAmount = model.getRefundAmount();
		Integer payoutState = model.getPayoutState();
		
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
		if(buildingId == null || buildingId < 1)
		{
			return MyBackInfo.fail(properties, "'building'不能为空");
		}
		if(eCodeOfBuilding == null || eCodeOfBuilding.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfBuilding'不能为空");
		}
		if(mainTableId == null || mainTableId < 1)
		{
			return MyBackInfo.fail(properties, "'mainTable'不能为空");
		}
		if(bankAccountSupervisedId == null || bankAccountSupervisedId < 1)
		{
			return MyBackInfo.fail(properties, "'bankAccountSupervised'不能为空");
		}
		if(supervisedBankAccount == null || supervisedBankAccount.length() == 0)
		{
			return MyBackInfo.fail(properties, "'supervisedBankAccount'不能为空");
		}
		if(allocableAmount == null || allocableAmount < 1)
		{
			return MyBackInfo.fail(properties, "'allocableAmount'不能为空");
		}
		if(appliedAmount == null || appliedAmount < 1)
		{
			return MyBackInfo.fail(properties, "'appliedAmount'不能为空");
		}
		if(escrowStandard == null || escrowStandard.length() == 0)
		{
			return MyBackInfo.fail(properties, "'escrowStandard'不能为空");
		}
		if(orgLimitedAmount == null || orgLimitedAmount < 1)
		{
			return MyBackInfo.fail(properties, "'orgLimitedAmount'不能为空");
		}
		if(currentFigureProgress == null || currentFigureProgress.length() == 0)
		{
			return MyBackInfo.fail(properties, "'currentFigureProgress'不能为空");
		}
		if(currentLimitedRatio == null || currentLimitedRatio < 1)
		{
			return MyBackInfo.fail(properties, "'currentLimitedRatio'不能为空");
		}
		if(currentLimitedAmount == null || currentLimitedAmount < 1)
		{
			return MyBackInfo.fail(properties, "'currentLimitedAmount'不能为空");
		}
		if(totalAccountAmount == null || totalAccountAmount < 1)
		{
			return MyBackInfo.fail(properties, "'totalAccountAmount'不能为空");
		}
		if(appliedPayoutAmount == null || appliedPayoutAmount < 1)
		{
			return MyBackInfo.fail(properties, "'appliedPayoutAmount'不能为空");
		}
		if(currentEscrowFund == null || currentEscrowFund < 1)
		{
			return MyBackInfo.fail(properties, "'currentEscrowFund'不能为空");
		}
		if(refundAmount == null || refundAmount < 1)
		{
			return MyBackInfo.fail(properties, "'refundAmount'不能为空");
		}
		if(payoutState == null || payoutState < 1)
		{
			return MyBackInfo.fail(properties, "'payoutState'不能为空");
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
		Empj_BuildingInfo building = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(buildingId);
		if(building == null)
		{
			return MyBackInfo.fail(properties, "'building(Id:" + buildingId + ")'不存在");
		}
		Tgpf_FundAppropriated_AF mainTable = (Tgpf_FundAppropriated_AF)tgpf_FundAppropriated_AFDao.findById(mainTableId);
		if(mainTable == null)
		{
			return MyBackInfo.fail(properties, "'mainTable(Id:" + mainTableId + ")'不存在");
		}
		Tgpj_BankAccountSupervised bankAccountSupervised = (Tgpj_BankAccountSupervised)tgpj_BankAccountSupervisedDao.findById(bankAccountSupervisedId);
		if(bankAccountSupervised == null)
		{
			return MyBackInfo.fail(properties, "'bankAccountSupervised(Id:" + bankAccountSupervisedId + ")'不存在");
		}
	
		Long tgpf_FundAppropriated_AFDtlId = model.getTableId();
		Tgpf_FundAppropriated_AFDtl tgpf_FundAppropriated_AFDtl = (Tgpf_FundAppropriated_AFDtl)tgpf_FundAppropriated_AFDtlDao.findById(tgpf_FundAppropriated_AFDtlId);
		if(tgpf_FundAppropriated_AFDtl == null)
		{
			return MyBackInfo.fail(properties, "'Tgpf_FundAppropriated_AFDtl(Id:" + tgpf_FundAppropriated_AFDtlId + ")'不存在");
		}
		
		tgpf_FundAppropriated_AFDtl.setTheState(theState);
		tgpf_FundAppropriated_AFDtl.setBusiState(busiState);
		tgpf_FundAppropriated_AFDtl.seteCode(eCode);
		tgpf_FundAppropriated_AFDtl.setUserStart(userStart);
		tgpf_FundAppropriated_AFDtl.setCreateTimeStamp(createTimeStamp);
		tgpf_FundAppropriated_AFDtl.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgpf_FundAppropriated_AFDtl.setUserRecord(userRecord);
		tgpf_FundAppropriated_AFDtl.setRecordTimeStamp(recordTimeStamp);
		tgpf_FundAppropriated_AFDtl.setBuilding(building);
		tgpf_FundAppropriated_AFDtl.seteCodeOfBuilding(eCodeOfBuilding);
		tgpf_FundAppropriated_AFDtl.setMainTable(mainTable);
		tgpf_FundAppropriated_AFDtl.setBankAccountSupervised(bankAccountSupervised);
		tgpf_FundAppropriated_AFDtl.setSupervisedBankAccount(supervisedBankAccount);
		tgpf_FundAppropriated_AFDtl.setAllocableAmount(allocableAmount);
		tgpf_FundAppropriated_AFDtl.setAppliedAmount(appliedAmount);
		tgpf_FundAppropriated_AFDtl.setEscrowStandard(escrowStandard);
		tgpf_FundAppropriated_AFDtl.setOrgLimitedAmount(orgLimitedAmount);
		tgpf_FundAppropriated_AFDtl.setCurrentFigureProgress(currentFigureProgress);
		tgpf_FundAppropriated_AFDtl.setCurrentLimitedRatio(currentLimitedRatio);
		tgpf_FundAppropriated_AFDtl.setCurrentLimitedAmount(currentLimitedAmount);
		tgpf_FundAppropriated_AFDtl.setTotalAccountAmount(totalAccountAmount);
		tgpf_FundAppropriated_AFDtl.setAppliedPayoutAmount(appliedPayoutAmount);
		tgpf_FundAppropriated_AFDtl.setCurrentEscrowFund(currentEscrowFund);
		tgpf_FundAppropriated_AFDtl.setRefundAmount(refundAmount);
		tgpf_FundAppropriated_AFDtl.setPayoutState(payoutState);
	
		tgpf_FundAppropriated_AFDtlDao.save(tgpf_FundAppropriated_AFDtl);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
