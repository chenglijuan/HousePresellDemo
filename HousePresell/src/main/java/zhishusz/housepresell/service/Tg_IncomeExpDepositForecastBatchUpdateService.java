package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tg_IncomeExpDepositForecastBatchForm;
import zhishusz.housepresell.controller.form.Tg_IncomeExpDepositForecastForm;
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
public class Tg_IncomeExpDepositForecastBatchUpdateService
{
	@Autowired
	private Tg_IncomeExpDepositForecastUpdateService tg_IncomeExpDepositForecastUpdateService;

	public Properties execute(Tg_IncomeExpDepositForecastBatchForm model)
	{
		Properties properties = new MyProperties();

		Tg_IncomeExpDepositForecastForm[] incomeExpDepositForecastFormList = model.getIncomeExpDepositForecastList();

		for (Tg_IncomeExpDepositForecastForm incomeExpDepositForecastForm : incomeExpDepositForecastFormList)
		{
			properties = tg_IncomeExpDepositForecastUpdateService.execute(incomeExpDepositForecastForm);
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
