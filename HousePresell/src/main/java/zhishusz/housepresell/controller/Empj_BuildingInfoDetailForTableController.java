package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.service.Empj_BuildingInfoDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_BuildingInfoRebuild;
import zhishusz.housepresell.util.rebuild.Tgpj_BuildingAccountRebuild;

/*
 * Controller详情：楼盘表楼幢详情
 * Company：ZhiShuSZ
 * */
@Controller
public class Empj_BuildingInfoDetailForTableController extends BaseController
{
	@Autowired
	private Empj_BuildingInfoDetailService service;
	@Autowired
	private Empj_BuildingInfoRebuild rebuild;
	@Autowired
	private Tgpj_BuildingAccountRebuild rebuild2;
	
	@RequestMapping(value="/Empj_BuildingInfoDetailForTable",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Empj_BuildingInfoForm model, HttpServletRequest request)
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
			properties.put("empj_BuildingInfo", rebuild.executeForBuildingTableDetail((Empj_BuildingInfo)(properties.get("empj_BuildingInfo"))));
			
			properties.put("tgpj_BuildingAccount", rebuild2.getDetailForSpecialFund((Tgpj_BuildingAccount)(properties.get("tgpj_BuildingAccount"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties, 7);
		
		super.writeOperateHistory("Empj_BuildingInfoDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
