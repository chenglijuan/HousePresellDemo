package zhishusz.housepresell.controller;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import zhishusz.housepresell.database.po.Empj_PjDevProgressForcastDtl;
import zhishusz.housepresell.util.rebuild.Empj_PjDevProgressForcastDtlRebuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Empj_PjDevProgressForcastForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Empj_PjDevProgressForcast;
import zhishusz.housepresell.service.Empj_PjDevProgressForcastDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_PjDevProgressForcastRebuild;

/*
 * Controller详情：项目-工程进度预测-主表
 * Company：ZhiShuSZ
 * */
@Controller
public class Empj_PjDevProgressForcastDetailController extends BaseController
{
	@Autowired
	private Empj_PjDevProgressForcastDetailService service;
	@Autowired
	private Empj_PjDevProgressForcastRebuild rebuild;
	@Autowired
	private Empj_PjDevProgressForcastDtlRebuild dtlRebuild;
	
	@RequestMapping(value="/Empj_PjDevProgressForcastDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Empj_PjDevProgressForcastForm model, HttpServletRequest request)
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
			properties.put("empj_PjDevProgressForcast", rebuild.getDetail((Empj_PjDevProgressForcast)(properties.get("empj_PjDevProgressForcast"))));
			properties.put("empj_pjDevProgressForcastDtlList", dtlRebuild.execute((List) (properties.get("empj_pjDevProgressForcastDtlList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Empj_PjDevProgressForcastDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
