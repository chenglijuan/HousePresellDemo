package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Emmp_CompanyAgencyAddService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Emmp_CompanyInfoRebuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

/*
 * Controller添加操作：代理公司
 * Company：ZhiShuSZ
 * */
@Controller
@EnableCaching
public class Emmp_CompanyAgencyAddController extends BaseController
{
	@Autowired
	private Emmp_CompanyAgencyAddService service;
	@Autowired
	private Emmp_CompanyInfoRebuild rebuild;
	
	@RequestMapping(value="/Emmp_CompanyAgencyAdd",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
//	@CacheEvict(value="Emmp_CompanyAgency", allEntries=true)
	public String execute(@RequestBody Emmp_CompanyInfoForm model, HttpServletRequest request)
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
			properties.put("emmp_CompanyInfo", rebuild.execute((Emmp_CompanyInfo)(properties.get("emmp_CompanyInfo"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Emmp_CompanyAgencyAdd", model, properties, jsonStr);
		
		return jsonStr;
	}
}
