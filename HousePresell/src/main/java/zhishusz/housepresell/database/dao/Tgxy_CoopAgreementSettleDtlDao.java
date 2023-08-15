package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettleDtl;

/*
 * Dao数据库操作：三方协议结算-子表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgxy_CoopAgreementSettleDtlDao extends BaseDao<Tgxy_CoopAgreementSettleDtl>
{
	public String getBasicHQL()
    {
    	return "from Tgxy_CoopAgreementSettleDtl where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if mainTable??> and mainTable=:mainTable</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if agreementDate??> and agreementDate=:agreementDate</#if>"
		+ " <#if seller??> and seller=:seller</#if>"
		+ " <#if buyer??> and buyer=:buyer</#if>"
		+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
		+ " <#if eCodeOfBuilding??> and eCodeOfBuilding=:eCodeOfBuilding</#if>"
		+ " <#if tgxy_TripleAgreement??> and tgxy_TripleAgreement=:tgxy_TripleAgreement</#if>"
		+ " <#if eCodeFromConstruction??> and eCodeFromConstruction=:eCodeFromConstruction</#if>";
    }
}
