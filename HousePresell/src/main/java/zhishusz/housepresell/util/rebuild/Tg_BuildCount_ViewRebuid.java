package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Tg_BuildCount_View;
import zhishusz.housepresell.util.MyProperties;

@Service
public class Tg_BuildCount_ViewRebuid extends RebuilderBase<Tg_BuildCount_View>
{

	@Override
	public Properties getSimpleInfo(Tg_BuildCount_View object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_BuildCount_View object)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_BuildCount_View> tg_BuildCount_ViewList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_BuildCount_ViewList != null)
		{
			for(Tg_BuildCount_View object:tg_BuildCount_ViewList)
			{
				Properties properties = new MyProperties();
				
				properties.put("billTimeStamp", object.getBillTimeStamp());
				properties.put("companyName", object.getCompanyName());
				properties.put("projectName", object.getProjectName());
				properties.put("eCodeFromConstruction", object.geteCodeFroMconstruction());
				properties.put("bankName", object.getBankName());
				properties.put("income", object.getIncome());
				properties.put("payout", object.getPayout());
				properties.put("balance", object.getBalance());
				properties.put("commercialLoan", object.getCommercialLoan());
				properties.put("accumulationFund", object.getAccumulationFund());
				
				list.add(properties);
			}
		}
		return list;
	}
}
