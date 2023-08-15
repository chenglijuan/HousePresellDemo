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

import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementVerMngForm;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementVerMng;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Sm_ApprovalProcess_CoopAgreementVerMngDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgRebuild;
import zhishusz.housepresell.util.rebuild.Tgxy_CoopAgreementVerMngRebuild;

/*
 * Controller详情：审批流-合作协议版本管理
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_ApprovalProcess_CoopAgreementVerMngDetailController extends BaseController{

	@Autowired
	private Sm_ApprovalProcess_CoopAgreementVerMngDetailService service;
	@Autowired
	private Tgxy_CoopAgreementVerMngRebuild rebuild;
	@Autowired
	private Sm_AttachmentCfgRebuild rebuild3;

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@RequestMapping(value = "/Sm_ApprovalProcess_Tgxy_CoopAgreementVerMngDetail", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST
	})
	@ResponseBody
	public String execute(@ModelAttribute Tgxy_CoopAgreementVerMngForm model, HttpServletRequest request)
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
			properties.put("Tgxy_CoopAgreementVerMng",
					rebuild.getSimpleInfo2((Tgxy_CoopAgreementVerMng) (properties.get("tgxy_CoopAgreementVerMng"))));

			properties.put("smAttachmentCfgList",
					rebuild3.getDetailForAdmin2((List) (properties.get("smAttachmentCfgList"))));
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Sm_ApprovalProcess_TripleAgreementVerMngDetail", model, properties, jsonStr);

		return jsonStr;
	}
}
