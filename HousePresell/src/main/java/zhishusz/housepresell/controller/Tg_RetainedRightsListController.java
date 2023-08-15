package zhishusz.housepresell.controller;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Tg_RetainedRightsForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tg_RetainedRightsSearchService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;


/*
 * Controller列表查询：留存权益拨付明细
 * create by li
 * 2018/09/18
 */
@Controller
public class Tg_RetainedRightsListController extends BaseController
{	
	@Autowired
	private Tg_RetainedRightsSearchService service;
	
	@RequestMapping(value="/Tg_RetainedRightsList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tg_RetainedRightsForm model)
	{
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
		
        super.writeOperateHistory("Tg_RetainedRightsList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
