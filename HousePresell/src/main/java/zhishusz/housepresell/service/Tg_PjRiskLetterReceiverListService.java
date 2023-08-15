package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_PjRiskLetterReceiverForm;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDao;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDtlDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskLetterReceiverDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_PjRiskLetterReceiver;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：项目风险函
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tg_PjRiskLetterReceiverListService
{
	@Autowired
	private Tg_PjRiskLetterReceiverDao tg_PjRiskLetterReceiverDao;
	@Autowired
	private Sm_CommonMessageDtlDao sm_CommonMessageDtlDao;
	@Autowired
	private Sm_CommonMessageDao sm_CommonMessageDao;

	
	@SuppressWarnings("unchecked")
	public Properties execute(Tg_PjRiskLetterReceiverForm model)
	{
		Properties properties = new MyProperties();
		
		Sm_User user = model.getUser();
		
		Emmp_CompanyInfo emmp_CompanyInfo = user.getCompany();
		model.setEmmp_CompanyInfo(emmp_CompanyInfo);
		model.setSendWay(0);
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		
		
		model.setTheState(S_TheState.Normal);
		
		Integer totalCount = tg_PjRiskLetterReceiverDao.findByPage_Size(tg_PjRiskLetterReceiverDao.getQuery_Size(tg_PjRiskLetterReceiverDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tg_PjRiskLetterReceiver> tg_PjRiskLetterReceiverList;
		if(totalCount > 0)
		{
			tg_PjRiskLetterReceiverList = tg_PjRiskLetterReceiverDao.findByPage(tg_PjRiskLetterReceiverDao.getQuery(tg_PjRiskLetterReceiverDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tg_PjRiskLetterReceiverList = new ArrayList<Tg_PjRiskLetterReceiver>();
		}
		
		properties.put("tg_PjRiskLetterReceiverList", tg_PjRiskLetterReceiverList);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
