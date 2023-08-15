package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Tg_HandleTimeLimitConfigForm;
import zhishusz.housepresell.database.dao.Tg_HandleTimeLimitConfigDao;
import zhishusz.housepresell.database.po.Tg_HandleTimeLimitConfig;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：办理时限配置表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tg_HandleTimeLimitConfigListService
{
	@Autowired
	private Tg_HandleTimeLimitConfigDao tg_HandleTimeLimitConfigDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tg_HandleTimeLimitConfigForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = tg_HandleTimeLimitConfigDao.findByPage_Size(tg_HandleTimeLimitConfigDao.getQuery_Size(tg_HandleTimeLimitConfigDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tg_HandleTimeLimitConfig> tg_HandleTimeLimitConfigList;
		if(totalCount > 0)
		{
			tg_HandleTimeLimitConfigList = tg_HandleTimeLimitConfigDao.findByPage(tg_HandleTimeLimitConfigDao.getQuery(tg_HandleTimeLimitConfigDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tg_HandleTimeLimitConfigList = new ArrayList<Tg_HandleTimeLimitConfig>();
		}
		
		properties.put("tg_HandleTimeLimitConfigList", tg_HandleTimeLimitConfigList);
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
