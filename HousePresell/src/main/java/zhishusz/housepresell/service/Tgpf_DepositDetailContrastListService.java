package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.controller.form.Tgpf_BankUploadDataDetailForm;
import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.dao.Tgpf_BankUploadDataDetailDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.database.po.Tgpf_BankUploadDataDetail;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：业务对账-详情页
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_DepositDetailContrastListService
{

	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;
	@Autowired
	private Tgpf_BankUploadDataDetailDao tgpf_BankUploadDataDetailDao;
	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_BalanceOfAccountForm model)
	{
		Properties properties = new MyProperties();
		
		Long tgpf_BalanceOfAccountId = model.getTableId();
		
		

		
		Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = (Tgpf_BalanceOfAccount) tgpf_BalanceOfAccountDao
				.findById(tgpf_BalanceOfAccountId);
		
		String theName = tgpf_BalanceOfAccount.getEscrowedAccountTheName();// 托管账户名称
		String theAccount = tgpf_BalanceOfAccount.getEscrowedAccount();// 托管账户
		
		model.setBillTimeStamp(tgpf_BalanceOfAccount.getBillTimeStamp());
		
		// 查询交易明细表中，是否全部业务对账成功
		// 查询条件：1.托管账户 2.托管账号 3.状态：正常 4.状态：正常
		Tgpf_DepositDetailForm depositDetailForm = new Tgpf_DepositDetailForm();
//		depositDetailForm.setTheNameOfBankAccountEscrowed(theName);
		depositDetailForm.setTheAccountOfBankAccountEscrowed(theAccount);
		depositDetailForm.setTheState(S_TheState.Normal); // 状态为正常
		depositDetailForm.setBillTimeStamp(model.getBillTimeStamp());
		depositDetailForm.setTheStateFromReverse(0);
		// 查询中心业务总笔数
//		String queryCenterCountCondition = " nvl(count(*),0) ";

		int centerTotalCount = tgpf_DepositDetailDao.findByPage_Size(tgpf_DepositDetailDao
				.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));

		
		// 查询中心业务总额度
		String queryCenterAmountCondition = " nvl(sum(loanAmountFromBank),0) ";

		Double centerTotalAmount = (Double)
				 tgpf_DepositDetailDao.findOneByQuery(tgpf_DepositDetailDao.getSpecialQuery(
						tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm, queryCenterAmountCondition));
		// 查询日终结算
		// 查询条件： 1.托管账户 2.托管账户名称 3.状态：正常
		Tgpf_BankUploadDataDetailForm tgpf_BankUploadDataDetailForm = new Tgpf_BankUploadDataDetailForm();
//		tgpf_BankUploadDataDetailForm.setTheAccountBankAccountEscrowed(theName);// 托管账号名称
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
		
		// 如果业务总笔数小于银行总笔数，则需要重新对账单上传
		if( centerTotalCount < bankTotalCount)
		{
			return MyBackInfo.fail(properties, "银行红冲后，请联系银行重新上传对账单！");
		}

		// 查询资金明细表的主键
		// 查询条件：1.托管账户名称 2.托管账户 3.对账日期 4.状态：正常
		Tgpf_DepositDetailForm tgpf_DepositDetailForm = new Tgpf_DepositDetailForm();
//		tgpf_DepositDetailForm.setTheNameOfBankAccountEscrowed(theName);
		tgpf_DepositDetailForm.setTheAccountOfBankAccountEscrowed(theAccount);
		tgpf_DepositDetailForm.setBillTimeStamp(model.getBillTimeStamp());
		tgpf_DepositDetailForm.setTheState(S_TheState.Normal);
		tgpf_DepositDetailForm.setTheStateFromReverse(0);

		Integer totalCount = tgpf_DepositDetailDao.findByPage_Size(
				tgpf_DepositDetailDao.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), tgpf_DepositDetailForm));

		List<Tgpf_DepositDetail> tgpf_DepositDetailList;
		if (totalCount > 0) {
			
			tgpf_DepositDetailList = tgpf_DepositDetailDao.findByPage(
					tgpf_DepositDetailDao.getQuery(tgpf_DepositDetailDao.getBasicHQL(), tgpf_DepositDetailForm));

			for (Tgpf_DepositDetail tgpf_DepositDetail : tgpf_DepositDetailList) {
				
				tgpf_DepositDetail.setCenterTotalCount(centerTotalCount);
				tgpf_DepositDetail.setCenterTotalAmount(centerTotalAmount);
				tgpf_DepositDetail.setBankTotalCount(bankTotalCount);
				tgpf_DepositDetail.setBankTotalAmount(bankTotalAmount);
				
				tgpf_DepositDetail.setTripleAgreementNum(tgpf_DepositDetail.getTripleAgreement().geteCodeOfTripleAgreement());
				
				// 银行平台流水号
				String eCodeFromBankPlatform = tgpf_DepositDetail.geteCodeFromBankPlatform();
				
				// 查询日终结算
				// 查询条件： 1.银行流水号 2.状态：正常
				Tgpf_BankUploadDataDetailForm bankUploadDataDetailForm = new Tgpf_BankUploadDataDetailForm();
				bankUploadDataDetailForm.setBkpltNo(eCodeFromBankPlatform);
				bankUploadDataDetailForm.setTheState(S_TheState.Normal);
				
				Integer bankCount = tgpf_BankUploadDataDetailDao.findByPage_Size(tgpf_BankUploadDataDetailDao.getQuery_Size(tgpf_BankUploadDataDetailDao.getBasicHQL(), bankUploadDataDetailForm));
				List<Tgpf_BankUploadDataDetail> tgpf_BankUploadDataDetailList;
				
				Tgpf_BankUploadDataDetail tgpf_BankUploadDataDetail = new Tgpf_BankUploadDataDetail();
				if(bankCount > 0)
				{
					tgpf_BankUploadDataDetailList = tgpf_BankUploadDataDetailDao.findByPage(tgpf_BankUploadDataDetailDao.getQuery(tgpf_BankUploadDataDetailDao.getBasicHQL(), bankUploadDataDetailForm));
					tgpf_BankUploadDataDetail = tgpf_BankUploadDataDetailList.get(0);
					
					// 记账日期
					tgpf_DepositDetail.setBankBillTimeStamp(tgpf_BankUploadDataDetail.getEnterTimeStamp());
					// 缴款金额
					tgpf_DepositDetail.setBankAmount(tgpf_BankUploadDataDetail.getTradeAmount());
					tgpf_DepositDetail.setTripleAgreementNumBank(tgpf_DepositDetail.getTripleAgreementNum());
					tgpf_DepositDetail.setTheNameOfCreditorBank(tgpf_DepositDetail.getTheNameOfCreditor());
					tgpf_DepositDetail.setBankAccountForLoanBank(tgpf_DepositDetail.getBankAccountForLoan());
				}
				else
				{
					// 记账日期
					tgpf_DepositDetail.setBankBillTimeStamp("");
					// 缴款金额
					tgpf_DepositDetail.setBankAmount(0.0);
					tgpf_DepositDetail.setTripleAgreementNumBank("");
					tgpf_DepositDetail.setTheNameOfCreditorBank("");
					tgpf_DepositDetail.setBankAccountForLoanBank("");
				}
			}
			
		}else{
			tgpf_DepositDetailList = new ArrayList<Tgpf_DepositDetail>();
		}
		
		properties.put("recState", tgpf_BalanceOfAccount.getReconciliationState());
		properties.put("tgpf_DepositDetailList", tgpf_DepositDetailList);
		properties.put("centerTotalCount", centerTotalCount);
		properties.put("centerTotalAmount", centerTotalAmount);
		properties.put("bankTotalCount", bankTotalCount);
		properties.put("bankTotalAmount", bankTotalAmount);
		properties.put("bankName", tgpf_BalanceOfAccount.getBankName());
		properties.put("theAccount", theAccount);
		properties.put("theName", tgpf_BalanceOfAccount.getBankBranch().getTheName());
		properties.put("billTimeStamp", tgpf_BalanceOfAccount.getBillTimeStamp());
		
//		properties.put(S_NormalFlag.keyword, keyword);
//		properties.put(S_NormalFlag.totalPage, totalPage);
//		properties.put(S_NormalFlag.pageNumber, pageNumber);
//		properties.put(S_NormalFlag.countPerPage, countPerPage);
//		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
