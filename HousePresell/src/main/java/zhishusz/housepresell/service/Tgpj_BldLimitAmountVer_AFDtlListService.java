package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFDtlForm;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDtlDao;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：受限额度设置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpj_BldLimitAmountVer_AFDtlListService
{
	@Autowired
	private Tgpj_BldLimitAmountVer_AFDtlDao tgpj_BldLimitAmountVer_AFDtlDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpj_BldLimitAmountVer_AFDtlForm model)
	{
		model.setOrderBy("limitedAmount desc");
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = tgpj_BldLimitAmountVer_AFDtlDao.findByPage_Size(tgpj_BldLimitAmountVer_AFDtlDao.getQuery_Size(tgpj_BldLimitAmountVer_AFDtlDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgpj_BldLimitAmountVer_AFDtl> tgpj_BldLimitAmountVer_AFDtlList;
		if(totalCount > 0)
		{
			tgpj_BldLimitAmountVer_AFDtlList = tgpj_BldLimitAmountVer_AFDtlDao.findByPage(tgpj_BldLimitAmountVer_AFDtlDao.getQuery(tgpj_BldLimitAmountVer_AFDtlDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tgpj_BldLimitAmountVer_AFDtlList = new ArrayList<Tgpj_BldLimitAmountVer_AFDtl>();
		}
		
		properties.put("tgpj_BldLimitAmountVer_AFDtlList", tgpj_BldLimitAmountVer_AFDtlList);
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
