package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpj_BldLimitAmountVer_AFExportExcelService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpj_BldLimitAmountVer_AFRebuild;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/*
 * ControllerExcel导出：机构信息
 * Company：ZhiShuSZ
 * */
@Controller
@EnableCaching
public class Tgpj_BldLimitAmountVer_AFExportExcelController extends BaseController
{
	@Autowired
	private Tgpj_BldLimitAmountVer_AFExportExcelService service;
	@Autowired
	private Tgpj_BldLimitAmountVer_AFRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Tgpj_BldLimitAmountVer_AFExportExcel",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	@Cacheable(value="Tgpj_BldLimitAmountVer_AF", key="#model.getMD5()")
	public String execute(@ModelAttribute Tgpj_BldLimitAmountVer_AFForm model, HttpServletRequest request)
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
//			properties.put("emmp_CompanyInfoList", rebuild.execute((List)(properties.get("emmp_CompanyInfoList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
//		super.writeOperateHistory("Tgpj_BldLimitAmountVer_AFList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
