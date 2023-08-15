package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_FundOverallPlan;

/*
 * Dao数据库操作：资金统筹
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_FundOverallPlanDao extends BaseDao<Tgpf_FundOverallPlan>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_FundOverallPlan where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if approvalState??> and approvalState=:approvalState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if lastCyberBankBillTimeStamp??> and lastCyberBankBillTimeStamp=:lastCyberBankBillTimeStamp</#if>"
		+ " <#if coordinatingTimeStamp1??> and createTimeStamp >= :coordinatingTimeStamp1 and createTimeStamp < :coordinatingTimeStamp2</#if>"
		+ " <#if fundOverallPlanDate??> and fundOverallPlanDate = :fundOverallPlanDate</#if>"
		+ " <#if keyword??> and eCode like :keyword</#if>"
		+ " <#if orderBy??> order by ${orderBy}</#if>";
    }
}
