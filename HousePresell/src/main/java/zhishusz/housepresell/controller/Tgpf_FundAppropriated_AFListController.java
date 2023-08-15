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
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFForm;
import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_FundAppropriated_AFListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_FundAppropriated_AFRebuild;
import zhishusz.housepresell.util.MyBackInfo;

/*
 * Controller列表查询：申请-用款-主表
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_FundAppropriated_AFListController extends BaseController
{
	@Autowired
	private Tgpf_FundAppropriated_AFListService service;
	@Autowired
	private Tgpf_FundAppropriated_AFRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Tgpf_FundAppropriated_AFList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
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
			properties.put("tgpf_FundAppropriated_AFList", rebuild.execute((List)(properties.get("tgpf_FundAppropriated_AFList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpf_FundAppropriated_AFList", model, properties, jsonStr);
		
		return jsonStr;
	}

	@RequestMapping(value = "/Tgpf_FundAppropriatedExport", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String reportExportExecute(@ModelAttribute Tgpf_FundAppropriated_AFForm model, HttpServletRequest request) {
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion()) {
			case 19000101: {
				properties = service.reportExportExecute(model);
				break;
			}
			default: {
				properties = new MyProperties();
				properties.put(S_NormalFlag.result, S_NormalFlag.fail);
				properties.put(S_NormalFlag.info, S_NormalFlag.info_ErrorInterfaceVersion);
				break;
			}
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		return jsonStr;
	}
}
