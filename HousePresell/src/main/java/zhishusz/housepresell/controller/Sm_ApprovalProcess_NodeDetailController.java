package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import zhishusz.housepresell.util.rebuild.Sm_ApprovalProcess_ConditionRebuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_NodeForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Node;
import zhishusz.housepresell.service.Sm_ApprovalProcess_NodeDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_ApprovalProcess_NodeRebuild;

import  java.util.List;

/*
 * Controller详情：审批流-节点
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_ApprovalProcess_NodeDetailController extends BaseController
{
	@Autowired
	private Sm_ApprovalProcess_NodeDetailService service;
	@Autowired
	private Sm_ApprovalProcess_NodeRebuild rebuild;
	@Autowired
	private Sm_ApprovalProcess_ConditionRebuild conditionRebuild;

	@RequestMapping(value="/Sm_ApprovalProcess_NodeDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_ApprovalProcess_NodeForm model, HttpServletRequest request)
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
			properties.put("sm_ApprovalProcess_Node", rebuild.getDetail((Sm_ApprovalProcess_Node)(properties.get("sm_ApprovalProcess_Node"))));
			properties.put("sm_approvalProcess_conditionList", conditionRebuild.execute((List)(properties.get("sm_approvalProcess_conditionList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Sm_ApprovalProcess_NodeDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
