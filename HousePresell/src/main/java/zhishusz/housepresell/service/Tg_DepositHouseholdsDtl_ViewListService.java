package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_DepositHouseholdsDtl_ViewForm;
import zhishusz.housepresell.database.dao.Tg_DepositHouseholdsDtl_ViewDao;
import zhishusz.housepresell.database.po.Tg_DepositHouseholdsDtl_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：托管项目户信息表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tg_DepositHouseholdsDtl_ViewListService
{
	@Autowired
	private Tg_DepositHouseholdsDtl_ViewDao tg_DepositHouseholdsDtl_ViewDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tg_DepositHouseholdsDtl_ViewForm model)
	{
		Properties properties = new MyProperties();
		
		String keyword = model.getKeyword();
		
		if (null != keyword && !keyword.trim().isEmpty())
		{
			model.setKeyword("%"+keyword+"%");
		}else{
			model.setKeyword(null);
		}
		
		if(null!=model.getPayWay())
		{
			model.setPayMethod(model.getPayWay());
		}
		
		if(null != model.getCityRegionId())
		{
			model.setCityRegionId(model.getCityRegionId());
		}
		else
		{
			model.setCityRegionId(null);
		}
		if(null != model.getProjectId())
		{
			model.setProjectId(model.getProjectId());
		}
		else
		{
			model.setProjectId(null);
		}
		
		if(null != model.getBuildingId())
		{
			model.setBuildingId(model.getBuildingId());
		}
		else
		{
			model.setBuildingId(null);
		}
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		
		Integer totalCount = tg_DepositHouseholdsDtl_ViewDao.findByPage_Size(tg_DepositHouseholdsDtl_ViewDao.getQuery_Size(tg_DepositHouseholdsDtl_ViewDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tg_DepositHouseholdsDtl_View> tg_DepositHouseholdsDtl_ViewList;
		if(totalCount > 0)
		{
			tg_DepositHouseholdsDtl_ViewList = tg_DepositHouseholdsDtl_ViewDao.findByPage(tg_DepositHouseholdsDtl_ViewDao.getQuery(tg_DepositHouseholdsDtl_ViewDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tg_DepositHouseholdsDtl_ViewList = new ArrayList<Tg_DepositHouseholdsDtl_View>();
		}
		
		properties.put("tg_DepositHouseholdsDtl_ViewList", tg_DepositHouseholdsDtl_ViewList);
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
