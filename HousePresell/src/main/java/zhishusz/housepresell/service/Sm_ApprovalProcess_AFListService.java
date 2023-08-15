package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import zhishusz.housepresell.util.MyDatetime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：审批流-申请单
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_ApprovalProcess_AFListService
{
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;

	private MyDatetime myDatetime = MyDatetime.getInstance();
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_ApprovalProcess_AFForm model)
	{
		Properties properties = new MyProperties();

		Long userStartId = model.getUserId(); //登录用户Id
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		//关键字
		String keyword = model.getKeyword();
		if(keyword !=null && keyword.length() > 0)
		{
			model.setKeyword("%"+keyword+"%");
		}
		else
		{
			model.setKeyword(null);
		}

		//业务编码
		String busiCode = model.getBusiCode();
		if(busiCode ==null || busiCode.length() == 0)
		{
			model.setBusiCode(null);
		}

		//申请日期
		if(model.getApprovalApplyDate() !=null && model.getApprovalApplyDate().length() > 0)
		{
			String[] applyDate = model.getApprovalApplyDate().split(" - ");
			Long startTimeStamp = myDatetime.stringToLong(applyDate[0]);
			Long dayTime = 24L * 60 * 60 * 1000 -1;
			Long endTimeStamp = myDatetime.stringToLong(applyDate[1])+dayTime;
			model.setStartTimeStamp(startTimeStamp);
			model.setEndTimeStamp(endTimeStamp);
		}

		//业务状态
		String busiState = model.getBusiState();
		if(busiState ==null || busiState.length() ==0)
		{
			model.setBusiState(null);
		}

		//排序
		String orderBy = model.getOrderBy();
		if(orderBy == null || orderBy.length() == 0)
		{
//			model.setOrderBy("createTimeStamp desc");
			model.setOrderBy(" DECODE(busiState ,'待提交',-2,'审核中',-1,'已完结',0 ) ");
		}

		//登录用户
		model.setUserStartId(userStartId);
		
		Integer totalCount = sm_ApprovalProcess_AFDao.findByPage_Size(sm_ApprovalProcess_AFDao.getQuery_Size(sm_ApprovalProcess_AFDao.getBasicHQL(), model));
		
//		Integer totalPage = totalCount / countPerPage;
//		if (totalCount % countPerPage > 0) totalPage++;
//		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_ApprovalProcess_AF> sm_ApprovalProcess_AFList;
		List<Sm_ApprovalProcess_AF> sm_ApprovalProcess_AFLists = new ArrayList<Sm_ApprovalProcess_AF>();
		if(totalCount > 0)
		{
//			sm_ApprovalProcess_AFList = sm_ApprovalProcess_AFDao.findByPage(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), model), pageNumber, countPerPage);
			sm_ApprovalProcess_AFList = sm_ApprovalProcess_AFDao.findByPage(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), model));
		
			totalCount = sm_ApprovalProcess_AFList.size();
			Integer totalPage = totalCount / countPerPage;
			if (totalCount % countPerPage > 0) totalPage++;
			if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
			if(pageNumber == 0)
			{
				pageNumber = 1;
			}
			if(totalCount > countPerPage)
			{
				int from = (pageNumber-1) * countPerPage;
				int to = from + countPerPage;
				if(totalCount % countPerPage > 0 && pageNumber == totalPage)
				{
					to = from + (totalCount % countPerPage);
				}
				
				if(null != model.getIsCountPage() && "0".equals(model.getIsCountPage()))
				{
					sm_ApprovalProcess_AFLists = sm_ApprovalProcess_AFList;
				}
				else
				{
					sm_ApprovalProcess_AFLists = sm_ApprovalProcess_AFList.subList(from ,to);
				}
				
			}
			else
			{
				sm_ApprovalProcess_AFLists = sm_ApprovalProcess_AFList;
			}
			properties.put(S_NormalFlag.totalPage, totalPage);
		}
		else
		{
			sm_ApprovalProcess_AFList = new ArrayList<Sm_ApprovalProcess_AF>();
		}
		
//		properties.put("sm_ApprovalProcess_AFList", sm_ApprovalProcess_AFList);
		properties.put("sm_ApprovalProcess_AFList", sm_ApprovalProcess_AFLists);
		properties.put(S_NormalFlag.keyword, keyword);
//		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
