package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Empj_UnitInfoForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Empj_UnitInfoListForProjectService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_UnitInfoRebuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Properties;

/*
 * Controller列表查询：楼幢-单元
 * Company：ZhiShuSZ
 * */
@Controller
public class Empj_UnitInfoListForProjectController extends BaseController
{
	@Autowired
	private Empj_UnitInfoListForProjectService service;
	@Autowired
	private Empj_UnitInfoRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Empj_UnitInfoListForProject",produces="text/html;charset=UTF-8",method={RequestMethod.GET,
			RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Empj_UnitInfoForm model, HttpServletRequest request)
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
			properties.put("empj_UnitInfoList", rebuild.execute((List)(properties.get("empj_UnitInfoList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Empj_UnitInfoList", model, properties, jsonStr);
		
		return jsonStr;
	}


}
