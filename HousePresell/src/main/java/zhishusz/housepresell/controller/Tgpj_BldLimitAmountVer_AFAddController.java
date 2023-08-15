package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpj_BldLimitAmountVer_AFAddService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/*
 * Controller添加操作：版本管理-受限节点设置
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpj_BldLimitAmountVer_AFAddController extends BaseController
{
	@Autowired
	private Tgpj_BldLimitAmountVer_AFAddService service;
	
	@RequestMapping(value="/Tgpj_BldLimitAmountVer_AFAdd",produces="text/html;charset=UTF-8",method={RequestMethod.POST})
	@ResponseBody
	public String execute(@RequestBody Tgpj_BldLimitAmountVer_AFForm model, HttpServletRequest request)
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
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpj_BldLimitAmountVer_AFAdd", model, properties, jsonStr);
		
		return jsonStr;
	}
}
