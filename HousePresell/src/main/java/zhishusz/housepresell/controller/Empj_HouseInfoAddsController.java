package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Empj_HouseInfoAddsService;
import zhishusz.housepresell.service.Empj_HouseInfoUpdateService;
import zhishusz.housepresell.service.Empj_HouseInfoUpdatesService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller添加操作：楼幢-户室
 * Company：ZhiShuSZ
 * */
@Controller
public class Empj_HouseInfoAddsController extends BaseController
{
	@Autowired
	private Empj_HouseInfoAddsService addservice;
	@Autowired
	private Empj_HouseInfoUpdatesService updateservice;
	
	@RequestMapping(value="/Empj_HouseInfoAdds",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Empj_HouseInfoForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				
				properties = addservice.execute(model);													
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
		
		super.writeOperateHistory("Empj_HouseInfoAdd", model, properties, jsonStr);
		
		return jsonStr;
	}
}
