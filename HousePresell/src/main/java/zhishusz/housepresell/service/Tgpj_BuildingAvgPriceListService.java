package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：楼幢-备案均价
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpj_BuildingAvgPriceListService
	{
	@Autowired
	private Tgpj_BuildingAvgPriceDao tgpj_BuildingAvgPriceDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpj_BuildingAvgPriceForm model)
	{
		Properties properties = new MyProperties();
		if(StringUtils.isEmpty(model.getOrderBy())){
			model.setOrderBy(null);
		}
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		Integer totalCount = tgpj_BuildingAvgPriceDao.findByPage_Size(tgpj_BuildingAvgPriceDao.createCriteriaForList(model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgpj_BuildingAvgPrice> tgpj_BuildingAvgPriceList;
		if(totalCount > 0)
		{
			tgpj_BuildingAvgPriceList = tgpj_BuildingAvgPriceDao.findByPage(tgpj_BuildingAvgPriceDao.createCriteriaForList(model), pageNumber, countPerPage);
		}
		else
		{
			tgpj_BuildingAvgPriceList = new ArrayList<Tgpj_BuildingAvgPrice>();
		}
		
		properties.put("tgpj_BuildingAvgPriceList", tgpj_BuildingAvgPriceList);
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
