package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Sm_Permission_RoleDataForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDataDao;
import zhishusz.housepresell.database.po.Sm_Permission_RoleData;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：角色与数据权限对应关系
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_Permission_RoleDataListService
{
	@Autowired
	private Sm_Permission_RoleDataDao sm_Permission_RoleDataDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_Permission_RoleDataForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = sm_Permission_RoleDataDao.findByPage_Size(sm_Permission_RoleDataDao.getQuery_Size(sm_Permission_RoleDataDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_Permission_RoleData> sm_Permission_RoleDataList;
		if(totalCount > 0)
		{
			sm_Permission_RoleDataList = sm_Permission_RoleDataDao.findByPage(sm_Permission_RoleDataDao.getQuery(sm_Permission_RoleDataDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			sm_Permission_RoleDataList = new ArrayList<Sm_Permission_RoleData>();
		}
		
		properties.put("sm_Permission_RoleDataList", sm_Permission_RoleDataList);
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
