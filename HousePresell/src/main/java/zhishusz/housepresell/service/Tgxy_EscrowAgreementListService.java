package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgxy_EscrowAgreementForm;
import zhishusz.housepresell.database.dao.Tgxy_EscrowAgreementDao;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：托管合作协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tgxy_EscrowAgreementListService
{
	@Autowired
	private Tgxy_EscrowAgreementDao tgxy_EscrowAgreementDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_EscrowAgreementForm model)
	{
		Properties properties = new MyProperties();
		// 分页条件
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		// 关键字
		String keyword = model.getKeyword();
		// 设置关键字 拼接like条件
		if (null != keyword)
		{
			model.setKeyword("%" + keyword + "%");
		}
		String contractApplicationDate = model.getContractApplicationDate();
		if (null == contractApplicationDate || contractApplicationDate.trim().isEmpty())
		{
			model.setContractApplicationDate(null);
		}
		model.setTheState(S_TheState.Normal);
		
		String startDate = model.getStartDate();
		if(null == startDate || startDate.trim().isEmpty())
		{
			model.setStartDate(null);
		}
		String endDate = model.getEndDate();
		if(null == endDate || endDate.trim().isEmpty())
		{
			model.setEndDate(null);
		}

		String approvalState = model.getApprovalState();
		if("".equals(approvalState))
		{
			model.setApprovalState(null);
		}
		
		Integer totalCount = tgxy_EscrowAgreementDao
				.findByPage_Size(tgxy_EscrowAgreementDao.getQuery_Size(tgxy_EscrowAgreementDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Tgxy_EscrowAgreement> tgxy_EscrowAgreementList;
		if (totalCount > 0)
		{
			tgxy_EscrowAgreementList = tgxy_EscrowAgreementDao.findByPage(
					tgxy_EscrowAgreementDao.getQuery(tgxy_EscrowAgreementDao.getBasicHQL(), model), pageNumber,
					countPerPage);
		}
		else
		{
			tgxy_EscrowAgreementList = new ArrayList<Tgxy_EscrowAgreement>();
		}

		properties.put("tgxy_EscrowAgreementList", tgxy_EscrowAgreementList);
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
