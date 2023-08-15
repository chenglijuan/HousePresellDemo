package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgxy_DepositAgreementForm;
import zhishusz.housepresell.database.dao.Tgxy_DepositAgreementDao;
import zhishusz.housepresell.database.po.Tgxy_DepositAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：协定存款协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tgxy_DepositAgreementListService
{
	@Autowired
	private Tgxy_DepositAgreementDao tgxy_DepositAgreementDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_DepositAgreementForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		// 查询关键字
		String keyword = model.getKeyword();

		// 在keyword补充like查询%%，方便模糊查询
		// 设置之前进行判空，预防%null% 出现
		if (null != keyword)
		{
			model.setKeyword("%" + keyword + "%");
		}

		if (null != model.getSignDate() && model.getSignDate().isEmpty())
		{
			model.setSignDate(null);
		}

		// 设置查询为删除状态
		model.setTheState(S_TheState.Normal);

		Integer totalCount = tgxy_DepositAgreementDao.findByPage_Size(
				tgxy_DepositAgreementDao.getQuery_Size(tgxy_DepositAgreementDao.getBasicHQL2(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Tgxy_DepositAgreement> tgxy_DepositAgreementList = new ArrayList<Tgxy_DepositAgreement>();
		if (totalCount > 0)
		{
			tgxy_DepositAgreementList = tgxy_DepositAgreementDao.findByPage(
					tgxy_DepositAgreementDao.getQuery(tgxy_DepositAgreementDao.getBasicHQL2(), model), pageNumber,
					countPerPage);
		}
		else
		{
			tgxy_DepositAgreementList = new ArrayList<Tgxy_DepositAgreement>();
		}

		properties.put("tgxy_DepositAgreementList", tgxy_DepositAgreementList);
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
