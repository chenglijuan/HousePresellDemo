package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;

import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：审批流-审批流程
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_ApprovalProcess_WorkflowDao extends BaseDao<Sm_ApprovalProcess_Workflow>
{
	public String getBasicHQL()
    {
    	return "from Sm_ApprovalProcess_Workflow where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
    	+ " <#if userUpdate??> and userUpdate=:userUpdate</#if>"
		+ " <#if theState??> and approvalProcess_AF.theState = 0 </#if>"
    	+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if lastAction??> and lastAction=:lastAction</#if>"
		+ " <#if roleId??> and role.tableId =:roleId</#if>"
		+ " <#if roleListId??> and role.tableId in (:roleListId)</#if>"
		+ " <#if approvalProcess_AFId??> and approvalProcess_AF.tableId =:approvalProcess_AFId</#if>"
		+ " <#if startTimeStamp??> and approvalProcess_AF.createTimeStamp >=:startTimeStamp</#if>"
		+ " <#if endTimeStamp??> and approvalProcess_AF.createTimeStamp <=:endTimeStamp</#if>"
		+ " <#if busiCode??> and approvalProcess_AF.busiCode =:busiCode</#if>"
		+ " <#if keyword??> and  CONCAT(approvalProcess_AF.eCode,approvalProcess_AF.applicant,approvalProcess_AF.theNameOfCompanyInfo,approvalProcess_AF.theme) like :keyword </#if>"
	    + " <#if orderBy?? && orderBy != \"\"> order by ${orderBy}</#if>";
    }
}
