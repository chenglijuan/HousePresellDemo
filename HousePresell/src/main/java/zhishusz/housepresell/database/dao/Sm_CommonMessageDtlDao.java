package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Sm_CommonMessageDtl;

/*
 * Dao数据库操作：消息推送关联
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_CommonMessageDtlDao extends BaseDao<Sm_CommonMessageDtl>
{
	public String getBasicHQL()
    {
    	return "from Sm_CommonMessageDtl where 1=1"
    	+ " <#if theState??> and theState = :theState</#if>"
		+ " <#if receiver??> and receiver=:receiver</#if>"
    	+ " <#if messageType??> and messageType=:messageType</#if>"
		+ " <#if message??> and message=:message</#if>"
    	+ " <#if message??> and message.tableId=:message</#if>"
    	+ " <#if isReader??> and isReader=:isReader</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if receiverId??> and receiver.tableId=:receiverId</#if>"
		+ " <#if busiCode??> and message.busiCode=:busiCode</#if>"
		+ " <#if keyword??> and ( message.theTitle like :keyword or message.orgDataCode like :keyword or message.theContent like :keyword ) </#if>"
    	+ " order by isreader asc ,createTimeStamp desc";
    }
	
	public String getBasicHQL1()
    {
    	return "from Sm_CommonMessageDtl where 1=1"
    	+ " <#if theState??> and theState =:theState </#if>"
    	+ " <#if receiver??> and receiver=:receiver</#if>"
    	+ " <#if messageType??> and messageType=:messageType</#if>"
    	+ " <#if message??> and message=:message</#if>"
    	+ " <#if busiState??> and busiState=:busiState</#if>"
    	+ " <#if isReader??> and isReader=:isReader</#if>"
    	+ " order by isreader asc ,createTimeStamp desc";
    }
	
}
