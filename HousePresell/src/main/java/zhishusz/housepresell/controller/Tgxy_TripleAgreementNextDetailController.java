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

import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Tgxy_ContractInfo;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.service.Tgxy_TripleAgreementNextDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgRebuild;
import zhishusz.housepresell.util.rebuild.Tgxy_ContractInfoRebuild;
import zhishusz.housepresell.util.rebuild.Tgxy_TripleAgreementRebuild;

/*
 * Controller详情：三方协议 下一条、上一条
 * Company：ZhiShuSZ
 */
@Controller
public class Tgxy_TripleAgreementNextDetailController extends BaseController
{
	@Autowired
	private Tgxy_TripleAgreementNextDetailService service;
	@Autowired
	private Tgxy_TripleAgreementRebuild rebuild;
	@Autowired
	private Tgxy_ContractInfoRebuild rebuild3;
	@Autowired
	private Sm_AttachmentCfgRebuild rebuild4;

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@RequestMapping(value = "/Tgxy_TripleAgreementNextDetail", produces = "text/html;charset=UTF-8", method = {
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

			properties.put("smAttachmentCfgList",
					rebuild4.getDetailForAdmin2((List) (properties.get("smAttachmentCfgList"))));

			properties.put("tgxy_ContractInfo",
					rebuild3.execute((Tgxy_ContractInfo) (properties.get("tgxy_ContractInfo"))));

		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Tgxy_TripleAgreementNextDetail", model, properties, jsonStr);

		return jsonStr;
	}
}
