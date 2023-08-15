package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFForm;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDao;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：版本管理-受限节点设置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpj_BldLimitAmountVer_AFListService
{
	@Autowired
	private Tgpj_BldLimitAmountVer_AFDao tgpj_BldLimitAmountVer_AFDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpj_BldLimitAmountVer_AFForm model)
	{
//		model.setOrderBy("eCode asc");
		if(StringUtils.isEmpty(model.getOrderBy())){
			model.setOrderBy(null);
		}
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
//		if(null == keyword || keyword.trim().isEmpty()){
//			model.setKeyword(null);
//		}else{
//			model.setKeyword("%"+keyword+"%");
//		}
		
		Integer totalCount = tgpj_BldLimitAmountVer_AFDao.findByPage_Size(tgpj_BldLimitAmountVer_AFDao.createCriteriaForList(model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgpj_BldLimitAmountVer_AF> tgpj_BldLimitAmountVer_AFList;
		if(totalCount > 0)
		{
			tgpj_BldLimitAmountVer_AFList = tgpj_BldLimitAmountVer_AFDao.findByPage(tgpj_BldLimitAmountVer_AFDao.createCriteriaForList(model), pageNumber, countPerPage);
		}
		else
		{
			tgpj_BldLimitAmountVer_AFList = new ArrayList<Tgpj_BldLimitAmountVer_AF>();
		}
		
		properties.put("tgpj_BldLimitAmountVer_AFList", tgpj_BldLimitAmountVer_AFList);
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
