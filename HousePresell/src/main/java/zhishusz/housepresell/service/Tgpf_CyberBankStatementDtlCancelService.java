package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementForm;
import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDtlDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
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

/*
 * Service添加操作：网银对账-撤销
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_CyberBankStatementDtlCancelService
{

	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;
	@Autowired
	private Tgpf_CyberBankStatementDao tgpf_CyberBankStatementDao;
	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;
	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	MyDouble myDouble = MyDouble.getInstance();
	
	public Properties execute(Tgpf_BalanceOfAccountForm model)
	{
		
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		for(int i = 0; i< idArr.length ; i++)
		{
			Long tgpf_BalanceOfAccountId = idArr[i];
			
			Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = (Tgpf_BalanceOfAccount)tgpf_BalanceOfAccountDao.findById(tgpf_BalanceOfAccountId);
			
			if(null != tgpf_BalanceOfAccount.getAccountType() && tgpf_BalanceOfAccount.getAccountType() == 1)
			{
				return MyBackInfo.fail(properties, "已经网银对账的数据不允许撤销！");
			}		
			
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
				
				Integer cyberBankStatementDtlCount = tgpf_CyberBankStatementDtlDao.findByPage_Size(tgpf_CyberBankStatementDtlDao.getQuery_Size(tgpf_CyberBankStatementDtlDao.getBasicHQL(), tgpf_CyberBankStatementDtlForm));

				if(cyberBankStatementDtlCount > 0)
				{
					tgpf_CyberBankStatementDtlList = tgpf_CyberBankStatementDtlDao.findByPage(tgpf_CyberBankStatementDtlDao.getQuery(tgpf_CyberBankStatementDtlDao.getBasicHQL(), tgpf_CyberBankStatementDtlForm));
					for(Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl : tgpf_CyberBankStatementDtlList)
					{						
						tgpf_CyberBankStatementDtl.setLastUpdateTimeStamp(System.currentTimeMillis());// 修改时间
						tgpf_CyberBankStatementDtl.setReconciliationState(0);// 业务对账状态
						tgpf_CyberBankStatementDtl.setReconciliationStamp(null);
						tgpf_CyberBankStatementDtl.setTgpf_DepositDetailId(null);
						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);
					}
				}
			}
			
			// 查询交易明细表中，是否全部业务对账成功
			// 查询条件：1.托管账户 2.托管账号 3.状态：正常
			Tgpf_DepositDetailForm tgpf_depositDetailForm = new Tgpf_DepositDetailForm();
			tgpf_depositDetailForm.setTheNameOfBankAccountEscrowed(theName);
			tgpf_depositDetailForm.setTheAccountOfBankAccountEscrowed(theAccount);
			tgpf_depositDetailForm.setTheState(S_TheState.Normal); // 状态为正常
			tgpf_depositDetailForm.setBillTimeStamp(billTimeStamp);
			
			Integer depositDetailCount = tgpf_DepositDetailDao.findByPage_Size(tgpf_DepositDetailDao.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), tgpf_depositDetailForm));
			
			List<Tgpf_DepositDetail> tgpf_DepositDetailList;
			if(depositDetailCount > 0)
			{
				tgpf_DepositDetailList = tgpf_DepositDetailDao.findByPage(tgpf_DepositDetailDao.getQuery(tgpf_DepositDetailDao.getBasicHQL(), tgpf_depositDetailForm));

				for(Tgpf_DepositDetail tgpf_DepositDetail : tgpf_DepositDetailList)
				{
					// 更新交易明细表
					// tgpf_DepositDetail.setUserUpdate(userUpdate);
					tgpf_DepositDetail.setLastUpdateTimeStamp(System.currentTimeMillis());
					tgpf_DepositDetail.setReconciliationTimeStampFromCyberBank(null);
					tgpf_DepositDetail.setReconciliationStateFromCyberBank(0);
					
					tgpf_DepositDetailDao.save(tgpf_DepositDetail);
				}
			}
			// 更新网银和业务对账列表
			tgpf_BalanceOfAccount.setLastUpdateTimeStamp(System.currentTimeMillis());;
			tgpf_BalanceOfAccount.setAccountType(0);
			
			tgpf_BalanceOfAccountDao.save(tgpf_BalanceOfAccount);
		}
	
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
