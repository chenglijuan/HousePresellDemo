package zhishusz.housepresell.service;

import java.util.Date;
import java.util.Properties;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
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
import zhishusz.housepresell.util.UserInfoCheck;

/*
 * Service添加操作：系统用户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_UserAddService
{
	private static final String BUSI_CODE = "010102";
	
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
	@Autowired
	private Sm_Permission_RoleDao sm_Permission_RoleDao;
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;
	@Autowired
	private AttachmentJudgeExistUtil attachmentJudgeExistUtil;
	
	public Properties execute(Sm_UserForm model)
	{
		Properties properties = new MyProperties();
		
		Sm_User userLogin = (Sm_User)sm_UserDao.findById(model.getUserId());
		if(userLogin == null)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NeedLogin);
		}
		
		String idType = model.getIdType();
		String loginPassword = model.getLoginPassword();
		String busiState = model.getBusiState();
		String theName = model.getTheName();
		String idNumber = model.getIdNumber();
		Integer isEncrypt = model.getIsEncrypt();
		String endTimeStampStr = model.getEndTimeStampStr();
		Long companyId = model.getCompanyId();
		String phoneNumber = model.getPhoneNumber();
		Long[] sm_Permission_RoleIdArr = model.getSm_Permission_RoleIdArr();
		
		String email = model.getEmail();
		String theAccount = model.getTheAccount();
		String ukeyNumber = model.getUkeyNumber();
		
		String isSignature = model.getIsSignature();
		
		if(StringUtils.isBlank(theName))
		{
			return MyBackInfo.fail(properties, "请填写真实姓名");
		}

		if(StringUtils.isBlank(theAccount)){
			return MyBackInfo.fail(properties, "请填写用户名称");
		}
		if(companyId == null || companyId < 1)
		{
			return MyBackInfo.fail(properties, "请选择企业");
		}
		if(isEncrypt == null)
		{
			return MyBackInfo.fail(properties, "请选择是否需要加密设备");
		}
		if(StringUtils.isBlank(phoneNumber)){
			return MyBackInfo.fail(properties, "请填写手机号码");
		}
		if(!UserInfoCheck.checkPhone(phoneNumber))
		{
			return MyBackInfo.fail(properties, "手机号码格式错误");
		}
		if( null != email && email.trim().length() > 0 && !MyString.getInstance().checkEmailNumber(email))
		{
			return MyBackInfo.fail(properties, "邮箱格式错误");
		}
		if(busiState == null || busiState.length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择是否启用");
		}
		if("1".equals(idType))
		{
			if(!MyString.getInstance().checkIdNumber(idNumber))
			{
				return MyBackInfo.fail(properties, "请输入正确的身份证号");
			}
		}
		//账户名
		theAccount = UserInfoCheck.smSqlInject(theAccount);
		//用户名校验
		theName = UserInfoCheck.smSqlInject(theName);
		//证件号码校验
		idNumber = UserInfoCheck.smSqlInject(idNumber);


		MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);
		if(!msgInfo.isSuccess())
		{
			return MyBackInfo.fail(properties, msgInfo.getInfo());
		}
		
		Emmp_CompanyInfo company = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(companyId);
		if(company == null)
		{
			return MyBackInfo.fail(properties, "选择的机构不存在");
		}

		Sm_UserForm sm_UserForm = new Sm_UserForm();
		sm_UserForm.setTheName(theName);
		sm_UserForm.setTheState(S_TheState.Normal);
		sm_UserForm.setBusiState("1");
		Sm_User sm_UserCheck = sm_UserDao.findOneByQuery_T(sm_UserDao.getQuery(sm_UserDao.getBasicHQL(), sm_UserForm));
		if(sm_UserCheck != null)
		{
			return MyBackInfo.fail(properties, "该名称的用户已存在并启用了");
		}
		Sm_User sm_User = new Sm_User();
		sm_User.setTheState(S_TheState.Normal);
		sm_User.setBusiState(busiState);
		sm_User.setStartTimeStamp(System.currentTimeMillis());
		sm_User.setEndTimeStamp(MyDatetime.getInstance().stringToLong(endTimeStampStr));
		sm_User.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		
		sm_User.setIsSignature(isSignature);
		sm_User.setUserStart(userLogin);
		sm_User.setErrPwdCount(10);
		sm_User.setCreateTimeStamp(System.currentTimeMillis());
		sm_User.setUserUpdate(userLogin);
		sm_User.setLastUpdateTimeStamp(System.currentTimeMillis());
		sm_User.seteCodeOfCompany(company.geteCode());
		sm_User.setTheNameOfCompany(company.getTheName());
		sm_User.setCompany(company);
		if(S_CompanyType.Zhengtai.equals(company.getTheType()))
		{
			sm_User.setTheType(S_UserType.ZhengtaiUser);
		}
		else
		{
			sm_User.setTheType(S_UserType.CommonUser);
		}
		
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

		sm_User.setLockUntil(System.currentTimeMillis());
		
		sm_User.setTheName(theName);
		sm_User.setIdType(idType);
		sm_User.setIdNumber(idNumber);
		sm_User.setPhoneNumber(phoneNumber);
		//初始密码默认为CZzttg00
		sm_User.setLoginPassword(AesUtil.getInstance().encrypt("CZzttg00"));
		sm_User.setEmail(email);
		sm_User.setTheAccount(theAccount);

		//设置密码过期时间为注册后的90天
		long time = new Date().getTime() + 24 * 3600 * 1000l * 90;
		sm_User.setPwdExpireTimeStamp(time);
//		
		sm_UserDao.save(sm_User);

		if(sm_Permission_RoleIdArr != null)
		{
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
		}
		
		sm_AttachmentBatchAddService.execute(model, sm_User.getTableId());
		
		properties.put("sm_User", sm_User);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}


}
