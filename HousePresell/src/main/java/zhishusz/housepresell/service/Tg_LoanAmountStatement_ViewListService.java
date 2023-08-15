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

import zhishusz.housepresell.controller.form.Tg_LoanAmountStatement_ViewForm;
import zhishusz.housepresell.database.dao.Tg_LoanAmountStatement_ViewListDao;
import zhishusz.housepresell.database.po.Tg_LoanAmountStatement_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：托管现金流量表
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_LoanAmountStatement_ViewListService
{
	@Autowired
	private Tg_LoanAmountStatement_ViewListDao tg_LoanAmountStatement_ViewListDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_LoanAmountStatement_ViewForm model)
	{
		Properties properties = new MyProperties();
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		// 获取查询条件
		String keyword = model.getKeyword();// 关键字
		String startBillTimeStamp = model.getBillTimeStamp().trim();// 记账日期 （起始）
		String endBillTimeStamp = model.getEndBillTimeStamp().trim();// 记账日期
																		// （结束）

		String queryKind = model.getQueryKind();

		if (null == queryKind || queryKind.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "查询类别为空");
		}

		List<Tg_LoanAmountStatement_View> tg_LoanAmountStatement_ViewList = new ArrayList<Tg_LoanAmountStatement_View>();
		Integer totalPage = 0;
		Integer totalCount = 0;
		
		double amountIn = 0.00;
		double amountOut = 0.00;

		if (!startBillTimeStamp.isEmpty() && !endBillTimeStamp.isEmpty())
		{
			Map<String, Object> retmap = new HashMap<String, Object>();
			// System.out.println("掉用存储过程开始：" + System.currentTimeMillis());
			try
			{
				retmap = tg_LoanAmountStatement_ViewListDao.getLoanAmountStatement_View(keyword, startBillTimeStamp,
						endBillTimeStamp, pageNumber, countPerPage, queryKind);
				totalPage = (Integer) retmap.get("totalPage");
				totalCount = (Integer) retmap.get("totalCount");
				
				amountIn = (double) retmap.get("amountIn");
				amountOut = (double) retmap.get("amountOut");
				tg_LoanAmountStatement_ViewList = (List<Tg_LoanAmountStatement_View>) retmap
						.get("qs_ProjectEscrowAmount_ViewLists");
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}

		}

		properties.put("tg_LoanAmountStatement_ViewList", tg_LoanAmountStatement_ViewList);

		properties.put("keyword", keyword);
		properties.put("amountIn", amountIn);
		properties.put("amountOut", amountOut);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
