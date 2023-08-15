package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.controller.form.Tgpf_DayEndBalancingForm;
import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.dao.Tgpf_DayEndBalancingDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.database.po.Tgpf_DayEndBalancing;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：业务对账列表新增操作
 * Company：ZhiShuSZ
 * 查询所有的托管账户，并根据时间查询，表中是否存在该天的记录，如果不存在，则新增，否则，修改
 */
@Service
@Transactional
public class Tgpf_DayEndBalancingPreAddService
{
	MyDatetime myDatetime = MyDatetime.getInstance();

	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao; // 托管账户
	@Autowired
	private Tgpf_DayEndBalancingDao tgpf_DayEndBalancingDao; // 日终结算
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;

	public Properties execute(Tgpf_DayEndBalancingForm model)
	{
		Properties properties = new MyProperties();

		String billTimeStap = model.getBillTimeStamp();

		if (null == billTimeStap || billTimeStap.trim().isEmpty())
		{
			billTimeStap = myDatetime.getSpecifiedDayBefore(myDatetime.dateToSimpleString(System.currentTimeMillis()));;
			model.setBillTimeStamp(billTimeStap);
		}

		model.setTheState(S_TheState.Normal);
		
		Sm_User userStart = (Sm_User)sm_UserDao.findById(1l); // admin

		// 托管账户表
		// 查询条件： 1.未删除
		Tgxy_BankAccountEscrowedForm bankAccountEscrowedForm = new Tgxy_BankAccountEscrowedForm();
		bankAccountEscrowedForm.setTheState(S_TheState.Normal); // 状态为正常
		Integer accountCount = tgxy_BankAccountEscrowedDao.findByPage_Size(tgxy_BankAccountEscrowedDao
				.getQuery_Size(tgxy_BankAccountEscrowedDao.getBasicHQL(), bankAccountEscrowedForm));

		// 校核托管账户是否存在
		List<Tgxy_BankAccountEscrowed> tgxy_BankAccountEscrowedList;
		if (accountCount > 0)
		{

			tgxy_BankAccountEscrowedList = tgxy_BankAccountEscrowedDao.findByPage(tgxy_BankAccountEscrowedDao
					.getQuery(tgxy_BankAccountEscrowedDao.getBasicHQL(), bankAccountEscrowedForm));

			for (Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowed : tgxy_BankAccountEscrowedList)
			{
				// 托管账户名称
				String theName = tgxy_BankAccountEscrowed.getTheName();
				// 托管账户
				String theAccount = tgxy_BankAccountEscrowed.getTheAccount();
				
				Emmp_BankInfo bank = tgxy_BankAccountEscrowed.getBank();
				// 银行名称
				String bankName = bank.getTheName();
				// 银行网点
				Emmp_BankBranch bankBranch = tgxy_BankAccountEscrowed.getBankBranch();
				
				Tgpf_DayEndBalancingForm tgpf_DayEndBalancingForm = new Tgpf_DayEndBalancingForm();
				tgpf_DayEndBalancingForm.setTgxy_BankAccountEscrowed(tgxy_BankAccountEscrowed);
				tgpf_DayEndBalancingForm.setTheState(S_TheState.Normal);
				tgpf_DayEndBalancingForm.setBillTimeStamp(billTimeStap);
				
				Integer dayEndBalancingCount = tgpf_DayEndBalancingDao.findByPage_Size(tgpf_DayEndBalancingDao.getQuery_Size(tgpf_DayEndBalancingDao.getBasicHQL(), tgpf_DayEndBalancingForm));
				
				// 有记录，为更新
				List<Tgpf_DayEndBalancing> tgpf_DayEndBalancingList;
				Tgpf_DayEndBalancing tgpf_DayEndBalancing = null; 
				if( dayEndBalancingCount >0)
				{
					tgpf_DayEndBalancingList = tgpf_DayEndBalancingDao.findByPage(tgpf_DayEndBalancingDao.getQuery(tgpf_DayEndBalancingDao.getBasicHQL(), tgpf_DayEndBalancingForm));
					
					tgpf_DayEndBalancing = tgpf_DayEndBalancingList.get(0);
				}
				else
				{
					tgpf_DayEndBalancing = new Tgpf_DayEndBalancing();
					tgpf_DayEndBalancing.setTheState(S_TheState.Normal);
//					tgpf_DayEndBalancing.setBusiState(busiState);
//					tgpf_DayEndBalancing.seteCode(eCode);
					tgpf_DayEndBalancing.setUserStart(userStart);
					tgpf_DayEndBalancing.setCreateTimeStamp(System.currentTimeMillis());
					tgpf_DayEndBalancing.setUserUpdate(userStart);
					tgpf_DayEndBalancing.setLastUpdateTimeStamp(System.currentTimeMillis());
//					tgpf_DayEndBalancing.setUserRecord(userRecord);
//					tgpf_DayEndBalancing.setRecordTimeStamp(recordTimeStamp);
					tgpf_DayEndBalancing.setTgxy_BankAccountEscrowed(tgxy_BankAccountEscrowed);
					tgpf_DayEndBalancing.setBankName(bankName);
					tgpf_DayEndBalancing.setEscrowedAccount(theAccount);
					tgpf_DayEndBalancing.setEscrowedAccountTheName(theName);
					tgpf_DayEndBalancing.setSettlementState(0);
//					tgpf_DayEndBalancing.setSettlementTime();					
				}
	
				// 对账列表
				// 查询条件 ：1.托管账户 2.托管账号 3.日期 4.状态为正常
				Tgpf_BalanceOfAccountForm tgpf_BalanceOfAccountForm = new Tgpf_BalanceOfAccountForm();
//				tgpf_BalanceOfAccountForm.setEscrowedAccountTheName(theName);
				tgpf_BalanceOfAccountForm.setEscrowedAccount(theAccount);
				tgpf_BalanceOfAccountForm.setBillTimeStamp(billTimeStap);
				tgpf_BalanceOfAccountForm.setTheState(S_TheState.Normal);
				
				Integer balanceOfAccountCount = tgpf_BalanceOfAccountDao.findByPage_Size(tgpf_BalanceOfAccountDao
						.getQuery_Size(tgpf_BalanceOfAccountDao.getBasicHQL(), tgpf_BalanceOfAccountForm));
				
				List<Tgpf_BalanceOfAccount> tgpf_BalanceOfAccountList;
				Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = new Tgpf_BalanceOfAccount();

				// 有记录，为更新
				if (balanceOfAccountCount > 0) {
					tgpf_BalanceOfAccountList = tgpf_BalanceOfAccountDao.findByPage(tgpf_BalanceOfAccountDao
							.getQuery(tgpf_BalanceOfAccountDao.getBasicHQL(), tgpf_BalanceOfAccountForm));
					
					tgpf_BalanceOfAccount = tgpf_BalanceOfAccountList.get(0);
					
					if( tgpf_BalanceOfAccount.getCenterTotalCount() > 0 )
					{
						tgpf_DayEndBalancing.setTotalCount(tgpf_BalanceOfAccount.getCyberBankTotalCount());
						tgpf_DayEndBalancing.setTotalAmount(tgpf_BalanceOfAccount.getCyberBankTotalAmount());
						tgpf_DayEndBalancing.setBillTimeStamp(billTimeStap);
						tgpf_DayEndBalancing.setRecordState(tgpf_BalanceOfAccount.getAccountType());
					}
					else
					{
						continue;
					}
				}
				else{
					continue;
//					tgpf_DayEndBalancing.setTotalCount(0);
//					tgpf_DayEndBalancing.setTotalAmount(0.0);
//					tgpf_DayEndBalancing.setBillTimeStamp(billTimeStap);
//					tgpf_DayEndBalancing.setRecordState(0);
				}
				tgpf_DayEndBalancingDao.save(tgpf_DayEndBalancing);
							
			}
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
