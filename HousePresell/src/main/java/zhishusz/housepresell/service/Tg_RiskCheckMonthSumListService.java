package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_RiskCheckMonthSumForm;
import zhishusz.housepresell.database.dao.Tg_RiskCheckMonthSumDao;
import zhishusz.housepresell.database.po.Tg_RiskCheckMonthSum;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TimeType;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：风控例行月汇总抽查表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tg_RiskCheckMonthSumListService
{
	@Autowired
	private Tg_RiskCheckMonthSumDao tg_RiskCheckMonthSumDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tg_RiskCheckMonthSumForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		
		String riskCheckSearchDateStr = model.getRiskCheckSearchDateStr();
		if(riskCheckSearchDateStr != null && riskCheckSearchDateStr.length() > 0)
		{
			String[] riskCheckSearchDateArr = riskCheckSearchDateStr.split(" - ");
			model.setSearchStartTimeStamp(MyDatetime.getInstance().stringToLong(riskCheckSearchDateArr[0]));
			model.setSearchEndTimeStamp(MyDatetime.getInstance().stringToLong(riskCheckSearchDateArr[1])+S_TimeType.Day);
		}
		
		Integer totalCount = tg_RiskCheckMonthSumDao.findByPage_Size(tg_RiskCheckMonthSumDao.getQuery_Size(tg_RiskCheckMonthSumDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tg_RiskCheckMonthSum> tg_RiskCheckMonthSumList;
		if(totalCount > 0)
		{
			tg_RiskCheckMonthSumList = tg_RiskCheckMonthSumDao.findByPage(tg_RiskCheckMonthSumDao.getQuery(tg_RiskCheckMonthSumDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tg_RiskCheckMonthSumList = new ArrayList<Tg_RiskCheckMonthSum>();
		}
		
		properties.put("tg_RiskCheckMonthSumList", tg_RiskCheckMonthSumList);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
