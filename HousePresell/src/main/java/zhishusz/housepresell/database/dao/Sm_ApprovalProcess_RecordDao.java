package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Record;

/*
 * Dao数据库操作：审批流-审批记录
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_ApprovalProcess_RecordDao extends BaseDao<Sm_ApprovalProcess_Record>
{
	public String getBasicHQL()
    {
    	return "from Sm_ApprovalProcess_Record where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if theContent??> and theContent=:theContent</#if>"
		+ " <#if attachmentList??> and attachmentList=:attachmentList</#if>"
		+ " <#if theAction??> and theAction=:theAction</#if>"
		+ " <#if operateTimeStamp??> and operateTimeStamp=:operateTimeStamp</#if>"
		+ " <#if workflowIdArray??> and approvalProcess.tableId in(:workflowIdArray)</#if>"
		+ " <#if approvalProcessId??> and approvalProcess.tableId=:approvalProcessId</#if>"
		+ " <#if workflowTime??> and operateTimeStamp > :workflowTime</#if>"
    	+ "	 order by operateTimeStamp";
    }
}
