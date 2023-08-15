package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import zhishusz.housepresell.database.po.state.S_TheState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Empj_PjDevProgressForcastDtlForm;
import zhishusz.housepresell.database.dao.Empj_PjDevProgressForcastDtlDao;
import zhishusz.housepresell.database.po.Empj_PjDevProgressForcastDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：项目-工程进度预测 -明细表 
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_PjDevProgressForcastDtlListService
{
	@Autowired
	private Empj_PjDevProgressForcastDtlDao empj_PjDevProgressForcastDtlDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_PjDevProgressForcastDtlForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		Integer theState = S_TheState.Normal;
		model.setTheState(theState);

		Integer totalCount = empj_PjDevProgressForcastDtlDao.findByPage_Size(empj_PjDevProgressForcastDtlDao.getQuery_Size(empj_PjDevProgressForcastDtlDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;

		List<Empj_PjDevProgressForcastDtl> empj_PjDevProgressForcastDtlList;
		if(totalCount > 0)
		{
			empj_PjDevProgressForcastDtlList = empj_PjDevProgressForcastDtlDao.findByPage(empj_PjDevProgressForcastDtlDao.getQuery(empj_PjDevProgressForcastDtlDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			empj_PjDevProgressForcastDtlList = new ArrayList<Empj_PjDevProgressForcastDtl>();
		}
		
		properties.put("empj_PjDevProgressForcastDtlList", empj_PjDevProgressForcastDtlList);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}

	@SuppressWarnings("unchecked")
	public Properties executeTemporary(Empj_PjDevProgressForcastDtlForm model)
	{
		Properties properties = new MyProperties();

//		Integer theState = S_TheState.Normal;   //编辑主表时，删除明细表信息，如果点击保存按钮后才更新主表与明细表关联数据，可释放此处
//		model.setTheState(theState);
		Long[] idArr = model.getIdArr();

		List<Empj_PjDevProgressForcastDtl> empj_PjDevProgressForcastDtlList;
		if (idArr != null && idArr.length > 0)
		{
			empj_PjDevProgressForcastDtlList = empj_PjDevProgressForcastDtlDao.findByPage(empj_PjDevProgressForcastDtlDao.getQuery(empj_PjDevProgressForcastDtlDao.getExcelListHQL(), model), null, null);
		}
		else
		{
			empj_PjDevProgressForcastDtlList =  new ArrayList<Empj_PjDevProgressForcastDtl>();
		}

		properties.put("empj_PjDevProgressForcastDtlList", empj_PjDevProgressForcastDtlList);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
