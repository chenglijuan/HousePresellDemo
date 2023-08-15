package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Sm_Permission_RoleCompanyTypeForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleCompanyTypeDao;
import zhishusz.housepresell.database.po.Sm_Permission_RoleCompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：角色与机构类型对应关系
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_Permission_RoleCompanyTypeListService
{
	@Autowired
	private Sm_Permission_RoleCompanyTypeDao sm_Permission_RoleCompanyTypeDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_Permission_RoleCompanyTypeForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = sm_Permission_RoleCompanyTypeDao.findByPage_Size(sm_Permission_RoleCompanyTypeDao.getQuery_Size(sm_Permission_RoleCompanyTypeDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_Permission_RoleCompanyType> sm_Permission_RoleCompanyTypeList;
		if(totalCount > 0)
		{
			sm_Permission_RoleCompanyTypeList = sm_Permission_RoleCompanyTypeDao.findByPage(sm_Permission_RoleCompanyTypeDao.getQuery(sm_Permission_RoleCompanyTypeDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			sm_Permission_RoleCompanyTypeList = new ArrayList<Sm_Permission_RoleCompanyType>();
		}
		
		properties.put("sm_Permission_RoleCompanyTypeList", sm_Permission_RoleCompanyTypeList);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
