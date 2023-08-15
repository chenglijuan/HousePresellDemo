package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountForm;
import zhishusz.housepresell.database.dao.Tgpf_RefundInfoDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service 审批流提交 操作：退房退款
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_RefundInfoApprovalProcessService
{
	
	@Autowired
	private Tgpf_RefundInfoDao tgpf_RefundInfoDao;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;
	
	@Autowired
	private Tgpj_BuildingAccountLogCalculateService tgpj_BuildingAccountLimitedUpdateService;
	
	public Properties execute(Tgpf_RefundInfoForm model)
	{
		Properties properties = new MyProperties();

		String busiCode = model.getBusiCode();
		Long tableId = model.getTableId();

		if(busiCode == null || busiCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "业务编码不能为空");
		}

		if(tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "未查询到有效的退房退款信息！");
		}

		//查询退房退款
		Tgpf_RefundInfo refundInfo = tgpf_RefundInfoDao.findById(tableId);
		
		properties = sm_ApprovalProcessGetService.execute(busiCode, model.getUserId());
		if(properties.getProperty("info").equals("noApproval") || properties.getProperty("result").equals("fail") )
		{
			if(properties.getProperty("info").equals("noApproval")){
				return MyBackInfo.fail(properties, "备案失败");
			}
			return properties;
		}
		
		//设置退房退款业务状态
		refundInfo.setBusiState(S_BusiState.NoRecord);
		
		tgpf_RefundInfoDao.save(refundInfo);//更新退房退款业务状态
		
		/*busiCode = refundInfo.geteCode().split("N")[0];*/
		
		// 查询楼幢账户
		Empj_BuildingInfo building = refundInfo.getBuilding();

		if (building == null)
		{
			return MyBackInfo.fail(properties, "关联楼幢账户");
		}
		
		// 查询楼幢账户
		Tgpj_BuildingAccountForm buildingAccountForm = new Tgpj_BuildingAccountForm();

		buildingAccountForm.setBuilding(building);

		buildingAccountForm.setTheState(S_TheState.Normal);

		Tgpj_BuildingAccount tgpj_BuildingAccount = tgpj_BuildingAccountDao.findOneByQuery_T(tgpj_BuildingAccountDao.getQuery(tgpj_BuildingAccountDao.getBasicHQL(), buildingAccountForm));

		if (null == tgpj_BuildingAccount)
		{

			return MyBackInfo.fail(properties, "楼幢账户查询为空");

		}
		
		setChanges(tgpj_BuildingAccount, refundInfo, busiCode);
		
		Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");
		
		model.setButtonType("2");
		
		//审批操作
		sm_approvalProcessService.execute(refundInfo, model, sm_approvalProcess_cfg);
		
		return properties;
	}
	
	/**
	 * 保存日志
	 * @param buildingAccount
	 */
	 private void setChanges(Tgpj_BuildingAccount buildingAccount, Tgpf_RefundInfo refundInfo, String busiCode) {
			// 不发生修改的字段
			Tgpj_BuildingAccountLog tgpj_BuildingAccountLog = new Tgpj_BuildingAccountLog();
			tgpj_BuildingAccountLog.setTheState(S_TheState.Normal);
			tgpj_BuildingAccountLog.setBusiState(buildingAccount.getBusiState());
			tgpj_BuildingAccountLog.seteCode(buildingAccount.geteCode());
			tgpj_BuildingAccountLog.setUserStart(buildingAccount.getUserStart());
			tgpj_BuildingAccountLog.setCreateTimeStamp(buildingAccount.getCreateTimeStamp());
			tgpj_BuildingAccountLog.setUserUpdate(buildingAccount.getUserUpdate());
			tgpj_BuildingAccountLog.setLastUpdateTimeStamp(System.currentTimeMillis());
			tgpj_BuildingAccountLog.setUserRecord(buildingAccount.getUserRecord());
			tgpj_BuildingAccountLog.setRecordTimeStamp(buildingAccount.getRecordTimeStamp());
			tgpj_BuildingAccountLog.setDevelopCompany(buildingAccount.getDevelopCompany());
			tgpj_BuildingAccountLog.seteCodeOfDevelopCompany(buildingAccount.geteCodeOfDevelopCompany());
			tgpj_BuildingAccountLog.setProject(buildingAccount.getProject());
			tgpj_BuildingAccountLog.setTheNameOfProject(buildingAccount.getTheNameOfProject());
			tgpj_BuildingAccountLog.setBuilding(buildingAccount.getBuilding());
			tgpj_BuildingAccountLog.setPayment(buildingAccount.getPayment());
			tgpj_BuildingAccountLog.seteCodeOfBuilding(buildingAccount.geteCodeOfBuilding());
			tgpj_BuildingAccountLog.setEscrowStandard(buildingAccount.getEscrowStandard());
			tgpj_BuildingAccountLog.setEscrowArea(buildingAccount.getEscrowArea());
			tgpj_BuildingAccountLog.setBuildingArea(buildingAccount.getBuildingArea());
			tgpj_BuildingAccountLog.setOrgLimitedAmount(buildingAccount.getOrgLimitedAmount());
			tgpj_BuildingAccountLog.setCurrentFigureProgress(buildingAccount.getCurrentFigureProgress());
			tgpj_BuildingAccountLog.setCurrentLimitedRatio(buildingAccount.getCurrentLimitedRatio());
			tgpj_BuildingAccountLog.setNodeLimitedAmount(buildingAccount.getNodeLimitedAmount());
			tgpj_BuildingAccountLog.setTotalGuaranteeAmount(buildingAccount.getTotalGuaranteeAmount());
			tgpj_BuildingAccountLog.setCashLimitedAmount(buildingAccount.getCashLimitedAmount());
			tgpj_BuildingAccountLog.setTotalAccountAmount(buildingAccount.getTotalAccountAmount());
			tgpj_BuildingAccountLog.setPayoutAmount(buildingAccount.getPayoutAmount());
			tgpj_BuildingAccountLog.setAppliedNoPayoutAmount(buildingAccount.getAppliedNoPayoutAmount());
			tgpj_BuildingAccountLog.setCurrentEscrowFund(buildingAccount.getCurrentEscrowFund());
			tgpj_BuildingAccountLog.setAppropriateFrozenAmount(buildingAccount.getAppropriateFrozenAmount());
			tgpj_BuildingAccountLog.setRecordAvgPriceOfBuildingFromPresellSystem(buildingAccount.getRecordAvgPriceOfBuildingFromPresellSystem());
			tgpj_BuildingAccountLog.setRecordAvgPriceOfBuilding(buildingAccount.getRecordAvgPriceOfBuilding());
			tgpj_BuildingAccountLog.setLogId(buildingAccount.getLogId());
			tgpj_BuildingAccountLog.setActualAmount(buildingAccount.getActualAmount());
			tgpj_BuildingAccountLog.setPaymentLines(buildingAccount.getPaymentLines());
			tgpj_BuildingAccountLog.setRelatedBusiCode(busiCode);
			tgpj_BuildingAccountLog.setTgpj_BuildingAccount(buildingAccount);
			tgpj_BuildingAccountLog.setVersionNo(buildingAccount.getVersionNo());
			tgpj_BuildingAccountLog.setPaymentProportion(buildingAccount.getPaymentProportion());
			tgpj_BuildingAccountLog.setBuildAmountPaid(buildingAccount.getBuildAmountPaid());
			tgpj_BuildingAccountLog.setBuildAmountPay(buildingAccount.getBuildAmountPay());
			tgpj_BuildingAccountLog.setTotalAmountGuaranteed(buildingAccount.getTotalAmountGuaranteed());
			tgpj_BuildingAccountLog.setCashLimitedAmount(buildingAccount.getCashLimitedAmount());
			tgpj_BuildingAccountLog.setEffectiveLimitedAmount(buildingAccount.getEffectiveLimitedAmount());
			tgpj_BuildingAccountLog.setSpilloverAmount(buildingAccount.getSpilloverAmount());
			tgpj_BuildingAccountLog.setAllocableAmount(buildingAccount.getAllocableAmount());
			tgpj_BuildingAccountLog.setBldLimitAmountVerDtl(buildingAccount.getBldLimitAmountVerDtl());
			tgpj_BuildingAccountLog.setRelatedBusiTableId(refundInfo.getTableId());
			
			// 已退款金额
			Double refundAmount = buildingAccount.getRefundAmount();
			// 已申请退款未拨付金额（元）
			Double applyRefundPayoutAmount = buildingAccount.getApplyRefundPayoutAmount();
			
			// 获取实际退款金额
			Double actualRefundAmount = refundInfo.getActualRefundAmount();

			if (null == refundAmount || refundAmount == 0)
			{
				refundAmount = actualRefundAmount;
			}
			else
			{
				refundAmount = MyDouble.getInstance().doubleAddDouble(refundAmount, actualRefundAmount);
			}

			applyRefundPayoutAmount = MyDouble.getInstance().doubleSubtractDouble(applyRefundPayoutAmount,
					actualRefundAmount);
			
			tgpj_BuildingAccountLog.setRefundAmount(refundAmount);
			tgpj_BuildingAccountLog.setApplyRefundPayoutAmount(applyRefundPayoutAmount);

			tgpj_BuildingAccountLimitedUpdateService.execute(tgpj_BuildingAccountLog);
	 }
}
