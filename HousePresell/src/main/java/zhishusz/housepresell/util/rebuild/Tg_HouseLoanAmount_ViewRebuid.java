package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Tg_HouseLoanAmount_View;
import zhishusz.housepresell.util.MyProperties;

@Service
public class Tg_HouseLoanAmount_ViewRebuid extends RebuilderBase<Tg_HouseLoanAmount_View>
{

	@Override
	public Properties getSimpleInfo(Tg_HouseLoanAmount_View object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_HouseLoanAmount_View object)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_HouseLoanAmount_View> tg_HouseLoanAmount_ViewList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_HouseLoanAmount_ViewList != null)
		{
			for(Tg_HouseLoanAmount_View object:tg_HouseLoanAmount_ViewList)
			{
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());
				properties.put("billTimeStamp", object.getBillTimeStamp());
				properties.put("projectName", object.getProjectName());
				properties.put("eCodeFroMconstruction", object.geteCodeFroMconstruction());
				properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());
				properties.put("unitCode", object.getUnitCode());
				properties.put("roomId", object.getRoomId());
				properties.put("forEcastArea", object.getForEcastArea());
				properties.put("contractStatus", object.getContractStatus());
				properties.put("buyerName", object.getBuyerName());
				properties.put("eCodeOfcertificate", object.geteCodeOfcertificate());
				properties.put("contractSumPrice", object.getContractSumPrice());
				properties.put("paymentMethod", object.getPaymentMethod());
				properties.put("loanAmount", object.getLoanAmount());
				properties.put("eCodeOfTripleagreement", object.geteCodeOfTripleagreement());
				properties.put("loanAmountIn", object.getLoanAmountIn());
				properties.put("fundProperty", object.getFundProperty());
				properties.put("reconciliationTSFromOB", object.getReconciliationTSFromOB());
				properties.put("loanBank", object.getLoanBank());
				properties.put("theNameOfCreditor", object.getTheNameOfCreditor());
				
				list.add(properties);
			}
		}
		return list;
	}
}
