package zhishusz.housepresell.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_BuildPayout_ViewForm;
import zhishusz.housepresell.database.dao.Tg_BuildPayout_ViewListDao;
import zhishusz.housepresell.database.po.Tg_BuildPayout_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：托管楼幢拨付明细统计表报表
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_BuildPayout_ViewListService
{
	@Autowired
	private Tg_BuildPayout_ViewListDao tg_BuildPayout_ViewListDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_BuildPayout_ViewForm model)
	{
		Properties properties = new MyProperties();
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		// 获取查询条件
		String userid=String.valueOf(model.getUserId());
		String keyword = model.getKeyword();// 关键字
		String companyId = String.valueOf(model.getCompanyId());// 开发企业ID
		String projectId = String.valueOf(model.getProjectId());// 项目ID
		String buildId = String.valueOf(model.getBuildId());// 楼幢ID
		
		if (companyId=="null") {
			companyId=null;			
		}
		
		if (projectId=="null") {
			projectId=null;			
		}
		
		if (buildId=="null") {
			buildId=null;			
		}

		String payoutDate = model.getPayoutDate().trim();// 拨付日期 （起始）
		String endPayoutDate = model.getEndPayoutDate().trim();// 拨付日期 （结束）

		

		List<Tg_BuildPayout_View> tg_BuildPayout_ViewList = new ArrayList<Tg_BuildPayout_View>();
		Integer totalPage = 0;
		Integer totalCount = 0;

		if (!payoutDate.isEmpty() && !endPayoutDate.isEmpty()) {
			Map<String, Object> retmap = new HashMap<String, Object>();
			// System.out.println("掉用存储过程开始：" + System.currentTimeMillis());
			try {
				retmap = tg_BuildPayout_ViewListDao.getBuildPayout_View(userid,companyId, projectId, buildId, keyword,
						payoutDate, endPayoutDate, pageNumber, countPerPage);
				totalPage = (Integer) retmap.get("totalPage");
				totalCount = (Integer) retmap.get("totalCount");
				tg_BuildPayout_ViewList = (List<Tg_BuildPayout_View>) retmap.get("tg_BuildPayout_ViewList");
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		properties.put("tg_BuildPayout_ViewList", tg_BuildPayout_ViewList);

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
