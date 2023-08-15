package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：附件
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_AttachmentListService
{
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_AttachmentForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = sm_AttachmentDao.findByPage_Size(sm_AttachmentDao.getQuery_Size(sm_AttachmentDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_Attachment> sm_AttachmentList;
		if(totalCount > 0)
		{
			sm_AttachmentList = sm_AttachmentDao.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			sm_AttachmentList = new ArrayList<Sm_Attachment>();
		}
		
		properties.put("sm_AttachmentList", sm_AttachmentList);
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
