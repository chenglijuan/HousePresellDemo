package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：管理角色
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_Permission_RoleListService
{
	@Autowired
	private Sm_Permission_RoleDao sm_Permission_RoleDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_Permission_RoleForm model)
	{
		Properties properties = new MyProperties();
		if(StringUtils.isEmpty(model.getOrderBy())){
			model.setOrderBy("enableTimeStamp desc");
		}
		
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
		if(totalCount > 0)
		{
			sm_Permission_RoleList = sm_Permission_RoleDao.findByPage(sm_Permission_RoleDao.getQuery(sm_Permission_RoleDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			sm_Permission_RoleList = new ArrayList<Sm_Permission_Role>();
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
