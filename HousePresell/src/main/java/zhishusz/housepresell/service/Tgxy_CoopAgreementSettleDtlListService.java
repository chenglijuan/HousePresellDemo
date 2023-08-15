package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleDtlForm;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDtlDao;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettleDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：三方协议结算-子表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgxy_CoopAgreementSettleDtlListService
{
	@Autowired
	private Tgxy_CoopAgreementSettleDtlDao tgxy_CoopAgreementSettleDtlDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_CoopAgreementSettleDtlForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = tgxy_CoopAgreementSettleDtlDao.findByPage_Size(tgxy_CoopAgreementSettleDtlDao.getQuery_Size(tgxy_CoopAgreementSettleDtlDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgxy_CoopAgreementSettleDtl> tgxy_CoopAgreementSettleDtlList;
		if(totalCount > 0)
		{
			tgxy_CoopAgreementSettleDtlList = tgxy_CoopAgreementSettleDtlDao.findByPage(tgxy_CoopAgreementSettleDtlDao.getQuery(tgxy_CoopAgreementSettleDtlDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tgxy_CoopAgreementSettleDtlList = new ArrayList<Tgxy_CoopAgreementSettleDtl>();
		}
		
		properties.put("tgxy_CoopAgreementSettleDtlList", tgxy_CoopAgreementSettleDtlList);
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
