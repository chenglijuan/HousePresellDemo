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

import zhishusz.housepresell.controller.form.Tg_BankLoanInSituation_ViewForm;
import zhishusz.housepresell.database.dao.Tg_BankLoanOutSituation_ViewListDao;
import zhishusz.housepresell.database.po.Tg_BankLoanOutSituation_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：银行放款项目出账情况表
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_BankLoanOutSituation_ViewListService
{
	@Autowired
	private Tg_BankLoanOutSituation_ViewListDao tg_BankLoanOutSituation_ViewListDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tg_BankLoanInSituation_ViewForm model)
	{
		Properties properties = new MyProperties();
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		// 获取查询条件
		String userid = String.valueOf(model.getUser().getTableId());// 用户主键
		String keyword = model.getKeyword();// 关键字
		String companyId = model.getCompanyId()==null?"":String.valueOf(model.getCompanyId());// 开发企业ID
		String projectId = model.getProjectId()==null?"":String.valueOf(model.getProjectId());// 项目ID
		
		String loanOutDate = model.getLoanOutDate().trim();
		String endLoanOutDate = model.getEndLoanOutDate().trim();
		
		String escrowAcount = model.getEscrowAcount();//托管账户

		List<Tg_BankLoanOutSituation_View> tg_BankLoanOutSituation_ViewList = new ArrayList<Tg_BankLoanOutSituation_View>();
		Map<String, Object> retmap = new HashMap<String, Object>();
		Integer totalPage = 0;
		Integer totalCount = 0;
		Double totalAmount = 0.00;
		// System.out.println("掉用存储过程开始：" + System.currentTimeMillis());
		try {
			retmap = tg_BankLoanOutSituation_ViewListDao.getBankLoanOutSituation_View(userid, companyId, projectId,
					keyword, loanOutDate, endLoanOutDate, pageNumber, countPerPage,escrowAcount);
			totalPage = (Integer) retmap.get("totalPage");
			totalCount = (Integer) retmap.get("totalCount");
			totalAmount = (Double) retmap.get("totalAmount");
			tg_BankLoanOutSituation_ViewList = (List<Tg_BankLoanOutSituation_View>) retmap.get("tg_BankLoanOutSituation_ViewList");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		

		properties.put("tg_BankLoanOutSituation_ViewList", tg_BankLoanOutSituation_ViewList);

		properties.put("keyword", keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		properties.put("totalAmount", totalAmount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
