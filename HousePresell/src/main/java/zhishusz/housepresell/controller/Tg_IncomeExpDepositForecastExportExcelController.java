package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Tg_IncomeExpDepositForecastForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tg_IncomeExpDepositForecastExportExcelService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tg_IncomeExpDepositForecastRebuild;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/*
 * Controller列表查询：收入预测
 * Company：ZhiShuSZ
 * */
@Controller
public class Tg_IncomeExpDepositForecastExportExcelController extends BaseController
{
	@Autowired
	private Tg_IncomeExpDepositForecastExportExcelService service;
	@Autowired
	private Tg_IncomeExpDepositForecastRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Tg_IncomeExpDepositForecastExportExcel",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tg_IncomeExpDepositForecastForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				properties = service.execute(model);
				break;
			}
			default :
			{
				properties = new MyProperties();
				properties.put(S_NormalFlag.result, S_NormalFlag.fail);
				properties.put(S_NormalFlag.info, S_NormalFlag.info_ErrorInterfaceVersion);
				break;
			}
		}
		
		if(MyBackInfo.isSuccess(properties))
		{
//			properties.put("tg_IncomeExpDepositForecastList", rebuild.execute((List)(properties.get("tg_IncomeExpDepositForecastList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
//		super.writeOperateHistory("Tg_IncomeExpDepositForecastList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
