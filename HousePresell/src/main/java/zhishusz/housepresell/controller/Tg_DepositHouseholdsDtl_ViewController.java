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

import zhishusz.housepresell.controller.form.Tg_DepositHouseholdsDtl_ViewForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tg_DepositHouseholdsDtl_ViewListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller列表查询：银行网点(开户行)
 * Company：ZhiShuSZ
 * */
@Controller
public class Tg_DepositHouseholdsDtl_ViewController extends BaseController
{
	@Autowired
	private Tg_DepositHouseholdsDtl_ViewListService service;
//	@Autowired
//	private Tg_DepositHouseholdsDtl_ViewRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Tg_DepositHouseholdsDtl_ViewList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tg_DepositHouseholdsDtl_ViewForm model, HttpServletRequest request)
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
		
//		if(MyBackInfo.isSuccess(properties))
//		{
//			properties.put("Tg_DepositHouseholdsDtl_ViewList", rebuild.getDetailForAdmin((List)(properties.get("Tg_DepositHouseholdsDtl_ViewList"))));
//		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tg_DepositHouseholdsDtl_ViewList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
