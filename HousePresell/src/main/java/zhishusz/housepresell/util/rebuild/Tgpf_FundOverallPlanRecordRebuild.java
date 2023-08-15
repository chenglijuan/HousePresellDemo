package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.database.po.Tgpf_FundOverallPlanRecord;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.stereotype.Service;

import java.util.Properties;

/*
 * Rebuilder：资金统筹
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpf_FundOverallPlanRecordRebuild extends RebuilderBase<Tgpf_FundOverallPlanRecord>
{
	@Override
	public Properties getSimpleInfo(Tgpf_FundOverallPlanRecord object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		//列表页面
		properties.put("tableId", object.getTableId());
		properties.put("payoutAmount",object.getPayoutAmount());
		properties.put("bankAccountEscrowedId",object.getBankAccountEscrowed().getTableId());
		properties.put("bankAccountSupervisedId",object.getBankAccountSupervised().getTableId());

		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_FundOverallPlanRecord object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		//详情页面
		properties.put("tableId", object.getTableId());
		properties.put("eCode", object.geteCode());
		
		return properties;
	}
}
