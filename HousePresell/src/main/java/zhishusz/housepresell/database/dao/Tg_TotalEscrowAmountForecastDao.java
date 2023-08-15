package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_TotalEscrowAmountForecast;

/*
 * Dao数据库操作：托管总资金的预测结存
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_TotalEscrowAmountForecastDao extends BaseDao<Tg_TotalEscrowAmountForecast>
{
	public String getBasicHQL()
    {
    	return "from Tg_TotalEscrowAmountForecast where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if lastAmount??> and lastAmount=:lastAmount</#if>"
		+ " <#if incomeTotal??> and incomeTotal=:incomeTotal</#if>"
		+ " <#if payTotal??> and payTotal=:payTotal</#if>"
		+ " <#if currentAmount??> and currentAmount=:currentAmount</#if>"
		+ " <#if escrowAmountReferenceValue??> and escrowAmountReferenceValue=:escrowAmountReferenceValue</#if>"
		+ " <#if escrowAmount??> and escrowAmount=:escrowAmount</#if>"
		+ " <#if referenceValue1??> and referenceValue1=:referenceValue1</#if>"
		+ " <#if referenceValue2??> and referenceValue2=:referenceValue2</#if>";
    }
}
