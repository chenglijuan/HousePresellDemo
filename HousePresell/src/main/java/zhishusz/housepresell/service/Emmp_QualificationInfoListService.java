package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Emmp_QualificationInfoForm;
import zhishusz.housepresell.database.dao.Emmp_QualificationInfoDao;
import zhishusz.housepresell.database.po.Emmp_QualificationInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：资质认证信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Emmp_QualificationInfoListService
{
	@Autowired
	private Emmp_QualificationInfoDao emmp_QualificationInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Emmp_QualificationInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = emmp_QualificationInfoDao.findByPage_Size(emmp_QualificationInfoDao.getQuery_Size(emmp_QualificationInfoDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Emmp_QualificationInfo> emmp_QualificationInfoList;
		if(totalCount > 0)
		{
			emmp_QualificationInfoList = emmp_QualificationInfoDao.findByPage(emmp_QualificationInfoDao.getQuery(emmp_QualificationInfoDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			emmp_QualificationInfoList = new ArrayList<Emmp_QualificationInfo>();
		}
		
		properties.put("emmp_QualificationInfoList", emmp_QualificationInfoList);
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
