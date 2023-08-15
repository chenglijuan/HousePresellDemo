package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Tg_LoanProjectCountM_View;
import zhishusz.housepresell.util.MyProperties;

/*
 * Dao数据库操作：托管项目统计表（财务部）
 * Company：ZhiShuSZ
 * */
@Service
public class Tg_LoanProjectCountM_ViewRebuid extends RebuilderBase<Tg_LoanProjectCountM_View>
{

	@Override
	public Properties getSimpleInfo(Tg_LoanProjectCountM_View object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_LoanProjectCountM_View object)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_LoanProjectCountM_View> tg_LoanProjectCountM_ViewList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tg_LoanProjectCountM_ViewList != null)
		{
			for(Tg_LoanProjectCountM_View object:tg_LoanProjectCountM_ViewList)
			{
				Properties properties = new MyProperties();
				properties.put("tableId", object.getTableId());
				properties.put("cityRegion", object.getCityRegion());
				properties.put("companyName", object.getCompanyName());
				properties.put("projectName", object.getProjectName());
				properties.put("upTotalFloorNumber", object.getUpTotalFloorNumber());
				properties.put("contractSigningDate", object.getContractSigningDate());
				properties.put("preSaleCardDate", object.getPreSaleCardDate());
				properties.put("preSalePermits", object.getPreSalePermits());
				properties.put("eCodeOfAgreement", object.geteCodeOfAgreement());
				properties.put("remark", object.getRemark());
				
				list.add(properties);
			}
		}
		return list;
	}
}
