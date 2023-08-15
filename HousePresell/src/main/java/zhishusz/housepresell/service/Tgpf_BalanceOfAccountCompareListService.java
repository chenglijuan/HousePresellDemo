package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.dao.Tgpf_BankUploadDataDetailDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service 业务对账（列表）
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_BalanceOfAccountCompareListService {

	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;		//资金归集明细
	@Autowired
	private Tgpf_BalanceOfAccountCompareService tgpf_BalanceOfAccountCompareService; //单个对账方法
	@Autowired
	private Tgpf_BalanceOfAccountCancelListService tgpf_BalanceOfAccountCancelService;// 单个撤销方法
	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	public Properties execute(Tgpf_BalanceOfAccountForm model)
	{
		Properties properties = new MyProperties();

		Long[] idArr = model.getIdArr();
		
		// 当传入空值时，取当天所有的记录，进行自动对账
		if ( null == idArr || idArr.length == 0)
		{
			String billTimeStap = model.getBillTimeStamp();
			
			if (null == billTimeStap || billTimeStap.trim().isEmpty())
			{
				billTimeStap = myDatetime.getSpecifiedDayBefore(myDatetime.dateToSimpleString(System.currentTimeMillis()));
				model.setBillTimeStamp(billTimeStap);
			}
			
			Tgpf_BalanceOfAccountForm tgpf_BalanceOfAccountForm = new Tgpf_BalanceOfAccountForm();
			tgpf_BalanceOfAccountForm.setBillTimeStamp(billTimeStap);
			tgpf_BalanceOfAccountForm.setTheState(S_TheState.Normal);
			
			Integer totalCount = tgpf_BalanceOfAccountDao.findByPage_Size(tgpf_BalanceOfAccountDao.getQuery_Size(tgpf_BalanceOfAccountDao.getBasicHQL(), tgpf_BalanceOfAccountForm));
			
			List<Tgpf_BalanceOfAccount> tgpf_BalanceOfAccountList;
			if(totalCount > 0)
			{
				tgpf_BalanceOfAccountList = tgpf_BalanceOfAccountDao.findByPage(tgpf_BalanceOfAccountDao.getQuery(tgpf_BalanceOfAccountDao.getBasicHQL(), tgpf_BalanceOfAccountForm));
				
				Long[] idArr1 = new Long[tgpf_BalanceOfAccountList.size()];
				for( int i = 0; i< tgpf_BalanceOfAccountList.size() ; i ++)
				{
					Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = tgpf_BalanceOfAccountList.get(i);
					
					/**
					 * xsz by time 2019-5-14 15:07:12
					 * 先比较总笔数和总金额是否对应，不对应则直接结束返回
					 */
					Integer centerTotalCount = null == tgpf_BalanceOfAccount.getCenterTotalCount()?0:tgpf_BalanceOfAccount.getCenterTotalCount();//业务总笔数
					Double centerTotalAmount = null == tgpf_BalanceOfAccount.getCenterTotalAmount()?0.00:tgpf_BalanceOfAccount.getCenterTotalAmount();//业务总金额
					Integer bankTotalCount = null == tgpf_BalanceOfAccount.getBankTotalCount()?0:tgpf_BalanceOfAccount.getBankTotalCount();//银行总笔数
					Double bankTotalAmount = null == tgpf_BalanceOfAccount.getBankTotalAmount()?0.00:tgpf_BalanceOfAccount.getBankTotalAmount();//银行总金额
					if((centerTotalCount-bankTotalCount == 0)&&(centerTotalAmount-bankTotalAmount == 0))
					{
						idArr1[i] = tgpf_BalanceOfAccountList.get(i).getTableId();		
					}
				}
				
				compareList(idArr1);				
			}
			else
			{
				tgpf_BalanceOfAccountList = new ArrayList<Tgpf_BalanceOfAccount>();
			}			
		}
		else
		{
			compareList(idArr);
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
	
	public void compareList(Long[] idArr)
	{
		for (int i = 0; i < idArr.length; i++) 
		{
			
			Long tgpf_BalanceOfAccountId = idArr[i];
			
			Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = (Tgpf_BalanceOfAccount) tgpf_BalanceOfAccountDao
					.findById(tgpf_BalanceOfAccountId);
			
			if(null != tgpf_BalanceOfAccount)
			{
			
			/**
			 * xsz by time 2019-5-14 15:07:12
			 * 先比较总笔数和总金额是否对应，不对应则直接结束返回
			 */
			Integer centerTotalCount = null == tgpf_BalanceOfAccount.getCenterTotalCount()?0:tgpf_BalanceOfAccount.getCenterTotalCount();//业务总笔数
			Double centerTotalAmount = null == tgpf_BalanceOfAccount.getCenterTotalAmount()?0.00:tgpf_BalanceOfAccount.getCenterTotalAmount();//业务总金额
			Integer bankTotalCount = null == tgpf_BalanceOfAccount.getBankTotalCount()?0:tgpf_BalanceOfAccount.getBankTotalCount();//银行总笔数
			Double bankTotalAmount = null == tgpf_BalanceOfAccount.getBankTotalAmount()?0.00:tgpf_BalanceOfAccount.getBankTotalAmount();//银行总金额
			if((centerTotalCount-bankTotalCount == 0)&&(centerTotalAmount-bankTotalAmount == 0))
			{
				String theName = tgpf_BalanceOfAccount.getEscrowedAccountTheName();// 托管账户名称
				String theAccount = tgpf_BalanceOfAccount.getEscrowedAccount();// 托管账户
				String billTimeStamp = tgpf_BalanceOfAccount.getBillTimeStamp();// 入账日期

				// 查询资金明细表的主键
				// 查询条件：1.托管账户名称 2.托管账户 3.对账日期 4.状态：正常
				Tgpf_DepositDetailForm tgpf_DepositDetailForm = new Tgpf_DepositDetailForm();
//				tgpf_DepositDetailForm.setTheNameOfBankAccountEscrowed(theName);
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
					
					depositDetailForm.setAccountType(0);
					depositDetailForm.setIdArr(depositDetailArr);
					
					tgpf_BalanceOfAccountCompareService.execute(depositDetailForm);
									
				}
				else
				{
					tgpf_BalanceOfAccount.setReconciliationDate(myDatetime.dateToSimpleString(System.currentTimeMillis()));
					tgpf_BalanceOfAccount.setReconciliationState(1);
					tgpf_BalanceOfAccountDao.save(tgpf_BalanceOfAccount);
				}
			}
			}
			
		}
	}
	
}
