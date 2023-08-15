package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Tg_HandleTimeLimitConfigForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Tg_HandleTimeLimitConfig;
import zhishusz.housepresell.service.Tg_HandleTimeLimitConfigDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tg_HandleTimeLimitConfigRebuild;

/*
 * Controller详情：办理时限配置表
 * Company：ZhiShuSZ
 * */
@Controller
public class Tg_HandleTimeLimitConfigDetailController extends BaseController
{
	@Autowired
	private Tg_HandleTimeLimitConfigDetailService service;
	@Autowired
	private Tg_HandleTimeLimitConfigRebuild rebuild;
	
	@RequestMapping(value="/Tg_HandleTimeLimitConfigDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tg_HandleTimeLimitConfigForm model, HttpServletRequest request)
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
			properties.put("tg_HandleTimeLimitConfig", rebuild.execute((Tg_HandleTimeLimitConfig)(properties.get("tg_HandleTimeLimitConfig"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tg_HandleTimeLimitConfigDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
