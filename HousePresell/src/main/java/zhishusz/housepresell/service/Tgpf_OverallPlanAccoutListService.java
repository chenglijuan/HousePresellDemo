package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Tgpf_OverallPlanAccoutForm;
import zhishusz.housepresell.database.dao.Tgpf_OverallPlanAccoutDao;
import zhishusz.housepresell.database.po.Tgpf_OverallPlanAccout;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：统筹-账户状况信息保存表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_OverallPlanAccoutListService
{
	@Autowired
	private Tgpf_OverallPlanAccoutDao tgpf_OverallPlanAccoutDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_OverallPlanAccoutForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = tgpf_OverallPlanAccoutDao.findByPage_Size(tgpf_OverallPlanAccoutDao.getQuery_Size(tgpf_OverallPlanAccoutDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgpf_OverallPlanAccout> tgpf_OverallPlanAccoutList;
		if(totalCount > 0)
		{
			tgpf_OverallPlanAccoutList = tgpf_OverallPlanAccoutDao.findByPage(tgpf_OverallPlanAccoutDao.getQuery(tgpf_OverallPlanAccoutDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tgpf_OverallPlanAccoutList = new ArrayList<Tgpf_OverallPlanAccout>();
		}
		
		properties.put("tgpf_OverallPlanAccoutList", tgpf_OverallPlanAccoutList);
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
