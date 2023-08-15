package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.service.Sm_AttachmentCfgDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgRebuild;

/*
 * Controller详情：附件配置
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_AttachmentCfgDetailController extends BaseController
{
	@Autowired
	private Sm_AttachmentCfgDetailService service;
	@Autowired
	private Sm_AttachmentCfgRebuild rebuild;
	
	@RequestMapping(value="/Sm_AttachmentCfgDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
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
			properties.put("sm_AttachmentCfg", rebuild.execute((Sm_AttachmentCfg)(properties.get("sm_AttachmentCfg"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Sm_AttachmentCfgDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
