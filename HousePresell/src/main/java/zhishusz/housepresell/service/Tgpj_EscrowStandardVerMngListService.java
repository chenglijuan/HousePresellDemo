package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import zhishusz.housepresell.database.po.state.S_TheState;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Tgpj_EscrowStandardVerMngForm;
import zhishusz.housepresell.database.dao.Tgpj_EscrowStandardVerMngDao;
import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：版本管理-托管标准
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpj_EscrowStandardVerMngListService
{
	@Autowired
	private Tgpj_EscrowStandardVerMngDao tgpj_EscrowStandardVerMngDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpj_EscrowStandardVerMngForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		String busiState = model.getBusiState();
		String theType = model.getTheType();
		String enableState = model.getEnableState();
		if (keyword != null && !"".equals(keyword))
		{
			model.setKeyword("%"+keyword+"%");
		}
		else
		{
			model.setKeyword(null);
		}

		if ("".equals(busiState) || "0".equals(busiState) || "全部".equals(busiState))
		{
			model.setBusiState(null);
		}

//		if ("1".equals(enableState))
//		{
//			model.setHasEnable(true);
//		}
//		else if("2".equals(enableState))
//		{
//			model.setHasEnable(false);
//		}
//		else
//		{
//			model.setHasEnable(null);
//		}

		if ("".equals(theType) || "全部".equals(theType))
		{
			model.setTheType(null);
		}

		if(StringUtils.isEmpty(model.getOrderBy()))
		{
			model.setOrderBy(null);
		}

		model.setTheState(S_TheState.Normal);
		//托管标准注意了：重新设置值

		Integer totalCount = tgpj_EscrowStandardVerMngDao.findByPage_Size(tgpj_EscrowStandardVerMngDao.getQuery_Size(tgpj_EscrowStandardVerMngDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgpj_EscrowStandardVerMng> tgpj_EscrowStandardVerMngList;
		if(totalCount > 0)
		{
			tgpj_EscrowStandardVerMngList = tgpj_EscrowStandardVerMngDao.findByPage(tgpj_EscrowStandardVerMngDao.getQuery(tgpj_EscrowStandardVerMngDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tgpj_EscrowStandardVerMngList = new ArrayList<Tgpj_EscrowStandardVerMng>();
		}
		
		properties.put("tgpj_EscrowStandardVerMngList", tgpj_EscrowStandardVerMngList);
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
