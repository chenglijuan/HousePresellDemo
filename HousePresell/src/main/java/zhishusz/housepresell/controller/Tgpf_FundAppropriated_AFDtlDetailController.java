package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFDtlForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AFDtl;
import zhishusz.housepresell.service.Tgpf_FundAppropriated_AFDtlDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_FundAppropriated_AFDtlRebuild;

/*
 * Controller详情：申请-用款-明细
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_FundAppropriated_AFDtlDetailController extends BaseController
{
	@Autowired
	private Tgpf_FundAppropriated_AFDtlDetailService service;
	@Autowired
	private Tgpf_FundAppropriated_AFDtlRebuild rebuild;
	
	@RequestMapping(value="/Tgpf_FundAppropriated_AFDtlDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_FundAppropriated_AFDtlForm model, HttpServletRequest request)
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
			properties.put("tgpf_FundAppropriated_AFDtl", rebuild.execute((Tgpf_FundAppropriated_AFDtl) (properties.get("tgpf_FundAppropriated_AFDtl"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpf_FundAppropriated_AFDtlDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
