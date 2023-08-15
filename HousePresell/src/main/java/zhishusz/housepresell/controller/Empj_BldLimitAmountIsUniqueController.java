package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Empj_BldLimitAmountIsUniqueService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/*
 * Controller用于判断楼幢对应的受限额度是否已经添加（是否是唯一的）
 * Company：ZhiShuSZ
 * Keyword：唯一 受限额度
 * */
@Controller
public class Empj_BldLimitAmountIsUniqueController extends BaseController
{
	@Autowired
	private Empj_BldLimitAmountIsUniqueService service;
	
	@RequestMapping(value="/Empj_BldLimitAmountIsUnique",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Empj_BldLimitAmount_AFForm model, HttpServletRequest request)
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
		
		super.writeOperateHistory("Empj_BldLimitAmount_AFDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
