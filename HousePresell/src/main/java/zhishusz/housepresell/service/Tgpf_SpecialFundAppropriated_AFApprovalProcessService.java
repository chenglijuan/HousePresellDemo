package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;

import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFForm;
import zhishusz.housepresell.controller.form.Tgxy_EscrowAgreementForm;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDtlDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgpj_EscrowStandardVerMngDao;
import zhishusz.housepresell.database.dao.Empj_BuildingExtendInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;

/*
 * Service提交操作:提交 特殊拨付申请
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_SpecialFundAppropriated_AFApprovalProcessService
{
	// 特殊拨付
	private static String BUSI_CODE = "061206";

	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDao tgpf_SpecialFundAppropriated_AFDao;// 特殊拨付申请
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDtlDao tgpf_SpecialFundAppropriated_AFDtlDao;// 特殊拨付申请子表
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;// eCode生成规则
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;// 楼幢账户
	@Autowired
	private Tgpj_BldLimitAmountVer_AFDao tgpj_BldLimitAmountVer_AFDao;// 版本管理-受限节点设置(主表)
	@Autowired
	private Tgpj_BldLimitAmountVer_AFDtlDao tgpj_BldLimitAmountVer_AFDtlDao;// 受限额度设置(子表)
	@Autowired
	private Tgpj_EscrowStandardVerMngDao tgpj_EscrowStandardVerMngDao;// 托管标准
	@Autowired
	private Empj_BuildingExtendInfoDao empj_BuildingExtendInfoDao;// 楼幢拓展表
	@Autowired
	private Tgpj_BuildingAccountLimitedUpdateService tgpj_BuildingAccountLimitedUpdateService;// 楼幢账户日志表计算公共方法

	public Properties execute(Tgpf_SpecialFundAppropriated_AFForm model)
	{
		Properties properties = new MyProperties();

		String buttonType = model.getButtonType(); // 1： 保存按钮 2：提交按钮

		if (null == buttonType || buttonType.trim().isEmpty())
		{
			buttonType = "2";
		}

		String busiCode = model.getBusiCode();
		Long tableId = model.getTableId();

		model.setButtonType(buttonType);

		if (busiCode == null || busiCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'业务编码'不能为空");
		}

		if (tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "请选择有效的特殊拨付申请");
		}

		Tgpf_SpecialFundAppropriated_AF tgpf_SpecialFundAppropriated_AF = tgpf_SpecialFundAppropriated_AFDao
				.findById(tableId);
		if (null == tgpf_SpecialFundAppropriated_AF)
		{
			return MyBackInfo.fail(properties, "未查询到有效的申请信息");
		}

		/*
		 * xsz by time 2018-11-13 15:29:54
		 * 根据审批状态判断是否提交
		 */
		if (S_ApprovalState.Examining.equals(tgpf_SpecialFundAppropriated_AF.getApprovalState()))
		{
			return MyBackInfo.fail(properties, "该协议已在审核中，不可重复提交");
		}
		else if (S_ApprovalState.Completed.equals(tgpf_SpecialFundAppropriated_AF.getApprovalState()))
		{
			return MyBackInfo.fail(properties, "该协议已审批完成，不可重复提交");
		}

		properties = sm_ApprovalProcessGetService.execute(busiCode, model.getUserId());
		/*
		 * 如果未查询到审批流程，则直接审批通过
		 */
		if ("noApproval".equals(properties.getProperty("info")))
		{
			/*
			 * 校验是否存在有效的划款子表信息
			 */
			Tgpf_SpecialFundAppropriated_AFDtlForm dtlForm = new Tgpf_SpecialFundAppropriated_AFDtlForm();
			dtlForm.setTheState(S_TheState.Normal);
			dtlForm.setSpecialAppropriated(tgpf_SpecialFundAppropriated_AF);
			Integer dtlCount = tgpf_SpecialFundAppropriated_AFDtlDao
					.findByPage_Size(tgpf_SpecialFundAppropriated_AFDtlDao
							.getQuery_Size(tgpf_SpecialFundAppropriated_AFDtlDao.getBasicHQL(), dtlForm));

			if (dtlCount == 0)
			{
				return MyBackInfo.fail(properties, "该协议未查询到有效的划款信息");
			}

			// 审批状态设置为已完结
			tgpf_SpecialFundAppropriated_AF.setApprovalState(S_ApprovalState.Completed);
			// 申请单状态
			tgpf_SpecialFundAppropriated_AF.setBusiState("6");
			// 拨付状态
			tgpf_SpecialFundAppropriated_AF.setApplyState(2);

			tgpf_SpecialFundAppropriated_AFDao.save(tgpf_SpecialFundAppropriated_AF);

			// 处理完成之后返回成功信息
			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		}
		else if ("fail".equals(properties.getProperty(S_NormalFlag.result)))
		{
			// 判断当前登录用户是否有权限发起审批
			return properties;
		}
		else
		{

			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties
					.get("sm_approvalProcess_cfg");

			/*
			 * xsz by time 2018-12-5 10:47:54
			 * 提交时先判断本次拨付申请金额（totalApplyAmount）是否大于楼幢账户表中的可拨付金额（buildingAccount.
			 * getAllocableAmount()），如果大于，则提示重新申请；
			 * 否则操作楼幢账户log表：
			 * 已申请未拨付金额（元）：appliedNoPayoutAmount +
			 * 拨付冻结金额 ：appropriateFrozenAmount +
			 * 可划拨金额（元）：allocableAmount -
			 * xsz by time 2018-12-6 11:27:13
			 * zcl提出需求：
			 * 发起特殊拨付申请时，需要检验“本次划款申请金额”<=“当前托管余额”；
			 */
			Empj_BuildingInfo empj_BuildingInfo = tgpf_SpecialFundAppropriated_AF.getBuilding();

			// 获取楼幢账户信息
			Tgpj_BuildingAccount buildingAccount = empj_BuildingInfo.getBuildingAccount();
			
			// if (tgpf_SpecialFundAppropriated_AF.getTotalApplyAmount() >
			// buildingAccount.getAllocableAmount())
			// {
			// return MyBackInfo.fail(properties, "当前可划拨金额小于拨付申请金额，请检查后重新申请！");
			// }
			
			if (tgpf_SpecialFundAppropriated_AF.getTotalApplyAmount() > buildingAccount.getCurrentEscrowFund())
			{
				return MyBackInfo.fail(properties, "本次划款申请金额不得大于当前托管余额，请检查后重新申请！");
			}

			// 审批操作
			properties = sm_approvalProcessService.execute(tgpf_SpecialFundAppropriated_AF, model,
					sm_approvalProcess_cfg);

			// 保存楼幢账户log表
			setChanges(buildingAccount, tgpf_SpecialFundAppropriated_AF);

			// 审批流程状态
			tgpf_SpecialFundAppropriated_AF.setApprovalState(S_ApprovalState.Examining);
			// 提交审批后状态置为提交态（2-已提交）
			tgpf_SpecialFundAppropriated_AF.setBusiState("2");

			tgpf_SpecialFundAppropriated_AFDao.save(tgpf_SpecialFundAppropriated_AF);

		}

		return properties;
	}

	/**
	 * 保存日志
	 * 
	 * 楼幢账户log表：Tgpj_BuildingAccountLog
	 * 已申请未拨付金额（元）：appliedNoPayoutAmount +
	 * 拨付冻结金额 ：appropriateFrozenAmount +
	 * 可划拨金额（元）：allocableAmount -
	 */
	private void setChanges(Tgpj_BuildingAccount buildingAccount, Tgpf_SpecialFundAppropriated_AF AF)
	{
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
		tgpj_BuildingAccountLog.setCurrentEscrowFund(buildingAccount.getCurrentEscrowFund());
		tgpj_BuildingAccountLog.setRecordAvgPriceOfBuildingFromPresellSystem(
				buildingAccount.getRecordAvgPriceOfBuildingFromPresellSystem());
		tgpj_BuildingAccountLog.setRecordAvgPriceOfBuilding(buildingAccount.getRecordAvgPriceOfBuilding());
		tgpj_BuildingAccountLog.setLogId(buildingAccount.getLogId());
		tgpj_BuildingAccountLog.setActualAmount(buildingAccount.getActualAmount());
		tgpj_BuildingAccountLog.setPaymentLines(buildingAccount.getPaymentLines());
		tgpj_BuildingAccountLog.setTgpj_BuildingAccount(buildingAccount);
		tgpj_BuildingAccountLog.setPaymentProportion(buildingAccount.getPaymentProportion());
		tgpj_BuildingAccountLog.setBuildAmountPaid(buildingAccount.getBuildAmountPaid());
		tgpj_BuildingAccountLog.setBuildAmountPay(buildingAccount.getBuildAmountPay());
		tgpj_BuildingAccountLog.setTotalAmountGuaranteed(buildingAccount.getTotalAmountGuaranteed());
		tgpj_BuildingAccountLog.setCashLimitedAmount(buildingAccount.getCashLimitedAmount());
		tgpj_BuildingAccountLog.setEffectiveLimitedAmount(buildingAccount.getEffectiveLimitedAmount());
		tgpj_BuildingAccountLog.setSpilloverAmount(buildingAccount.getSpilloverAmount());
		tgpj_BuildingAccountLog.setBldLimitAmountVerDtl(buildingAccount.getBldLimitAmountVerDtl());
		tgpj_BuildingAccountLog.setRefundAmount(buildingAccount.getRefundAmount());
		tgpj_BuildingAccountLog.setApplyRefundPayoutAmount(buildingAccount.getApplyRefundPayoutAmount());

		tgpj_BuildingAccountLog.setVersionNo(buildingAccount.getVersionNo());
		tgpj_BuildingAccountLog.setRelatedBusiCode(BUSI_CODE);
		tgpj_BuildingAccountLog.setRelatedBusiTableId(AF.getTableId());

		Double allocableAmount = buildingAccount.getAllocableAmount();
		if (null == allocableAmount || allocableAmount < 0)
		{
			allocableAmount = 0.00;
		}
		Double appliedNoPayoutAmount = buildingAccount.getAppliedNoPayoutAmount();
		if (null == appliedNoPayoutAmount || appliedNoPayoutAmount < 0)
		{
			appliedNoPayoutAmount = 0.00;
		}
		Double appropriateFrozenAmount = buildingAccount.getAppropriateFrozenAmount();
		if (null == appropriateFrozenAmount || appropriateFrozenAmount < 0)
		{
			appropriateFrozenAmount = 0.00;
		}

		allocableAmount = MyDouble.getInstance().doubleSubtractDouble(allocableAmount, AF.getTotalApplyAmount());
		appliedNoPayoutAmount = MyDouble.getInstance().doubleAddDouble(appliedNoPayoutAmount, AF.getTotalApplyAmount());
		appropriateFrozenAmount = MyDouble.getInstance().doubleAddDouble(appropriateFrozenAmount,
				AF.getTotalApplyAmount());

		// 可划拨金额（元）：allocableAmount -
		tgpj_BuildingAccountLog.setAllocableAmount(allocableAmount);
		// 已申请未拨付金额（元）：appliedNoPayoutAmount +
		tgpj_BuildingAccountLog.setAppliedNoPayoutAmount(appliedNoPayoutAmount);
		// 拨付冻结金额 ：appropriateFrozenAmount +
		tgpj_BuildingAccountLog.setAppropriateFrozenAmount(appropriateFrozenAmount);

		tgpj_BuildingAccountLimitedUpdateService.execute(tgpj_BuildingAccountLog);
	}

}
