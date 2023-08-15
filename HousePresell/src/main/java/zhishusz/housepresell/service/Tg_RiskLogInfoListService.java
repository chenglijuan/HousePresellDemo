package zhishusz.housepresell.service;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_RiskLogInfoForm;
import zhishusz.housepresell.database.dao.Tg_RiskLogInfoDao;
import zhishusz.housepresell.database.po.Tg_RiskLogInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：风险日志管理
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_RiskLogInfoListService
{
	@Autowired
	private Tg_RiskLogInfoDao tg_RiskLogInfoDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_RiskLogInfoForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		String logDate = model.getLogDate();
		model.setTheState(S_TheState.Normal);
		// 设置之前进行判空，预防%null% 出现
		if (null != keyword)
		{
			model.setKeyword("%" + keyword + "%");
		}

		if (null == logDate || logDate.trim().isEmpty())
		{
			model.setLogDate(null);
		}

		Integer totalCount = tg_RiskLogInfoDao
				.findByPage_Size(tg_RiskLogInfoDao.getQuery_Size(tg_RiskLogInfoDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Tg_RiskLogInfo> tg_RiskLogInfoList;
		if (totalCount > 0)
		{
			tg_RiskLogInfoList = tg_RiskLogInfoDao.findByPage(
					tg_RiskLogInfoDao.getQuery(tg_RiskLogInfoDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tg_RiskLogInfoList = new ArrayList<Tg_RiskLogInfo>();
		}

		properties.put("tg_RiskLogInfoList", tg_RiskLogInfoList);
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
