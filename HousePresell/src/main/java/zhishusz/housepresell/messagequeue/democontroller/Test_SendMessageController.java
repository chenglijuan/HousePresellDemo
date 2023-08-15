package zhishusz.housepresell.messagequeue.democontroller;

import zhishusz.housepresell.controller.BaseController;
import zhishusz.housepresell.controller.form.Emmp_BankBranchForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.messagequeue.producer.MQKey_EventType;
import zhishusz.housepresell.messagequeue.producer.MQKey_OrgType;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MQConnectionUtil;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Properties;

/*
 * Controller操作：测试Demo
 * Company：ZhiShuSZ
 * */
@Controller
public class Test_SendMessageController extends BaseController
{
	@Autowired MQConnectionUtil mqConnectionUtil;
	@RequestMapping(value="/Test_SendMessage",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Emmp_BankBranchForm model)
	{
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				properties=new Properties();
				try
				{
					System.out.println("send message! "+MQKey_EventType.BUYER_SELL_HOUSE.name()+"  "+ MQKey_OrgType.DEVELOPER);
					mqConnectionUtil.sendMessage(MQKey_EventType.BUYER_SELL_HOUSE, MQKey_OrgType.DEVELOPER,model);
				}catch (Exception e){

				}

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
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
//		super.writeOperateHistory("Emmp_BankBranchAdd", model, properties, jsonStr);
		
		return jsonStr;
	}
}
