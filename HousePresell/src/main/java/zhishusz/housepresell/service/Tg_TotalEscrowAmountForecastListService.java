package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Tg_TotalEscrowAmountForecastForm;
import zhishusz.housepresell.database.dao.Tg_TotalEscrowAmountForecastDao;
import zhishusz.housepresell.database.po.Tg_TotalEscrowAmountForecast;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：托管总资金的预测结存
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tg_TotalEscrowAmountForecastListService
{
	@Autowired
	private Tg_TotalEscrowAmountForecastDao tg_TotalEscrowAmountForecastDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tg_TotalEscrowAmountForecastForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = tg_TotalEscrowAmountForecastDao.findByPage_Size(tg_TotalEscrowAmountForecastDao.getQuery_Size(tg_TotalEscrowAmountForecastDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tg_TotalEscrowAmountForecast> tg_TotalEscrowAmountForecastList;
		if(totalCount > 0)
		{
			tg_TotalEscrowAmountForecastList = tg_TotalEscrowAmountForecastDao.findByPage(tg_TotalEscrowAmountForecastDao.getQuery(tg_TotalEscrowAmountForecastDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tg_TotalEscrowAmountForecastList = new ArrayList<Tg_TotalEscrowAmountForecast>();
		}
		
		properties.put("tg_TotalEscrowAmountForecastList", tg_TotalEscrowAmountForecastList);
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
