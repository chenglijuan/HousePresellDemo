package zhishusz.housepresell.controller;

import java.util.Properties;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Sm_AttachmentCfgListService;
import zhishusz.housepresell.service.Sm_AttachmentCfgListsService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgRebuild;
import zhishusz.housepresell.util.MyBackInfo;

/*
 * Controller列表查询：附件配置
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_AttachmentCfgListsController extends BaseController
{
	@Autowired
	private Sm_AttachmentCfgListsService service;
	@Autowired
	private Sm_AttachmentCfgRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Sm_AttachmentCfgLists",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_AttachmentCfgForm model, HttpServletRequest request)
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
			properties.put("newAttachmentCfg", rebuild.getDetailForAcceptType((List)(properties.get("newAttachmentCfg"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("newAttachmentCfg", model, properties, jsonStr);
		
		return jsonStr;
	}

	@RequestMapping(value="/Sm_AttachmentCfgListsCopy",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String executeCopy(@ModelAttribute Sm_AttachmentCfgForm model, HttpServletRequest request)
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
			properties.put("newAttachmentCfg", rebuild.getDetailForAcceptType((List)(properties.get("newAttachmentCfg"))));
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("newAttachmentCfg", model, properties, jsonStr);

		return jsonStr;
	}

}
