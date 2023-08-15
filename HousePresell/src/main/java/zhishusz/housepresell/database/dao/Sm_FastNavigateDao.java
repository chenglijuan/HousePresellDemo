package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Sm_FastNavigate;

/*
 * Dao数据库操作：快捷导航信息
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_FastNavigateDao extends BaseDao<Sm_FastNavigate>
{
	public String getBasicHQL()
    {
    	return "from Sm_FastNavigate where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if userTableId??> and userTableId=:userTableId</#if>"
		+ " <#if menuTableId??> and menuTableId=:menuTableId</#if>"
		+ " <#if theNameOfMenu??> and theNameOfMenu=:theNameOfMenu</#if>"
		+ " <#if theLinkOfMenu??> and theLinkOfMenu=:theLinkOfMenu</#if>"
		+ " <#if orderNumber??> and orderNumber=:orderNumber</#if>"
		+ " order by orderNumber desc ";
    }
	
	public String getBasicHQLByList()
    {
    	return "from Sm_FastNavigate where 1=1"
    	+ " <#if userTableId??> and userTableId=:userTableId</#if>";
    }
}
