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

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Sm_AttachmentCfgUpdateService;
import zhishusz.housepresell.service.Sm_AttachmentCfgYwchangeService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgRebuild;
/*
 * Controller更改业务类型操作：附件配置
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_AttachmentCfgYwchangeController extends BaseController{
	@Autowired
	private Sm_AttachmentCfgYwchangeService service;
	@Autowired
	private Sm_AttachmentCfgRebuild rebuild;
	
	@RequestMapping(value="/Sm_AttachmentCfgYwchange",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_AttachmentCfgForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				//获取业务类型的编号
			    String busiTypeId= request.getParameter("busiTypeId");
				properties = service.execute(model,busiTypeId);
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
			properties.put("sm_AttachmentCfgList", rebuild.getDetailForAcceptType((List<Sm_AttachmentCfg>)(properties.get("sm_AttachmentCfgList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Sm_AttachmentCfgYwchange", model, properties, jsonStr);
		
		return jsonStr;
	}
}
