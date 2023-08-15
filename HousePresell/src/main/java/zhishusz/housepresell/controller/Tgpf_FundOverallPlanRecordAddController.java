package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Tgpf_FundOverallPlanForm;
import zhishusz.housepresell.controller.form.Tgpf_FundOverallPlanRecordForm;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlanDetail;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlanRecord;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_FundOverallPlanAddService;
import zhishusz.housepresell.service.Tgpf_FundOverallPlanRecordAddService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

/*
 * Controller添加操作：资金统筹 - 打款记录
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_FundOverallPlanRecordAddController extends BaseController
{
	@Autowired
	private Tgpf_FundOverallPlanRecordAddService service;

	@RequestMapping(value="/Tgpf_FundOverallPlanRecordAdd",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@RequestBody Tgpf_FundOverallPlanRecordForm model, HttpServletRequest request)
	{
//		model.init(request);
		Properties properties = null;
		System.out.println(model.getInterfaceVersion());
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
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpf_FundOverallPlanRecordAdd", model, properties, jsonStr);
		
		return jsonStr;
	}
}
