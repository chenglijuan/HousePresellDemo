package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Emmp_ComAccountForm;
import zhishusz.housepresell.database.dao.Emmp_ComAccountDao;
import zhishusz.housepresell.database.po.Emmp_ComAccount;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;

/*
 * Service更新操作：机构-财务账号信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_ComAccountUpdateService
{
	@Autowired
	private Emmp_ComAccountDao emmp_ComAccountDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;
	
	public Properties execute(Emmp_ComAccountForm model)
	{
		Properties properties = new MyProperties();
		
		Integer theState = model.getTheState();
		String busiState = model.getBusiState();
		String eCode = model.geteCode();
		Long userStartId = model.getUserStartId();
		Long createTimeStamp = model.getCreateTimeStamp();
		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
		Long userRecordId = model.getUserRecordId();
		Long recordTimeStamp = model.getRecordTimeStamp();
		String officeAddress = model.getOfficeAddress();
		String officePhone = model.getOfficePhone();
		Long bankBranchId = model.getBankBranchId();
		String theNameOfBank = model.getTheNameOfBank();
		String bankAccount = model.getBankAccount();
		String theNameOfBankAccount = model.getTheNameOfBankAccount();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(busiState == null || busiState.length()< 1)
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
		if(officeAddress == null || officeAddress.length() == 0)
		{
			return MyBackInfo.fail(properties, "'officeAddress'不能为空");
		}
		if(officePhone == null || officePhone.length() == 0)
		{
			return MyBackInfo.fail(properties, "'officePhone'不能为空");
		}
		if(bankBranchId == null || bankBranchId < 1)
		{
			return MyBackInfo.fail(properties, "'bankBranch'不能为空");
		}
		if(theNameOfBank == null || theNameOfBank.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfBank'不能为空");
		}
		if(bankAccount == null || bankAccount.length() == 0)
		{
			return MyBackInfo.fail(properties, "'bankAccount'不能为空");
		}
		if(theNameOfBankAccount == null || theNameOfBankAccount.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfBankAccount'不能为空");
		}
		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart(Id:" + userStartId + ")'不存在");
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
	
		Long emmp_ComAccountId = model.getTableId();
		Emmp_ComAccount emmp_ComAccount = (Emmp_ComAccount)emmp_ComAccountDao.findById(emmp_ComAccountId);
		if(emmp_ComAccount == null)
		{
			return MyBackInfo.fail(properties, "'Emmp_ComAccount(Id:" + emmp_ComAccountId + ")'不存在");
		}
		
		emmp_ComAccount.setTheState(theState);
		emmp_ComAccount.setBusiState(busiState);
		emmp_ComAccount.seteCode(eCode);
		emmp_ComAccount.setUserStart(userStart);
		emmp_ComAccount.setCreateTimeStamp(createTimeStamp);
		emmp_ComAccount.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		emmp_ComAccount.setUserRecord(userRecord);
		emmp_ComAccount.setRecordTimeStamp(recordTimeStamp);
		emmp_ComAccount.setOfficeAddress(officeAddress);
		emmp_ComAccount.setOfficePhone(officePhone);
		emmp_ComAccount.setBankBranch(bankBranch);
		emmp_ComAccount.setTheNameOfBank(theNameOfBank);
		emmp_ComAccount.setBankAccount(bankAccount);
		emmp_ComAccount.setTheNameOfBankAccount(theNameOfBankAccount);
	
		emmp_ComAccountDao.save(emmp_ComAccount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
