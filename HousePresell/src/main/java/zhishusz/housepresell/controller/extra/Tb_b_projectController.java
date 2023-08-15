package zhishusz.housepresell.controller.extra;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.BaseController;
import zhishusz.housepresell.controller.form.extra.Tb_b_projectForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.extra.Tb_b_projectService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/**
 * 中间库-项目取数
 * @ClassName:  Tb_b_projectController   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年9月25日 下午11:21:06   
 * @version V1.0 
 *
 */
@Controller
public class Tb_b_projectController extends BaseController
{
	@Autowired
	private Tb_b_projectService service;
	
	@RequestMapping(value="/Tb_b_project",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tb_b_projectForm model, HttpServletRequest request)
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
		
		super.writeOperateHistory("Tb_b_companyList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
