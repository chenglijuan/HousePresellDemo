package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Tg_BankLoanInSituation_View;
import zhishusz.housepresell.util.MyProperties;

@Service
public class Tg_BankLoanInSituation_ViewRebuid extends RebuilderBase<Tg_BankLoanInSituation_View>
{

	@Override
	public Properties getSimpleInfo(Tg_BankLoanInSituation_View object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_BankLoanInSituation_View object)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_BankLoanInSituation_View> tg_BankLoanInSituation_ViewList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_BankLoanInSituation_ViewList != null)
		{
			for(Tg_BankLoanInSituation_View object:tg_BankLoanInSituation_ViewList)
			{
				Properties properties = new MyProperties();
				properties.put("tableId", object.getTableId());
				properties.put("companyName", object.getCompanyName());
				properties.put("projectName", object.getProjectName());
				properties.put("escrowAcount", object.getEscrowAcount());
				properties.put("escrowAcountShortName", object.getEscrowAcountShortName());
				properties.put("billTimeStamp", object.getBillTimeStamp());
				properties.put("loanAmountIn", object.getLoanAmountIn());
				
				list.add(properties);
			}
		}
		return list;
	}
}
