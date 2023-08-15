package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Empj_BldEscrowCompleted_DtlForm;
import zhishusz.housepresell.database.dao.Empj_BldEscrowCompleted_DtlDao;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted_Dtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：申请表-项目托管终止（审批）-明细表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_BldEscrowCompleted_DtlListService
{
	@Autowired
	private Empj_BldEscrowCompleted_DtlDao empj_BldEscrowCompleted_DtlDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_BldEscrowCompleted_DtlForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		String busiState = model.getBusiState();
		Long developConpanyId = model.getDevelopCompanyId();
		Long projectId = model.getProjectId();

		if (keyword != null && !"".equals(keyword)) 
		{
			model.setKeyword("%"+keyword+"%");
		}
		else
		{
			model.setKeyword(null);
		}
		if ("0".equals(busiState)) 
		{
			model.setBusiState(null);
		}
		if (developConpanyId == null || developConpanyId <= 1)
		{
			model.setDevelopCompanyId(null);
		}
		if (projectId == null || projectId <= 1)
		{
			model.setProjectId(null);			
		}
		model.setTheState(S_TheState.Normal);

		Integer totalCount =
				empj_BldEscrowCompleted_DtlDao.findByPage_Size(empj_BldEscrowCompleted_DtlDao.createCriteriaForList(model, null));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Empj_BldEscrowCompleted_Dtl> empj_BldEscrowCompleted_DtlList;
		if(totalCount > 0)
		{
			empj_BldEscrowCompleted_DtlList =
					empj_BldEscrowCompleted_DtlDao.findByPage(empj_BldEscrowCompleted_DtlDao.createCriteriaForList(model, Order.desc("lastUpdateTimeStamp")), pageNumber,
					countPerPage);
		}
		else
		{
			empj_BldEscrowCompleted_DtlList = new ArrayList<Empj_BldEscrowCompleted_Dtl>();
		}
		
		properties.put("empj_BldEscrowCompleted_DtlList", empj_BldEscrowCompleted_DtlList);
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
