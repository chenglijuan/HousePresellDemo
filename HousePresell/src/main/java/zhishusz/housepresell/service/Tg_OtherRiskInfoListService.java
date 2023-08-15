package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Tg_OtherRiskInfoForm;
import zhishusz.housepresell.database.dao.Tg_OtherRiskInfoDao;
import zhishusz.housepresell.database.po.Tg_OtherRiskInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：其他风险信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tg_OtherRiskInfoListService
{
	@Autowired
	private Tg_OtherRiskInfoDao tg_OtherRiskInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tg_OtherRiskInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		String riskInputDate =  model.getRiskInputDate();
		
		if (null != keyword && !keyword.trim().isEmpty())
		{
			model.setKeyword("%"+keyword.trim()+"%");
		}else{
			model.setKeyword(null);
		}
		
		if (null == riskInputDate || riskInputDate.trim().isEmpty())
		{
			model.setRiskInputDate(null);
		}
		
		model.setTheState(S_TheState.Normal);
		
		Integer totalCount = tg_OtherRiskInfoDao.findByPage_Size(tg_OtherRiskInfoDao.getQuery_Size(tg_OtherRiskInfoDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tg_OtherRiskInfo> tg_OtherRiskInfoList;
		if(totalCount > 0)
		{
			tg_OtherRiskInfoList = tg_OtherRiskInfoDao.findByPage(tg_OtherRiskInfoDao.getQuery(tg_OtherRiskInfoDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tg_OtherRiskInfoList = new ArrayList<Tg_OtherRiskInfo>();
		}
		
		properties.put("tg_OtherRiskInfoList", tg_OtherRiskInfoList);
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
