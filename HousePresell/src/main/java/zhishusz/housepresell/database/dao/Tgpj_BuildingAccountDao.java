package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;

/*
 * Dao数据库操作：楼幢账户
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpj_BuildingAccountDao extends BaseDao<Tgpj_BuildingAccount>
{
	public String getBasicHQL()
    {
    	return "from Tgpj_BuildingAccount where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
		+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
		+ " <#if eCodeOfBuilding??> and eCodeOfBuilding=:eCodeOfBuilding</#if>"
		+ " <#if escrowStandard??> and escrowStandard=:escrowStandard</#if>"
		+ " <#if escrowArea??> and escrowArea=:escrowArea</#if>"
		+ " <#if buildingArea??> and buildingArea=:buildingArea</#if>"
		+ " <#if orgLimitedAmount??> and orgLimitedAmount=:orgLimitedAmount</#if>"
		+ " <#if currentFigureProgress??> and currentFigureProgress=:currentFigureProgress</#if>"
		+ " <#if currentLimitedRatio??> and currentLimitedRatio=:currentLimitedRatio</#if>"
		+ " <#if nodeLimitedAmount??> and nodeLimitedAmount=:nodeLimitedAmount</#if>"
		+ " <#if totalGuaranteeAmount??> and totalGuaranteeAmount=:totalGuaranteeAmount</#if>"
		+ " <#if cashLimitedAmount??> and cashLimitedAmount=:cashLimitedAmount</#if>"
		+ " <#if effectiveLimitedAmount??> and effectiveLimitedAmount=:effectiveLimitedAmount</#if>"
		+ " <#if totalAccountAmount??> and totalAccountAmount=:totalAccountAmount</#if>"
		+ " <#if spilloverAmount??> and spilloverAmount=:spilloverAmount</#if>"
		+ " <#if payoutAmount??> and payoutAmount=:payoutAmount</#if>"
		+ " <#if appliedNoPayoutAmount??> and appliedNoPayoutAmount=:appliedNoPayoutAmount</#if>"
		+ " <#if applyRefundPayoutAmount??> and applyRefundPayoutAmount=:applyRefundPayoutAmount</#if>"
		+ " <#if refundAmount??> and refundAmount=:refundAmount</#if>"
		+ " <#if currentEscrowFund??> and currentEscrowFund=:currentEscrowFund</#if>"
		+ " <#if allocableAmount??> and allocableAmount=:allocableAmount</#if>"
		+ " <#if recordAvgPriceOfBuildingFromPresellSystem??> and recordAvgPriceOfBuildingFromPresellSystem=:recordAvgPriceOfBuildingFromPresellSystem</#if>"
		+ " <#if recordAvgPriceOfBuilding??> and recordAvgPriceOfBuilding=:recordAvgPriceOfBuilding</#if>"
		+ " <#if logId??> and logId=:logId</#if>"
		+ " <#if paymentId??> and payment.tableId=:paymentId</#if>"
		+ " <#if developCompany??> and developCompany=:developCompany</#if>"
		+ " <#if projectId??> and project.tableId=:projectId</#if>"
		+ " <#if cityRegionId??> and project.cityRegion.tableId=:cityRegionId</#if>"
		+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and project.cityRegion.tableId in :cityRegionInfoIdArr</#if>"
		+ " <#if developCompanyId??> and developCompany.tableId=:developCompanyId</#if>"
		+ " <#if building??> and building=:building</#if>";
    }
}
