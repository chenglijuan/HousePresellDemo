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
import zhishusz.housepresell.controller.form.Tgpf_DayEndBalancingForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_BalanceOfAccountBusContrastAddService;
import zhishusz.housepresell.service.Tgpf_DayEndBalancingListService;
import zhishusz.housepresell.service.Tgpf_DayEndBalancingPreAddService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_DayEndBalancingRebuild;

/*
 * Controller列表查询：业务对账-日终结算
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_DayEndBalancingListController extends BaseController
{
	@Autowired
	private Tgpf_DayEndBalancingListService service;
	@Autowired
	private Tgpf_DayEndBalancingRebuild rebuild;
	@Autowired
	private Tgpf_BalanceOfAccountBusContrastAddService contrastAddService;
	@Autowired
	private Tgpf_DayEndBalancingPreAddService preAddService;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Tgpf_DayEndBalancingList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_DayEndBalancingForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				Tgpf_BalanceOfAccountForm tgpf_BalanceOfAccountForm = new Tgpf_BalanceOfAccountForm();
				tgpf_BalanceOfAccountForm.setBillTimeStamp(model.getBillTimeStamp());
				contrastAddService.execute(tgpf_BalanceOfAccountForm);
				preAddService.execute(model);
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
			properties.put("tgpf_DayEndBalancingList", rebuild.getDetailForAdmin((List)(properties.get("tgpf_DayEndBalancingList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpf_DayEndBalancingList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
