package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_IncomeForecastForm;
import zhishusz.housepresell.database.dao.Tg_IncomeForecastDao;
import zhishusz.housepresell.database.po.Tg_IncomeForecast;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：收入预测
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_IncomeForecastDeleteService
{
	@Autowired
	private Tg_IncomeForecastDao tg_IncomeForecastDao;

	public Properties execute(Tg_IncomeForecastForm model)
	{
		Properties properties = new MyProperties();

		Long tg_IncomeForecastId = model.getTableId();
		Tg_IncomeForecast tg_IncomeForecast = (Tg_IncomeForecast)tg_IncomeForecastDao.findById(tg_IncomeForecastId);
		if(tg_IncomeForecast == null)
		{
			return MyBackInfo.fail(properties, "'Tg_IncomeForecast(Id:" + tg_IncomeForecastId + ")'不存在");
		}
		
		tg_IncomeForecast.setTheState(S_TheState.Deleted);
		tg_IncomeForecastDao.save(tg_IncomeForecast);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
