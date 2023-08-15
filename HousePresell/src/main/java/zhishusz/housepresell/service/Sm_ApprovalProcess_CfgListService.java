package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_CfgForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_CfgDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：审批流-流程配置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_ApprovalProcess_CfgListService
{
	@Autowired
	private Sm_ApprovalProcess_CfgDao sm_ApprovalProcess_CfgDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_ApprovalProcess_CfgForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		String orderBy = model.getOrderBy();

		if(orderBy == null || orderBy.length() == 0)
		{
			model.setOrderBy(null);
		}

		if(keyword == null || keyword.length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%"+keyword+"%");
		}

		Integer totalCount = sm_ApprovalProcess_CfgDao.findByPage_Size(sm_ApprovalProcess_CfgDao.getQuery_Size(sm_ApprovalProcess_CfgDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_ApprovalProcess_Cfg> sm_ApprovalProcess_CfgList;
		if(totalCount > 0)
		{
			sm_ApprovalProcess_CfgList = sm_ApprovalProcess_CfgDao.findByPage(sm_ApprovalProcess_CfgDao.getQuery(sm_ApprovalProcess_CfgDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			sm_ApprovalProcess_CfgList = new ArrayList<Sm_ApprovalProcess_Cfg>();
		}
		
		properties.put("sm_ApprovalProcess_CfgList", sm_ApprovalProcess_CfgList);
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
