package zhishusz.housepresell.service;import zhishusz.housepresell.controller.form.Emmp_BankBranchForm;import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;import zhishusz.housepresell.database.dao.Sm_UserDao;import zhishusz.housepresell.database.po.Emmp_BankBranch;import zhishusz.housepresell.database.po.Emmp_BankInfo;import zhishusz.housepresell.database.po.extra.MsgInfo;import zhishusz.housepresell.database.po.state.S_NormalFlag;import zhishusz.housepresell.database.po.state.S_TheState;import zhishusz.housepresell.util.MyBackInfo;import zhishusz.housepresell.util.MyProperties;import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;import java.util.Properties;import javax.transaction.Transactional;import cn.hutool.core.lang.Validator;/* * Service添加操作：银行网点(开户行) * Company：ZhiShuSZ * */@Service@Transactionalpublic class Emmp_BankBranchAddService{	private static final String BUSI_CODE = "020202";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"		@Autowired	private Emmp_BankBranchDao emmp_BankBranchDao;	@Autowired	private Sm_UserDao sm_UserDao;	@Autowired	private Emmp_BankInfoDao emmp_bankInfoDao;	@Autowired	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;	//附件相关	@Autowired	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;	@Autowired	private AttachmentJudgeExistUtil attachmentJudgeExistUtil;		public Properties execute(Emmp_BankBranchForm model)	{		Properties properties = new MyProperties();		Integer theState = S_TheState.Normal;		String busiState = model.getBusiState();		//		String eCode = model.geteCode();//		Long userStartId = model.getUserStartId();		//		Long createTimeStamp = System.currentTimeMillis();		//		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();//		Long userRecordId = model.getUserRecordId();		//		Long recordTimeStamp = model.getRecordTimeStamp();		String theName = model.getTheName();		String shortName = model.getShortName();		String address = model.getAddress();		String contactPerson = model.getContactPerson();		String contactPhone = model.getContactPhone();		String leader = model.getLeader();		Long bankId = model.getBankId();		String subjCode = model.getSubjCode();				String desubjCode = model.getDesubjCode();		String bblcsubjCode = model.getBblcsubjCode();		String jgcksubjCode = model.getJgcksubjCode();				Integer isDocking = model.getIsDocking();//是否对接资金系统		if(null == isDocking){			return MyBackInfo.fail(properties, "请选择是否对接资金系统！");		}		String interbankCode = model.getInterbankCode();//联行号		if(1 == isDocking){			if(null == interbankCode || interbankCode.trim().isEmpty()){				return MyBackInfo.fail(properties, "请输入联行号！");			}		}						//		if(theState == null || theState < 1)		//		{		//			return MyBackInfo.fail(properties, "状态 S_TheState 初始为Normal不能为空");		//		}		//		if(busiState == null || busiState.length() < 1)		//		{		//			return MyBackInfo.fail(properties, "业务状态不能为空");		//		}		//		if(eCode == null || eCode.length() == 0)		//		{		//			return MyBackInfo.fail(properties, "编号不能为空");		//		}		//		if(userStartId == null || userStartId < 1)		//		{//			return MyBackInfo.fail(properties, "'userStartId'不能为空");		//		if(createTimeStamp == null || createTimeStamp < 1)		//		{		//			return MyBackInfo.fail(properties, "创建时间不能为空");		//		}		//		if(lastUpdateTimeStamp == null || lastUpdateTimeStamp < 1)		//		{		//			return MyBackInfo.fail(properties, "最后修改日期不能为空");		//		}		//		if(userRecordId == null || userRecordId < 1)		//		{		//			return MyBackInfo.fail(properties, "备案人不能为空");		//		}		//		if(recordTimeStamp == null || recordTimeStamp < 1)		//		{		//			return MyBackInfo.fail(properties, "备案日期不能为空");		//		}		if (theName == null || theName.length() == 0)		{			return MyBackInfo.fail(properties, "开户行名称不能为空");		}		if (shortName == null || shortName.length() == 0)		{			return MyBackInfo.fail(properties, "开户行简称不能为空");		}		if (address == null || address.length() == 0)		{			return MyBackInfo.fail(properties, "开户行地址不能为空");		}		//		if(contactPerson == null || contactPerson.length() == 0)		//		{		//			return MyBackInfo.fail(properties, "联系人不能为空");		//		}		//		if(contactPhone == null || contactPhone.length() == 0)		//		{		//			return MyBackInfo.fail(properties, "联系电话不能为空");		//		}		//		if(leader == null || leader.length() == 0)		//		{		//			return MyBackInfo.fail(properties, "负责人不能为空");		//		}		//		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);		//		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);		Emmp_BankInfo bank = emmp_bankInfoDao.findById(bankId);		//		if(userStart == null)		//		{		//			return MyBackInfo.fail(properties, "创建人不能为空");		//		}		//		if(userRecord == null)		//		{		//			return MyBackInfo.fail(properties, "备案人不能为空");		//		}		if(bank==null){//			return MyBackInfo.fail(properties, "'bankId'不能为空");			return MyBackInfo.fail(properties, "该银行无法找到");		}		MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);		if(!msgInfo.isSuccess()){			return MyBackInfo.fail(properties, msgInfo.getInfo());		}		//逻辑开始		if (contactPhone != null && contactPhone.length() > 0)		{			if (!Validator.isMobile(contactPhone))			{				return MyBackInfo.fail(properties, S_NormalFlag.info_PhoneNumberFail);			}		}		long createTimeStamp = System.currentTimeMillis();		Emmp_BankBranch emmp_BankBranch = new Emmp_BankBranch();		emmp_BankBranch.setTheState(theState);		emmp_BankBranch.setBusiState(busiState);		emmp_BankBranch.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));		emmp_BankBranch.setUserStart(model.getUser());		emmp_BankBranch.setUserUpdate(model.getUser());		emmp_BankBranch.setCreateTimeStamp(createTimeStamp);		emmp_BankBranch.setLastUpdateTimeStamp(System.currentTimeMillis());		emmp_BankBranch.setTheName(theName);		emmp_BankBranch.setShortName(shortName);		emmp_BankBranch.setAddress(address);		emmp_BankBranch.setContactPerson(contactPerson);		emmp_BankBranch.setContactPhone(contactPhone);		emmp_BankBranch.setLeader(leader);		emmp_BankBranch.setBank(bank);		emmp_BankBranch.setIsUsing(0);		emmp_BankBranch.setSubjCode(subjCode);				emmp_BankBranch.setDesubjCode(desubjCode);		emmp_BankBranch.setBblcsubjCode(bblcsubjCode);		emmp_BankBranch.setJgcksubjCode(jgcksubjCode);				emmp_BankBranch.setIsDocking(isDocking);		emmp_BankBranch.setInterbankCode(interbankCode);				emmp_BankBranchDao.save(emmp_BankBranch);		sm_AttachmentBatchAddService.execute(model, emmp_BankBranch.getTableId());				properties.put(S_NormalFlag.result, S_NormalFlag.success);		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);		properties.put("tableId", emmp_BankBranch.getTableId());		return properties;	}}