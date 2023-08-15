package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Tg_LoanAmountStatement_View;
import zhishusz.housepresell.util.MyProperties;

/*
 * Dao数据库操作：托管现金流量表
 * Company：ZhiShuSZ
 * */
@Service
public class Tg_LoanAmountStatement_ViewRebuid extends RebuilderBase<Tg_LoanAmountStatement_View>
{

	@Override
	public Properties getSimpleInfo(Tg_LoanAmountStatement_View object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_LoanAmountStatement_View object)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_LoanAmountStatement_View> tg_LoanAmountStatement_ViewList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_LoanAmountStatement_ViewList != null)
		{
			for(Tg_LoanAmountStatement_View object:tg_LoanAmountStatement_ViewList)
			{
				Properties properties = new MyProperties();
				properties.put("tableId", object.getTableId());
				properties.put("billTimeStamp", object.getBillTimeStamp());
				properties.put("lastAmount", object.getLastAmount());
				properties.put("loanAmountIn", object.getLoanAmountIn());
				properties.put("depositReceipt", object.getDepositReceipt());
				properties.put("payoutAmount", object.getPayoutAmount());
				properties.put("depositExpire", object.getDepositExpire());
				properties.put("currentBalance", object.getCurrentBalance());
				
				list.add(properties);
			}
		}
		return list;
	}
}
