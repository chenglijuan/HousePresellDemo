package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Empj_ProjProgForcast_AF;

/*
 * Dao数据库操作：工程进度巡查-主 Company：ZhiShuSZ
 */
@Repository
public class Empj_ProjProgForcast_AFDao extends BaseDao<Empj_ProjProgForcast_AF> {
    public String getBasicHQL() {
        return "from Empj_ProjProgForcast_AF where 1=1" 
            + " <#if theState??> and theState=:theState</#if>"
            + " <#if busiState??> and busiState=:busiState</#if>" 
            + " <#if eCode??> and eCode=:eCode</#if>"
            + " <#if approvalState??> and approvalState=:approvalState</#if>" 
            + " <#if companyId??> and company.tableId=:companyId</#if>"

            + " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
            + " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
            + " <#if projectId??> and project.tableId=:projectId</#if>"
            + " <#if areaId??> and area.tableId =:areaId</#if>"
            + " <#if timeStamp??> and num_to_datechar(recordTimeStamp) =:timeStamp</#if>"
            
            + " <#if timeStampStart??> and num_to_datechar(recordTimeStamp) >=:timeStampStart</#if>"
            + " <#if timeStampEnd??> and num_to_datechar(recordTimeStamp) <=:timeStampEnd</#if>"
            /*
             * xsz by time 2018-11-12 15:08:02
             * 加数据权限控制
             * 开发企业、区域、项目
             */
            + " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (area.tableId in :cityRegionInfoIdArr or userStart.tableId=:userId)</#if>"

            + " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
            + " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>" 
                + " <#list projectInfoIdArr as projectInfoId>"
                    + " <#if projectInfoId_index == 0> (project.tableId = ${projectInfoId?c} )</#if>"
                    + " <#if projectInfoId_index != 0> or (project.tableId = ${projectInfoId?c} )</#if>"
                + " </#list>"
            + " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> )</#if>" 
            + " </#if>"

            + " <#if keyword??> and ( eCode like :keyword or forcastPeople like :keyword or areaName like :keyword or projectName like :keyword ) </#if>"

            + " order by decode(approvalState ,'待提交', 1, '审核中', 2, '已完结', 3 , 0), areaName , projectName ,tableId desc ,createTimeStamp desc";
    }
    
    public String getBasicReportHQL() {
        return "from Empj_ProjProgForcast_AF where 1=1" 
            + " <#if theState??> and theState=:theState</#if>"
            + " <#if busiState??> and busiState=:busiState</#if>" 
            + " <#if eCode??> and eCode=:eCode</#if>"
            + " <#if approvalState??> and approvalState=:approvalState</#if>" 
            + " <#if companyId??> and company.tableId=:companyId</#if>"

            + " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
            + " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
            + " <#if projectId??> and project.tableId=:projectId</#if>"
            + " <#if areaId??> and area.tableId =:areaId</#if>" 
            + " <#if timeStamp??> and num_to_datechar(recordTimeStamp) =:timeStamp</#if>"
            
            + " <#if timeStampStart??> and num_to_datechar(recordTimeStamp) >=:timeStampStart</#if>"
            + " <#if timeStampEnd??> and num_to_datechar(recordTimeStamp) <=:timeStampEnd</#if>"
            /*
             * xsz by time 2018-11-12 15:08:02
             * 加数据权限控制
             * 开发企业、区域、项目
             */
            + " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (area.tableId in :cityRegionInfoIdArr or userStart.tableId=:userId)</#if>"

            + " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
            + " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>" 
                + " <#list projectInfoIdArr as projectInfoId>"
                    + " <#if projectInfoId_index == 0> (project.tableId = ${projectInfoId?c} )</#if>"
                    + " <#if projectInfoId_index != 0> or (project.tableId = ${projectInfoId?c} )</#if>"
                + " </#list>"
            + " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> )</#if>" 
            + " </#if>"

            + " <#if keyword??> and ( eCode like :keyword or forcastPeople like :keyword or areaName like :keyword or projectName like :keyword ) </#if>"

            + " order by decode(approvalState ,'待提交', 1, '审核中', 2, '已完结', 3 , 0),createTimeStamp desc";
    }
    
    
    public String getHandlerPicHQL() {
        return "from Empj_ProjProgForcast_AF where 1=1" 
            + " <#if theState??> and theState=:theState</#if>"
            + " <#if busiState??> and busiState=:busiState</#if>" 
            + " <#if eCode??> and eCode=:eCode</#if>"
            + " <#if approvalState??> and approvalState=:approvalState</#if>" 
            + " <#if companyId??> and company.tableId=:companyId</#if>"
            
            + " <#if handleState??> and NVL(handleState,'0') = '0'</#if>"
            
            + " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
            + " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
            + " <#if projectId??> and project.tableId=:projectId</#if>"
            + " <#if areaId??> and area.tableId =:areaId</#if>"
            + " <#if timeStamp??> and num_to_datechar(recordTimeStamp) =:timeStamp</#if>"
            + " order by applyDate ";
    }

}
