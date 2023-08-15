package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：楼幢-户室
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_HouseInfoListService
{
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_HouseInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		
		model.setTheState(S_TheState.Normal);
		
		Integer totalCount = empj_HouseInfoDao.findByPage_Size(empj_HouseInfoDao.getQuery_Size(empj_HouseInfoDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Empj_HouseInfo> empj_HouseInfoList;
		if(totalCount > 0)
		{
			empj_HouseInfoList = empj_HouseInfoDao.findByPage(empj_HouseInfoDao.getQuery(empj_HouseInfoDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			empj_HouseInfoList = new ArrayList<Empj_HouseInfo>();
		}
		
		properties.put("empj_HouseInfoList", empj_HouseInfoList);
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
