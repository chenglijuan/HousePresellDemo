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
import zhishusz.housepresell.service.Tgpf_BalanceOfAccountPreCompareService;
import zhishusz.housepresell.service.Tgpf_DepositDetailContrastListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_DepositDetailRebuild;

/*
 * Controller列表查询：业务对账-查询根据银行列表分类
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_DepositDetailContrastListController
{
	@Autowired
	private Tgpf_DepositDetailContrastListService service;
	@Autowired
	private Tgpf_BalanceOfAccountPreCompareService preservice;
	
	@Autowired
	private Tgpf_DepositDetailRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/tgpf_DepositDetailContrastList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_BalanceOfAccountForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
//				properties = preservice.execute(model);
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
			properties.put("tgpf_DepositDetailList", rebuild.getDetail((List)(properties.get("tgpf_DepositDetailList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
//		super.writeOperateHistory("Tgpf_DepositDetailContrastListController", model, properties, jsonStr);
		
		return jsonStr;
	}
}
