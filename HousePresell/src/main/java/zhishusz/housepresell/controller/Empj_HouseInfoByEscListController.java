package zhishusz.housepresell.controller;

import java.util.Properties;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Empj_HouseInfoByEscListService;
import zhishusz.housepresell.service.Empj_HouseInfoListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_HouseInfoRebuild;
import zhishusz.housepresell.util.MyBackInfo;

/**
 * 查询指定户室列表
 * @ClassName:  Empj_HouseInfoByEscListController   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年9月4日 下午5:25:43   
 * @version V1.0 
 *
 */
@Controller
public class Empj_HouseInfoByEscListController extends BaseController
{
	@Autowired
	private Empj_HouseInfoByEscListService service;
	@Autowired
	private Empj_HouseInfoRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Empj_HouseInfoByEscList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Empj_HouseInfoForm model, HttpServletRequest request)
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
			properties.put("empj_HouseInfoList", rebuild.execute((List)(properties.get("empj_HouseInfoList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Empj_HouseInfoByEscList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
