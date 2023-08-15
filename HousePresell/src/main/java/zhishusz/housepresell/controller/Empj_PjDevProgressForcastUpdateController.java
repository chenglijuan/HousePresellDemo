package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zhishusz.housepresell.controller.form.Empj_PjDevProgressForcastForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Empj_PjDevProgressForcastUpdateService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller更新操作：项目-工程进度预测-主表
 * Company：ZhiShuSZ
 * */
@Controller
public class Empj_PjDevProgressForcastUpdateController extends BaseController
{
	@Autowired
	private Empj_PjDevProgressForcastUpdateService service;
	
	@RequestMapping(value="/Empj_PjDevProgressForcastUpdate",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@RequestBody Empj_PjDevProgressForcastForm model, HttpServletRequest request)
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
		
		super.writeOperateHistory("Empj_PjDevProgressForcastUpdate", model, properties, jsonStr);
		
		return jsonStr;
	}
}
