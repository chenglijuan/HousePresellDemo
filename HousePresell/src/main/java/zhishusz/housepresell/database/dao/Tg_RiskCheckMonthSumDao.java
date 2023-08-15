package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_RiskCheckMonthSum;

/*
 * Dao数据库操作：风控例行抽查月汇总表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_RiskCheckMonthSumDao extends BaseDao<Tg_RiskCheckMonthSum>
{
	public String getBasicHQL()
    {
    	return "from Tg_RiskCheckMonthSum where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if rectificationState?? && rectificationState != \"\"> and rectificationState=:rectificationState</#if>"
		+ " <#if spotTimeStamp??> and spotTimeStamp=:spotTimeStamp</#if>"
    	+ " <#if searchStartTimeStamp??> and createTimeStamp>=:searchStartTimeStamp</#if>"
		+ " <#if searchEndTimeStamp??> and createTimeStamp<=:searchEndTimeStamp</#if>"
    	+ " order by spotTimeStamp desc";
    }
}
