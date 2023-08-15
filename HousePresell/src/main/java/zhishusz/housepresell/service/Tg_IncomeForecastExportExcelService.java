package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tg_IncomeForecastForm;
import zhishusz.housepresell.database.dao.Tg_IncomeForecastDao;
import zhishusz.housepresell.database.po.Tg_IncomeForecast;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Tg_IncomeForecastExportExcelTemplate;

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
public class Tg_IncomeForecastExportExcelService
{
	private MyDatetime myDatetime = MyDatetime.getInstance();

	@Autowired
	private Tg_IncomeForecastDao tg_IncomeForecastDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_IncomeForecastForm model)
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

		Integer totalCount = tg_IncomeForecastDao.findByPage_Size(tg_IncomeForecastDao.getQuery_Size(tg_IncomeForecastDao.getBasicHQL(), model));

		List<Tg_IncomeForecast> tg_IncomeForecastList;
		if(totalCount > 0)
		{
			tg_IncomeForecastList = tg_IncomeForecastDao.findByPage(tg_IncomeForecastDao.getQuery(tg_IncomeForecastDao.getBasicHQL(), model));
		}
		else
		{
			tg_IncomeForecastList = new ArrayList<Tg_IncomeForecast>();
		}

		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();


		Properties propertiesExport = exportToExcelUtil.execute(tg_IncomeForecastList, Tg_IncomeForecastExportExcelTemplate.class,
				"收入预测");

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
