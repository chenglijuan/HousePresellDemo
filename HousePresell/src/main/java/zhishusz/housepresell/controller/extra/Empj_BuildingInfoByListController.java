package zhishusz.housepresell.controller.extra;

import java.util.Properties;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.BaseController;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.extra.Empj_BuildingInfoByListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_BuildingInfoRebuild;
import zhishusz.housepresell.util.MyBackInfo;

/**
 * 查询指定楼幢信息列表 
 * ==》已签署合作协议的楼装信息
 * @ClassName:  Empj_BuildingInfoByListController   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年9月07日 上午10:31:51   
 * @version V1.0 
 *
 * 用于新增三方协议的加载
 * Tgxy_EscrowAgreement
 */
@Controller
public class Empj_BuildingInfoByListController extends BaseController
{
	@Autowired
	private Empj_BuildingInfoByListService service;
	@Autowired
	private Empj_BuildingInfoRebuild rebuild;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/Empj_BuildingInfoByList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
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
			properties.put("empj_BuildingInfoList", rebuild.getByList((List<Empj_BuildingInfo>)(properties.get("empj_BuildingInfoList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Empj_BuildingInfoByList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
