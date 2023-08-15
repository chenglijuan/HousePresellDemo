package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.controller.form.Tgpf_BankUploadDataDetailForm;
import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.dao.Tgpf_BankUploadDataDetailDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.database.po.Tgpf_BankUploadDataDetail;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service 业务撤销（单）
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_BalanceOfAccountCancelListService {
	
	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;		//资金归集明细
	@Autowired
	private Tgpf_BalanceOfAccountCompareService tgpf_BalanceOfAccountCompareService; //单个对账方法
	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;
	
	public Properties execute(Tgpf_BalanceOfAccountForm model)
	{
		Properties properties = new MyProperties();

		Long[] idArr = model.getIdArr();
		
		String timeStamp = model.getBillTimeStamp(); // 获取记账日期
		
		if ( null == idArr || idArr.length == 0)
		{
			return MyBackInfo.fail(properties, "请选择需要撤销的单据！");
		}

		for (int i = 0; i < idArr.length; i++) {
			Long tgpf_BalanceOfAccountId = idArr[i];
			
			Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = (Tgpf_BalanceOfAccount) tgpf_BalanceOfAccountDao
					.findById(tgpf_BalanceOfAccountId);
			

			String theName = tgpf_BalanceOfAccount.getEscrowedAccountTheName();// 托管账户名称
			String theAccount = tgpf_BalanceOfAccount.getEscrowedAccount();// 托管账户
			String billTimeStamp = tgpf_BalanceOfAccount.getBillTimeStamp();// 入账日期

			// 查询资金明细表的主键
			// 查询条件：1.托管账户名称 2.托管账户 3.对账日期 4.状态：正常
			Tgpf_DepositDetailForm tgpf_DepositDetailForm = new Tgpf_DepositDetailForm();
//			tgpf_DepositDetailForm.setTheNameOfBankAccountEscrowed(theName);
			tgpf_DepositDetailForm.setTheAccountOfBankAccountEscrowed(theAccount);
			tgpf_DepositDetailForm.setBillTimeStamp(billTimeStamp);
			tgpf_DepositDetailForm.setTheState(S_TheState.Normal);

			Integer totalCount = tgpf_DepositDetailDao.findByPage_Size(
					tgpf_DepositDetailDao.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), tgpf_DepositDetailForm));

			Long[] depositDetailArr = new Long[totalCount];
			
			List<Tgpf_DepositDetail> tgpf_DepositDetailList;
			Tgpf_DepositDetail tgpf_DepositDetail = new Tgpf_DepositDetail();
			if (totalCount > 0) {
				tgpf_DepositDetailList = tgpf_DepositDetailDao.findByPage(
						tgpf_DepositDetailDao.getQuery(tgpf_DepositDetailDao.getBasicHQL(), tgpf_DepositDetailForm));

				for (int j = 0; j<totalCount ; j++) {
					
					tgpf_DepositDetail = tgpf_DepositDetailList.get(j);
					
					depositDetailArr[j] = tgpf_DepositDetail.getTableId();				
				}
				
				Tgpf_DepositDetailForm depositDetailForm = new Tgpf_DepositDetailForm();
				
				depositDetailForm.setAccountType(2);
				depositDetailForm.setIdArr(depositDetailArr);
				
				tgpf_BalanceOfAccountCompareService.execute(depositDetailForm);
			}
			
			tgpf_BalanceOfAccount.setReconciliationState(0);
			tgpf_BalanceOfAccount.setReconciliationDate(null);
//			tgpf_BalanceOfAccount.setUserUpdate(userUpdate);
			tgpf_BalanceOfAccount.setLastUpdateTimeStamp(System.currentTimeMillis());
		
			tgpf_BalanceOfAccountDao.save(tgpf_BalanceOfAccount);
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
