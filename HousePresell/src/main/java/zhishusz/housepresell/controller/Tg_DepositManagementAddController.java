package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import zhishusz.housepresell.database.po.Tg_DepositManagement;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.rebuild.Tg_DepositManagementRebuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import zhishusz.housepresell.controller.form.Tg_DepositManagementForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tg_DepositManagementAddService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller添加操作：存单管理
 * Company：ZhiShuSZ
 * */
@Controller
public class Tg_DepositManagementAddController extends BaseController
{
	@Autowired
	private Tg_DepositManagementAddService service;
	@Autowired
	private Tg_DepositManagementRebuild rebuild;
	
	@RequestMapping(value="/Tg_DepositManagementAdd",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@RequestBody Tg_DepositManagementForm model, HttpServletRequest request)
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
			properties.put("tg_DepositManagement", rebuild.getDetailForApproval((Tg_DepositManagement)(properties.get("tg_DepositManagement"))));
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Tg_DepositManagementAdd", model, properties, jsonStr);
		
		return jsonStr;
	}
}
