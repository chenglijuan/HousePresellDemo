package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.database.po.Tgpf_FundOverallPlanDetail;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.stereotype.Service;

import java.util.Properties;

/*
 * Rebuilder：用款申请汇总信息
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpf_fundOverallPlanDetailRebuid extends RebuilderBase<Tgpf_FundOverallPlanDetail>
{
	private MyDouble myDouble = MyDouble.getInstance();
	@Override
	public Properties getSimpleInfo(Tgpf_FundOverallPlanDetail object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("theNameOfAccount",object.getTheNameOfAccount());
		properties.put("supervisedBankAccount", object.getSupervisedBankAccount());
		properties.put("appliedAmount", myDouble.pointTOThousandths(object.getAppliedAmount()));

		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_FundOverallPlanDetail object)
	{
		return null;
	}
}
