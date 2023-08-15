package zhishusz.housepresell.controller;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Tg_RiskCheckMonthSumForm;
import zhishusz.housepresell.database.po.Tg_RiskCheckMonthSum;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tg_RiskCheckMonthSumDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tg_RiskCheckBusiCodeSumRebuild;
import zhishusz.housepresell.util.rebuild.Tg_RiskCheckMonthSumRebuild;

/*
 * Controller详情：风控例行抽查业务汇总表
 * Company：ZhiShuSZ
 * */
@Controller
public class Tg_RiskCheckMonthSumDetailController extends BaseController
{
	@Autowired
	private Tg_RiskCheckMonthSumDetailService service;
	@Autowired
	private Tg_RiskCheckMonthSumRebuild rebuild;
	@Autowired
	private Tg_RiskCheckBusiCodeSumRebuild tg_RiskCheckBusiCodeSumRebuild;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/Tg_RiskCheckMonthSumDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tg_RiskCheckMonthSumForm model, HttpServletRequest request)
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
			properties.put("tg_RiskCheckMonthSum", rebuild.execute((Tg_RiskCheckMonthSum)(properties.get("tg_RiskCheckMonthSum"))));
			properties.put("tg_RiskCheckBusiCodeSumList", tg_RiskCheckBusiCodeSumRebuild.execute((List)(properties.get("tg_RiskCheckBusiCodeSumList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tg_RiskCheckMonthSumDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
