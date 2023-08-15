package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;

/*
 * Dao数据库操作：申请-用款-主表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_FundAppropriated_AFDao extends BaseDao<Tgpf_FundAppropriated_AF>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_FundAppropriated_AF where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
		+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
		+ " <#if eCodeOfProject??> and eCodeOfProject=:eCodeOfProject</#if>"
		+ " <#if applyDate??> and applyDate=:applyDate</#if>"
		+ " <#if fundAppropriatedApplyDate??> and applyDate=:fundAppropriatedApplyDate</#if>"
		+ " <#if startTimeStamp??> and createTimeStamp>=:startTimeStamp</#if>"
		+ " <#if endTimeStamp??> and createTimeStamp<=:endTimeStamp</#if>"
		+ " <#if keyword??> and  eCode like :keyword </#if>"
		+ " <#if fundOverallPlanId??> and fundOverallPlan.tableId = :fundOverallPlanId </#if>"
		+ " <#if developCompanyId??> and developCompany.tableId = :developCompanyId </#if>"
		+ " <#if projectId??> and project.tableId = :projectId </#if>"
		+ " <#if applyState??> and applyState=:applyState </#if>"
		+ " <#if applyType??> and NVL(applyType,'0') = :applyType </#if>"
		
		//风控抽查条件
		+ " <#if approvalState??> and approvalState=:approvalState</#if>"
		+ " <#if checkStartTimeStamp??> and createTimeStamp>=:checkStartTimeStamp</#if>"
		+ " <#if checkEndTimeStamp??> and createTimeStamp<:checkEndTimeStamp</#if>"
		
		+ " <#if payoutState1??> and (applyState =:payoutState1 or applyState =:payoutState2 or applyState =:payoutState3)</#if>"
		+ " <#if fundOverallPlanDate??> and fundOverallPlan.fundOverallPlanDate =:fundOverallPlanDate</#if>"
		+ " <#if orderBy??> order by ${orderBy}</#if>";

    }
}
