package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_RecordForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_RecordDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Record;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：审批流-审批记录
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_ApprovalProcess_RecordListService
{
	@Autowired
	private Sm_ApprovalProcess_RecordDao sm_ApprovalProcess_RecordDao;
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_approvalProcess_afDao ;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_ApprovalProcess_RecordForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = sm_ApprovalProcess_RecordDao.findByPage_Size(sm_ApprovalProcess_RecordDao.getQuery_Size(sm_ApprovalProcess_RecordDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_ApprovalProcess_Record> sm_ApprovalProcess_RecordList;
		if(totalCount > 0)
		{
			sm_ApprovalProcess_RecordList = sm_ApprovalProcess_RecordDao.findByPage(sm_ApprovalProcess_RecordDao.getQuery(sm_ApprovalProcess_RecordDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			sm_ApprovalProcess_RecordList = new ArrayList<Sm_ApprovalProcess_Record>();
		}
		
		properties.put("sm_ApprovalProcess_RecordList", sm_ApprovalProcess_RecordList);
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
