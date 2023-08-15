package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Tgpf_FundOverallPlanForm;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlan;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_FundOverallPlanDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_FundAppropriatedRebuild;
import zhishusz.housepresell.util.rebuild.Tgpf_FundAppropriated_AFDtlRebuild;
import zhishusz.housepresell.util.rebuild.Tgpf_FundOverallPlanRebuild;
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
 * Controller详情：资金统筹
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_FundOverallPlanDetailController extends BaseController
{
	@Autowired
	private Tgpf_FundOverallPlanDetailService service;
	@Autowired
	private Tgpf_FundOverallPlanRebuild rebuild;
	@Autowired
	private Tgpf_FundAppropriated_AFDtlRebuild fundAppropriated_afDtlRebuild;
	@Autowired
	private Tgpf_FundAppropriatedRebuild fundAppropriatedRebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Tgpf_FundOverallPlanDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_FundOverallPlanForm model, HttpServletRequest request)
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
			/**
			 * 统筹主表信息
			 */
			properties.put("tgpf_FundOverallPlan",rebuild.getDetail((Tgpf_FundOverallPlan) properties.get("tgpf_FundOverallPlan")));

			/**
			 * 用款申请楼幢信息（用款申请明细表）
			 */
			properties.put("tgpf_fundAppropriated_afDtlList", fundAppropriated_afDtlRebuild.execute((List)properties.get("tgpf_fundAppropriated_afDtlList")));

			/**
			 * 拨付统筹信息
			 */
			properties.put("tgpf_FundAppropriatedList", fundAppropriatedRebuild.execute((List)properties.get("tgpf_FundAppropriatedList")));

		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpf_FundOverallPlanDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
