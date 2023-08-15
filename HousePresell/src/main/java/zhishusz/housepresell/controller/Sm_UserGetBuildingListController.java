package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Sm_UserGetBuildingListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_UserRebuild;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Dechert on 2018-09-05.
 * Company: zhishusz
 * 通过用户获得楼幢信息的方法
 */
@Controller
public class Sm_UserGetBuildingListController extends BaseController
{
	@Autowired
	private Sm_UserGetBuildingListService service;
	@Autowired
	private Sm_UserRebuild rebuild;

	@RequestMapping(value="/Sm_UserGetBuildingList",produces="text/html;charset=UTF-8",method={ RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_UserForm model, HttpServletRequest request)
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

		if (MyBackInfo.isSuccess(properties))
		{
			properties.put("buildingList",
					rebuild.userGetBuildingList((List<Empj_BuildingInfo>) (properties.get("buildingList"))));
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Sm_UserGetBuildingList", model, properties, jsonStr);

		return jsonStr;
	}
}
