package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Sm_CommonMessage;

/*
 * Dao数据库操作：消息推送主体
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_CommonMessageDao extends BaseDao<Sm_CommonMessage>
{
	public String getBasicHQL()
    {
    	return "from Sm_CommonMessage where 1=1"
    	+ " <#if OrgDataId??> and OrgDataId=:OrgDataId</#if>"
    	+ " <#if busiCode??> and busiCode=:busiCode</#if>"
    	+ " <#if orgDataCode??> and orgDataCode=:orgDataCode</#if>"
    	+ " <#if theState??> and theState=:theState</#if>";
    }

}
