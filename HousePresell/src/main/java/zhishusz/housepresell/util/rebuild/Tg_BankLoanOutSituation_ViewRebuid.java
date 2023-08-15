package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Tg_BankLoanOutSituation_View;
import zhishusz.housepresell.util.MyProperties;

@Service
public class Tg_BankLoanOutSituation_ViewRebuid extends RebuilderBase<Tg_BankLoanOutSituation_View>
{

	@Override
	public Properties getSimpleInfo(Tg_BankLoanOutSituation_View object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_BankLoanOutSituation_View object)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_BankLoanOutSituation_View> tg_BankLoanOutSituation_ViewList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_BankLoanOutSituation_ViewList != null)
		{
			for(Tg_BankLoanOutSituation_View object:tg_BankLoanOutSituation_ViewList)
			{
				Properties properties = new MyProperties();
				properties.put("tableId", object.getTableId());
				properties.put("companyName", object.getCompanyName());
				properties.put("projectName", object.getProjectName());
				properties.put("escrowAcount", object.getEscrowAcount());
				properties.put("escrowAcountShortName", object.getEscrowAcountShortName());
				properties.put("loanOutDate", object.getLoanOutDate());
				properties.put("loanAmountOut", object.getLoanAmountOut());
				
				list.add(properties);
			}
		}
		return list;
	}
}
