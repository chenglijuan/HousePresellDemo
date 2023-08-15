package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.controller.form.Emmp_OrgMemberForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentBatchForm;
import zhishusz.housepresell.database.dao.Emmp_ComAccountDao;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Sm_StreetInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_ComAccount;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_StreetInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;

/*
 * Service添加操作：机构信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_CompanyInfoAddService
{
	private static final String ADD_BUSI_CODE 	= "020101"; //代理公司新增

	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_ComAccountDao emmp_ComAccountDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	@Autowired
	private Sm_StreetInfoDao sm_StreetInfoDao;
	@Autowired
	private Sm_ApprovalProcess_ApplyService sm_ApprovalProcess_applyService;

	//业务编码
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;

	//机构成员
	@Autowired
	private Emmp_OrgMemberAddService emmp_OrgMemberAddService;
	
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService; //发起审批流程
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	//附件相关
	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;

	@Autowired
	private AttachmentJudgeExistUtil attachmentJudgeExistUtil;

	public Properties execute(Emmp_CompanyInfoForm model)
	{
		Properties properties = new MyProperties();

		Long loginUserId = model.getUserId();
		if (loginUserId == null || loginUserId < 1)
		{
			return MyBackInfo.fail(properties, "登录用户不能为空");
		}

		// 发起人校验
		//1 如果该业务没有配置审批流程，直接保存
		//2 如果该业务配置了审批流程 ，判断用户能否与对应模块下的审批流程的发起人角色匹配
		properties = sm_ApprovalProcessGetService.execute(ADD_BUSI_CODE, loginUserId);
		if(! properties.getProperty("info").equals("noApproval") && properties.getProperty("result").equals("fail"))
		{
			return properties;
		}

		String theType = model.getTheType();  //机构类型
		String companyGroup = model.getCompanyGroup(); //归属集团
		String theName = model.getTheName();
		String shortName = model.getShortName();
		String eCodeFromPresellSystem = model.geteCodeFromPresellSystem();
		Long establishmentDate = model.getEstablishmentDate();
		String qualificationGrade = model.getQualificationGrade();
		String unifiedSocialCreditCode = model.getUnifiedSocialCreditCode();
		Double registeredFund = model.getRegisteredFund();
		String businessScope = model.getBusinessScope();
		String registeredDateStr = model.getRegisteredDateStr();
		Long expiredDate = model.getExpiredDate();
		String legalPerson = model.getLegalPerson();
		String contactPerson = model.getContactPerson();
		String contactPhone = model.getContactPhone();
		String projectLeader = model.getProjectLeader();
		Long financialAccountId = model.getFinancialAccountId();
		List qualificationInformationList = model.getQualificationInformationList();
		Long cityRegionId = model.getCityRegionId();
		Long streetId = model.getStreetId();
		String theURL = model.getTheURL();
		String address = model.getAddress();
		String email = model.getEmail();
		String theFax = model.getTheFax();
		String eCodeOfPost = model.geteCodeOfPost();
		String introduction = model.getIntroduction();
		String remark = model.getRemark();
		Long logId = model.getLogId();
		String isUsedState = model.getIsUsedState();

		Emmp_ComAccount financialAccount = (Emmp_ComAccount)emmp_ComAccountDao.findById(financialAccountId);

		//判断机构类型
		if(theType == null || theType.length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择机构类型");
		}
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "请输入开发企业名称");
		}

		// 检查开发企业名称唯一性
		Emmp_CompanyInfoForm checkNameForm = new Emmp_CompanyInfoForm();
		checkNameForm.setTheState(S_TheState.Normal);
		checkNameForm.setTheName(theName);
		Integer checkNameCount =
				emmp_CompanyInfoDao.findByPage_Size(emmp_CompanyInfoDao.getQuery_Size(emmp_CompanyInfoDao.checkUniquenessHQL(),
						checkNameForm));
		if (checkNameCount > 0)
		{
			return MyBackInfo.fail(properties, "开发企业名称已被占用");
		}

		if(unifiedSocialCreditCode == null || unifiedSocialCreditCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "请输入统一社会信用代码");
		}

		//判断统一社会信用代码格式
		/*if(!MyString.getInstance().isValid(unifiedSocialCreditCode))
		{
			return MyBackInfo.fail(properties, "统一社会信用代码格式不正确");
		}*/

		// 检查统一社会信用唯一性
		Emmp_CompanyInfoForm checkUnifiedSocialCreditCodeForm = new Emmp_CompanyInfoForm();
		checkUnifiedSocialCreditCodeForm.setTheState(S_TheState.Normal);
		checkUnifiedSocialCreditCodeForm.setUnifiedSocialCreditCode(unifiedSocialCreditCode);
		Integer checkUnifiedSocialCreditCodeCount =
				emmp_CompanyInfoDao.findByPage_Size(emmp_CompanyInfoDao.getQuery_Size(emmp_CompanyInfoDao.checkUniquenessHQL(),
						checkUnifiedSocialCreditCodeForm));
		if (checkUnifiedSocialCreditCodeCount > 0)
		{
			return MyBackInfo.fail(properties, "统一社会信用代码格式已存在");
		}

		if(registeredDateStr == null || registeredDateStr.length() == 0)
		{
			return MyBackInfo.fail(properties, "'请选择企业成立日期");
		}
		if(address == null || address.length() == 0)
		{
			return MyBackInfo.fail(properties, "请输入企业经营地址");
		}
		if (cityRegionId == null || cityRegionId == 0)
		{
			return MyBackInfo.fail(properties, "请选择区域");
		}
		//所属区域
		Sm_CityRegionInfo cityRegion = (Sm_CityRegionInfo)sm_CityRegionInfoDao.findById(cityRegionId);
		if(cityRegion == null)
		{
			return MyBackInfo.fail(properties, "区域不存在");
		}

//		if (streetId == null || streetId > 0)
//		{
//			return MyBackInfo.fail(properties, "请选择街道");
//		}
		//街道
		Sm_StreetInfo street = (Sm_StreetInfo)sm_StreetInfoDao.findById(streetId);
//		if(street == null)
//		{
//			return MyBackInfo.fail(properties, "街道不存在");
//		}

		MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);
		if(!msgInfo.isSuccess())
		{
			return MyBackInfo.fail(properties, msgInfo.getInfo());
		}
		
		

		Emmp_CompanyInfo emmp_CompanyInfo = new Emmp_CompanyInfo();
		emmp_CompanyInfo.setTheState(S_TheState.Normal);
		emmp_CompanyInfo.seteCode(sm_BusinessCodeGetService.execute(ADD_BUSI_CODE));
		emmp_CompanyInfo.setUserStart(model.getUser());
		emmp_CompanyInfo.setCreateTimeStamp(System.currentTimeMillis());
		emmp_CompanyInfo.setUserUpdate(model.getUser());
		emmp_CompanyInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
		emmp_CompanyInfo.setTheType(theType);
		emmp_CompanyInfo.setCompanyGroup(companyGroup);
		emmp_CompanyInfo.setTheName(theName);
		emmp_CompanyInfo.setShortName(shortName);
		emmp_CompanyInfo.seteCodeFromPresellSystem(eCodeFromPresellSystem);
		emmp_CompanyInfo.setEstablishmentDate(establishmentDate);
		emmp_CompanyInfo.setQualificationGrade(qualificationGrade);
		emmp_CompanyInfo.setUnifiedSocialCreditCode(unifiedSocialCreditCode);
		emmp_CompanyInfo.setRegisteredFund(registeredFund);
		emmp_CompanyInfo.setBusinessScope(businessScope);
		//		emmp_CompanyInfo.setRegisteredDate(registeredDate);
		emmp_CompanyInfo.setRegisteredDate(MyDatetime.getInstance().parse(registeredDateStr).getTime());
		emmp_CompanyInfo.setExpiredDate(expiredDate);
		emmp_CompanyInfo.setLegalPerson(legalPerson);
		emmp_CompanyInfo.setContactPerson(contactPerson);
		emmp_CompanyInfo.setContactPhone(contactPhone);
		emmp_CompanyInfo.setProjectLeader(projectLeader);
		emmp_CompanyInfo.setFinancialAccount(financialAccount);
		emmp_CompanyInfo.setQualificationInformationList(qualificationInformationList);
		emmp_CompanyInfo.setCityRegion(cityRegion);
		emmp_CompanyInfo.setStreet(street);
		emmp_CompanyInfo.setTheURL(theURL);
		emmp_CompanyInfo.setAddress(address);
		emmp_CompanyInfo.setEmail(email);
		emmp_CompanyInfo.setTheFax(theFax);
		emmp_CompanyInfo.seteCodeOfPost(eCodeOfPost);
		emmp_CompanyInfo.setIntroduction(introduction);
		emmp_CompanyInfo.setRemark(remark);
		emmp_CompanyInfo.setLogId(logId);

		emmp_CompanyInfo.setIsUsedState(isUsedState);

		if(properties.getProperty("info").equals("noApproval"))
		{
			emmp_CompanyInfo.setBusiState(S_BusiState.HaveRecord);
			emmp_CompanyInfo.setApprovalState(S_ApprovalState.Completed);
			emmp_CompanyInfoDao.save(emmp_CompanyInfo);
		}
		else
		{
			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");

			//业务状态
			emmp_CompanyInfo.setBusiState(S_BusiState.NoRecord);
			emmp_CompanyInfoDao.save(emmp_CompanyInfo);

			sm_approvalProcessService.execute(emmp_CompanyInfo, model, sm_approvalProcess_cfg);
		}
		
		//机构成员
		Emmp_OrgMemberForm[] orgMemberList = model.getOrgMemberList();
		if (orgMemberList != null && orgMemberList.length > 0)
		{
			for (Emmp_OrgMemberForm orgMemberForm : orgMemberList)
			{
				orgMemberForm.setCompanyId(emmp_CompanyInfo.getTableId());
				emmp_OrgMemberAddService.execute(orgMemberForm);
			}
		}
		
		//附件材料
		sm_AttachmentBatchAddService.execute(model, emmp_CompanyInfo.getTableId());

		properties.put("emmp_CompanyInfo", emmp_CompanyInfo);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

}
