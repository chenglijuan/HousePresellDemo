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

import zhishusz.housepresell.controller.form.Sm_Permission_RoleUIForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Sm_Permission_RoleUIListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_Permission_RoleRebuild;

/*
 * Controller列表查询：角色与UI权限对应关系
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_Permission_RoleUIListController extends BaseController
{
	@Autowired
	private Sm_Permission_RoleUIListService service;
	@Autowired
	private Sm_Permission_RoleRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Sm_Permission_RoleUIList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_Permission_RoleUIForm model, HttpServletRequest request)
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
			properties.put("sm_Permission_RoleList", rebuild.getDetailForAdmin((List)(properties.get("sm_Permission_RoleList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Sm_Permission_RoleUIList", model, properties, jsonStr);
		
		return jsonStr;
	}
}