package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Tgpf_DayEndBalancingForm;
import zhishusz.housepresell.database.dao.Tgpf_DayEndBalancingDao;
import zhishusz.housepresell.database.po.Tgpf_DayEndBalancing;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：业务对账-日终结算
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_DayEndBalancingListService
{
	@Autowired
	private Tgpf_DayEndBalancingDao tgpf_DayEndBalancingDao;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_DayEndBalancingForm model)
	{
		Properties properties = new MyProperties();
		
		String billTimeStap = model.getBillTimeStamp();

		if (null == billTimeStap || billTimeStap.trim().isEmpty())
		{
			billTimeStap = myDatetime.dateToSimpleString(System.currentTimeMillis());
			model.setBillTimeStamp(billTimeStap);
		}

		model.setTheState(S_TheState.Normal);
		
		/**
		 * xsz by time 2019-8-8 14:12:07
		 * 解决网银上传数据和业务对账条数不匹配显示问题
		 */
		model.setTotalCount(0);
		/**
		 * xsz by time 2019-8-8 14:12:07
		 * 解决网银上传数据和业务对账条数不匹配显示问题
		 */
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = tgpf_DayEndBalancingDao.findByPage_Size(tgpf_DayEndBalancingDao.getQuery_Size(tgpf_DayEndBalancingDao.getSpecialHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		Long sumCount = 0l;
		Double sumAmount = 0.0;
		List<Tgpf_DayEndBalancing> tgpf_DayEndBalancingList;
		if(totalCount > 0)
		{
			tgpf_DayEndBalancingList = tgpf_DayEndBalancingDao.findByPage(tgpf_DayEndBalancingDao.getQuery(tgpf_DayEndBalancingDao.getSpecialHQL(), model), pageNumber, countPerPage);
			
			// 查询总笔数
			String querySumCountCondition = " nvl(sum(totalCount),0) ";

			sumCount = (Long)
					tgpf_DayEndBalancingDao.findOneByQuery(tgpf_DayEndBalancingDao.getSpecialQuery(
							tgpf_DayEndBalancingDao.getBasicHQL(), model, querySumCountCondition));
			
			// 查询总笔数
			String querySumAmountCondition = " nvl(sum(totalAmount),0) ";

			sumAmount = (Double)
					tgpf_DayEndBalancingDao.findOneByQuery(tgpf_DayEndBalancingDao.getSpecialQuery(
							tgpf_DayEndBalancingDao.getBasicHQL(), model, querySumAmountCondition));

		}
		else
		{
			tgpf_DayEndBalancingList = new ArrayList<Tgpf_DayEndBalancing>();
		}
		
		properties.put("tgpf_DayEndBalancingList", tgpf_DayEndBalancingList);
		properties.put("sumCount", sumCount);
		properties.put("sumAmount", sumAmount);
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
