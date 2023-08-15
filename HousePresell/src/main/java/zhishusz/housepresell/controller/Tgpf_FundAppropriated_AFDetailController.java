package zhishusz.housepresell.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import zhishusz.housepresell.util.rebuild.Tgpf_FundAppropriated_AFDtlRebuild;
import zhishusz.housepresell.util.rebuild.Tgpf_FundOverallPlanRebuild;
import zhishusz.housepresell.util.rebuild.Tgpf_fundOverallPlanDetailRebuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.service.Tgpf_FundAppropriated_AFDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_FundAppropriated_AFRebuild;

/*
 * Controller详情：申请-用款-主表
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_FundAppropriated_AFDetailController extends BaseController
{
	@Autowired
	private Tgpf_FundAppropriated_AFDetailService service;
	@Autowired
	private Tgpf_FundAppropriated_AFRebuild fundAppropriated_afRebuild;
	@Autowired
	private Tgpf_FundAppropriated_AFDtlRebuild fundAppropriated_afDtlRebuild;
	@Autowired
	private Tgpf_fundOverallPlanDetailRebuid fundOverallPlanDetailRebuid;
	
	@RequestMapping(value="/Tgpf_FundAppropriated_AFDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_FundAppropriated_AFForm model, HttpServletRequest request)
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
			 * 用款基本信息（用款申请主表）
			 */
			properties.put("tgpf_FundAppropriated_AF", fundAppropriated_afRebuild.getDetail((Tgpf_FundAppropriated_AF)(properties.get("tgpf_FundAppropriated_AF"))));
			/**
			 * 用款申请楼幢信息（用款申请明细表）
			 */
			properties.put("tgpf_fundAppropriated_afDtlList", fundAppropriated_afDtlRebuild.execute((List)properties.get("tgpf_fundAppropriated_afDtlList")));
			/**
			 * 用款申请汇总信息
			 */
			properties.put("tgpf_fundOverallPlanDetailList", fundOverallPlanDetailRebuid.execute((List)properties.get("tgpf_fundOverallPlanDetailList")));
		}
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpf_FundAppropriated_AFDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
