package zhishusz.housepresell.messagequeue.consumer;

import zhishusz.housepresell.controller.form.Emmp_BankBranchForm;
import zhishusz.housepresell.messagequeue.producer.MQKey_EventType;
import zhishusz.housepresell.messagequeue.producer.MQKey_OrgType;
import zhishusz.housepresell.service.Emmp_BankBranchListService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Dechert on 2018-08-09.
 * Company: zhishusz
 */

@Service
public class DeveloperConsumer extends BaseConsumer
{
	private MQKey_OrgType type = MQKey_OrgType.DEVELOPER;

	@Autowired private Emmp_BankBranchListService emmp_bankBranchListService;

	@Override public void onInit()
	{
		//xsz by 2019-12-12 17:09:56
		//初步怀疑此步骤是导致开户行银行莫名成倍递增的原因
		
		/*mqConnectionUtil.getMessage(MQKey_EventType.BUYER_SELL_HOUSE, type, info -> {
			System.out.println("DeveloperListener BUYER_SELL_HOUSE get info is " + info);
			Emmp_BankBranchForm emmp_bankBranchForm = gson.fromJson(info, Emmp_BankBranchForm.class);
			emmp_bankBranchListService.execute(emmp_bankBranchForm);
		});

		mqConnectionUtil.getMessage(MQKey_EventType.BUYER_BUY_HOUSE, type, info -> {
			System.out.println("DeveloperListener BUYER_BUY_HOUSE get info is " + info);
		});*/
	}
}
