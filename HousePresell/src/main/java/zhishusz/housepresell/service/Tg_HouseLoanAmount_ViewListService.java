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

import zhishusz.housepresell.controller.form.Tg_HouseLoanAmount_ViewForm;
import zhishusz.housepresell.database.dao.Tg_HouseLoanAmount_ViewListDao;
import zhishusz.housepresell.database.po.Tg_HouseLoanAmount_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：户入账详细
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_HouseLoanAmount_ViewListService
{
	@Autowired
	private Tg_HouseLoanAmount_ViewListDao tg_HouseLoanAmount_ViewListDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_HouseLoanAmount_ViewForm model)
	{
		Properties properties = new MyProperties();
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		// 获取查询条件
		String usedId =String.valueOf(model.getUserId());//用户主键
		String keyword = model.getKeyword();// 关键字
		String companyId = model.getCompanyId()==null?"":String.valueOf(model.getCompanyId());// 开发企业ID
		String projectId = model.getProjectId()==null?"":String.valueOf(model.getProjectId());// 项目ID
		String buildId = model.getBuildId()==null?"":String.valueOf(model.getBuildId());// 楼幢ID

		String startBillTimeStamp = model.getBillTimeStamp().trim();// 记账日期 （起始）
		String endBillTimeStamp = model.getEndBillTimeStamp().trim();// 记账日期 （结束）
		

		List<Tg_HouseLoanAmount_View> tg_HouseLoanAmount_ViewList = new ArrayList<Tg_HouseLoanAmount_View>();
		Integer totalPage = 0;
		Integer totalCount = 0;

		Map<String, Object> retmap = new HashMap<String, Object>();
		try {
			retmap = tg_HouseLoanAmount_ViewListDao.getHouseLoanAmount_View(usedId,companyId, projectId, buildId, keyword,
					startBillTimeStamp, endBillTimeStamp,pageNumber, countPerPage);
			totalPage = (Integer) retmap.get("totalPage");
			totalCount = (Integer) retmap.get("totalCount");
			tg_HouseLoanAmount_ViewList = (List<Tg_HouseLoanAmount_View>) retmap.get("tg_HouseLoanAmount_ViewList");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		properties.put("tg_HouseLoanAmount_ViewList", tg_HouseLoanAmount_ViewList);
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
