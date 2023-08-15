package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_projectDetailOfBrowse_ViewForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Tg_projectDetailOfBrowse_ViewDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Tg_projectDetailOfBrowse_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：托管项目详情一览表
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_ProjectDetailOfBrowse_ViewListService
{

	@Autowired
	private Tg_projectDetailOfBrowse_ViewDao tg_projectDetailOfBrowse_ViewDao;

	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;

	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_projectDetailOfBrowse_ViewForm model)
	{
		Properties properties = new MyProperties();
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		// 获取查询条件
		String keyword = model.getKeyword();// 关键字

		String querdate = model.getQueryDate();// 查询时间

		Long projectId = model.getProjectId();// 项目Id

		Long cityRegionId = model.getCityRegionId();// 区域Id

		if (null == keyword || keyword.length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%" + keyword + "%");
		}

		if (null == projectId || projectId < 1)
		{
			model.setProjectName(null);
		}
		else
		{
			// 查询项目信息
			Empj_ProjectInfo empj_ProjectInfo = empj_ProjectInfoDao.findById(projectId);
			if (null == empj_ProjectInfo)
			{
				return MyBackInfo.fail(properties, "未查询到有效的项目信息");
			}

			model.setProjectName(empj_ProjectInfo.getTheName());
		}

		if (null == cityRegionId || cityRegionId < 1)
		{
			model.setCityRegion(null);
		}
		else
		{
			// 查询区域信息
			Sm_CityRegionInfo cityRegionInfo = sm_CityRegionInfoDao.findById(cityRegionId);
			if (null == cityRegionInfo)
			{
				return MyBackInfo.fail(properties, "未查询到有效的区域信息");
			}

			model.setCityRegion(cityRegionInfo.getTheName());
		}

		String year = null;// 当前时间年份

		String month = null;// 当前时间月份

		if (null == querdate || querdate.length() < 1)
		{
			// 获取当前日期
			Long currentTimer = System.currentTimeMillis();

			String dateToSimpleString = MyDatetime.getInstance().dateToSimpleString(currentTimer);

			year = dateToSimpleString.split("-")[0];// 获取当前时间年份

			month = dateToSimpleString.split("-")[1];// 获取当前时间月份
		}
		else
		{
			year = querdate.split("-")[0];// 获取当前时间年份

			month = querdate.split("-")[1];// 获取当前时间月份
		}

		model.setQueryYear(year);

		model.setQueryMonth(month);

		Integer totalCount = tg_projectDetailOfBrowse_ViewDao.findByPage_Size(
				tg_projectDetailOfBrowse_ViewDao.getQuery_Size(tg_projectDetailOfBrowse_ViewDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Tg_projectDetailOfBrowse_View> tg_projectDetailOfBrowse_ViewList = null;
		if (totalCount > 0)
		{
			tg_projectDetailOfBrowse_ViewList = tg_projectDetailOfBrowse_ViewDao.findByPage(
					tg_projectDetailOfBrowse_ViewDao.getQuery(tg_projectDetailOfBrowse_ViewDao.getBasicHQL(), model),
					pageNumber, countPerPage);
		}
		else
		{
			tg_projectDetailOfBrowse_ViewList = new ArrayList<Tg_projectDetailOfBrowse_View>();
		}

		properties.put("tg_projectDetailOfBrowse_ViewList", tg_projectDetailOfBrowse_ViewList);

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
