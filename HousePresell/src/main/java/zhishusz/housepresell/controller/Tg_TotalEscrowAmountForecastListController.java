package zhishusz.housepresell.controller;

import java.util.Properties;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import zhishusz.housepresell.controller.form.Tg_TotalEscrowAmountForecastForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tg_TotalEscrowAmountForecastListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tg_TotalEscrowAmountForecastRebuild;
import zhishusz.housepresell.util.MyBackInfo;

/*
 * Controller列表查询：托管总资金的预测结存
 * Company：ZhiShuSZ
 * */
@Controller
public class Tg_TotalEscrowAmountForecastListController extends BaseController
{
	@Autowired
	private Tg_TotalEscrowAmountForecastListService service;
	@Autowired
	private Tg_TotalEscrowAmountForecastRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Tg_TotalEscrowAmountForecastList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tg_TotalEscrowAmountForecastForm model, HttpServletRequest request)
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
			properties.put("tg_TotalEscrowAmountForecastList", rebuild.execute((List)(properties.get("tg_TotalEscrowAmountForecastList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tg_TotalEscrowAmountForecastList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
