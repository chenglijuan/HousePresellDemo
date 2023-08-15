package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Empj_BuildingAccountSupervisedForm;
import zhishusz.housepresell.database.dao.Empj_BuildingAccountSupervisedDao;
import zhishusz.housepresell.database.po.Empj_BuildingAccountSupervised;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：楼幢与楼幢监管账号关联表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_BuildingAccountSupervisedListService
{
	@Autowired
	private Empj_BuildingAccountSupervisedDao empj_BuildingAccountSupervisedDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_BuildingAccountSupervisedForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = empj_BuildingAccountSupervisedDao.findByPage_Size(empj_BuildingAccountSupervisedDao.getQuery_Size(empj_BuildingAccountSupervisedDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Empj_BuildingAccountSupervised> empj_BuildingAccountSupervisedList;
		if(totalCount > 0)
		{
			empj_BuildingAccountSupervisedList = empj_BuildingAccountSupervisedDao.findByPage(empj_BuildingAccountSupervisedDao.getQuery(empj_BuildingAccountSupervisedDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			empj_BuildingAccountSupervisedList = new ArrayList<Empj_BuildingAccountSupervised>();
		}
		
		properties.put("empj_BuildingAccountSupervisedList", empj_BuildingAccountSupervisedList);
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
