package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Tgpf_FundAppropriatedForm;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriatedDao;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：资金拨付
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_FundAppropriatedListService
{
	@Autowired
	private Tgpf_FundAppropriatedDao tgpf_FundAppropriatedDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_FundAppropriatedForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();

		Integer totalCount = tgpf_FundAppropriatedDao.findByPage_Size(tgpf_FundAppropriatedDao.getQuery_Size(tgpf_FundAppropriatedDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgpf_FundAppropriated> tgpf_FundAppropriatedList;
		if(totalCount > 0)
		{
			tgpf_FundAppropriatedList = tgpf_FundAppropriatedDao.findByPage(tgpf_FundAppropriatedDao.getQuery(tgpf_FundAppropriatedDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tgpf_FundAppropriatedList = new ArrayList<Tgpf_FundAppropriated>();
		}
		
		properties.put("tgpf_FundAppropriatedList", tgpf_FundAppropriatedList);
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
