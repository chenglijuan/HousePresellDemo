package zhishusz.housepresell.controller;

import java.util.Properties;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_WorkflowForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.CommonService;
import zhishusz.housepresell.service.Sm_ApprovalProcess_WorkflowListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_ApprovalProcess_WorkflowRebuild;
import zhishusz.housepresell.util.MyBackInfo;

/*
 * Controller列表查询：审批流-审批流程
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_ApprovalProcess_WorkflowListController extends BaseController
{
	@Autowired
	private Sm_ApprovalProcess_WorkflowListService service;
	@Autowired
	private Sm_ApprovalProcess_WorkflowRebuild rebuild;
	/*@Autowired
	private CommonService commonService;*/
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Sm_ApprovalProcess_WorkflowList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_ApprovalProcess_WorkflowForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				//commonService.updateAfStateExecute();
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
			properties.put("sm_ApprovalProcess_WorkflowList", rebuild.getDetailForAdmin((List)(properties.get("sm_ApprovalProcess_WorkflowList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Sm_ApprovalProcess_WorkflowList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
