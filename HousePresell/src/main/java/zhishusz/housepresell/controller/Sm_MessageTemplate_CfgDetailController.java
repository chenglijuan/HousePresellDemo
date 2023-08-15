package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Sm_MessageTemplate_CfgForm;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_MessageTemplate_Cfg;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Sm_MessageTemplate_CfgDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_ApprovalProcess_CfgRebuild;
import zhishusz.housepresell.util.rebuild.Sm_MessageTemplate_CfgRebuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

/*
 * Controller详情：审批流-消息模板配置
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_MessageTemplate_CfgDetailController extends BaseController
{
	@Autowired
	private Sm_MessageTemplate_CfgDetailService service;
	@Autowired
	private Sm_MessageTemplate_CfgRebuild rebuild;
	
	@RequestMapping(value="/Sm_MessageTemplate_CfgDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_MessageTemplate_CfgForm model, HttpServletRequest request)
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
			properties.put("sm_MessageTemplate_CfgDetail", rebuild.execute((Sm_MessageTemplate_Cfg)(properties.get("sm_MessageTemplate_CfgDetail"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Sm_MessageTemplate_CfgDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
