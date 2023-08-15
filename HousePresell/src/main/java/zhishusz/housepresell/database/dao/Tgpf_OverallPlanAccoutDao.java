package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_OverallPlanAccout;

/*
 * Dao数据库操作：统筹-账户状况信息保存表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_OverallPlanAccoutDao extends BaseDao<Tgpf_OverallPlanAccout>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_OverallPlanAccout where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if eCodeOfFundOverallPlan??> and eCodeOfFundOverallPlan=:eCodeOfFundOverallPlan</#if>"
		+ " <#if eCodeOfAFWithdrawMainTable??> and eCodeOfAFWithdrawMainTable=:eCodeOfAFWithdrawMainTable</#if>"
		+ " <#if overallPlanAmount??> and overallPlanAmount >:overallPlanAmount</#if>"
		+ " <#if fundOverallPlanId??> and fundOverallPlan.tableId =:fundOverallPlanId</#if>"
		+ "  order by  NLSSORT(bankAccountEscrowed.theNameOfBank,'NLS_SORT = SCHINESE_PINYIN_M')";
    }
}
