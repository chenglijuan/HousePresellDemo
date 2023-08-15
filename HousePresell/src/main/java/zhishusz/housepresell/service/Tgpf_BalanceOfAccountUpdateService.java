package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;

/*
 * Service更新操作：对账列表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_BalanceOfAccountUpdateService
{
	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;
	
	public Properties execute(Tgpf_BalanceOfAccountForm model)
	{
		Properties properties = new MyProperties();
		
		Integer theState = model.getTheState();
		String busiState = model.getBusiState();
		String eCode = model.geteCode();
		Long userStartId = model.getUserStartId();
		Long createTimeStamp = model.getCreateTimeStamp();
		Long userUpdateId = model.getUserUpdateId();
		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
		Long userRecordId = model.getUserRecordId();
		Long recordTimeStamp = model.getRecordTimeStamp();
		String billTimeStamp = model.getBillTimeStamp();
		String bankName = model.getBankName();
		String escrowedAccount = model.getEscrowedAccount();
		String escrowedAccountTheName = model.getEscrowedAccountTheName();
		Integer centerTotalCount = model.getCenterTotalCount();
		Double centerTotalAmount = model.getCenterTotalAmount();
		Integer bankTotalCount = model.getBankTotalCount();
		Double bankTotalAmount = model.getBankTotalAmount();
		Integer cyberBankTotalCount = model.getCyberBankTotalCount();
		Double cyberBankTotalAmount = model.getCyberBankTotalAmount();
		Integer accountType = model.getAccountType();
		String reconciliationDate = model.getReconciliationDate();
		Integer reconciliationState = model.getReconciliationState();
		Long bankBranchId = model.getBankBranchId();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(busiState == null || busiState.length() == 0)
		{
			return MyBackInfo.fail(properties, "'busiState'不能为空");
		}
		if(eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCode'不能为空");
		}
		if(userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(createTimeStamp == null || createTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'createTimeStamp'不能为空");
		}
		if(userUpdateId == null || userUpdateId < 1)
		{
			return MyBackInfo.fail(properties, "'userUpdate'不能为空");
		}
		if(lastUpdateTimeStamp == null || lastUpdateTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'lastUpdateTimeStamp'不能为空");
		}
		if(userRecordId == null || userRecordId < 1)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if(recordTimeStamp == null || recordTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'recordTimeStamp'不能为空");
		}
		if(billTimeStamp == null || billTimeStamp.length() == 0)
		{
			return MyBackInfo.fail(properties, "'billTimeStamp'不能为空");
		}
		if(bankName == null || bankName.length() == 0)
		{
			return MyBackInfo.fail(properties, "'bankName'不能为空");
		}
		if(escrowedAccount == null || escrowedAccount.length() == 0)
		{
			return MyBackInfo.fail(properties, "'escrowedAccount'不能为空");
		}
		if(escrowedAccountTheName == null || escrowedAccountTheName.length() == 0)
		{
			return MyBackInfo.fail(properties, "'escrowedAccountTheName'不能为空");
		}
		if(centerTotalCount == null || centerTotalCount < 1)
		{
			return MyBackInfo.fail(properties, "'centerTotalCount'不能为空");
		}
		if(centerTotalAmount == null || centerTotalAmount < 1)
		{
			return MyBackInfo.fail(properties, "'centerTotalAmount'不能为空");
		}
		if(bankTotalCount == null || bankTotalCount < 1)
		{
			return MyBackInfo.fail(properties, "'bankTotalCount'不能为空");
		}
		if(bankTotalAmount == null || bankTotalAmount < 1)
		{
			return MyBackInfo.fail(properties, "'bankTotalAmount'不能为空");
		}
		if(cyberBankTotalCount == null || cyberBankTotalCount < 1)
		{
			return MyBackInfo.fail(properties, "'cyberBankTotalCount'不能为空");
		}
		if(cyberBankTotalAmount == null || cyberBankTotalAmount < 1)
		{
			return MyBackInfo.fail(properties, "'cyberBankTotalAmount'不能为空");
		}
		if(accountType == null || accountType < 1)
		{
			return MyBackInfo.fail(properties, "'accountType'不能为空");
		}
		if(reconciliationDate == null || reconciliationDate.length() == 0)
		{
			return MyBackInfo.fail(properties, "'reconciliationDate'不能为空");
		}
		if(reconciliationState == null || reconciliationState < 1)
		{
			return MyBackInfo.fail(properties, "'reconciliationState'不能为空");
		}
		if(bankBranchId == null || bankBranchId < 1)
		{
			return MyBackInfo.fail(properties, "'bankBranch'不能为空");
		}
		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart(Id:" + userStartId + ")'不存在");
		}
		Sm_User userUpdate = (Sm_User)sm_UserDao.findById(userUpdateId);
		if(userUpdate == null)
		{
			return MyBackInfo.fail(properties, "'userUpdate(Id:" + userUpdateId + ")'不存在");
		}
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord(Id:" + userRecordId + ")'不存在");
		}
		Emmp_BankBranch bankBranch = (Emmp_BankBranch)emmp_BankBranchDao.findById(bankBranchId);
		if(bankBranch == null)
		{
			return MyBackInfo.fail(properties, "'bankBranch(Id:" + bankBranchId + ")'不存在");
		}
	
		Long tgpf_BalanceOfAccountId = model.getTableId();
		Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = (Tgpf_BalanceOfAccount)tgpf_BalanceOfAccountDao.findById(tgpf_BalanceOfAccountId);
		if(tgpf_BalanceOfAccount == null)
		{
			return MyBackInfo.fail(properties, "'Tgpf_BalanceOfAccount(Id:" + tgpf_BalanceOfAccountId + ")'不存在");
		}
		
		tgpf_BalanceOfAccount.setTheState(theState);
		tgpf_BalanceOfAccount.setBusiState(busiState);
		tgpf_BalanceOfAccount.seteCode(eCode);
		tgpf_BalanceOfAccount.setUserStart(userStart);
		tgpf_BalanceOfAccount.setCreateTimeStamp(createTimeStamp);
		tgpf_BalanceOfAccount.setUserUpdate(userUpdate);
		tgpf_BalanceOfAccount.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgpf_BalanceOfAccount.setUserRecord(userRecord);
		tgpf_BalanceOfAccount.setRecordTimeStamp(recordTimeStamp);
		tgpf_BalanceOfAccount.setBillTimeStamp(billTimeStamp);
		tgpf_BalanceOfAccount.setBankName(bankName);
		tgpf_BalanceOfAccount.setEscrowedAccount(escrowedAccount);
		tgpf_BalanceOfAccount.setEscrowedAccountTheName(escrowedAccountTheName);
		tgpf_BalanceOfAccount.setCenterTotalCount(centerTotalCount);
		tgpf_BalanceOfAccount.setCenterTotalAmount(centerTotalAmount);
		tgpf_BalanceOfAccount.setBankTotalCount(bankTotalCount);
		tgpf_BalanceOfAccount.setBankTotalAmount(bankTotalAmount);
		tgpf_BalanceOfAccount.setCyberBankTotalCount(cyberBankTotalCount);
		tgpf_BalanceOfAccount.setCyberBankTotalAmount(cyberBankTotalAmount);
		tgpf_BalanceOfAccount.setAccountType(accountType);
		tgpf_BalanceOfAccount.setReconciliationDate(reconciliationDate);
		tgpf_BalanceOfAccount.setReconciliationState(reconciliationState);
		tgpf_BalanceOfAccount.setBankBranch(bankBranch);
	
		tgpf_BalanceOfAccountDao.save(tgpf_BalanceOfAccount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
