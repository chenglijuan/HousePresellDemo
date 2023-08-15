package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_WorkflowForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Sm_ApprovalProcess_WorkflowRecallService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

/*
 * Controller已办流程：撤回
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_ApprovalProcess_WorkflowRecallController extends BaseController
{
	@Autowired
	private Sm_ApprovalProcess_WorkflowRecallService service;
	
	@RequestMapping(value="/Sm_ApprovalProcess_WorkflowRecall",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_ApprovalProcess_WorkflowForm model, HttpServletRequest request)
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
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Sm_ApprovalProcess_WorkflowRecall", model, properties, jsonStr);
		
		return jsonStr;
	}
}
