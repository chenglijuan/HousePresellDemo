package zhishusz.housepresell.service.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.extra.Qs_RecordAmount_ViewForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.dao.extra.Qs_RecordAmount_ViewDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.extra.Qs_RecordAmount_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：入账金额核对表
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Qs_RecordAmount_ViewListService
{
	@Autowired
	private Qs_RecordAmount_ViewDao qs_RecordAmount_ViewDao;
	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao; // 开户行
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;// 托管账号

	@SuppressWarnings("unchecked")
	public Properties execute(Qs_RecordAmount_ViewForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		// 关键字
		String keyword = model.getKeyword();

		// 入账日期
		String recordDateStart = model.getRecordDateStart().trim();
		String recordDateEnd = model.getRecordDateEnd().trim();

		// 银行名称（开户行）
		String theNameOfBank = model.getTheNameOfBank();

		// 托管账户名称
		String theNameOfEscrow = model.getTheNameOfEscrow();

		if (null == recordDateStart || recordDateStart.trim().isEmpty())
		{
			model.setRecordDateStart(null);
		}
		else
		{
			model.setRecordDateStart(recordDateStart);
		}
		if (null == recordDateEnd || recordDateEnd.trim().isEmpty())
		{
			model.setRecordDateEnd(null);
		}
		else
		{
			model.setRecordDateEnd(recordDateEnd);
		}
		if (null == theNameOfBank || theNameOfBank.trim().isEmpty())
		{
			model.setTheNameOfBank(null);
		}
		else
		{
			Emmp_BankBranch branch = emmp_BankBranchDao.findById(Long.parseLong(theNameOfBank));

			model.setTheNameOfBank(branch.getTheName());
		}
		if (null != theNameOfEscrow && theNameOfEscrow.trim().isEmpty())
		{
			model.setTheNameOfEscrow(null);
		}
		else
		{
			Tgxy_BankAccountEscrowed escrowed = tgxy_BankAccountEscrowedDao.findById(Long.parseLong(theNameOfEscrow));

			model.setTheNameOfEscrow(escrowed.getTheName());
		}

		if (keyword != null && keyword.trim().length() >= 1)
		{
			model.setKeyword("%" + keyword + "%");
		}
		else
		{
			model.setKeyword(null);
		}

		Integer totalCount = qs_RecordAmount_ViewDao
				.findByPage_Size(qs_RecordAmount_ViewDao.getQuery_Size(qs_RecordAmount_ViewDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Qs_RecordAmount_View> qs_RecordAmount_ViewList;
		if (totalCount > 0)
		{
			qs_RecordAmount_ViewList = qs_RecordAmount_ViewDao.findByPage(
					qs_RecordAmount_ViewDao.getQuery(qs_RecordAmount_ViewDao.getBasicHQL(), model), pageNumber,
					countPerPage);
		}
		else
		{
			qs_RecordAmount_ViewList = new ArrayList<Qs_RecordAmount_View>();
		}

		properties.put("qs_RecordAmount_ViewList", qs_RecordAmount_ViewList);
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
