package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Sm_CityRegionInfoForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.service.Sm_CityRegionInfoDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_CityRegionInfoRebuild;

/*
 * Controller详情：基础数据-城市区域
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_CityRegionInfoDetailController extends BaseController
{
	@Autowired
	private Sm_CityRegionInfoDetailService service;
	@Autowired
	private Sm_CityRegionInfoRebuild rebuild;
	
	@RequestMapping(value="/Sm_CityRegionInfoDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_CityRegionInfoForm model, HttpServletRequest request)
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
			properties.put("sm_CityRegionInfo", rebuild.execute((Sm_CityRegionInfo)(properties.get("sm_CityRegionInfo"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Sm_CityRegionInfoDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
