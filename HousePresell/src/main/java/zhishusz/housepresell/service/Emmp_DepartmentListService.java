package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Emmp_DepartmentForm;
import zhishusz.housepresell.database.dao.Emmp_DepartmentDao;
import zhishusz.housepresell.database.po.Emmp_Department;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：部门
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Emmp_DepartmentListService
{
	@Autowired
	private Emmp_DepartmentDao emmp_DepartmentDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Emmp_DepartmentForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = emmp_DepartmentDao.findByPage_Size(emmp_DepartmentDao.getQuery_Size(emmp_DepartmentDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Emmp_Department> emmp_DepartmentList;
		if(totalCount > 0)
		{
			emmp_DepartmentList = emmp_DepartmentDao.findByPage(emmp_DepartmentDao.getQuery(emmp_DepartmentDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			emmp_DepartmentList = new ArrayList<Emmp_Department>();
		}
		
		properties.put("emmp_DepartmentList", emmp_DepartmentList);
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
