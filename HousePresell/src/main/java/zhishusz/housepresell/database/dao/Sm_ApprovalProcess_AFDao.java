package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;

/*
 * Dao数据库操作：审批流-申请单
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_ApprovalProcess_AFDao extends BaseDao<Sm_ApprovalProcess_AF>
{
	public String getBasicHQL()
    {
    	return "from Sm_ApprovalProcess_AF where 1=1"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if sourceId??> and sourceId=:sourceId</#if>"
		+ " <#if sourceType??> and sourceType=:sourceType</#if>"
		+ " <#if orgObjJsonFilePath??> and orgObjJsonFilePath=:orgObjJsonFilePath</#if>"
		+ " <#if expectObjJsonFilePath??> and expectObjJsonFilePath=:expectObjJsonFilePath</#if>"
		+ " <#if attachmentList??> and attachmentList=:attachmentList</#if>"
		+ " <#if workFlowList??> and workFlowList=:workFlowList</#if>"
		+ " <#if currentIndex??> and currentIndex=:currentIndex</#if>"
		+ " <#if keyword??> and  CONCAT(eCode,applicant,theNameOfCompanyInfo,theme) like :keyword </#if>"
		+ " <#if userStartId??> and userStart.tableId =:userStartId</#if>"
	    + " <#if busiCode??> and busiCode =:busiCode</#if>"
		+ " <#if startTimeStamp??> and createTimeStamp >=:startTimeStamp</#if>"
		+ " <#if endTimeStamp??> and createTimeStamp <:endTimeStamp</#if>"
		+ " <#if orderBy??> order by ${orderBy}</#if>";

    }
	
	public String getBasicStartHQL()
    {
    	return "from Sm_ApprovalProcess_AF where 1=1"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if sourceId??> and sourceId=:sourceId</#if>"
		+ " <#if sourceType??> and sourceType=:sourceType</#if>"
		+ " <#if orgObjJsonFilePath??> and orgObjJsonFilePath=:orgObjJsonFilePath</#if>"
		+ " <#if expectObjJsonFilePath??> and expectObjJsonFilePath=:expectObjJsonFilePath</#if>"
		+ " <#if attachmentList??> and attachmentList=:attachmentList</#if>"
		+ " <#if workFlowList??> and workFlowList=:workFlowList</#if>"
		+ " <#if currentIndex??> and currentIndex=:currentIndex</#if>"
		+ " <#if keyword??> and  CONCAT(eCode,applicant,theNameOfCompanyInfo,theme) like :keyword </#if>"
		+ " <#if userStartId??> and userStart.tableId =:userStartId</#if>"
	    + " <#if busiCode??> and busiCode =:busiCode</#if>"
		+ " <#if startTimeStamp??> and startTimeStamp >=:startTimeStamp</#if>"
		+ " <#if endTimeStamp??> and startTimeStamp <:endTimeStamp</#if>"
		+ " <#if orderBy??> order by ${orderBy}</#if>";

    }
}
