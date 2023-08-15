package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Empj_PaymentGuaranteeChild;

@Repository
public class Empj_PaymentGuaranteeChildDao extends BaseDao<Empj_PaymentGuaranteeChild>{
	public String getBasicHQL(){
		return "FROM Empj_PaymentGuaranteeChild WHERE 1=1"
				+ " <#if empj_PaymentGuarantee??> and empj_PaymentGuarantee = :empj_PaymentGuarantee </#if>"//主表主键
				+ " <#if empj_BuildingInfo??> and empj_BuildingInfo = :empj_BuildingInfo </#if>"//楼幢
				+ " <#if theState??> and theState = :theState </#if>";			
	}
	
	// 校验是否审核通过
	public String getSpeicalHQL(){
		return "FROM Empj_PaymentGuaranteeChild WHERE "
				+ " <#if condition??> empj_PaymentGuarantee.busiState in :condition</#if> "
		    	+ " <#if theState??> and theState= :theState</#if>"
				+ " <#if empj_BuildingInfo??> and empj_BuildingInfo = :empj_BuildingInfo </#if>"//楼幢
				;				
	}
	
	// 校验
	public String getCheckHQL(){
		return "FROM Empj_PaymentGuaranteeChild WHERE 1=1"
		    	+ " <#if theState??> and theState= :theState</#if>"
				+ " <#if empj_BuildingInfoId??> and empj_BuildingInfo.tableId = :empj_BuildingInfoId </#if>"//楼幢
				+ " and (empj_PaymentGuarantee.busiState is null or empj_PaymentGuarantee.busiState <> '2') ";				
	}
	
}
