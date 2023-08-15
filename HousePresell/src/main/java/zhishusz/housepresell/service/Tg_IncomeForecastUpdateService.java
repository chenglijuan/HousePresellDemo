package zhishusz.housepresell.service;

import zhishusz.housepresell.database.po.state.S_TheState;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Service更新操作：收入预测
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_IncomeForecastUpdateService
{
	@Autowired
	private Tg_IncomeForecastDao tg_IncomeForecastDao;
	
	public Properties execute(Tg_IncomeForecastForm model)
	{
		Properties properties = new MyProperties();

		Double incomeTrendForecast = model.getIncomeTrendForecast();
		Double fixedExpire = model.getFixedExpire();
		Double bankLending = model.getBankLending();
		Double incomeForecast1 = model.getIncomeForecast1();
		Double incomeForecast2 = model.getIncomeForecast2();
		Double incomeForecast3 = model.getIncomeForecast3();

		Long tg_IncomeForecastId = model.getTableId();

		Tg_IncomeForecast tg_IncomeForecast = (Tg_IncomeForecast)tg_IncomeForecastDao.findById(tg_IncomeForecastId);
		if(tg_IncomeForecast == null)
		{
			return MyBackInfo.fail(properties, "'Tg_IncomeForecast(Id:" + tg_IncomeForecastId + ")'不存在");
		}

		tg_IncomeForecast.setIncomeTrendForecast(incomeTrendForecast);
		tg_IncomeForecast.setFixedExpire(fixedExpire);
		tg_IncomeForecast.setBankLending(bankLending);
		tg_IncomeForecast.setIncomeForecast1(incomeForecast1);
		tg_IncomeForecast.setIncomeForecast2(incomeForecast2);
		tg_IncomeForecast.setIncomeForecast3(incomeForecast3);
		tg_IncomeForecast.setIncomeTotal(isDoubleNull(incomeTrendForecast) + isDoubleNull(fixedExpire) + isDoubleNull(bankLending) + isDoubleNull(incomeForecast1) + isDoubleNull(incomeForecast2) + isDoubleNull(incomeForecast3));
	
		tg_IncomeForecastDao.save(tg_IncomeForecast);
		
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
