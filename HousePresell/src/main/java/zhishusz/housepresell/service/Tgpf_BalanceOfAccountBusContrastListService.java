package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：业务对账列表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_BalanceOfAccountBusContrastListService
{
	
	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;
	@Autowired
	private Tgpf_BalanceOfAccountBusContrastAddService tgpf_BalanceOfAccountBusContrastAddService;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	// 可传入参数 ： 1.记账日期billTimeStamp 2.关键字 keyword
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_BalanceOfAccountForm model)
	{
		Properties properties = new MyProperties();
		

		
		String keyword = model.getKeyword();
		String billTimeStap = model.getBillTimeStamp();
		
		if (null != keyword && !keyword.trim().isEmpty())
		{
			model.setKeyword("%"+keyword+"%");
		}else{
			model.setKeyword(null);
		}
		
		if (null == billTimeStap || billTimeStap.trim().isEmpty())
		{
			
			model.setBillTimeStamp(myDatetime.getSpecifiedDayBefore(myDatetime.dateToSimpleString(System.currentTimeMillis())));
		}
		
		
		if ( null == model.getAccountType() || 0 == model.getAccountType() )
		{
			model.setReconciliationDate("1");
		}else{
			model.setAccountType(1);
		}
		
		if ( null == model.getTheType() )
		{
			model.setTheType(null);
		}
		
		/**
		 * xsz by time 2019-8-8 14:12:07
		 * 解决网银上传数据和业务对账条数不匹配显示问题
		 */
		model.setCenterTotalCount(0);
		/**
		 * xsz by time 2019-8-8 14:12:07
		 * 解决网银上传数据和业务对账条数不匹配显示问题
		 */
		
		model.setTheState(S_TheState.Normal);
		
//		tgpf_BalanceOfAccountBusContrastAddService.execute(model);
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		

		Integer totalCount = tgpf_BalanceOfAccountDao.findByPage_Size(tgpf_BalanceOfAccountDao.getQuery_Size(tgpf_BalanceOfAccountDao.getSpecialHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgpf_BalanceOfAccount> tgpf_BalanceOfAccountList;
		if(totalCount > 0)
		{
			tgpf_BalanceOfAccountList = tgpf_BalanceOfAccountDao.findByPage(tgpf_BalanceOfAccountDao.getQuery(tgpf_BalanceOfAccountDao.getSpecialHQL(), model), pageNumber, countPerPage);
		}
		else
		{ 
			tgpf_BalanceOfAccountList = new ArrayList<Tgpf_BalanceOfAccount>();
		}
		
		properties.put("tgpf_BalanceOfAccountList", tgpf_BalanceOfAccountList);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		properties.put("billTimeStamp", billTimeStap);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
