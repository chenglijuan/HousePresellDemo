package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import zhishusz.housepresell.database.po.state.S_TheState;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Empj_PjDevProgressForcastForm;
import zhishusz.housepresell.database.dao.Empj_PjDevProgressForcastDao;
import zhishusz.housepresell.database.po.Empj_PjDevProgressForcast;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：项目-工程进度预测-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_PjDevProgressForcastListService
{
	@Autowired
	private Empj_PjDevProgressForcastDao empj_PjDevProgressForcastDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_PjDevProgressForcastForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		String busiState = model.getBusiState();
		if (keyword != null && !"".equals(keyword))
		{
			model.setKeyword("%"+keyword+"%");
		}
		else
		{
			model.setKeyword(null);
		}
		if ("".equals(busiState) || "0".equals(busiState) || "全部".equals(busiState))
		{
			model.setBusiState(null);
		}
		model.setTheState(S_TheState.Normal);

		Integer totalCount =
				empj_PjDevProgressForcastDao.findByPage_Size(empj_PjDevProgressForcastDao.createNewCriteriaForList(model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Empj_PjDevProgressForcast> empj_PjDevProgressForcastList;
		if(totalCount > 0)
		{//(empj_PjDevProgressForcastDao.createCriteriaForList(model, Order.desc("patrolTimestamp"))
			empj_PjDevProgressForcastList =
					empj_PjDevProgressForcastDao.findByPage(empj_PjDevProgressForcastDao.createNewCriteriaForList(model)
							, pageNumber, countPerPage);
		}
		else
		{
			empj_PjDevProgressForcastList = new ArrayList<Empj_PjDevProgressForcast>();
		}
		
		properties.put("empj_PjDevProgressForcastList", empj_PjDevProgressForcastList);
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
