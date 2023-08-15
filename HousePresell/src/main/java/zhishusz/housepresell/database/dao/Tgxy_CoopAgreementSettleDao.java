package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettle;

/*
 * Dao数据库操作：三方协议结算-主表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgxy_CoopAgreementSettleDao extends BaseDao<Tgxy_CoopAgreementSettle>
{
	public String getBasicHQL()
    {
    	return "from Tgxy_CoopAgreementSettle where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if signTimeStamp??> and signTimeStamp=:signTimeStamp</#if>"
		+ " <#if applySettlementDate??> and applySettlementDate=:applySettlementDate</#if>"
		+ " <#if startSettlementDate??> and startSettlementDate=:startSettlementDate</#if>"
		+ " <#if endSettlementDate??> and endSettlementDate=:endSettlementDate</#if>"
		+ " <#if protocolNumbers??> and protocolNumbers=:protocolNumbers</#if>"
		+ " <#if agentCompany??> and agentCompany=:agentCompany</#if>"
		+ " <#if settlementState??> and settlementState=:settlementState</#if>";
    }
	
	public String getSpecialHQL()
    {
    	return "from Tgxy_CoopAgreementSettle where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if agentCompany??> and agentCompany=:agentCompany</#if>"
		+ " <#if keyword??> and ( eCode like :keyword or agentCompany.theName like :keyword )</#if>"
		+ " <#if settlementState??> and settlementState=:settlementState</#if>";
    }
}
