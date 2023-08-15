package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleDtlForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettleDtl;
import zhishusz.housepresell.service.Tgxy_CoopAgreementSettleDtlDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgxy_CoopAgreementSettleDtlRebuild;

/*
 * Controller详情：三方协议结算-子表
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgxy_CoopAgreementSettleDtlDetailController extends BaseController
{
	@Autowired
	private Tgxy_CoopAgreementSettleDtlDetailService service;
	@Autowired
	private Tgxy_CoopAgreementSettleDtlRebuild rebuild;
	
	@RequestMapping(value="/Tgxy_CoopAgreementSettleDtlDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgxy_CoopAgreementSettleDtlForm model, HttpServletRequest request)
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
			properties.put("tgxy_CoopAgreementSettleDtl", rebuild.execute((Tgxy_CoopAgreementSettleDtl)(properties.get("tgxy_CoopAgreementSettleDtl"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgxy_CoopAgreementSettleDtlDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
