package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Sm_ApprovalProcess_AFUpdateService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller更新操作：审批流-申请单
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_ApprovalProcess_AFUpdateController extends BaseController
{
	@Autowired
	private Sm_ApprovalProcess_AFUpdateService service;
	
	@RequestMapping(value="/Sm_ApprovalProcess_AFUpdate",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_ApprovalProcess_AFForm model, HttpServletRequest request)
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
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Sm_ApprovalProcess_AFUpdate", model, properties, jsonStr);
		
		return jsonStr;
	}
}
