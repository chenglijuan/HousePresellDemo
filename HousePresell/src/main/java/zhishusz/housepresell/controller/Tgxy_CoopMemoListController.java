package zhishusz.housepresell.controller;

import java.util.Properties;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import zhishusz.housepresell.controller.form.Tgxy_CoopMemoForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgxy_CoopMemoListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgxy_CoopMemoRebuild;
import zhishusz.housepresell.util.MyBackInfo;

/*
 * Controller列表查询：合作备忘录
 * Company：ZhiShuSZ
 */
@Controller
@EnableCaching
public class Tgxy_CoopMemoListController extends BaseController
{
	@Autowired
	private Tgxy_CoopMemoListService service;
	@Autowired
	private Tgxy_CoopMemoRebuild rebuild;

	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	@RequestMapping(value = "/Tgxy_CoopMemoList", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST
	})
	@ResponseBody
	@Cacheable(value = "Tgxy_CoopMemo", key = "#model.getMD5()")
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

		if (MyBackInfo.isSuccess(properties))
		{
			properties.put("tgxy_CoopMemoList", rebuild.execute((List) (properties.get("tgxy_CoopMemoList"))));
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Tgxy_CoopMemoList", model, properties, jsonStr);

		return jsonStr;
	}
}
