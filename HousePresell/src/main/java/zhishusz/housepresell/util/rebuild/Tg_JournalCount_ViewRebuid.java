package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Tg_JournalCount_View;
import zhishusz.housepresell.util.MyProperties;

/*
 * Dao数据库操作：日记账统计表
 * Company：ZhiShuSZ
 * */
@Service
public class Tg_JournalCount_ViewRebuid extends RebuilderBase<Tg_JournalCount_View>
{

	@Override
	public Properties getSimpleInfo(Tg_JournalCount_View object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_JournalCount_View object)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_JournalCount_View> tg_JournalCount_ViewList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_JournalCount_ViewList != null)
		{
			for(Tg_JournalCount_View object:tg_JournalCount_ViewList)
			{
				Properties properties = new MyProperties();
				properties.put("tableId", object.getTableId());
				properties.put("loanInDate", object.getLoanInDate());
				properties.put("escrowAcountName", object.getEscrowAcountName());
				properties.put("tradeCount", object.getTradeCount());
				properties.put("totalTradeAmount", object.getTotalTradeAmount());
				properties.put("aToatlLoanAmoutCount", object.getaToatlLoanAmoutCount());
				properties.put("aToatlLoanAmout", object.getaToatlLoanAmout());
				properties.put("bToatlLoanAmoutCount", object.getbToatlLoanAmoutCount());
				properties.put("bToatlLoanAmout", object.getbToatlLoanAmout());
				properties.put("oToatlLoanAmoutCount", object.getoToatlLoanAmoutCount());
				properties.put("oToatlLoanAmout", object.getoToatlLoanAmout());
				properties.put("aFristToatlLoanAmoutCount", object.getaFristToatlLoanAmoutCount());
				properties.put("aFristToatlLoanAmout", object.getaFristToatlLoanAmout());
				properties.put("aToBusinessToatlLoanAmoutCount", object.getaToBusinessToatlLoanAmoutCount());
				properties.put("aToBusinessToatlLoanAmout", object.getaToBusinessToatlLoanAmout());
				
				list.add(properties);
			}
		}
		return list;
	}
}
