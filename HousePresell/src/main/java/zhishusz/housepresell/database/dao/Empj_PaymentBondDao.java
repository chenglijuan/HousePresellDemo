package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Empj_PaymentBond;

/*
 * Dao数据库操作：支付保函
 * Company：ZhiShuSZ
 * */
@Repository
public class Empj_PaymentBondDao extends BaseDao<Empj_PaymentBond>
{
	public String getBasicHQL()
    {
    	return "from Empj_PaymentBond where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if companyId??> and company.tableId=:companyId</#if>"
		+ " <#if projectId??> and project.tableId=:projectId</#if>"
		+ " <#if applyDate??> and applyDate=:applyDate</#if>"
		+ " <#if approvalState??> and approvalState=:approvalState</#if>"
		
        + " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
        + " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>"
            + " <#list projectInfoIdArr as projectInfoId>"
                + " <#if projectInfoId_index == 0> (project.tableId = ${projectInfoId?c} )</#if>"
                + " <#if projectInfoId_index != 0> or (project.tableId = ${projectInfoId?c} )</#if>"
            + " </#list>"
        + " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> or (userStart.tableId=:userId) )</#if>"
        + " </#if>"
		
		+ " <#if keyword??> and ( companyName like :keyword or projectName like :keyword or guaranteeNo like :keyword or eCode like :keyword ) </#if>"
		+ " ORDER BY decode(approvalState,'0','0','1','1','2','2','3','3','待提交','0','审核中','1','已完结','2',4) , createTimeStamp DESC ";
    }
	
	public String getSumBasicHQL()
    {
	    
        return "from Empj_PaymentBond where 1=1"
        + " <#if theState??> and theState=:theState</#if>"
        + " <#if eCode??> and eCode=:eCode</#if>"
        + " <#if companyId??> and company.tableId=:companyId</#if>"
        + " <#if projectId??> and project.tableId=:projectId</#if>"
        + " <#if applyDate??> and applyDate=:applyDate</#if>"
        + " <#if approvalState??> and approvalState=:approvalState</#if>";
        
     }
}
