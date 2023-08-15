package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountLogForm;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountLogDao;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeDao;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;

/*
 * Service更新操作：楼幢账户Log表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BuildingAccountLogUpdateService
{
	@Autowired
	private Tgpj_BuildingAccountLogDao tgpj_BuildingAccountLogDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Empj_PaymentGuaranteeDao empj_PaymentGuaranteeDao;
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;
	
	public Properties execute(Tgpj_BuildingAccountLogForm model)
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
		Long buildingId = model.getBuildingId();
		Long paymentId = model.getPaymentId();
		String eCodeOfBuilding = model.geteCodeOfBuilding();
		String escrowStandard = model.getEscrowStandard();
		Double escrowArea = model.getEscrowArea();
		Double buildingArea = model.getBuildingArea();
		Double orgLimitedAmount = model.getOrgLimitedAmount();
		String currentFigureProgress = model.getCurrentFigureProgress();
		Double currentLimitedRatio = model.getCurrentLimitedRatio();
		Double nodeLimitedAmount = model.getNodeLimitedAmount();
		Double totalGuaranteeAmount = model.getTotalGuaranteeAmount();
		Double cashLimitedAmount = model.getCashLimitedAmount();
		Double effectiveLimitedAmount = model.getEffectiveLimitedAmount();
		Double totalAccountAmount = model.getTotalAccountAmount();
		Double spilloverAmount = model.getSpilloverAmount();
		Double payoutAmount = model.getPayoutAmount();
		Double appliedNoPayoutAmount = model.getAppliedNoPayoutAmount();
		Double applyRefundPayoutAmount = model.getApplyRefundPayoutAmount();
		Double refundAmount = model.getRefundAmount();
		Double currentEscrowFund = model.getCurrentEscrowFund();
		Double allocableAmount = model.getAllocableAmount();
		Double appropriateFrozenAmount = model.getAppropriateFrozenAmount();
		Double recordAvgPriceOfBuildingFromPresellSystem = model.getRecordAvgPriceOfBuildingFromPresellSystem();
		Double recordAvgPriceOfBuilding = model.getRecordAvgPriceOfBuilding();
		Long logId = model.getLogId();
		Double actualAmount = model.getActualAmount();
		Double paymentLines = model.getPaymentLines();
		Double paymentProportion = model.getPaymentProportion();
		Double buildAmountPaid = model.getBuildAmountPaid();
		Double buildAmountPay = model.getBuildAmountPay();
		Double totalAmountGuaranteed = model.getTotalAmountGuaranteed();
		String relatedBusiCode = model.getRelatedBusiCode();
		Long relatedBusiTableId = model.getRelatedBusiTableId();
		Long tgpj_BuildingAccountId = model.getTgpj_BuildingAccountId();
		Long versionNo = model.getVersionNo();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(busiState == null || busiState.length() == 0)
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
		if(userUpdateId == null || userUpdateId < 1)
		{
			return MyBackInfo.fail(properties, "'userUpdate'不能为空");
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
		if(buildingId == null || buildingId < 1)
		{
			return MyBackInfo.fail(properties, "'building'不能为空");
		}
		if(paymentId == null || paymentId < 1)
		{
			return MyBackInfo.fail(properties, "'payment'不能为空");
		}
		if(eCodeOfBuilding == null || eCodeOfBuilding.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfBuilding'不能为空");
		}
		if(escrowStandard == null || escrowStandard.length() == 0)
		{
			return MyBackInfo.fail(properties, "'escrowStandard'不能为空");
		}
		if(escrowArea == null || escrowArea < 1)
		{
			return MyBackInfo.fail(properties, "'escrowArea'不能为空");
		}
		if(buildingArea == null || buildingArea < 1)
		{
			return MyBackInfo.fail(properties, "'buildingArea'不能为空");
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
		if(nodeLimitedAmount == null || nodeLimitedAmount < 1)
		{
			return MyBackInfo.fail(properties, "'nodeLimitedAmount'不能为空");
		}
		if(totalGuaranteeAmount == null || totalGuaranteeAmount < 1)
		{
			return MyBackInfo.fail(properties, "'totalGuaranteeAmount'不能为空");
		}
		if(cashLimitedAmount == null || cashLimitedAmount < 1)
		{
			return MyBackInfo.fail(properties, "'cashLimitedAmount'不能为空");
		}
		if(effectiveLimitedAmount == null || effectiveLimitedAmount < 1)
		{
			return MyBackInfo.fail(properties, "'effectiveLimitedAmount'不能为空");
		}
		if(totalAccountAmount == null || totalAccountAmount < 1)
		{
			return MyBackInfo.fail(properties, "'totalAccountAmount'不能为空");
		}
		if(spilloverAmount == null || spilloverAmount < 1)
		{
			return MyBackInfo.fail(properties, "'spilloverAmount'不能为空");
		}
		if(payoutAmount == null || payoutAmount < 1)
		{
			return MyBackInfo.fail(properties, "'payoutAmount'不能为空");
		}
		if(appliedNoPayoutAmount == null || appliedNoPayoutAmount < 1)
		{
			return MyBackInfo.fail(properties, "'appliedNoPayoutAmount'不能为空");
		}
		if(applyRefundPayoutAmount == null || applyRefundPayoutAmount < 1)
		{
			return MyBackInfo.fail(properties, "'applyRefundPayoutAmount'不能为空");
		}
		if(refundAmount == null || refundAmount < 1)
		{
			return MyBackInfo.fail(properties, "'refundAmount'不能为空");
		}
		if(currentEscrowFund == null || currentEscrowFund < 1)
		{
			return MyBackInfo.fail(properties, "'currentEscrowFund'不能为空");
		}
		if(allocableAmount == null || allocableAmount < 1)
		{
			return MyBackInfo.fail(properties, "'allocableAmount'不能为空");
		}
		if(appropriateFrozenAmount == null || appropriateFrozenAmount < 1)
		{
			return MyBackInfo.fail(properties, "'appropriateFrozenAmount'不能为空");
		}
		if(recordAvgPriceOfBuildingFromPresellSystem == null || recordAvgPriceOfBuildingFromPresellSystem < 1)
		{
			return MyBackInfo.fail(properties, "'recordAvgPriceOfBuildingFromPresellSystem'不能为空");
		}
		if(recordAvgPriceOfBuilding == null || recordAvgPriceOfBuilding < 1)
		{
			return MyBackInfo.fail(properties, "'recordAvgPriceOfBuilding'不能为空");
		}
		if(logId == null || logId < 1)
		{
			return MyBackInfo.fail(properties, "'logId'不能为空");
		}
		if(actualAmount == null || actualAmount < 1)
		{
			return MyBackInfo.fail(properties, "'actualAmount'不能为空");
		}
		if(paymentLines == null || paymentLines < 1)
		{
			return MyBackInfo.fail(properties, "'paymentLines'不能为空");
		}
		if(paymentProportion == null || paymentProportion < 1)
		{
			return MyBackInfo.fail(properties, "'paymentProportion'不能为空");
		}
		if(buildAmountPaid == null || buildAmountPaid < 1)
		{
			return MyBackInfo.fail(properties, "'buildAmountPaid'不能为空");
		}
		if(buildAmountPay == null || buildAmountPay < 1)
		{
			return MyBackInfo.fail(properties, "'buildAmountPay'不能为空");
		}
		if(totalAmountGuaranteed == null || totalAmountGuaranteed < 1)
		{
			return MyBackInfo.fail(properties, "'totalAmountGuaranteed'不能为空");
		}
		if(relatedBusiCode == null || relatedBusiCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'relatedBusiCode'不能为空");
		}
		if(relatedBusiTableId == null || relatedBusiTableId < 1)
		{
			return MyBackInfo.fail(properties, "'relatedBusiTableId'不能为空");
		}
		if(tgpj_BuildingAccountId == null || tgpj_BuildingAccountId < 1)
		{
			return MyBackInfo.fail(properties, "'tgpj_BuildingAccount'不能为空");
		}
		if(versionNo == null || versionNo < 1)
		{
			return MyBackInfo.fail(properties, "'versionNo'不能为空");
		}
		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart(Id:" + userStartId + ")'不存在");
		}
		Sm_User userUpdate = (Sm_User)sm_UserDao.findById(userUpdateId);
		if(userUpdate == null)
		{
			return MyBackInfo.fail(properties, "'userUpdate(Id:" + userUpdateId + ")'不存在");
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
		Empj_PaymentGuarantee payment = (Empj_PaymentGuarantee)empj_PaymentGuaranteeDao.findById(paymentId);
		if(payment == null)
		{
			return MyBackInfo.fail(properties, "'payment(Id:" + paymentId + ")'不存在");
		}
		Tgpj_BuildingAccount tgpj_BuildingAccount = (Tgpj_BuildingAccount)tgpj_BuildingAccountDao.findById(tgpj_BuildingAccountId);
		if(tgpj_BuildingAccount == null)
		{
			return MyBackInfo.fail(properties, "'tgpj_BuildingAccount(Id:" + tgpj_BuildingAccountId + ")'不存在");
		}
	
		Long tgpj_BuildingAccountLogId = model.getTableId();
		Tgpj_BuildingAccountLog tgpj_BuildingAccountLog = (Tgpj_BuildingAccountLog)tgpj_BuildingAccountLogDao.findById(tgpj_BuildingAccountLogId);
		if(tgpj_BuildingAccountLog == null)
		{
			return MyBackInfo.fail(properties, "'Tgpj_BuildingAccountLog(Id:" + tgpj_BuildingAccountLogId + ")'不存在");
		}
		
		tgpj_BuildingAccountLog.setTheState(theState);
		tgpj_BuildingAccountLog.setBusiState(busiState);
		tgpj_BuildingAccountLog.seteCode(eCode);
		tgpj_BuildingAccountLog.setUserStart(userStart);
		tgpj_BuildingAccountLog.setCreateTimeStamp(createTimeStamp);
		tgpj_BuildingAccountLog.setUserUpdate(userUpdate);
		tgpj_BuildingAccountLog.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgpj_BuildingAccountLog.setUserRecord(userRecord);
		tgpj_BuildingAccountLog.setRecordTimeStamp(recordTimeStamp);
		tgpj_BuildingAccountLog.setDevelopCompany(developCompany);
		tgpj_BuildingAccountLog.seteCodeOfDevelopCompany(eCodeOfDevelopCompany);
		tgpj_BuildingAccountLog.setProject(project);
		tgpj_BuildingAccountLog.setTheNameOfProject(theNameOfProject);
		tgpj_BuildingAccountLog.setBuilding(building);
		tgpj_BuildingAccountLog.setPayment(payment);
		tgpj_BuildingAccountLog.seteCodeOfBuilding(eCodeOfBuilding);
		tgpj_BuildingAccountLog.setEscrowStandard(escrowStandard);
		tgpj_BuildingAccountLog.setEscrowArea(escrowArea);
		tgpj_BuildingAccountLog.setBuildingArea(buildingArea);
		tgpj_BuildingAccountLog.setOrgLimitedAmount(orgLimitedAmount);
		tgpj_BuildingAccountLog.setCurrentFigureProgress(currentFigureProgress);
		tgpj_BuildingAccountLog.setCurrentLimitedRatio(currentLimitedRatio);
		tgpj_BuildingAccountLog.setNodeLimitedAmount(nodeLimitedAmount);
		tgpj_BuildingAccountLog.setTotalGuaranteeAmount(totalGuaranteeAmount);
		tgpj_BuildingAccountLog.setCashLimitedAmount(cashLimitedAmount);
		tgpj_BuildingAccountLog.setEffectiveLimitedAmount(effectiveLimitedAmount);
		tgpj_BuildingAccountLog.setTotalAccountAmount(totalAccountAmount);
		tgpj_BuildingAccountLog.setSpilloverAmount(spilloverAmount);
		tgpj_BuildingAccountLog.setPayoutAmount(payoutAmount);
		tgpj_BuildingAccountLog.setAppliedNoPayoutAmount(appliedNoPayoutAmount);
		tgpj_BuildingAccountLog.setApplyRefundPayoutAmount(applyRefundPayoutAmount);
		tgpj_BuildingAccountLog.setRefundAmount(refundAmount);
		tgpj_BuildingAccountLog.setCurrentEscrowFund(currentEscrowFund);
		tgpj_BuildingAccountLog.setAllocableAmount(allocableAmount);
		tgpj_BuildingAccountLog.setAppropriateFrozenAmount(appropriateFrozenAmount);
		tgpj_BuildingAccountLog.setRecordAvgPriceOfBuildingFromPresellSystem(recordAvgPriceOfBuildingFromPresellSystem);
		tgpj_BuildingAccountLog.setRecordAvgPriceOfBuilding(recordAvgPriceOfBuilding);
		tgpj_BuildingAccountLog.setLogId(logId);
		tgpj_BuildingAccountLog.setActualAmount(actualAmount);
		tgpj_BuildingAccountLog.setPaymentLines(paymentLines);
		tgpj_BuildingAccountLog.setPaymentProportion(paymentProportion);
		tgpj_BuildingAccountLog.setBuildAmountPaid(buildAmountPaid);
		tgpj_BuildingAccountLog.setBuildAmountPay(buildAmountPay);
		tgpj_BuildingAccountLog.setTotalAmountGuaranteed(totalAmountGuaranteed);
		tgpj_BuildingAccountLog.setRelatedBusiCode(relatedBusiCode);
		tgpj_BuildingAccountLog.setRelatedBusiTableId(relatedBusiTableId);
		tgpj_BuildingAccountLog.setTgpj_BuildingAccount(tgpj_BuildingAccount);
		tgpj_BuildingAccountLog.setVersionNo(versionNo);
	
		tgpj_BuildingAccountLogDao.save(tgpj_BuildingAccountLog);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
