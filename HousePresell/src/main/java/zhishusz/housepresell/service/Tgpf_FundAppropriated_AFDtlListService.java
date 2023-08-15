package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFDtlForm;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：申请-用款-明细
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_FundAppropriated_AFDtlListService
{
	@Autowired
	private Tgpf_FundAppropriated_AFDtlDao tgpf_FundAppropriated_AFDtlDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_FundAppropriated_AFDtlForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = tgpf_FundAppropriated_AFDtlDao.findByPage_Size(tgpf_FundAppropriated_AFDtlDao.getQuery_Size(tgpf_FundAppropriated_AFDtlDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgpf_FundAppropriated_AFDtl> tgpf_FundAppropriated_AFDtlList;
		if(totalCount > 0)
		{
			tgpf_FundAppropriated_AFDtlList = tgpf_FundAppropriated_AFDtlDao.findByPage(tgpf_FundAppropriated_AFDtlDao.getQuery(tgpf_FundAppropriated_AFDtlDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tgpf_FundAppropriated_AFDtlList = new ArrayList<Tgpf_FundAppropriated_AFDtl>();
		}
		
		properties.put("tgpf_FundAppropriated_AFDtlList", tgpf_FundAppropriated_AFDtlList);
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
