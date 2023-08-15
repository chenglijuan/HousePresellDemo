package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.service.Sm_UserExitService;
import zhishusz.housepresell.util.JsonUtil;

/**
 * 用户退出控制类
 * 清除用户登录保存的session信息
 */
@Controller
public class Sm_UserExitController extends BaseController
{
	@Autowired
	private Sm_UserExitService service;
	@RequestMapping(value="/Sm_UserExit",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_UserForm model, HttpServletRequest request)
	{
		Properties properties = null;
		properties = service.execute(model, request);
//		//清除保存到的session信息
//		HttpSession session = request.getSession();
//		session.invalidate();
//		
//		Properties properties = new MyProperties();
//		properties.put(S_NormalFlag.result, S_NormalFlag.success);
//		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Sm_UserExit", model, properties, jsonStr);
		
		return jsonStr;
	}
}
