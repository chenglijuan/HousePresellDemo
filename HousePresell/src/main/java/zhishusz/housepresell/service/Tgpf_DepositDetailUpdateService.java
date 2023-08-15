package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.po.Tgpf_DayEndBalancing;
import zhishusz.housepresell.database.dao.Tgpf_DayEndBalancingDao;

/*
 * Service更新操作：资金归集-明细表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_DepositDetailUpdateService
{
	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;
	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;
	@Autowired
	private Tgpf_DayEndBalancingDao tgpf_DayEndBalancingDao;
	
	public Properties execute(Tgpf_DepositDetailForm model)
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
		String eCodeFromPayment = model.geteCodeFromPayment();
		Integer fundProperty = model.getFundProperty();
		Long bankAccountEscrowedId = model.getBankAccountEscrowedId();
		Long bankBranchId = model.getBankBranchId();
		String theNameOfBankAccountEscrowed = model.getTheNameOfBankAccountEscrowed();
		String theAccountOfBankAccountEscrowed = model.getTheAccountOfBankAccountEscrowed();
		String theNameOfCreditor = model.getTheNameOfCreditor();
		String idType = model.getIdType();
		String idNumber = model.getIdNumber();
		String bankAccountForLoan = model.getBankAccountForLoan();
		Double loanAmountFromBank = model.getLoanAmountFromBank();
		String billTimeStamp = model.getBillTimeStamp();
		String eCodeFromBankCore = model.geteCodeFromBankCore();
		String eCodeFromBankPlatform = model.geteCodeFromBankPlatform();
		String remarkFromDepositBill = model.getRemarkFromDepositBill();
		Long theNameOfBankBranchFromDepositBillId = model.getTheNameOfBankBranchFromDepositBillId();
		String eCodeFromBankWorker = model.geteCodeFromBankWorker();
		Integer depositState = model.getDepositState();
		Long dayEndBalancingId = model.getDayEndBalancingId();
		String depositDatetime = model.getDepositDatetime();
		String reconciliationTimeStampFromBusiness = model.getReconciliationTimeStampFromBusiness();
		Integer reconciliationStateFromBusiness = model.getReconciliationStateFromBusiness();
		String reconciliationTimeStampFromCyberBank = model.getReconciliationTimeStampFromCyberBank();
		Integer reconciliationStateFromCyberBank = model.getReconciliationStateFromCyberBank();
		Boolean hasVoucher = model.getHasVoucher();
		String timestampFromReverse = model.getTimestampFromReverse();
		Integer theStateFromReverse = model.getTheStateFromReverse();
		String eCodeFromReverse = model.geteCodeFromReverse();
		
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
		if(eCodeFromPayment == null || eCodeFromPayment.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeFromPayment'不能为空");
		}
		if(fundProperty == null || fundProperty < 1)
		{
			return MyBackInfo.fail(properties, "'fundProperty'不能为空");
		}
		if(bankAccountEscrowedId == null || bankAccountEscrowedId < 1)
		{
			return MyBackInfo.fail(properties, "'bankAccountEscrowed'不能为空");
		}
		if(bankBranchId == null || bankBranchId < 1)
		{
			return MyBackInfo.fail(properties, "'bankBranch'不能为空");
		}
		if(theNameOfBankAccountEscrowed == null || theNameOfBankAccountEscrowed.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfBankAccountEscrowed'不能为空");
		}
		if(theAccountOfBankAccountEscrowed == null || theAccountOfBankAccountEscrowed.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theAccountOfBankAccountEscrowed'不能为空");
		}
		if(theNameOfCreditor == null || theNameOfCreditor.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfCreditor'不能为空");
		}
		if(idType == null || idType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'idType'不能为空");
		}
		if(idNumber == null || idNumber.length() == 0)
		{
			return MyBackInfo.fail(properties, "'idNumber'不能为空");
		}
		if(bankAccountForLoan == null || bankAccountForLoan.length() == 0)
		{
			return MyBackInfo.fail(properties, "'bankAccountForLoan'不能为空");
		}
		if(loanAmountFromBank == null || loanAmountFromBank < 1)
		{
			return MyBackInfo.fail(properties, "'loanAmountFromBank'不能为空");
		}
		if(billTimeStamp == null || billTimeStamp.length() == 0)
		{
			return MyBackInfo.fail(properties, "'billTimeStamp'不能为空");
		}
		if(eCodeFromBankCore == null || eCodeFromBankCore.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeFromBankCore'不能为空");
		}
		if(eCodeFromBankPlatform == null || eCodeFromBankPlatform.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeFromBankPlatform'不能为空");
		}
		if(remarkFromDepositBill == null || remarkFromDepositBill.length() == 0)
		{
			return MyBackInfo.fail(properties, "'remarkFromDepositBill'不能为空");
		}
		if(theNameOfBankBranchFromDepositBillId == null || theNameOfBankBranchFromDepositBillId < 1)
		{
			return MyBackInfo.fail(properties, "'theNameOfBankBranchFromDepositBill'不能为空");
		}
		if(eCodeFromBankWorker == null || eCodeFromBankWorker.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeFromBankWorker'不能为空");
		}
		if(depositState == null || depositState < 1)
		{
			return MyBackInfo.fail(properties, "'depositState'不能为空");
		}
		if(dayEndBalancingId == null || dayEndBalancingId < 1)
		{
			return MyBackInfo.fail(properties, "'dayEndBalancing'不能为空");
		}
		if(depositDatetime == null || depositDatetime.length() == 0)
		{
			return MyBackInfo.fail(properties, "'depositDatetime'不能为空");
		}
		if(reconciliationTimeStampFromBusiness == null || reconciliationTimeStampFromBusiness.length() == 0)
		{
			return MyBackInfo.fail(properties, "'reconciliationTimeStampFromBusiness'不能为空");
		}
		if(reconciliationStateFromBusiness == null || reconciliationStateFromBusiness < 1)
		{
			return MyBackInfo.fail(properties, "'reconciliationStateFromBusiness'不能为空");
		}
		if(reconciliationTimeStampFromCyberBank == null || reconciliationTimeStampFromCyberBank.length() == 0)
		{
			return MyBackInfo.fail(properties, "'reconciliationTimeStampFromCyberBank'不能为空");
		}
		if(reconciliationStateFromCyberBank == null || reconciliationStateFromCyberBank < 1)
		{
			return MyBackInfo.fail(properties, "'reconciliationStateFromCyberBank'不能为空");
		}
		if(hasVoucher == null)
		{
			return MyBackInfo.fail(properties, "'hasVoucher'不能为空");
		}
		if(timestampFromReverse == null || timestampFromReverse.length() == 0)
		{
			return MyBackInfo.fail(properties, "'timestampFromReverse'不能为空");
		}
		if(theStateFromReverse == null || theStateFromReverse < 1)
		{
			return MyBackInfo.fail(properties, "'theStateFromReverse'不能为空");
		}
		if(eCodeFromReverse == null || eCodeFromReverse.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeFromReverse'不能为空");
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
		Tgxy_BankAccountEscrowed bankAccountEscrowed = (Tgxy_BankAccountEscrowed)tgxy_BankAccountEscrowedDao.findById(bankAccountEscrowedId);
		if(bankAccountEscrowed == null)
		{
			return MyBackInfo.fail(properties, "'bankAccountEscrowed(Id:" + bankAccountEscrowedId + ")'不存在");
		}
		Emmp_BankBranch bankBranch = (Emmp_BankBranch)emmp_BankBranchDao.findById(bankBranchId);
		if(bankBranch == null)
		{
			return MyBackInfo.fail(properties, "'bankBranch(Id:" + bankBranchId + ")'不存在");
		}
		Emmp_BankBranch theNameOfBankBranchFromDepositBill = (Emmp_BankBranch)emmp_BankBranchDao.findById(theNameOfBankBranchFromDepositBillId);
		if(theNameOfBankBranchFromDepositBill == null)
		{
			return MyBackInfo.fail(properties, "'theNameOfBankBranchFromDepositBill(Id:" + theNameOfBankBranchFromDepositBillId + ")'不存在");
		}
		Tgpf_DayEndBalancing dayEndBalancing = (Tgpf_DayEndBalancing)tgpf_DayEndBalancingDao.findById(dayEndBalancingId);
		if(dayEndBalancing == null)
		{
			return MyBackInfo.fail(properties, "'dayEndBalancing(Id:" + dayEndBalancingId + ")'不存在");
		}
	
		Long tgpf_DepositDetailId = model.getTableId();
		Tgpf_DepositDetail tgpf_DepositDetail = (Tgpf_DepositDetail)tgpf_DepositDetailDao.findById(tgpf_DepositDetailId);
		if(tgpf_DepositDetail == null)
		{
			return MyBackInfo.fail(properties, "'Tgpf_DepositDetail(Id:" + tgpf_DepositDetailId + ")'不存在");
		}
		
		tgpf_DepositDetail.setTheState(theState);
		tgpf_DepositDetail.setBusiState(busiState);
		tgpf_DepositDetail.seteCode(eCode);
		tgpf_DepositDetail.setUserStart(userStart);
		tgpf_DepositDetail.setCreateTimeStamp(createTimeStamp);
		tgpf_DepositDetail.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgpf_DepositDetail.setUserRecord(userRecord);
		tgpf_DepositDetail.setRecordTimeStamp(recordTimeStamp);
		tgpf_DepositDetail.seteCodeFromPayment(eCodeFromPayment);
		tgpf_DepositDetail.setFundProperty(fundProperty);
		tgpf_DepositDetail.setBankAccountEscrowed(bankAccountEscrowed);
		tgpf_DepositDetail.setBankBranch(bankBranch);
		tgpf_DepositDetail.setTheNameOfBankAccountEscrowed(theNameOfBankAccountEscrowed);
		tgpf_DepositDetail.setTheAccountOfBankAccountEscrowed(theAccountOfBankAccountEscrowed);
		tgpf_DepositDetail.setTheNameOfCreditor(theNameOfCreditor);
		tgpf_DepositDetail.setIdType(idType);
		tgpf_DepositDetail.setIdNumber(idNumber);
		tgpf_DepositDetail.setBankAccountForLoan(bankAccountForLoan);
		tgpf_DepositDetail.setLoanAmountFromBank(loanAmountFromBank);
		tgpf_DepositDetail.setBillTimeStamp(billTimeStamp);
		tgpf_DepositDetail.seteCodeFromBankCore(eCodeFromBankCore);
		tgpf_DepositDetail.seteCodeFromBankPlatform(eCodeFromBankPlatform);
		tgpf_DepositDetail.setRemarkFromDepositBill(remarkFromDepositBill);
		tgpf_DepositDetail.setTheNameOfBankBranchFromDepositBill(theNameOfBankBranchFromDepositBill);
		tgpf_DepositDetail.seteCodeFromBankWorker(eCodeFromBankWorker);
		tgpf_DepositDetail.setDepositState(depositState);
		tgpf_DepositDetail.setDayEndBalancing(dayEndBalancing);
		tgpf_DepositDetail.setDepositDatetime(depositDatetime);
		tgpf_DepositDetail.setReconciliationTimeStampFromBusiness(reconciliationTimeStampFromBusiness);
		tgpf_DepositDetail.setReconciliationStateFromBusiness(reconciliationStateFromBusiness);
		tgpf_DepositDetail.setReconciliationTimeStampFromCyberBank(reconciliationTimeStampFromCyberBank);
		tgpf_DepositDetail.setReconciliationStateFromCyberBank(reconciliationStateFromCyberBank);
		tgpf_DepositDetail.setHasVoucher(hasVoucher);
		tgpf_DepositDetail.setTimestampFromReverse(timestampFromReverse);
		tgpf_DepositDetail.setTheStateFromReverse(theStateFromReverse);
		tgpf_DepositDetail.seteCodeFromReverse(eCodeFromReverse);
	
		tgpf_DepositDetailDao.save(tgpf_DepositDetail);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
