package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_DepositFloat_ViewForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.dao.Tg_DepositFloat_ViewDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Tg_DepositFloat_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * service 列表查询：托协定存款统计表
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tg_DepositFloat_ViewListService
{

	@Autowired
	private Tg_DepositFloat_ViewDao tg_DepositFloat_ViewDao;

	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_DepositFloat_ViewForm model)
	{
		Properties properties = new MyProperties();
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		// 获取查询条件
		String keyword = model.getKeyword();// 关键字

		Long bankBrenchId = model.getBankBrenchId();// 开户行Id

		String signDateStart = model.getSignDateStart();

		String signDateEnd = model.getSignDateEnd();

		String endExpirationDateStart = model.getEndExpirationDateStart();

		String endExpirationDateEnd = model.getEndExpirationDateEnd();

		if (null == keyword || keyword.trim().length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%" + keyword + "%");
		}

		if (null != bankBrenchId && keyword.length() == 0)
		{
			Emmp_BankBranch bankBranch = emmp_BankBranchDao.findById(bankBrenchId);

			if (null == bankBranch)
			{
				return MyBackInfo.fail(properties, "未查询到有效的开户行信息");
			}

			model.setBankName(bankBranch.getTheName());
		}

		if (null == signDateStart || signDateStart.trim().length() == 0)
		{
			model.setSignDateStart(null);
		}

		if (null == signDateEnd || signDateEnd.trim().length() == 0)
		{
			model.setSignDateEnd(null);
		}
		
		if (null == endExpirationDateStart || endExpirationDateStart.trim().length() == 0)
		{
			model.setEndExpirationDateStart(null);
		}

		if (null == endExpirationDateEnd || endExpirationDateEnd.trim().length() == 0)
		{
			model.setEndExpirationDateEnd(null);
		}

		Integer totalCount = tg_DepositFloat_ViewDao
				.findByPage_Size(tg_DepositFloat_ViewDao.getQuery_Size(tg_DepositFloat_ViewDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Tg_DepositFloat_View> tg_DepositFloat_ViewList = null;
		if (totalCount > 0)
		{
			tg_DepositFloat_ViewList = tg_DepositFloat_ViewDao.findByPage(
					tg_DepositFloat_ViewDao.getQuery(tg_DepositFloat_ViewDao.getBasicHQL(), model), pageNumber,
					countPerPage);
		}
		else
		{
			tg_DepositFloat_ViewList = new ArrayList<Tg_DepositFloat_View>();
		}

		properties.put("tg_DepositFloat_ViewList", tg_DepositFloat_ViewList);
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
