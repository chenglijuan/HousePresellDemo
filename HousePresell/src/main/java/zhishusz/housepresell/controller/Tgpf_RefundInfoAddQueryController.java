package zhishusz.housepresell.controller;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_RefundInfoAddQueryService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpj_BankAccountSupervisedRebuild;

/**
 * Controller 新增时根据输入的三方协议号查询相关信息：退房退款-贷款已结清
 * Company：ZhiShuSZ
 * @author lei.sun
 * @date 2018年8月8日13:42:38
 */

@Controller
public class Tgpf_RefundInfoAddQueryController extends BaseController{

	@Autowired
	private Tgpf_RefundInfoAddQueryService service;
	
	@Autowired
	private Tgpj_BankAccountSupervisedRebuild rebuild;
	
	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@RequestMapping(value="/Tgpf_RefundInfoAddQuery",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_RefundInfoForm model)
	{
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
			properties.put("bankAccountSupervisedList", rebuild.executeForSelectList2((List)(properties.get("bankAccountSupervisedList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpf_RefundInfoAddQuery", model, properties, jsonStr);
		
		return jsonStr;
	}
}
