package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Empj_BuildingExtendInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingExtendInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：楼幢-扩展信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_BuildingExtendInfoListService
{
	@Autowired
	private Empj_BuildingExtendInfoDao empj_BuildingExtendInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_BuildingExtendInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = empj_BuildingExtendInfoDao.findByPage_Size(empj_BuildingExtendInfoDao.getQuery_Size(empj_BuildingExtendInfoDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Empj_BuildingExtendInfo> empj_BuildingExtendInfoList;
		if(totalCount > 0)
		{
			empj_BuildingExtendInfoList = empj_BuildingExtendInfoDao.findByPage(empj_BuildingExtendInfoDao.getQuery(empj_BuildingExtendInfoDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			empj_BuildingExtendInfoList = new ArrayList<Empj_BuildingExtendInfo>();
		}
		
		properties.put("empj_BuildingExtendInfoList", empj_BuildingExtendInfoList);
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
