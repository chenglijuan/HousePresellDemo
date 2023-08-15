package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.extra.Tg_EarlyWarningFrom;
import zhishusz.housepresell.service.SendWarningMessageService;
import zhishusz.housepresell.util.JsonUtil;

/**
 * Controller 开发企业修改预警
 * @author ZS004
 * Company：ZhiShuSZ
 */
@Controller
public class Tg_EditCompanyWarningController extends BaseController
{
	
	@Autowired
	private SendWarningMessageService service;
	
	@RequestMapping(value="/Tg_EditCompanyWarning",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tg_EarlyWarningFrom model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties =  service.execute(model);
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tg_EditCompanyWarning", model, properties, jsonStr);
		
		return jsonStr;
	}
}
