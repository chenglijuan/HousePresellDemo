package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tg_RiskCheckBusiCodeSumForm;
import zhishusz.housepresell.controller.form.Tg_RiskRoutineCheckInfoForm;
import zhishusz.housepresell.database.dao.Tg_RiskRoutineCheckInfoDao;
import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：风控例行抽查表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tg_RiskRoutineCheckInfoListService
{
	@Autowired
	private Tg_RiskRoutineCheckInfoDao tg_RiskRoutineCheckInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tg_RiskRoutineCheckInfoForm model)
	{
		Properties properties = new MyProperties();
		String enableDateSearchStr = model.getDateStr();
		if(enableDateSearchStr != null && enableDateSearchStr.length() > 0)
		{
			long startTimeStamp = MyDatetime.getInstance().stringToLong(enableDateSearchStr, "yyyy-MM");
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(startTimeStamp);
			calendar.add(Calendar.MONTH, 1);
			model.setStartDateTimeStamp(startTimeStamp);
			model.setEndDateTimeStamp(calendar.getTimeInMillis());
		}

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = tg_RiskRoutineCheckInfoDao.findByPage_Size(tg_RiskRoutineCheckInfoDao.getQuery_Size(tg_RiskRoutineCheckInfoDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tg_RiskRoutineCheckInfo> tg_RiskRoutineCheckInfoList;
		if(totalCount > 0)
		{
			tg_RiskRoutineCheckInfoList = tg_RiskRoutineCheckInfoDao.findByPage(tg_RiskRoutineCheckInfoDao.getQuery(tg_RiskRoutineCheckInfoDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tg_RiskRoutineCheckInfoList = new ArrayList<Tg_RiskRoutineCheckInfo>();
		}
		
		properties.put("tg_RiskRoutineCheckInfoList", tg_RiskRoutineCheckInfoList);
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
