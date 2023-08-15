package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Tgxy_ContractInfoForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Tgxy_ContractInfo;
import zhishusz.housepresell.service.Tgxy_ContractInfoDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgxy_ContractInfoRebuild;

/*
 * Controller详情：预售系统买卖合同
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgxy_ContractInfoDetailController extends BaseController
{
	@Autowired
	private Tgxy_ContractInfoDetailService service;
	@Autowired
	private Tgxy_ContractInfoRebuild rebuild;
	
	@RequestMapping(value="/Tgxy_ContractInfoDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgxy_ContractInfoForm model, HttpServletRequest request)
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
			properties.put("tgxy_ContractInfo", rebuild.execute((Tgxy_ContractInfo)(properties.get("tgxy_ContractInfo"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgxy_ContractInfoDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
