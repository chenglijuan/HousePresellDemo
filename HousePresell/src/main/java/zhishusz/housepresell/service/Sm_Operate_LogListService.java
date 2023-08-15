package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Sm_Operate_LogForm;
import zhishusz.housepresell.database.dao.Sm_Operate_LogDao;
import zhishusz.housepresell.database.po.Sm_Operate_Log;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：日志-关键操作
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_Operate_LogListService
{
	@Autowired
	private Sm_Operate_LogDao sm_Operate_LogDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_Operate_LogForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = sm_Operate_LogDao.findByPage_Size(sm_Operate_LogDao.getQuery_Size(sm_Operate_LogDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_Operate_Log> sm_Operate_LogList;
		if(totalCount > 0)
		{
			sm_Operate_LogList = sm_Operate_LogDao.findByPage(sm_Operate_LogDao.getQuery(sm_Operate_LogDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			sm_Operate_LogList = new ArrayList<Sm_Operate_Log>();
		}
		
		properties.put("sm_Operate_LogList", sm_Operate_LogList);
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
