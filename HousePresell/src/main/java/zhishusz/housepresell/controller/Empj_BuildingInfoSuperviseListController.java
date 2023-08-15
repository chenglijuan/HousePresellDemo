package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Tgpj_BankAccountSupervisedForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Empj_BuildingInfoSuperviseListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_BuildingInfoRebuild;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/*
 * Controller列表查询：楼幢-基础信息
 * Company：ZhiShuSZ
 * */
@Controller
public class Empj_BuildingInfoSuperviseListController extends BaseController
{
	@Autowired
	private Empj_BuildingInfoSuperviseListService service;
	@Autowired
	private Empj_BuildingInfoRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Empj_BuildingInfoSuperviseList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpj_BankAccountSupervisedForm model, HttpServletRequest request)
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
			properties.put("empj_BuildingInfoList", rebuild.executeSuperviseBuildingList((List)(properties.get("empj_BuildingInfoList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Empj_BuildingInfoList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
