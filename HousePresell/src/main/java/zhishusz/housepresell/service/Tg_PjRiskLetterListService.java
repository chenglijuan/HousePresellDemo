package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Tg_PjRiskLetterForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskLetterDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Tg_PjRiskLetter;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：项目风险函
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tg_PjRiskLetterListService
{
	@Autowired
	private Tg_PjRiskLetterDao tg_PjRiskLetterDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tg_PjRiskLetterForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		String letterDateBegin = model.getLetterDateBegin();//风险评估日期-开始
		String letterDateEnd = model.getLetterDateEnd();//风险评估日期-结束
		Long developCompanyId = model.getDevelopCompanyId();//开发企业Id
		Long projectId = model.getProjectId();//获取项目Id
		Long cityRegionId = model.getCityRegionId();//获取区域Id
		
		if (null != keyword && !keyword.trim().isEmpty())
		{
			model.setKeyword("%"+keyword+"%");
		}else{
			model.setKeyword(null);
		}
		
		if (null == letterDateBegin || letterDateBegin.trim().isEmpty())
		{
			model.setLetterDateBegin(null);
		}
		
		if (null == letterDateEnd || letterDateEnd.trim().isEmpty())
		{
			model.setLetterDateEnd(null);
		}
		
		
		
		if (null != developCompanyId)
		{
			//查询开发企业
			Emmp_CompanyInfo emmp_CompanyInfo = emmp_CompanyInfoDao.findById(developCompanyId);
			if(emmp_CompanyInfo == null){
				return MyBackInfo.fail(properties, "为查询到有效的开发企业数据！");
			}else{
				model.setDevelopCompany(emmp_CompanyInfo);
			}
		}
		
		if (null != projectId)
		{
			//查询预售项目
			Empj_ProjectInfo empj_ProjectInfo = empj_ProjectInfoDao.findById(projectId);
			if(empj_ProjectInfo == null){
				return MyBackInfo.fail(properties, "为查询到有效的项目数据！");
			}else{
				model.setProject(empj_ProjectInfo);
			}
		}
		
		if (null != cityRegionId)
		{
			//查询所属区域
			Sm_CityRegionInfo sm_CityRegionInfo = sm_CityRegionInfoDao.findById(cityRegionId);
			if(sm_CityRegionInfo == null){
				return MyBackInfo.fail(properties, "为查询到有效的区域！");
			}else{
				model.setCityRegion(sm_CityRegionInfo);
			}
		}
		
		model.setTheState(S_TheState.Normal);
		
		Integer totalCount = tg_PjRiskLetterDao.findByPage_Size(tg_PjRiskLetterDao.getQuery_Size(tg_PjRiskLetterDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tg_PjRiskLetter> tg_PjRiskLetterList;
		if(totalCount > 0)
		{
			tg_PjRiskLetterList = tg_PjRiskLetterDao.findByPage(tg_PjRiskLetterDao.getQuery(tg_PjRiskLetterDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tg_PjRiskLetterList = new ArrayList<Tg_PjRiskLetter>();
		}
		
		properties.put("tg_PjRiskLetterList", tg_PjRiskLetterList);
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
