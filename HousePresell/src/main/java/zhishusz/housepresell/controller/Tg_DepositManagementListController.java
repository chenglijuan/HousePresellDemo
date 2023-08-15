package zhishusz.housepresell.controller;

import java.util.Properties;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import zhishusz.housepresell.controller.form.Tg_DepositManagementForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tg_DepositManagementListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tg_DepositManagementRebuild;
import zhishusz.housepresell.util.MyBackInfo;

/*
 * Controller列表查询：存单管理
 * Company：ZhiShuSZ
 * */
@Controller
public class Tg_DepositManagementListController extends BaseController
{
	@Autowired
	private Tg_DepositManagementListService service;
	@Autowired
	private Tg_DepositManagementRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Tg_DepositManagementList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tg_DepositManagementForm model, HttpServletRequest request)
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
			properties.put("tg_DepositManagementList", rebuild.execute((List)(properties.get("tg_DepositManagementList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tg_DepositManagementList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
