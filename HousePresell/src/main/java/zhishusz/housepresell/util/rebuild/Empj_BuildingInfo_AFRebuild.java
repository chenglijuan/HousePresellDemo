package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Empj_BuildingInfo_AF;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：申请表-楼幢变更
 * Company：ZhiShuSZ
 * */
@Service
public class Empj_BuildingInfo_AFRebuild extends RebuilderBase<Empj_BuildingInfo_AF>
{
	@Override
	public Properties getSimpleInfo(Empj_BuildingInfo_AF object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Empj_BuildingInfo_AF object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("userRecord", object.getUserRecord());
		properties.put("userRecordId", object.getUserRecord().getTableId());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("developCompany", object.getDevelopCompany());
		properties.put("developCompanyId", object.getDevelopCompany().getTableId());
		properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
		properties.put("project", object.getProject());
		properties.put("projectId", object.getProject().getTableId());
		properties.put("theNameOfProject", object.getTheNameOfProject());
		properties.put("eCodeOfProject", object.geteCodeOfProject());
		properties.put("building", object.getBuilding());
		properties.put("buildingId", object.getBuilding().getTableId());
		properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
		properties.put("buildingArea", object.getBuildingArea());
		properties.put("escrowArea", object.getEscrowArea());
		properties.put("deliveryType", object.getDeliveryType());
		properties.put("upfloorNumber", object.getUpfloorNumber());
		properties.put("downfloorNumber", object.getDownfloorNumber());
		properties.put("landMortgageState", object.getLandMortgageState());
		properties.put("landMortgagor", object.getLandMortgagor());
		properties.put("landMortgageAmount", object.getLandMortgageAmount());
		properties.put("remark", object.getRemark());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Empj_BuildingInfo_AF> empj_BuildingInfo_AFList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(empj_BuildingInfo_AFList != null)
		{
			for(Empj_BuildingInfo_AF object:empj_BuildingInfo_AFList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				properties.put("userRecord", object.getUserRecord());
				properties.put("userRecordId", object.getUserRecord().getTableId());
				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				properties.put("developCompany", object.getDevelopCompany());
				properties.put("developCompanyId", object.getDevelopCompany().getTableId());
				properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
				properties.put("project", object.getProject());
				properties.put("projectId", object.getProject().getTableId());
				properties.put("theNameOfProject", object.getTheNameOfProject());
				properties.put("eCodeOfProject", object.geteCodeOfProject());
				properties.put("building", object.getBuilding());
				properties.put("buildingId", object.getBuilding().getTableId());
				properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
				properties.put("buildingArea", object.getBuildingArea());
				properties.put("escrowArea", object.getEscrowArea());
				properties.put("deliveryType", object.getDeliveryType());
				properties.put("upfloorNumber", object.getUpfloorNumber());
				properties.put("downfloorNumber", object.getDownfloorNumber());
				properties.put("landMortgageState", object.getLandMortgageState());
				properties.put("landMortgagor", object.getLandMortgagor());
				properties.put("landMortgageAmount", object.getLandMortgageAmount());
				properties.put("remark", object.getRemark());
				
				list.add(properties);
			}
		}
		return list;
	}
}
