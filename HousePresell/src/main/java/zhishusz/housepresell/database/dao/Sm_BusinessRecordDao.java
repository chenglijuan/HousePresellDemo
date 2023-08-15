package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Sm_BusinessRecord;

/**
 * 
 * @author ZS_XSZ
 *         DAO-业务关联记录表
 *
 */
@Repository
public class Sm_BusinessRecordDao extends BaseDao<Sm_BusinessRecord>
{
	public String getBasicHQL()
	{
		return "from Sm_BusinessRecord where 1=1" 
				+ " <#if theState??> and theState=:theState</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>"
				+ " <#if busiCode??> and busiCode=:busiCode</#if>"
				+ " <#if approvalProcess_AF??> and approvalProcess_AF=:approvalProcess_AF</#if>"
				+ " <#if codeOfBusiness??> and codeOfBusiness=:codeOfBusiness</#if>";
	}
	
	//用于审批流权限过滤
	public String getBasicHQL2()
	{
		return "from Sm_BusinessRecord where 1=1" 
				+ " <#if theState??> and theState=:theState</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>"
				+ " <#if busiCode??> and busiCode=:busiCode</#if>"
				+ " <#if codeOfBusiness??> and codeOfBusiness=:codeOfBusiness</#if>"
				+ " <#if afId??> and approvalProcess_AF.tableId=:afId</#if>"
				+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (cityRegion.tableId in :cityRegionInfoIdArr )</#if>"
//				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (projectInfo.tableId in :projectInfoIdArr )</#if>"
				
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>"
					+ " <#list projectInfoIdArr as projectInfoId>"
						+ " <#if projectInfoId_index == 0> (projectInfo.tableId = ${projectInfoId?c} )</#if>"
						+ " <#if projectInfoId_index != 0> or (projectInfo.tableId = ${projectInfoId?c} )</#if>"
					+ " </#list>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> )</#if>"
				+ " </#if>"
				
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> and (</#if>"
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)>"
					+ " <#list buildingInfoIdIdArr as buildingInfoId>"
						+ " <#if buildingInfoId_index == 0> (buildingInfo.tableId = ${buildingInfoId?c} )</#if>"
						+ " <#if buildingInfoId_index != 0> or (buildingInfo.tableId = ${buildingInfoId?c} )</#if>"
						+ " </#list>"
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> )</#if>"
				+ " </#if>";
	}
}
