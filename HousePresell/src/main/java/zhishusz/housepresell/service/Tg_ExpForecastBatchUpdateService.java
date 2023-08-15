package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tg_ExpForecastBatchForm;
import zhishusz.housepresell.controller.form.Tg_ExpForecastForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

/*
 * Service更新操作：支出预测
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_ExpForecastBatchUpdateService
{
	@Autowired
	private Tg_ExpForecastUpdateService tg_ExpForecastUpdateService;

	public Properties execute(Tg_ExpForecastBatchForm model)
	{
		Properties properties = new MyProperties();

		Tg_ExpForecastForm[] expForecastFormList = model.getExpForecastList();

		for (Tg_ExpForecastForm expForecastForm : expForecastFormList)
		{
			properties = tg_ExpForecastUpdateService.execute(expForecastForm);
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
