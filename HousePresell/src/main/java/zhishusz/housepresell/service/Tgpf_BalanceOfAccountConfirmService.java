package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：对账列表-对账确认
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_BalanceOfAccountConfirmService
{
	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;		//资金归集明细
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	public Properties execute(Tgpf_BalanceOfAccountForm model)
	{
		Properties properties = new MyProperties();
					
		Long tgpf_BalanceOfAccountId = model.getTableId();
		
		Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = (Tgpf_BalanceOfAccount)tgpf_BalanceOfAccountDao.findById(tgpf_BalanceOfAccountId);
		if(tgpf_BalanceOfAccount == null)
		{
			return MyBackInfo.fail(properties, "'Tgpf_BalanceOfAccount(Id:" + tgpf_BalanceOfAccountId + ")'不存在");
		}
		
		String theName = tgpf_BalanceOfAccount.getEscrowedAccountTheName();// 托管账户名称
		String theAccount = tgpf_BalanceOfAccount.getEscrowedAccount();// 托管账户
		String billTimeStamp = tgpf_BalanceOfAccount.getBillTimeStamp(); // 记账日期
		
		
		// 查询交易明细表中，是否全部业务对账成功
		// 查询条件：1.托管账户 2.托管账号 3.状态：正常  4. 业务对账状态：已对账
		Tgpf_DepositDetailForm depositDetailForm = new Tgpf_DepositDetailForm();
//		depositDetailForm.setTheNameOfBankAccountEscrowed(theName);
		depositDetailForm.setTheAccountOfBankAccountEscrowed(theAccount);
		depositDetailForm.setTheState(S_TheState.Normal); // 状态为正常
		depositDetailForm.setBillTimeStamp(billTimeStamp);
		depositDetailForm.setReconciliationStateFromBusiness(0);
		
		// 初始化：业务对账：已对账状态
		int reconciliationState = 1;
		
		// 查询资金归集明细表，如果存在记录，则为未对账状态
		Integer busContrastCount = tgpf_DepositDetailDao.findByPage_Size(
				tgpf_DepositDetailDao.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));
		if (busContrastCount > 0) {
			reconciliationState = 0;
		}
		
		tgpf_BalanceOfAccount.setReconciliationState(reconciliationState);
		tgpf_BalanceOfAccount.setReconciliationDate(myDatetime.dateToSimpleString(System.currentTimeMillis()));

//		tgpf_BalanceOfAccount.setUserUpdate(userUpdate);
		tgpf_BalanceOfAccount.setLastUpdateTimeStamp(System.currentTimeMillis());
	
		tgpf_BalanceOfAccountDao.save(tgpf_BalanceOfAccount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
