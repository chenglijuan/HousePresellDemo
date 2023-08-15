package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import zhishusz.housepresell.database.po.state.S_DepositState;
import zhishusz.housepresell.util.MyDatetime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Tg_DepositManagementForm;
import zhishusz.housepresell.database.dao.Tg_DepositManagementDao;
import zhishusz.housepresell.database.po.Tg_DepositManagement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：存单管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tg_DepositManagementListService
{
	@Autowired
	private Tg_DepositManagementDao tg_DepositManagementDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tg_DepositManagementForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();

		if (keyword != null)
		{
			model.setKeyword("%"+keyword+"%");
		}

		if (model.getOrderBy() != null && model.getOrderBy().length() > 0)
		{
			String[] orderByAr = model.getOrderBy().split(" ");
			if ("startDateStr".equals(orderByAr[0]))
			{
				model.setOrderBy("startDate");
			}
			else if ("stopDateStr".equals(orderByAr[0]))
			{
				model.setOrderBy("stopDate");
			}
			else
			{
				model.setOrderBy(orderByAr[0]);
			}
			model.setOrderByType(orderByAr[1]);
		}
		else
		{
			if (S_DepositState.InProgress.equals(model.getDepositState()))
			{
				model.setOrderBy("startDate");
			}
			else
			{
				model.setOrderBy("stopDate desc, startDate");
			}
			model.setOrderByType("desc");
		}

		if (model.getDepositState() == null || model.getDepositState().length() == 0)
		{
			model.setExceptDepositState("03");
		}

		if (model.getStartDateStr() != null && model.getStartDateStr().length() > 0)
		{
			model.setStartDate(MyDatetime.getInstance().stringToLong(model.getStartDateStr()));
		}

		if (model.getStopDateStr() != null && model.getStopDateStr().length() > 0)
		{
			String stopDateStr = model.getStopDateStr().trim();
			
			model.setStopDate(MyDatetime.getInstance().stringToLong(stopDateStr));
		}
		
		if (model.getListDateStrEnd() != null && model.getListDateStrEnd().length() > 0)
		{
			String listDateStrEnd = model.getListDateStrEnd().trim();
			
			model.setListDateStrEndLon(MyDatetime.getInstance().stringToLong(listDateStrEnd));
		}

		if ("".equals(model.getDepositState()))
		{
			model.setDepositState(null);
		}

		if ("".equals(model.getDepositProperty()))
		{
			model.setDepositProperty(null);
		}

		if ("".equals(model.getBankOfDepositId()))
		{
			model.setBankOfDepositId(null);
		}

		Integer totalCount;
		if (S_DepositState.InProgress.equals(model.getDepositState()))
		{
			totalCount = tg_DepositManagementDao.findByPage_Size(tg_DepositManagementDao.getQuery_Size(tg_DepositManagementDao.getBasicHQLInProgress(), model));
		}
		else
		{
			totalCount = tg_DepositManagementDao.findByPage_Size(tg_DepositManagementDao.getQuery_Size(tg_DepositManagementDao.getBasicHQL(), model));
		}
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tg_DepositManagement> tg_DepositManagementList;
		if(totalCount > 0)
		{
			if (S_DepositState.InProgress.equals(model.getDepositState()))
			{
				tg_DepositManagementList = tg_DepositManagementDao.findByPage(tg_DepositManagementDao.getQuery(tg_DepositManagementDao.getBasicHQLInProgress(), model), pageNumber, countPerPage);
			}
			else
			{
				tg_DepositManagementList = tg_DepositManagementDao.findByPage(tg_DepositManagementDao.getQuery(tg_DepositManagementDao.getBasicHQL(), model), pageNumber, countPerPage);
			}
		}
		else
		{
			tg_DepositManagementList = new ArrayList<Tg_DepositManagement>();
		}
		
		properties.put("tg_DepositManagementList", tg_DepositManagementList);
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
