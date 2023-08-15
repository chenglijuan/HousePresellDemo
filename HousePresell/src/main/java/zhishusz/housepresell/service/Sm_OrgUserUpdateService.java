package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UserType;
import zhishusz.housepresell.util.AesUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;

/*
 * Service更新操作：系统用户+机构用户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_OrgUserUpdateService
{
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;
	@Autowired
	private Sm_Permission_RoleDao sm_Permission_RoleDao;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;// 附件配置
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;// 附件
	@Autowired
	private AttachmentJudgeExistUtil attachmentJudgeExistUtil;
	
	private static final String BUSI_CODE = "010102";
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_UserForm model)
	{
		Properties properties = new MyProperties();

		Sm_User userLogin = (Sm_User)sm_UserDao.findById(model.getUserId());
		if(userLogin == null)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NeedLogin);
		}
		
		Long tableId = model.getTableId();
		String idType = model.getIdType();
		String loginPassword = model.getLoginPassword();
		String busiState = model.getBusiState();
		String theName = model.getTheName();
		String idNumber = model.getIdNumber();
		Integer isEncrypt = model.getIsEncrypt();
		String effectiveDateStr = model.getEffectiveDateStr();
		Long companyId = model.getCompanyId();
		String phoneNumber = model.getPhoneNumber();
		Long[] sm_Permission_RoleIdArr = model.getSm_Permission_RoleIdArr();
		String isSignature = model.getIsSignature();
		
		String email = model.getEmail();
		String theAccount = model.getTheAccount();
		String ukeyNumber = model.getUkeyNumber();

		if(theAccount == null || theAccount.length() == 0)
		{
			return MyBackInfo.fail(properties, "请填写用户名称");
		}
		if(tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "请选择需要编辑的用户");
		}
		if(loginPassword == null || loginPassword.length() == 0)
		{
			return MyBackInfo.fail(properties, "请填写初始密码");
		}
		if(busiState == null || busiState.length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择是否启用");
		}
		if("1".equals(busiState))
		{
			if(effectiveDateStr == null || effectiveDateStr.length() == 0)
			{
				return MyBackInfo.fail(properties, "请选择生效时间");
			}
		}
		if("1".equals(idType))
		{
			if(!MyString.getInstance().checkIdNumber(idNumber))
			{
				return MyBackInfo.fail(properties, "请输入正确的身份证号");
			}
		}
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "请填写真实姓名");
		}
		if(isEncrypt == null)
		{
			return MyBackInfo.fail(properties, "请选择是否加密");
		}
		if(companyId == null || companyId < 1)
		{
			return MyBackInfo.fail(properties, "请选择企业");
		}
		if(phoneNumber == null || phoneNumber.length() == 0)
		{
			return MyBackInfo.fail(properties, "请填写手机号码");
		}
		if(!MyString.getInstance().checkPhoneNumber(phoneNumber))
		{
			return MyBackInfo.fail(properties, "手机号码格式错误");
		}
		if( null != email && email.trim().length() > 0 && !MyString.getInstance().checkEmailNumber(email))
		{
			return MyBackInfo.fail(properties, "邮箱格式错误");
		}
//		if(sm_Permission_RoleIdArr == null || sm_Permission_RoleIdArr.length == 0)
//		{
//			return MyBackInfo.fail(properties, "请选择用户角色");
//		}

		Sm_User sm_User = sm_UserDao.findById(tableId);
		Emmp_CompanyInfo company = emmp_CompanyInfoDao.findById(companyId);
		if(sm_User == null)
		{
			return MyBackInfo.fail(properties, "选择编辑的用户不存在");
		}
		if(company == null)
		{
			return MyBackInfo.fail(properties, "选择的机构不存在");
		}
		
		MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);
		if(!msgInfo.isSuccess())
		{
			return MyBackInfo.fail(properties, msgInfo.getInfo());
		}

		
		Sm_UserForm sm_UserForm = new Sm_UserForm();
		sm_UserForm.setTheAccount(theAccount);
		sm_UserForm.setTheState(S_TheState.Normal);
		sm_UserForm.setBusiState("1");
		Sm_User sm_UserCheck = sm_UserDao.findOneByQuery_T(sm_UserDao.getQuery(sm_UserDao.getBasicHQL(), sm_UserForm));
		if(sm_UserCheck != null && !sm_UserCheck.equals(sm_User))
		{
			return MyBackInfo.fail(properties, "该名称的用户已存在并启用了");
		}
		
		sm_User.setIsSignature(isSignature);
		sm_User.setIdType(idType);
		sm_User.setLoginPassword(AesUtil.getInstance().encrypt(loginPassword));
		sm_User.setBusiState(busiState);
		sm_User.setTheName(theName);
		sm_User.setIdNumber(idNumber);
		if(isEncrypt == 1 && (null == ukeyNumber || ukeyNumber.length() < 1))
		{
			return MyBackInfo.fail(properties, "请插入加密设备");
		}
		else if(isEncrypt == 0)
		{
			sm_User.setIsEncrypt(0);
			sm_User.setIsSignature("0");
			sm_User.setUkeyNumber(null);
		}
		else
		{
			// Ukey登陆方式
			sm_User.setIsEncrypt(1);
			sm_User.setUkeyNumber(ukeyNumber);
		}
		if(effectiveDateStr != null && effectiveDateStr.length() > 0)
		{
			String[] effectiveDateArr = effectiveDateStr.split(" - ");
			sm_User.setStartTimeStamp(MyDatetime.getInstance().stringToLong(effectiveDateArr[0]));
			sm_User.setEndTimeStamp(MyDatetime.getInstance().stringToLong(effectiveDateArr[1]));
		}
		sm_User.setCompany(company);
		sm_User.seteCodeOfCompany(company.geteCode());
		sm_User.setTheNameOfCompany(company.getTheName());
		if(S_CompanyType.Zhengtai.equals(company.getTheType()))
		{
			sm_User.setTheType(S_UserType.ZhengtaiUser);
		}
		else
		{
			sm_User.setTheType(S_UserType.CommonUser);
		}
		sm_User.setPhoneNumber(phoneNumber);
		sm_User.setUserUpdate(userLogin);
		sm_User.setLastUpdateTimeStamp(System.currentTimeMillis());
	
		sm_User.setEmail(email);
		sm_User.setTheAccount(theAccount);
		sm_UserDao.save(sm_User);
		
		Sm_Permission_RoleUserForm sm_Permission_RoleUserForm = new Sm_Permission_RoleUserForm();
		sm_Permission_RoleUserForm.setTheState(S_TheState.Normal);
		sm_Permission_RoleUserForm.setSm_UserId(tableId);
		List<Sm_Permission_RoleUser> sm_Permission_RoleUserList = sm_Permission_RoleUserDao.findByPage(sm_Permission_RoleUserDao.getQuery(sm_Permission_RoleUserDao.getBasicHQL(), sm_Permission_RoleUserForm));
		for(Sm_Permission_RoleUser sm_Permission_RoleUser : sm_Permission_RoleUserList)
		{
			sm_Permission_RoleUser.setTheState(S_TheState.Deleted);
			sm_Permission_RoleUserDao.save(sm_Permission_RoleUser);
		}
		
		for(int i=0;i<sm_Permission_RoleIdArr.length;i++)
		{
			Sm_Permission_Role sm_Permission_Role = sm_Permission_RoleDao.findById(sm_Permission_RoleIdArr[i]);
			if(sm_Permission_Role == null)
			{
				return MyBackInfo.fail(properties, "选择的角色不存在");
			}
			Sm_Permission_RoleUser sm_Permission_RoleUser = new Sm_Permission_RoleUser();
			sm_Permission_RoleUser.setEmmp_companyInfo(company);
			sm_Permission_RoleUser.setSm_Permission_Role(sm_Permission_Role);
			sm_Permission_RoleUser.setSm_User(sm_User);
			sm_Permission_RoleUser.setTheState(S_TheState.Normal);
			sm_Permission_RoleUserDao.save(sm_Permission_RoleUser);
		}
		
		sm_AttachmentBatchAddService.execute(model, sm_User.getTableId());
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
