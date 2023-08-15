package zhishusz.housepresell.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_BuildingRemainRightLogForm;
import zhishusz.housepresell.controller.form.Tgpf_DayEndBalancingForm;
import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_DayEndBalancingDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_DayEndBalancing;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * 业务对账-日终结算-后台任务
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_DayEndBalancingManualTaskServiceCopy
{
	@Autowired
	private Tgpf_DayEndBalancingDao tgpf_DayEndBalancingDao;
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;		//资金归集明细
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao; // 楼幢信息表
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;// 楼幢账户
	@Autowired
	private Tgpf_BuildingRemainRightLogPublicAddService tgpf_BuildingRemainRightLogPublicAdd;// 留存权益计算方法
	@Autowired
	private Tgpj_BuildingAccountLimitedUpdateService tgpj_BuildingAccountLimitedUpdateService;
	

	MyDatetime myDatetime = MyDatetime.getInstance();
	
	MyDouble myDouble = MyDouble.getInstance();
	
	private static final String BUSI_CODE = "200204";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"

	
	public Properties execute(Tgpf_DayEndBalancingForm model)
	{
		
		Properties properties = new MyProperties();

		Sm_User userStart = model.getUser(); // admin
		
		String currentTime = myDatetime.dateToSimpleString(System.currentTimeMillis());
		// 记账日期
		String billTimeStamp = model.getBillTimeStamp();
		
		Map<String,Double> buildingMap = new HashMap<String,Double>();
		
		Long[] idArr = model.getIdArr();
		
		if( null == idArr || idArr.length < 0)
		{
			return MyBackInfo.fail(properties, "请选择需要日终结算的银行！");
		}
		
		for(int i = 0;i<idArr.length; i++)
		{
			Long tgpf_DayEndBalancingId = idArr[i];
			
			Tgpf_DayEndBalancing tgpf_DayEndBalancing = (Tgpf_DayEndBalancing)tgpf_DayEndBalancingDao.findById(tgpf_DayEndBalancingId);
			
			if(tgpf_DayEndBalancing == null || S_TheState.Deleted.equals(tgpf_DayEndBalancing.getTheState()))
			{
				return MyBackInfo.fail(properties, "有不存在的记录！");
			}
			
			if(tgpf_DayEndBalancing.getRecordState() != 1 || tgpf_DayEndBalancing.getSettlementState() != 1)
			{
				continue;
			}
			
			// 今天的网银对账金额
			Double totalAmount = tgpf_DayEndBalancing.getTotalAmount();

			Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowed = tgpf_DayEndBalancing.getTgxy_BankAccountEscrowed();

			String theAccount = tgxy_BankAccountEscrowed.getTheAccount();
			
			billTimeStamp = tgpf_DayEndBalancing.getBillTimeStamp();

//			// 根据托管账户查询出三方协议
//			// 查询条件：1.托管账户 2. 状态：正常
//
//			Tgxy_TripleAgreementForm tgxy_TripleAgreementForm = new Tgxy_TripleAgreementForm();
//			tgxy_TripleAgreementForm.setTgxy_BankAccountEscrowed(tgxy_BankAccountEscrowed);
//			tgxy_TripleAgreementForm.setTheState(S_TheState.Normal);
//
//			Integer tripleAgreemenCount = tgxy_TripleAgreementDao.findByPage_Size(tgxy_TripleAgreementDao
//					.getQuery_Size(tgxy_TripleAgreementDao.getBasicHQL(), tgxy_TripleAgreementForm));
//
//			List<Tgxy_TripleAgreement> tgxy_TripleAgreementList;
//			if (tripleAgreemenCount > 0)
//			{
//				tgxy_TripleAgreementList = tgxy_TripleAgreementDao.findByPage(tgxy_TripleAgreementDao
//						.getQuery(tgxy_TripleAgreementDao.getBasicHQL(), tgxy_TripleAgreementForm));
//
//				// 从三方协议中取出户信息
//				// 更新户信息中的入账金额
//				for (Tgxy_TripleAgreement tgxy_TripleAgreement : tgxy_TripleAgreementList)
//				{
			
			// 查询交易明细表中，是否全部业务对账成功
			// 查询条件：1.记账日期 2.三方协议 3.状态：正常 4.网银对账完成
			Tgpf_DepositDetailForm depositDetailForm = new Tgpf_DepositDetailForm();
			depositDetailForm.setTheState(S_TheState.Normal); // 状态为正常
			depositDetailForm.setBillTimeStamp(billTimeStamp);
			depositDetailForm.setTheAccountOfBankAccountEscrowed(theAccount);
			depositDetailForm.setReconciliationStateFromCyberBank(1);
			
			Integer depositCount = tgpf_DepositDetailDao.findByPage_Size(tgpf_DepositDetailDao.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));

			List<Tgpf_DepositDetail> tgpf_DepositDetailList;
			if(depositCount > 0)
			{
				tgpf_DepositDetailList = tgpf_DepositDetailDao.findByPage(tgpf_DepositDetailDao.getQuery(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));
				
				for(Tgpf_DepositDetail tgpf_DepositDetail : tgpf_DepositDetailList)
				{				
					Tgxy_TripleAgreement tgxy_TripleAgreement = tgpf_DepositDetail.getTripleAgreement();
					
					Double loanAmountFromBank = tgpf_DepositDetail.getLoanAmountFromBank();
					
					Double totalAmountOfHouse = 0.0;// 户入账总金额

					if (null != tgxy_TripleAgreement.getTotalAmountOfHouse())
					{
						totalAmountOfHouse = tgxy_TripleAgreement.getTotalAmountOfHouse();
					}

					totalAmountOfHouse = myDouble.doubleAddDouble(loanAmountFromBank, totalAmountOfHouse);

					// 更新户入账金额
					tgxy_TripleAgreement.setTotalAmountOfHouse(totalAmountOfHouse);
					tgxy_TripleAgreement.setTotalAmount(totalAmountOfHouse);
					tgxy_TripleAgreementDao.save(tgxy_TripleAgreement);
					
					tgpf_DepositDetail.setLastUpdateTimeStamp(System.currentTimeMillis());
					tgpf_DepositDetail.setDayEndBalancing(tgpf_DayEndBalancing);
					tgpf_DepositDetailDao.save(tgpf_DepositDetail);
					
					Empj_BuildingInfo empj_BuildingInfo = tgxy_TripleAgreement.getBuildingInfo();
					String buildingTable = empj_BuildingInfo.getTableId().toString();

					if (buildingMap.containsKey(buildingTable))
					{
						Double buildingAmount = buildingMap.get(buildingTable);

						buildingMap.put(buildingTable, myDouble.doubleAddDouble(buildingAmount, loanAmountFromBank));

					}
					else
					{
						buildingMap.put(buildingTable, loanAmountFromBank);
					}					
				}			
			}
			else
			{
				continue;
			}
			
					// 查询中心业务总额度
//					String queryCenterAmountCondition = " nvl(sum(loanAmountFromBank),0) ";
//
//					Double centerTotalAmount = (Double) tgpf_DepositDetailDao
//							.findOneByQuery(tgpf_DepositDetailDao.getSpecialQuery(tgpf_DepositDetailDao.getBasicHQL(),
//									depositDetailForm, queryCenterAmountCondition));
//
//					// 如果额度 <=0 ,则不算
//					if (centerTotalAmount <= 0)
//					{
//						continue;
//					}			
//				}
//			}
			
			// 更新托管账户中的大额占比等数据
			// 托管收入、活期余额、大额占比、大额+活期占比、理财占比、总资金沉淀占比等
			Double income = 0.0;// 托管收入
			Double payout = 0.0;// 托管支出
			Double certOfDeposit = 0.0;// 大额存单
			Double structuredDeposit = 0.0;// 结构性存款
			Double breakEvenFinancial = 0.0;// 保本理财
			
			if(null != tgxy_BankAccountEscrowed.getIncome())
			{
				income = tgxy_BankAccountEscrowed.getIncome();
			}
			if(null != tgxy_BankAccountEscrowed.getPayout())
			{
				payout = tgxy_BankAccountEscrowed.getPayout(); 
			}
			if(null != tgxy_BankAccountEscrowed.getCertOfDeposit())
			{
				certOfDeposit = tgxy_BankAccountEscrowed.getCertOfDeposit(); 
			}
			if(null != tgxy_BankAccountEscrowed.getStructuredDeposit())
			{
				structuredDeposit = tgxy_BankAccountEscrowed.getStructuredDeposit(); 
			}
			if(null != tgxy_BankAccountEscrowed.getBreakEvenFinancial())
			{
				breakEvenFinancial = tgxy_BankAccountEscrowed.getBreakEvenFinancial(); 
			}
			
			//托管可拨付
			Double canPayAmount = null == tgxy_BankAccountEscrowed.getCanPayAmount()?0.00:tgxy_BankAccountEscrowed.getCanPayAmount();
			canPayAmount = myDouble.doubleAddDouble(totalAmount, canPayAmount);
			tgxy_BankAccountEscrowed.setCanPayAmount(canPayAmount);
			
			// 托管收入 收入 + 今天的入账总额
			income = myDouble.doubleAddDouble(totalAmount, income);
			tgxy_BankAccountEscrowed.setIncome(income);
			
			// 活期余额 = 托管收入-托管支出-大额存单-结构性存款-保本理财
			Double sumBanlance = myDouble.doubleAddDouble(myDouble.doubleAddDouble(payout, certOfDeposit),
					myDouble.doubleAddDouble(structuredDeposit, breakEvenFinancial));
			Double currentBalance = myDouble.doubleSubtractDouble(income, sumBanlance);
			tgxy_BankAccountEscrowed.setCurrentBalance(currentBalance);

			// 大额占比 = 大额存单/托管收入
			Double largeRatio = myDouble.getShort(myDouble.div(certOfDeposit, income, 4), 2);
			tgxy_BankAccountEscrowed.setLargeRatio(largeRatio);

			// 大额+活期占比 = (大额存单+活期)/托管收入
			Double largeAndCurrentRatio = myDouble
					.getShort(myDouble.div(myDouble.doubleAddDouble(currentBalance, certOfDeposit), income, 4), 2);
			tgxy_BankAccountEscrowed.setLargeAndCurrentRatio(largeAndCurrentRatio);

			// 理财占比 =（结构性存款+保本理财）/托管收入
			Double financialRatio = myDouble.getShort(
					myDouble.div(myDouble.doubleAddDouble(structuredDeposit, breakEvenFinancial), income, 4), 2);
			tgxy_BankAccountEscrowed.setFinancialRatio(financialRatio);

			// 总资金沉淀占比 = （大额存单+结构性存款+保本理财+活期余额）/托管收入 或者 （托管收入-托管支出）/托管收入
			Double sumAmount = myDouble.doubleSubtractDouble(income, payout);
			Double totalFundsRatio = myDouble.getShort(myDouble.div(sumAmount, income, 4), 2);
			tgxy_BankAccountEscrowed.setTotalFundsRatio(totalFundsRatio);

			tgxy_BankAccountEscrowed.setUserUpdate(userStart);
			tgxy_BankAccountEscrowed.setLastUpdateTimeStamp(System.currentTimeMillis());
			tgxy_BankAccountEscrowedDao.save(tgxy_BankAccountEscrowed);

			// 更新日终结算时间，日终结算状态
			tgpf_DayEndBalancing.setSettlementState(2);
			tgpf_DayEndBalancing.setSettlementTime(currentTime);
			tgpf_DayEndBalancingDao.save(tgpf_DayEndBalancing);

		}

		// 更新楼幢信息
		Iterator<String> it = buildingMap.keySet().iterator(); // map.keySet()得到的是set集合，可以使用迭代器遍历

		while (it.hasNext())
		{

			String key = it.next();
			Long buildingTableId = Long.parseLong(key);
			Empj_BuildingInfo empj_BuildingInfo = (Empj_BuildingInfo) empj_BuildingInfoDao.findById(buildingTableId);

			Tgpj_BuildingAccountForm tgpj_BuildingAccountForm = new Tgpj_BuildingAccountForm();
			tgpj_BuildingAccountForm.setTheState(S_TheState.Normal);
			tgpj_BuildingAccountForm.setBuilding(empj_BuildingInfo);
			Integer buildingCount = tgpj_BuildingAccountDao.findByPage_Size(tgpj_BuildingAccountDao
					.getQuery_Size(tgpj_BuildingAccountDao.getBasicHQL(), tgpj_BuildingAccountForm));

			Tgpj_BuildingAccount buildingAccount = new Tgpj_BuildingAccount();

			List<Tgpj_BuildingAccount> tgpj_BuildingAccountList;
			if (buildingCount > 0)
			{
				tgpj_BuildingAccountList = tgpj_BuildingAccountDao.findByPage(tgpj_BuildingAccountDao
						.getQuery(tgpj_BuildingAccountDao.getBasicHQL(), tgpj_BuildingAccountForm));
				buildingAccount = tgpj_BuildingAccountList.get(0);
			}
			else
			{
				continue;
			}

			// 留存权益计算
			// Tgpf_BuildingRemainRightLogForm tgpf_BuildingRemainRightLogForm =
			// new Tgpf_BuildingRemainRightLogForm();
			// tgpf_BuildingRemainRightLogForm.setBillTimeStamp(billTimeStamp);
			// tgpf_BuildingRemainRightLogForm.setBuildingId(buildingTableId);
			// tgpf_BuildingRemainRightLogForm.setSrcBusiType("入账");

			// tgpf_BuildingRemainRightLogPublicAdd.execute(tgpf_BuildingRemainRightLogForm);

			// 总入账金额（元）、重新计算 溢出金额（元）和 可划拨金额（元）、当前托管余额。
			// 入账总金额是指所有有效合同的总的贷款入账。
			Double thisAmount = buildingMap.get(key); // 本次入账金额
			Double totalAccountAmount = 0.0;// 总入账金额（元）
			Double currentEscrowFund = 0.0;// 当前托管余额（元）
			Double effectiveLimitedAmount = 0.0;// 有效受限额度
			Double allocableAmount = 0.0;// 可拨付金额
			Double spilloverAmount = 0.0;// 溢出金额

			if (null != buildingAccount.getTotalAccountAmount())
			{
				totalAccountAmount = buildingAccount.getTotalAccountAmount();
			}

			if (null != buildingAccount.getCurrentEscrowFund())
			{
				currentEscrowFund = buildingAccount.getCurrentEscrowFund();
			}

			if (null != buildingAccount.getEffectiveLimitedAmount())
			{
				effectiveLimitedAmount = buildingAccount.getEffectiveLimitedAmount();
			}

			if (null != buildingAccount.getAllocableAmount())
			{
				allocableAmount = buildingAccount.getAllocableAmount();
			}

			if (null != buildingAccount.getSpilloverAmount())
			{
				spilloverAmount = buildingAccount.getSpilloverAmount();
			}

			// 新的入账金额
			Double newIncome = myDouble.doubleAddDouble(totalAccountAmount, thisAmount);
			buildingAccount.setTotalAccountAmount(newIncome);

			// 新的托管余额
			Double newBalance = myDouble.doubleAddDouble(currentEscrowFund, thisAmount);
			buildingAccount.setCurrentEscrowFund(newBalance);

			// 比较新的托管余额是否大于受限额度,大于则需要变更拨付金额和溢出金额
			if (newBalance > effectiveLimitedAmount)
			{
				// 旧托管金额大于受限额度,那么可拨付金额和溢出金额直接增加。否则，应该等于新的托管余额-受限额度
				if (currentEscrowFund > effectiveLimitedAmount)
				{
					allocableAmount = myDouble.doubleAddDouble(allocableAmount, thisAmount);
					spilloverAmount = myDouble.doubleAddDouble(spilloverAmount, thisAmount);
				}
				else
				{
					allocableAmount = myDouble.doubleAddDouble(allocableAmount,
							myDouble.doubleSubtractDouble(newBalance, effectiveLimitedAmount));
					spilloverAmount = myDouble.doubleAddDouble(spilloverAmount,
							myDouble.doubleSubtractDouble(newBalance, effectiveLimitedAmount));
				}
				buildingAccount.setAllocableAmount(allocableAmount);
				buildingAccount.setSpilloverAmount(spilloverAmount);
			}

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
			tgpj_BuildingAccountLog.setApplyRefundPayoutAmount(buildingAccount.getApplyRefundPayoutAmount());
			tgpj_BuildingAccountLog.setRefundAmount(buildingAccount.getRefundAmount());
			tgpj_BuildingAccountLog.setCurrentEscrowFund(buildingAccount.getCurrentEscrowFund());
			tgpj_BuildingAccountLog.setAppropriateFrozenAmount(buildingAccount.getAppropriateFrozenAmount());
			tgpj_BuildingAccountLog.setRecordAvgPriceOfBuildingFromPresellSystem(buildingAccount.getRecordAvgPriceOfBuildingFromPresellSystem());
			tgpj_BuildingAccountLog.setRecordAvgPriceOfBuilding(buildingAccount.getRecordAvgPriceOfBuilding());
			tgpj_BuildingAccountLog.setLogId(buildingAccount.getLogId());
			tgpj_BuildingAccountLog.setActualAmount(buildingAccount.getActualAmount());
			tgpj_BuildingAccountLog.setPaymentLines(buildingAccount.getPaymentLines());
			tgpj_BuildingAccountLog.setRelatedBusiCode(BUSI_CODE);
//			tgpj_BuildingAccountLog.setRelatedBusiTableId(tgpf_DayEndBalancingId);
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

			tgpj_BuildingAccountLimitedUpdateService.execute(tgpj_BuildingAccountLog);

			// tgpj_BuildingAccountDao.save(buildingAccount);
		}

		// 更新楼幢信息
		Iterator<String> buildingIt = buildingMap.keySet().iterator(); // map.keySet()得到的是set集合，可以使用迭代器遍历
		// 计算留存权益
		while (buildingIt.hasNext())
		{
			String key = buildingIt.next();
			Long buildingTableId = Long.parseLong(key);

			// 留存权益计算
			Tgpf_BuildingRemainRightLogForm tgpf_BuildingRemainRightLogForm = new Tgpf_BuildingRemainRightLogForm();
			tgpf_BuildingRemainRightLogForm.setBillTimeStamp(billTimeStamp);
			tgpf_BuildingRemainRightLogForm.setBuildingId(buildingTableId);
			tgpf_BuildingRemainRightLogForm.setSrcBusiType("入账");

			tgpf_BuildingRemainRightLogPublicAdd.execute(tgpf_BuildingRemainRightLogForm);
		}
			
											

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}

}
