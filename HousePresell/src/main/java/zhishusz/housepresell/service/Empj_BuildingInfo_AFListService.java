package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Empj_BuildingInfo_AFForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfo_AFDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo_AF;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：申请表-楼幢变更
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_BuildingInfo_AFListService
{
	@Autowired
	private Empj_BuildingInfo_AFDao empj_BuildingInfo_AFDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_BuildingInfo_AFForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = empj_BuildingInfo_AFDao.findByPage_Size(empj_BuildingInfo_AFDao.getQuery_Size(empj_BuildingInfo_AFDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Empj_BuildingInfo_AF> empj_BuildingInfo_AFList;
		if(totalCount > 0)
		{
			empj_BuildingInfo_AFList = empj_BuildingInfo_AFDao.findByPage(empj_BuildingInfo_AFDao.getQuery(empj_BuildingInfo_AFDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			empj_BuildingInfo_AFList = new ArrayList<Empj_BuildingInfo_AF>();
		}
		
		properties.put("empj_BuildingInfo_AFList", empj_BuildingInfo_AFList);
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
