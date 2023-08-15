package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Empj_ProjectInfo_AFForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Empj_ProjectInfo_AF;
import zhishusz.housepresell.service.Empj_ProjectInfo_AFDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_ProjectInfo_AFRebuild;

/*
 * Controller详情：申请表-项目信息变更
 * Company：ZhiShuSZ
 * */
@Controller
public class Empj_ProjectInfo_AFDetailController extends BaseController
{
	@Autowired
	private Empj_ProjectInfo_AFDetailService service;
	@Autowired
	private Empj_ProjectInfo_AFRebuild rebuild;
	
	@RequestMapping(value="/Empj_ProjectInfo_AFDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Empj_ProjectInfo_AFForm model, HttpServletRequest request)
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
			properties.put("empj_ProjectInfo_AF", rebuild.execute((Empj_ProjectInfo_AF)(properties.get("empj_ProjectInfo_AF"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Empj_ProjectInfo_AFDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
