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

import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Sm_ApprovalProcess_RefundInfoDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgRebuild;
import zhishusz.housepresell.util.rebuild.Sm_Permission_RoleRebuild;
import zhishusz.housepresell.util.rebuild.Sm_Permission_RoleUserRebuild;
import zhishusz.housepresell.util.rebuild.Tgpf_RefundInfoRebuild;

/*
 * Controller详情：退房退款详情 - 审批流
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_ApprovalProcess_RefundInfoDetailController extends BaseController
{
	@Autowired
	private Tgpf_RefundInfoRebuild rebuild;
	@Autowired
	private Sm_AttachmentCfgRebuild rebuild3;
	@Autowired
	private Sm_ApprovalProcess_RefundInfoDetailService service;
	@Autowired
	private Sm_Permission_RoleRebuild rebuild2;
	
	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@RequestMapping(value="/Sm_ApprovalProcess_RefundInfoDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_RefundInfoForm model, HttpServletRequest request)
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
			properties.put("tgpf_RefundInfo", rebuild.execute((Tgpf_RefundInfo)(properties.get("tgpf_RefundInfo"))));
			
			properties.put("sm_Permission_Role", rebuild2.getDetail3((Sm_Permission_Role)(properties.get("sm_Permission_Role"))));
			
			properties.put("smAttachmentCfgList", rebuild3.getDetailForAdmin2((List)(properties.get("smAttachmentCfgList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Sm_ApprovalProcess_RefundInfoDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
