package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_RemainRight;

/*
 * Dao数据库操作：留存权益
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_RemainRightDao extends BaseDao<Tgpf_RemainRight>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_RemainRight where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if enterTimeStamp??> and enterTimeStamp=:enterTimeStamp</#if>"
		+ " <#if buyer??> and buyer=:buyer</#if>"
		+ " <#if theNameOfCreditor??> and theNameOfCreditor=:theNameOfCreditor</#if>"
		+ " <#if idNumberOfCreditor??> and idNumberOfCreditor=:idNumberOfCreditor</#if>"
		+ " <#if eCodeOfContractRecord??> and eCodeOfContractRecord=:eCodeOfContractRecord</#if>"
		+ " <#if eCodeOfTripleAgreement??> and eCodeOfTripleAgreement=:eCodeOfTripleAgreement</#if>"
		+ " <#if srcBusiType??> and srcBusiType=:srcBusiType</#if>"
		+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
		+ " <#if eCodeOfBuilding??> and eCodeOfBuilding=:eCodeOfBuilding</#if>"
		+ " <#if eCodeOfBuildingUnit??> and eCodeOfBuildingUnit=:eCodeOfBuildingUnit</#if>"
		+ " <#if eCodeFromRoom??> and eCodeFromRoom=:eCodeFromRoom</#if>"
		+ " <#if actualDepositAmount??> and actualDepositAmount=:actualDepositAmount</#if>"
		+ " <#if depositAmountFromLoan??> and depositAmountFromLoan=:depositAmountFromLoan</#if>"
		+ " <#if theAccountFromLoan??> and theAccountFromLoan=:theAccountFromLoan</#if>"
		+ " <#if fundProperty??> and fundProperty=:fundProperty</#if>"
		+ " <#if theNameOfBankPayedIn??> and theNameOfBankPayedIn=:theNameOfBankPayedIn</#if>"
		+ " <#if theRatio??> and theRatio=:theRatio</#if>"
		+ " <#if theAmount??> and theAmount=:theAmount</#if>"
		+ " <#if limitedRetainRight??> and limitedRetainRight=:limitedRetainRight</#if>"
		+ " <#if withdrawableRetainRight??> and withdrawableRetainRight=:withdrawableRetainRight</#if>"
		+ " <#if currentDividedAmout??> and currentDividedAmout=:currentDividedAmout</#if>"
		+ " <#if remark??> and remark=:remark</#if>"
		+ " <#if houseId??> and house.tableId=:houseId</#if>"
		+ " <#if buildingRemainRightLogId??> and buildingRemainRightLog.tableId=:buildingRemainRightLogId</#if>"
	    + " order by house asc";
    }
}
