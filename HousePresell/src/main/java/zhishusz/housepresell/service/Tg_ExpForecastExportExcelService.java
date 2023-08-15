package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tg_ExpForecastForm;
import zhishusz.housepresell.database.dao.Tg_ExpForecastDao;
import zhishusz.housepresell.database.po.Tg_ExpForecast;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Tg_ExpForecastExportExcelTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：收入预测
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_ExpForecastExportExcelService
{
	private MyDatetime myDatetime = MyDatetime.getInstance();

	@Autowired
	private Tg_ExpForecastDao tg_ExpForecastDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_ExpForecastForm model)
	{
		Properties properties = new MyProperties();

		if (model.getStartTimeStr() == null || model.getStartTimeStr().length() == 0 ||
			model.getEndTimeStr() == null || model.getEndTimeStr().length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择时间段");
		}

		Long startTimeStamp = myDatetime.stringToLong(model.getStartTimeStr());
		Long endTimeStamp =   myDatetime.stringToLong(model.getEndTimeStr());

		model.setStartTimeStamp(startTimeStamp);
		model.setEndTimeStamp(endTimeStamp);

		model.setTheState(S_TheState.Normal);

		Integer totalCount = tg_ExpForecastDao.findByPage_Size(tg_ExpForecastDao.getQuery_Size(tg_ExpForecastDao.getBasicHQL(), model));

		List<Tg_ExpForecast> tg_ExpForecastList;
		if(totalCount > 0)
		{
			tg_ExpForecastList = tg_ExpForecastDao.findByPage(tg_ExpForecastDao.getQuery(tg_ExpForecastDao.getBasicHQL(), model));
		}
		else
		{
			tg_ExpForecastList = new ArrayList<Tg_ExpForecast>();
		}

		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();


		Properties propertiesExport = exportToExcelUtil.execute(tg_ExpForecastList, Tg_ExpForecastExportExcelTemplate.class,
				"支出预测");

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
