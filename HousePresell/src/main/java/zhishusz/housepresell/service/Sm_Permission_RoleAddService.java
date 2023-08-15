package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：管理角色
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Permission_RoleAddService
{
	private static final String BUSI_CODE = "010102";
	
	@Autowired
	private Sm_Permission_RoleDao sm_Permission_RoleDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Sm_Permission_RoleForm model)
	{
		MyDatetime myDatetime = MyDatetime.getInstance();
		
		Properties properties = new MyProperties();

		Sm_User userLogin = sm_UserDao.findById(model.getUserId());
		if(userLogin == null)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NeedLogin);
		}
		
		String theName = model.getTheName();
		String companyType = model.getCompanyType();
		String busiType = model.getBusiType();
		String downDateStr = model.getDownDateStr();
		String remark = model.getRemark();
		
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "请填写角色名称");
		}
		if(companyType == null || companyType.length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择机构类型");
		}
		if(downDateStr != null && downDateStr.length() > 0)
		{
			if(System.currentTimeMillis() > myDatetime.stringToLong(downDateStr))
			{
				return MyBackInfo.fail(properties, "停用日期必须大于启用日期");
			}
		}

		Sm_Permission_RoleForm sm_Permission_RoleForm = new Sm_Permission_RoleForm();
		sm_Permission_RoleForm.setTheName(theName);
		sm_Permission_RoleForm.setTheState(S_TheState.Normal);
		sm_Permission_RoleForm.setBusiType("0");
		Sm_Permission_Role sm_Permission_RoleCheck = sm_Permission_RoleDao.findOneByQuery_T(sm_Permission_RoleDao.getQuery(sm_Permission_RoleDao.getBasicHQL(), sm_Permission_RoleForm));
		if(sm_Permission_RoleCheck != null)
		{
			return MyBackInfo.fail(properties, "该名称的角色已存在并启用了");
		}
		
		Sm_Permission_Role sm_Permission_Role = new Sm_Permission_Role();
		sm_Permission_Role.setUserStart(userLogin);
		sm_Permission_Role.setCreateTimeStamp(System.currentTimeMillis());
		sm_Permission_Role.setUserUpdate(userLogin);
		sm_Permission_Role.setLastUpdateTimeStamp(System.currentTimeMillis());
		sm_Permission_Role.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		sm_Permission_Role.setTheName(theName);
		sm_Permission_Role.setCompanyType(companyType);
		sm_Permission_Role.setBusiType(busiType);//0:启用 1：停用
		sm_Permission_Role.setEnableTimeStamp(System.currentTimeMillis());
		if(downDateStr != null && downDateStr.length() > 0)
		{
			sm_Permission_Role.setDownTimeStamp(myDatetime.stringToLong(downDateStr));
		}
		sm_Permission_Role.setRemark(remark);
		sm_Permission_Role.setTheState(S_TheState.Normal);
		sm_Permission_RoleDao.save(sm_Permission_Role);

		properties.put("sm_Permission_Role", sm_Permission_Role);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
