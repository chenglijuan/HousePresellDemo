package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Sm_Permission_RoleForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_RoleBusiType;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：管理角色下拉选
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_Permission_RoleListForSelectService
{
	@Autowired
	private Sm_Permission_RoleDao sm_Permission_RoleDao;
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_Permission_RoleForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword_Old = model.getKeyword();
		String keyword = Checker.getInstance().checkKeyword(model.getKeyword());
		model.setKeyword(keyword);

		String enableDateSearchStr = model.getEnableDateSearchStr();
		if(enableDateSearchStr != null && enableDateSearchStr.length() > 0)
		{
			String[] enableDateSearchArr = enableDateSearchStr.split(" - ");
			model.setEnableTimeStamp(MyDatetime.getInstance().stringToLong(enableDateSearchArr[0]));
			model.setDownTimeStamp(MyDatetime.getInstance().stringToLong(enableDateSearchArr[1]));
		}
		
		Integer totalCount = sm_Permission_RoleDao.findByPage_Size(sm_Permission_RoleDao.getQuery_Size(sm_Permission_RoleDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_Permission_Role> sm_Permission_RoleList;
		List<Sm_Permission_Role> sm_Permission_RoleList_Com;
		if(totalCount > 0)
		{
		    sm_Permission_RoleList = sm_Permission_RoleDao.findByPage(sm_Permission_RoleDao.getQuery(sm_Permission_RoleDao.getBasicHQL(), model), pageNumber, countPerPage);
		    if("12".equals(model.getCompanyType())){
		        model.setCompanyType("31");
		        sm_Permission_RoleList_Com = sm_Permission_RoleDao.findByPage(sm_Permission_RoleDao.getQuery(sm_Permission_RoleDao.getBasicHQL(), model), pageNumber, countPerPage);
		        sm_Permission_RoleList.addAll(sm_Permission_RoleList_Com);
		    }
		    
		}
		else
		{
			sm_Permission_RoleList = new ArrayList<Sm_Permission_Role>();
		}
		
		Long chooseUserId = model.getChooseUserId();//有选择的用户
		if(chooseUserId != null)
		{
			Sm_Permission_RoleUserForm sm_Permission_RoleUserForm = new Sm_Permission_RoleUserForm();
			sm_Permission_RoleUserForm.setTheState(S_TheState.Normal);
			sm_Permission_RoleUserForm.setSm_UserId(chooseUserId);
			List<Sm_Permission_RoleUser> sm_Permission_RoleUserList = sm_Permission_RoleUserDao.findByPage(sm_Permission_RoleUserDao.getQuery(sm_Permission_RoleUserDao.getBasicHQL(), sm_Permission_RoleUserForm));
			if(sm_Permission_RoleUserList != null && !sm_Permission_RoleUserList.isEmpty())
			{
				for(Sm_Permission_RoleUser sm_Permission_RoleUser : sm_Permission_RoleUserList)
				{
					if(S_RoleBusiType.InValid.equals(sm_Permission_RoleUser.getSm_Permission_Role().getBusiType()))
					{
						sm_Permission_RoleList.add(sm_Permission_RoleUser.getSm_Permission_Role());
					}
				}
			}
		}
		
		properties.put("sm_Permission_RoleList", sm_Permission_RoleList);
		properties.put(S_NormalFlag.keyword, keyword_Old);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
