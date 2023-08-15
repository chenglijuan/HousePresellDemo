package zhishusz.housepresell.messagequeue.consumer;

import zhishusz.housepresell.controller.form.extra.Tg_EarlyWarningFrom;
import zhishusz.housepresell.messagequeue.producer.MQKey_EventType;
import zhishusz.housepresell.messagequeue.producer.MQKey_OrgType;
import zhishusz.housepresell.service.SendWarningMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 预警
 * 
 * @ClassName: EarlyConsumer
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月25日 下午6:06:04
 * @version V1.0
 *
 */
@Service
public class EarlyConsumer extends BaseConsumer
{
	private MQKey_OrgType type = MQKey_OrgType.EARLY;
	@Autowired
	private SendWarningMessageService service;

	@Override
	public void onInit()
	{
		mqConnectionUtil.getMessage(MQKey_EventType.EARLY_WARNING_SENDER, type, info -> {
			System.out.println("=============调用MQ ："+info+"============");
			Tg_EarlyWarningFrom model = gson.fromJson((String) info, Tg_EarlyWarningFrom.class);
			System.out.println(model);
			service.execute(model);
		});
	}
}
