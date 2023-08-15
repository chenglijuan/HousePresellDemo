package zhishusz.housepresell.util.rebuild;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：系统用户+机构用户
 * Company：ZhiShuSZ
 */
@Service
public class Sm_UserRebuild extends RebuilderBase<Sm_User>
{
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;

	private MyDatetime myDatetime = MyDatetime.getInstance();

	@Override
	public Properties getSimpleInfo(Sm_User object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("eCode", object.geteCode()); // 用户编码
		properties.put("theAccount", object.getTheAccount());// 账户名
		properties.put("theName", object.getTheName()); // 真实姓名
		if (object.getCompany() != null)
		{
			properties.put("theNameOfCompany", object.getCompany().getTheName());// 所属机构
		}
		properties.put("idType", object.getIdType());// 证件类型
		properties.put("idNumber", object.getIdNumber());// 证件号码
		properties.put("busiState", object.getBusiState());// 是否启用
		properties.put("isEncrypt", object.getIsEncrypt());// 是否加密
		if (null == object.getIsSignature())
		{
			properties.put("isSignature", "0");
		}
		else
		{
			properties.put("isSignature", object.getIsSignature());
		}

		// 会导致角色详情出现卡顿
		// Sm_Permission_RoleUserForm sm_Permission_RoleUserForm = new
		// Sm_Permission_RoleUserForm();
		// sm_Permission_RoleUserForm.setUserId(object.getTableId());
		// sm_Permission_RoleUserForm.setTheState(S_TheState.Normal);
		// //用户角色关系列表
		// List<Sm_Permission_RoleUser> sm_Permission_RoleUserList =
		// sm_Permission_RoleUserDao.findByPage(sm_Permission_RoleUserDao.getQuery(sm_Permission_RoleUserDao.getBasicHQL(),
		// sm_Permission_RoleUserForm));
		// Set<Sm_Permission_Role> sm_Permission_RoleSet = new
		// HashSet<Sm_Permission_Role>();
		// for(Sm_Permission_RoleUser sm_Permission_RoleUser :
		// sm_Permission_RoleUserList)
		// {
		// if(sm_Permission_RoleUser.getSm_Permission_Role() == null) continue;
		// sm_Permission_RoleSet.add(sm_Permission_RoleUser.getSm_Permission_Role());
		// }
		// properties.put("sm_Permission_RoleSet", sm_Permission_RoleSet);

		if (object.getLockUntil() != null)
		{
			if (object.getLockUntil() > System.currentTimeMillis())
			{
				properties.put("applyState", 1);
			}
			else
			{
				properties.put("applyState", 2);
			}
		}

		return properties;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Properties getDetail(Sm_User object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("theType", object.getTheType());
		properties.put("tableId", object.getTableId());
		// properties.put("eCode", object.geteCode());
		properties.put("theName", object.getTheName());
		properties.put("theAccount", object.getTheAccount());// 账户名
		if (object.getCompany() != null)
		{
			properties.put("theNameOfCompany", object.getCompany().getTheName());
			properties.put("developCompanyId", object.getCompany().getTableId());
			properties.put("developCompanyType", object.getCompany().getTheType());

			Sm_BaseParameterForm sm_BaseParameterForm = new Sm_BaseParameterForm();
			sm_BaseParameterForm.setTheState(S_TheState.Normal);
			sm_BaseParameterForm.setTheValue(object.getCompany().getTheType());
			sm_BaseParameterForm.setParametertype("8");

			Integer totalCount = sm_BaseParameterDao.findByPage_Size(
					sm_BaseParameterDao.getQuery_Size(sm_BaseParameterDao.getBasicHQL(), sm_BaseParameterForm));

			List<Sm_BaseParameter> sm_BaseParameterList;
			if (totalCount > 0)
			{
				sm_BaseParameterList = sm_BaseParameterDao.findByPage(
						sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), sm_BaseParameterForm));
				String retParam = sm_BaseParameterList.get(0).getTheName();
				properties.put("companyType", retParam);
			}

			// 机构类型
			properties.put("companyPermission", object.getCompany().getTheType());
			
			String pageHome = "home.shtml";
			switch (object.getCompany().getTheType())
			{
			case "0":
				pageHome = "home.shtml";
				break;

			case "1":
				pageHome = "homeDeveloper.shtml";
				break;

			case "11":
				pageHome = "home.shtml";
				break;

			case "12":
				pageHome = "home.shtml";
				break;

			case "21":
				pageHome = "homeCooperativeAgency.shtml";
				break;

			default:
				pageHome = "home.shtml";
				break;
			}
			properties.put("pageHome", pageHome);

		}

		properties.put("idType", object.getIdType());
		properties.put("idNumber", object.getIdNumber());
		properties.put("phoneNumber", object.getPhoneNumber());
//		properties.put("pwd", AesUtil.getInstance().decrypt(object.getLoginPassword()));
		properties.put("pwd", "");
		//properties.put("pwd","123");
		properties.put("busiState", object.getBusiState());
		properties.put("email", object.getEmail());// 邮箱
		properties.put("ukeyNumber", object.getUkeyNumber());// 邮箱
		// TODO 是否加密 加密设备序列号 生效时间 是否锁定
		if (object.getUserStart() != null)
		{
			properties.put("userStart", object.getUserStart().getTheName());// 创建人
		}
		properties.put("createTimeStamp", myDatetime.dateToString2(object.getCreateTimeStamp()));

//		//如果密码为初始密码
		if("ece620f19cce210755a7b7d0991f7fb9".equals(object.getLoginPassword())){
			properties.put("pwdExpireTimeStamp","");
		}else{
			//不等于空  如果当前时间小于过期时间 表示没有过期  如果大于表示过期了
			if(object.getPwdExpireTimeStamp() != null){
				// 没有过期
				if(object.getPwdExpireTimeStamp() > new Date().getTime()){
					properties.put("pwdExpireTimeStamp",myDatetime.dateToString2(object.getPwdExpireTimeStamp()));
				}else{
					//过期
					properties.put("pwdExpireTimeStamp","");
				}
			}else{
				//密码为初始密码：
				//等于空  过期
				properties.put("pwdExpireTimeStamp","");
			}
		}

		if (object.getLockUntil() != null)
		{
			if (object.getLockUntil() > System.currentTimeMillis())
			{
				properties.put("applyState", 1);
			}
			else
			{
				properties.put("applyState", 2);
			}
		}

		properties.put("isEncrypt", object.getIsEncrypt());

		if (null == object.getIsSignature())
		{
			properties.put("isSignature", "0");
		}
		else
		{
			properties.put("isSignature", object.getIsSignature());
		}

		// 角色列表
		Sm_Permission_RoleUserForm sm_Permission_RoleUserForm = new Sm_Permission_RoleUserForm();
		sm_Permission_RoleUserForm.setTheState(S_TheState.Normal);
		sm_Permission_RoleUserForm.setSm_UserId(object.getTableId());
		List<Sm_Permission_RoleUser> sm_Permission_RoleUserList = sm_Permission_RoleUserDao
				.findByPage(sm_Permission_RoleUserDao.getQuery(sm_Permission_RoleUserDao.getBasicHQL(),
						sm_Permission_RoleUserForm));
		Set<Sm_Permission_Role> sm_Permission_RoleSet = new HashSet<Sm_Permission_Role>();
		List<Long> sm_Permission_RoleIdList = new ArrayList<Long>();
		for (Sm_Permission_RoleUser sm_Permission_RoleUser : sm_Permission_RoleUserList)
		{
			if (sm_Permission_RoleUser.getSm_Permission_Role() == null)
				continue;
			sm_Permission_RoleSet.add(sm_Permission_RoleUser.getSm_Permission_Role());
			sm_Permission_RoleIdList.add(sm_Permission_RoleUser.getSm_Permission_Role().getTableId());
		}
		properties.put("sm_Permission_RoleSet", sm_Permission_RoleSet);
		properties.put("sm_Permission_RoleIdList", sm_Permission_RoleIdList);
		if (object.getStartTimeStamp() != null && object.getEndTimeStamp() != null)
		{
			properties.put("effectiveDateStr", myDatetime.dateToSimpleString(object.getStartTimeStamp()) + " - "
					+ myDatetime.dateToSimpleString(object.getEndTimeStamp()));
		}

		// 获取机构类型
		Emmp_CompanyInfo companyInfo = object.getCompany();
		if (null != companyInfo)
		{

			String type = "";
			if (null != companyInfo.getTheType())
			{
				switch (companyInfo.getTheType())
				{
				case S_CompanyType.Development:
					type = "开发企业";
					break;
				case S_CompanyType.Agency:
					type = "代理公司";
					break;
				case S_CompanyType.Witness:
					type = "进度见证服务单位";
					break;
				case S_CompanyType.Cooperation:
					type = "合作机构";
					
				case S_CompanyType.Supervisor:
                    type = "监理机构";
					break;
				}

				properties.put(S_NormalFlag.companyType, type);
			}
		}

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_User> sm_UserList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (sm_UserList != null)
		{
			for (Sm_User object : sm_UserList)
			{
				Properties properties = new MyProperties();

				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", myDatetime.dateToString2(object.getCreateTimeStamp()));
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				properties.put("userRecord", object.getUserRecord());
				properties.put("userRecordId", object.getUserRecord().getTableId());
				properties.put("recordTimeStamp", myDatetime.dateToString2(object.getRecordTimeStamp()));
				properties.put("lockUntil", object.getLockUntil());
				properties.put("company", object.getCompany());
				properties.put("companyId", object.getCompany().getTableId());
				properties.put("eCodeOfCompany", object.geteCodeOfCompany());
				properties.put("theNameOfCompany", object.getTheNameOfCompany());
				properties.put("department", object.getDepartment());
				properties.put("departmentId", object.getDepartment().getTableId());
				properties.put("eCodeOfDepartment", object.geteCodeOfDepartment());
				properties.put("theNameOfDepartment", object.getTheNameOfDepartment());
				properties.put("theType", object.getTheType());
				properties.put("theName", object.getTheName());
				properties.put("realName", object.getRealName());
				properties.put("idType", object.getIdType());
				properties.put("idNumber", object.getIdNumber());
				properties.put("phoneNumber", object.getPhoneNumber());
				properties.put("email", object.getEmail());
				properties.put("weixin", object.getWeixin());
				properties.put("qq", object.getQq());
				properties.put("address", object.getAddress());
				properties.put("loginPassword", object.getLoginPassword());
				properties.put("errPwdCount", object.getErrPwdCount());
				properties.put("heardImagePath", object.getHeardImagePath());
				properties.put("lastLoginTimeStamp", object.getLastLoginTimeStamp());
				properties.put("loginMode", object.getLoginMode());
				properties.put("ukeyNumber", object.getUkeyNumber());
				properties.put("hasQC", object.getHasQC());
				properties.put("qualificationInfo", object.getQualificationInfo());
				properties.put("qualificationInfoId", object.getQualificationInfo().getTableId());
				properties.put("menuPermissionHtmlPath", object.getMenuPermissionHtmlPath());
				properties.put("buttonPermissionJsonPath", object.getButtonPermissionJsonPath());
				properties.put("dataPermssion", object.getDataPermssion());

				list.add(properties);
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List userGetBuildingList(List<Empj_BuildingInfo> buildingList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (buildingList != null)
		{
			for (Empj_BuildingInfo object : buildingList)
			{

				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());
				if (object.getDevelopCompany() != null)
				{
					properties.put("developCompanyName", object.getDevelopCompany().getTheName());
					properties.put("developCompanyId", object.getDevelopCompany().getTableId());
					properties.put("developCompanyEcode", object.getDevelopCompany().geteCode());
				}
				properties.put("eCode", object.geteCode());
				properties.put("theNameFromFinancialAccounting", object.getTheNameFromFinancialAccounting());
				if (object.getExtendInfo() != null)
				{
					properties.put("isSupportPGS", object.getExtendInfo().getIsSupportPGS());
					properties.put("landMortgagor", object.getExtendInfo().getLandMortgagor());
					properties.put("landMortgageAmount", object.getExtendInfo().getLandMortgageAmount());
					properties.put("landMortgageState", object.getExtendInfo().getLandMortgageState());
				}
				if (object.getUserStart() != null)
				{
					properties.put("sm_UserStartName", object.getUserStart().getRealName());
					properties.put("userStartId", object.getUserStart().getTableId());
				}
				if (object.getUserRecord() != null)
				{
					properties.put("sm_UserRecordName", object.getUserRecord().getRealName());
					properties.put("userRecordId", object.getUserRecord().getTableId());
				}
				if (object.getProject() != null)
				{
					properties.put("projectName", object.getProject().getTheName());
					properties.put("projectId", object.getProject().getTableId());
					properties.put("projectEcode", object.getProject().geteCode());
				}
				properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
				properties.put("empj_ProjectInfoName", object.getTheNameOfProject());
				properties.put("eCodeOfProjectFromPresellSystem", object.getTheNameFromPresellSystem());
				properties.put("busiState", object.getBusiState());
				if (object.getCreateTimeStamp() != null)
				{
					properties.put("createTimeStamp", myDatetime.dateToString2(object.getCreateTimeStamp()));
				}
				if (object.getRecordTimeStamp() != null)
				{
					properties.put("recordTimeStamp", myDatetime.dateToString2(object.getRecordTimeStamp()));
				}
				properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());
				properties.put("theNameFromPresellSystem", object.geteCodeOfProjectFromPresellSystem());
				properties.put("eCodeFromPresellSystem", object.geteCodeFromPresellSystem());
				properties.put("eCodeFromPresellCert", object.geteCodeFromPresellCert());
				properties.put("buildingArea", object.getBuildingArea());
				properties.put("upfloorNumber", object.getUpfloorNumber());
				properties.put("escrowArea", object.getEscrowArea());
				properties.put("escrowStandard", object.getEscrowStandard());
				properties.put("deliveryType", object.getDeliveryType());
				properties.put("downfloorNumber", object.getDownfloorNumber());
				properties.put("theType", object.getTheType());
				properties.put("remark", object.getRemark());
				properties.put("theName", object.geteCodeFromConstruction());

				list.add(properties);
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List getList(List<Sm_User> sm_UserList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (sm_UserList != null)
		{
			for (Sm_User object : sm_UserList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());
				properties.put("theName", object.getTheName());

				list.add(properties);
			}
		}
		return list;
	}
	
	//用户登录成功后返回
	public Properties getDetailForLogin(Sm_User object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("theType", object.getTheType());
		properties.put("tableId", object.getTableId());
		properties.put("theName", object.getTheName());
		properties.put("theAccount", object.getTheAccount());// 账户名

		properties.put("isEncrypt", object.getIsEncrypt());

		if (null == object.getIsSignature())
		{
			properties.put("isSignature", "0");
		}
		else
		{
			properties.put("isSignature", object.getIsSignature());
		}

		return properties;
	}

}
