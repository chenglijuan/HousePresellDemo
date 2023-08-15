package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：楼幢-扩展信息
 * Company：ZhiShuSZ
 * */
@Service
public class Empj_BuildingExtendInfoRebuild extends RebuilderBase<Empj_BuildingExtendInfo>
{
	@Override
	public Properties getSimpleInfo(Empj_BuildingExtendInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Empj_BuildingExtendInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("buildingInfo", object.getBuildingInfo());
		properties.put("buildingInfoId", object.getBuildingInfo().getTableId());
		properties.put("presellState", object.getPresellState());
		properties.put("eCodeOfPresell", object.geteCodeOfPresell());
		properties.put("presellDate", object.getPresellDate());
		properties.put("limitState", object.getLimitState());
		properties.put("escrowState", object.getEscrowState());
		properties.put("landMortgageState", object.getLandMortgageState());
		properties.put("landMortgagor", object.getLandMortgagor());
		properties.put("landMortgageAmount", object.getLandMortgageAmount());
		properties.put("isSupportPGS", object.getIsSupportPGS());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Empj_BuildingExtendInfo> empj_BuildingExtendInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(empj_BuildingExtendInfoList != null)
		{
			for(Empj_BuildingExtendInfo object:empj_BuildingExtendInfoList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("buildingInfo", object.getBuildingInfo());
				properties.put("buildingInfoId", object.getBuildingInfo().getTableId());
				properties.put("presellState", object.getPresellState());
				properties.put("eCodeOfPresell", object.geteCodeOfPresell());
				properties.put("presellDate", object.getPresellDate());
				properties.put("limitState", object.getLimitState());
				properties.put("escrowState", object.getEscrowState());
				properties.put("landMortgageState", object.getLandMortgageState());
				properties.put("landMortgagor", object.getLandMortgagor());
				properties.put("landMortgageAmount", object.getLandMortgageAmount());
				properties.put("isSupportPGS", object.getIsSupportPGS());
				
				list.add(properties);
			}
		}
		return list;
	}
}
