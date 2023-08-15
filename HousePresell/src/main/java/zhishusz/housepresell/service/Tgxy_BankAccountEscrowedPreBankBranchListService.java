package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
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
 * Service列表查询：根据开户行加载托管账户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgxy_BankAccountEscrowedPreBankBranchListService
{
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_BankAccountEscrowedForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		
		model.setTheState(S_TheState.Normal);
		Integer totalCount = tgxy_BankAccountEscrowedDao.findByPage_Size(tgxy_BankAccountEscrowedDao.getQuery_Size(tgxy_BankAccountEscrowedDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgxy_BankAccountEscrowed> tgxy_BankAccountEscrowedList;
		if(totalCount > 0)
		{
			tgxy_BankAccountEscrowedList = tgxy_BankAccountEscrowedDao.findByPage(tgxy_BankAccountEscrowedDao.getQuery(tgxy_BankAccountEscrowedDao.getBasicHQL(), model));
		}
		else
		{
			tgxy_BankAccountEscrowedList = new ArrayList<Tgxy_BankAccountEscrowed>();
		}
		
		properties.put("tgxy_BankAccountEscrowedList", tgxy_BankAccountEscrowedList);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
