package zhishusz.housepresell.controller;

import java.util.Properties;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_RefundInfoListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_RefundInfoRebuild;
import zhishusz.housepresell.util.MyBackInfo;

/*
 * Controller列表查询：退房退款-贷款已结清
 * Company：ZhiShuSZ
 */
@Controller
public class Tgpf_RefundInfoListController extends BaseController
{
	@Autowired
	private Tgpf_RefundInfoListService service;
	@Autowired
	private Tgpf_RefundInfoRebuild rebuild;

	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	@RequestMapping(value = "/Tgpf_RefundInfoList", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST
	})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_RefundInfoForm model, HttpServletRequest request)
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
			properties.put("tgpf_RefundInfoList", rebuild.execute((List) (properties.get("tgpf_RefundInfoList"))));
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Tgpf_RefundInfoList", model, properties, jsonStr);

		return jsonStr;
	}
}
