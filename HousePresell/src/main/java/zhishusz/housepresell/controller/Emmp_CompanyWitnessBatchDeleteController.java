package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Emmp_CompanyWitnessBatchDeleteService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

/*
 * Controller批量删除：机构信息
 * Company：ZhiShuSZ
 * */
@Controller
@EnableCaching
public class Emmp_CompanyWitnessBatchDeleteController extends BaseController
{
	@Autowired
	private Emmp_CompanyWitnessBatchDeleteService service;
	
	@RequestMapping(value="/Emmp_CompanyWitnessBatchDelete",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
//	@CacheEvict(value="Emmp_CompanyWitness", allEntries=true)
	public String execute(@ModelAttribute Emmp_CompanyInfoForm model, HttpServletRequest request)
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
		
		super.writeOperateHistory("Emmp_CompanyWitnessBatchDelete", model, properties, jsonStr);
		
		return jsonStr;
	}
}
