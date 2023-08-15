package zhishusz.housepresell.util.rebuild;

import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Tg_InterestForecast_View;
import zhishusz.housepresell.util.MyProperties;


@Service
public class Tg_InterestForecast_ViewRebuild extends RebuilderBase<Tg_InterestForecast_View>
{

	@Override
	public Properties getSimpleInfo(Tg_InterestForecast_View object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		properties.put("tableId", object.getTableId());
		properties.put("depositProperty", object.getDepositProperty());
		properties.put("bankName", object.getBankName());
		properties.put("registerTime", object.getRegisterTime());
		properties.put("startDate", object.getStartDate());
		properties.put("principalAmount", object.getPrincipalAmount());
		properties.put("stopDate", object.getStopDate());
		properties.put("storagePeriod", object.getStoragePeriod());
		properties.put("annualRate", object.getAnnualRate());
		properties.put("floatAnnualRate", object.getFloatAnnualRate());
		properties.put("interest", object.getInterest());
		properties.put("openAccountCertificate", object.getOpenAccountCertificate());
		return properties;
	}

	@Override
	public Properties getDetail(Tg_InterestForecast_View object)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
