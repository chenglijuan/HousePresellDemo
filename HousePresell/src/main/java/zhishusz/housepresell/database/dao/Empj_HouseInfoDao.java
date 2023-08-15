package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Empj_HouseInfo;

/*
 * Dao数据库操作：楼幢-户室
 * Company：ZhiShuSZ
 */
@Repository
public class Empj_HouseInfoDao extends BaseDao<Empj_HouseInfo>
{
	public String getBasicHQL()
	{
		return "from Empj_HouseInfo where 1=1" + " <#if theState??> and theState=:theState</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>" + " <#if eCode??> and eCode=:eCode</#if>"
				+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
				+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
				+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
				+ " <#if eCodeOfBuilding??> and eCodeOfBuilding=:eCodeOfBuilding</#if>"
				+ " <#if eCodeOfUnitInfo??> and eCodeOfUnitInfo=:eCodeOfUnitInfo</#if>"
				+ " <#if eCodeFromPresellSystem??> and eCodeFromPresellSystem=:eCodeFromPresellSystem</#if>"
				+ " <#if eCodeFromEscrowSystem??> and eCodeFromEscrowSystem=:eCodeFromEscrowSystem</#if>"
				+ " <#if eCodeFromPublicSecurity??> and eCodeFromPublicSecurity=:eCodeFromPublicSecurity</#if>"
				+ " <#if addressFromPublicSecurity??> and addressFromPublicSecurity=:addressFromPublicSecurity</#if>"
				+ " <#if recordPrice??> and recordPrice=:recordPrice</#if>"
				+ " <#if lastTimeStampSyncRecordPriceToPresellSystem??> and lastTimeStampSyncRecordPriceToPresellSystem=:lastTimeStampSyncRecordPriceToPresellSystem</#if>"
				+ " <#if settlementStateOfTripleAgreement??> and settlementStateOfTripleAgreement=:settlementStateOfTripleAgreement</#if>"
				+ " <#if eCodeFromPresellCert??> and eCodeFromPresellCert=:eCodeFromPresellCert</#if>"
				+ " <#if floor??> and floor=:floor</#if>" + " <#if roomId??> and roomId=:roomId</#if>"
				+ " <#if theNameOfRoomId??> and theNameOfRoomId=:theNameOfRoomId</#if>"
				+ " <#if isOverFloor??> and isOverFloor=:isOverFloor</#if>"
				+ " <#if overFloors??> and overFloors=:overFloors</#if>"
				+ " <#if position??> and position=:position</#if>" + " <#if purpose??> and purpose=:purpose</#if>"
				+ " <#if property??> and property=:property</#if>"
				+ " <#if deliveryType??> and deliveryType=:deliveryType</#if>"
				+ " <#if forecastArea??> and forecastArea=:forecastArea</#if>"
				+ " <#if actualArea??> and actualArea=:actualArea</#if>"
				+ " <#if innerconsArea??> and innerconsArea=:innerconsArea</#if>"
				+ " <#if shareConsArea??> and shareConsArea=:shareConsArea</#if>"
				+ " <#if useArea??> and useArea=:useArea</#if>"
				+ " <#if balconyArea??> and balconyArea=:balconyArea</#if>" + " <#if heigh??> and heigh=:heigh</#if>"
				+ " <#if unitType??> and unitType=:unitType</#if>"
				+ " <#if roomNumber??> and roomNumber=:roomNumber</#if>"
				+ " <#if hallNumber??> and hallNumber=:hallNumber</#if>"
				+ " <#if kitchenNumber??> and kitchenNumber=:kitchenNumber</#if>"
				+ " <#if toiletNumber??> and toiletNumber=:toiletNumber</#if>"
				+ " <#if eCodeOfOriginalHouse??> and eCodeOfOriginalHouse=:eCodeOfOriginalHouse</#if>"
				+ " <#if isOpen??> and isOpen=:isOpen</#if>" + " <#if isPresell??> and isPresell=:isPresell</#if>"
				+ " <#if isMortgage??> and isMortgage=:isMortgage</#if>"
				+ " <#if limitState??> and limitState=:limitState</#if>"
				+ " <#if eCodeOfRealBuidingUnit??> and eCodeOfRealBuidingUnit=:eCodeOfRealBuidingUnit</#if>"
				+ " <#if eCodeOfBusManage1??> and eCodeOfBusManage1=:eCodeOfBusManage1</#if>"
				+ " <#if eCodeOfBusManage2??> and eCodeOfBusManage2=:eCodeOfBusManage2</#if>"
				+ " <#if eCodeOfMapping??> and eCodeOfMapping=:eCodeOfMapping</#if>"
				+ " <#if eCodeOfPicture??> and eCodeOfPicture=:eCodeOfPicture</#if>"
				+ " <#if remark??> and remark=:remark</#if>" + " <#if logId??> and logId=:logId</#if>"
				+ " <#if tripleAgreement??> and tripleAgreement=:tripleAgreement</#if>"
				+ " <#if unitInfo??> and unitInfo=:unitInfo</#if>"
				+ " <#if buildingId??> and building.tableId=:buildingId</#if>"
			    + " <#if buildingIdArr??> and building.tableId in :buildingIdArr</#if>"
				+ " order by unitNumber , eCodeFromEscrowSystem , roomId";
	}

	public String getDetailHQL()
	{
		return "from Empj_HouseInfo where 1=1 and theState=0 "
				+ " <#if tableId??> and tableId = :tableId </#if>"
				+ " <#if externalId??> and ( externalId = :externalId or externalCode = :externalId )</#if>";
	}
	
	//查某楼幢下的所有户室按楼层排序
	public String getBuildingHQL()
	{
		return "from Empj_HouseInfo where 1=1 and theState=0"
				+ " <#if buildingId??> and building.tableId=:buildingId</#if>"
				+ " order by rowNumber desc,"
				+ " unitNumber asc, colNumber asc";
	}

	public String getExcelListHQL()
	{
		return "from Empj_HouseInfo where 1=1"
				+ " <#if idArr??> and tableId in :idArr</#if>";
	}

	//获取项目下的所有户
	public String getExcelListForProjectHQL()
	{
		return "from Empj_HouseInfo where 1=1"
				+ " <#if theState??> and theState=:theState</#if>"
				+ " <#if idArr??> and building.tableId in :idArr</#if>"
				+ " order by eCodeOfBuilding , unitNumber , roomId";
	}

	//获取这个单元下的户
	public String getHouseInfoByUnitHQL()
	{
		return "from Empj_HouseInfo where 1=1 and theState=0"
				+ " <#if roomId??> and roomId=:roomId</#if>"
				+ " <#if unitInfo??> and unitInfo=:unitInfo</#if>"
				+ " order by createTimeStamp desc";
	}
	
	public String getBuildingHQL2()
	{
		return "from Empj_HouseInfo where 1=1 and theState=0"
				+ " <#if buildingId??> and building.tableId=:buildingId</#if>"
				+ " and tripleAgreement is not null";
//				+ " and not exits (select * from Tgpf_RefundInfo T where T.tripleAgreement = tripleAgreement) ";
	}
}
