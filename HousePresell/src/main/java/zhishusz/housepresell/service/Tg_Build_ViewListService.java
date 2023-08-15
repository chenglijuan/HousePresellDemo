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

import zhishusz.housepresell.controller.form.Tg_Build_ViewForm;
import zhishusz.housepresell.database.dao.Tg_Build_ViewListDao;
import zhishusz.housepresell.database.po.Tg_Build_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：托管楼幢详细
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_Build_ViewListService {
	@Autowired
	private Tg_Build_ViewListDao tg_Build_ViewListDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_Build_ViewForm model) {
		Properties properties = new MyProperties();
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		// 获取查询条件
		String keyword = model.getKeyword();// 关键字

		/*
		 * 关键字搜索条件 单元信息、房间号、合同编号、三方协议号
		 */
		String userid = String.valueOf(model.getUser().getTableId());// 用户主键
		String companyId = String.valueOf(model.getCompanyId());// 开发企业ID
		String projectId = String.valueOf(model.getProjectId());// 项目ID
		String buildId = String.valueOf(model.getBuildId());// 楼幢ID

		String startBillTimeStamp = model.getBillTimeStamp().trim();// 记账日期 （起始）
		String endBillTimeStamp = model.getEndBillTimeStamp().trim();// 记账日期 （结束）

		String paymentMethod = String.valueOf(model.getPaymentMethod());// 付款方式
		String escrowState = String.valueOf(model.getEscrowState());// 托管状态 0 未托管 1 已托管 2 申请托管终止 3 托管终止

		List<Tg_Build_View> tg_Build_ViewList = new ArrayList<Tg_Build_View>();

		Map<String, Object> retmap = new HashMap<String, Object>();
		Integer totalPage = 0;
		Integer totalCount = 0;
		try {
			retmap = tg_Build_ViewListDao.getBuild_View(userid, companyId, projectId, buildId, keyword, paymentMethod,
					escrowState, startBillTimeStamp, endBillTimeStamp, pageNumber, countPerPage);
			totalPage = (Integer) retmap.get("totalPage");
			totalCount = (Integer) retmap.get("totalCount");
			tg_Build_ViewList = (List<Tg_Build_View>) retmap.get("tg_Build_ViewList");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		properties.put("tg_Build_ViewList", tg_Build_ViewList);
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
