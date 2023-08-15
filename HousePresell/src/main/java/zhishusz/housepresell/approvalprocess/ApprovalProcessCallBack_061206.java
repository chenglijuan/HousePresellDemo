package zhishusz.housepresell.approvalprocess;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Tgpf_BuildingRemainRightLogForm;
import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFDtlForm;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_ApplyState;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_SpecialFundApplyState;
import zhishusz.housepresell.database.po.state.S_SpecialFundBusiState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.external.service.BatchBankTransfersService;
import zhishusz.housepresell.service.Tgpf_BuildingRemainRightLogPublicAddService;
import zhishusz.housepresell.service.Tgpj_BuildingAccountLimitedUpdateService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/**
 * @date 2018-12-3 08:57:01
 * @author XSZ
 * @version 1.0 特殊拨付申请审核
 */
@Transactional
public class ApprovalProcessCallBack_061206 implements IApprovalProcessCallback {
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDao tgpf_SpecialFundAppropriated_AFDao;
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDtlDao tgpf_SpecialFundAppropriated_AFDtlDao;
	// @Autowired
	// private Tgpj_BuildingAccountDao tgpj_buildingAccountDao;
	// @Autowired
	// private MQConnectionUtil mqConnectionUtil;
	// @Autowired
	// private Tgpj_BuildingAccountLogDao tgpj_BuildingAccountLogDao;
	// @Autowired
	// private Tgpf_BuildingRemainRightLogPublicAddService
	// tgpf_BuildingRemainRightLogPublicAddService;
	@Autowired
	private Tgpj_BuildingAccountLimitedUpdateService tgpj_BuildingAccountLimitedUpdateService;// 楼幢账户日志表计算公共方法
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;// 托管账户
	@Autowired
	private Tgpf_BuildingRemainRightLogPublicAddService tgpf_BuildingRemainRightLogPublicAdd;// 留存权益计算方法

	@Autowired
	private BatchBankTransfersService batchBankTransfersService;

	// 特殊拨付
	private static String BUSI_CODE = "061206";

	@SuppressWarnings("unchecked")
	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm) {
		Properties properties = new MyProperties();

		try {
			String workflowEcode = approvalProcessWorkflow.geteCode();

			// 获取正在处理的申请单
			Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();

			// 获取正在处理的申请单所属的流程配置
			Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = sm_ApprovalProcess_AF.getConfiguration();
			String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;

			// 获取正在审批的用款申请
			Long specialFundAppropriated_AFId = sm_ApprovalProcess_AF.getSourceId();
			Tgpf_SpecialFundAppropriated_AF tgpf_SpecialFundAppropriated_AF = tgpf_SpecialFundAppropriated_AFDao
					.findById(specialFundAppropriated_AFId);

			if (tgpf_SpecialFundAppropriated_AF == null) {
				return MyBackInfo.fail(properties, "审批的用款申请不存在");
			}

			Empj_BuildingInfo building = tgpf_SpecialFundAppropriated_AF.getBuilding();

			if (null == building || null == building.getBuildingAccount()) {
				return MyBackInfo.fail(properties, "关联楼幢或楼幢账户为空");
			}

			// 驳回到发起人 ，发起人撤回 ，不通过 - 回写楼幢账户金额
			if (S_ApprovalState.WaitSubmit.equals(sm_ApprovalProcess_AF.getBusiState())) {
				// 1.撤回后申请单状态回复为 1-初始
				tgpf_SpecialFundAppropriated_AF.setBusiState(S_SpecialFundBusiState.Saved);
				tgpf_SpecialFundAppropriated_AFDao.save(tgpf_SpecialFundAppropriated_AF);

				// 对楼幢账户进行计算
				noPassChange(building.getBuildingAccount(), tgpf_SpecialFundAppropriated_AF);

				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "操作成功");

			}

			// 不通过操作
			if (S_ApprovalState.NoPass.equals(sm_ApprovalProcess_AF.getBusiState())) {
				tgpf_SpecialFundAppropriated_AF.setApplyState(S_ApplyState.Rescinded); // 已撤销
				tgpf_SpecialFundAppropriated_AF.setApprovalState(S_ApprovalState.NoPass); // 不通过
				tgpf_SpecialFundAppropriated_AF.setTheState(S_TheState.Deleted); // 逻辑删除
				tgpf_SpecialFundAppropriated_AF.setLastUpdateTimeStamp(System.currentTimeMillis());
				tgpf_SpecialFundAppropriated_AF.setUserUpdate(baseForm.getUser());

				tgpf_SpecialFundAppropriated_AFDao.save(tgpf_SpecialFundAppropriated_AF);

				// 删除明细表
				Tgpf_SpecialFundAppropriated_AFDtlForm dtlForm = new Tgpf_SpecialFundAppropriated_AFDtlForm();
				dtlForm.setTheState(S_TheState.Normal);
				dtlForm.setSpecialAppropriated(tgpf_SpecialFundAppropriated_AF);

				List<Tgpf_SpecialFundAppropriated_AFDtl> list;
				list = tgpf_SpecialFundAppropriated_AFDtlDao.findByPage(tgpf_SpecialFundAppropriated_AFDtlDao
						.getQuery(tgpf_SpecialFundAppropriated_AFDtlDao.getBasicHQL(), dtlForm));

				// 对楼幢账户进行计算
				noPassChange(building.getBuildingAccount(), tgpf_SpecialFundAppropriated_AF);

				if (null != list && list.size() > 0) {
					for (Tgpf_SpecialFundAppropriated_AFDtl dtl : list) {
						dtl.setTheState(S_TheState.Deleted);
						dtl.setLastUpdateTimeStamp(System.currentTimeMillis());
						dtl.setUserUpdate(baseForm.getUser());
						tgpf_SpecialFundAppropriated_AFDtlDao.save(dtl);
					}
				}

				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "操作成功");

			}

			switch (approvalProcessWork) {
			case "061206001_ZS":
				if (S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState())
						&& S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState())) {
					/*
					 * xsz by time 用款申请明细信息
					 */
					String payoutDate = "";
					Tgpf_SpecialFundAppropriated_AFDtlForm dtlForm = new Tgpf_SpecialFundAppropriated_AFDtlForm();
					dtlForm.setTheState(S_TheState.Normal);
					dtlForm.setSpecialAppropriated(tgpf_SpecialFundAppropriated_AF);

					List<Tgpf_SpecialFundAppropriated_AFDtl> list;
					list = tgpf_SpecialFundAppropriated_AFDtlDao.findByPage(tgpf_SpecialFundAppropriated_AFDtlDao
							.getQuery(tgpf_SpecialFundAppropriated_AFDtlDao.getBasicHQL(), dtlForm));

					// 更新拨付信息
					for (Tgpf_SpecialFundAppropriated_AFDtl tgpf_SpecialFundAppropriated_AFDtl : list) {

						/*
						 * xsz by time 2018-12-24 15:37:40 判断是否维护了拨付日期
						 */
						if (null == tgpf_SpecialFundAppropriated_AFDtl.getPayoutDate()
								|| tgpf_SpecialFundAppropriated_AFDtl.getPayoutDate().trim().isEmpty()) {
							return MyBackInfo.fail(properties, "请先维护拨付信息（拨付日期）！");
						}
						payoutDate = tgpf_SpecialFundAppropriated_AFDtl.getPayoutDate();
						/*
						 * 根据拨付金额，从托管账户中减去相应的金额 canPayAmount：托管可拨付
						 * appliedAmount：拨付金额
						 */
						// 本次拨付金额
						Double appliedAmount = tgpf_SpecialFundAppropriated_AFDtl.getAppliedAmount();
						Tgxy_BankAccountEscrowed bankAccountEscrowed = tgpf_SpecialFundAppropriated_AFDtl
								.getBankAccountEscrowed();
						/**
						 * 托管支出 + 活期余额 - 托管可拨付 -
						 */
						// 托管支出
						Double payout = null == bankAccountEscrowed.getPayout() ? 0.00
								: bankAccountEscrowed.getPayout();
						// 活期余额
						Double currentBalance = null == bankAccountEscrowed.getCurrentBalance() ? 0.00
								: bankAccountEscrowed.getCurrentBalance();
						// 托管可拨付金额
						Double canPayAmount = null == bankAccountEscrowed.getCanPayAmount() ? 0.00
								: bankAccountEscrowed.getCanPayAmount();

//						if (canPayAmount < appliedAmount) {
//							properties.put(S_NormalFlag.result, S_NormalFlag.fail);
//							properties.put(S_NormalFlag.info, "托管账户余额不足，请重新划款！");
//
//							return properties;
//						}

						bankAccountEscrowed.setPayout(MyDouble.getInstance().doubleAddDouble(payout, appliedAmount));
						bankAccountEscrowed.setCurrentBalance(
								MyDouble.getInstance().doubleSubtractDouble(currentBalance, appliedAmount));
						// 更新托管账户
						bankAccountEscrowed.setCanPayAmount(
								MyDouble.getInstance().doubleSubtractDouble(canPayAmount, appliedAmount));

						tgxy_BankAccountEscrowedDao.save(bankAccountEscrowed);

						tgpf_SpecialFundAppropriated_AFDtl.setPayoutState(S_SpecialFundApplyState.Appropriated);
						tgpf_SpecialFundAppropriated_AFDtlDao.save(tgpf_SpecialFundAppropriated_AFDtl);
					}

					tgpf_SpecialFundAppropriated_AF.setApplyState(S_ApplyState.Admissible); // 用款申请单状态
					tgpf_SpecialFundAppropriated_AF.setApprovalState(S_ApprovalState.Completed);// 流程状态
					tgpf_SpecialFundAppropriated_AF.setUserRecord(baseForm.getUser());// 备案人
					tgpf_SpecialFundAppropriated_AF.setRecordTimeStamp(System.currentTimeMillis());// 备案日期
					tgpf_SpecialFundAppropriated_AF.setAfPayoutDate(payoutDate);
					tgpf_SpecialFundAppropriated_AFDao.save(tgpf_SpecialFundAppropriated_AF);

					// 对楼幢账户进行计算
					passChange(building.getBuildingAccount(), tgpf_SpecialFundAppropriated_AF);

					// 留存权益计算
					Tgpf_BuildingRemainRightLogForm tgpf_BuildingRemainRightLogForm = new Tgpf_BuildingRemainRightLogForm();
					tgpf_BuildingRemainRightLogForm
							.setBillTimeStamp(MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));// 记账日期（当前日期）
					tgpf_BuildingRemainRightLogForm.setBuildingId(building.getTableId());
					tgpf_BuildingRemainRightLogForm.setSrcBusiType("入账");

					tgpf_BuildingRemainRightLogPublicAdd.execute(tgpf_BuildingRemainRightLogForm);

				}

				break;

			case "061206001_2":

				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "操作成功");

				if (S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState())) {
					/*
					 * xsz by time 2018-12-24 10:01:35 财务统筹审批即维护托管账号信息的审批节点
					 * 需要校验信息是否正常才能通过审批，即只有提交过后才能通过审批
					 */
					Tgpf_SpecialFundAppropriated_AFDtlForm dtlForm = new Tgpf_SpecialFundAppropriated_AFDtlForm();
					dtlForm.setTheState(S_TheState.Normal);
					dtlForm.setSpecialAppropriated(tgpf_SpecialFundAppropriated_AF);

					List<Tgpf_SpecialFundAppropriated_AFDtl> list;
					list = tgpf_SpecialFundAppropriated_AFDtlDao.findByPage(tgpf_SpecialFundAppropriated_AFDtlDao
							.getQuery(tgpf_SpecialFundAppropriated_AFDtlDao.getBasicHQL(), dtlForm));
					if (null == list || list.size() < 1) {
						return MyBackInfo.fail(properties, "请先维护划款信息！");
					}

				}

				break;

			case "061206001_3":
				
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "操作成功");

				if (S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState())) {
					Tgpf_SpecialFundAppropriated_AFDtlForm dtlForm = new Tgpf_SpecialFundAppropriated_AFDtlForm();
					dtlForm.setTheState(S_TheState.Normal);
					dtlForm.setSpecialAppropriated(tgpf_SpecialFundAppropriated_AF);

					List<Tgpf_SpecialFundAppropriated_AFDtl> list;
					list = tgpf_SpecialFundAppropriated_AFDtlDao.findByPage(tgpf_SpecialFundAppropriated_AFDtlDao
							.getQuery(tgpf_SpecialFundAppropriated_AFDtlDao.getBasicHQL(), dtlForm));

					if (null != list && list.size() > 0) {
						// 是否推送
						boolean isPush = true;
						Tgxy_BankAccountEscrowed bankAccountEscrowed;
						for (Tgpf_SpecialFundAppropriated_AFDtl dtl : list) {
							bankAccountEscrowed = dtl.getBankAccountEscrowed();
							if (null == bankAccountEscrowed) {
								return MyBackInfo.fail(properties, "请先维护划款账户信息！");
							}

							if (!(1 == bankAccountEscrowed.getBankBranch().getIsDocking())) {
								isPush = false;
								break;
							}

						}

						// 进行推送
						if (isPush) {
							properties = batchBankTransfersService.execute("061206",
									tgpf_SpecialFundAppropriated_AF.getTableId(),
									sm_ApprovalProcess_AF.getCurrentIndex(), baseForm);
						}
					}

				}

				break;

			default:
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "没有需要处理的回调");
			}
		} catch (Exception e) {
			e.printStackTrace();
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_BusiError);
		}

		return properties;
	}

	public void noPassChange(Tgpj_BuildingAccount buildingAccount, Tgpf_SpecialFundAppropriated_AF AF) {
		// 不发生修改的字段
		Tgpj_BuildingAccountLog tgpj_BuildingAccountLog = new Tgpj_BuildingAccountLog();
		tgpj_BuildingAccountLog.setTheState(S_TheState.Normal);
		tgpj_BuildingAccountLog.setBusiState("0");
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
		if (null == allocableAmount || allocableAmount < 0) {
			allocableAmount = 0.00;
		}
		Double appliedNoPayoutAmount = buildingAccount.getAppliedNoPayoutAmount();
		if (null == appliedNoPayoutAmount || appliedNoPayoutAmount < 0) {
			appliedNoPayoutAmount = 0.00;
		}
		Double appropriateFrozenAmount = buildingAccount.getAppropriateFrozenAmount();
		if (null == appropriateFrozenAmount || appropriateFrozenAmount < 0) {
			appropriateFrozenAmount = 0.00;
		}

		allocableAmount = MyDouble.getInstance().doubleAddDouble(allocableAmount, AF.getTotalApplyAmount());
		appliedNoPayoutAmount = MyDouble.getInstance().doubleSubtractDouble(appliedNoPayoutAmount,
				AF.getTotalApplyAmount());
		appropriateFrozenAmount = MyDouble.getInstance().doubleSubtractDouble(appropriateFrozenAmount,
				AF.getTotalApplyAmount());

		// 可划拨金额（元）：allocableAmount +
		tgpj_BuildingAccountLog.setAllocableAmount(allocableAmount);
		// 已申请未拨付金额（元）：appliedNoPayoutAmount -
		tgpj_BuildingAccountLog.setAppliedNoPayoutAmount(appliedNoPayoutAmount);
		// 拨付冻结金额 ：appropriateFrozenAmount -
		tgpj_BuildingAccountLog.setAppropriateFrozenAmount(appropriateFrozenAmount);

		tgpj_BuildingAccountLimitedUpdateService.execute(tgpj_BuildingAccountLog);

	}

	public void passChange(Tgpj_BuildingAccount buildingAccount, Tgpf_SpecialFundAppropriated_AF AF) {
		// 不发生修改的字段
		Tgpj_BuildingAccountLog tgpj_BuildingAccountLog = new Tgpj_BuildingAccountLog();
		tgpj_BuildingAccountLog.setTheState(S_TheState.Normal);
		tgpj_BuildingAccountLog.setBusiState("1");
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
		if (null == allocableAmount || allocableAmount < 0) {
			allocableAmount = 0.00;
		}
		Double appliedNoPayoutAmount = buildingAccount.getAppliedNoPayoutAmount();
		if (null == appliedNoPayoutAmount || appliedNoPayoutAmount < 0) {
			appliedNoPayoutAmount = 0.00;
		}
		Double appropriateFrozenAmount = buildingAccount.getAppropriateFrozenAmount();
		if (null == appropriateFrozenAmount || appropriateFrozenAmount < 0) {
			appropriateFrozenAmount = 0.00;
		}
		Double payoutAmount = buildingAccount.getPayoutAmount();
		if (null == payoutAmount || payoutAmount < 0) {
			payoutAmount = 0.00;
		}

		// 已拨付金额 +
		payoutAmount = MyDouble.getInstance().doubleAddDouble(payoutAmount, AF.getTotalApplyAmount());
		// 已申请未拨付金额（元） -
		appliedNoPayoutAmount = MyDouble.getInstance().doubleSubtractDouble(appliedNoPayoutAmount,
				AF.getTotalApplyAmount());
		// 拨付冻结金额 ：appropriateFrozenAmount -
		appropriateFrozenAmount = MyDouble.getInstance().doubleSubtractDouble(appropriateFrozenAmount,
				AF.getTotalApplyAmount());

		// 已拨付金额 +
		tgpj_BuildingAccountLog.setPayoutAmount(payoutAmount);
		// 已申请未拨付金额（元）：appliedNoPayoutAmount -
		tgpj_BuildingAccountLog.setAppliedNoPayoutAmount(appliedNoPayoutAmount);
		// 拨付冻结金额 ：appropriateFrozenAmount -
		tgpj_BuildingAccountLog.setAppropriateFrozenAmount(appropriateFrozenAmount);

		tgpj_BuildingAccountLimitedUpdateService.execute(tgpj_BuildingAccountLog);
	}
}
