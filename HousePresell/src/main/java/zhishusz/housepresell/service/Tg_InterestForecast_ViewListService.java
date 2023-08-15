package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_InterestForecast_ViewForm;
import zhishusz.housepresell.database.dao.Tg_InterestForecast_ViewDao;
import zhishusz.housepresell.database.po.Tg_InterestForecast_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * service 列表查询：利息预测表
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tg_InterestForecast_ViewListService
{

	@Autowired
	private Tg_InterestForecast_ViewDao tg_InterestForecast_ViewDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_InterestForecast_ViewForm model)
	{
		Properties properties = new MyProperties();
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		String startDate = model.getLoanInDate();// 存单存入时间
		String stopDate = model.getEndLoanInDate();// 存单到期时间

		// 获取查询条件
		String keyword = model.getKeyword();// 关键字

		if (null == keyword || keyword.length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%" + keyword + "%");
		}

		if (null == startDate || startDate.trim().length() == 0)
		{
			model.setStartDate(null);
		}
		else
		{
			model.setStartDate(startDate.trim());
		}
		
		if (null == stopDate || stopDate.trim().length() == 0)
		{
			model.setStopDate(null);
		}
		else
		{
			model.setStopDate(stopDate);
		}

		Integer totalCount = tg_InterestForecast_ViewDao.findByPage_Size(
				tg_InterestForecast_ViewDao.getQuery_Size(tg_InterestForecast_ViewDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Tg_InterestForecast_View> tg_InterestForecast_ViewList = null;
		if (totalCount > 0)
		{
			tg_InterestForecast_ViewList = tg_InterestForecast_ViewDao.findByPage(
					tg_InterestForecast_ViewDao.getQuery(tg_InterestForecast_ViewDao.getBasicHQL(), model), pageNumber,
					countPerPage);
		}
		else
		{
			tg_InterestForecast_ViewList = new ArrayList<Tg_InterestForecast_View>();
		}

		properties.put("tg_InterestForecast_ViewList", tg_InterestForecast_ViewList);
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
