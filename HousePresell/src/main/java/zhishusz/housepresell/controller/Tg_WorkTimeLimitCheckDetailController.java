package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Tg_WorkTimeLimitCheckForm;
import zhishusz.housepresell.database.po.Tg_HandleTimeLimitConfig;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tg_WorkTimeLimitCheckDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tg_HandleTimeLimitConfigRebuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

/*
 * Controller列表查询：工作时限检查详情
 * Company：ZhiShuSZ
 * */
@Controller
public class Tg_WorkTimeLimitCheckDetailController extends BaseController
{
	@Autowired
	private Tg_WorkTimeLimitCheckDetailService service;
	@Autowired
	private Tg_HandleTimeLimitConfigRebuild rebuild;

	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Tg_WorkTimeLimitCheckDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tg_WorkTimeLimitCheckForm model, HttpServletRequest request)
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
			properties.put("handleTimeLimitConfig", rebuild.execute((Tg_HandleTimeLimitConfig)(properties.get("handleTimeLimitConfig"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tg_WorkTimeLimitCheckList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
