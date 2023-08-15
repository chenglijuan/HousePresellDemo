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

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_BalanceOfAccountBusContrastListService;
import zhishusz.housepresell.service.Tgpf_BalanceOfAccountPreAddService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_BalanceOfAccountRebuild;

/*
 * Controller列表查询：网银对账
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_BalanceOfAccountBusContrastListController
{
	@Autowired
	private Tgpf_BalanceOfAccountBusContrastListService service;
	//	@Autowired
//	private Tgpf_BalanceOfAccountBusContrastAddService addService;
	@Autowired
	private Tgpf_BalanceOfAccountPreAddService addService;

	@Autowired
	private Tgpf_BalanceOfAccountRebuild rebuild;

	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Tgpf_BalanceOfAccountBusContrastList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_BalanceOfAccountForm model, HttpServletRequest request)
	{
		model.init(request);

		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				addService.execute(model);
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
			properties.put("tgpf_BalanceOfAccountList", rebuild.getDetailForAdmin((List)(properties.get("tgpf_BalanceOfAccountList"))));
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

//		super.writeOperateHistory("Tgpf_BalanceOfAccountList", model, properties, jsonStr);

		return jsonStr;
	}
}
