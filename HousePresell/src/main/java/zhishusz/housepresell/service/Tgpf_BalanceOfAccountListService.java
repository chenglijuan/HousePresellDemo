package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：对账列表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_BalanceOfAccountListService
{
	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_BalanceOfAccountForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = tgpf_BalanceOfAccountDao.findByPage_Size(tgpf_BalanceOfAccountDao.getQuery_Size(tgpf_BalanceOfAccountDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgpf_BalanceOfAccount> tgpf_BalanceOfAccountList;
		if(totalCount > 0)
		{
			tgpf_BalanceOfAccountList = tgpf_BalanceOfAccountDao.findByPage(tgpf_BalanceOfAccountDao.getQuery(tgpf_BalanceOfAccountDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tgpf_BalanceOfAccountList = new ArrayList<Tgpf_BalanceOfAccount>();
		}
		
		properties.put("tgpf_BalanceOfAccountList", tgpf_BalanceOfAccountList);
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
