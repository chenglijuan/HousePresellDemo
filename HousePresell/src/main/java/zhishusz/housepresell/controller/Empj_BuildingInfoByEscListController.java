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
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Empj_BuildingInfoByEscListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_BuildingInfoRebuild;
import zhishusz.housepresell.util.MyBackInfo;

/**
 * 查询指定楼幢信息列表
 * @ClassName:  Empj_BuildingInfoByEscListController   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年8月30日 下午7:58:13   
 * @version V1.0 
 *
 * 用于新增托管合作协议加载：
 * Tgxy_EscrowAgreement
 */
@Controller
public class Empj_BuildingInfoByEscListController extends BaseController
{
	@Autowired
	private Empj_BuildingInfoByEscListService service;
	@Autowired
	private Empj_BuildingInfoRebuild rebuild;
	
	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@RequestMapping(value="/Empj_BuildingInfoByEscList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Empj_BuildingInfoForm model, HttpServletRequest request)
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
			properties.put("empj_BuildingInfoList", rebuild.executeSuperviseBuildingList((List)(properties.get("empj_BuildingInfoList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Empj_BuildingInfoList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
