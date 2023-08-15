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

import zhishusz.housepresell.controller.form.Sm_CityRegionInfoForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RangeAuthorizationForm;
import zhishusz.housepresell.database.po.Sm_Permission_RangeAuthorization;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Sm_Permission_RangeAuthorizationForZTDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyInteger;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_CityRegionInfoRebuild;
import zhishusz.housepresell.util.rebuild.Sm_Permission_RangeAuthorizationRebuild;

/*
 * Controller列表查询：正泰机构范围授权详情
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_Permission_RangeAuthorizationForZTDetailController extends BaseController
{
	@Autowired
	private Sm_Permission_RangeAuthorizationForZTDetailService service;
	@Autowired
	private Sm_Permission_RangeAuthorizationRebuild rebuild;
	@Autowired
	private Sm_CityRegionInfoRebuild sm_CityRegionInfoRebuild;
	
	@RequestMapping(value="/Sm_Permission_RangeAuthorizationForZTDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_Permission_RangeAuthorizationForm model, HttpServletRequest request)
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
			properties.put("sm_Permission_RangeAuthorization", rebuild.getDetail((Sm_Permission_RangeAuthorization)(properties.get("sm_Permission_RangeAuthorization"))));
			
			Integer rangeAuthType = MyInteger.getInstance().parse(properties.get("rangeAuthType"));
			Sm_CityRegionInfoForm sm_CityRegionInfoForm = new Sm_CityRegionInfoForm();
			sm_CityRegionInfoForm.setRangeAuthType(rangeAuthType);
			properties.put("sm_CityRegionInfoList", sm_CityRegionInfoRebuild.getDetailForRangeAuth((List)(properties.get("sm_CityRegionInfoList")), sm_CityRegionInfoForm));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Sm_Permission_RangeAuthorizationForZTDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
