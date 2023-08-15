package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_ProInspectionSchedule_ViewForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Tg_ProInspectionSchedule_ViewDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Tg_ProInspectionSchedule_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：项目巡查预测计划表
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_ProInspectionSchedule_ViewListService
{

	@Autowired
	private Tg_ProInspectionSchedule_ViewDao tg_ProInspectionSchedule_ViewDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tg_ProInspectionSchedule_ViewForm model)
	{
		Properties properties = new MyProperties();
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		// 获取查询条件
		String keyword = model.getKeyword();// 关键字

		Long projectId = model.getProjectId();// 项目ID
		Long cityRegionId = model.getCityRegionId();//区域ID
		
		String progressOfUpdateTime = model.getProgressOfUpdateTime();//当前更新进度时间

		if (null == keyword || keyword.length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%" + keyword + "%");
		}
		
		if (null == progressOfUpdateTime || progressOfUpdateTime.length() == 0)
		{
			model.setProgressOfUpdateTime(null);
		}
		
		if (null == cityRegionId || cityRegionId == 0)
		{
			model.setCityRegion(null);
		}
		else
		{
			Sm_CityRegionInfo cityRegionInfo = sm_CityRegionInfoDao.findById(cityRegionId);
			model.setCityRegion(cityRegionInfo.getTheName());
		}
		
		if (null == projectId || projectId == 0)
		{
			model.setProjectName(null);
		}
		else
		{
			Empj_ProjectInfo empj_ProjectInfo = empj_ProjectInfoDao.findById(projectId);
			model.setProjectName(empj_ProjectInfo.getTheName());
		}

		Integer totalCount = tg_ProInspectionSchedule_ViewDao.findByPage_Size(
				tg_ProInspectionSchedule_ViewDao.getQuery_Size(tg_ProInspectionSchedule_ViewDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Tg_ProInspectionSchedule_View> tg_ProInspectionSchedule_ViewList = null;
		if (totalCount > 0)
		{
			tg_ProInspectionSchedule_ViewList = tg_ProInspectionSchedule_ViewDao.findByPage(
					tg_ProInspectionSchedule_ViewDao.getQuery(tg_ProInspectionSchedule_ViewDao.getBasicHQL(), model), pageNumber,
					countPerPage);
		}
		else
		{
			tg_ProInspectionSchedule_ViewList = new ArrayList<Tg_ProInspectionSchedule_View>();
		}

		properties.put("tg_ProInspectionSchedule_ViewList", tg_ProInspectionSchedule_ViewList);

		properties.put("keyword", keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
