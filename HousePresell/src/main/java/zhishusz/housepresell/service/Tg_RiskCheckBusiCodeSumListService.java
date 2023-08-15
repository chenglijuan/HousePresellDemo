package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_RiskCheckBusiCodeSumForm;
import zhishusz.housepresell.database.dao.Tg_RiskCheckBusiCodeSumDao;
import zhishusz.housepresell.database.po.Tg_RiskCheckBusiCodeSum;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：风控例行月汇总抽查表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tg_RiskCheckBusiCodeSumListService
{
	@Autowired
	private Tg_RiskCheckBusiCodeSumDao tg_RiskCheckBusiCodeSumDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tg_RiskCheckBusiCodeSumForm model)
	{
		Properties properties = new MyProperties();
		
		MyDatetime myDatetime = MyDatetime.getInstance();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		
		if(model.getSpotTimeStr() != null && model.getSpotTimeStr().length() > 0)
		{
			model.setSpotTimeStamp(myDatetime.stringToLong(model.getSpotTimeStr()+"-01"));
		}
		
		Integer totalCount = tg_RiskCheckBusiCodeSumDao.findByPage_Size(tg_RiskCheckBusiCodeSumDao.getQuery_Size(tg_RiskCheckBusiCodeSumDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tg_RiskCheckBusiCodeSum> tg_RiskCheckBusiCodeSumList;
		if(totalCount > 0)
		{
			tg_RiskCheckBusiCodeSumList = tg_RiskCheckBusiCodeSumDao.findByPage(tg_RiskCheckBusiCodeSumDao.getQuery(tg_RiskCheckBusiCodeSumDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tg_RiskCheckBusiCodeSumList = new ArrayList<Tg_RiskCheckBusiCodeSum>();
		}
		
		properties.put("tg_RiskCheckBusiCodeSumList", tg_RiskCheckBusiCodeSumList);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
