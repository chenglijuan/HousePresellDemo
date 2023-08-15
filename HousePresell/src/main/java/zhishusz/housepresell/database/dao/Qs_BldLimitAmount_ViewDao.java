package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Qs_BldLimitAmount_View;

/*
 * Dao数据库操作：申请表-进度节点变更
 * Company：ZhiShuSZ
 * */
@Repository
public class Qs_BldLimitAmount_ViewDao extends BaseDao<Qs_BldLimitAmount_View> {
    public String getBasicHQL() {
        return "from Qs_BldLimitAmount_View where 1=1"
                + " <#if theState??> and theState=:theState</#if>"
                + " <#if approvalState??> and approvalState=:approvalState</#if>"
                + " <#if tableId??> and tableId=:tableId</#if>"
                + " <#if busiState??> and busiState=:busiState</#if>"
                + " <#if eCode??> and eCode=:eCode</#if>"
                + " <#if developCompanyId??> and developCompany.tableId=:developCompanyId</#if>"
                + " <#if projectId??> and project.tableId=:projectId</#if>"
                + " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
                + " <#if checkStartTimeStamp??> and createTimeStamp>=:checkStartTimeStamp</#if>"
                + " <#if checkEndTimeStamp??> and createTimeStamp<:checkEndTimeStamp</#if>"
                + " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
                + " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
                + " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
                + " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
                + " <#if eCodeOfProject??> and eCodeOfProject=:eCodeOfProject</#if>"
                
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>"
					+ " <#list projectInfoIdArr as projectInfoId>"
						+ " <#if projectInfoId_index == 0> (project.tableId = ${projectInfoId?c} )</#if>"
						+ " <#if projectInfoId_index != 0> or (project.tableId = ${projectInfoId?c} )</#if>"
					+ " </#list>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> )</#if>"
				+ " </#if>"
                + " <#if keyword??> and eCode like :keyword</#if>"
                + " order by decode(approvalState,'待提交',9,'审核中',8,'已完结',7,'不通过',6,9) desc,recordTimeStamp desc";
    }
    
    public String getBasicreportHQL() {
        return "from Qs_BldLimitAmount_View where 1=1 and approvalState = '已完结' "
                + " <#if theState??> and theState=:theState</#if>"
                + " <#if approvalState??> and approvalState=:approvalState</#if>"
                + " <#if tableId??> and tableId=:tableId</#if>"
                + " <#if busiState??> and busiState=:busiState</#if>"
                + " <#if eCode??> and eCode=:eCode</#if>"
                + " <#if developCompanyId??> and developCompany.tableId=:developCompanyId</#if>"
                + " <#if projectId??> and project.tableId=:projectId</#if>"
                + " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
                + " <#if checkStartTimeStamp??> and createTimeStamp>=:checkStartTimeStamp</#if>"
                + " <#if checkEndTimeStamp??> and createTimeStamp<:checkEndTimeStamp</#if>"
                + " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
                + " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
                + " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
                + " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
                + " <#if eCodeOfProject??> and eCodeOfProject=:eCodeOfProject</#if>"
                + " <#if completeStartLong??> and appointTimeStamp>=:completeStartLong</#if>"
                + " <#if completeEndLong??> and appointTimeStamp<=:completeEndLong</#if>"
                
                + " <#if companyId??> and company.tableId=:companyId </#if>"
                + " <#if companyName??> and company.theName like :companyName </#if>"
                
                + " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
                + " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>"
                    + " <#list projectInfoIdArr as projectInfoId>"
                        + " <#if projectInfoId_index == 0> (project.tableId = ${projectInfoId?c} )</#if>"
                        + " <#if projectInfoId_index != 0> or (project.tableId = ${projectInfoId?c} )</#if>"
                    + " </#list>"
                + " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> )</#if>"
                + " </#if>"
                + " <#if keyword??> and eCode like :keyword</#if>"
                + " order by decode(approvalState,'待提交',9,'审核中',8,'已完结',7,'不通过',6,9) desc,recordTimeStamp desc";
    }
    
    

}
