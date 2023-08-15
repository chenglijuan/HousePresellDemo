package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_UnitInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：楼幢-单元
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_UnitInfoBuildingListService
{
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_BuildingInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
				
		String keyword = model.getKeyword();
		
		if (keyword != null && !"".equals(keyword)) 
		{
			model.setKeyword("%"+keyword+"%");
		}
		else
		{
			model.setKeyword(null);
		}
		
		Integer totalCount = empj_BuildingInfoDao.findByPage_Size(empj_BuildingInfoDao.getQuery_Size(empj_BuildingInfoDao.getUnitHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Empj_BuildingInfo> empj_BuildingInfoList;
		if(totalCount > 0)
		{
			empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getUnitHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			empj_BuildingInfoList = new ArrayList<Empj_BuildingInfo>();
		}
		
		properties.put("empj_BuildingInfoList", empj_BuildingInfoList);
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
