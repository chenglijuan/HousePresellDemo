package zhishusz.housepresell.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementForm;
import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDtlDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatementDtl;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
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
public class Tgpf_CyberBankStatementDtlConfirmService
{

	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;
	@Autowired
	private Tgpf_CyberBankStatementDao tgpf_CyberBankStatementDao;
	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;
	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;		//资金归集明细
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	MyDouble myDouble = MyDouble.getInstance();
	
	public Properties execute(Tgpf_BalanceOfAccountForm model)
	{
		Properties properties = new MyProperties();
		
		Long tgpf_BalanceOfAccountId = model.getTableId();
		
		Sm_User user = model.getUser();
		
		Tgpf_CyberBankStatement tgpf_CyberBankStatement = new Tgpf_CyberBankStatement();
		
		Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = (Tgpf_BalanceOfAccount)tgpf_BalanceOfAccountDao.findById(tgpf_BalanceOfAccountId);
		if(tgpf_BalanceOfAccount == null)
		{
			return MyBackInfo.fail(properties, "'Tgpf_BalanceOfAccount(Id:" + tgpf_BalanceOfAccountId + ")'不存在");
		}
		
		String theName = tgpf_BalanceOfAccount.getEscrowedAccountTheName();// 托管账户名称
		String theAccount = tgpf_BalanceOfAccount.getEscrowedAccount();// 托管账户
		String billTimeStamp = tgpf_BalanceOfAccount.getBillTimeStamp(); // 记账日期
				
		// 初始化：网银对账：已对账状态
		int bankReconciliationState = 1;

		// 查询网银表中，是否全部网银对账成功
		// 查询条件：1.托管账户 2.托管账号 3.网银对账状态：0 未对账 4.状态：正常
		Tgpf_CyberBankStatementForm tgpf_CyberBankStatementForm = new Tgpf_CyberBankStatementForm();
		tgpf_CyberBankStatementForm.setTheAccountOfBankAccountEscrowed(theAccount);
		tgpf_CyberBankStatementForm.setBillTimeStamp(billTimeStamp);
		tgpf_CyberBankStatementForm.setTheState(S_TheState.Normal);
		
		// 查询资金归集明细表，如果存在记录，则为未对账状态
		Integer tgpf_CyberBankCount = tgpf_CyberBankStatementDao.findByPage_Size(tgpf_CyberBankStatementDao.getQuery_Size(tgpf_CyberBankStatementDao.getBasicHQL(), tgpf_CyberBankStatementForm));
		
		List<Tgpf_CyberBankStatement> tgpf_CyberBankStatementList;
		
		// 不存在记录，则直接返回空列表
		if(tgpf_CyberBankCount > 0)
		{
			tgpf_CyberBankStatementList = tgpf_CyberBankStatementDao.findByPage(tgpf_CyberBankStatementDao.getQuery(tgpf_CyberBankStatementDao.getBasicHQL(), tgpf_CyberBankStatementForm));
			tgpf_CyberBankStatement = tgpf_CyberBankStatementList.get(0);
			
			// 查询网银上传明细
			// 查询条件：1.网银主键 2.状态为正常 3.对账状态为未对账
			Tgpf_CyberBankStatementDtlForm tgpf_CyberBankStatementDtlForm = new Tgpf_CyberBankStatementDtlForm();
			tgpf_CyberBankStatementDtlForm.setMainTable(tgpf_CyberBankStatement);
			tgpf_CyberBankStatementDtlForm.setTheState(S_TheState.Normal);
			tgpf_CyberBankStatementDtlForm.setReconciliationState(0);
			
			Integer cyberBankStatementDtlCount = tgpf_CyberBankStatementDtlDao.findByPage_Size(tgpf_CyberBankStatementDtlDao.getQuery_Size(tgpf_CyberBankStatementDtlDao.getBasicHQL(), tgpf_CyberBankStatementDtlForm));

			Tgpf_DepositDetailForm depositDetailForm = new Tgpf_DepositDetailForm();
//			depositDetailForm.setTheNameOfBankAccountEscrowed(theName);
			depositDetailForm.setTheAccountOfBankAccountEscrowed(theAccount);
			depositDetailForm.setTheState(S_TheState.Normal); // 状态为正常
			depositDetailForm.setBillTimeStamp(billTimeStamp);
			depositDetailForm.setTheStateFromReverse(0);
			
			// 增加查询条件 ：4.网银对账状态：0 未对账
			depositDetailForm.setReconciliationStateFromCyberBank(0);

			// 查询资金归集明细表，如果存在记录，则为未对账状态
			Integer busContrastCount = tgpf_DepositDetailDao.findByPage_Size(
					tgpf_DepositDetailDao.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));
		
			if(cyberBankStatementDtlCount > 0 || busContrastCount > 0)
			{
				bankReconciliationState = 0;
				return MyBackInfo.fail(properties, "存在未完成对账的记录");
			}
		}
		else
		{
			Tgpf_DepositDetailForm depositDetailForm = new Tgpf_DepositDetailForm();
//			depositDetailForm.setTheNameOfBankAccountEscrowed(theName);
			depositDetailForm.setTheAccountOfBankAccountEscrowed(theAccount);
			depositDetailForm.setTheState(S_TheState.Normal); // 状态为正常
			depositDetailForm.setBillTimeStamp(billTimeStamp);
			depositDetailForm.setTheStateFromReverse(0);
			
			// 增加查询条件 ：4.网银对账状态：0 未对账
			depositDetailForm.setReconciliationStateFromCyberBank(0);

			// 查询资金归集明细表，如果存在记录，则为未对账状态
			Integer busContrastCount = tgpf_DepositDetailDao.findByPage_Size(
					tgpf_DepositDetailDao.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));
		
			if( busContrastCount > 0)
			{
				bankReconciliationState = 0;
				return MyBackInfo.fail(properties, "请上传网银对账数据");
			}
		}
	
		if( null != tgpf_CyberBankStatement && 0 == bankReconciliationState)
		{
			tgpf_CyberBankStatement.setReconciliationState(1);
			tgpf_CyberBankStatement.setRecordTimeStamp(System.currentTimeMillis());
			
			
			tgpf_CyberBankStatementDao.save(tgpf_CyberBankStatement);
			
		}
		
		tgpf_BalanceOfAccount.setAccountType(bankReconciliationState);

		tgpf_BalanceOfAccount.setUserUpdate(user);
		tgpf_BalanceOfAccount.setLastUpdateTimeStamp(System.currentTimeMillis());
	
		tgpf_BalanceOfAccountDao.save(tgpf_BalanceOfAccount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
