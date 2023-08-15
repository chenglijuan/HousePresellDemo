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
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service 业务对账（单）
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_BalanceOfAccountCompareService {

	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;		//资金归集明细
	@Autowired
	private Tgpf_BankUploadDataDetailDao tgpf_BankUploadDataDetailDao;// 银行对账单
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	MyDouble myDouble = MyDouble.getInstance();
	
	public Properties execute(Tgpf_DepositDetailForm model)
	{
		Properties properties = new MyProperties();
		
		//是否全部完成标志
		boolean flag = true ;
		
		String billTimeStamp = "";
		
		int accountType = 0;
		// 对账类型（0-自动对账，1-手动对账，2-撤销）
		if( null == model.getAccountType()||model.getAccountType()< 1)
		{
			accountType = 0;
		}else
		{
			accountType = model.getAccountType();
		}
			
		Long[] idArr = model.getIdArr();
		
		Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowed = new Tgxy_BankAccountEscrowed();
		
		for(int i = 0;i<idArr.length;i++)
		{

			// 查询交易明细表中
			Long tgpf_DepositDetailId = idArr[i];
			
			Tgpf_DepositDetail tgpf_DepositDetail = (Tgpf_DepositDetail) tgpf_DepositDetailDao
					.findById(tgpf_DepositDetailId);
			// 修改人
			// Sm_User userUpdate =
			// (Sm_User)sm_UserDao.findById(model.getUserUpdateId());
			Sm_User userUpdate = model.getUser();
			// 对账日期
			String reconciliationTime = myDatetime.dateToSimpleString(System.currentTimeMillis());
			// 银行平台流水号
			String eCodeFromBankPlatform = tgpf_DepositDetail.geteCodeFromBankPlatform();
			// 明细表对账时间
			billTimeStamp = tgpf_DepositDetail.getBillTimeStamp();
			// 明细表对账金额
			String loanAmountFromBank = myDouble.doubleToString(tgpf_DepositDetail.getLoanAmountFromBank(), 3);
			
			tgxy_BankAccountEscrowed = tgpf_DepositDetail.getBankAccountEscrowed();

			// 查询日终结算
			// 查询条件： 1.银行流水号 2.状态：正常
			Tgpf_BankUploadDataDetailForm tgpf_BankUploadDataDetailForm = new Tgpf_BankUploadDataDetailForm();
			tgpf_BankUploadDataDetailForm.setBkpltNo(eCodeFromBankPlatform);
			tgpf_BankUploadDataDetailForm.setTheState(S_TheState.Normal);

			Integer totalCount = tgpf_BankUploadDataDetailDao.findByPage_Size(tgpf_BankUploadDataDetailDao
					.getQuery_Size(tgpf_BankUploadDataDetailDao.getBasicHQL(), tgpf_BankUploadDataDetailForm));
			List<Tgpf_BankUploadDataDetail> tgpf_BankUploadDataDetailList;

			Tgpf_BankUploadDataDetail tgpf_BankUploadDataDetail = new Tgpf_BankUploadDataDetail();
			if (totalCount > 0)
			{
				tgpf_BankUploadDataDetailList = tgpf_BankUploadDataDetailDao.findByPage(tgpf_BankUploadDataDetailDao
						.getQuery(tgpf_BankUploadDataDetailDao.getBasicHQL(), tgpf_BankUploadDataDetailForm));
				tgpf_BankUploadDataDetail = tgpf_BankUploadDataDetailList.get(0);
				// 日终结算对账时间
				String enterTimeStamp = tgpf_BankUploadDataDetail.getEnterTimeStamp();
				// 日终结算对账金额
				String tradeAmount = myDouble.doubleToString(tgpf_BankUploadDataDetail.getTradeAmount(), 3);

				if (accountType == 0)
				{
					if(billTimeStamp.equals(enterTimeStamp) && loanAmountFromBank.equals(tradeAmount))
					{
						// 更新交易明细表
						tgpf_DepositDetail.setUserUpdate(userUpdate);
						tgpf_DepositDetail.setLastUpdateTimeStamp(System.currentTimeMillis());
						tgpf_DepositDetail.setReconciliationTimeStampFromBusiness(reconciliationTime);
						tgpf_DepositDetail.setReconciliationStateFromBusiness(1);

						tgpf_DepositDetailDao.save(tgpf_DepositDetail);

						// 更新对账明细表
						tgpf_BankUploadDataDetail.setUserUpdate(userUpdate);//
						// 修改人
						tgpf_BankUploadDataDetail.setLastUpdateTimeStamp(System.currentTimeMillis());// 修改时间
						tgpf_BankUploadDataDetail.setReconciliationState(1);// 业务对账状态
						tgpf_BankUploadDataDetail.setReconciliationStamp(reconciliationTime);

						tgpf_BankUploadDataDetailDao.save(tgpf_BankUploadDataDetail);
					}
					else
					{
						flag = false;
					}
				} 
				else if( accountType == 1 && loanAmountFromBank.equals(tradeAmount) )
				{
					// 更新交易明细表
					tgpf_DepositDetail.setUserUpdate(userUpdate);
					tgpf_DepositDetail.setLastUpdateTimeStamp(System.currentTimeMillis());
					tgpf_DepositDetail.setReconciliationTimeStampFromBusiness(reconciliationTime);
					tgpf_DepositDetail.setReconciliationStateFromBusiness(1);

					tgpf_DepositDetailDao.save(tgpf_DepositDetail);

					// 更新对账明细表
					tgpf_BankUploadDataDetail.setUserUpdate(userUpdate);//
					// 修改人
					tgpf_BankUploadDataDetail.setLastUpdateTimeStamp(System.currentTimeMillis());// 修改时间
					tgpf_BankUploadDataDetail.setReconciliationState(1);// 业务对账状态
					tgpf_BankUploadDataDetail.setReconciliationStamp(reconciliationTime);

					tgpf_BankUploadDataDetailDao.save(tgpf_BankUploadDataDetail);
				}
				else if (accountType == 2)
				{

					// 更新交易明细表
					tgpf_DepositDetail.setUserUpdate(userUpdate);
					tgpf_DepositDetail.setLastUpdateTimeStamp(System.currentTimeMillis());
					tgpf_DepositDetail.setReconciliationTimeStampFromBusiness(null);
					tgpf_DepositDetail.setReconciliationStateFromBusiness(0);

					tgpf_DepositDetailDao.save(tgpf_DepositDetail);

					// 更新对账明细表
					tgpf_BankUploadDataDetail.setUserUpdate(userUpdate);//
					// 修改人
					tgpf_BankUploadDataDetail.setLastUpdateTimeStamp(System.currentTimeMillis());// 修改时间
					tgpf_BankUploadDataDetail.setReconciliationState(0);// 业务对账状态
					tgpf_BankUploadDataDetail.setReconciliationStamp(null);

					tgpf_BankUploadDataDetailDao.save(tgpf_BankUploadDataDetail);
				}
			}
		}
		
		if(accountType == 0 && flag)
		{
			Tgpf_BalanceOfAccountForm tgpf_BalanceOfAccountForm = new Tgpf_BalanceOfAccountForm();
			tgpf_BalanceOfAccountForm.setTgxy_BankAccountEscrowed(tgxy_BankAccountEscrowed);
			tgpf_BalanceOfAccountForm.setBillTimeStamp(billTimeStamp);
			tgpf_BalanceOfAccountForm.setTheState(S_TheState.Normal);

			Integer totalCount = tgpf_BalanceOfAccountDao.findByPage_Size(tgpf_BalanceOfAccountDao
					.getQuery_Size(tgpf_BalanceOfAccountDao.getBasicHQL(), tgpf_BalanceOfAccountForm));

			List<Tgpf_BalanceOfAccount> tgpf_BalanceOfAccountList;
			Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = new Tgpf_BalanceOfAccount();

			// 有记录，为更新
			if (totalCount > 0) {
				tgpf_BalanceOfAccountList = tgpf_BalanceOfAccountDao.findByPage(tgpf_BalanceOfAccountDao
						.getQuery(tgpf_BalanceOfAccountDao.getBasicHQL(), tgpf_BalanceOfAccountForm));
				tgpf_BalanceOfAccount = tgpf_BalanceOfAccountList.get(0);
				
				tgpf_BalanceOfAccount.setReconciliationDate(myDatetime.dateToSimpleString(System.currentTimeMillis()));
				tgpf_BalanceOfAccount.setReconciliationState(1);
				tgpf_BalanceOfAccountDao.save(tgpf_BalanceOfAccount);
			} 
		}
			
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
