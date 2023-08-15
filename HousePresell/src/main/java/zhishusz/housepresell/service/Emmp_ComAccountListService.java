package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Emmp_ComAccountForm;
import zhishusz.housepresell.database.dao.Emmp_ComAccountDao;
import zhishusz.housepresell.database.po.Emmp_ComAccount;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：机构-财务账号信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Emmp_ComAccountListService
{
	@Autowired
	private Emmp_ComAccountDao emmp_ComAccountDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Emmp_ComAccountForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = emmp_ComAccountDao.findByPage_Size(emmp_ComAccountDao.getQuery_Size(emmp_ComAccountDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Emmp_ComAccount> emmp_ComAccountList;
		if(totalCount > 0)
		{
			emmp_ComAccountList = emmp_ComAccountDao.findByPage(emmp_ComAccountDao.getQuery(emmp_ComAccountDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			emmp_ComAccountList = new ArrayList<Emmp_ComAccount>();
		}
		
		properties.put("emmp_ComAccountList", emmp_ComAccountList);
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
