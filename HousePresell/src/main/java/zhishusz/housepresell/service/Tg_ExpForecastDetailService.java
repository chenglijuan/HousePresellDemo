package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_ExpForecastForm;
import zhishusz.housepresell.database.dao.Tg_ExpForecastDao;
import zhishusz.housepresell.database.po.Tg_ExpForecast;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：支出预测
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_ExpForecastDetailService
{
	@Autowired
	private Tg_ExpForecastDao tg_ExpForecastDao;

	public Properties execute(Tg_ExpForecastForm model)
	{
		Properties properties = new MyProperties();

		Long tg_ExpForecastId = model.getTableId();
		Tg_ExpForecast tg_ExpForecast = (Tg_ExpForecast)tg_ExpForecastDao.findById(tg_ExpForecastId);
		if(tg_ExpForecast == null || S_TheState.Deleted.equals(tg_ExpForecast.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Tg_ExpForecast(Id:" + tg_ExpForecastId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tg_ExpForecast", tg_ExpForecast);

		return properties;
	}
}
