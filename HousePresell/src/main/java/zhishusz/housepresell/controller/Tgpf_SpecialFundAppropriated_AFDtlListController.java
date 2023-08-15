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
import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFDtlForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_SpecialFundAppropriated_AFDtlListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_SpecialFundAppropriated_AFDtlRebuild;
import zhishusz.housepresell.util.MyBackInfo;

/*
 * Controller列表查询：特殊拨付-申请子表
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_SpecialFundAppropriated_AFDtlListController extends BaseController
{
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDtlListService service;
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDtlRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Tgpf_SpecialFundAppropriated_AFDtlList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_SpecialFundAppropriated_AFDtlForm model, HttpServletRequest request)
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
			properties.put("tgpf_SpecialFundAppropriated_AFDtlList", rebuild.execute((List)(properties.get("tgpf_SpecialFundAppropriated_AFDtlList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpf_SpecialFundAppropriated_AFDtlList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
