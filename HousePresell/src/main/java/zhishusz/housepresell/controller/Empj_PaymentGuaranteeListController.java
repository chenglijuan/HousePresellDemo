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

import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Empj_PaymentGuaranteeListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_PaymentGuaranteeListRebuild;

@Controller
public class Empj_PaymentGuaranteeListController extends BaseController
{

	@Autowired
	private Empj_PaymentGuaranteeListService service;
	@Autowired
	private Empj_PaymentGuaranteeListRebuild rebuild;

	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	@RequestMapping(value = "/Empj_PaymentGuaranteeList", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST
	})
	@ResponseBody
	public String execute(@ModelAttribute Empj_PaymentGuaranteeForm model, HttpServletRequest request)
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
			properties.put("empj_PaymentGuaranteeList",
					rebuild.execute((List) (properties.get("empj_PaymentGuaranteeList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Empj_PaymentGuaranteeList", model, properties, jsonStr);

		return jsonStr;
	}
}
