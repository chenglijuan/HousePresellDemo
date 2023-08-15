package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_CfgForm;
import zhishusz.housepresell.controller.form.Sm_MessageTemplate_CfgForm;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/*
 * Service添加操作：审批流-流程配置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_MessageTemplate_CfgAddService
{
	@Autowired
	private Sm_MessageTemplate_CfgDao sm_messageTemplate_cfgDao;
	@Autowired
	private Sm_Permission_RoleDao sm_permission_roleDao;
	@Autowired
	private Sm_UserDao sm_userDao;
	@Autowired
	private Sm_BaseParameterDao sm_baseParameterDao;
	
	public Properties execute(Sm_MessageTemplate_CfgForm model)
	{
		Properties properties = new MyProperties();

		Long userId = model.getUserId();
		Long busiId = model.getBusiId();
		String eCode = model.geteCode();//模板编码
		String theName = model.getTheName();  //消息模板名称
		String theDescribe = model.getTheDescribe(); //模板描述
		String theTitle = model.getTheTitle(); //消息标题
		String theContent = model.getTheContent();//消息内容
		Long []idArr = model.getIdArr(); //角色Id

		if(userId == null || userId < 1)
		{
			return MyBackInfo.fail(properties, "'登录用户'不能为空");
		}
		Sm_User userStart = sm_userDao.findById(userId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'登录用户'不能为空");
		}

		Sm_BaseParameter sm_baseParameter = sm_baseParameterDao.findById(busiId);
		if(sm_baseParameter == null )
		{
			return MyBackInfo.fail(properties, "'请选择'业务编码");
		}
		String busiCode = sm_baseParameter.getTheValue(); //业务编码

		if(eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'模板编码'不能为空");
		}
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "'消息模板名称'不能为空");
		}

		List<Sm_Permission_Role> sm_permission_roleList = new ArrayList<>();
		if(idArr !=null && idArr.length > 0)
		{
			for(Long roleId : idArr)
			{
				Sm_Permission_Role sm_permission_role = sm_permission_roleDao.findById(roleId);
				if(sm_permission_role == null)
				{
					return MyBackInfo.fail(properties, "'角色信息'不能为空");
				}
				sm_permission_roleList.add(sm_permission_role);
			}
		}

		Sm_MessageTemplate_Cfg sm_messageTemplate_cfg = new Sm_MessageTemplate_Cfg();
		sm_messageTemplate_cfg.setTheState(S_TheState.Normal);
		sm_messageTemplate_cfg.setUserStart(userStart);
		sm_messageTemplate_cfg.setUserUpdate(userStart);
		sm_messageTemplate_cfg.setSm_baseParameter(sm_baseParameter);
		sm_messageTemplate_cfg.setBusiCode(busiCode);
		sm_messageTemplate_cfg.seteCode(eCode);
		sm_messageTemplate_cfg.setTheName(theName);
		sm_messageTemplate_cfg.setTheDescribe(theDescribe);
		sm_messageTemplate_cfg.setTheTitle(theTitle);
		sm_messageTemplate_cfg.setTheContent(theContent);
		sm_messageTemplate_cfg.setCreateTimeStamp(System.currentTimeMillis());
		sm_messageTemplate_cfg.setLastUpdateTimeStamp(System.currentTimeMillis());
		sm_messageTemplate_cfg.setSm_permission_roleList(sm_permission_roleList);
		sm_messageTemplate_cfgDao.save(sm_messageTemplate_cfg);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tableId",sm_messageTemplate_cfg.getTableId());

		return properties;
	}
}
