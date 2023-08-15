package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementForm;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementDao;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：合作协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tgxy_CoopAgreementListService
{
	@Autowired
	private Tgxy_CoopAgreementDao tgxy_CoopAgreementDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_CoopAgreementForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
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

		Integer totalCount = tgxy_CoopAgreementDao
				.findByPage_Size(tgxy_CoopAgreementDao.getQuery_Size(tgxy_CoopAgreementDao.getBasicHQL2(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Tgxy_CoopAgreement> tgxy_CoopAgreementList;
		if (totalCount > 0)
		{
			tgxy_CoopAgreementList = tgxy_CoopAgreementDao.findByPage(
					tgxy_CoopAgreementDao.getQuery(tgxy_CoopAgreementDao.getBasicHQL2(), model), pageNumber,
					countPerPage);
		}
		else
		{
			tgxy_CoopAgreementList = new ArrayList<Tgxy_CoopAgreement>();
		}

		properties.put("tgxy_CoopAgreementList", tgxy_CoopAgreementList);
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
