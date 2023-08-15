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

import zhishusz.housepresell.controller.form.Tg_PjRiskLetterForm;
import zhishusz.housepresell.database.po.Tg_PjRiskLetter;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tg_PjRiskLetterDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgRebuild;
import zhishusz.housepresell.util.rebuild.Tg_PjRiskLetterRebuild;
import zhishusz.housepresell.util.rebuild.Tg_PjRiskLetterReceiverRebuild;

/*
 * Controller详情：项目风险函
 * Company：ZhiShuSZ
 * */
@Controller
public class Tg_PjRiskLetterDetailController extends BaseController
{
	@Autowired
	private Tg_PjRiskLetterDetailService service;
	@Autowired
	private Tg_PjRiskLetterRebuild rebuild;
	@Autowired
	private Tg_PjRiskLetterReceiverRebuild rebuild2;
	@Autowired
	private Sm_AttachmentCfgRebuild rebuild3;
	
	@RequestMapping(value="/Tg_PjRiskLetterDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tg_PjRiskLetterForm model, HttpServletRequest request)
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
			properties.put("tg_PjRiskLetter", rebuild.getDetail((Tg_PjRiskLetter)(properties.get("tg_PjRiskLetter"))));
			
			properties.put("tg_PjRiskLetterReceiverList", rebuild2.getListForAdmin((List)(properties.get("tg_PjRiskLetterReceiverList"))));
			
			properties.put("smAttachmentCfgList", rebuild3.getDetailForAdmin2((List)(properties.get("smAttachmentCfgList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tg_PjRiskLetterDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
