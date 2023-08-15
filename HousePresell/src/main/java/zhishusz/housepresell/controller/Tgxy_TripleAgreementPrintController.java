package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.service.Tgxy_TripleAgreementPrintService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgxy_TripleAgreementRebuild;

/*
 * Controller详情：三方协议打印
 * Company：ZhiShuSZ
 */
@Controller
public class Tgxy_TripleAgreementPrintController extends BaseController
{
	@Autowired
	private Tgxy_TripleAgreementPrintService service;
	@Autowired
	private Tgxy_TripleAgreementRebuild rebuild;

	@RequestMapping(value = "/Tgxy_TripleAgreementPrint", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST
	})
	@ResponseBody
	public String execute(@ModelAttribute Tgxy_TripleAgreementForm model, HttpServletRequest request)
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
			properties.put("tgxy_TripleAgreement",
					rebuild.execute((Tgxy_TripleAgreement) (properties.get("tgxy_TripleAgreement"))));

		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Tgxy_TripleAgreementPrint", model, properties, jsonStr);

		return jsonStr;
	}
}
