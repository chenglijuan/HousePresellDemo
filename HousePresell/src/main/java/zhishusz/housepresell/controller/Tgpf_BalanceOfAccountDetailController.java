package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.service.Tgpf_BalanceOfAccountDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_BalanceOfAccountRebuild;

/*
 * Controller详情：对账列表
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_BalanceOfAccountDetailController extends BaseController
{
	@Autowired
	private Tgpf_BalanceOfAccountDetailService service;
	@Autowired
	private Tgpf_BalanceOfAccountRebuild rebuild;
	
	@RequestMapping(value="/Tgpf_BalanceOfAccountDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_BalanceOfAccountForm model, HttpServletRequest request)
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
			properties.put("tgpf_BalanceOfAccount", rebuild.execute((Tgpf_BalanceOfAccount)(properties.get("tgpf_BalanceOfAccount"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpf_BalanceOfAccountDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
