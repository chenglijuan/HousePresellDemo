package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Tg_Build_View;
import zhishusz.housepresell.util.MyProperties;

@Service
public class Tg_Build_ViewRebuid extends RebuilderBase<Tg_Build_View>
{

	@Override
	public Properties getSimpleInfo(Tg_Build_View object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_Build_View object)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_Build_View> tg_Build_ViewList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_Build_ViewList != null)
		{
			for(Tg_Build_View object:tg_Build_ViewList)
			{
				Properties properties = new MyProperties();
				
				properties.put("billTimeStamp", object.getBillTimeStamp());
				properties.put("companyName", object.getCompanyName());
				properties.put("projectName", object.getProjectName());
				properties.put("eCodeFroMconstruction", object.geteCodeFroMconstruction());
				properties.put("unitCode", object.getUnitCode());
				properties.put("roomId", object.getRoomId());
				properties.put("forEcastArea", object.getForEcastArea());
				properties.put("contractStatus", object.getContractStatus());
				properties.put("contractNo", object.getContractNo());
				properties.put("eCodeOfTripleagreement", object.geteCodeOfTripleagreement());
				properties.put("buyerName", object.getBuyerName());
				properties.put("eCodeOfCertificate", object.geteCodeOfCertificate());
				properties.put("theNameOfCreditor", object.getTheNameOfCreditor());
				properties.put("contractSumPrice", object.getContractSumPrice());
				properties.put("paymentMethod", object.getPaymentMethod());
				properties.put("firstPaymentAmount", object.getFirstPaymentAmount());
				properties.put("loanAmount", object.getLoanAmount());
				properties.put("escrowState", object.getEscrowState());
				properties.put("contractSignDate", object.getContractSignDate());
				properties.put("loanAmountIn", object.getLoanAmountIn());
				properties.put("theAmountOfRetainedequity", object.getTheAmountOfRetainedequity());
				properties.put("statementState", object.getStatementState());
				
				
				list.add(properties);
			}
		}
		return list;
	}
}
