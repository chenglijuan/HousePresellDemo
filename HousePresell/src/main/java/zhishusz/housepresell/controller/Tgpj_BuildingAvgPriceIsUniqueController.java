package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpj_BuildingAvgPriceIsUniqueService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/*
 * Controller用于判断楼幢对应的备案均价是否已经添加（是否是唯一的）
 * Keyword:楼幢 备案均价 唯一 不可重复
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpj_BuildingAvgPriceIsUniqueController extends BaseController
{
	@Autowired
	private Tgpj_BuildingAvgPriceIsUniqueService service;
//	@Autowired
//	private Tgpj_BuildingAvgPriceRebuild rebuild;
	
	@RequestMapping(value="/Tgpj_BuildingAvgPriceIsUnique",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpj_BuildingAvgPriceForm model, HttpServletRequest request)
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
		
//		if(MyBackInfo.isSuccess(properties))
//		{
//			properties.put("tgpj_BuildingAvgPrice", rebuild.execute((Tgpj_BuildingAvgPrice)(properties.get("tgpj_BuildingAvgPrice"))));
//		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpj_BuildingAvgPriceDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}
