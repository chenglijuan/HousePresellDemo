package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Empj_ProjectInfo_AFForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfo_AFDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo_AF;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：申请表-项目信息变更(审批)
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_ProjectInfo_AFListService
{
	@Autowired
	private Empj_ProjectInfo_AFDao empj_ProjectInfo_AFDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_ProjectInfo_AFForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = empj_ProjectInfo_AFDao.findByPage_Size(empj_ProjectInfo_AFDao.getQuery_Size(empj_ProjectInfo_AFDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Empj_ProjectInfo_AF> empj_ProjectInfo_AFList;
		if(totalCount > 0)
		{
			empj_ProjectInfo_AFList = empj_ProjectInfo_AFDao.findByPage(empj_ProjectInfo_AFDao.getQuery(empj_ProjectInfo_AFDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			empj_ProjectInfo_AFList = new ArrayList<Empj_ProjectInfo_AF>();
		}
		
		properties.put("empj_ProjectInfo_AFList", empj_ProjectInfo_AFList);
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
