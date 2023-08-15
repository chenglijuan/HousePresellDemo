package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

@Service
public class Sm_UserGetForLoginService {
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;
	
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Sm_UserForm model) {
		Properties properties = new MyProperties();

		Sm_User user = model.getUser();
		
		if(null == user)
		{
			return MyBackInfo.fail(properties, "登录信息已失效，请重新登录！");
		}

		String pageHome = "home.shtml";
		String pageHomeStr = "home";
		
		String codeOfCompany = user.geteCodeOfCompany();
		
		Emmp_CompanyInfoForm companyInfoForm = new Emmp_CompanyInfoForm();
		companyInfoForm.seteCode(codeOfCompany);
		List<Emmp_CompanyInfo> listCompany;
		listCompany = emmp_CompanyInfoDao.findByPage(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBasicHQL(), companyInfoForm));
		if(null == listCompany || listCompany.size() == 0)
		{
			pageHome = "home.shtml";
		}else{
			String theType = listCompany.get(0).getTheType();
			
			switch (theType) {
			case "0":
				pageHome = "home.shtml";
				break;

			case "1":
				/* 判断登陆用户是否开发企业销售管理人员 */
				Sm_Permission_RoleUserForm sm_Permission_RoleUserForm = new Sm_Permission_RoleUserForm();

				sm_Permission_RoleUserForm.setTheState(S_TheState.Normal);			
				sm_Permission_RoleUserForm.setSm_UserId(model.getUserId());

				Integer totalCount1 = sm_Permission_RoleUserDao.findByPage_Size(sm_Permission_RoleUserDao
						.getQuery_Size(sm_Permission_RoleUserDao.getBasicHQL(), sm_Permission_RoleUserForm));

				sm_Permission_RoleUserForm.setSm_Permission_RoleId(104851L);// 开发企业销售人员id：104851
				
				Integer totalCount2 = sm_Permission_RoleUserDao.findByPage_Size(sm_Permission_RoleUserDao
						.getQuery_Size(sm_Permission_RoleUserDao.getBasicHQL(), sm_Permission_RoleUserForm));

				if (totalCount1== 1&&totalCount2==1) {
					pageHome = "homeSaler.shtml";// 用户仅有销售管理员角色
				} else {
					pageHome = "homeDeveloper.shtml";
				}
				break;

			case "11":
				pageHome = "home.shtml";
				break;

			case "12":
				pageHome = "home.shtml";
				break;

			case "21":
				pageHome = "homeCooperativeAgency.shtml";
				pageHomeStr = "homeCooperativeAgency";
				break;

			default:
				pageHome = "home.shtml";
				break;
			}
		}
		
		properties.put("pageHome", pageHome);
		properties.put("pageHomeStr", pageHomeStr);

		// 是否签章
		String isSignature = user.getIsSignature();
		if (null == isSignature || isSignature.trim().isEmpty()) {
			isSignature = "0";
		}
		properties.put("isSignature", isSignature);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
