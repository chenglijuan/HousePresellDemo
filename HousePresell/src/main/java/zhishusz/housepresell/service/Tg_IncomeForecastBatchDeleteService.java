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
 * Service批量删除：收入预测
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_IncomeForecastBatchDeleteService
{
	@Autowired
	private Tg_IncomeForecastDao tg_IncomeForecastDao;

	public Properties execute(Tg_IncomeForecastForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tg_IncomeForecast tg_IncomeForecast = (Tg_IncomeForecast)tg_IncomeForecastDao.findById(tableId);
			if(tg_IncomeForecast == null)
			{
				return MyBackInfo.fail(properties, "'Tg_IncomeForecast(Id:" + tableId + ")'不存在");
			}
		
			tg_IncomeForecast.setTheState(S_TheState.Deleted);
			tg_IncomeForecastDao.save(tg_IncomeForecast);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
