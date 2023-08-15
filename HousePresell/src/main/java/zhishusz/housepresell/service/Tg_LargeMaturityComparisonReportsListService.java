package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_LargeMaturityComparisonReportsForm;
import zhishusz.housepresell.database.dao.Tg_LargeMaturityComparisonReportsDao;
import zhishusz.housepresell.database.po.Tg_BigAmountCompare_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：大额到期对比表
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tg_LargeMaturityComparisonReportsListService
{
	@Autowired
	private Tg_LargeMaturityComparisonReportsDao tg_LargeMaturityComparisonReportsDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_LargeMaturityComparisonReportsForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		// 关键字
		String keyword = model.getKeyword();

		// 存入日期
		String depositDateStart = model.getDepositDateStart().trim();
		String depositDateEnd = model.getDepositDateEnd().trim();
		// 到期日期
		String dueDateStart = model.getDueDateStart().trim();
		String dueDateEnd = model.getDueDateEnd().trim();
		// 银行名称（开户行）
		String theNameOfDepositBank = model.getTheNameOfDepositBank();

		String depositMethod = model.getDepositMethod();// 存款性质

		if (null != depositDateStart && depositDateStart.trim().isEmpty())
		{
			model.setDepositDateStart(null);
		}
		else
		{
			model.setDepositDateStart(depositDateStart);
		}
		if (null == depositMethod || depositMethod.trim().isEmpty())
		{
			model.setDepositMethod(null);
		}
		if (null != depositDateEnd && depositDateEnd.trim().isEmpty())
		{
			model.setDepositDateEnd(null);
		}
		else
		{
			model.setDepositDateEnd(depositDateEnd);
		}
		if (null != dueDateStart && dueDateStart.trim().isEmpty())
		{
			model.setDueDateStart(null);
		}
		else
		{
			model.setDueDateStart(dueDateStart);
		}
		if (null != dueDateEnd && dueDateEnd.trim().isEmpty())
		{
			model.setDueDateEnd(null);
		}
		else
		{
			model.setDueDateEnd(dueDateEnd);
		}
		if (null != theNameOfDepositBank && theNameOfDepositBank.trim().isEmpty())
		{
			model.setTheNameOfDepositBank(null);
		}

		if (keyword != "")
		{
			model.setKeyword("%" + keyword + "%");
		}
		else
		{
			model.setKeyword(null);
		}

		Integer totalCount = tg_LargeMaturityComparisonReportsDao.findByPage_Size(tg_LargeMaturityComparisonReportsDao
				.getQuery_Size(tg_LargeMaturityComparisonReportsDao.getBasicHQLbylike(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Tg_BigAmountCompare_View> tg_LargeMaturityComparisonReports;
		if (totalCount > 0)
		{
			tg_LargeMaturityComparisonReports = tg_LargeMaturityComparisonReportsDao
					.findByPage(
							tg_LargeMaturityComparisonReportsDao
									.getQuery(tg_LargeMaturityComparisonReportsDao.getBasicHQLbylike(), model),
							pageNumber, countPerPage);

			/*
			 * xsz by time 2018-9-6 13:43:49
			 * 合计统计：
			 * ===》需要合计的字段：
			 * 存款金额（depositAmount）、
			 * 利息（expectInterest）、
			 * 实际到期利息（realInterest）、
			 * 差异（compareDifference）
			 * ===》不需要合计的字段：
			 * 银行名称（theNameOfDepositBank）、
			 * 存入时间（depositDate）、
			 * 到期时间（dueDate）、
			 * 存款期限（depositTimeLimit）、
			 * 利率（interestRate）
			 */

			Tg_BigAmountCompare_View qb = new Tg_BigAmountCompare_View();
			qb.setTheNameOfBank("合计");
			qb.setTheNameOfDepositBank(null);
			qb.setDepositDate(null);
			qb.setDueDate(null);
			qb.setDepositTimeLimit("-");
			qb.setInterestRate(null);

			// 设置初始值
			qb.setDepositAmount(0.00);
			qb.setExpectInterest(0.00);
			qb.setRealInterest(0.00);
			qb.setCompareDifference(0.00);

			/*
			 * double类型计算
			 * 
			 * doubleAddDouble 加
			 * doubleSubtractDouble 减
			 * doubleMultiplyDouble 乘
			 * div 除
			 * getShort() 保留小数位
			 */
			MyDouble dplan = MyDouble.getInstance();

			for (Tg_BigAmountCompare_View qv : tg_LargeMaturityComparisonReports)
			{
				// 存款金额
				qb.setDepositAmount(dplan.doubleAddDouble(qb.getDepositAmount(), qv.getDepositAmount()));
				// 利息
				qb.setExpectInterest(dplan.doubleAddDouble(qb.getExpectInterest(), qv.getExpectInterest()));
				// 实际到期利息
				qb.setRealInterest(dplan.doubleAddDouble(qb.getRealInterest(), qv.getRealInterest()));
				// 差异
				qb.setCompareDifference(dplan.doubleAddDouble(qb.getCompareDifference(), qv.getCompareDifference()));
			}

			// 合计信息入列表
			tg_LargeMaturityComparisonReports.add(qb);

		}
		else
		{
			tg_LargeMaturityComparisonReports = new ArrayList<Tg_BigAmountCompare_View>();
		}

		properties.put("tg_BigAmountCompare_ViewList", tg_LargeMaturityComparisonReports);
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
