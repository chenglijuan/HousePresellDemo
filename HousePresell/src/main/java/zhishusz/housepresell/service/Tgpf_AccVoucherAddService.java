package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_AccVoucherForm;
import zhishusz.housepresell.database.dao.Tgpf_AccVoucherDao;
import zhishusz.housepresell.database.po.Tgpf_AccVoucher;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
	
/*
 * Service添加操作：推送给财务系统-凭证
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_AccVoucherAddService
{
	@Autowired
	private Tgpf_AccVoucherDao tgpf_AccVoucherDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Emmp_BankInfoDao emmp_BankInfoDao;
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;
	
	public Properties execute(Tgpf_AccVoucherForm model)
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
		String billTimeStamp = model.getBillTimeStamp();
		String theType = model.getTheType();
		Integer tradeCount = model.getTradeCount();
		Double totalTradeAmount = model.getTotalTradeAmount();
		String contentJson = model.getContentJson();
		String payoutTimeStamp = model.getPayoutTimeStamp();
		Long companyId = model.getCompanyId();
		String theNameOfCompany = model.getTheNameOfCompany();
		Long projectId = model.getProjectId();
		String theNameOfProject = model.getTheNameOfProject();
		Long bankId = model.getBankId();
		String theNameOfBank = model.getTheNameOfBank();
		Integer DayEndBalancingState = model.getDayEndBalancingState();
		Long bankAccountEscrowedId = model.getBankAccountEscrowedId();
		String theAccountOfBankAccountEscrowed = model.getTheAccountOfBankAccountEscrowed();
		Double payoutAmount = model.getPayoutAmount();
		
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
		if(billTimeStamp == null || billTimeStamp.length() == 0)
		{
			return MyBackInfo.fail(properties, "'billTimeStamp'不能为空");
		}
		if(theType == null || theType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theType'不能为空");
		}
		if(tradeCount == null || tradeCount < 1)
		{
			return MyBackInfo.fail(properties, "'tradeCount'不能为空");
		}
		if(totalTradeAmount == null || totalTradeAmount < 1)
		{
			return MyBackInfo.fail(properties, "'totalTradeAmount'不能为空");
		}
		if(contentJson == null || contentJson.length() == 0)
		{
			return MyBackInfo.fail(properties, "'contentJson'不能为空");
		}
		if(payoutTimeStamp == null || payoutTimeStamp.length() == 0)
		{
			return MyBackInfo.fail(properties, "'payoutTimeStamp'不能为空");
		}
		if(companyId == null || companyId < 1)
		{
			return MyBackInfo.fail(properties, "'company'不能为空");
		}
		if(theNameOfCompany == null || theNameOfCompany.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfCompany'不能为空");
		}
		if(projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "'project'不能为空");
		}
		if(theNameOfProject == null || theNameOfProject.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfProject'不能为空");
		}
		if(bankId == null || bankId < 1)
		{
			return MyBackInfo.fail(properties, "'bank'不能为空");
		}
		if(theNameOfBank == null || theNameOfBank.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfBank'不能为空");
		}
		if(DayEndBalancingState == null || DayEndBalancingState < 1)
		{
			return MyBackInfo.fail(properties, "'DayEndBalancingState'不能为空");
		}
		if(bankAccountEscrowedId == null || bankAccountEscrowedId < 1)
		{
			return MyBackInfo.fail(properties, "'bankAccountEscrowed'不能为空");
		}
		if(theAccountOfBankAccountEscrowed == null || theAccountOfBankAccountEscrowed.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theAccountOfBankAccountEscrowed'不能为空");
		}
		if(payoutAmount == null || payoutAmount < 1)
		{
			return MyBackInfo.fail(properties, "'payoutAmount'不能为空");
		}

		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		Emmp_CompanyInfo company = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(companyId);
		Empj_ProjectInfo project = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(projectId);
		Emmp_BankInfo bank = (Emmp_BankInfo)emmp_BankInfoDao.findById(bankId);
		Tgxy_BankAccountEscrowed bankAccountEscrowed = (Tgxy_BankAccountEscrowed)tgxy_BankAccountEscrowedDao.findById(bankAccountEscrowedId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if(company == null)
		{
			return MyBackInfo.fail(properties, "'company'不能为空");
		}
		if(project == null)
		{
			return MyBackInfo.fail(properties, "'project'不能为空");
		}
		if(bank == null)
		{
			return MyBackInfo.fail(properties, "'bank'不能为空");
		}
		if(bankAccountEscrowed == null)
		{
			return MyBackInfo.fail(properties, "'bankAccountEscrowed'不能为空");
		}
	
		Tgpf_AccVoucher tgpf_AccVoucher = new Tgpf_AccVoucher();
		tgpf_AccVoucher.setTheState(theState);
		tgpf_AccVoucher.setBusiState(busiState);
		tgpf_AccVoucher.seteCode(eCode);
		tgpf_AccVoucher.setUserStart(userStart);
		tgpf_AccVoucher.setCreateTimeStamp(createTimeStamp);
		tgpf_AccVoucher.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgpf_AccVoucher.setUserRecord(userRecord);
		tgpf_AccVoucher.setRecordTimeStamp(recordTimeStamp);
		tgpf_AccVoucher.setBillTimeStamp(billTimeStamp);
		tgpf_AccVoucher.setTheType(theType);
		tgpf_AccVoucher.setTradeCount(tradeCount);
		tgpf_AccVoucher.setTotalTradeAmount(totalTradeAmount);
		tgpf_AccVoucher.setContentJson(contentJson);
		tgpf_AccVoucher.setPayoutTimeStamp(payoutTimeStamp);
		tgpf_AccVoucher.setCompany(company);
		tgpf_AccVoucher.setTheNameOfCompany(theNameOfCompany);
		tgpf_AccVoucher.setProject(project);
		tgpf_AccVoucher.setTheNameOfProject(theNameOfProject);
		tgpf_AccVoucher.setBank(bank);
		tgpf_AccVoucher.setTheNameOfBank(theNameOfBank);
		tgpf_AccVoucher.setDayEndBalancingState(DayEndBalancingState);
		tgpf_AccVoucher.setBankAccountEscrowed(bankAccountEscrowed);
		tgpf_AccVoucher.setTheAccountOfBankAccountEscrowed(theAccountOfBankAccountEscrowed);
		tgpf_AccVoucher.setPayoutAmount(payoutAmount);
		tgpf_AccVoucherDao.save(tgpf_AccVoucher);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
