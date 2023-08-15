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
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tg_PjRiskAssessmentAddQueryService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Emmp_CompanyInfoRebuild;
import zhishusz.housepresell.util.rebuild.Empj_ProjectInfoRebuild;
import zhishusz.housepresell.util.rebuild.Sm_CityRegionInfoRebuild;

/*
 * Controller添加操作：项目风险评估-新增查询
 * Company：ZhiShuSZ
 * */
@Controller
public class Tg_PjRiskAssessmentAddQueryController extends BaseController
{
	@Autowired
	private Tg_PjRiskAssessmentAddQueryService service;
	@Autowired
	private Empj_ProjectInfoRebuild rebuild;
	@Autowired
	private Sm_CityRegionInfoRebuild rebuild2;
	@Autowired
	private Emmp_CompanyInfoRebuild rebuild3;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/Tg_PjRiskAssessmentAddQuery",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
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
			properties.put("projectList", rebuild.executeForHomePageList((List<Empj_ProjectInfo>) properties.get("projectList")));
			
			properties.put("cityRegion", rebuild2.getSelectForQuery((Sm_CityRegionInfo) properties.get("cityRegion")));
			
			properties.put("companyInfo", rebuild3.getSelectForQuery((Emmp_CompanyInfo) properties.get("companyInfo")));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tg_PjRiskAssessmentAddQuery", model, properties, jsonStr);
		
		return jsonStr;
	}
}
