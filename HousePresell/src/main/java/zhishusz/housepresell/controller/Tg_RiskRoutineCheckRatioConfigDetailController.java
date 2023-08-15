package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Tg_RiskRoutineCheckRatioConfigForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckRatioConfig;
import zhishusz.housepresell.service.Tg_RiskRoutineCheckRatioConfigDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tg_RiskRoutineCheckRatioConfigRebuild;

/*
 * Controller详情：风控例行抽查比例配置表
 * Company：ZhiShuSZ
 * */
@Controller
public class Tg_RiskRoutineCheckRatioConfigDetailController extends BaseController
{
	@Autowired
	private Tg_RiskRoutineCheckRatioConfigDetailService service;
	@Autowired
	private Tg_RiskRoutineCheckRatioConfigRebuild rebuild;
	
	@RequestMapping(value="/Tg_RiskRoutineCheckRatioConfigDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tg_RiskRoutineCheckRatioConfigForm model, HttpServletRequest request)
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
			properties.put("tg_RiskRoutineCheckRatioConfig", rebuild.execute((Tg_RiskRoutineCheckRatioConfig)(properties.get("tg_RiskRoutineCheckRatioConfig"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tg_RiskRoutineCheckRatioConfigDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
