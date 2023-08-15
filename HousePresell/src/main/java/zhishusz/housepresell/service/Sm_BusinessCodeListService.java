package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Sm_BusinessCodeForm;
import zhishusz.housepresell.database.dao.Sm_BusinessCodeDao;
import zhishusz.housepresell.database.po.Sm_BusinessCode;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：业务编号
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_BusinessCodeListService
{
	@Autowired
	private Sm_BusinessCodeDao sm_BusinessCodeDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_BusinessCodeForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = sm_BusinessCodeDao.findByPage_Size(sm_BusinessCodeDao.getQuery_Size(sm_BusinessCodeDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_BusinessCode> sm_BusinessCodeList;
		if(totalCount > 0)
		{
			sm_BusinessCodeList = sm_BusinessCodeDao.findByPage(sm_BusinessCodeDao.getQuery(sm_BusinessCodeDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			sm_BusinessCodeList = new ArrayList<Sm_BusinessCode>();
		}
		
		properties.put("sm_BusinessCodeList", sm_BusinessCodeList);
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
