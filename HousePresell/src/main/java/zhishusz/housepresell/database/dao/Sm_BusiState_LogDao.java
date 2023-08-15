package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.database.po.Sm_BusiState_Log;

import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：日志-业务状态
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_BusiState_LogDao extends BaseDao<Sm_BusiState_Log>
{
	public String getBasicHQL()
    {
    	return "from Sm_BusiState_Log where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if remoteAddress??> and remoteAddress=:remoteAddress</#if>"
		+ " <#if operateTimeStamp??> and operateTimeStamp=:operateTimeStamp</#if>"
		+ " <#if sourceId??> and sourceId=:sourceId</#if>"
		+ " <#if sourceType??> and sourceType=:sourceType</#if>"
		+ " <#if keyword??> and userOperate.theName like :keyword</#if>"
		+ " <#if orgObjJsonFilePath??> and orgObjJsonFilePath=:orgObjJsonFilePath</#if>"
		+ " <#if newObjJsonFilePath??> and newObjJsonFilePath=:newObjJsonFilePath</#if>"
				+"order by operateTimeStamp desc";
    }
}
