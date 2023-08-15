package zhishusz.housepresell.controller;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import zhishusz.housepresell.util.rebuild.Sm_ApprovalProcess_NodeRebuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_CfgForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.service.Sm_ApprovalProcess_CfgDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_ApprovalProcess_CfgRebuild;

/*
 * Controller详情：审批流-流程配置
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_ApprovalProcess_CfgDetailController extends BaseController
{
	@Autowired
	private Sm_ApprovalProcess_CfgDetailService service;
	@Autowired
	private Sm_ApprovalProcess_CfgRebuild rebuild;
	@Autowired
	private Sm_ApprovalProcess_NodeRebuild nodeRebuild;
	
	@RequestMapping(value="/Sm_ApprovalProcess_CfgDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_ApprovalProcess_CfgForm model, HttpServletRequest request)
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
			properties.put("sm_ApprovalProcess_CfgDetail", rebuild.getDetail((Sm_ApprovalProcess_Cfg)(properties.get("sm_ApprovalProcess_Cfg"))));
			properties.put("sm_ApprovalProcess_NodeList", nodeRebuild.getDetailForAdmin((List)(properties.get("sm_ApprovalProcess_NodeList"))));
			properties.put("sm_ApprovalProcess_ModalNodeList", nodeRebuild.getModalNodeList((List)(properties.get("sm_ApprovalProcess_ModalNodeList"))));
			properties.put("sm_ApprovalProcess_Cfg", rebuild.getDetail((Sm_ApprovalProcess_Cfg)(properties.get("sm_ApprovalProcess_Cfg"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Sm_ApprovalProcess_CfgDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
