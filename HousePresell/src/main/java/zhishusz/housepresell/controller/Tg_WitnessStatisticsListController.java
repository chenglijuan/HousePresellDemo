package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Tg_WitnessStatisticsForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tg_WitnessStatisticsListService;
import zhishusz.housepresell.service.Tg_WitnessStatisticsbyareaService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller列表查询：见证报告统计表
 * Company：ZhiShuSZ
 * */
@Controller
public class Tg_WitnessStatisticsListController extends BaseController
{
	@Autowired
	private Tg_WitnessStatisticsListService service;
		
	@RequestMapping(value="/Tg_WitnessStatisticsList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tg_WitnessStatisticsForm model, HttpServletRequest request)
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
		
		/*if(MyBackInfo.isSuccess(properties))
		{
			properties.put("empj_UnitInfoList", rebuild.getUnitInfoDetailList((List)(properties.get("empj_UnitInfoList"))));
		}*/
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tg_WitnessStatisticsList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
