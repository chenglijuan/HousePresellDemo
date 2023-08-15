package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import zhishusz.housepresell.util.MyDatetime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Tgpf_FundOverallPlanForm;
import zhishusz.housepresell.database.dao.Tgpf_FundOverallPlanDao;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlan;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：资金统筹
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_FundOverallPlanListService
{
	@Autowired
	private Tgpf_FundOverallPlanDao tgpf_FundOverallPlanDao;

	private MyDatetime myDatetime = MyDatetime.getInstance();
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_FundOverallPlanForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		//关键字
		String keyword = model.getKeyword();
		if(keyword == null || keyword.length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%"+keyword+"%");
		}

		//统筹日期
		String fundOverallPlanDate = model.getFundOverallPlanDate();//统筹日期
		if(fundOverallPlanDate == null || fundOverallPlanDate.length() == 0)
		{
			model.setFundOverallPlanDate(null);
		}

		//业务状态
		String busiState = model.getBusiState(); //业务状态
		if(busiState == null || busiState.length() == 0)
		{
			model.setBusiState(null);
		}

		//审批状态
		String approvalState = model.getApprovalState();//审批状态
		if(approvalState == null || approvalState.length() == 0)
		{
			model.setApprovalState(null);
		}

		//排序
		String orderBy = model.getOrderBy();
		if(orderBy == null || orderBy.length() == 0)
		{
			model.setOrderBy(" decode(approvalState,'待提交',0,'审核中',1,'已完结',2,'不通过',3,-1), createTimeStamp desc");
		}

		Integer totalCount = tgpf_FundOverallPlanDao.findByPage_Size(tgpf_FundOverallPlanDao.getQuery_Size(tgpf_FundOverallPlanDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgpf_FundOverallPlan> tgpf_FundOverallPlanList;
		if(totalCount > 0)
		{
			tgpf_FundOverallPlanList = tgpf_FundOverallPlanDao.findByPage(tgpf_FundOverallPlanDao.getQuery(tgpf_FundOverallPlanDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tgpf_FundOverallPlanList = new ArrayList<Tgpf_FundOverallPlan>();
		}
		
		properties.put("tgpf_FundOverallPlanList", tgpf_FundOverallPlanList);
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
