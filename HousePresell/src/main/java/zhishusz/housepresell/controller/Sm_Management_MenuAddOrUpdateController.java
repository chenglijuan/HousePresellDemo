package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Sm_Permission_UIResourceForm;
import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Sm_Management_MenuAddOrUpdateService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_Permission_UIResourceRebuild;

/*
 * Controller列表查询：添加或编辑菜单项
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_Management_MenuAddOrUpdateController extends BaseController
{
	@Autowired
	private Sm_Management_MenuAddOrUpdateService service;
	@Autowired
	private Sm_Permission_UIResourceRebuild rebuild;
	
	@RequestMapping(value="/Sm_Management_MenuAddOrUpdate",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_Permission_UIResourceForm model, HttpServletRequest request)
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
			properties.put("sm_Permission_UIResource_New", rebuild.getSimpleDetailForManagement_Menu_Add((Sm_Permission_UIResource)(properties.get("sm_Permission_UIResource_New"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Sm_Management_MenuAddOrUpdate", model, properties, jsonStr);
		
		return jsonStr;
	}
}
