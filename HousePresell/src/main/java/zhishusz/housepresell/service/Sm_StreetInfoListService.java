package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_StreetInfoForm;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Sm_StreetInfoDao;
import zhishusz.housepresell.database.po.Sm_StreetInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：Sm_StreetInfo
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_StreetInfoListService
{
	@Autowired
	private Sm_StreetInfoDao sm_StreetInfoDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_StreetInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		model.setCityRegion(sm_CityRegionInfoDao.findById(model.getCityRegionId()));
		
		Integer totalCount = sm_StreetInfoDao.findByPage_Size(sm_StreetInfoDao.getQuery_Size(sm_StreetInfoDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_StreetInfo> sm_StreetInfoList;
		if(totalCount > 0)
		{
			sm_StreetInfoList = sm_StreetInfoDao.findByPage(sm_StreetInfoDao.getQuery(sm_StreetInfoDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			sm_StreetInfoList = new ArrayList<Sm_StreetInfo>();
		}
		
		properties.put("sm_StreetInfoList", sm_StreetInfoList);
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
