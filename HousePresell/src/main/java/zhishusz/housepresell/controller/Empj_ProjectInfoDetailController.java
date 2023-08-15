package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.service.Empj_ProjectInfoDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_ProjectInfoRebuild;

/*
 * Controller详情：项目信息
 * Company：ZhiShuSZ
 * */
@Controller
public class Empj_ProjectInfoDetailController extends BaseController
{
	@Autowired
	private Empj_ProjectInfoDetailService service;
	@Autowired
	private Empj_ProjectInfoRebuild rebuild;
	
	@RequestMapping(value="/Empj_ProjectInfoDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Empj_ProjectInfoForm model, HttpServletRequest request)
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
			//审批流
			properties.put("empj_ProjectInfo", rebuild.getDetailForApprovalProcess((Empj_ProjectInfo)(properties.get("empj_ProjectInfo"))));

//			if (model.getGetDetailType() != null && "1".equals(model.getGetDetailType()))
//			{
//				//审批流
//				properties.put("empj_ProjectInfo", rebuild.getDetailForApprovalProcess((Empj_ProjectInfo)(properties.get("empj_ProjectInfo"))));
//			}
//			else
//			{
//				properties.put("empj_ProjectInfo", rebuild.execute((Empj_ProjectInfo)(properties.get("empj_ProjectInfo"))));
//			}
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Empj_ProjectInfoDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
