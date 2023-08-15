package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Tg_RiskRoutineMonthSumForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tg_RiskRoutineMonthSumDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tg_RiskCheckBusiCodeSumRebuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Properties;

/*
 * Controller列表查询：风控月度小结详情
 * Company：ZhiShuSZ
 * */
@Controller
public class Tg_RiskRoutineMonthSumDetailController extends BaseController
{
	@Autowired
	private Tg_RiskRoutineMonthSumDetailService service;
	@Autowired
	private Tg_RiskCheckBusiCodeSumRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Tg_RiskRoutineMonthSumDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tg_RiskRoutineMonthSumForm model, HttpServletRequest request)
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
			properties.put("tg_RiskRoutineMonthSumDetail", rebuild.executeRiskRoutineMonthSumDetail((List)(properties.get("tg_RiskRoutineMonthSumDetail"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tg_WorkTimeLimitCheckList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
