package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;

/*
 * Dao数据库操作：楼幢账户Log表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpj_BuildingAccountLogDao extends BaseDao<Tgpj_BuildingAccountLog>
{
	public String getBasicHQL()
    {
    	return "from Tgpj_BuildingAccountLog where 1=1"
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
		+ " <#if appropriateFrozenAmount??> and appropriateFrozenAmount=:appropriateFrozenAmount</#if>"
		+ " <#if recordAvgPriceOfBuildingFromPresellSystem??> and recordAvgPriceOfBuildingFromPresellSystem=:recordAvgPriceOfBuildingFromPresellSystem</#if>"
		+ " <#if recordAvgPriceOfBuilding??> and recordAvgPriceOfBuilding=:recordAvgPriceOfBuilding</#if>"
		+ " <#if logId??> and logId=:logId</#if>"
		+ " <#if actualAmount??> and actualAmount=:actualAmount</#if>"
		+ " <#if paymentLines??> and paymentLines=:paymentLines</#if>"
		+ " <#if paymentProportion??> and paymentProportion=:paymentProportion</#if>"
		+ " <#if buildAmountPaid??> and buildAmountPaid=:buildAmountPaid</#if>"
		+ " <#if buildAmountPay??> and buildAmountPay=:buildAmountPay</#if>"
		+ " <#if totalAmountGuaranteed??> and totalAmountGuaranteed=:totalAmountGuaranteed</#if>"
		+ " <#if relatedBusiCode??> and relatedBusiCode=:relatedBusiCode</#if>"
		+ " <#if relatedBusiTableId??> and relatedBusiTableId=:relatedBusiTableId</#if>"
		+ " <#if versionNo??> and versionNo=:versionNo</#if>"
		+ " order by tableId desc";
    }
	
	// 根据楼幢账户查询版本号最大的楼幢账户log表
	// 查询条件：1.业务编码 2.楼幢账户 3.关联主键 4.根据版本号大小排序
	public String getSpecialHQL()
    {
    	return "from Tgpj_BuildingAccountLog where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if versionNo??> and versionNo=:versionNo</#if>"
    	+ " <#if relatedBusiCode??> and relatedBusiCode=:relatedBusiCode</#if>"
    	+ " <#if tgpj_BuildingAccount??> and tgpj_BuildingAccount=:tgpj_BuildingAccount</#if>"
		+ " <#if relatedBusiTableId??> and relatedBusiTableId=:relatedBusiTableId</#if>"
		+ " order by versionNo desc";
    }
}
