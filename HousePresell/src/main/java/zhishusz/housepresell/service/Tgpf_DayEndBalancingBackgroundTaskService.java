package zhishusz.housepresell.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.controller.form.Tgpf_DayEndBalancingForm;
import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.dao.Tgpf_DayEndBalancingDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.database.po.Tgpf_DayEndBalancing;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * 业务对账-日终结算-后台任务
 * Company：ZhiShuSZ
 * */
@Lazy(false)
@Component
@Service
@Transactional
public class Tgpf_DayEndBalancingBackgroundTaskService
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
	private Tgpf_DayEndBalancingManualTaskService tgpf_DayEndBalancingManualTaskService;// 楼幢账户
	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;
	
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	MyDouble myDouble = MyDouble.getInstance();

//	@Scheduled(cron="0 0 20 * * ? ") 八点
//	@Scheduled(cron="0 0 2 * * ? ") 两点
	public Properties execute()
	{
		
		Properties properties = new MyProperties();

		Sm_User userStart = (Sm_User)sm_UserDao.findById(1l); // admin
		
		String currentTime = myDatetime.dateToSimpleString(System.currentTimeMillis());
		
		Map<String,Double> buildingMap = new HashMap<String,Double>();
		
		// 查询日终结算表
		// 查询条件：1.当前日期前一天 2.已对账 3. 已经申请日终结算 4. 总笔数大于1
		String dayBefore = myDatetime.getSpecifiedDayBefore(currentTime);
		String billTimeStamp = myDatetime.getSpecifiedDayBefore(dayBefore); 
		Tgpf_DayEndBalancingForm tgpf_DayEndBalancingForm = new Tgpf_DayEndBalancingForm();
//		tgpf_DayEndBalancingForm.setBillTimeStamp(billTimeStamp);
		tgpf_DayEndBalancingForm.setRecordState(1);
		tgpf_DayEndBalancingForm.setSettlementState(1);
		tgpf_DayEndBalancingForm.setTheState(S_TheState.Normal);
		
		Integer dayEndBalancingCount = tgpf_DayEndBalancingDao.findByPage_Size(tgpf_DayEndBalancingDao.getQuery_Size(tgpf_DayEndBalancingDao.getBalancingHQL(), tgpf_DayEndBalancingForm));
		
		List<Tgpf_DayEndBalancing> tgpf_DayEndBalancingList;
		if(dayEndBalancingCount > 0)
		{
			tgpf_DayEndBalancingList = tgpf_DayEndBalancingDao.findByPage(tgpf_DayEndBalancingDao.getQuery(tgpf_DayEndBalancingDao.getBalancingHQL(), tgpf_DayEndBalancingForm));
			
			Long[] idArr =new Long[tgpf_DayEndBalancingList.size()];
			
			Tgpf_DayEndBalancingForm dayEndBalancingForm = new Tgpf_DayEndBalancingForm();
			
			int index = 0 ;
			for(Tgpf_DayEndBalancing tgpf_DayEndBalancing : tgpf_DayEndBalancingList)
			{
				idArr[index] = tgpf_DayEndBalancing.getTableId();
				index ++;
			}
			
			dayEndBalancingForm.setIdArr(idArr);
			dayEndBalancingForm.setBillTimeStamp(billTimeStamp);
			dayEndBalancingForm.setUser(userStart);
			
			tgpf_DayEndBalancingManualTaskService.execute(dayEndBalancingForm);		
		}
		
		// 进行结账操作
		// 循环托管账户
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

				Tgpf_DayEndBalancingForm dayEndBalancingForm = new Tgpf_DayEndBalancingForm();
				dayEndBalancingForm.setTgxy_BankAccountEscrowed(tgxy_BankAccountEscrowed);
				dayEndBalancingForm.setTheState(S_TheState.Normal);
				dayEndBalancingForm.setBillTimeStamp(dayBefore);

				Integer dayEndCount = tgpf_DayEndBalancingDao.findByPage_Size(tgpf_DayEndBalancingDao
						.getQuery_Size(tgpf_DayEndBalancingDao.getBasicHQL(), dayEndBalancingForm));

				// 有记录，为更新
				List<Tgpf_DayEndBalancing> tgpf_DayEndList;
				Tgpf_DayEndBalancing tgpf_DayEndBalancing = null;
				if (dayEndCount > 0)
				{
					tgpf_DayEndList = tgpf_DayEndBalancingDao.findByPage(tgpf_DayEndBalancingDao
							.getQuery(tgpf_DayEndBalancingDao.getBasicHQL(), dayEndBalancingForm));

					tgpf_DayEndBalancing = tgpf_DayEndList.get(0);
				}
				else
				{
					tgpf_DayEndBalancing = new Tgpf_DayEndBalancing();
					tgpf_DayEndBalancing.setTheState(S_TheState.Normal);
					// tgpf_DayEndBalancing.setBusiState(busiState);
					// tgpf_DayEndBalancing.seteCode(eCode);
					tgpf_DayEndBalancing.setUserStart(userStart);
					tgpf_DayEndBalancing.setCreateTimeStamp(System.currentTimeMillis());
					tgpf_DayEndBalancing.setUserUpdate(userStart);
					tgpf_DayEndBalancing.setLastUpdateTimeStamp(System.currentTimeMillis());
					// tgpf_DayEndBalancing.setUserRecord(userRecord);
					// tgpf_DayEndBalancing.setRecordTimeStamp(recordTimeStamp);
					tgpf_DayEndBalancing.setTgxy_BankAccountEscrowed(tgxy_BankAccountEscrowed);
					tgpf_DayEndBalancing.setBankName(bankName);
					tgpf_DayEndBalancing.setEscrowedAccount(theAccount);
					tgpf_DayEndBalancing.setEscrowedAccountTheName(theName);
					tgpf_DayEndBalancing.setSettlementState(0);
					// tgpf_DayEndBalancing.setSettlementTime();
				}

				// 对账列表
				// 查询条件 ：1.托管账户 2.托管账号 3.日期 4.状态为正常
				Tgpf_BalanceOfAccountForm tgpf_BalanceOfAccountForm = new Tgpf_BalanceOfAccountForm();
				tgpf_BalanceOfAccountForm.setEscrowedAccount(theAccount);
				tgpf_BalanceOfAccountForm.setBillTimeStamp(dayBefore);
				tgpf_BalanceOfAccountForm.setTheState(S_TheState.Normal);

				Integer balanceOfAccountCount = tgpf_BalanceOfAccountDao.findByPage_Size(tgpf_BalanceOfAccountDao
						.getQuery_Size(tgpf_BalanceOfAccountDao.getBasicHQL(), tgpf_BalanceOfAccountForm));

				List<Tgpf_BalanceOfAccount> tgpf_BalanceOfAccountList;
				Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = new Tgpf_BalanceOfAccount();

				// 有记录，为更新
				if (balanceOfAccountCount > 0)
				{
					tgpf_BalanceOfAccountList = tgpf_BalanceOfAccountDao.findByPage(tgpf_BalanceOfAccountDao
							.getQuery(tgpf_BalanceOfAccountDao.getBasicHQL(), tgpf_BalanceOfAccountForm));

					tgpf_BalanceOfAccount = tgpf_BalanceOfAccountList.get(0);

					tgpf_DayEndBalancing.setTotalCount(tgpf_BalanceOfAccount.getCyberBankTotalCount());
					tgpf_DayEndBalancing.setTotalAmount(tgpf_BalanceOfAccount.getCyberBankTotalAmount());
					tgpf_DayEndBalancing.setBillTimeStamp(dayBefore);
					tgpf_DayEndBalancing.setRecordState(tgpf_BalanceOfAccount.getAccountType());
					
				}
				else
				{
					tgpf_DayEndBalancing.setTotalCount(0);
					tgpf_DayEndBalancing.setTotalAmount(0.0);
					tgpf_DayEndBalancing.setBillTimeStamp(dayBefore);
					tgpf_DayEndBalancing.setRecordState(0);
				}
				tgpf_DayEndBalancingDao.save(tgpf_DayEndBalancing);

			}
		}
			
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}

}
