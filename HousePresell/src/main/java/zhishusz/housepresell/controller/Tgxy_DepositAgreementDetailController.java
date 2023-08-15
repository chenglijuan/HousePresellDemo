package zhishusz.housepresell.controller;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Tgxy_DepositAgreementForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Tgxy_DepositAgreement;
import zhishusz.housepresell.service.Tgxy_DepositAgreementDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgRebuild;
import zhishusz.housepresell.util.rebuild.Tgxy_DepositAgreementRebuild;

/*
 * Controller详情：协定存款协议
 * Company：ZhiShuSZ
 */
@Controller
public class Tgxy_DepositAgreementDetailController extends BaseController
{
	@Autowired
	private Tgxy_DepositAgreementDetailService service;
	@Autowired
	private Tgxy_DepositAgreementRebuild rebuild;
	@Autowired
	private Sm_AttachmentCfgRebuild rebuild3;

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@RequestMapping(value = "/Tgxy_DepositAgreementDetail", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST
	})
	@ResponseBody
	public String execute(@ModelAttribute Tgxy_DepositAgreementForm model, HttpServletRequest request)
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
			properties.put("tgxy_DepositAgreement",
					rebuild.execute((Tgxy_DepositAgreement) (properties.get("tgxy_DepositAgreement"))));

			properties.put("smAttachmentCfgList",
					rebuild3.getDetailForAdmin2((List) (properties.get("smAttachmentCfgList"))));
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Tgxy_DepositAgreementDetail", model, properties, jsonStr);

		return jsonStr;
	}
}
