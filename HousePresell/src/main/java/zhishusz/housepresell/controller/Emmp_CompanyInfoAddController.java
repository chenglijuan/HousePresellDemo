package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.rebuild.Emmp_CompanyInfoRebuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Emmp_CompanyInfoAddService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller添加操作：机构信息
 * Company：ZhiShuSZ
 * */
@Controller
@EnableCaching
public class Emmp_CompanyInfoAddController extends BaseController
{
	@Autowired
	private Emmp_CompanyInfoAddService service;
	@Autowired
	private Emmp_CompanyInfoRebuild rebuild;
	
	@RequestMapping(value="/Emmp_CompanyInfoAdd",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	@CacheEvict(value="Emmp_CompanyInfo", allEntries=true)
	public String execute(@RequestBody Emmp_CompanyInfoForm model, HttpServletRequest request)
	{

		model.init(request);

		Properties properties = null;

		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				System.out.println("进入到service__" + MyDatetime.getInstance().dateToString(System.currentTimeMillis()));
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
		
		super.writeOperateHistory("Emmp_CompanyInfoAdd", model, properties, jsonStr);
		
		return jsonStr;
	}
}
