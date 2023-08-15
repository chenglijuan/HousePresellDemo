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

import zhishusz.housepresell.controller.form.Tgpf_AccVoucherForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_AccVoucherDisbursementListService;
import zhishusz.housepresell.service.Tgpf_AccVoucherDisbursementPreAddService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_AccVoucherRebuild;

/*
 * Controller列表查询：推送给财务系统-凭证
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_AccVoucherDisbursementListController extends BaseController
{
	@Autowired
	private Tgpf_AccVoucherDisbursementListService service;
	@Autowired
	private Tgpf_AccVoucherRebuild rebuild;
	@Autowired
	private Tgpf_AccVoucherDisbursementPreAddService accVoucherPreAddService;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Tgpf_AccVoucherDisbursementList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_AccVoucherForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				accVoucherPreAddService.execute(model);				
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
			properties.put("tgpf_AccVoucherList", rebuild.getDisbursementList((List)(properties.get("tgpf_AccVoucherList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
//		super.writeOperateHistory("Tgpf_AccVoucherList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
