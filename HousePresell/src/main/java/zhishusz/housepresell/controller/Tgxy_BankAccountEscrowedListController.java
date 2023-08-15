package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowedView;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgxy_BankAccountEscrowedListService;
import zhishusz.housepresell.service.Tgxy_BankAccountEscrowedViewListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgxy_BankAccountEscrowedRebuild;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/*
 * Controller列表查询：托管账户
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgxy_BankAccountEscrowedListController extends BaseController
{
	@Autowired
	private Tgxy_BankAccountEscrowedListService service;
	@Autowired
	private Tgxy_BankAccountEscrowedRebuild rebuild;

	@Autowired
	private Tgxy_BankAccountEscrowedViewListService viewListService;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Tgxy_BankAccountEscrowedList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgxy_BankAccountEscrowedForm model, HttpServletRequest request)
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
//			properties.put("tgxy_BankAccountEscrowedList", rebuild.execute((List)(properties.get("tgxy_BankAccountEscrowedList"))));
			properties.put("tgxy_BankAccountEscrowedList", rebuild.getDetailForAdmin((List)(properties.get("tgxy_BankAccountEscrowedList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgxy_BankAccountEscrowedList", model, properties, jsonStr);
		
		return jsonStr;
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Tgxy_BankAccountEscrowedViewList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String bankAccountEscrowedViewList(@ModelAttribute Tgxy_BankAccountEscrowedForm model, HttpServletRequest request)
	{
		model.init(request);
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				properties = viewListService.execute(model);
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
			/**本地开发测试环境使用*/
		//	properties.put("tgxy_BankAccountEscrowedList", rebuild.getDetailForAdmin((List)(properties.get("tgxy_BankAccountEscrowedList"))));

			/**生产环境用使用 生产环境需要同步3.0的银行金额数据*/
        	properties.put("tgxy_BankAccountEscrowedList", properties.get("tgxy_BankAccountEscrowedList"));
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Tgxy_BankAccountEscrowedList", model, properties, jsonStr);

		return jsonStr;
	}

}
