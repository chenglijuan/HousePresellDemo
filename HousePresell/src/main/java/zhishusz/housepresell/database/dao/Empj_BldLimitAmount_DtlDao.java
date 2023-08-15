package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Empj_BldLimitAmount_Dtl;

/*
 * Dao数据库操作：申请表-进度节点变更-申请子表
 * Company：ZhiShuSZ
 * */
@Repository
public class Empj_BldLimitAmount_DtlDao extends BaseDao<Empj_BldLimitAmount_Dtl> {
    public String getBasicHQL() {
        return "from Empj_BldLimitAmount_Dtl where 1=1"
                + " <#if theState??> and theState=:theState</#if>"
                + " <#if eCodeOfMainTable??> and eCodeOfMainTable=:eCodeOfMainTable</#if>"
                + " <#if mainTable??> and mainTable=:mainTable</#if>";
    }
    
    /**
     * 获取在审核中的申请楼幢信息
     * @return
     */
    public String getHQLByBuildingList() {
        return "from Empj_BldLimitAmount_Dtl where 1=1"
                + " <#if theState??> and theState=:theState</#if>"
                + " <#if afState??> and mainTable.approvalState=:afState</#if>";
    }
    
    
    public String getBasicHQLByBuild() {
        return "from Empj_BldLimitAmount_Dtl where 1=1"
                + " <#if theState??> and theState=:theState</#if>"
                + " <#if building??> and building=:building</#if>"
                + " <#if eCodeOfBuilding??> and eCodeOfBuilding=:eCodeOfBuilding</#if>";
    }
    
    public String getCheckByBuild() {
        return "from Empj_BldLimitAmount_Dtl where 1=1"
                + " <#if theState??> and theState=:theState</#if>"
                + " <#if building??> and building=:building</#if>"
                + " <#if eCodeOfBuilding??> and eCodeOfBuilding=:eCodeOfBuilding</#if>"
                + " and (mainTable.theState = 0 and mainTable.approvalState <> '已完结')";
    }

}
