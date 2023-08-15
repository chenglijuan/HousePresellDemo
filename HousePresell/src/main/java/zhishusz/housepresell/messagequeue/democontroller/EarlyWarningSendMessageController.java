package zhishusz.housepresell.messagequeue.democontroller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.BaseController;
import zhishusz.housepresell.controller.form.Emmp_BankBranchForm;
import zhishusz.housepresell.controller.form.extra.Tg_EarlyWarningFrom;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.messagequeue.producer.MQKey_EventType;
import zhishusz.housepresell.messagequeue.producer.MQKey_OrgType;
import zhishusz.housepresell.service.SendWarningMessageService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MQConnectionUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller操作：预警MQ触发
 * Company：ZhiShuSZ
 */
@Controller
public class EarlyWarningSendMessageController extends BaseController
{
//	@Autowired
//	MQConnectionUtil mqConnectionUtil;
	@Autowired
	private SendWarningMessageService service;

	/*@RequestMapping(value = "/EarlyWarningSendMessage", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST
	})
	@ResponseBody
	public void execute(@ModelAttribute Tg_EarlyWarningFrom model)
	{
		
		service.execute(model);
		System.out.println(model.getTableId());
		System.out.println(model.getOtherId());
//		mqConnectionUtil.sendMessage(MQKey_EventType.EARLY_WARNING_SENDER, MQKey_OrgType.EARLY, model);

	}*/
	
	@RequestMapping(value="/EarlyWarningSendMessage",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tg_EarlyWarningFrom model, HttpServletRequest request)
	{
//		model.init(request);
		
		Properties properties = null;
//		switch(model.getInterfaceVersion())
//		{
//			case 19000101:
//			{
		properties = service.execute(model);
//				break;
//			}
//			default :
//			{
//				properties = new MyProperties();
//				properties.put(S_NormalFlag.result, S_NormalFlag.fail);
//				properties.put(S_NormalFlag.info, S_NormalFlag.info_ErrorInterfaceVersion);
//				break;
//			}
//		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
//		super.writeOperateHistory("EarlyWarningSendMessage", model, properties, jsonStr);
		
		return jsonStr;
	}
}
