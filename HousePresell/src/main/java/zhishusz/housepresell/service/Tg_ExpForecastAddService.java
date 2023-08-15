package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_ExpForecastForm;
import zhishusz.housepresell.database.dao.Tg_ExpForecastDao;
import zhishusz.housepresell.database.po.Tg_ExpForecast;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
	
/*
 * Service添加操作：支出预测
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_ExpForecastAddService
{
	@Autowired
	private Tg_ExpForecastDao tg_ExpForecastDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Tg_ExpForecastForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = model.getTheState();
		String busiState = model.getBusiState();
		String eCode = model.geteCode();
		Long userStartId = model.getUserStartId();
		Long createTimeStamp = model.getCreateTimeStamp();
		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
		Long userRecordId = model.getUserRecordId();
		Long recordTimeStamp = model.getRecordTimeStamp();
		Long theDay = model.getTheDay();
		Integer theWeek = model.getTheWeek();
		Double payTrendForecast = model.getPayTrendForecast();
		Double applyAmount = model.getApplyAmount();
		Double payableFund = model.getPayableFund();
		Double nodeChangePayForecast = model.getNodeChangePayForecast();
		Double handlingFixedDeposit = model.getHandlingFixedDeposit();
		Double payForecast1 = model.getPayForecast1();
		Double payForecast2 = model.getPayForecast2();
		Double payForecast3 = model.getPayForecast3();
		Double payTotal = model.getPayTotal();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(busiState == null || busiState.length()< 1)
		{
			return MyBackInfo.fail(properties, "'busiState'不能为空");
		}
		if(eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCode'不能为空");
		}
		if(userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(createTimeStamp == null || createTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'createTimeStamp'不能为空");
		}
		if(lastUpdateTimeStamp == null || lastUpdateTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'lastUpdateTimeStamp'不能为空");
		}
		if(userRecordId == null || userRecordId < 1)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if(recordTimeStamp == null || recordTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'recordTimeStamp'不能为空");
		}
		if(theWeek == null || theWeek < 1)
		{
			return MyBackInfo.fail(properties, "'theWeek'不能为空");
		}
		if(payTrendForecast == null || payTrendForecast < 1)
		{
			return MyBackInfo.fail(properties, "'payTrendForecast'不能为空");
		}
		if(applyAmount == null || applyAmount < 1)
		{
			return MyBackInfo.fail(properties, "'applyAmount'不能为空");
		}
		if(payableFund == null || payableFund < 1)
		{
			return MyBackInfo.fail(properties, "'payableFund'不能为空");
		}
		if(nodeChangePayForecast == null || nodeChangePayForecast < 1)
		{
			return MyBackInfo.fail(properties, "'nodeChangePayForecast'不能为空");
		}
		if(handlingFixedDeposit == null || handlingFixedDeposit < 1)
		{
			return MyBackInfo.fail(properties, "'handlingFixedDeposit'不能为空");
		}
		if(payForecast1 == null || payForecast1 < 1)
		{
			return MyBackInfo.fail(properties, "'payForecast1'不能为空");
		}
		if(payForecast2 == null || payForecast2 < 1)
		{
			return MyBackInfo.fail(properties, "'payForecast2'不能为空");
		}
		if(payForecast3 == null || payForecast3 < 1)
		{
			return MyBackInfo.fail(properties, "'payForecast3'不能为空");
		}
		if(payTotal == null || payTotal < 1)
		{
			return MyBackInfo.fail(properties, "'payTotal'不能为空");
		}

		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
	
		Tg_ExpForecast tg_ExpForecast = new Tg_ExpForecast();
		tg_ExpForecast.setTheState(theState);
		tg_ExpForecast.setBusiState(busiState);
		tg_ExpForecast.seteCode(eCode);
		tg_ExpForecast.setUserStart(userStart);
		tg_ExpForecast.setCreateTimeStamp(createTimeStamp);
		tg_ExpForecast.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tg_ExpForecast.setUserRecord(userRecord);
		tg_ExpForecast.setRecordTimeStamp(recordTimeStamp);
		tg_ExpForecast.setTheDay(theDay);
		tg_ExpForecast.setTheWeek(theWeek);
		tg_ExpForecast.setPayTrendForecast(payTrendForecast);
		tg_ExpForecast.setApplyAmount(applyAmount);
		tg_ExpForecast.setPayableFund(payableFund);
		tg_ExpForecast.setNodeChangePayForecast(nodeChangePayForecast);
		tg_ExpForecast.setHandlingFixedDeposit(handlingFixedDeposit);
		tg_ExpForecast.setPayForecast1(payForecast1);
		tg_ExpForecast.setPayForecast2(payForecast2);
		tg_ExpForecast.setPayForecast3(payForecast3);
		tg_ExpForecast.setPayTotal(payTotal);
		tg_ExpForecastDao.save(tg_ExpForecast);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
