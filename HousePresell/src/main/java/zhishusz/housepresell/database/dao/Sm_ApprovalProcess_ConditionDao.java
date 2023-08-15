package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Condition;
import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：审批流-节点
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_ApprovalProcess_ConditionDao extends BaseDao<Sm_ApprovalProcess_Condition>
{
	public String getBasicHQL()
    {
    	return "from Sm_ApprovalProcess_Condition where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if theContent??> and theContent=:theContent</#if>"
		+ " <#if nextStep??> and nextStep=:nextStep</#if>";
    }
}
