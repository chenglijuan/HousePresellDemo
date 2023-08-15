package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Sm_eCode_Log;

/*
 * Dao数据库操作：eCode记录
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_eCode_LogDao extends BaseDao<Sm_eCode_Log>
{
	public String getBasicHQL()
    {
    	return "from Sm_eCode_Log where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if recordState??> and recordState=:recordState</#if>"
		+ " <#if recordRejectReason??> and recordRejectReason=:recordRejectReason</#if>"
		+ " <#if busiCode??> and busiCode=:busiCode</#if>"
		+ " <#if theYear??> and theYear=:theYear</#if>"
		+ " <#if theMonth??> and theMonth=:theMonth</#if>"
		+ " <#if theDay??> and theDay=:theDay</#if>"
		+ " <#if ticketCount??> and ticketCount=:ticketCount</#if>";
    }
}
