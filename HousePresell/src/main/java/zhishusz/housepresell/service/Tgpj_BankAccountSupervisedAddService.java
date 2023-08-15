package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpj_BankAccountSupervisedForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpj_BankAccountSupervisedDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.transaction.Transactional;
	
/*
 * Service添加操作：监管账户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BankAccountSupervisedAddService
{
	private static final String BUSI_CODE = "200102";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"

	@Autowired
	private Tgpj_BankAccountSupervisedDao tgpj_BankAccountSupervisedDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
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

		Integer theState = S_TheState.Normal;
//		String busiState = model.getBusiState();
		String theAccount = model.getTheAccount();
//		Long userStartId = model.getUserStartId();
		Long createTimeStamp =System.currentTimeMillis();
//		Long userUpdateId = model.getUserUpdateId();
		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
//		Long userRecordId = model.getUserRecordId();
//		Long recordTimeStamp = model.getRecordTimeStamp();
		Long developCompanyId = model.getDevelopCompanyId();
//		String eCodeOfDevelopCompany = model.geteCodeOfDevelopCompany();
//		Long projectId = model.getProjectId();
//		String theNameOfProject = model.getTheNameOfProject();
//		Long bankId = model.getBankId();
		String theNameOfBank = model.getTheNameOfBank();
//		String shortNameOfBank = model.getShortNameOfBank();
		/*Long bankBranchId = model.getBankBranchId();*/
		String theName = model.getTheName();
//		String theAccount = model.getTheAccount();
//		String remark = model.getRemark();
//		String contactPerson = model.getContactPerson();
//		String contactPhone = model.getContactPhone();

		if(developCompanyId == null || developCompanyId < 1)
		{
			return MyBackInfo.fail(properties, "开发企业不能为空");
		}
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "监管账号名称不能为空");
		}
		if(theNameOfBank == null || theNameOfBank.length() < 1)
		{
			return MyBackInfo.fail(properties, "开户行不能为空");
		}

		Tgpj_BankAccountSupervisedForm accountQueryForm = new Tgpj_BankAccountSupervisedForm();
		accountQueryForm.setTheState(S_TheState.Normal);
		accountQueryForm.setTheAccount(theAccount);
		Integer accountSize = tgpj_BankAccountSupervisedDao.findByPage_Size(tgpj_BankAccountSupervisedDao.getQuery_Size(tgpj_BankAccountSupervisedDao.getBasicHQL(), accountQueryForm));
		if(accountSize>0){
			return MyBackInfo.fail(properties, "该监管账户的账号已经存在，请更改监管账号");
		}

//		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
//		Sm_User userUpdate = (Sm_User)sm_UserDao.findById(userUpdateId);
//		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		Emmp_CompanyInfo developCompany = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(developCompanyId);
//		Empj_ProjectInfo project = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(projectId);
//		Emmp_BankInfo bank = (Emmp_BankInfo)emmp_BankInfoDao.findById(bankId);
		/*Emmp_BankBranch bankBranch = (Emmp_BankBranch)emmp_BankBranchDao.findById(bankBranchId);
		Emmp_BankInfo bank = bankBranch.getBank();*/

		//		if(userStart == null)
//		{
//			return MyBackInfo.fail(properties, "创建人不能为空");
//		}
//		if(userRecord == null)
//		{
//			return MyBackInfo.fail(properties, "备案人不能为空");
//		}
		if(developCompany == null)
		{
			return MyBackInfo.fail(properties, "关联企业不能为空");
		}
//		if(project == null)
//		{
//			return MyBackInfo.fail(properties, "项目不能为空");
//		}
//		if(bank == null)
//		{
//			return MyBackInfo.fail(properties, "所属银行不能为空");
//		}
		/*if(bankBranch == null)
		{
			return MyBackInfo.fail(properties, "所属支行不能为空");
		}*/
		MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);
		if(!msgInfo.isSuccess()){
			return MyBackInfo.fail(properties, msgInfo.getInfo());
		}
	
		Tgpj_BankAccountSupervised tgpj_BankAccountSupervised = new Tgpj_BankAccountSupervised();
		tgpj_BankAccountSupervised.setTheState(theState);
//		tgpj_BankAccountSupervised.setBusiState(busiState);
		tgpj_BankAccountSupervised.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
//		tgpj_BankAccountSupervised.setUserStart(userStart);
		tgpj_BankAccountSupervised.setCreateTimeStamp(createTimeStamp);
		tgpj_BankAccountSupervised.setUserStart(model.getUser());
		tgpj_BankAccountSupervised.setUserUpdate(model.getUser());
//		tgpj_BankAccountSupervised.setUserUpdate(userUpdate);
		tgpj_BankAccountSupervised.setLastUpdateTimeStamp(lastUpdateTimeStamp);
//		tgpj_BankAccountSupervised.setUserRecord(userRecord);
//		tgpj_BankAccountSupervised.setRecordTimeStamp(recordTimeStamp);
		tgpj_BankAccountSupervised.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgpj_BankAccountSupervised.setDevelopCompany(developCompany);
//		tgpj_BankAccountSupervised.seteCodeOfDevelopCompany(eCodeOfDevelopCompany);
		/*tgpj_BankAccountSupervised.setBank(bank);*/
		tgpj_BankAccountSupervised.setTheNameOfBank(theNameOfBank);
//		tgpj_BankAccountSupervised.setShortNameOfBank(shortNameOfBank);
		/*tgpj_BankAccountSupervised.setBankBranch(bankBranch);*/
		tgpj_BankAccountSupervised.setTheName(theName);
		tgpj_BankAccountSupervised.setTheAccount(theAccount);
		tgpj_BankAccountSupervised.setIsUsing(0);
//		tgpj_BankAccountSupervised.setRemark(remark);
//		tgpj_BankAccountSupervised.setContactPerson(contactPerson);
//		tgpj_BankAccountSupervised.setContactPhone(contactPhone);
		tgpj_BankAccountSupervisedDao.save(tgpj_BankAccountSupervised);

		sm_AttachmentBatchAddService.execute(model, tgpj_BankAccountSupervised.getTableId());



		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tableId", tgpj_BankAccountSupervised.getTableId());

		return properties;
	}
}
