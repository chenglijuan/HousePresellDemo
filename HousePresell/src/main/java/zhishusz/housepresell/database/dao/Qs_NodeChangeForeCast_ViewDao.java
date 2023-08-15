package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.extra.Qs_NodeChangeForeCast_View;

/*
 * Dao数据库操作：节点变更预测表
 * Company：ZhiShuSZ
 * */
@Repository
public class Qs_NodeChangeForeCast_ViewDao extends BaseDao<Qs_NodeChangeForeCast_View> {
    public String getBasicHQL() {
        return "from Qs_NodeChangeForeCast_View where 1=1"
                
            
                + " <#if billTimeStamp??> and FORECASTCOMPLETEDATE >=:billTimeStamp</#if>"
                + " <#if endBillTimeStamp??> and FORECASTCOMPLETEDATE<=:endBillTimeStamp</#if>"
            
                + " <#if FORECASTCOMPLETEDATE??> and FORECASTCOMPLETEDATE=:FORECASTCOMPLETEDATE</#if>"
                + " <#if COMMPANYID??> and COMMPANYID=:COMMPANYID</#if>"
                + " <#if PROJECTID??> and PROJECTID=:PROJECTID</#if>"

                + " ORDER BY FORECASTCOMPLETEDATE,COMMPANYID,PROJECTID,BUILDCODE";
    }
    
}
