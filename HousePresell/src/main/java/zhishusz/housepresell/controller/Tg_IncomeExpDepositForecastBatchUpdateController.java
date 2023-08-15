package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Tg_IncomeExpDepositForecastBatchForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tg_IncomeExpDepositForecastBatchUpdateService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/*
 * Controller更新操作：收入预测
 * Company：ZhiShuSZ
 * */
@Controller
public class Tg_IncomeExpDepositForecastBatchUpdateController extends BaseController
{
	@Autowired
	private Tg_IncomeExpDepositForecastBatchUpdateService service;
	
	@RequestMapping(value="/Tg_IncomeExpDepositForecastBatchUpdate",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@RequestBody Tg_IncomeExpDepositForecastBatchForm model, HttpServletRequest request)
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
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tg_IncomeExpDepositForecastUpdate", model, properties, jsonStr);
		
		return jsonStr;
	}
}
