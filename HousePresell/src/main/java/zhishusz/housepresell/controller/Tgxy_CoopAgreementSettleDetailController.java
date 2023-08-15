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

import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleForm;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettle;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgxy_CoopAgreementSettleDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgRebuild;
import zhishusz.housepresell.util.rebuild.Tgxy_CoopAgreementSettleDtlRebuild;
import zhishusz.housepresell.util.rebuild.Tgxy_CoopAgreementSettleRebuild;

/*
 * Controller详情：三方协议结算-主表
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgxy_CoopAgreementSettleDetailController extends BaseController
{
	@Autowired
	private Tgxy_CoopAgreementSettleDetailService service;
	@Autowired
	private Tgxy_CoopAgreementSettleRebuild rebuild;
	@Autowired
	private Tgxy_CoopAgreementSettleDtlRebuild rebuild2;
	@Autowired
	private Sm_AttachmentCfgRebuild rebuild3;
	
	@RequestMapping(value="/Tgxy_CoopAgreementSettleDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgxy_CoopAgreementSettleForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				properties = service.execute(model);
				break;
			}
			default :
			{
				properties = new MyProperties();
				properties.put(S_NormalFlag.result, S_NormalFlag.fail);
				properties.put(S_NormalFlag.info, S_NormalFlag.info_ErrorInterfaceVersion);
				break;
			}
		}
		
		if(MyBackInfo.isSuccess(properties))
		{
			properties.put("tgxy_CoopAgreementSettle", rebuild.getDetail((Tgxy_CoopAgreementSettle)(properties.get("tgxy_CoopAgreementSettle"))));
			
			properties.put("tgxy_CoopAgreementSettleDtlList", rebuild2.getDetailForAdmin((List)(properties.get("tgxy_CoopAgreementSettleDtlList"))));
			
			properties.put("smAttachmentCfgList", rebuild3.getDetailForAdmin2((List)(properties.get("smAttachmentCfgList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
//		super.writeOperateHistory("Tgxy_CoopAgreementSettleDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
