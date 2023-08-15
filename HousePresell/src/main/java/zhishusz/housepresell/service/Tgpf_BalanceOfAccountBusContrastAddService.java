package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.controller.form.Tgpf_BankUploadDataDetailForm;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementForm;
import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.dao.Tgpf_BankUploadDataDetailDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDtlDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatementDtl;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：业务对账列表新增操作
 * Company：ZhiShuSZ
 * 查询所有的托管账户，并根据时间查询，表中是否存在该天的记录，如果不存在，则新增，否则，修改
 * */
@Service
@Transactional
public class Tgpf_BalanceOfAccountBusContrastAddService {

	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao; //托管账户
	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;		//资金归集明细
	@Autowired
	private Tgpf_BankUploadDataDetailDao tgpf_BankUploadDataDetailDao;// 银行对账单
	@Autowired
	private Tgpf_CyberBankStatementDao tgpf_CyberBankStatementDao;
	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	public Properties execute(Tgpf_BalanceOfAccountForm model)
	{
		
		Properties properties = new MyProperties();
		
		String billTimeStap = model.getBillTimeStamp();
		
		if (null == billTimeStap || billTimeStap.trim().isEmpty())
		{
			billTimeStap = myDatetime.getSpecifiedDayBefore(myDatetime.dateToSimpleString(System.currentTimeMillis()));
			model.setBillTimeStamp(billTimeStap);
		}
		
		model.setTheState(S_TheState.Normal);
		
		Sm_User userStart = model.getUser(); // admin
		
		// 托管账户表
		// 查询条件： 1.未删除
		Tgxy_BankAccountEscrowedForm bankAccountEscrowedForm = new Tgxy_BankAccountEscrowedForm();
		bankAccountEscrowedForm.setTheState(S_TheState.Normal); // 状态为正常
		Integer accountCount = tgxy_BankAccountEscrowedDao.findByPage_Size(tgxy_BankAccountEscrowedDao
				.getQuery_Size(tgxy_BankAccountEscrowedDao.getBasicHQL(), bankAccountEscrowedForm));

		// 校核托管账户是否存在
		List<Tgxy_BankAccountEscrowed> tgxy_BankAccountEscrowedList;
		if (accountCount > 0) {
			
			tgxy_BankAccountEscrowedList = tgxy_BankAccountEscrowedDao.findByPage(tgxy_BankAccountEscrowedDao
					.getQuery(tgxy_BankAccountEscrowedDao.getBasicHQL(), bankAccountEscrowedForm));

			for (Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowed : tgxy_BankAccountEscrowedList) {
				// 托管账户名称
				String theName = tgxy_BankAccountEscrowed.getTheName();
				// 托管账户
				String theAccount = tgxy_BankAccountEscrowed.getTheAccount();
				
				Emmp_BankInfo bank = tgxy_BankAccountEscrowed.getBank();
				// 银行名称
				String bankName = bank.getTheName();
				// 银行网点
				Emmp_BankBranch bankBranch = tgxy_BankAccountEscrowed.getBankBranch();

				// 对账列表
				// 查询条件 ：1.托管账户 2.托管账号 3.日期 4.状态为正常
				Tgpf_BalanceOfAccountForm tgpf_BalanceOfAccountForm = new Tgpf_BalanceOfAccountForm();
				tgpf_BalanceOfAccountForm.setEscrowedAccount(tgxy_BankAccountEscrowed.getTheAccount());
				tgpf_BalanceOfAccountForm.setBillTimeStamp(model.getBillTimeStamp());
				tgpf_BalanceOfAccountForm.setTheState(S_TheState.Normal);

				Integer totalCount = tgpf_BalanceOfAccountDao.findByPage_Size(tgpf_BalanceOfAccountDao
						.getQuery_Size(tgpf_BalanceOfAccountDao.getBasicHQL(), tgpf_BalanceOfAccountForm));

				List<Tgpf_BalanceOfAccount> tgpf_BalanceOfAccountList;
				Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = new Tgpf_BalanceOfAccount();

				// 有记录，为更新
				if (totalCount == 1) {
					tgpf_BalanceOfAccountList = tgpf_BalanceOfAccountDao.findByPage(tgpf_BalanceOfAccountDao
							.getQuery(tgpf_BalanceOfAccountDao.getBasicHQL(), tgpf_BalanceOfAccountForm));
					tgpf_BalanceOfAccount = tgpf_BalanceOfAccountList.get(0);
					
				}
				else if(totalCount > 1)
				{
					tgpf_BalanceOfAccountList = tgpf_BalanceOfAccountDao.findByPage(tgpf_BalanceOfAccountDao
							.getQuery(tgpf_BalanceOfAccountDao.getBasicHQL(), tgpf_BalanceOfAccountForm));
					
					int index = 0;
					
					for(Tgpf_BalanceOfAccount balanceOfAccount : tgpf_BalanceOfAccountList) {
											
						if( balanceOfAccount.getCenterTotalCount() > 0 )
						{
							tgpf_BalanceOfAccount = balanceOfAccount;
							index ++;
						}
						balanceOfAccount.setTheState(S_TheState.Deleted);
						tgpf_BalanceOfAccountDao.save(balanceOfAccount);						
					}
					if(index == 0)
					{
						tgpf_BalanceOfAccount = tgpf_BalanceOfAccountList.get(0);
					}
					else
					{
						tgpf_BalanceOfAccount.setTheState(S_TheState.Normal);
						tgpf_BalanceOfAccountDao.save(tgpf_BalanceOfAccount);	
					}
				}		
				else // 没有记录，为新增记录
				{
					tgpf_BalanceOfAccount.setCreateTimeStamp(System.currentTimeMillis());
					// tgpf_BalanceOfAccount.setBusiState(busiState);
					// tgpf_BalanceOfAccount.seteCode(eCode);
					tgpf_BalanceOfAccount.setUserStart(userStart);
					// tgpf_BalanceOfAccount.setUserRecord(userRecord);
					// tgpf_BalanceOfAccount.setRecordTimeStamp(recordTimeStamp);
					tgpf_BalanceOfAccount.setBillTimeStamp(model.getBillTimeStamp());
					tgpf_BalanceOfAccount.setBankName(bankName);
					tgpf_BalanceOfAccount.setEscrowedAccount(theAccount);
					tgpf_BalanceOfAccount.setEscrowedAccountTheName(theName);
					tgpf_BalanceOfAccount.setTgxy_BankAccountEscrowed(tgxy_BankAccountEscrowed);
					tgpf_BalanceOfAccount.setAccountType(0);
//					tgpf_BalanceOfAccount.setReconciliationDate(reconciliationDate);
					tgpf_BalanceOfAccount.setReconciliationState(0);
				}

				// 查询交易明细表中，是否全部业务对账成功
				// 查询条件：1.托管账户 2.托管账号 3.状态：正常
				Tgpf_DepositDetailForm depositDetailForm = new Tgpf_DepositDetailForm();
//				depositDetailForm.setTheNameOfBankAccountEscrowed(theName);
				depositDetailForm.setTheAccountOfBankAccountEscrowed(theAccount);
				depositDetailForm.setTheState(S_TheState.Normal); // 状态为正常
				depositDetailForm.setBillTimeStamp(model.getBillTimeStamp());
				depositDetailForm.setTheStateFromReverse(0);

				int centerTotalCount = tgpf_DepositDetailDao.findByPage_Size(tgpf_DepositDetailDao
						.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));

				// 查询中心业务总额度
				String queryCenterAmountCondition = " nvl(sum(loanAmountFromBank),0) ";

				Double centerTotalAmount = (Double)
						 tgpf_DepositDetailDao.findOneByQuery(tgpf_DepositDetailDao.getSpecialQuery(
								tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm, queryCenterAmountCondition));
				
				// 如果存在业务对账数据，才保存到表中
				if( centerTotalCount > 0 || centerTotalAmount > 0)
				{
					// 增加查询条件 ：4.业务对账状态：0 未对账
					depositDetailForm.setReconciliationStateFromBusiness(0);
					// 初始化：业务对账：已对账状态
					int reconciliationState = 1;

					// 查询资金归集明细表，如果存在未对账记录，则为未对账状态
					Integer busContrastCount = tgpf_DepositDetailDao.findByPage_Size(
							tgpf_DepositDetailDao.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));
					if (busContrastCount > 0) {
						reconciliationState = 0;
						tgpf_BalanceOfAccount.setReconciliationState(reconciliationState);
					}
					else
					{
						// 如果总笔数等于0，不修改状态
						if( centerTotalCount == 0 )
						{
							
						}
						else
						{
							tgpf_BalanceOfAccount.setReconciliationDate(myDatetime.dateToSimpleString(System.currentTimeMillis()));
							tgpf_BalanceOfAccount.setReconciliationState(reconciliationState);
						}					
					}

					// 初始化：网银对账：已对账状态
//					int bankReconciliationState = 1;

					// 查询交易明细表中，是否全部网银对账成功
					// 查询条件：1.托管账户 2.托管账号 3.网银对账状态：0 未对账 4.状态：正常
//					Tgpf_DepositDetailForm bankdepositDetailForm = new Tgpf_DepositDetailForm();
//					bankdepositDetailForm.setTheNameOfBankAccountEscrowed(theName);
//					bankdepositDetailForm.setTheAccountOfBankAccountEscrowed(theAccount);
//					bankdepositDetailForm.setReconciliationStateFromCyberBank(0);
//					bankdepositDetailForm.setTheState(S_TheState.Normal); // 状态为正常
//					bankdepositDetailForm.setBillTimeStamp(model.getBillTimeStamp());
//					bankdepositDetailForm.setTheStateFromReverse(0);

//					// 查询资金归集明细表，如果存在记录，则为未对账状态
//					Integer bankContrastCount = tgpf_DepositDetailDao.findByPage_Size(tgpf_DepositDetailDao
//							.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), bankdepositDetailForm));
//					if (bankContrastCount > 0) {
//						bankReconciliationState = 0;
//					}

					// 查询日终结算
					// 查询条件： 1.托管账户 2.托管账户名称 3.状态：正常
					Tgpf_BankUploadDataDetailForm tgpf_BankUploadDataDetailForm = new Tgpf_BankUploadDataDetailForm();
//					tgpf_BankUploadDataDetailForm.setTheAccountBankAccountEscrowed(theName);// 托管账号名称
					tgpf_BankUploadDataDetailForm.setTheAccountOfBankAccountEscrowed(theAccount);// 托管账户
					tgpf_BankUploadDataDetailForm.setTheState(S_TheState.Normal);
					tgpf_BankUploadDataDetailForm.setEnterTimeStamp(model.getBillTimeStamp());

					// 查询银行总笔数
					int bankTotalCount = tgpf_BankUploadDataDetailDao.findByPage_Size(
							tgpf_BankUploadDataDetailDao.getQuery_Size(tgpf_BankUploadDataDetailDao.getBasicHQL(),
									tgpf_BankUploadDataDetailForm));

					// 查询银行总金额
					String queryBankAmountCondition = " nvl(sum(tradeAmount),0) ";

					// 查询银行总金额
					Double bankTotalAmount = (Double) tgpf_BankUploadDataDetailDao.findOneByQuery(
							tgpf_BankUploadDataDetailDao.getSpecialQuery(tgpf_BankUploadDataDetailDao.getBasicHQL(),
									tgpf_BankUploadDataDetailForm, queryBankAmountCondition));
					
					
					// 查询网银对账主表
					// 查询条件：1.托管账号 2.记账日期 3.状态：正常
					Tgpf_CyberBankStatementForm tgpf_CyberBankStatementForm = new Tgpf_CyberBankStatementForm();
					tgpf_CyberBankStatementForm.setTheAccountOfBankAccountEscrowed(theAccount);
					tgpf_CyberBankStatementForm.setBillTimeStamp(model.getBillTimeStamp());
					tgpf_CyberBankStatementForm.setTheState(S_TheState.Normal);
					
					Integer tgpf_CyberBankCount = tgpf_CyberBankStatementDao.findByPage_Size(tgpf_CyberBankStatementDao.getQuery_Size(tgpf_CyberBankStatementDao.getBasicHQL(), tgpf_CyberBankStatementForm));
					
					int cyberBankStatementDtlCount = 0;
					Double cyberBankTotalAmount = 0.0;
					int accountType = 0;
					
					List<Tgpf_CyberBankStatement> tgpf_CyberBankStatementList;
					Tgpf_CyberBankStatement tgpf_CyberBankStatement = new Tgpf_CyberBankStatement();
					if(tgpf_CyberBankCount > 0)
					{
						tgpf_CyberBankStatementList = tgpf_CyberBankStatementDao.findByPage(tgpf_CyberBankStatementDao.getQuery(tgpf_CyberBankStatementDao.getBasicHQL(), tgpf_CyberBankStatementForm));
						tgpf_CyberBankStatement = tgpf_CyberBankStatementList.get(0);	
						
						accountType = tgpf_CyberBankStatement.getReconciliationState();
						
						// 查询网银上传明细
						// 查询条件：1.网银主键 2.状态为正常
						Tgpf_CyberBankStatementDtlForm tgpf_CyberBankStatementDtlForm = new Tgpf_CyberBankStatementDtlForm();
						tgpf_CyberBankStatementDtlForm.setMainTable(tgpf_CyberBankStatement);
						tgpf_CyberBankStatementDtlForm.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtlForm.setTradeTimeStamp(billTimeStap);
//						tgpf_CyberBankStatementDtlForm.setReconciliationState(1);
						
						cyberBankStatementDtlCount = tgpf_CyberBankStatementDtlDao.findByPage_Size(tgpf_CyberBankStatementDtlDao.getQuery_Size(tgpf_CyberBankStatementDtlDao.getBasicHQL(), tgpf_CyberBankStatementDtlForm));
						
						// 查询网银总金额
						String queryCyberBankAmountCondition = " nvl(sum(income),0) ";
						// 查询银行总金额
						cyberBankTotalAmount = (Double) tgpf_CyberBankStatementDtlDao.findOneByQuery(
								tgpf_CyberBankStatementDtlDao.getSpecialQuery(tgpf_CyberBankStatementDtlDao.getBasicHQL(),
										tgpf_CyberBankStatementDtlForm, queryCyberBankAmountCondition));
					}
					
					

					tgpf_BalanceOfAccount.setTheState(S_TheState.Normal);
					tgpf_BalanceOfAccount.setUserUpdate(userStart);
					tgpf_BalanceOfAccount.setLastUpdateTimeStamp(System.currentTimeMillis());
					tgpf_BalanceOfAccount.setCenterTotalCount(centerTotalCount);
					tgpf_BalanceOfAccount.setCenterTotalAmount(centerTotalAmount);
					tgpf_BalanceOfAccount.setBankTotalCount(bankTotalCount);
					tgpf_BalanceOfAccount.setBankTotalAmount(bankTotalAmount);
					tgpf_BalanceOfAccount.setCyberBankTotalCount(cyberBankStatementDtlCount);
					tgpf_BalanceOfAccount.setCyberBankTotalAmount(cyberBankTotalAmount);
					tgpf_BalanceOfAccount.setBankBranch(bankBranch);
					tgpf_BalanceOfAccountDao.save(tgpf_BalanceOfAccount);
				}
				else
				{
					continue;
				}				
			}
		} else {

			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, "未找到托管账户信息！");
			
			return properties;
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
