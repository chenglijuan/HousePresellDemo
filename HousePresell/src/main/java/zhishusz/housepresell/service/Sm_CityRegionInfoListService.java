package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Sm_CityRegionInfoForm;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_UserType;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：基础数据-城市区域
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_CityRegionInfoListService
{
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_CityRegionInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Sm_User user = model.getUser();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		if(S_UserType.ZhengtaiUser.equals(user.getTheType()) )
		{
			model.setCityRegionInfoIdArr(null);
		}
		
		Emmp_CompanyInfo company = user.getCompany();
		if(null != company && S_CompanyType.Development.equals(company.getTheType()))
		{
			model.setCityRegionInfoIdArr(null);
		}
		
		Integer totalCount = sm_CityRegionInfoDao.findByPage_Size(sm_CityRegionInfoDao.getQuery_Size(sm_CityRegionInfoDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_CityRegionInfo> sm_CityRegionInfoList;
		if(totalCount > 0)
		{
			sm_CityRegionInfoList = sm_CityRegionInfoDao.findByPage(sm_CityRegionInfoDao.getQuery(sm_CityRegionInfoDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			sm_CityRegionInfoList = new ArrayList<Sm_CityRegionInfo>();
		}
		
		properties.put("sm_CityRegionInfoList", sm_CityRegionInfoList);
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
