package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Empj_ProjProgInspection_AF;

/*
 * Dao数据库操作：项目进度巡查-主 Company：ZhiShuSZ
 */
@Repository
public class Empj_ProjProgInspection_AFDao extends BaseDao<Empj_ProjProgInspection_AF> {
    public String getBasicHQL() {
        return "from Empj_ProjProgInspection_AF where 1=1" 
            + " and nowNode.limitedAmount <> 0 "
            + " <#if theState??> and theState=:theState</#if>"
            + " <#if busiState??> and busiState=:busiState</#if>" 
            + " <#if eCode??> and eCode=:eCode</#if>"
            + " <#if approvalState??> and approvalState=:approvalState</#if>"

            + " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
            + " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
            + " <#if projectId??> and project.tableId=:projectId</#if>"
            + " <#if areaId??> and areaInfo.tableId =:areaId</#if>"
            /*
             * xsz by time 2018-11-12 15:08:02
             * 加数据权限控制
             * 开发企业、区域、项目
             */
            + " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (areaInfo.tableId in :cityRegionInfoIdArr or userStart.tableId=:userId)</#if>"

            + " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
            + " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>" 
                + " <#list projectInfoIdArr as projectInfoId>"
                    + " <#if projectInfoId_index == 0> (project.tableId = ${projectInfoId?c} )</#if>"
                    + " <#if projectInfoId_index != 0> or (project.tableId = ${projectInfoId?c} )</#if>"
                + " </#list>"
            + " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> )</#if>" 
            + " </#if>"

            + " <#if keyword??> and ( eCode like :keyword or areaName like :keyword or projectName like :keyword  or buildCode like :keyword ) </#if>"

            + " order by approvalState , areaName, projectName, to_number(regexp_substr(buildCode,'[0-9]*[0-9]',1))";
    }
    
    
    public String getBasicHQLUpdate() {
        return "from Empj_ProjProgInspection_AF where 1=1" 
            + " and nowNode.limitedAmount <> 0 "
            + " <#if theState??> and theState=:theState</#if>"
            + " <#if busiState??> and busiState=:busiState</#if>" 
            + " <#if eCode??> and eCode=:eCode</#if>"
            + " <#if approvalState??> and approvalState=:approvalState</#if>"
            + " <#if buildInfo??> and buildInfo=:buildInfo</#if>"

            + " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
            + " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
            + " <#if projectId??> and project.tableId=:projectId</#if>"
            + " <#if areaId??> and areaInfo.tableId =:areaId</#if>";
    }

}
