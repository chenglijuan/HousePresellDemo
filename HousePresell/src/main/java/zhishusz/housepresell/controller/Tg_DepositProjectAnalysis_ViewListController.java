package zhishusz.housepresell.controller;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Tg_DepositProjectAnalysis_ViewForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tg_DepositProjectAnalysis_ViewCopyListService;
import zhishusz.housepresell.service.Tg_DepositProjectAnalysis_ViewListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tg_DepositProjectAnalysis_ViewRebuild;

/*
 * Controller更新操作：托管项目情况分析表
 * Company：ZhiShuSZ
 * */
@Controller
public class Tg_DepositProjectAnalysis_ViewListController  extends BaseController
{

//	@Autowired
//	private Tg_DepositProjectAnalysis_ViewListService service;
	@Autowired
	private Tg_DepositProjectAnalysis_ViewCopyListService service;
	
	@Autowired
	private Tg_DepositProjectAnalysis_ViewRebuild rebuild;
	
	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@RequestMapping(value="/Tg_DepositProjectAnalysis_ViewList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tg_DepositProjectAnalysis_ViewForm model, HttpServletRequest request)
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
			properties.put("tg_DepositProjectAnalysis_ViewList", rebuild.execute((List)(properties.get("tg_DepositProjectAnalysis_ViewList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
//		super.writeOperateHistory("Tg_DepositProjectAnalysis_ViewList", model, properties, jsonStr);
		
		return jsonStr;
	}
}
