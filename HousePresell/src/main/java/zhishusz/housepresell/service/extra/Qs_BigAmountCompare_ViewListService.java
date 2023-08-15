package zhishusz.housepresell.service.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.extra.Qs_BigAmountCompare_ViewForm;
import zhishusz.housepresell.database.dao.extra.Qs_BigAmountCompare_ViewDao;
import zhishusz.housepresell.database.po.extra.Qs_BigAmountCompare_View;
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
public class Qs_BigAmountCompare_ViewListService
{
	@Autowired
	private Qs_BigAmountCompare_ViewDao qs_BigAmountCompare_ViewDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Qs_BigAmountCompare_ViewForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		// 关键字
		String keyword = model.getKeyword();

		// 存入日期
		String depositDateStart = model.getDepositDateStart();
		String depositDateEnd = model.getDepositDateEnd();
		// 到期日期
		String dueDateStart = model.getDueDateStart();
		String dueDateEnd = model.getDueDateEnd();
		// 银行名称（开户行）
		String theNameOfDepositBank = model.getTheNameOfDepositBank();

		if (null != depositDateStart && depositDateStart.trim().isEmpty())
		{
			model.setDepositDateStart(null);
		}
		if (null != depositDateEnd && depositDateEnd.trim().isEmpty())
		{
			model.setDepositDateEnd(null);
		}
		if (null != dueDateStart && dueDateStart.trim().isEmpty())
		{
			model.setDueDateStart(null);
		}
		if (null != dueDateEnd && dueDateEnd.trim().isEmpty())
		{
			model.setDueDateEnd(null);
		}
		if (null != theNameOfDepositBank && theNameOfDepositBank.trim().isEmpty())
		{
			model.setTheNameOfDepositBank(null);
		}

		if (keyword != null)
		{
			model.setKeyword("%" + keyword + "%");
		}

		Integer totalCount = qs_BigAmountCompare_ViewDao.findByPage_Size(
				qs_BigAmountCompare_ViewDao.getQuery_Size(qs_BigAmountCompare_ViewDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Qs_BigAmountCompare_View> qs_BigAmountCompare_ViewList;
		if (totalCount > 0)
		{
			qs_BigAmountCompare_ViewList = qs_BigAmountCompare_ViewDao.findByPage(
					qs_BigAmountCompare_ViewDao.getQuery(qs_BigAmountCompare_ViewDao.getBasicHQL(), model), pageNumber,
					countPerPage);

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

			Qs_BigAmountCompare_View qb = new Qs_BigAmountCompare_View();
			qb.setTheNameOfDepositBank("合计");
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

			for (Qs_BigAmountCompare_View qv : qs_BigAmountCompare_ViewList)
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
			
			//合计信息入列表
			qs_BigAmountCompare_ViewList.add(qb);

		}
		else
		{
			qs_BigAmountCompare_ViewList = new ArrayList<Qs_BigAmountCompare_View>();
		}

		properties.put("qs_BigAmountCompare_ViewList", qs_BigAmountCompare_ViewList);
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
