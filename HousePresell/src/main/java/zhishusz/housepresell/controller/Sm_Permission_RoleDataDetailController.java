package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleDataForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Sm_Permission_RoleData;
import zhishusz.housepresell.service.Sm_Permission_RoleDataDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_Permission_RoleDataRebuild;

/*
 * Controller详情：角色与数据权限对应关系
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_Permission_RoleDataDetailController extends BaseController
{
	@Autowired
	private Sm_Permission_RoleDataDetailService service;
	@Autowired
	private Sm_Permission_RoleDataRebuild rebuild;
	
	@RequestMapping(value="/Sm_Permission_RoleDataDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_Permission_RoleDataForm model, HttpServletRequest request)
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
			properties.put("sm_Permission_RoleData", rebuild.execute((Sm_Permission_RoleData)(properties.get("sm_Permission_RoleData"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Sm_Permission_RoleDataDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
