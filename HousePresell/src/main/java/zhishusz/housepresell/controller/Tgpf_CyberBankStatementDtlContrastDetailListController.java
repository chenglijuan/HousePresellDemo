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

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_CyberBankStatementDtlContrastDetailListService;
import zhishusz.housepresell.service.Tgpf_CyberBankStatementDtlPreCompareDBService;
import zhishusz.housepresell.service.Tgpf_CyberBankStatementDtlPreCompareService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_CyberBankStatementDtlRebuild;

/*
 * Controller列表查询：网银对账-详情列表页
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_CyberBankStatementDtlContrastDetailListController
{

	@Autowired
	private Tgpf_CyberBankStatementDtlContrastDetailListService service;
	/*@Autowired
	private Tgpf_CyberBankStatementDtlPreCompareService preCompareService;	*/
	
	@Autowired
	private Tgpf_CyberBankStatementDtlPreCompareDBService preCompareService;	
	
	@Autowired
	private Tgpf_CyberBankStatementDtlRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/tgpf_CyberBankStatementDtlContrastDetailList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_BalanceOfAccountForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				preCompareService.execute(model);
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
			properties.put("tgpf_CyberBankStatementDtlList", rebuild.getDetailList((List)(properties.get("tgpf_CyberBankStatementDtlList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
//		super.writeOperateHistory("Tgpf_CyberBankStatementDtlList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
