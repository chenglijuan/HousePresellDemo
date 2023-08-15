package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountForm;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：楼幢账户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpj_BuildingAccountListService
{
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpj_BuildingAccountForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = tgpj_BuildingAccountDao.findByPage_Size(tgpj_BuildingAccountDao.getQuery_Size(tgpj_BuildingAccountDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgpj_BuildingAccount> tgpj_BuildingAccountList;
		if(totalCount > 0)
		{
			tgpj_BuildingAccountList = tgpj_BuildingAccountDao.findByPage(tgpj_BuildingAccountDao.getQuery(tgpj_BuildingAccountDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tgpj_BuildingAccountList = new ArrayList<Tgpj_BuildingAccount>();
		}
		
		properties.put("tgpj_BuildingAccountList", tgpj_BuildingAccountList);
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
