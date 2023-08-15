package zhishusz.housepresell.controller;

import java.util.Properties;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.util.rebuild.Tgpf_FundAppropriated_AFDtlRebuild;
import zhishusz.housepresell.util.rebuild.Tgpf_FundAppropriated_AFRebuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Tgpf_FundAppropriatedForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_FundAppropriatedDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_FundAppropriatedRebuild;

/*
 * Controller详情：资金拨付
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_FundAppropriatedDetailController extends BaseController
{
	@Autowired
	private Tgpf_FundAppropriatedDetailService service;
	@Autowired
	private Tgpf_FundAppropriatedRebuild rebuild;
	@Autowired
	private Tgpf_FundAppropriated_AFRebuild fundAppropriated_afRebuild;
	@Autowired
	private Tgpf_FundAppropriated_AFDtlRebuild fundAppropriated_afDtlRebuild;
	
	@RequestMapping(value="/Tgpf_FundAppropriatedDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_FundAppropriatedForm model, HttpServletRequest request)
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
			 * 拨付计划
			 */
			properties.put("tgpf_fundAppropriatedList", rebuild.execute((List)properties.get("tgpf_fundAppropriatedList")));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpf_FundAppropriatedDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
