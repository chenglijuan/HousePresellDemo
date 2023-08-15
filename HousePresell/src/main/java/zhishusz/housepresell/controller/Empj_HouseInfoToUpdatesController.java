package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Empj_HouseInfoToUpdatesService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_HouseInfoRebuild;

/*
 * Controller更新操作：楼幢-户室
 * Company：ZhiShuSZ
 * */
@Controller
public class Empj_HouseInfoToUpdatesController extends BaseController
{
	@Autowired
	private Empj_HouseInfoToUpdatesService service;
	@Autowired
	private Empj_HouseInfoRebuild rebuild;
	
	@RequestMapping(value="/Empj_HouseInfoToUpdates",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Empj_HouseInfoForm model, HttpServletRequest request)
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
			properties.put("empj_HouseInfo",rebuild.getSimpleInfoToUpdate((Empj_HouseInfo)(properties.get("empj_HouseInfo"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Empj_HouseInfoToUpdates", model, properties, jsonStr);
		
		return jsonStr;
	}
}
