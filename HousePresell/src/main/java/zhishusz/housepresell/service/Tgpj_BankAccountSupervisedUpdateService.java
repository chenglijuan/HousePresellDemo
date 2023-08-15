package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpj_BankAccountSupervisedForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpj_BankAccountSupervisedDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
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
 * Service更新操作：监管账户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BankAccountSupervisedUpdateService
{
	@Autowired
	private Tgpj_BankAccountSupervisedDao tgpj_BankAccountSupervisedDao;
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
	public Properties execute(Tgpj_BankAccountSupervisedForm model)
	{
		Properties properties = new MyProperties();
		
//		Integer theState = model.getTheState();
//		String busiState = model.getBusiState();
//		String eCode = model.geteCode();
		Long userStartId = model.getUserStartId();
//		Long createTimeStamp = model.getCreateTimeStamp();
		Long userUpdateId = model.getUserUpdateId();
		Long lastUpdateTimeStamp = System.currentTimeMillis();
		Long userRecordId = model.getUserRecordId();
//		Long recordTimeStamp = model.getRecordTimeStamp();
		Long developCompanyId = model.getDevelopCompanyId();
//		String eCodeOfDevelopCompany = model.geteCodeOfDevelopCompany();
//		Long projectId = model.getProjectId();
//		String theNameOfProject = model.getTheNameOfProject();
//		Long bankId = model.getBankId();
//		String theNameOfBank = model.getTheNameOfBank();
//		String shortNameOfBank = model.getShortNameOfBank();
		Long bankBranchId = model.getBankBranchId();
		String theName = model.getTheName();
		String theAccount = model.getTheAccount();
		Integer isUsing = model.getIsUsing();
//		String remark = model.getRemark();
//		String contactPerson = model.getContactPerson();
//		String contactPhone = model.getContactPhone();

		if(developCompanyId == null || developCompanyId < 1)
		{
			return MyBackInfo.fail(properties, "开发企业不能为空");
		}
		if(bankBranchId == null || bankBranchId < 1)
		{
			return MyBackInfo.fail(properties, "开户行不能为空");
		}
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "监管账号名称不能为空");
		}
		if(isUsing==null){
			return MyBackInfo.fail(properties, "请选择是否启用");
		}

		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
//		if(userStart == null)
//		{
//			return MyBackInfo.fail(properties, "'userStart(Id:" + userStartId + ")'不存在");
//		}
		Sm_User userUpdate = (Sm_User)sm_UserDao.findById(userUpdateId);
//		if(userUpdate == null)
//		{
//			return MyBackInfo.fail(properties, "'userUpdate(Id:" + userUpdateId + ")'不存在");
//		}
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
//		if(userRecord == null)
//		{
//			return MyBackInfo.fail(properties, "'userRecord(Id:" + userRecordId + ")'不存在");
//		}
		Emmp_CompanyInfo developCompany = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(developCompanyId);
		if(developCompany == null)
		{
			return MyBackInfo.fail(properties, "'developCompany(Id:" + developCompanyId + ")'不存在");
		}
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
			return MyBackInfo.fail(properties, "'bankBranch(Id:" + bankBranchId + ")'不存在");
		}
	
		Long tgpj_BankAccountSupervisedId = model.getTableId();
		Tgpj_BankAccountSupervised tgpj_BankAccountSupervised = (Tgpj_BankAccountSupervised)tgpj_BankAccountSupervisedDao.findById(tgpj_BankAccountSupervisedId);
		Tgpj_BankAccountSupervised tgpj_BankAccountSupervisedOld = ObjectCopier.copy(tgpj_BankAccountSupervised);
		if(tgpj_BankAccountSupervised == null)
		{
			return MyBackInfo.fail(properties, "'Tgpj_BankAccountSupervised(Id:" + tgpj_BankAccountSupervisedId + ")'不存在");
		}
		MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);
		if(!msgInfo.isSuccess()){
			return MyBackInfo.fail(properties, msgInfo.getInfo());
		}

//		Tgpj_BankAccountSupervisedForm accountQueryForm = new Tgpj_BankAccountSupervisedForm();
//		accountQueryForm.setTheState(S_TheState.Normal);
//		accountQueryForm.setTheAccount(theAccount);
//		Integer accountSize = tgpj_BankAccountSupervisedDao.findByPage_Size(tgpj_BankAccountSupervisedDao.getQuery_Size(tgpj_BankAccountSupervisedDao.getBasicHQL(), accountQueryForm));
//		if(accountSize>1){
//			return MyBackInfo.fail(properties, "该监管账户账户名已经存在，请更改监管账户名");
//		}
		
//		tgpj_BankAccountSupervised.setTheState(theState);
//		tgpj_BankAccountSupervised.setBusiState(busiState);
//		tgpj_BankAccountSupervised.seteCode(eCode);
		tgpj_BankAccountSupervised.setUserStart(userStart);
//		tgpj_BankAccountSupervised.setCreateTimeStamp(createTimeStamp);
		tgpj_BankAccountSupervised.setUserUpdate(userUpdate);
		tgpj_BankAccountSupervised.setUserUpdate(model.getUser());
		tgpj_BankAccountSupervised.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgpj_BankAccountSupervised.setUserRecord(userRecord);
//		tgpj_BankAccountSupervised.setRecordTimeStamp(recordTimeStamp);
		tgpj_BankAccountSupervised.setDevelopCompany(developCompany);
//		tgpj_BankAccountSupervised.seteCodeOfDevelopCompany(eCodeOfDevelopCompany);
//		tgpj_BankAccountSupervised.setBank(bank);
//		tgpj_BankAccountSupervised.setTheNameOfBank(theNameOfBank);
//		tgpj_BankAccountSupervised.setShortNameOfBank(shortNameOfBank);
		tgpj_BankAccountSupervised.setBankBranch(bankBranch);
		tgpj_BankAccountSupervised.setTheName(theName);
		tgpj_BankAccountSupervised.setTheAccount(theAccount);
//		tgpj_BankAccountSupervised.setRemark(remark);
//		tgpj_BankAccountSupervised.setContactPerson(contactPerson);
//		tgpj_BankAccountSupervised.setContactPhone(contactPhone);
		tgpj_BankAccountSupervised.setIsUsing(isUsing);
	
		tgpj_BankAccountSupervisedDao.save(tgpj_BankAccountSupervised);
		logAddService.addLog(model, tgpj_BankAccountSupervisedId, tgpj_BankAccountSupervisedOld, tgpj_BankAccountSupervised);

		sm_AttachmentBatchAddService.execute(model, tgpj_BankAccountSupervised.getTableId());


		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tableId", tgpj_BankAccountSupervised.getTableId());
		
		return properties;
	}
}
