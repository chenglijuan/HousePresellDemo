package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountForm;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
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
 * Service更新操作：楼幢账户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BuildingAccountUpdateService
{
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	public Properties execute(Tgpj_BuildingAccountForm model)
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
		Long projectId = model.getProjectId();
		String theNameOfProject = model.getTheNameOfProject();
		Long buildingId = model.getBuildingId();
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
		Double recordAvgPriceOfBuildingFromPresellSystem = model.getRecordAvgPriceOfBuildingFromPresellSystem();
		Double recordAvgPriceOfBuilding = model.getRecordAvgPriceOfBuilding();
		Long logId = model.getLogId();
		
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
		if(buildingId == null || buildingId < 1)
		{
			return MyBackInfo.fail(properties, "'building'不能为空");
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
	
		Long tgpj_BuildingAccountId = model.getTableId();
		Tgpj_BuildingAccount tgpj_BuildingAccount = (Tgpj_BuildingAccount)tgpj_BuildingAccountDao.findById(tgpj_BuildingAccountId);
		if(tgpj_BuildingAccount == null)
		{
			return MyBackInfo.fail(properties, "'Tgpj_BuildingAccount(Id:" + tgpj_BuildingAccountId + ")'不存在");
		}
		
		tgpj_BuildingAccount.setTheState(theState);
		tgpj_BuildingAccount.setBusiState(busiState);
		tgpj_BuildingAccount.seteCode(eCode);
		tgpj_BuildingAccount.setUserStart(userStart);
		tgpj_BuildingAccount.setCreateTimeStamp(createTimeStamp);
		tgpj_BuildingAccount.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgpj_BuildingAccount.setUserRecord(userRecord);
		tgpj_BuildingAccount.setRecordTimeStamp(recordTimeStamp);
		tgpj_BuildingAccount.setDevelopCompany(developCompany);
		tgpj_BuildingAccount.seteCodeOfDevelopCompany(eCodeOfDevelopCompany);
		tgpj_BuildingAccount.setProject(project);
		tgpj_BuildingAccount.setTheNameOfProject(theNameOfProject);
		tgpj_BuildingAccount.setBuilding(building);
		tgpj_BuildingAccount.seteCodeOfBuilding(eCodeOfBuilding);
		tgpj_BuildingAccount.setEscrowStandard(escrowStandard);
		tgpj_BuildingAccount.setEscrowArea(escrowArea);
		tgpj_BuildingAccount.setBuildingArea(buildingArea);
		tgpj_BuildingAccount.setOrgLimitedAmount(orgLimitedAmount);
		tgpj_BuildingAccount.setCurrentFigureProgress(currentFigureProgress);
		tgpj_BuildingAccount.setCurrentLimitedRatio(currentLimitedRatio);
		tgpj_BuildingAccount.setNodeLimitedAmount(nodeLimitedAmount);
		tgpj_BuildingAccount.setTotalGuaranteeAmount(totalGuaranteeAmount);
		tgpj_BuildingAccount.setCashLimitedAmount(cashLimitedAmount);
		tgpj_BuildingAccount.setEffectiveLimitedAmount(effectiveLimitedAmount);
		tgpj_BuildingAccount.setTotalAccountAmount(totalAccountAmount);
		tgpj_BuildingAccount.setSpilloverAmount(spilloverAmount);
		tgpj_BuildingAccount.setPayoutAmount(payoutAmount);
		tgpj_BuildingAccount.setAppliedNoPayoutAmount(appliedNoPayoutAmount);
		tgpj_BuildingAccount.setApplyRefundPayoutAmount(applyRefundPayoutAmount);
		tgpj_BuildingAccount.setRefundAmount(refundAmount);
		tgpj_BuildingAccount.setCurrentEscrowFund(currentEscrowFund);
		tgpj_BuildingAccount.setAllocableAmount(allocableAmount);
		tgpj_BuildingAccount.setRecordAvgPriceOfBuildingFromPresellSystem(recordAvgPriceOfBuildingFromPresellSystem);
		tgpj_BuildingAccount.setRecordAvgPriceOfBuilding(recordAvgPriceOfBuilding);
		tgpj_BuildingAccount.setLogId(logId);
	
		tgpj_BuildingAccountDao.save(tgpj_BuildingAccount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
