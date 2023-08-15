package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFDtlForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.service.Tgpj_BldLimitAmountVer_AFDtlDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpj_BldLimitAmountVer_AFDtlRebuild;

/*
 * Controller详情：受限额度设置
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpj_BldLimitAmountVer_AFDtlDetailController extends BaseController
{
	@Autowired
	private Tgpj_BldLimitAmountVer_AFDtlDetailService service;
	@Autowired
	private Tgpj_BldLimitAmountVer_AFDtlRebuild rebuild;
	
	@RequestMapping(value="/Tgpj_BldLimitAmountVer_AFDtlDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpj_BldLimitAmountVer_AFDtlForm model, HttpServletRequest request)
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
			properties.put("tgpj_BldLimitAmountVer_AFDtl", rebuild.execute((Tgpj_BldLimitAmountVer_AFDtl)(properties.get("tgpj_BldLimitAmountVer_AFDtl"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpj_BldLimitAmountVer_AFDtlDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
