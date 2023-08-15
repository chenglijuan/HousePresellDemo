package zhishusz.housepresell.messagequeue.democontroller;

import zhishusz.housepresell.controller.BaseController;
import zhishusz.housepresell.controller.form.Emmp_BankBranchForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.messagequeue.producer.MQKey_EventType;
import zhishusz.housepresell.messagequeue.producer.MQKey_OrgType;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MQConnectionUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.fileupload.OssServerUtil;
import com.google.gson.Gson;
import com.xiaominfo.oss.sdk.ReceiveMessage;

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
public class Test_SendMessage3Controller extends BaseController
{
	@Autowired MQConnectionUtil mqConnectionUtil;

	@Autowired OssServerUtil ossServerUtil;
	@Autowired Gson gson;
	@RequestMapping(value="/Test_SendMessage3",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Emmp_BankBranchForm model)
	{
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				System.out.println("进入方法！！！！！");
				properties=new Properties();
				mqConnectionUtil.sendMessage(MQKey_EventType.APPROPRIATION_RETAINED, MQKey_OrgType.SYSTEM,"test");


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
