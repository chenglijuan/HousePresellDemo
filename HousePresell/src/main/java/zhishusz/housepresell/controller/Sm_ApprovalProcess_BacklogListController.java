package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Sm_CommonMessageNoticeForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Sm_ApprovalProcess_BacklogListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_CommonMessageNoticeRebuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Properties;

/*
 * Controller列表查询：审批流-审批流程
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_ApprovalProcess_BacklogListController extends BaseController
{
	@Autowired
	private Sm_ApprovalProcess_BacklogListService service;
	@Autowired
	private Sm_CommonMessageNoticeRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Sm_ApprovalProcess_BacklogList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_CommonMessageNoticeForm model, HttpServletRequest request)
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
			properties.put("sm_ApprovalProcess_BacklogList", rebuild.getApproval_BacklogList((List)(properties.get("Sm_CommonMessage"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Sm_ApprovalProcess_BacklogList", model, properties, jsonStr);
		
		return jsonStr;
	}
}