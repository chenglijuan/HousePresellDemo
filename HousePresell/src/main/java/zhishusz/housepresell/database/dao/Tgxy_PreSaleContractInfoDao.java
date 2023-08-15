package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgxy_ContractInfo;

/*
 * Dao数据库操作：预售系统买卖合同
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgxy_PreSaleContractInfoDao extends BaseDao<Tgxy_ContractInfo>
{
	public String getBasicHQL()
    {
    	return "from Tgxy_PreSaleContractInfo where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if eCodeOfContractRecord??> and eCodeOfContractRecord=:eCodeOfContractRecord</#if>"
		+ " <#if theNameFormCompany??> and theNameFormCompany=:theNameFormCompany</#if>"
		+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
		+ " <#if eCodeFromConstruction??> and eCodeFromConstruction=:eCodeFromConstruction</#if>"
		+ " <#if eCodeOfHouseInfo??> and eCodeOfHouseInfo=:eCodeOfHouseInfo</#if>"
		+ " <#if roomIdOfHouseInfo??> and roomIdOfHouseInfo=:roomIdOfHouseInfo</#if>"
		+ " <#if contractSumPrice??> and contractSumPrice=:contractSumPrice</#if>"
		+ " <#if buildingArea??> and buildingArea=:buildingArea</#if>"
		+ " <#if position??> and position=:position</#if>"
		+ " <#if contractSignDate??> and contractSignDate=:contractSignDate</#if>"
		+ " <#if paymentMethod??> and paymentMethod=:paymentMethod</#if>"
		+ " <#if loanBank??> and loanBank=:loanBank</#if>"
		+ " <#if firstPaymentAmount??> and firstPaymentAmount=:firstPaymentAmount</#if>"
		+ " <#if loanAmount??> and loanAmount=:loanAmount</#if>"
		+ " <#if escrowState??> and escrowState=:escrowState</#if>"
		+ " <#if payDate??> and payDate=:payDate</#if>"
		+ " <#if eCodeOfBuilding??> and eCodeOfBuilding=:eCodeOfBuilding</#if>"
		+ " <#if eCodeFromPublicSecurity??> and eCodeFromPublicSecurity=:eCodeFromPublicSecurity</#if>"
		+ " <#if contractRecordDate??> and contractRecordDate=:contractRecordDate</#if>"
		+ " <#if syncPerson??> and syncPerson=:syncPerson</#if>"
		+ " <#if syncDate??> and syncDate=:syncDate</#if>";
    }
}
