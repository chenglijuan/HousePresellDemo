package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.ObjectCopier;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

/*
 * Service更新操作：托管账户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_BankAccountEscrowedUpdateService
{
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;
	@Autowired
	private Sm_BusiState_LogAddService logAddService;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Emmp_BankInfoDao emmp_BankInfoDao;
	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;
	@Autowired
	private AttachmentJudgeExistUtil attachmentJudgeExistUtil;
	

	//附件相关
	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
	public Properties execute(Tgxy_BankAccountEscrowedForm model)
	{
		Properties properties = new MyProperties();
		
//		Integer theState = model.getTheState();
		String busiState = model.getBusiState();
		String eCode = model.geteCode();
		Long userStartId = model.getUserStartId();
//		Long createTimeStamp = model.getCreateTimeStamp();
//		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
		Long userRecordId = model.getUserRecordId();
		Long recordTimeStamp = model.getRecordTimeStamp();
		Long companyId = model.getCompanyId();
		Long projectId = model.getProjectId();
//		Long bankId = model.getBankId();
		String theNameOfBank = model.getTheNameOfBank();
		String shortNameOfBank = model.getShortNameOfBank();
		Long bankBranchId = model.getBankBranchId();
		String theName = model.getTheName();
		String theAccount = model.getTheAccount();
		String remark = model.getRemark();
		String contactPerson = model.getContactPerson();
		String contactPhone = model.getContactPhone();
		Long updatedStamp = model.getUpdatedStamp();
		Double income = model.getIncome();
		Double payout = model.getPayout();
		Double certOfDeposit = model.getCertOfDeposit();
		Double structuredDeposit = model.getStructuredDeposit();
		Double breakEvenFinancial = model.getBreakEvenFinancial();
		Double currentBalance = model.getCurrentBalance();
		Double largeRatio = model.getLargeRatio();
		Double largeAndCurrentRatio = model.getLargeAndCurrentRatio();
		Double financialRatio = model.getFinancialRatio();
		Double totalFundsRatio = model.getTotalFundsRatio();
		Integer isUsing = model.getIsUsing();

//		if(theState == null || theState < 1)
//		{
//			return MyBackInfo.fail(properties, "状态 S_TheState 初始为Normal不能为空");
//		}
//		if(busiState == null || busiState.length()< 1)
//		{
//			return MyBackInfo.fail(properties, "业务状态不能为空");
//		}
		if(theAccount == null || theAccount.length() == 0)
		{
			return MyBackInfo.fail(properties, "托管账户不能为空");
		}
//		if(userStartId == null || userStartId < 1)
//		{
//			return MyBackInfo.fail(properties, "创建人不能为空");
//		}
//		if(createTimeStamp == null || createTimeStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "创建时间不能为空");
//		}
//		if(lastUpdateTimeStamp == null || lastUpdateTimeStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "最后修改日期不能为空");
//		}
//		if(userRecordId == null || userRecordId < 1)
//		{
//			return MyBackInfo.fail(properties, "备案人不能为空");
//		}
//		if(recordTimeStamp == null || recordTimeStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "备案日期不能为空");
//		}
//		if(companyId == null || companyId < 1)
//		{
//			return MyBackInfo.fail(properties, "所属机构不能为空");
//		}
//		if(projectId == null || projectId < 1)
//		{
//			return MyBackInfo.fail(properties, "所属项目不能为空");
//		}
//		if(bankId == null || bankId < 1)
//		{
//			return MyBackInfo.fail(properties, "所属银行不能为空");
//		}
//		if(theNameOfBank == null || theNameOfBank.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "开户行名称/银行名称-冗余不能为空");
//		}
//		if(shortNameOfBank == null || shortNameOfBank.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "银行简称不能为空");
//		}
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "托管账户名称不能为空");
		}
		if(bankBranchId == null || bankBranchId < 1)
		{
			return MyBackInfo.fail(properties, "开户行不能为空");
		}
		if (isUsing == null)
		{
			return MyBackInfo.fail(properties, "请选择是否启用");
		}

//		if(theAccount == null || theAccount.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "账号不能为空");
//		}
//		if(remark == null || remark.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "备注不能为空");
//		}
//		if(contactPerson == null || contactPerson.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "联系人-姓名不能为空");
//		}
//		if(contactPhone == null || contactPhone.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "联系人-手机号不能为空");
//		}
//		if(updatedStamp == null || updatedStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "更新日期不能为空");
//		}
//		if(income == null || income < 1)
//		{
//			return MyBackInfo.fail(properties, "托管收入不能为空");
//		}
//		if(payout == null || payout < 1)
//		{
//			return MyBackInfo.fail(properties, "托管支出不能为空");
//		}
//		if(certOfDeposit == null || certOfDeposit < 1)
//		{
//			return MyBackInfo.fail(properties, "大额存单不能为空");
//		}
//		if(structuredDeposit == null || structuredDeposit < 1)
//		{
//			return MyBackInfo.fail(properties, "结构性存款不能为空");
//		}
//		if(breakEvenFinancial == null || breakEvenFinancial < 1)
//		{
//			return MyBackInfo.fail(properties, "保本理财不能为空");
//		}
//		if(currentBalance == null || currentBalance < 1)
//		{
//			return MyBackInfo.fail(properties, "活期余额不能为空");
//		}
//		if(largeRatio == null || largeRatio < 1)
//		{
//			return MyBackInfo.fail(properties, "大额占比不能为空");
//		}
//		if(largeAndCurrentRatio == null || largeAndCurrentRatio < 1)
//		{
//			return MyBackInfo.fail(properties, "大额+活期占比不能为空");
//		}
//		if(financialRatio == null || financialRatio < 1)
//		{
//			return MyBackInfo.fail(properties, "理财占比不能为空");
//		}
//		if(totalFundsRatio == null || totalFundsRatio < 1)
//		{
//			return MyBackInfo.fail(properties, "总资金沉淀占比不能为空");
//		}
		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
//		if(userStart == null)
//		{
//			return MyBackInfo.fail(properties, "'userStart(Id:" + userStartId + ")'不存在");
//		}
//		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
//		if(userRecord == null)
//		{
//			return MyBackInfo.fail(properties, "'userRecord(Id:" + userRecordId + ")'不存在");
//		}
//		Emmp_CompanyInfo company = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(companyId);
//		if(company == null)
//		{
//			return MyBackInfo.fail(properties, "'company(Id:" + companyId + ")'不存在");
//		}
//		Empj_ProjectInfo project = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(projectId);
//		if(project == null)
//		{
//			return MyBackInfo.fail(properties, "'project(Id:" + projectId + ")'不存在");
//		}
//		Emmp_BankInfo bank = (Emmp_BankInfo)emmp_BankInfoDao.findById(bankId);
//		if(bank == null)
//		{
//			return MyBackInfo.fail(properties, "'bank(Id:" + bankId + ")'不存在");
//		}
		Emmp_BankBranch bankBranch = (Emmp_BankBranch)emmp_BankBranchDao.findById(bankBranchId);
		if(bankBranch == null)
		{
			return MyBackInfo.fail(properties, "该开户行不存在");
		}
		Emmp_BankInfo bank = bankBranch.getBank();

		Long tgxy_BankAccountEscrowedId = model.getTableId();
		Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowed = (Tgxy_BankAccountEscrowed)tgxy_BankAccountEscrowedDao.findById(tgxy_BankAccountEscrowedId);
		Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowedOld = ObjectCopier.copy(tgxy_BankAccountEscrowed);
		if(tgxy_BankAccountEscrowed == null)
		{
			return MyBackInfo.fail(properties, "该托管账户不存在");
		}
		MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);
		if(!msgInfo.isSuccess()){
			return MyBackInfo.fail(properties, msgInfo.getInfo());
		}

		//逻辑开始
//		if(!Validator.isMobile(contactPhone)){
//			return MyBackInfo.fail(properties, S_NormalFlag.info_PhoneNumberFail);
//		}

		long lastUpdateTimeStamp=System.currentTimeMillis();
		if(recordTimeStamp!=null){
			recordTimeStamp=System.currentTimeMillis();
		}
		
//		tgxy_BankAccountEscrowed.setTheState(theState);
		tgxy_BankAccountEscrowed.setBusiState(busiState);
		tgxy_BankAccountEscrowed.seteCode(eCode);
		tgxy_BankAccountEscrowed.setUserStart(userStart);
//		tgxy_BankAccountEscrowed.setCreateTimeStamp(createTimeStamp);
		tgxy_BankAccountEscrowed.setUserUpdate(model.getUser());
		tgxy_BankAccountEscrowed.setLastUpdateTimeStamp(System.currentTimeMillis());
//		tgxy_BankAccountEscrowed.setUserRecord(userRecord);
		tgxy_BankAccountEscrowed.setRecordTimeStamp(recordTimeStamp);
//		tgxy_BankAccountEscrowed.setCompany(company);
//		tgxy_BankAccountEscrowed.setProject(project);
		tgxy_BankAccountEscrowed.setBank(bank);
		tgxy_BankAccountEscrowed.setTheNameOfBank(theNameOfBank);
		tgxy_BankAccountEscrowed.setShortNameOfBank(shortNameOfBank);
		tgxy_BankAccountEscrowed.setBankBranch(bankBranch);
		tgxy_BankAccountEscrowed.setTheName(theName);
		tgxy_BankAccountEscrowed.setTheAccount(theAccount);
		tgxy_BankAccountEscrowed.setRemark(remark);
		tgxy_BankAccountEscrowed.setContactPerson(contactPerson);
		tgxy_BankAccountEscrowed.setContactPhone(contactPhone);
		tgxy_BankAccountEscrowed.setUpdatedStamp(updatedStamp);
		tgxy_BankAccountEscrowed.setIncome(income);
		tgxy_BankAccountEscrowed.setPayout(payout);
		tgxy_BankAccountEscrowed.setCertOfDeposit(certOfDeposit);
		tgxy_BankAccountEscrowed.setStructuredDeposit(structuredDeposit);
		tgxy_BankAccountEscrowed.setBreakEvenFinancial(breakEvenFinancial);
		tgxy_BankAccountEscrowed.setCurrentBalance(currentBalance);
		tgxy_BankAccountEscrowed.setLargeRatio(largeRatio);
		tgxy_BankAccountEscrowed.setLargeAndCurrentRatio(largeAndCurrentRatio);
		tgxy_BankAccountEscrowed.setFinancialRatio(financialRatio);
		tgxy_BankAccountEscrowed.setTotalFundsRatio(totalFundsRatio);
		tgxy_BankAccountEscrowed.setIsUsing(isUsing);

		tgxy_BankAccountEscrowedDao.save(tgxy_BankAccountEscrowed);
//		logAddService.addLog(model, tgxy_BankAccountEscrowedId, tgxy_BankAccountEscrowedOld, tgxy_BankAccountEscrowed);
		sm_AttachmentBatchAddService.execute(model, model.getTableId());

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tableId", tgxy_BankAccountEscrowed.getTableId());
		
		return properties;
	}
}
