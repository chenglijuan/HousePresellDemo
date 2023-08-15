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
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgxy_CoopAgreementListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgxy_CoopAgreementRebuild;
import zhishusz.housepresell.util.MyBackInfo;

/*
 * Controller列表查询：合作协议
 * Company：ZhiShuSZ
 */
@Controller
@EnableCaching
public class Tgxy_CoopAgreementListController extends BaseController
{
	@Autowired
	private Tgxy_CoopAgreementListService service;
	@Autowired
	private Tgxy_CoopAgreementRebuild rebuild;

	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	@RequestMapping(value = "/Tgxy_CoopAgreementList", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST
	})
	@ResponseBody
	@Cacheable(value = "Tgxy_CoopAgreement", key = "#model.getMD5()")
	public String execute(@ModelAttribute Tgxy_CoopAgreementForm model, HttpServletRequest request)
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
			properties.put("tgxy_CoopAgreementList",
					rebuild.execute((List) (properties.get("tgxy_CoopAgreementList"))));
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Tgxy_CoopAgreementList", model, properties, jsonStr);

		return jsonStr;
	}
}
