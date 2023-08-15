package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;

import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：楼幢-单元
 * Company：ZhiShuSZ
 * */
@Service
public class Empj_UnitInfoRebuild extends RebuilderBase<Empj_UnitInfo>
{
	@Override
	public Properties getSimpleInfo(Empj_UnitInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());

		Empj_BuildingInfo buildingInfo = object.getBuilding();
		if (buildingInfo != null)
		{
			properties.put("buildingId", buildingInfo.getTableId());
			properties.put("buildingECode", buildingInfo.geteCode());
		}
		properties.put("theName", object.getTheName());
		properties.put("upfloorNumber", object.getUpfloorNumber());
		properties.put("upfloorHouseHoldNumber", object.getUpfloorHouseHoldNumber());
		properties.put("downfloorNumber", object.getDownfloorNumber());
		properties.put("downfloorHouseHoldNumber", object.getDownfloorHouseHoldNumber());
		if (object.getElevatorNumber() !=null && object.getElevatorNumber()>=4)
		{
			properties.put("iselevator", "有");
		}
		else
		{
			properties.put("iselevator", "无");
		}
		properties.put("elevatorNumber", object.getElevatorNumber());
		properties.put("hasSecondaryWaterSupply", object.getHasSecondaryWaterSupply());
		properties.put("remark", object.getRemark());
		
		return properties;
	}

	@Override
	public Properties getDetail(Empj_UnitInfo object)
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
		properties.put("building", object.getBuilding());
		properties.put("buildingId", object.getBuilding().getTableId());
		properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
		properties.put("theName", object.getTheName());
		properties.put("upfloorNumber", object.getUpfloorNumber());
		properties.put("upfloorHouseHoldNumber", object.getUpfloorHouseHoldNumber());
		properties.put("downfloorNumber", object.getDownfloorNumber());
		properties.put("downfloorHouseHoldNumber", object.getDownfloorHouseHoldNumber());
		properties.put("elevatorNumber", object.getElevatorNumber());
		properties.put("hasSecondaryWaterSupply", object.getHasSecondaryWaterSupply());
		properties.put("remark", object.getRemark());
		properties.put("logId", object.getLogId());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Empj_UnitInfo> empj_UnitInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(empj_UnitInfoList != null)
		{
			for(Empj_UnitInfo object:empj_UnitInfoList)
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
				properties.put("building", object.getBuilding());
				properties.put("buildingId", object.getBuilding().getTableId());
				properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
				properties.put("theName", object.getTheName());
				properties.put("upfloorNumber", object.getUpfloorNumber());
				properties.put("upfloorHouseHoldNumber", object.getUpfloorHouseHoldNumber());
				properties.put("downfloorNumber", object.getDownfloorNumber());
				properties.put("downfloorHouseHoldNumber", object.getDownfloorHouseHoldNumber());
				properties.put("elevatorNumber", object.getElevatorNumber());
				properties.put("hasSecondaryWaterSupply", object.getHasSecondaryWaterSupply());
				properties.put("remark", object.getRemark());
				properties.put("logId", object.getLogId());
				
				list.add(properties);
			}
		}
		return list;
	}
	
	
	@SuppressWarnings("rawtypes")
	public List getBuildingList(List<Empj_BuildingInfo> empj_BuildingInfo)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(empj_BuildingInfo != null)
		{
			for(Empj_BuildingInfo object:empj_BuildingInfo)
			{
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());			
				properties.put("theNameOfProject", object.getTheNameOfProject());
				properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
				properties.put("position", object.getPosition());
				
				if(null == object.getUnitNumber() || 0 == object.getUnitNumber())
				{
					properties.put("unitState", 0);
					properties.put("unitNumber", 0);
				}
				else
				{
					properties.put("unitState", 1);
					properties.put("unitNumber", object.getUnitNumber());
				}

								
				list.add(properties);
			}
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public List getUnitInfoDetailList(List<Empj_UnitInfo> empj_UnitInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(empj_UnitInfoList != null)
		{
			for(Empj_UnitInfo object:empj_UnitInfoList)
			{
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());			

				properties.put("theName", object.getTheName());
				properties.put("upfloorNumber", object.getUpfloorNumber());
				properties.put("upfloorHouseHoldNumber", object.getUpfloorHouseHoldNumber());
				properties.put("downfloorNumber", object.getDownfloorNumber());
				properties.put("downfloorHouseHoldNumber", object.getDownfloorHouseHoldNumber());
				if(null == object.getElevatorNumber() || 0 == object.getElevatorNumber())
				{
					properties.put("elevatorNumber", 0);
				}
				else
				{
					properties.put("elevatorNumber", object.getElevatorNumber());
				}
				if(null == object.getHasSecondaryWaterSupply() || !object.getHasSecondaryWaterSupply())
				{
					properties.put("secondaryWaterSupply", 0);
				}
				else
				{
					properties.put("secondaryWaterSupply", 1);
				}
								
				properties.put("remark", object.getRemark());
				
				list.add(properties);
			}
		}
		return list;
	}
	
	
	public Properties getUnitDetail(Empj_UnitInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());			

		properties.put("theName", object.getTheName());
		properties.put("upfloorNumber", object.getUpfloorNumber());
		properties.put("upfloorHouseHoldNumber", object.getUpfloorHouseHoldNumber());
		properties.put("downfloorNumber", object.getDownfloorNumber());
		properties.put("downfloorHouseHoldNumber", object.getDownfloorHouseHoldNumber());
		if(null == object.getElevatorNumber() || 0 == object.getElevatorNumber())
		{
			properties.put("elevatorNumber", 0);
		}
		else
		{
			properties.put("elevatorNumber", object.getElevatorNumber());
		}
		if(null == object.getHasSecondaryWaterSupply() || !object.getHasSecondaryWaterSupply())
		{
			properties.put("secondaryWaterSupply", 0);
		}
		else
		{
			properties.put("secondaryWaterSupply", 1);
		}
		properties.put("remark", object.getRemark());
		
		return properties;
	}
	
	
	@SuppressWarnings("rawtypes")
	public List getHouseList(List<Empj_HouseInfo> empj_HouseInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(empj_HouseInfoList != null)
		{
			for(Empj_HouseInfo object:empj_HouseInfoList)
			{
				Properties properties = new MyProperties();
				
				properties.put("roomId", object.getRoomId());
				properties.put("purpose", object.getPurpose());
				properties.put("actualArea", object.getActualArea());
				properties.put("innerconsArea", object.getInnerconsArea());
								
				list.add(properties);
			}
		}
		return list;
	}

}
