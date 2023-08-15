package zhishusz.housepresell.messagequeue.consumer;

import zhishusz.housepresell.util.MQConnectionUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Dechert on 2018-08-09.
 * Company: zhishusz
 */
@Service
public class InitAllConsumer
{
	@Autowired MQConnectionUtil mqConnectionUtil;
	@Autowired DeveloperConsumer developerConsumer;
	@Autowired EarlyConsumer earlyConsumer;
	@Autowired AppropriationRetainedConsumer appropriationRetainedConsumer;

	public void onInit()
	{
		System.out.println("init send");
		mqConnectionUtil.initSend();
		developerConsumer.onInit();
		earlyConsumer.onInit();
		appropriationRetainedConsumer.onInit();
	}
}
