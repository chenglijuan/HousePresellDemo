package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Empj_UnitInfo;

/*
 * Dao数据库操作：楼幢-单元
 * Company：ZhiShuSZ
 * */
@Repository
public class Empj_UnitInfoDao extends BaseDao<Empj_UnitInfo>
{
	public String getBasicHQL()
    {
    	return "from Empj_UnitInfo where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if eCodeOfBuilding??> and eCodeOfBuilding=:eCodeOfBuilding</#if>"
		+ " <#if theName??> and theName=:theName</#if>"
		+ " <#if upfloorNumber??> and upfloorNumber=:upfloorNumber</#if>"
		+ " <#if upfloorHouseHoldNumber??> and upfloorHouseHoldNumber=:upfloorHouseHoldNumber</#if>"
		+ " <#if downfloorNumber??> and downfloorNumber=:downfloorNumber</#if>"
		+ " <#if downfloorHouseHoldNumber??> and downfloorHouseHoldNumber=:downfloorHouseHoldNumber</#if>"
		+ " <#if elevatorNumber??> and elevatorNumber=:elevatorNumber</#if>"
		+ " <#if hasSecondaryWaterSupply??> and hasSecondaryWaterSupply=:hasSecondaryWaterSupply</#if>"
		+ " <#if remark??> and remark=:remark</#if>"
		+ " <#if building??> and building=:building</#if>"
		+ " <#if logId??> and logId=:logId</#if>"
		+ " <#if buildingIdArr??> and building.tableId in :buildingIdArr</#if>"
		+ " order by theName,eCodeOfBuilding desc";
    }

	public String getExcelListHQL()
	{
		return "from Empj_UnitInfo where 1=1"
				+ " <#if idArr??> and tableId in :idArr</#if>";
	}

	//获取项目下的所有单元
	public String getExcelListForProjectHQL()
	{
		return "from Empj_UnitInfo where 1=1"
				+ " <#if theState??> and theState=:theState</#if>"
				+ " <#if idArr??> and building.tableId in :idArr</#if>"
				+ " order by eCodeOfBuilding , theName";
	}

	//添加模糊查询
	public String getBasicHQLByLIke()
	{
	    return "from Empj_UnitInfo where 1=1"
	    + " <#if keyword??> and ( theName like:keyword or building.eCodeFromConstruction like:keyword )</#if>"
	    + " <#if projectName??> and building.project.theName=:projectName</#if> "
	    + " <#if buildingId??> and building.tableId=:buildingId</#if> "
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (building.cityRegion.tableId in :cityRegionInfoIdArr or userStart.tableId=:userId)</#if>"
//		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (building.project.tableId in :projectInfoIdArr or userStart.tableId=:userId)</#if>"
//		+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> and (building.tableId in :buildingInfoIdIdArr or userStart.tableId=:userId)</#if>"

		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>"
			+ " <#list projectInfoIdArr as projectInfoId>"
				+ " <#if projectInfoId_index == 0> (building.project.tableId = ${projectInfoId?c} )</#if>"
				+ " <#if projectInfoId_index != 0> or (building.project.tableId = ${projectInfoId?c} )</#if>"
			+ " </#list>"
		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> or userStart.tableId=:userId )</#if>"
		+ " </#if>"
		
		+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> and (</#if>"
		+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)>"
			+ " <#list buildingInfoIdIdArr as buildingInfoId>"
				+ " <#if buildingInfoId_index == 0> (building.tableId = ${buildingInfoId?c} )</#if>"
				+ " <#if buildingInfoId_index != 0> or (building.tableId = ${buildingInfoId?c} )</#if>"
				+ " </#list>"
		+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> or userStart.tableId=:userId )</#if>"
		+ " </#if>"
	    
	    + " order by createTimeStamp asc";		
	}

}
