package zhishusz.housepresell.controller;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopMemoForm;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Tgxy_CoopMemo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Empj_PaymentGuaranteeDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_PaymentGuaranteeListRebuild;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgRebuild;

/*
 * Controller详情:支付保证撤销
 */
@Controller
@EnableCaching
public class Empj_PaymentGuaranteeDetailController extends BaseController
{
	@Autowired
	private Empj_PaymentGuaranteeDetailService service;
	
	@Autowired
	private Empj_PaymentGuaranteeListRebuild rebuild;
	
	@Autowired
	private Sm_AttachmentCfgRebuild rebuild2;
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@RequestMapping(value = "/Empj_PaymentGuaranteeDetail", produces = "text/html;charset=UTF-8", method = {
		RequestMethod.GET, RequestMethod.POST})
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
			properties.put("empj_PaymentGuarantee", rebuild.execute((Empj_PaymentGuarantee) (properties.get("empj_PaymentGuarantee"))));
			
			properties.put("smAttachmentCfgList", rebuild2.getDetailForAdmin2((List)(properties.get("smAttachmentCfgList"))));
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Empj_PaymentGuaranteeDetail", model, properties, jsonStr);

		return jsonStr;
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	}
