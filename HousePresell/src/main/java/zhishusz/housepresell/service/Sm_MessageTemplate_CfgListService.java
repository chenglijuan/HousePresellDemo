package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_MessageTemplate_CfgForm;
import zhishusz.housepresell.database.dao.Sm_MessageTemplate_CfgDao;
import zhishusz.housepresell.database.po.Sm_MessageTemplate_Cfg;
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
 * Service列表查询：审批流-流程配置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_MessageTemplate_CfgListService
{
	@Autowired
	private Sm_MessageTemplate_CfgDao sm_messageTemplate_cfgDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Sm_MessageTemplate_CfgForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();

		String orderBy = model.getOrderBy();

		if(orderBy == null || orderBy.length() == 0)
		{
			model.setOrderBy(null);
		}

		if(keyword == null || keyword.length()==0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%"+keyword+"%");
		}

		Integer totalCount = sm_messageTemplate_cfgDao.findByPage_Size(sm_messageTemplate_cfgDao.getQuery_Size(sm_messageTemplate_cfgDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;

		List<Sm_MessageTemplate_Cfg> sm_MessageTemplate_CfgList;
		if(totalCount > 0)
		{
			sm_MessageTemplate_CfgList = sm_messageTemplate_cfgDao.findByPage(sm_messageTemplate_cfgDao.getQuery(sm_messageTemplate_cfgDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			sm_MessageTemplate_CfgList = new ArrayList<Sm_MessageTemplate_Cfg>();
		}

		properties.put("sm_MessageTemplate_CfgList", sm_MessageTemplate_CfgList);
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
