package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.service.Sm_UserDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_UserRebuild;

/*
 * Controller详情：系统用户+机构用户
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_UserDetailController extends BaseController
{
	@Autowired
	private Sm_UserDetailService service;
	@Autowired
	private Sm_UserRebuild rebuild;
	
	@RequestMapping(value="/Sm_UserDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_UserForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				Object obj = request.getSession().getAttribute("user");
				Sm_User user = (Sm_User)obj;
				if(!("TEST".equals(user.getTheAccount()) || "jssq".equals(user.getTheAccount()))){
					properties = new MyProperties();
					properties.put(S_NormalFlag.result, S_NormalFlag.fail);
					properties.put(S_NormalFlag.info, "无授权操作");
					break;
				}
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
			properties.put("sm_User", rebuild.getDetail((Sm_User)(properties.get("sm_User"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Sm_UserDetail", model, properties, jsonStr);
		
		return jsonStr;
	}

	@RequestMapping(value="/Sm_UserDetail_copy",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute1(@ModelAttribute Sm_UserForm model, HttpServletRequest request)
	{
		model.init(request);

		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				Object obj = request.getSession().getAttribute("user");
				Sm_User user = (Sm_User)obj;
				//判断是开发企业的账号
				Emmp_CompanyInfo companyInfo = user.getCompany();
				// 如果是开发企业访问 直接返回无授权操作
				if(StringUtils.isBlank(companyInfo.geteCode()) || !"10000".equals(companyInfo.geteCode())){
					properties = new MyProperties();
					properties.put(S_NormalFlag.result, S_NormalFlag.fail);
					properties.put(S_NormalFlag.info, "无授权操作");
					break;
				}
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
			properties.put("sm_User", rebuild.getDetail((Sm_User)(properties.get("sm_User"))));
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Sm_UserDetail", model, properties, jsonStr);

		return jsonStr;
	}

}
