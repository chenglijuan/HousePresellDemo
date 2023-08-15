package zhishusz.housepresell.controller;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Tg_PjRiskAssessmentForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Tg_PjRiskAssessment;
import zhishusz.housepresell.service.Tg_PjRiskAssessmentDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgRebuild;
import zhishusz.housepresell.util.rebuild.Tg_PjRiskAssessmentRebuild;

/*
 * Controller详情：项目风险评估
 * Company：ZhiShuSZ
 * */
@Controller
public class Tg_PjRiskAssessmentDetailController extends BaseController
{
	@Autowired
	private Tg_PjRiskAssessmentDetailService service;
	@Autowired
	private Tg_PjRiskAssessmentRebuild rebuild;
	@Autowired
	private Sm_AttachmentCfgRebuild rebuild3;
	
	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@RequestMapping(value="/Tg_PjRiskAssessmentDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tg_PjRiskAssessmentForm model, HttpServletRequest request)
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
			properties.put("tg_PjRiskAssessment", rebuild.execute((Tg_PjRiskAssessment)(properties.get("tg_PjRiskAssessment"))));
			
			properties.put("smAttachmentCfgList", rebuild3.getDetailForAdmin2((List)(properties.get("smAttachmentCfgList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tg_PjRiskAssessmentDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
