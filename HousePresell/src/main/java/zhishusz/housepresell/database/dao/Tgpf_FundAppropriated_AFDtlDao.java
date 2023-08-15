package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AFDtl;

/*
 * Dao数据库操作：申请-用款-明细
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_FundAppropriated_AFDtlDao extends BaseDao<Tgpf_FundAppropriated_AFDtl>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_FundAppropriated_AFDtl where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if eCodeOfBuilding??> and eCodeOfBuilding=:eCodeOfBuilding</#if>"
		+ " <#if supervisedBankAccount??> and supervisedBankAccount=:supervisedBankAccount</#if>"
		+ " <#if allocableAmount??> and allocableAmount=:allocableAmount</#if>"
		+ " <#if appliedAmount??> and appliedAmount=:appliedAmount</#if>"
		+ " <#if escrowStandard??> and escrowStandard=:escrowStandard</#if>"
		+ " <#if orgLimitedAmount??> and orgLimitedAmount=:orgLimitedAmount</#if>"
		+ " <#if currentFigureProgress??> and currentFigureProgress=:currentFigureProgress</#if>"
		+ " <#if currentLimitedRatio??> and currentLimitedRatio=:currentLimitedRatio</#if>"
		+ " <#if currentLimitedAmount??> and currentLimitedAmount=:currentLimitedAmount</#if>"
		+ " <#if totalAccountAmount??> and totalAccountAmount=:totalAccountAmount</#if>"
		+ " <#if appliedPayoutAmount??> and appliedPayoutAmount=:appliedPayoutAmount</#if>"
		+ " <#if currentEscrowFund??> and currentEscrowFund=:currentEscrowFund</#if>"
		+ " <#if refundAmount??> and refundAmount=:refundAmount</#if>"
		+ " <#if mainTable??> and mainTable=:mainTable</#if>"
		+ " <#if buildingId??> and building.tableId=:buildingId</#if>"
		+ " <#if payoutState??> and payoutState=:payoutState</#if>"
		+ " order by createTimeStamp desc";
    }
	
	public String getSpecialHQL()
    {
    	return "from Tgpf_FundAppropriated_AFDtl where 1=1"
		+ " <#if theState??> and theState=:theState and mainTable.theState =:theState</#if>"
		+ " <#if building??> and building=:building</#if>"
		+ " <#if payoutStatement??> and mainTable.applyState in (1,2,3,4,5) </#if>";
    }
	
	public String getCheckHQL()
    {
    	return "from Tgpf_FundAppropriated_AFDtl where 1=1"
		+ " <#if theState??> and theState=:theState and mainTable.theState =:theState</#if>"
		+ " <#if buildingId??> and building.tableId =:buildingId</#if>"
		+ " and (mainTable.approvalState is null or mainTable.approvalState <> '已完结') ";
    }
	
	public String getBuildListSortHQL()
    {
        return "from Tgpf_FundAppropriated_AFDtl where 1=1"
        + " <#if theState??> and theState=:theState and mainTable.theState =:theState</#if>"
        + " <#if afId??> and mainTable.tableId =:afId</#if>"
        + " order by to_number(regexp_substr(building.eCodeFromConstruction,'[0-9]*[0-9]',1)) asc";
    }
	
	public String getJumpBasicHQL()
    {
    	return "from Tgpf_FundAppropriated_AFDtl where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if eCodeOfBuilding??> and eCodeOfBuilding=:eCodeOfBuilding</#if>"
		+ " <#if supervisedBankAccount??> and supervisedBankAccount=:supervisedBankAccount</#if>"
		+ " <#if allocableAmount??> and allocableAmount=:allocableAmount</#if>"
		+ " <#if appliedAmount??> and appliedAmount=:appliedAmount</#if>"
		+ " <#if escrowStandard??> and escrowStandard=:escrowStandard</#if>"
		+ " <#if orgLimitedAmount??> and orgLimitedAmount=:orgLimitedAmount</#if>"
		+ " <#if currentFigureProgress??> and currentFigureProgress=:currentFigureProgress</#if>"
		+ " <#if currentLimitedRatio??> and currentLimitedRatio=:currentLimitedRatio</#if>"
		+ " <#if currentLimitedAmount??> and currentLimitedAmount=:currentLimitedAmount</#if>"
		+ " <#if totalAccountAmount??> and totalAccountAmount=:totalAccountAmount</#if>"
		+ " <#if appliedPayoutAmount??> and appliedPayoutAmount=:appliedPayoutAmount</#if>"
		+ " <#if currentEscrowFund??> and currentEscrowFund=:currentEscrowFund</#if>"
		+ " <#if refundAmount??> and refundAmount=:refundAmount</#if>"
		+ " <#if mainTable??> and mainTable=:mainTable</#if>"
		+ " <#if buildingId??> and building.tableId=:buildingId</#if>"
		+ " <#if payoutState??> and payoutState=:payoutState</#if>"
		+ " and mainTable.theState = 0"
		+ " order by createTimeStamp desc";
    }
}
