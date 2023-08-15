package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Service更新操作：支出预测
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_ExpForecastUpdateService
{
	@Autowired
	private Tg_ExpForecastDao tg_ExpForecastDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Tg_ExpForecastForm model)
	{
		Properties properties = new MyProperties();

		Double payTrendForecast = model.getPayTrendForecast();
		Double applyAmount = model.getApplyAmount();
		Double payableFund = model.getPayableFund();
		Double nodeChangePayForecast = model.getNodeChangePayForecast();
		Double handlingFixedDeposit = model.getHandlingFixedDeposit();
		Double payForecast1 = model.getPayForecast1();
		Double payForecast2 = model.getPayForecast2();
		Double payForecast3 = model.getPayForecast3();

		Long tg_ExpForecastId = model.getTableId();

		Tg_ExpForecast tg_ExpForecast = (Tg_ExpForecast)tg_ExpForecastDao.findById(tg_ExpForecastId);
		if(tg_ExpForecast == null)
		{
			return MyBackInfo.fail(properties, "'Tg_ExpForecast(Id:" + tg_ExpForecastId + ")'不存在");
		}

		tg_ExpForecast.setPayTrendForecast(payTrendForecast);
		tg_ExpForecast.setApplyAmount(applyAmount);
		tg_ExpForecast.setPayableFund(payableFund);
		tg_ExpForecast.setNodeChangePayForecast(nodeChangePayForecast);
		tg_ExpForecast.setHandlingFixedDeposit(handlingFixedDeposit);
		tg_ExpForecast.setPayForecast1(payForecast1);
		tg_ExpForecast.setPayForecast2(payForecast2);
		tg_ExpForecast.setPayForecast3(payForecast3);
	
		tg_ExpForecastDao.save(tg_ExpForecast);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
