package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tg_IncomeExpDepositForecastForm;
import zhishusz.housepresell.database.dao.Tg_IncomeExpDepositForecastDao;
import zhishusz.housepresell.database.po.Tg_IncomeExpDepositForecast;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Tg_IncomeExpDepositForecastExportExcelTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：收支存预测
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_IncomeExpDepositForecastExportExcelService
{
	private MyDatetime myDatetime = MyDatetime.getInstance();

	@Autowired
	private Tg_IncomeExpDepositForecastDao tg_IncomeExpDepositForecastDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_IncomeExpDepositForecastForm model)
	{
		Properties properties = new MyProperties();

		if (model.getStartTimeStr() == null || model.getStartTimeStr().length() == 0 ||
			model.getEndTimeStr() == null || model.getEndTimeStr().length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择时间");
		}

		Long startTimeStamp = myDatetime.stringToLong(model.getStartTimeStr());
		Long endTimeStamp =   myDatetime.stringToLong(model.getEndTimeStr());

		model.setStartTimeStamp(startTimeStamp);
		model.setEndTimeStamp(endTimeStamp);

		model.setTheState(S_TheState.Normal);

		Integer totalCount = tg_IncomeExpDepositForecastDao.findByPage_Size(tg_IncomeExpDepositForecastDao.getQuery_Size(tg_IncomeExpDepositForecastDao.getBasicHQL(), model));

		List<Tg_IncomeExpDepositForecast> tg_IncomeExpDepositForecastList;
		if(totalCount > 0)
		{
			tg_IncomeExpDepositForecastList = tg_IncomeExpDepositForecastDao.findByPage(tg_IncomeExpDepositForecastDao.getQuery(tg_IncomeExpDepositForecastDao.getBasicHQL(), model));
			for (Tg_IncomeExpDepositForecast tg_IncomeExpDepositForecast : tg_IncomeExpDepositForecastList) {
			    tg_IncomeExpDepositForecast.setCollocationBalance(tg_IncomeExpDepositForecast.getCollocationBalance() - tg_IncomeExpDepositForecast.getCollocationReference());
            }
		}
		else
		{
			tg_IncomeExpDepositForecastList = new ArrayList<Tg_IncomeExpDepositForecast>();
		}

		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();


		Properties propertiesExport = exportToExcelUtil.execute(tg_IncomeExpDepositForecastList, Tg_IncomeExpDepositForecastExportExcelTemplate.class,
				"收支存预测");

		if(S_NormalFlag.fail.equals(propertiesExport.get(S_NormalFlag.result)))
		{
			return properties;
		}

		properties.put("fileDownloadPath", model.getServerBasePath()+propertiesExport.get("fileRelativePath"));

		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
