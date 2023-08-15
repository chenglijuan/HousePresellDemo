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
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Sm_AttachmentCfgListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgRebuild;
import zhishusz.housepresell.util.MyBackInfo;

/*
 * Controller列表查询：附件配置
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_AttachmentCfgListController extends BaseController
{
	@Autowired
	private Sm_AttachmentCfgListService service;
	@Autowired
	private Sm_AttachmentCfgRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Sm_AttachmentCfgList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_AttachmentCfgForm model, HttpServletRequest request)
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
			if ("详情".equals(model.getReqSource()))
			{
				properties.put("sm_AttachmentCfgList", rebuild.getDetailForAdmin2((List)(properties.get("sm_AttachmentCfgList"))));
			}
			else
			{
				properties.put("sm_AttachmentCfgList", rebuild.getListForApproval((List)(properties.get(
						"sm_AttachmentCfgList")), model));
			}
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Sm_AttachmentCfgList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
