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
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Sm_ApprovalProcess_PaymentGuaranteeApplyService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_PaymentGuaranteeApplyChildRebuild;
import zhishusz.housepresell.util.rebuild.Empj_PaymentGuaranteeApplyRebuild;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgRebuild;

/*
 * Controller详情审批：审批  三方协议
 * Company：ZhiShuSZ
 */
@Controller
public class Sm_ApprovalProcess_PaymentGuaranteeApplyController extends BaseController
{
	@Autowired
	private Sm_ApprovalProcess_PaymentGuaranteeApplyService service;
	@Autowired
	private Empj_PaymentGuaranteeApplyRebuild rebuild;
	@Autowired
	private Empj_PaymentGuaranteeApplyChildRebuild rebuild2;
	@Autowired
	private Sm_AttachmentCfgRebuild rebuild3;

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@RequestMapping(value = "/Sm_ApprovalProcess_PaymentGuaranteeApply", produces = "text/html;charset=UTF-8", method = {
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

		if(MyBackInfo.isSuccess(properties))
		{
			properties.put("empj_PaymentGuarantee", rebuild.getDetail((Empj_PaymentGuarantee)(properties.get("empj_PaymentGuarantee"))));
			
			properties.put("empj_PaymentGuaranteeChildList", rebuild2.getApplyList((List)(properties.get("empj_PaymentGuaranteeChildList"))));
			
			properties.put("smAttachmentCfgList", rebuild3.getDetailForAdmin2((List)(properties.get("smAttachmentCfgList"))));
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Sm_ApprovalProcess_PaymentGuaranteeApply", model, properties, jsonStr);

		return jsonStr;
	}
}
