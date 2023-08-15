package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Sm_eCode_LogForm;
import zhishusz.housepresell.database.dao.Sm_eCode_LogDao;
import zhishusz.housepresell.database.po.Sm_eCode_Log;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：eCode记录
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_eCode_LogListService
{
	@Autowired
	private Sm_eCode_LogDao sm_eCode_LogDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_eCode_LogForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = sm_eCode_LogDao.findByPage_Size(sm_eCode_LogDao.getQuery_Size(sm_eCode_LogDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_eCode_Log> sm_eCode_LogList;
		if(totalCount > 0)
		{
			sm_eCode_LogList = sm_eCode_LogDao.findByPage(sm_eCode_LogDao.getQuery(sm_eCode_LogDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			sm_eCode_LogList = new ArrayList<Sm_eCode_Log>();
		}
		
		properties.put("sm_eCode_LogList", sm_eCode_LogList);
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
