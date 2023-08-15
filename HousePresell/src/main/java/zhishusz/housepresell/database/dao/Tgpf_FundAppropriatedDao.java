package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_FundAppropriated;

/*
 * Dao数据库操作：资金拨付
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_FundAppropriatedDao extends BaseDao<Tgpf_FundAppropriated>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_FundAppropriated where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
		+ " <#if overallPlanPayoutAmount??> and overallPlanPayoutAmount >:overallPlanPayoutAmount</#if>"
		+ " <#if actualPayoutDate??> and actualPayoutDate=:actualPayoutDate</#if>"
		+ " <#if eCodeFromPayoutBill??> and eCodeFromPayoutBill=:eCodeFromPayoutBill</#if>"
		+ " <#if currentPayoutAmount??> and currentPayoutAmount=:currentPayoutAmount</#if>"
		+ " <#if fundAppropriated_AFId??> and fundAppropriated_AF.tableId =:fundAppropriated_AFId</#if>"
		+ " <#if fundOverallPlanId??> and fundOverallPlan.tableId =:fundOverallPlanId</#if>";
    }

	public String getBasicHQL2()
	{
		return "from Tgpf_FundAppropriated where 1=1"
				+ " <#if theState??> and theState=:theState</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>"
				+ " <#if fundOverallPlanId??> and fundOverallPlan.tableId !=:fundOverallPlanId</#if>"
				+ " <#if bankAccountEscrowedId??> and bankAccountEscrowed.tableId =:bankAccountEscrowedId</#if>";
	}
	
	public String getCurrentPayoutAmount()
	{
		return "from Tgpf_FundAppropriated where 1=1"
				+ " <#if theState??> and theState=:theState</#if>"
				+ " <#if actualPayoutDate??> and (actualPayoutDate like :actualPayoutDate)</#if>"
				+ " <#if cityRegionId??> and bankAccountEscrowed.project.cityRegion.tableId =:cityRegionId</#if>";
	}
}
