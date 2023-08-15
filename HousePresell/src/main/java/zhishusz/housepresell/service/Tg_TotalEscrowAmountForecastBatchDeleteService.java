package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_TotalEscrowAmountForecastForm;
import zhishusz.housepresell.database.dao.Tg_TotalEscrowAmountForecastDao;
import zhishusz.housepresell.database.po.Tg_TotalEscrowAmountForecast;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：托管总资金的预测结存
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_TotalEscrowAmountForecastBatchDeleteService
{
	@Autowired
	private Tg_TotalEscrowAmountForecastDao tg_TotalEscrowAmountForecastDao;

	public Properties execute(Tg_TotalEscrowAmountForecastForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tg_TotalEscrowAmountForecast tg_TotalEscrowAmountForecast = (Tg_TotalEscrowAmountForecast)tg_TotalEscrowAmountForecastDao.findById(tableId);
			if(tg_TotalEscrowAmountForecast == null)
			{
				return MyBackInfo.fail(properties, "'Tg_TotalEscrowAmountForecast(Id:" + tableId + ")'不存在");
			}
		
			tg_TotalEscrowAmountForecast.setTheState(S_TheState.Deleted);
			tg_TotalEscrowAmountForecastDao.save(tg_TotalEscrowAmountForecast);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
