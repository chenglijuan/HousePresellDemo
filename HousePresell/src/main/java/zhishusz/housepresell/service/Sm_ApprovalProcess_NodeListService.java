package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_NodeForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_NodeDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Node;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：审批流-节点
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_ApprovalProcess_NodeListService
{
	@Autowired
	private Sm_ApprovalProcess_NodeDao sm_ApprovalProcess_NodeDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_ApprovalProcess_NodeForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();

		Integer totalCount = sm_ApprovalProcess_NodeDao.findByPage_Size(sm_ApprovalProcess_NodeDao.getQuery_Size(sm_ApprovalProcess_NodeDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		List<Sm_ApprovalProcess_Node> sm_ApprovalProcess_NodeList;

		if(model.getIdArr()!=null && model.getIdArr().length ==0)
		{
			sm_ApprovalProcess_NodeList = null;
		}
		else
		{
			if(totalCount > 0)
			{
				sm_ApprovalProcess_NodeList = sm_ApprovalProcess_NodeDao.findByPage(sm_ApprovalProcess_NodeDao.getQuery(sm_ApprovalProcess_NodeDao.getBasicHQL(), model), pageNumber, countPerPage);
			}
			else
			{
				sm_ApprovalProcess_NodeList = new ArrayList<Sm_ApprovalProcess_Node>();
			}
		}
		
		properties.put("sm_ApprovalProcess_NodeList", sm_ApprovalProcess_NodeList);
		properties.put("sm_ApprovalProcess_ModalNodeList", sm_ApprovalProcess_NodeList);
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
