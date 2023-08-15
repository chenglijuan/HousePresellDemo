package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tg_IncomeForecastBatchForm;
import zhishusz.housepresell.controller.form.Tg_IncomeForecastForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
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
public class Tg_IncomeForecastBatchUpdateService
{
	@Autowired
	private Tg_IncomeForecastUpdateService tg_IncomeForecastUpdateService;

	public Properties execute(Tg_IncomeForecastBatchForm model)
	{
		Properties properties = new MyProperties();

		Tg_IncomeForecastForm[] incomeForecastFormList = model.getIncomeForecastList();

		for (Tg_IncomeForecastForm incomeForecastForm : incomeForecastFormList)
		{
			properties = tg_IncomeForecastUpdateService.execute(incomeForecastForm);
			if (S_NormalFlag.fail.equals(properties.get(S_NormalFlag.result)))
			{
				return properties;
			}
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
