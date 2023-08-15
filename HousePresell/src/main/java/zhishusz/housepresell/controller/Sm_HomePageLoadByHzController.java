package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.extra.Tb_b_contractFrom;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Sm_HomePageLoadByHzService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller 首页展示加载-合作机构首页
 * Company：ZhiShuSZ
 */
@Controller
public class Sm_HomePageLoadByHzController extends BaseController
{

	@Autowired
	private Sm_HomePageLoadByHzService service;
	
	@RequestMapping(value = "/Sm_HomePageLoadByHz", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST
	})
	@ResponseBody
	public String execute(@ModelAttribute Tb_b_contractFrom model, HttpServletRequest request)
	{
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion())
		{
		case 19000101:
		{
			properties = service.execute(model);
			break;
		}
		default:
		{
			properties = new MyProperties();
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_ErrorInterfaceVersion);
			break;
		}
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		//super.writeOperateHistory("Sm_FastNavigateUpdate", model, properties, jsonStr);

		return jsonStr;
	}
}
