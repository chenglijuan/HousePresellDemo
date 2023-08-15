package zhishusz.housepresell.controller.extra;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.BaseController;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.extra.Tg_EarlyWarningFrom;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.service.extra.Empj_BuildingInfoByDetailService;
import zhishusz.housepresell.service.extra.Tg_HouseEarlyForOneService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_BuildingInfoRebuild;

/**
 * 户室预警消息1推送触发控制类
 * @ClassName:  Tg_HouseEarlyForOneController   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年9月16日 下午5:56:00   
 * @version V1.0 
 *
 */
@Controller
public class Tg_HouseEarlyForOneController extends BaseController
{
	@Autowired
	private Tg_HouseEarlyForOneService service;
	
	@RequestMapping(value="/Tg_HouseEarlyForOne",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tg_EarlyWarningFrom model, HttpServletRequest request)
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
		
		super.writeOperateHistory("Tg_HouseEarlyForOneController", model, properties, jsonStr);
		
		return jsonStr;
	}
}
