package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Sm_FastNavigateForm;
import zhishusz.housepresell.database.dao.Sm_FastNavigateDao;
import zhishusz.housepresell.database.po.Sm_FastNavigate;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：快捷导航信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_FastNavigateListService
{
	@Autowired
	private Sm_FastNavigateDao sm_FastNavigateDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_FastNavigateForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = sm_FastNavigateDao.findByPage_Size(sm_FastNavigateDao.getQuery_Size(sm_FastNavigateDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_FastNavigate> sm_FastNavigateList;
		if(totalCount > 0)
		{
			sm_FastNavigateList = sm_FastNavigateDao.findByPage(sm_FastNavigateDao.getQuery(sm_FastNavigateDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			sm_FastNavigateList = new ArrayList<Sm_FastNavigate>();
		}
		
		properties.put("sm_FastNavigateList", sm_FastNavigateList);
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
