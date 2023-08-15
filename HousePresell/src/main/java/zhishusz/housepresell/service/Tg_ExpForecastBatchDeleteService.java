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
 * Service批量删除：支出预测
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_ExpForecastBatchDeleteService
{
	@Autowired
	private Tg_ExpForecastDao tg_ExpForecastDao;

	public Properties execute(Tg_ExpForecastForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tg_ExpForecast tg_ExpForecast = (Tg_ExpForecast)tg_ExpForecastDao.findById(tableId);
			if(tg_ExpForecast == null)
			{
				return MyBackInfo.fail(properties, "'Tg_ExpForecast(Id:" + tableId + ")'不存在");
			}
		
			tg_ExpForecast.setTheState(S_TheState.Deleted);
			tg_ExpForecastDao.save(tg_ExpForecast);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
