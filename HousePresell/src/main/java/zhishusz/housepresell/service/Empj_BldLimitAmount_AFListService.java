package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
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
 * Service列表查询：申请表-受限额度变更
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_BldLimitAmount_AFListService
{
	@Autowired
	private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_BldLimitAmount_AFForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		Integer totalCount = empj_BldLimitAmount_AFDao.findByPage_Size(empj_BldLimitAmount_AFDao.createCriteriaForList(model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Empj_BldLimitAmount_AF> empj_BldLimitAmount_AFList;
		List<Empj_BldLimitAmount_AF> empj_BldLimitAmount_AFListPage = new ArrayList<Empj_BldLimitAmount_AF>(); 
		if(totalCount > 0)
		{
//			empj_BldLimitAmount_AFList = empj_BldLimitAmount_AFDao.findByPage(empj_BldLimitAmount_AFDao.createCriteriaForList(model), pageNumber, countPerPage);
			empj_BldLimitAmount_AFList = empj_BldLimitAmount_AFDao.findByPage(empj_BldLimitAmount_AFDao.createCriteriaForList(model));
			int startPage = (pageNumber - 1 ) * countPerPage;
			int endPage = pageNumber * countPerPage;
			if(endPage > empj_BldLimitAmount_AFList.size())
			{
				endPage = empj_BldLimitAmount_AFList.size();
			}
			empj_BldLimitAmount_AFListPage = empj_BldLimitAmount_AFList.subList(startPage, endPage);
		}
		else
		{
			empj_BldLimitAmount_AFList = new ArrayList<Empj_BldLimitAmount_AF>();
		}
		
		properties.put("empj_BldLimitAmount_AFList", empj_BldLimitAmount_AFListPage);
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
