package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Node;

/*
 * Dao数据库操作：审批流-节点
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_ApprovalProcess_NodeDao extends BaseDao<Sm_ApprovalProcess_Node>
{
	public String getBasicHQL()
    {
    	return "from Sm_ApprovalProcess_Node where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if theName??> and theName=:theName</#if>"
		+ " <#if orgJob??> and orgJob=:orgJob</#if>"
		+ " <#if orderNumber??> and orderNumber=:orderNumber</#if>"
		+ " <#if idArr??> and tableId in(:idArr)</#if>";
    }
}
