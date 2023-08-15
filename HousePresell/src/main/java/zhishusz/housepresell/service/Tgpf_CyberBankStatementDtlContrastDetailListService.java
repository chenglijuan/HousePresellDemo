package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementForm;
import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDtlDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatementDtl;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：网银对账列表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_CyberBankStatementDtlContrastDetailListService
{

	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;
	@Autowired
	private Tgpf_CyberBankStatementDao tgpf_CyberBankStatementDao;
	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;
	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	
	public Properties execute(Tgpf_BalanceOfAccountForm model)
	{
		
		Properties properties = new MyProperties();
		
		Map idKeys = new HashMap();
		
		Long tgpf_BalanceOfAccountId = model.getTableId();
		
		Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = (Tgpf_BalanceOfAccount)tgpf_BalanceOfAccountDao.findById(tgpf_BalanceOfAccountId);
			
		// 如果业务总笔数小于银行总笔数，则需要重新对账单上传
		if( null != tgpf_BalanceOfAccount.getReconciliationState() &&  tgpf_BalanceOfAccount.getReconciliationState() == 0)
		{
			return MyBackInfo.fail(properties, "请先进行业务对账！");
		}
		
//		String theName = tgpf_BalanceOfAccount.getEscrowedAccountTheName();// 托管账户名称
		String theAccount = tgpf_BalanceOfAccount.getEscrowedAccount();// 托管账户
		String billTimeStamp = tgpf_BalanceOfAccount.getBillTimeStamp();// 入账日期
		
		// 查询网银对账主表
		// 查询条件：1.托管账号 2.记账日期 3.状态：正常
		Tgpf_CyberBankStatementForm tgpf_CyberBankStatementForm = new Tgpf_CyberBankStatementForm();
		tgpf_CyberBankStatementForm.setTheAccountOfBankAccountEscrowed(theAccount);
		tgpf_CyberBankStatementForm.setBillTimeStamp(billTimeStamp);
		tgpf_CyberBankStatementForm.setTheState(S_TheState.Normal);
		
		Integer tgpf_CyberBankCount = tgpf_CyberBankStatementDao.findByPage_Size(tgpf_CyberBankStatementDao.getQuery_Size(tgpf_CyberBankStatementDao.getBasicHQL(), tgpf_CyberBankStatementForm));
		
		List<Tgpf_CyberBankStatementDtl> tgpf_CyberBankStatementDtlList;
		
		List<Tgpf_CyberBankStatement> tgpf_CyberBankStatementList;
		
		// 不存在记录，则直接返回空列表
		if(tgpf_CyberBankCount > 0)
		{
			tgpf_CyberBankStatementList = tgpf_CyberBankStatementDao.findByPage(tgpf_CyberBankStatementDao.getQuery(tgpf_CyberBankStatementDao.getBasicHQL(), tgpf_CyberBankStatementForm));
			Tgpf_CyberBankStatement tgpf_CyberBankStatement = tgpf_CyberBankStatementList.get(0);
			
			// 查询网银上传明细
			// 查询条件：1.网银主键 2.状态为正常
			Tgpf_CyberBankStatementDtlForm tgpf_CyberBankStatementDtlForm = new Tgpf_CyberBankStatementDtlForm();
			tgpf_CyberBankStatementDtlForm.setMainTable(tgpf_CyberBankStatement);
			tgpf_CyberBankStatementDtlForm.setTheState(S_TheState.Normal);
//			tgpf_CyberBankStatementDtlForm.setReconciliationState(1);
			
			Integer cyberBankStatementDtlCount = tgpf_CyberBankStatementDtlDao.findByPage_Size(tgpf_CyberBankStatementDtlDao.getQuery_Size(tgpf_CyberBankStatementDtlDao.getBasicHQL(), tgpf_CyberBankStatementDtlForm));

			if(cyberBankStatementDtlCount > 0)
			{
				tgpf_CyberBankStatementDtlList = tgpf_CyberBankStatementDtlDao.findByPage(tgpf_CyberBankStatementDtlDao.getQuery(tgpf_CyberBankStatementDtlDao.getBasicHQL(), tgpf_CyberBankStatementDtlForm));
				for(Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl : tgpf_CyberBankStatementDtlList)
				{
					// 交易日期
					String tradeTimeStamp = tgpf_CyberBankStatementDtl.getTradeTimeStamp();
					// 对方账号
					String recipientAccount = tgpf_CyberBankStatementDtl.getRecipientAccount();
					if( recipientAccount == null || recipientAccount.length() == 0)
					{
						tgpf_CyberBankStatementDtl.setRecipientAccount("");
						recipientAccount = "";
					}
					// 对方账号名称
					String recipientName = tgpf_CyberBankStatementDtl.getRecipientName();
					if( recipientName == null || recipientName.length() == 0)
					{
						tgpf_CyberBankStatementDtl.setRecipientName("");
						recipientName = "";
					}
					
					// 交易金额
					Double income = tgpf_CyberBankStatementDtl.getIncome();
					if( income < 0 || income ==null)
					{
						continue;
					}
					// 关联的资金归集详情
					Long tgpf_DepositDetailId= tgpf_CyberBankStatementDtl.getTgpf_DepositDetailId();
					
					Tgpf_DepositDetail tgpf_DepositDetail = new Tgpf_DepositDetail();
					
					if( tgpf_DepositDetailId != null && tgpf_DepositDetailId >=0)
					{
						tgpf_DepositDetail = (Tgpf_DepositDetail)tgpf_DepositDetailDao.findById(tgpf_DepositDetailId);
						
						Tgxy_TripleAgreement tripleAgreement = tgpf_DepositDetail.getTripleAgreement();
						
						tgpf_CyberBankStatementDtl.setBusRecipientName(tgpf_DepositDetail.getTheNameOfCreditor());
						tgpf_CyberBankStatementDtl.setBusTradeTimeStamp(tgpf_DepositDetail.getBillTimeStamp());
						tgpf_CyberBankStatementDtl.setBusIecipientAccount(tgpf_DepositDetail.getBankAccountForLoan());
						tgpf_CyberBankStatementDtl.setTripleAgreementNum(tripleAgreement.geteCodeOfTripleAgreement());
						tgpf_CyberBankStatementDtl.setBusIncome(tgpf_DepositDetail.getLoanAmountFromBank());						
						tgpf_CyberBankStatementDtl.setBusRemark(tgpf_DepositDetail.getRemarkFromDepositBill());
						tgpf_CyberBankStatementDtl.setCyBankTripleAgreementNum(tripleAgreement.geteCodeOfTripleAgreement());
						
						idKeys.put(tgpf_DepositDetailId.toString(), tgpf_DepositDetailId.toString());
					}
					else
					{
						tgpf_CyberBankStatementDtl.setBusRecipientName("");
						tgpf_CyberBankStatementDtl.setBusTradeTimeStamp("");
						tgpf_CyberBankStatementDtl.setBusIecipientAccount("");
						tgpf_CyberBankStatementDtl.setTripleAgreementNum("");
						tgpf_CyberBankStatementDtl.setBusIncome(new Double(0));						
						tgpf_CyberBankStatementDtl.setBusRemark("");
						tgpf_CyberBankStatementDtl.setCyBankTripleAgreementNum("");
					}
				}		
			}
			else
			{
				tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
			}
			
		}
		else
		{
			tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		}
		
		
		// 匹配业务对账
		// 查询条件：1.交易日期 2.托管账户账号  3.托管账户  5.状态为正常
		Tgpf_DepositDetailForm tgpf_DepositDetailForm = new Tgpf_DepositDetailForm();
		tgpf_DepositDetailForm.setBillTimeStamp(billTimeStamp);
//		tgpf_DepositDetailForm.setTheNameOfBankAccountEscrowed(theName);
		tgpf_DepositDetailForm.setTheAccountOfBankAccountEscrowed(theAccount);
		tgpf_DepositDetailForm.setTheState(S_TheState.Normal);
		tgpf_DepositDetailForm.setTheStateFromReverse(0);
	
		Integer depositDetailCount = tgpf_DepositDetailDao.findByPage_Size(tgpf_DepositDetailDao.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), tgpf_DepositDetailForm));					
		List<Tgpf_DepositDetail> tgpf_DepositDetailList;
		if(depositDetailCount > 0)
		{
			tgpf_DepositDetailList = tgpf_DepositDetailDao.findByPage(tgpf_DepositDetailDao.getQuery(tgpf_DepositDetailDao.getBasicHQL(), tgpf_DepositDetailForm));
			
			for(Tgpf_DepositDetail depositDetail : tgpf_DepositDetailList)
			{
				
				if(idKeys.containsKey(depositDetail.getTableId().toString()))
				{
					continue;
				}
				
				
				Tgpf_CyberBankStatementDtl cyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
				
				cyberBankStatementDtl.setTableId(-depositDetail.getTableId());
				cyberBankStatementDtl.setTheState(0);
				cyberBankStatementDtl.setTradeTimeStamp("");
				cyberBankStatementDtl.setRecipientAccount("");
				cyberBankStatementDtl.setRecipientName("");
				cyberBankStatementDtl.setRemark("");
				cyberBankStatementDtl.setIncome(new Double(0));
				cyberBankStatementDtl.setReconciliationState(depositDetail.getReconciliationStateFromCyberBank());
				cyberBankStatementDtl.setTradeAmount(new Double(0));
				
				Tgxy_TripleAgreement tripleAgreement = depositDetail.getTripleAgreement();
				
				cyberBankStatementDtl.setBusRecipientName(depositDetail.getTheNameOfCreditor());
				cyberBankStatementDtl.setBusTradeTimeStamp(depositDetail.getBillTimeStamp());
				cyberBankStatementDtl.setBusIecipientAccount(depositDetail.getBankAccountForLoan());
				cyberBankStatementDtl.setTripleAgreementNum(tripleAgreement.geteCodeOfTripleAgreement());
				cyberBankStatementDtl.setBusIncome(depositDetail.getLoanAmountFromBank());						
				cyberBankStatementDtl.setBusRemark(depositDetail.getRemarkFromDepositBill());
				cyberBankStatementDtl.setCyBankTripleAgreementNum(tripleAgreement.geteCodeOfTripleAgreement());
				
				tgpf_CyberBankStatementDtlList.add(cyberBankStatementDtl);
			
			}
		}
						
		properties.put("recState",tgpf_BalanceOfAccount.getAccountType());
		properties.put("escrowedAccountTheName",tgpf_BalanceOfAccount.getBankBranch().getTheName());
		properties.put("escrowedAccount",theAccount);
		properties.put("bankName",tgpf_BalanceOfAccount.getBankName());
		properties.put("billTimeStamp",billTimeStamp);
		properties.put("centerTotalCount",tgpf_BalanceOfAccount.getCenterTotalCount());
		properties.put("centerTotalAmount",tgpf_BalanceOfAccount.getCenterTotalAmount());
		properties.put("cyberBankTotalCount",tgpf_BalanceOfAccount.getCyberBankTotalCount());
		properties.put("cyberBankTotalAmount",tgpf_BalanceOfAccount.getCyberBankTotalAmount());
		properties.put("tgpf_CyberBankStatementDtlList",tgpf_CyberBankStatementDtlList);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
