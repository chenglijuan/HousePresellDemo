package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_BankUploadDataDetailForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_BankUploadDataDetailDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_BankUploadDataDetail;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：银行对账单数据
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_BankUploadDataDetailAddService
{
	@Autowired
	private Tgpf_BankUploadDataDetailDao tgpf_BankUploadDataDetailDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_BankInfoDao emmp_BankInfoDao;
	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;
	
	public Properties execute(Tgpf_BankUploadDataDetailForm model)
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
		Long bankId = model.getBankId();
		String theNameOfBank = model.getTheNameOfBank();
		Long bankBranchId = model.getBankBranchId();
		String theNameOfBankBranch = model.getTheNameOfBankBranch();
		Long bankAccountEscrowedId = model.getBankAccountEscrowedId();
		String theNameOfBankAccountEscrowed = model.getTheNameOfBankAccountEscrowed();
		String theAccountBankAccountEscrowed = model.getTheAccountBankAccountEscrowed();
		String theAccountOfBankAccountEscrowed = model.getTheAccountOfBankAccountEscrowed();
		Double tradeAmount = model.getTradeAmount();
		String enterTimeStamp = model.getEnterTimeStamp();
		String recipientAccount = model.getRecipientAccount();
		String recipientName = model.getRecipientName();
		String lastCfgUser = model.getLastCfgUser();
		Long lastCfgTimeStamp = model.getLastCfgTimeStamp();
		String bkpltNo = model.getBkpltNo();
		String eCodeOfTripleAgreement = model.geteCodeOfTripleAgreement();
		Integer reconciliationState = model.getReconciliationState();
		String reconciliationStamp = model.getReconciliationStamp();
		String remark = model.getRemark();
		String coreNo = model.getCoreNo();
		String reconciliationUser = model.getReconciliationUser();
		
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
		if(bankId == null || bankId < 1)
		{
			return MyBackInfo.fail(properties, "'bank'不能为空");
		}
		if(theNameOfBank == null || theNameOfBank.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfBank'不能为空");
		}
		if(bankBranchId == null || bankBranchId < 1)
		{
			return MyBackInfo.fail(properties, "'bankBranch'不能为空");
		}
		if(theNameOfBankBranch == null || theNameOfBankBranch.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfBankBranch'不能为空");
		}
		if(bankAccountEscrowedId == null || bankAccountEscrowedId < 1)
		{
			return MyBackInfo.fail(properties, "'bankAccountEscrowed'不能为空");
		}
		if(theNameOfBankAccountEscrowed == null || theNameOfBankAccountEscrowed.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfBankAccountEscrowed'不能为空");
		}
		if(theAccountBankAccountEscrowed == null || theAccountBankAccountEscrowed.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theAccountBankAccountEscrowed'不能为空");
		}
		if(theAccountOfBankAccountEscrowed == null || theAccountOfBankAccountEscrowed.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theAccountOfBankAccountEscrowed'不能为空");
		}
		if(tradeAmount == null || tradeAmount < 1)
		{
			return MyBackInfo.fail(properties, "'tradeAmount'不能为空");
		}
		if(enterTimeStamp == null || enterTimeStamp.length() == 0)
		{
			return MyBackInfo.fail(properties, "'enterTimeStamp'不能为空");
		}
		if(recipientAccount == null || recipientAccount.length() == 0)
		{
			return MyBackInfo.fail(properties, "'recipientAccount'不能为空");
		}
		if(recipientName == null || recipientName.length() == 0)
		{
			return MyBackInfo.fail(properties, "'recipientName'不能为空");
		}
		if(lastCfgUser == null || lastCfgUser.length() == 0)
		{
			return MyBackInfo.fail(properties, "'lastCfgUser'不能为空");
		}
		if(lastCfgTimeStamp == null || lastCfgTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'lastCfgTimeStamp'不能为空");
		}
		if(bkpltNo == null || bkpltNo.length() == 0)
		{
			return MyBackInfo.fail(properties, "'bkpltNo'不能为空");
		}
		if(eCodeOfTripleAgreement == null || eCodeOfTripleAgreement.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfTripleAgreement'不能为空");
		}
		if(reconciliationState == null || reconciliationState < 1)
		{
			return MyBackInfo.fail(properties, "'reconciliationState'不能为空");
		}
		if(reconciliationStamp == null || reconciliationStamp.length() == 0)
		{
			return MyBackInfo.fail(properties, "'reconciliationStamp'不能为空");
		}
		if(remark == null || remark.length() == 0)
		{
			return MyBackInfo.fail(properties, "'remark'不能为空");
		}
		if(coreNo == null || coreNo.length() == 0)
		{
			return MyBackInfo.fail(properties, "'coreNo'不能为空");
		}
		if(reconciliationUser == null || reconciliationUser.length() == 0)
		{
			return MyBackInfo.fail(properties, "'reconciliationUser'不能为空");
		}

		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		Emmp_BankInfo bank = (Emmp_BankInfo)emmp_BankInfoDao.findById(bankId);
		Emmp_BankBranch bankBranch = (Emmp_BankBranch)emmp_BankBranchDao.findById(bankBranchId);
		Tgxy_BankAccountEscrowed bankAccountEscrowed = (Tgxy_BankAccountEscrowed)tgxy_BankAccountEscrowedDao.findById(bankAccountEscrowedId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if(bank == null)
		{
			return MyBackInfo.fail(properties, "'bank'不能为空");
		}
		if(bankBranch == null)
		{
			return MyBackInfo.fail(properties, "'bankBranch'不能为空");
		}
		if(bankAccountEscrowed == null)
		{
			return MyBackInfo.fail(properties, "'bankAccountEscrowed'不能为空");
		}
	
		Tgpf_BankUploadDataDetail tgpf_BankUploadDataDetail = new Tgpf_BankUploadDataDetail();
		tgpf_BankUploadDataDetail.setTheState(theState);
		tgpf_BankUploadDataDetail.setBusiState(busiState);
		tgpf_BankUploadDataDetail.seteCode(eCode);
		tgpf_BankUploadDataDetail.setUserStart(userStart);
		tgpf_BankUploadDataDetail.setCreateTimeStamp(createTimeStamp);
		tgpf_BankUploadDataDetail.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgpf_BankUploadDataDetail.setUserRecord(userRecord);
		tgpf_BankUploadDataDetail.setRecordTimeStamp(recordTimeStamp);
		tgpf_BankUploadDataDetail.setBank(bank);
		tgpf_BankUploadDataDetail.setTheNameOfBank(theNameOfBank);
		tgpf_BankUploadDataDetail.setBankBranch(bankBranch);
		tgpf_BankUploadDataDetail.setTheNameOfBankBranch(theNameOfBankBranch);
		tgpf_BankUploadDataDetail.setBankAccountEscrowed(bankAccountEscrowed);
		tgpf_BankUploadDataDetail.setTheNameOfBankAccountEscrowed(theNameOfBankAccountEscrowed);
		tgpf_BankUploadDataDetail.setTheAccountBankAccountEscrowed(theAccountBankAccountEscrowed);
		tgpf_BankUploadDataDetail.setTheAccountOfBankAccountEscrowed(theAccountOfBankAccountEscrowed);
		tgpf_BankUploadDataDetail.setTradeAmount(tradeAmount);
		tgpf_BankUploadDataDetail.setEnterTimeStamp(enterTimeStamp);
		tgpf_BankUploadDataDetail.setRecipientAccount(recipientAccount);
		tgpf_BankUploadDataDetail.setRecipientName(recipientName);
		tgpf_BankUploadDataDetail.setLastCfgUser(lastCfgUser);
		tgpf_BankUploadDataDetail.setLastCfgTimeStamp(lastCfgTimeStamp);
		tgpf_BankUploadDataDetail.setBkpltNo(bkpltNo);
		tgpf_BankUploadDataDetail.seteCodeOfTripleAgreement(eCodeOfTripleAgreement);
		tgpf_BankUploadDataDetail.setReconciliationState(reconciliationState);
		tgpf_BankUploadDataDetail.setReconciliationStamp(reconciliationStamp);
		tgpf_BankUploadDataDetail.setRemark(remark);
		tgpf_BankUploadDataDetail.setCoreNo(coreNo);
		tgpf_BankUploadDataDetail.setReconciliationUser(reconciliationUser);
		tgpf_BankUploadDataDetailDao.save(tgpf_BankUploadDataDetail);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
