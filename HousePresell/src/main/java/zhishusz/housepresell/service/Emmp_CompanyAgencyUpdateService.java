package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.controller.form.Emmp_OrgMemberForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.database.dao.Emmp_ComAccountDao;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Emmp_OrgMemberDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Sm_StreetInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Emmp_OrgMember;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_ButtonType;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;
import zhishusz.housepresell.util.ObjectCopier;
import zhishusz.housepresell.util.convert.OrgMemberConverter;
import zhishusz.housepresell.util.objectdiffer.model.Emmp_CompanyInfoTemplate;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;
import zhishusz.housepresell.util.project.CompanyLogUitl;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service更新操作：机构信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_CompanyAgencyUpdateService
{
	private static final String ADD_BUSI_CODE 	= "020103"; //代理公司新增
	private static final String UPDATE_BUSI_CODE= "020105"; //代理公司变更 

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
	private Emmp_OrgMemberDao emmp_OrgMemberDao;
	@Autowired
	private Emmp_OrgMemberAddService emmp_OrgMemberAddService;

	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService; //发起审批流程
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private Sm_PoCompareResult sm_PoCompareResult;

	//附件相关
	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;

	@Autowired
	private AttachmentJudgeExistUtil attachmentJudgeExistUtil;

	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private CompanyLogUitl companyLogUitl;
	@Autowired
	private Sm_BusiState_LogAddService logAddService;

	public Properties execute(Emmp_CompanyInfoForm model)
	{

		Properties properties = new MyProperties();

		String buttonType = model.getButtonType(); //1： 保存按钮  2：提交按钮
		Long loginUserId = model.getUserId();
		if (loginUserId == null || loginUserId < 1)
		{
			return MyBackInfo.fail(properties, "登录用户不能为空");
		}
		Long emmp_CompanyInfoId = model.getTableId();
		Emmp_CompanyInfo emmp_CompanyInfo = emmp_CompanyInfoDao.findById(emmp_CompanyInfoId);
		if(emmp_CompanyInfo == null)
		{
			return MyBackInfo.fail(properties, "代理公司不存在");
		}

		//-------------前台Form表单start----------//
		Long tableId = model.getTableId();
		String theType = model.getTheType();
		String theName = model.getTheName();
		String unifiedSocialCreditCode = model.getUnifiedSocialCreditCode();
		String registeredDateStr = model.getRegisteredDateStr();
		String legalPerson = model.getLegalPerson();
		String projectLeader = model.getProjectLeader();
		Long financialAccountId = model.getFinancialAccountId();
		Long cityRegionId = model.getCityRegionId();
		Long streetId = model.getStreetId();
		String address = model.getAddress();
		String isUsedState = model.getIsUsedState();

		//判断机构类型
		if (theType == null || theType.length() == 0)
		{
			return MyBackInfo.fail(properties, "机构类型不能为空");
		}

		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "请输入代理公司名称");
		}

		// 检查机构名称唯一性
		Emmp_CompanyInfoForm checkNameForm = new Emmp_CompanyInfoForm();
		checkNameForm.setExceptTableId(tableId);
		checkNameForm.setTheState(S_TheState.Normal);
		checkNameForm.setTheName(theName);
		Integer checkNameCount =
				emmp_CompanyInfoDao.findByPage_Size(emmp_CompanyInfoDao.getQuery_Size(emmp_CompanyInfoDao.checkUniquenessHQL(),
						checkNameForm));
		if (checkNameCount > 0)
		{
			return MyBackInfo.fail(properties, "代理公司名称已被占用");
		}

		if(address == null || address.length() == 0)
		{
			return MyBackInfo.fail(properties, "请输入代理公司地址");
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
		checkUnifiedSocialCreditCodeForm.setExceptTableId(tableId);
		checkUnifiedSocialCreditCodeForm.setTheState(S_TheState.Normal);
		checkUnifiedSocialCreditCodeForm.setUnifiedSocialCreditCode(unifiedSocialCreditCode);
		Integer checkUnifiedSocialCreditCodeCount =
				emmp_CompanyInfoDao.findByPage_Size(emmp_CompanyInfoDao.getQuery_Size(emmp_CompanyInfoDao.checkUniquenessHQL(),
						checkUnifiedSocialCreditCodeForm));
		if (checkUnifiedSocialCreditCodeCount > 0)
		{
			return MyBackInfo.fail(properties, "统一社会信用代码已存在");
		}

		if(emmp_CompanyInfo == null)
		{
			return MyBackInfo.fail(properties, "要修改的代理公司不存在");
		}

		MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);
		if(!msgInfo.isSuccess())
		{
			return MyBackInfo.fail(properties, msgInfo.getInfo());
		}

		Emmp_OrgMemberForm emmp_orgMemberForm = new Emmp_OrgMemberForm();
		emmp_orgMemberForm.setCompanyId(emmp_CompanyInfoId);
		emmp_orgMemberForm.setTheState(S_TheState.Normal);
		List<Emmp_OrgMember> orgMemberListOrg = emmp_OrgMemberDao.findByPage(emmp_OrgMemberDao.getQuery(emmp_OrgMemberDao.getBasicHQL(), emmp_orgMemberForm));
		Emmp_CompanyInfoTemplate companyOldTemplate = companyLogUitl.getCopyTemplate(emmp_CompanyInfo, orgMemberListOrg, S_CompanyType.Agency);

//		Emmp_OrgMemberForm[] orgMemberList = Arrays.stream(model.getOrgMemberList()).filter(emmp_orgMemberForm->
//				!"del".equals(emmp_orgMemberForm.getChangeState())
//		).toArray(Emmp_OrgMemberForm[]::new);
//
//		model.setOrgMemberList(orgMemberList);

		emmp_CompanyInfo.setUserUpdate(model.getUser());
		emmp_CompanyInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
		emmp_CompanyInfo.setIsUsedState(isUsedState);
		emmp_CompanyInfoDao.save(emmp_CompanyInfo);
		Session currentSession = emmp_CompanyInfoDao.getCurrentSession();
		currentSession.evict(emmp_CompanyInfo);
		//-------------前台Form表单end----------//

		//取得需要审批的字段变更前的对象
		Emmp_CompanyInfo emmp_CompanyInfoOld = ObjectCopier.copy(emmp_CompanyInfo);

		emmp_CompanyInfo.setTheName(theName);
		emmp_CompanyInfo.setAddress(address);
		emmp_CompanyInfo.setRegisteredDate(MyDatetime.getInstance().stringToLong(registeredDateStr));
		emmp_CompanyInfo.setLegalPerson(legalPerson);
		emmp_CompanyInfo.setProjectLeader(projectLeader);

		//取得需要审批的字段变更后的对象
		Emmp_CompanyInfo emmp_CompanyInfoNew = ObjectCopier.copy(emmp_CompanyInfo);

		//此时比较两者
		Boolean flag = sm_PoCompareResult.execute(emmp_CompanyInfoOld, emmp_CompanyInfoNew);

		if (flag) //如果基本字段没有变化再看机构成员列表
		{
			for (Emmp_OrgMemberForm orgMemberForm : model.getOrgMemberList())
			{
				//如果有form没有tableId，说明有新增
				if (orgMemberForm.getTableId() == null || orgMemberForm.getTableId() == 0)
				{
					flag = false;
					break;
				}
			}
			if (flag) //如果没有新增再看有没有删除
			{
				Integer totalCountNew = model.getOrgMemberList().length;

				Emmp_OrgMemberForm theModel = new Emmp_OrgMemberForm();
				theModel.setCompanyId(model.getTableId());
				theModel.setTheState(S_TheState.Normal);

				Integer totalCount = emmp_OrgMemberDao.findByPage_Size(emmp_OrgMemberDao.getQuery_Size(emmp_OrgMemberDao.getBasicHQL(), theModel));
				if (totalCountNew < totalCount)
				{
					flag = false;
				}
			}
		}

		if (flag) //如果机构成员列表没有变化再看附件列表
		{
			for (Sm_AttachmentForm formOSS : model.getGeneralAttachmentList())
			{
				//如果有form没有tableId，说明有新增
				if (formOSS.getTableId() == null || formOSS.getTableId() == 0)
				{
					flag = false;
					break;
				}
			}
			if (!flag) //如果没有新增再看有没有删除
			{
				Integer totalCountNew = model.getGeneralAttachmentList().length;

				Sm_AttachmentForm theForm = new Sm_AttachmentForm();
				theForm.setTheState(S_TheState.Normal);
				theForm.setBusiType(ADD_BUSI_CODE);
				theForm.setSourceId(MyString.getInstance().parse(model.getTableId()));

				Integer totalCount = sm_AttachmentDao.findByPage_Size(sm_AttachmentDao.getQuery_Size(sm_AttachmentDao.getBasicHQL(), theForm));

				if (totalCountNew < totalCount)
				{
					flag = false;
				}
			}
		}

		//先判断是否是未备案
		//如果是未备案则先保存到数据库然后根据是提交按钮还是保存按钮判断是否走新增的审批流
		if(S_BusiState.NoRecord.equals(emmp_CompanyInfo.getBusiState()))
		{
			emmp_CompanyInfoDao.update(emmp_CompanyInfo);

			saveOrgmemberList(model);
			sm_AttachmentBatchAddService.execute(model, emmp_CompanyInfoId);
			//如果是提交按钮则需要走新增的审批流
			if(S_ButtonType.Submit.equals(buttonType))
			{
				properties = sm_ApprovalProcessGetService.execute(ADD_BUSI_CODE, model.getUserId());
				if("fail".equals(properties.getProperty(S_NormalFlag.result)))
				{
					return properties;
				}

				//没有配置审批流程无需走审批流直接保存数据库
				if(!"noApproval".equals(properties.getProperty(S_NormalFlag.info)))
				{
					//有相应的审批流程配置才走审批流程
					Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");

					//审批操作
					sm_approvalProcessService.execute(emmp_CompanyInfo, model, sm_approvalProcess_cfg);
				}
				else
				{
					emmp_CompanyInfo.setBusiState(S_BusiState.HaveRecord);
					emmp_CompanyInfo.setApprovalState(S_ApprovalState.Completed);
					emmp_CompanyInfoDao.update(emmp_CompanyInfo);
				}
			}
		}
		else if(!flag)
		{
			properties = sm_ApprovalProcessGetService.execute(UPDATE_BUSI_CODE, model.getUserId());
			if("fail".equals(properties.getProperty(S_NormalFlag.result)))
			{
				return properties;
			}

			//没有配置审批流程无需走审批流直接保存数据库
			if("noApproval".equals(properties.getProperty(S_NormalFlag.info)))
			{
				emmp_CompanyInfo.setBusiState(S_BusiState.HaveRecord);
				emmp_CompanyInfo.setApprovalState(S_ApprovalState.Completed);
				emmp_CompanyInfoDao.update(emmp_CompanyInfo);

				saveOrgmemberList(model);
				sm_AttachmentBatchAddService.execute(model, emmp_CompanyInfoId);
				ArrayList<Emmp_OrgMember> orgMemberListNew = OrgMemberConverter.orgMemberFormList2OrgMemberList(model.getOrgMemberList());
				Emmp_CompanyInfoTemplate companyNewTemplate = companyLogUitl.getCopyTemplate(emmp_CompanyInfo, orgMemberListNew, S_CompanyType.Agency);
				logAddService.addLog(model, tableId, companyOldTemplate, companyNewTemplate);
				//日志，备案人，备案日期
				//TODO
			}
			else
			{
				//做一个还原操作
				try
				{
					PropertyUtils.copyProperties(emmp_CompanyInfo, emmp_CompanyInfoOld);
				}
				catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
				{
					e.printStackTrace();
				}

				//有相应的审批流程配置才走审批流程
				Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");

				emmp_CompanyInfo.setUserUpdate(model.getUser());
				emmp_CompanyInfo.setLastUpdateTimeStamp(System.currentTimeMillis());

				//上传到数据库的列表做一个新增或是删除的状态的保存
				Emmp_OrgMemberForm theModel = new Emmp_OrgMemberForm();
				theModel.setCompanyId(model.getTableId());
				theModel.setTheState(S_TheState.Normal);
				List<Emmp_OrgMember> emmp_OrgMemberList = emmp_OrgMemberDao.findByPage(emmp_OrgMemberDao.getQuery(emmp_OrgMemberDao.getBasicHQL(), theModel), null, null);

				model.setOrgMemberList(setOrgMemberState(model.getOrgMemberList(), emmp_OrgMemberList));

				//审批操作
				sm_approvalProcessService.execute(emmp_CompanyInfo, model, sm_approvalProcess_cfg);
			}
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}

	public void saveOrgmemberList(Emmp_CompanyInfoForm model)
	{
		Emmp_OrgMemberForm theModel = new Emmp_OrgMemberForm();
		theModel.setCompanyId(model.getTableId());
		theModel.setTheState(S_TheState.Normal);

		List<Emmp_OrgMember> emmp_OrgMemberList = emmp_OrgMemberDao.findByPage(emmp_OrgMemberDao.getQuery(emmp_OrgMemberDao.getBasicHQL(), theModel), null, null);
		for (Emmp_OrgMember emmp_OrgMember : emmp_OrgMemberList)
		{
			emmp_OrgMember.setTheState(S_TheState.Deleted);
			emmp_OrgMemberDao.save(emmp_OrgMember);
		}

		Emmp_OrgMemberForm[] orgMemberList = model.getOrgMemberList();
		if (orgMemberList != null && orgMemberList.length > 0)
		{
			for (Emmp_OrgMemberForm orgMemberForm : orgMemberList)
			{
				orgMemberForm.setCompanyId(model.getTableId());
				emmp_OrgMemberAddService.execute(orgMemberForm);
			}
		}
	}


	public Emmp_OrgMemberForm[] setOrgMemberState(Emmp_OrgMemberForm[] oldFormArray, List<Emmp_OrgMember> emmp_OrgMemberList)
	{
		List<Emmp_OrgMemberForm> newFormList = new ArrayList<>();

		List<Emmp_OrgMemberForm> oldList = new ArrayList<>();

		if (oldFormArray != null && oldFormArray.length > 0)
		{
			for (Emmp_OrgMemberForm obj:oldFormArray)
			{
				oldList.add(obj);
			}
		}

		for (Emmp_OrgMember orgMember1:emmp_OrgMemberList)
		{
			int theIndex = -1;
			for (int i = 0; i < oldList.size(); i ++)
			{
				Emmp_OrgMemberForm orgMember2 = oldList.get(i);
				if (orgMember1.getTableId().equals(orgMember2.getTableId()))
				{
					theIndex = i;
					break;
				}
			}

			if (theIndex == -1)  //如果是-1，说明该成员已经被删了
			{
				Emmp_OrgMemberForm theForm = new Emmp_OrgMemberForm();
				theForm.setTableId(orgMember1.getTableId());
				theForm.setTheName(orgMember1.getTheName());
				theForm.setIdType(orgMember1.getIdType());
				theForm.setIdNumber(orgMember1.getIdNumber());
				theForm.setTheNameOfDepartment(orgMember1.getTheNameOfDepartment());
				if (orgMember1.getParameter() != null)
				{
					theForm.setParameterId(orgMember1.getParameter().getTableId());
				}
				theForm.setPositionName(orgMember1.getPositionName());
				theForm.setPhoneNumber(orgMember1.getPhoneNumber());
				theForm.setQq(orgMember1.getQq());
				theForm.setWeixin(orgMember1.getWeixin());
				theForm.setChangeState("del");

				newFormList.add(theForm);
			}
			else  //如果不是-1，说明两边都存在无变更，记录Form里的数据 同时删除
			{
				newFormList.add(oldList.get(theIndex));

				oldList.remove(theIndex);

			}
		}

		for (Emmp_OrgMemberForm theForm:oldList)  //剩下的都是新增的
		{
			theForm.setChangeState("add");

			newFormList.add(theForm);
		}

		Emmp_OrgMemberForm[] array = new Emmp_OrgMemberForm[newFormList.size()];
		Emmp_OrgMemberForm[] newFormArray = newFormList.toArray(array);

		return  newFormArray;
	}
}
