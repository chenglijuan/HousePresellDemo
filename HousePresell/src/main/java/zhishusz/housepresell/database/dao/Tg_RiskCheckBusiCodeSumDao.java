package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_RiskCheckBusiCodeSum;

/*
 * Dao数据库操作：风控例行抽查表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_RiskCheckBusiCodeSumDao extends BaseDao<Tg_RiskCheckBusiCodeSum>
{
	public String getBasicHQL()
    {
    	return "from Tg_RiskCheckBusiCodeSum where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if spotTimeStamp??> and spotTimeStamp=:spotTimeStamp</#if>"
		+ " <#if bigBusiType?? && bigBusiType != \"\"> and bigBusiType=:bigBusiType</#if>"
		+ " <#if smallBusiType?? && smallBusiType != \"\"> and smallBusiType=:smallBusiType</#if>"
	    + " <#if startDateTimeStamp??> and spotTimeStamp>=:startDateTimeStamp</#if>"
	    + " <#if endDateTimeStamp??> and spotTimeStamp<:endDateTimeStamp</#if>"
	    + " order by spotTimeStamp desc";
    }
}
