package zhishusz.housepresell.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.json.JSONUtil;

/*
 * Service添加操作：网银对账-详情页面-自动对账
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_CyberBankStatementDtlAutoCompareService
{

	@Autowired
	private Tgpf_CyberBankStatementDao tgpf_CyberBankStatementDao;
	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;
	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	MyDouble myDouble = MyDouble.getInstance();
	
	public Properties execute(Tgpf_CyberBankStatementDtlForm model)
	{
		String jsonMsg = model.getTgpf_CyberBankStatementDtlContrastDetailList();
		
		model = (Tgpf_CyberBankStatementDtlForm) JSONUtil.toBean(jsonMsg, Tgpf_CyberBankStatementDtlForm.class);
		
		Properties properties = new MyProperties();
		// 对账日期
		String reconciliationTime = myDatetime.dateToSimpleString(System.currentTimeMillis());
		// 时间戳
		String tradeTime = "必须字段";
		// 收款账户
		String reciAccount = model.getRecipientAccount();
		// 收款账号
		String reciName = model.getRecipientName();
		// 交易金额
		String tradeAmount = "必须字段";
		// 摘要
		String remark = model.getRemark();
		

		if ((reciAccount == null || reciAccount.length() == 0) && (reciName == null || reciName.length() == 0)
				&& (remark == null || remark.length() == 0))
		{
			return MyBackInfo.fail(properties, "自动对账时，对方账户，对方户名，摘要不能同时为空！");
		}
		
		// 保存这次对账已经匹配的主键
		Map idKeys = new HashMap();
		// id
		Long tgpf_BalanceOfAccountId = model.getTableId();
		
		Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = (Tgpf_BalanceOfAccount)tgpf_BalanceOfAccountDao.findById(tgpf_BalanceOfAccountId);
		
		String theName = tgpf_BalanceOfAccount.getEscrowedAccountTheName();// 托管账户名称
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
					// 对方账户名称
					String recipientName = tgpf_CyberBankStatementDtl.getRecipientName();
					// 交易金额
					Double income = tgpf_CyberBankStatementDtl.getIncome();
					// 备注
					String bankRemark = tgpf_CyberBankStatementDtl.getRemark();
					
					if ( (reciAccount == null || reciAccount.length()==0 ) 
							&& (reciName == null || reciName.length()==0) 
							&& (remark == null || remark.length()==0) 
							&& (bankRemark == null || bankRemark.length()==0))
					{
						continue;
					}
					
					// 匹配网银对账
					// 查询条件：1.交易日期 2.对方账号 3.对方账号名称 4.交易金额  5.备注 6.状态为正常 7. 未对账
					Tgpf_DepositDetailForm tgpf_DepositDetailForm = new Tgpf_DepositDetailForm();
					
					if(tradeTime != null && tradeTime.length() != 0)
					{
						tgpf_DepositDetailForm.setBillTimeStamp(tradeTimeStamp);
					}
					if(reciAccount != null && reciAccount.length() != 0)
					{
						tgpf_DepositDetailForm.setBankAccountForLoan(recipientAccount);
					}
					if(remark != null && remark.length() != 0)
					{
						tgpf_DepositDetailForm.setRemarkFromDepositBill(bankRemark);
					}
					if(reciName != null && reciName.length() != 0)
					{
						tgpf_DepositDetailForm.setTheNameOfCreditor(recipientName);
					}
					if(tradeAmount != null && tradeAmount.length() != 0)
					{
						tgpf_DepositDetailForm.setLoanAmountFromBank(income);
					}
					
					tgpf_DepositDetailForm.setTheState(S_TheState.Normal);
					tgpf_DepositDetailForm.setReconciliationStateFromCyberBank(0);
					
					Integer depositDetailCount = tgpf_DepositDetailDao.findByPage_Size(tgpf_DepositDetailDao.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), tgpf_DepositDetailForm));
					
					List<Tgpf_DepositDetail> tgpf_DepositDetailList;
					if(depositDetailCount > 0)
					{
						tgpf_DepositDetailList = tgpf_DepositDetailDao
								.findByPage(tgpf_DepositDetailDao.getQuery(tgpf_DepositDetailDao.getBasicHQL(), tgpf_DepositDetailForm));
						
						for(Tgpf_DepositDetail tgpf_DepositDetail : tgpf_DepositDetailList)
						{
							String tgpf_DepositDetailId = tgpf_DepositDetail.getTableId().toString();
							//key 存在
							if(idKeys.containsKey(tgpf_DepositDetailId)){						
								continue;
							}					
							// 更新交易明细表
							// tgpf_DepositDetail.setUserUpdate(userUpdate);
							tgpf_DepositDetail.setLastUpdateTimeStamp(System.currentTimeMillis());
							tgpf_DepositDetail.setReconciliationTimeStampFromCyberBank(reconciliationTime);
							tgpf_DepositDetail.setReconciliationStateFromCyberBank(1);

							tgpf_DepositDetailDao.save(tgpf_DepositDetail);
							
							// 更新对账明细表
							tgpf_CyberBankStatementDtl.setLastUpdateTimeStamp(System.currentTimeMillis());// 修改时间
							tgpf_CyberBankStatementDtl.setReconciliationState(1);// 业务对账状态
							tgpf_CyberBankStatementDtl.setReconciliationStamp(reconciliationTime);

							tgpf_CyberBankStatementDtl.setTgpf_DepositDetailId(tgpf_DepositDetail.getTableId());
							tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);
							
							idKeys.put(tgpf_DepositDetailId, tgpf_DepositDetailId);
							
							break;					
						}
					}
				}
			}
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
