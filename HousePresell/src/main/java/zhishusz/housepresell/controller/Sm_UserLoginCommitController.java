package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Sm_UserLoginCommitService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_UserRebuild;

/**
 * 用户登录确认控制类
 * 挤掉已登录用户
 * @ClassName:  Sm_UserLoginController   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年9月3日 下午1:55:47   
 * @version V1.0 
 *
 */
@Controller
public class Sm_UserLoginCommitController extends BaseController
{
	@Autowired
	private Sm_UserLoginCommitService service;
	@Autowired
	private Sm_UserRebuild rebuild;
	
	@RequestMapping(value="/Sm_UserLoginCommit",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_UserForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				properties = service.execute(model,request);
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
			
			properties.put("sm_User", rebuild.getDetailForLogin((Sm_User)(properties.get("sm_User"))));
			
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Sm_UserLoginCommit", model, properties, jsonStr);
		
		return jsonStr;
	}
}
