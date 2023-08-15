package zhishusz.housepresell.messagequeue.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_BuildingRemainRightLogForm;
import zhishusz.housepresell.messagequeue.producer.MQKey_EventType;
import zhishusz.housepresell.messagequeue.producer.MQKey_OrgType;
import zhishusz.housepresell.service.Tgpf_BuildingRemainRightLogPublicAddService;

/**
 * Created by Dechert on 2018-08-09.
 * Company: zhishusz
 */

@Service
public class AppropriationRetainedConsumer extends BaseConsumer
{
	private MQKey_OrgType type = MQKey_OrgType.SYSTEM;

	@Autowired private Tgpf_BuildingRemainRightLogPublicAddService tgpf_BuildingRemainRightLogPublicAddService;

	@Override public void onInit()
	{
		mqConnectionUtil.getMessage(MQKey_EventType.APPROPRIATION_RETAINED, type, info -> 
		{
			System.out.println("留存权益请求参数" + info);
			Tgpf_BuildingRemainRightLogForm tgpf_BuildingRemainRightLogForm = gson.fromJson(info, Tgpf_BuildingRemainRightLogForm.class);
			tgpf_BuildingRemainRightLogPublicAddService.execute(tgpf_BuildingRemainRightLogForm);
		});
	}
}
