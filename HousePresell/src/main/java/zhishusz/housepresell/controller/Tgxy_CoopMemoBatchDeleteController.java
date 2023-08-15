package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Tgxy_CoopMemoForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgxy_CoopMemoBatchDeleteService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller批量删除：合作备忘录
 * Company：ZhiShuSZ
 */
@Controller
@EnableCaching
public class Tgxy_CoopMemoBatchDeleteController extends BaseController
{
	@Autowired
	private Tgxy_CoopMemoBatchDeleteService service;

	@RequestMapping(value = "/Tgxy_CoopMemoBatchDelete", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST
	})
	@ResponseBody
	@CacheEvict(value = "Tgxy_CoopMemo", allEntries = true)
	public String execute(@ModelAttribute Tgxy_CoopMemoForm model, HttpServletRequest request)
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

		super.writeOperateHistory("Tgxy_CoopMemoBatchDelete", model, properties, jsonStr);

		return jsonStr;
	}
}
