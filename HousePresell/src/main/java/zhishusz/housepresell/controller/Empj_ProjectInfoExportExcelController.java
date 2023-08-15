package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Empj_ProjectInfoExportExcelService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * ControllerExcel导出：项目信息
 * Company：ZhiShuSZ
 * */
@Controller
@EnableCaching
public class Empj_ProjectInfoExportExcelController extends BaseController
{
	@Autowired
	private Empj_ProjectInfoExportExcelService service;

	@RequestMapping(value="/Empj_ProjectInfoExportExcel",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Empj_ProjectInfoForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch (model.getInterfaceVersion())
		{
			case 19000101:	
			{
				properties = service.execute(model);			
				break;
			}
			default:
				properties = new MyProperties();
				properties.put(S_NormalFlag.result, S_NormalFlag.fail);
				properties.put(S_NormalFlag.info, S_NormalFlag.info_ErrorInterfaceVersion);
				break;
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Empj_ProjectInfoExportExcel", model, properties, jsonStr);
		
		return jsonStr;
	}
	
}
