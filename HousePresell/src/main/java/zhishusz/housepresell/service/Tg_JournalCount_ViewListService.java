package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.util.StrUtil;
import zhishusz.housepresell.controller.form.Tg_JournalCount_ViewForm;
import zhishusz.housepresell.database.dao.Tg_JournalCount_ViewListDao;
import zhishusz.housepresell.database.po.Tg_JournalCount_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：日记账统计
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_JournalCount_ViewListService
{
	@Autowired
	private Tg_JournalCount_ViewListDao tg_JournalCount_ViewListDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_JournalCount_ViewForm model)
	{
		Properties properties = new MyProperties();
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		// 获取查询条件
		String keyword = model.getKeyword();// 关键字

		String startloanInDate = model.getLoanInDate();// 入账日期 （起始）
		String endLoanInDate = model.getEndLoanInDate();// 入账日期 （结束）

		if (null == keyword || keyword.length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%" + keyword + "%");
		}
		if (null == startloanInDate || startloanInDate.length() == 0)
		{
			model.setLoanInDate(null);
		}
		else
		{
			model.setLoanInDate(startloanInDate.trim());
		}
		if (null == endLoanInDate || endLoanInDate.length() == 0)
		{
			model.setEndLoanInDate(null);
		}
		else
		{
			model.setEndLoanInDate(endLoanInDate.trim());
		}
		
		String escrowAcountNameBak = StrUtil.isBlank(model.getEscrowAcountName()) ? null : model.getEscrowAcountName().trim();
		model.setEscrowAcountName(escrowAcountNameBak);
		
		Integer totalCount = tg_JournalCount_ViewListDao.findByPage_Size(
				tg_JournalCount_ViewListDao.getQuery_Size(tg_JournalCount_ViewListDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Tg_JournalCount_View> tg_JournalCount_ViewList = null;
		if (totalCount > 0)
		{
			tg_JournalCount_ViewList = tg_JournalCount_ViewListDao.findByPage(
					tg_JournalCount_ViewListDao.getQuery(tg_JournalCount_ViewListDao.getBasicHQL(), model), pageNumber,
					countPerPage);
		}
		else
		{
			tg_JournalCount_ViewList = new ArrayList<Tg_JournalCount_View>();
		}

		Tg_JournalCount_View tg_JournalCount = new Tg_JournalCount_View();

		String loanInDate = "总计";// 入账日期
		String escrowAcountName = "";// 托管账户名称
		Integer tradeCount = 0;// 确认总笔数
		Double totalTradeAmount = 0.0;// 确认总金额
		Integer aToatlLoanAmoutCount = 0;// 公积金贷款总笔数
		Double aToatlLoanAmout = 0.0;// 公积金贷款总金额
		Integer bToatlLoanAmoutCount = 0;// 商业贷款总笔数
		Double bToatlLoanAmout = 0.0;// 商业贷款总金额
		Integer oToatlLoanAmoutCount = 0;// 自有资金总笔数
		Double oToatlLoanAmout = 0.0;// 自有资金总金额
		Integer aFristToatlLoanAmoutCount = 0;// 公积金首付款总笔数
		Double aFristToatlLoanAmout = 0.0;// 公积金首付款总金额
		Integer aToBusinessToatlLoanAmoutCount = 0;// 公转商贷款总笔数
		Double aToBusinessToatlLoanAmout = 0.0;// 公转商贷款总金额

		if (tg_JournalCount_ViewList.size() > 0)
		{
			for (Tg_JournalCount_View tg_JournalCount_View : tg_JournalCount_ViewList)
			{
				tradeCount = tradeCount + tg_JournalCount_View.getTradeCount();
				totalTradeAmount = MyDouble.getInstance().doubleAddDouble(totalTradeAmount,
						tg_JournalCount_View.getTotalTradeAmount());
				aToatlLoanAmoutCount = aToatlLoanAmoutCount + tg_JournalCount_View.getaToatlLoanAmoutCount();
				aToatlLoanAmout = MyDouble.getInstance().doubleAddDouble(aToatlLoanAmout,
						tg_JournalCount_View.getaToatlLoanAmout());
				bToatlLoanAmoutCount = bToatlLoanAmoutCount + tg_JournalCount_View.getbToatlLoanAmoutCount();
				bToatlLoanAmout = MyDouble.getInstance().doubleAddDouble(bToatlLoanAmout,
						tg_JournalCount_View.getbToatlLoanAmout());
				oToatlLoanAmoutCount = oToatlLoanAmoutCount + tg_JournalCount_View.getoToatlLoanAmoutCount();
				oToatlLoanAmout = MyDouble.getInstance().doubleAddDouble(oToatlLoanAmout,
						tg_JournalCount_View.getoToatlLoanAmout());
				aFristToatlLoanAmoutCount = aFristToatlLoanAmoutCount
						+ tg_JournalCount_View.getaFristToatlLoanAmoutCount();
				aFristToatlLoanAmout = MyDouble.getInstance().doubleAddDouble(aFristToatlLoanAmout,
						tg_JournalCount_View.getaFristToatlLoanAmout());
				aToBusinessToatlLoanAmoutCount = aToBusinessToatlLoanAmoutCount
						+ tg_JournalCount_View.getaToBusinessToatlLoanAmoutCount();
				aToBusinessToatlLoanAmout = MyDouble.getInstance().doubleAddDouble(aToBusinessToatlLoanAmout,
						tg_JournalCount_View.getaToBusinessToatlLoanAmout());
			}
			
			tg_JournalCount.setLoanInDate(loanInDate);
			tg_JournalCount.setEscrowAcountName(escrowAcountName);
			tg_JournalCount.setTradeCount(tradeCount);
			tg_JournalCount.setTotalTradeAmount(totalTradeAmount);
			tg_JournalCount.setaToatlLoanAmout(aToatlLoanAmout);
			tg_JournalCount.setaToatlLoanAmoutCount(aToatlLoanAmoutCount);
			tg_JournalCount.setbToatlLoanAmout(bToatlLoanAmout);
			tg_JournalCount.setbToatlLoanAmoutCount(bToatlLoanAmoutCount);
			tg_JournalCount.setoToatlLoanAmout(oToatlLoanAmout);
			tg_JournalCount.setoToatlLoanAmoutCount(oToatlLoanAmoutCount);
			tg_JournalCount.setaFristToatlLoanAmout(aFristToatlLoanAmout);
			tg_JournalCount.setaFristToatlLoanAmoutCount(aFristToatlLoanAmoutCount);
			tg_JournalCount.setaToBusinessToatlLoanAmout(aToBusinessToatlLoanAmout);
			tg_JournalCount.setaToBusinessToatlLoanAmoutCount(aToBusinessToatlLoanAmoutCount);

			tg_JournalCount_ViewList.add(tg_JournalCount);
		}
		

		properties.put("tg_JournalCount_ViewList", tg_JournalCount_ViewList);

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
