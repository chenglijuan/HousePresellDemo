package zhishusz.housepresell.approvalprocess;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Tgpf_RemainRightForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountLogForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.dao.Tgpf_RefundInfoDao;
import zhishusz.housepresell.database.dao.Tgpf_RemainRightDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountLogDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.database.po.Tgpf_RemainRight;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.external.service.BatchBankTransfersService;
import zhishusz.housepresell.service.Tgpj_BuildingAccountLimitedUpdateService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;

/*
 * Service备案回写操作：资金管理-退房退款
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class ApprovalProcessCallBack_06120201 implements IApprovalProcessCallback
{
	@Autowired
	private Tgpf_RefundInfoDao tgpf_RefundInfoDao;
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;
	@Autowired
	private Tgpj_BuildingAccountLogDao tgpj_BuildingAccountLogDao;
	@Autowired
	private Tgpj_BuildingAccountLimitedUpdateService tgpj_BuildingAccountLimitedUpdateService;
	@Autowired
	private Tgpf_RemainRightDao tgpf_RemainRightDao;
	@Autowired
	private BatchBankTransfersService batchBankTransfersService;

	@Override
	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm)
	{
		Properties properties = new Properties();

		String workflowEcode = approvalProcessWorkflow.geteCode();

		// 获取正在处理的申请单
		Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();

		// 获取正在处理的申请单所属的流程配置
		Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = sm_ApprovalProcess_AF.getConfiguration();

		String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;

		// 获取单据Id
		Long tableId = sm_ApprovalProcess_AF.getSourceId();

		// 查询退房退款信息
		Tgpf_RefundInfo tgpf_RefundInfo = tgpf_RefundInfoDao.findById(tableId);
		if (tgpf_RefundInfo == null)
		{
			return MyBackInfo.fail(properties, "未获取到有效的退房退款数据！");
		}

		String busiCode = tgpf_RefundInfo.geteCode().split("N")[0];

		// 查询楼幢账户
		Empj_BuildingInfo building = tgpf_RefundInfo.getBuilding();

		if (building == null)
		{
			return MyBackInfo.fail(properties, "未查询到楼幢信息");
		}

		Tgpj_BuildingAccountForm form = new Tgpj_BuildingAccountForm();
		form.setTheState(0);
		form.setBuilding(building);

		Tgpj_BuildingAccount tgpj_BuildingAccount = tgpj_BuildingAccountDao
				.findOneByQuery_T(tgpj_BuildingAccountDao.getQuery(tgpj_BuildingAccountDao.getBasicHQL(), form));

		if (null == tgpj_BuildingAccount)
		{
			return MyBackInfo.fail(properties, "未查询到有效的楼幢托管账户信息");
		}

		// 驳回到发起人 ，发起人撤销
		if (S_ApprovalState.WaitSubmit.equals(sm_ApprovalProcess_AF.getBusiState()))
		{
			tgpf_RefundInfo.setBusiState(S_ApprovalState.WaitSubmit);
			tgpf_RefundInfoDao.save(tgpf_RefundInfo);
		}

		// 不通过
		if (S_ApprovalState.NoPass.equals(sm_ApprovalProcess_AF.getBusiState()))
		{
			// tgpj_BuildingAccount.setVersionNo(tgpj_BuildingAccount.getVersionNo()+1);

			// 已退款金额
			Double refundAmount = tgpj_BuildingAccount.getRefundAmount();

			/*
			 * 驳回到发起人后更新<楼幢账户>：减少“已申请退款未拨付金额（元）
			 */
			// 已申请退款未拨付金额
			Double applyRefundPayoutAmount = tgpj_BuildingAccount.getApplyRefundPayoutAmount();
			// 获取实际退款金额
			Double actualRefundAmount = tgpf_RefundInfo.getActualRefundAmount();

			applyRefundPayoutAmount = MyDouble.getInstance().doubleSubtractDouble(applyRefundPayoutAmount,
					actualRefundAmount);

			setChange(properties, refundAmount, applyRefundPayoutAmount, tgpj_BuildingAccount, tgpf_RefundInfo,
					busiCode);
			
			//重新计算户入账总金额
			Tgxy_TripleAgreementForm tripleAgreementForm = new Tgxy_TripleAgreementForm();
			tripleAgreementForm.setTheState(S_TheState.Normal);
			tripleAgreementForm.seteCodeOfTripleAgreement(tgpf_RefundInfo.geteCodeOfTripleAgreement());
			Tgxy_TripleAgreement tripleAgreement = tgxy_TripleAgreementDao
					.findOneByQuery_T(tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getBasicHQL(), tripleAgreementForm));
			if (null == tripleAgreement)
			{
				return MyBackInfo.fail(properties, "未关联到有效的三方协议信息");
			}
			Double totalAmountOfHouse = tripleAgreement.getTotalAmountOfHouse();// 户入账总金额
			if (null == totalAmountOfHouse)
			{
				totalAmountOfHouse = 0.00;
			}
			totalAmountOfHouse = MyDouble.getInstance().doubleAddDouble(totalAmountOfHouse, refundAmount);
			tripleAgreement.setTotalAmountOfHouse(totalAmountOfHouse);
			tgxy_TripleAgreementDao.update(tripleAgreement);
		}

		//退款账号维护判断  统筹角色节点编码
		Integer action = baseForm.getTheAction();//获取审批状态
		if(null != action && 0 == action && approvalProcessWork.equals("06120201001_TC")){
			String refundBankAccount = tgpf_RefundInfo.getRefundBankAccount();//退款账号
			String refundBankName = tgpf_RefundInfo.getRefundBankName();//退款银行
			if(null == refundBankAccount || null == refundBankName){
				return MyBackInfo.fail(properties, "退款信息为空，请维护退款账号和退款银行之后并点击保存按钮");
			}
		}
		
		if(null != action && 0 == action && approvalProcessWork.equals("06120201001_ZS")){
			String refundTimeStamp = tgpf_RefundInfo.getRefundTimeStamp();//退款日期
			if(null == refundTimeStamp || null == refundTimeStamp){
				return MyBackInfo.fail(properties, "退款信息为空，请维护退款日期之后并点击保存按钮");
			}
		}
		
		if (approvalProcessWork.equals("06120201001_ZS"))
		{
			if (S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState())
					&& S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
			{
				// 获取实际退款金额
				Double actualRefundAmount = tgpf_RefundInfo.getActualRefundAmount();
				/*
				 * 更新托管账户表
				 * 计算：增加托管支出 减少活期余额
				 */
				Tgxy_BankAccountEscrowed theBankAccountEscrowed = tgpf_RefundInfo.getTheBankAccountEscrowed();

				if (theBankAccountEscrowed == null)
				{
					return MyBackInfo.fail(properties, "未查询到银行账户信息，请核对退款银行和退款账号信息。");
				}

				// 托管支出
				Double payout = theBankAccountEscrowed.getPayout();
				// 活期余额
				Double currentBalance = theBankAccountEscrowed.getCurrentBalance();
				
				// 托管可拨付金额
				Double canPayAmount = theBankAccountEscrowed.getCanPayAmount();
				
				/**
				 * 托管支出 +
				 * 活期余额 -
				 * 托管可拨付 -
				 */
				
//				if(null == canPayAmount)
//				{
//					canPayAmount = 0.00;
//				}
//
//				canPayAmount = MyDouble.getInstance().doubleSubtractDouble(canPayAmount, actualRefundAmount);
//
//				if (null == canPayAmount || 0 > canPayAmount)
//				{
//					canPayAmount = 0.00;
//				}
//
//				theBankAccountEscrowed.setCanPayAmount(canPayAmount);
//
//				if (null == payout || payout == 0)
//				{
//					payout = actualRefundAmount;
//				}
//				else
//				{
//					payout = MyDouble.getInstance().doubleAddDouble(payout, actualRefundAmount);
//				}
//
//				if (null == currentBalance)
//				{
//					currentBalance = 0.00;
//				}
//
//				currentBalance = MyDouble.getInstance().doubleSubtractDouble(currentBalance, actualRefundAmount);
//
//				if (null == currentBalance || 0 > currentBalance)
//				{
//					currentBalance = 0.00;
//				}



				if(null == canPayAmount)
				{
					canPayAmount = 0.00;
				}

				canPayAmount = MyDouble.getInstance().doubleSubtractDouble(canPayAmount, actualRefundAmount);

				if (null == canPayAmount)
				{
					canPayAmount = 0.00;
				}

				theBankAccountEscrowed.setCanPayAmount(canPayAmount);

				if (null == payout || payout == 0)
				{
					payout = actualRefundAmount;
				}
				else
				{
					payout = MyDouble.getInstance().doubleAddDouble(payout, actualRefundAmount);
				}

				if (null == currentBalance)
				{
					currentBalance = 0.00;
				}

				currentBalance = MyDouble.getInstance().doubleSubtractDouble(currentBalance, actualRefundAmount);

				if (null == currentBalance)
				{
					currentBalance = 0.00;
				}


				theBankAccountEscrowed.setPayout(payout);
				theBankAccountEscrowed.setCurrentBalance(currentBalance);

				tgxy_BankAccountEscrowedDao.save(theBankAccountEscrowed);

				/*
				 * 更新楼幢账户表
				 * 计算：增加“已退款金额（元）”、减少“已申请退款未拨付金额（元）”
				 */
				// 已退款金额
				Double refundAmount = tgpj_BuildingAccount.getRefundAmount();
				// 已申请退款未拨付金额（元）
				Double applyRefundPayoutAmount = tgpj_BuildingAccount.getApplyRefundPayoutAmount();

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

				properties = setChange(properties, refundAmount, applyRefundPayoutAmount, tgpj_BuildingAccount,
						tgpf_RefundInfo, busiCode);

				// 查询三方协议
				Tgxy_TripleAgreement tgxy_TripleAgreement = tgpf_RefundInfo.getTripleAgreement();
				if (tgxy_TripleAgreement == null)
				{
					return MyBackInfo.fail(properties, "未查询到有效的三方协议数据！");
				}

				tgxy_TripleAgreement.setTheStateOfTripleAgreementEffect("3");
				
				tgxy_TripleAgreement.setTheAmountOfRetainedEquity(0.00);
				tgxy_TripleAgreement.setTheAmountOfInterestRetained(0.00);
				tgxy_TripleAgreement.setTheAmountOfInterestUnRetained(0.00);
				tgxy_TripleAgreement.setTotalAmountOfHouse(0.00);
				
				tgxy_TripleAgreementDao.save(tgxy_TripleAgreement);
				
				
				// 维护退房退款信息-退款日期
				// tgpf_RefundInfo.setRefundTimeStamp(MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
				tgpf_RefundInfo.setBusiState(S_BusiState.HaveRecord);
				tgpf_RefundInfo.setApprovalState(S_ApprovalState.Completed);

				tgpf_RefundInfoDao.save(tgpf_RefundInfo);
				
				/**
				 * xsz by time 2019-2-18 17:50:21
				 * 通过三方协议查询对应的留存权益信息并更新
				 */
//				Tgpf_RemainRightForm tgpf_RemainRightModel = new Tgpf_RemainRightForm();
//				tgpf_RemainRightModel.setTheState(S_TheState.Normal);
//				tgpf_RemainRightModel.seteCodeOfTripleAgreement(tgxy_TripleAgreement.geteCodeOfTripleAgreement());
//				
//				List<Tgpf_RemainRight> tgpf_RemainRightList;
//				tgpf_RemainRightList = tgpf_RemainRightDao.findByPage(tgpf_RemainRightDao.getQuery(tgpf_RemainRightDao.getBasicHQL(), tgpf_RemainRightModel));
//				if(null != tgpf_RemainRightList && tgpf_RemainRightList.size()>0)
//				{
//					for (Tgpf_RemainRight tgpf_RemainRight : tgpf_RemainRightList)
//					{
//						tgpf_RemainRight.setTheAmount(0.00);
//						tgpf_RemainRight.setLimitedRetainRight(0.00);
//						tgpf_RemainRight.setWithdrawableRetainRight(0.00);
//						
//						tgpf_RemainRightDao.save(tgpf_RemainRight);
//					}
//				}

				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
			}
			else
			{
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
			}

		}
		else if(approvalProcessWork.equals("06120201001_4"))
		{
			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
			
			if (S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
			{
				/*
				 * 校验托管账户的银行是否
				 * 判断是否是资金系统支持的开户行
				 */
				Tgxy_BankAccountEscrowed theBankAccountEscrowed = tgpf_RefundInfo.getTheBankAccountEscrowed();
				if (theBankAccountEscrowed == null)
				{
					return MyBackInfo.fail(properties, "未查询到银行账户信息，请核对退款银行和退款账号信息。");
				}
				
				Emmp_BankBranch bankBranch = theBankAccountEscrowed.getBankBranch();
				if(1 == bankBranch.getIsDocking()){
					properties = batchBankTransfersService.execute(busiCode, tableId, sm_ApprovalProcess_AF.getCurrentIndex(), baseForm);
				}
			}
			
			
		}		
		else
		{
			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, "没有需要处理的回调");
		}

		return properties;
	}

	/**
	 * 保存修改文件
	 * 
	 * @param properties
	 * @param refundAmount
	 *            实际退款金额
	 * @param applyRefundPayoutAmount
	 *            已申请退款未拨付金额
	 * @param tgpj_BuildingAccount
	 * @param tgpf_RefundInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Properties setChange(Properties properties, Double refundAmount, Double applyRefundPayoutAmount,
			Tgpj_BuildingAccount tgpj_BuildingAccount, Tgpf_RefundInfo tgpf_RefundInfo, String busiCode)
	{

		Long accountVersion = tgpj_BuildingAccount.getVersionNo();
		if (null == tgpj_BuildingAccount.getVersionNo() || tgpj_BuildingAccount.getVersionNo() < 0)
		{
			accountVersion = 1l;
		}

		// 根据楼幢账户查询版本号最大的楼幢账户log表
		// 查询条件：1.业务编码 2.楼幢账户 3.关联主键 4.根据版本号大小排序
		Tgpj_BuildingAccountLogForm tgpj_BuildingAccountLogForm = new Tgpj_BuildingAccountLogForm();
		tgpj_BuildingAccountLogForm.setTheState(S_TheState.Normal);
		tgpj_BuildingAccountLogForm.setRelatedBusiCode(busiCode);
		tgpj_BuildingAccountLogForm.setTgpj_BuildingAccount(tgpj_BuildingAccount);
		tgpj_BuildingAccountLogForm.setRelatedBusiTableId(tgpf_RefundInfo.getTableId());

		Integer logCount = tgpj_BuildingAccountLogDao.findByPage_Size(tgpj_BuildingAccountLogDao
				.getQuery_Size(tgpj_BuildingAccountLogDao.getSpecialHQL(), tgpj_BuildingAccountLogForm));

		List<Tgpj_BuildingAccountLog> tgpj_BuildingAccountLogList;
		if (logCount > 0)
		{
			tgpj_BuildingAccountLogList = tgpj_BuildingAccountLogDao.findByPage(tgpj_BuildingAccountLogDao
					.getQuery(tgpj_BuildingAccountLogDao.getBasicHQL(), tgpj_BuildingAccountLogForm));

			Tgpj_BuildingAccountLog buildingAccountLog = tgpj_BuildingAccountLogList.get(0);
			// 获取日志表的版本号
			Long logVersionNo = buildingAccountLog.getVersionNo();
			if (null == buildingAccountLog.getVersionNo() || buildingAccountLog.getVersionNo() < 0)
			{
				logVersionNo = 1l;
			}
			if (logVersionNo > accountVersion)
			{
				return MyBackInfo.fail(properties, "备案版本存在回档，请核实后重新发起！");
			}
			else
			{
				// if(logVersionNo == accountVersion)
				// {
				// tgpj_BuildingAccountLimitedUpdateService.execute(buildingAccountLog);
				// }
				// else if(logVersionNo < accountVersion)
				// {
				// 不发生修改的字段
				Tgpj_BuildingAccountLog tgpj_BuildingAccountLog = new Tgpj_BuildingAccountLog();
				tgpj_BuildingAccountLog.setBldLimitAmountVerDtl(tgpj_BuildingAccount.getBldLimitAmountVerDtl());
				tgpj_BuildingAccountLog.setTheState(S_TheState.Normal);
				tgpj_BuildingAccountLog.setBusiState(tgpj_BuildingAccount.getBusiState());
				tgpj_BuildingAccountLog.seteCode(tgpj_BuildingAccount.geteCode());
				tgpj_BuildingAccountLog.setUserStart(tgpj_BuildingAccount.getUserStart());
				tgpj_BuildingAccountLog.setCreateTimeStamp(tgpj_BuildingAccount.getCreateTimeStamp());
				tgpj_BuildingAccountLog.setUserUpdate(tgpj_BuildingAccount.getUserUpdate());
				tgpj_BuildingAccountLog.setLastUpdateTimeStamp(System.currentTimeMillis());
				tgpj_BuildingAccountLog.setUserRecord(tgpj_BuildingAccount.getUserRecord());
				tgpj_BuildingAccountLog.setRecordTimeStamp(tgpj_BuildingAccount.getRecordTimeStamp());
				tgpj_BuildingAccountLog.setDevelopCompany(tgpj_BuildingAccount.getDevelopCompany());
				tgpj_BuildingAccountLog.seteCodeOfDevelopCompany(tgpj_BuildingAccount.geteCodeOfDevelopCompany());
				tgpj_BuildingAccountLog.setProject(tgpj_BuildingAccount.getProject());
				tgpj_BuildingAccountLog.setTheNameOfProject(tgpj_BuildingAccount.getTheNameOfProject());
				tgpj_BuildingAccountLog.setBuilding(tgpj_BuildingAccount.getBuilding());
				tgpj_BuildingAccountLog.setPayment(tgpj_BuildingAccount.getPayment());
				tgpj_BuildingAccountLog.seteCodeOfBuilding(tgpj_BuildingAccount.geteCodeOfBuilding());
				tgpj_BuildingAccountLog.setEscrowStandard(tgpj_BuildingAccount.getEscrowStandard());
				tgpj_BuildingAccountLog.setEscrowArea(tgpj_BuildingAccount.getEscrowArea());
				tgpj_BuildingAccountLog.setBuildingArea(tgpj_BuildingAccount.getBuildingArea());
				tgpj_BuildingAccountLog.setOrgLimitedAmount(tgpj_BuildingAccount.getOrgLimitedAmount());
				tgpj_BuildingAccountLog.setCurrentFigureProgress(tgpj_BuildingAccount.getCurrentFigureProgress());
				tgpj_BuildingAccountLog.setCurrentLimitedRatio(tgpj_BuildingAccount.getCurrentLimitedRatio());
				tgpj_BuildingAccountLog.setNodeLimitedAmount(tgpj_BuildingAccount.getNodeLimitedAmount());
				tgpj_BuildingAccountLog.setTotalGuaranteeAmount(tgpj_BuildingAccount.getTotalGuaranteeAmount());
				tgpj_BuildingAccountLog.setCashLimitedAmount(tgpj_BuildingAccount.getCashLimitedAmount());
				tgpj_BuildingAccountLog.setTotalAccountAmount(tgpj_BuildingAccount.getTotalAccountAmount());
				tgpj_BuildingAccountLog.setPayoutAmount(tgpj_BuildingAccount.getPayoutAmount());
				tgpj_BuildingAccountLog.setAppliedNoPayoutAmount(tgpj_BuildingAccount.getAppliedNoPayoutAmount());
				tgpj_BuildingAccountLog.setCurrentEscrowFund(tgpj_BuildingAccount.getCurrentEscrowFund());
				tgpj_BuildingAccountLog.setRecordAvgPriceOfBuildingFromPresellSystem(
						tgpj_BuildingAccount.getRecordAvgPriceOfBuildingFromPresellSystem());
				tgpj_BuildingAccountLog.setRecordAvgPriceOfBuilding(tgpj_BuildingAccount.getRecordAvgPriceOfBuilding());
				tgpj_BuildingAccountLog.setLogId(tgpj_BuildingAccount.getLogId());
				tgpj_BuildingAccountLog.setActualAmount(tgpj_BuildingAccount.getActualAmount());
				tgpj_BuildingAccountLog.setPaymentLines(tgpj_BuildingAccount.getPaymentLines());
				tgpj_BuildingAccountLog.setRelatedBusiCode(busiCode);
				tgpj_BuildingAccountLog.setRelatedBusiTableId(tgpf_RefundInfo.getTableId());
				tgpj_BuildingAccountLog.setTgpj_BuildingAccount(tgpj_BuildingAccount);
				tgpj_BuildingAccountLog.setVersionNo(tgpj_BuildingAccount.getVersionNo());
				tgpj_BuildingAccountLog.setPaymentProportion(tgpj_BuildingAccount.getPaymentProportion());
				tgpj_BuildingAccountLog.setBuildAmountPaid(tgpj_BuildingAccount.getBuildAmountPaid());
				tgpj_BuildingAccountLog.setBuildAmountPay(tgpj_BuildingAccount.getBuildAmountPay());
				tgpj_BuildingAccountLog.setTotalAmountGuaranteed(tgpj_BuildingAccount.getTotalAmountGuaranteed());
				tgpj_BuildingAccountLog.setCashLimitedAmount(tgpj_BuildingAccount.getCashLimitedAmount());
				tgpj_BuildingAccountLog.setEffectiveLimitedAmount(tgpj_BuildingAccount.getEffectiveLimitedAmount());
				tgpj_BuildingAccountLog.setSpilloverAmount(tgpj_BuildingAccount.getSpilloverAmount());
				tgpj_BuildingAccountLog.setAllocableAmount(tgpj_BuildingAccount.getAllocableAmount());
				tgpj_BuildingAccountLog.setAppropriateFrozenAmount(tgpj_BuildingAccount.getAppropriateFrozenAmount());

				// 修改产生了变更的字段
				tgpj_BuildingAccountLog.setRefundAmount(refundAmount);
				tgpj_BuildingAccountLog.setApplyRefundPayoutAmount(applyRefundPayoutAmount);

				tgpj_BuildingAccountLogDao.save(tgpj_BuildingAccountLog);

				tgpj_BuildingAccountLimitedUpdateService.execute(tgpj_BuildingAccountLog);
			}
			/*
			 * else if (logVersionNo > accountVersion)
			 * {
			 * return MyBackInfo.fail(properties, "备案版本存在回档，请核实后重新发起！");
			 * }
			 */
		}
		else
		{
			return MyBackInfo.fail(properties, "存在未申请备案的楼幢，请核实后重新发起！");
		}
		return properties;
	}
}
