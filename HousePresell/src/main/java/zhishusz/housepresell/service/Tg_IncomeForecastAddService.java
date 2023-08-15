package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_IncomeForecastForm;
import zhishusz.housepresell.database.dao.Tg_IncomeForecastDao;
import zhishusz.housepresell.database.po.Tg_IncomeForecast;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
	
/*
 * Service添加操作：收入预测
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_IncomeForecastAddService
{
	@Autowired
	private Tg_IncomeForecastDao tg_IncomeForecastDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Tg_IncomeForecastForm model)
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
		Double incomeTrendForecast = model.getIncomeTrendForecast();
		Double fixedExpire = model.getFixedExpire();
		Double bankLending = model.getBankLending();
		Double incomeForecast1 = model.getIncomeForecast1();
		Double incomeForecast2 = model.getIncomeForecast2();
		Double incomeForecast3 = model.getIncomeForecast3();
		Double incomeTotal = model.getIncomeTotal();
		
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
		if(incomeTrendForecast == null || incomeTrendForecast < 1)
		{
			return MyBackInfo.fail(properties, "'incomeTrendForecast'不能为空");
		}
		if(fixedExpire == null || fixedExpire < 1)
		{
			return MyBackInfo.fail(properties, "'fixedExpire'不能为空");
		}
		if(bankLending == null || bankLending < 1)
		{
			return MyBackInfo.fail(properties, "'bankLending'不能为空");
		}
		if(incomeForecast1 == null || incomeForecast1 < 1)
		{
			return MyBackInfo.fail(properties, "'incomeForecast1'不能为空");
		}
		if(incomeForecast2 == null || incomeForecast2 < 1)
		{
			return MyBackInfo.fail(properties, "'incomeForecast2'不能为空");
		}
		if(incomeForecast3 == null || incomeForecast3 < 1)
		{
			return MyBackInfo.fail(properties, "'incomeForecast3'不能为空");
		}
		if(incomeTotal == null || incomeTotal < 1)
		{
			return MyBackInfo.fail(properties, "'incomeTotal'不能为空");
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
	
		Tg_IncomeForecast tg_IncomeForecast = new Tg_IncomeForecast();
		tg_IncomeForecast.setTheState(theState);
		tg_IncomeForecast.setBusiState(busiState);
		tg_IncomeForecast.seteCode(eCode);
		tg_IncomeForecast.setUserStart(userStart);
		tg_IncomeForecast.setCreateTimeStamp(createTimeStamp);
		tg_IncomeForecast.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tg_IncomeForecast.setUserRecord(userRecord);
		tg_IncomeForecast.setRecordTimeStamp(recordTimeStamp);
		tg_IncomeForecast.setTheDay(theDay);
		tg_IncomeForecast.setTheWeek(theWeek);
		tg_IncomeForecast.setIncomeTrendForecast(incomeTrendForecast);
		tg_IncomeForecast.setFixedExpire(fixedExpire);
		tg_IncomeForecast.setBankLending(bankLending);
		tg_IncomeForecast.setIncomeForecast1(incomeForecast1);
		tg_IncomeForecast.setIncomeForecast2(incomeForecast2);
		tg_IncomeForecast.setIncomeForecast3(incomeForecast3);
		tg_IncomeForecast.setIncomeTotal(incomeTotal);
		tg_IncomeForecastDao.save(tg_IncomeForecast);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
