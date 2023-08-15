package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Empj_ProjProgInspection_DTL;

/*
 * Dao数据库操作：项目进度巡查-子 Company：ZhiShuSZ
 */
@Repository
public class Empj_ProjProgInspection_DTLDao extends BaseDao<Empj_ProjProgInspection_DTL> {
    public String getBasicHQL() {
        return "from Empj_ProjProgInspection_DTL where 1=1" + " <#if theState??> and theState=:theState</#if>"
            + " <#if busiState??> and busiState=:busiState</#if>" + " <#if eCode??> and eCode=:eCode</#if>"
            + " <#if approvalState??> and approvalState=:approvalState</#if>"
            + " <#if afCode??> and afCode=:afCode</#if>" + " <#if afInfo??> and afInfo=:afInfo</#if>"
            + " <#if buildInfo??> and buildInfo=:buildInfo</#if>"
            + " <#if forecastcompletedate??> and forecastcompletedate=:forecastcompletedate</#if>"
            + " <#if forecastNode??> and forecastNode=:forecastNode</#if>"
            + " <#if keyword??> and ( agreementVersion like :keyword or theNameOfDevelopCompany like :keyword or theNameOfProject like :keyword or eCodeOfAgreement like :keyword ) </#if>"
            + " ORDER BY forecastNode.limitedAmount desc";
    }
    
    public String getBasicCompareHQL() {
        return "from Empj_ProjProgInspection_DTL where 1=1" + " <#if theState??> and theState=:theState</#if>"
            + " <#if busiState??> and busiState=:busiState</#if>" + " <#if eCode??> and eCode=:eCode</#if>"
            + " <#if approvalState??> and approvalState=:approvalState</#if>"
            + " <#if afCode??> and afCode=:afCode</#if>" + " <#if afInfo??> and afInfo=:afInfo</#if>"
            + " <#if buildInfo??> and buildInfo=:buildInfo</#if>"
            + " <#if forecastcompletedate??> and forecastcompletedate=:forecastcompletedate</#if>"
            + " <#if nowLimit??> and forecastNode.limitedAmount<:nowLimit</#if>"
            + " <#if keyword??> and ( agreementVersion like :keyword or theNameOfDevelopCompany like :keyword or theNameOfProject like :keyword or eCodeOfAgreement like :keyword ) </#if>";
    }

}
