package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Sm_Operate_Log;

/*
 * Dao数据库操作：日志-关键操作
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_Operate_LogDao extends BaseDao<Sm_Operate_Log>
{
	public String getBasicHQL()
    {
    	return "from Sm_Operate_Log where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if remoteAddress??> and remoteAddress=:remoteAddress</#if>"
		+ " <#if operate??> and operate=:operate</#if>"
		+ " <#if inputForm??> and inputForm=:inputForm</#if>"
		+ " <#if result??> and result=:result</#if>"
		+ " <#if info??> and info=:info</#if>"
		+ " <#if returnJson??> and returnJson=:returnJson</#if>"
		+ " <#if startTimeStamp??> and startTimeStamp=:startTimeStamp</#if>"
		+ " <#if endTimeStamp??> and endTimeStamp=:endTimeStamp</#if>";
    }
}
