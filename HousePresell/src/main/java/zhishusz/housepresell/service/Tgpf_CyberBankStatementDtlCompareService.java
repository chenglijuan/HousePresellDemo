package zhishusz.housepresell.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDtlDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatementDtl;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.json.JSONUtil;

/*
 * Service添加操作：网银对账-单个对账方法
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_CyberBankStatementDtlCompareService
{

	@Autowired
	private Tgpf_CyberBankStatementDao tgpf_CyberBankStatementDao;
	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;
	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	MyDouble myDouble = MyDouble.getInstance();
	
	public Properties execute(Tgpf_CyberBankStatementDtlForm model)
	{
		String jsonMsg = model.getTgpf_CyberBankStatementDtlContrastDetailList();
		
		model = (Tgpf_CyberBankStatementDtlForm) JSONUtil.toBean(jsonMsg, Tgpf_CyberBankStatementDtlForm.class);
		
		Properties properties = new MyProperties();
		// 保存这次对账已经匹配的主键
		Map idKeys = new HashMap();
		
		List<Tgpf_CyberBankStatementDtlForm> tgpf_CyberBankStatementDtlList = model.getTgpf_CyberBankStatementDtl();		
		// 对账日期
		String reconciliationTime = myDatetime.dateToSimpleString(System.currentTimeMillis());
		
		for(Tgpf_CyberBankStatementDtlForm tgpf_CyberBankStatementDtlForm :tgpf_CyberBankStatementDtlList)
		{		
			Long tableId = tgpf_CyberBankStatementDtlForm.getTableId();
			if( null == tableId || tableId < 0 )
			{
				continue;
			}
			
			String cyBankTripleAgreementNum = tgpf_CyberBankStatementDtlForm.getCyBankTripleAgreementNum();
			
			Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = (Tgpf_CyberBankStatementDtl)tgpf_CyberBankStatementDtlDao.findById(tableId);			
			// 交易日期
			String tradeTimeStamp = tgpf_CyberBankStatementDtl.getTradeTimeStamp();
			// 对方账号
			String recipientAccount = tgpf_CyberBankStatementDtl.getRecipientAccount();
			if( recipientAccount == null || recipientAccount.length() == 0)
			{
				tgpf_CyberBankStatementDtl.setRecipientAccount("");
				recipientAccount = "";
			}
			// 对方账户名称
			String recipientName = tgpf_CyberBankStatementDtl.getRecipientName();
			if( recipientName == null || recipientName.length() == 0)
			{
				tgpf_CyberBankStatementDtl.setRecipientName("");
				recipientName = "";
			}
			// 交易金额
			Double income = tgpf_CyberBankStatementDtl.getIncome();
			// 备注
			String bankRemark = tgpf_CyberBankStatementDtl.getRemark();
			if( bankRemark == null || bankRemark.length() == 0)
			{
				tgpf_CyberBankStatementDtl.setRemark("");
				bankRemark = "";
			}
			// 取得主表
			Tgpf_CyberBankStatement tgpf_CyberBankStatement = tgpf_CyberBankStatementDtl.getMainTable();
			// 监管账号
			String theAccount = tgpf_CyberBankStatement.getTheAccountOfBankAccountEscrowed();
			
			if(tgpf_CyberBankStatementDtl.getReconciliationState() == 1)
			{
				continue;
			}
			
			// 匹配网银对账
			// 查询条件：1.交易日期 2.对方账号 3.对方账号名称 4.交易金额  5.备注 6.状态为正常 7. 未对账
			Tgpf_DepositDetailForm tgpf_DepositDetailForm = new Tgpf_DepositDetailForm();
			
			tgpf_DepositDetailForm.setBillTimeStamp(tradeTimeStamp);
			
			tgpf_DepositDetailForm.setTheState(S_TheState.Normal);
			tgpf_DepositDetailForm.setReconciliationStateFromCyberBank(0);
			tgpf_DepositDetailForm.setTheAccountOfBankAccountEscrowed(theAccount);
			tgpf_DepositDetailForm.setTheStateFromReverse(0);
			
			if(cyBankTripleAgreementNum != null && cyBankTripleAgreementNum.length() != 0)
			{
				Tgxy_TripleAgreementForm Tgxy_TripleAgreementForm = new Tgxy_TripleAgreementForm();
				Tgxy_TripleAgreementForm.setTheState(S_TheState.Normal);
				Tgxy_TripleAgreementForm.seteCodeOfTripleAgreement(cyBankTripleAgreementNum);
				Integer tripleAgreementCount = tgxy_TripleAgreementDao.findByPage_Size(tgxy_TripleAgreementDao.getQuery_Size(tgxy_TripleAgreementDao.getBasicHQL(), Tgxy_TripleAgreementForm));
				
				List<Tgxy_TripleAgreement> tgxy_TripleAgreementList;
				if(tripleAgreementCount > 0)
				{
					tgxy_TripleAgreementList = tgxy_TripleAgreementDao.findByPage(tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getBasicHQL(), Tgxy_TripleAgreementForm));
					tgpf_DepositDetailForm.setTripleAgreement(tgxy_TripleAgreementList.get(0));
				}				
			}
			else
			{
				continue;
			}
			
			tgpf_DepositDetailForm.setLoanAmountFromBank(income);
							
			Integer depositDetailCount = tgpf_DepositDetailDao.findByPage_Size(tgpf_DepositDetailDao.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), tgpf_DepositDetailForm));
			
			// 判断 1对1 关系（根据金额查找不到的时候，判断是否是多条相加）
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
			}else{
				// 判断 1 对 多关系
				// 匹配网银对账
				// 查询条件：1.交易日期 2.对方账号 3.对方账号名称 4.交易金额  5.备注 6.状态为正常 7. 未对账
				Tgpf_DepositDetailForm depositDetailForm = new Tgpf_DepositDetailForm();
				
				depositDetailForm.setBillTimeStamp(tradeTimeStamp);
				
				depositDetailForm.setTheState(S_TheState.Normal);
				depositDetailForm.setReconciliationStateFromCyberBank(0);
				depositDetailForm.setTheAccountOfBankAccountEscrowed(theAccount);
				depositDetailForm.setTheStateFromReverse(0);
				
				if(cyBankTripleAgreementNum != null && cyBankTripleAgreementNum.length() != 0)
				{
					Tgxy_TripleAgreementForm Tgxy_TripleAgreementForm = new Tgxy_TripleAgreementForm();
					Tgxy_TripleAgreementForm.setTheState(S_TheState.Normal);
					Tgxy_TripleAgreementForm.seteCodeOfTripleAgreement(cyBankTripleAgreementNum);
					Integer tripleAgreementCount = tgxy_TripleAgreementDao.findByPage_Size(tgxy_TripleAgreementDao.getQuery_Size(tgxy_TripleAgreementDao.getBasicHQL(), Tgxy_TripleAgreementForm));
					
					List<Tgxy_TripleAgreement> tgxy_TripleAgreementList;
					if(tripleAgreementCount > 0)
					{
						tgxy_TripleAgreementList = tgxy_TripleAgreementDao.findByPage(tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getBasicHQL(), Tgxy_TripleAgreementForm));
						tgpf_DepositDetailForm.setTripleAgreement(tgxy_TripleAgreementList.get(0));
					}	
				}
				
				// 查询中心业务总额度
				String queryAmountCondition = " nvl(sum(loanAmountFromBank),0) ";

				Double totalAmount = (Double)
						 tgpf_DepositDetailDao.findOneByQuery(tgpf_DepositDetailDao.getSpecialQuery(
								tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm, queryAmountCondition));
							
				Integer detailCount = tgpf_DepositDetailDao.findByPage_Size(tgpf_DepositDetailDao.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));

				if( detailCount > 0 && totalAmount.toString().equals(income.toString()))
				{
					tgpf_DepositDetailList = tgpf_DepositDetailDao
							.findByPage(tgpf_DepositDetailDao.getQuery(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));
					
					for(Tgpf_DepositDetail tgpf_DepositDetail : tgpf_DepositDetailList)
					{
						String tgpf_DepositDetailId = tgpf_DepositDetail.getTableId().toString();
						
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
					}
				}
			
			}
		}				
					
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
