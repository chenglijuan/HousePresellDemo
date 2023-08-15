package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tg_IncomeExpDepositForecastForm;
import zhishusz.housepresell.database.dao.Tg_IncomeExpDepositForecastDao;
import zhishusz.housepresell.database.po.Tg_IncomeExpDepositForecast;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

/*
 * Service更新操作：收入预测
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_IncomeExpDepositForecastUpdateService
{
	@Autowired
	private Tg_IncomeExpDepositForecastDao tg_IncomeExpDepositForecastDao;
	
	public Properties execute(Tg_IncomeExpDepositForecastForm model)
	{
		Properties properties = new MyProperties();

		Double lastDaySurplus = model.getLastDaySurplus();
		Double incomeTotal = model.getIncomeTotal();
		Double expTotal = model.getExpTotal();
		Double todaySurplus = model.getTodaySurplus();
		Double collocationReference = model.getCollocationReference();
		Double collocationBalance = model.getCollocationBalance();
		Double canDepositReference1 = model.getCanDepositReference1();
		Double canDepositReference2 = model.getCanDepositReference2();

		Long tg_IncomeExpDepositForecastId = model.getTableId();

		Tg_IncomeExpDepositForecast tg_IncomeExpDepositForecast = (Tg_IncomeExpDepositForecast)tg_IncomeExpDepositForecastDao.findById(tg_IncomeExpDepositForecastId);
		if(tg_IncomeExpDepositForecast == null)
		{
			return MyBackInfo.fail(properties, "'Tg_IncomeExpDepositForecast(Id:" + tg_IncomeExpDepositForecastId + ")'不存在");
		}

		tg_IncomeExpDepositForecast.setLastDaySurplus(lastDaySurplus);
		tg_IncomeExpDepositForecast.setIncomeTotal(incomeTotal);
		tg_IncomeExpDepositForecast.setExpTotal(expTotal);
		tg_IncomeExpDepositForecast.setTodaySurplus(todaySurplus);
		tg_IncomeExpDepositForecast.setCollocationReference(collocationReference);
//		tg_IncomeExpDepositForecast.setCollocationBalance(collocationBalance);
		tg_IncomeExpDepositForecast.setCanDepositReference1(canDepositReference1);
		tg_IncomeExpDepositForecast.setCanDepositReference2(canDepositReference2);
	
		tg_IncomeExpDepositForecastDao.update(tg_IncomeExpDepositForecast);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}

	private Double isDoubleNull(Double value)
	{
		if (value == null)
		{
			return 0.0;
		}
		else
		{
			return value;
		}
	}


}
