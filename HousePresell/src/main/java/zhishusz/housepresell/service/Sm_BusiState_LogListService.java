package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_BusiState_LogForm;
import zhishusz.housepresell.database.dao.Sm_BusiState_LogDao;
import zhishusz.housepresell.database.po.Sm_BusiState_Log;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：日志-业务状态
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_BusiState_LogListService
{
	@Autowired
	private Sm_BusiState_LogDao sm_BusiState_LogDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_BusiState_LogForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		if(keyword != null && keyword.length()>0)
		{
			model.setKeyword("%"+keyword+"%");
		}
		else {
			model.setKeyword(null);
		}
		
		Integer totalCount = sm_BusiState_LogDao.findByPage_Size(sm_BusiState_LogDao.getQuery_Size(sm_BusiState_LogDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_BusiState_Log> sm_BusiState_LogList;
		if(totalCount > 0)
		{
			sm_BusiState_LogList = sm_BusiState_LogDao.findByPage(sm_BusiState_LogDao.getQuery(sm_BusiState_LogDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			sm_BusiState_LogList = new ArrayList<Sm_BusiState_Log>();
		}
		
		properties.put("sm_BusiState_LogList", sm_BusiState_LogList);
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
