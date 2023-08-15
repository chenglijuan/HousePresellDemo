package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Empj_PresellDocumentInfoForm;
import zhishusz.housepresell.database.dao.Empj_PresellDocumentInfoDao;
import zhishusz.housepresell.database.po.Empj_PresellDocumentInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：预售证信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_PresellDocumentInfoListService
{
	@Autowired
	private Empj_PresellDocumentInfoDao empj_PresellDocumentInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_PresellDocumentInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = empj_PresellDocumentInfoDao.findByPage_Size(empj_PresellDocumentInfoDao.getQuery_Size(empj_PresellDocumentInfoDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Empj_PresellDocumentInfo> empj_PresellDocumentInfoList;
		if(totalCount > 0)
		{
			empj_PresellDocumentInfoList = empj_PresellDocumentInfoDao.findByPage(empj_PresellDocumentInfoDao.getQuery(empj_PresellDocumentInfoDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			empj_PresellDocumentInfoList = new ArrayList<Empj_PresellDocumentInfo>();
		}
		
		properties.put("empj_PresellDocumentInfoList", empj_PresellDocumentInfoList);
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
