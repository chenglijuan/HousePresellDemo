package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Tg_BuildPayout_View;
import zhishusz.housepresell.util.MyProperties;

@Service
public class Tg_BuildPayout_ViewRebuid extends RebuilderBase<Tg_BuildPayout_View>
{

	@Override
	public Properties getSimpleInfo(Tg_BuildPayout_View object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_BuildPayout_View object)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_BuildPayout_View> tg_BuildPayout_ViewList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_BuildPayout_ViewList != null)
		{
			for(Tg_BuildPayout_View object:tg_BuildPayout_ViewList)
			{
				Properties properties = new MyProperties();
				
				properties.put("companyName", object.getCompanyName());
				properties.put("projectName", object.getProjectName());
				properties.put("eCodeFroMconstruction", object.geteCodeFroMconstruction());
				properties.put("currentFigureProgress", object.getCurrentFigureProgress());
				properties.put("eCodeFromPayoutBill", object.geteCodeFromPayoutBill());
				properties.put("currentApplyPayoutAmount", object.getCurrentApplyPayoutAmount());
				properties.put("currentPayoutAmount", object.getCurrentPayoutAmount());
				properties.put("payoutDate", object.getPayoutDate());
				properties.put("payoutBank", object.getPayoutBank());
				properties.put("payoutBankAccount", object.getPayoutBankAccount());
				
				list.add(properties);
			}
		}
		return list;
	}
}
