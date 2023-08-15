package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Empj_BuildingInfo_AF;

/*
 * Dao数据库操作：申请表-楼幢变更
 * Company：ZhiShuSZ
 * */
@Repository
public class Empj_BuildingInfo_AFDao extends BaseDao<Empj_BuildingInfo_AF>
{
	public String getBasicHQL()
    {
    	return "from Empj_BuildingInfo_AF where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
		+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
		+ " <#if eCodeOfProject??> and eCodeOfProject=:eCodeOfProject</#if>"
		+ " <#if eCodeOfBuilding??> and eCodeOfBuilding=:eCodeOfBuilding</#if>"
		+ " <#if buildingArea??> and buildingArea=:buildingArea</#if>"
		+ " <#if escrowArea??> and escrowArea=:escrowArea</#if>"
		+ " <#if deliveryType??> and deliveryType=:deliveryType</#if>"
		+ " <#if upfloorNumber??> and upfloorNumber=:upfloorNumber</#if>"
		+ " <#if downfloorNumber??> and downfloorNumber=:downfloorNumber</#if>"
		+ " <#if landMortgageState??> and landMortgageState=:landMortgageState</#if>"
		+ " <#if landMortgagor??> and landMortgagor=:landMortgagor</#if>"
		+ " <#if landMortgageAmount??> and landMortgageAmount=:landMortgageAmount</#if>"
		+ " <#if remark??> and remark=:remark</#if>";
    }
}
