package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Emmp_OrgMemberForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Emmp_OrgMember;
import zhishusz.housepresell.service.Emmp_OrgMemberDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Emmp_OrgMemberRebuild;

/*
 * Controller详情：机构成员
 * Company：ZhiShuSZ
 * */
@Controller
public class Emmp_OrgMemberDetailController extends BaseController
{
	@Autowired
	private Emmp_OrgMemberDetailService service;
	@Autowired
	private Emmp_OrgMemberRebuild rebuild;
	
	@RequestMapping(value="/Emmp_OrgMemberDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Emmp_OrgMemberForm model, HttpServletRequest request)
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
			properties.put("emmp_OrgMember", rebuild.execute((Emmp_OrgMember)(properties.get("emmp_OrgMember"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Emmp_OrgMemberDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
