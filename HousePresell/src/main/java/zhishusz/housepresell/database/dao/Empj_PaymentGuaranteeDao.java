package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;

@Repository
public  class Empj_PaymentGuaranteeDao extends BaseDao<Empj_PaymentGuarantee>
{
	public String getBasicHQL()
	{
		return "FROM Empj_PaymentGuarantee WHERE 1=1 AND theState=0 "
				
				//风控抽查条件
				+ " <#if approvalState??> and approvalState=:approvalState</#if>"
				+ " <#if checkStartTimeStamp??> and createTimeStamp>=:checkStartTimeStamp</#if>"
				+ " <#if checkEndTimeStamp??> and createTimeStamp<:checkEndTimeStamp</#if>"
				
				+ " <#if busiState??> and busiState=:busiState</#if>"
		    	+ " <#if keyword??> and (companyName like :keyword or projectName like :keyword or applyDate like :keyword"
		    	+ " or guaranteeNo like :keyword or eCode like :keyword or revokeNo like :keyword)</#if>"
				+ " <#if emmp_CompanyInfo??> and company = :emmp_CompanyInfo </#if>"//开发企业
				+ " <#if empj_ProjectInfo??> and project = :empj_ProjectInfo </#if>"//项目名称
				+ " <#if applyDate??> and applyDate = :applyDate </#if>" //申请日期
				+ " <#if apply??> and ( busiState in (0,1,2,3,4) ) </#if>" //申请
				+ " <#if cancel??> and ( busiState in (2,3,4) and revokeNo <> null ) </#if>" //撤销
				+ " <#if eCode??> and eCode = :eCode </#if>"//开发企业
				
				// 增加权限控制
//				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (project.tableId in :projectInfoIdArr or userStart.tableId=:userId)</#if>"
				
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>"
					+ " <#list projectInfoIdArr as projectInfoId>"
						+ " <#if projectInfoId_index == 0> (project.tableId = ${projectInfoId?c} )</#if>"
						+ " <#if projectInfoId_index != 0> or (project.tableId = ${projectInfoId?c} )</#if>"
					+ " </#list>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> )</#if>"
				+ " </#if>"
				
				+ " order by busiState asc,applyDate desc" //申请日期
				;
	}
	
	public String getProjectHQL()
    {
    	return "from Empj_PaymentGuarantee where 1=1 and theState=0"
    			+ " <#if projectId??> and project.tableId =:projectId</#if>"
    			+ " order by eCodeFromConstruction asc";
    }

	
	
}
