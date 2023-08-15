package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_ExpForecast;

/*
 * Dao数据库操作：支出预测
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_ExpForecastDao extends BaseDao<Tg_ExpForecast>
{
	public String getBasicHQL()
    {
    	return "from Tg_ExpForecast where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theDay??> and theDay=:theDay</#if>"
		+ " <#if theWeek??> and theWeek=:theWeek</#if>"
		+ " <#if payTrendForecast??> and payTrendForecast=:payTrendForecast</#if>"
		+ " <#if applyAmount??> and applyAmount=:applyAmount</#if>"
		+ " <#if payableFund??> and payableFund=:payableFund</#if>"
		+ " <#if nodeChangePayForecast??> and nodeChangePayForecast=:nodeChangePayForecast</#if>"
		+ " <#if handlingFixedDeposit??> and handlingFixedDeposit=:handlingFixedDeposit</#if>"
		+ " <#if payForecast1??> and payForecast1=:payForecast1</#if>"
		+ " <#if payForecast2??> and payForecast2=:payForecast2</#if>"
		+ " <#if payForecast3??> and payForecast3=:payForecast3</#if>"
		+ " <#if payTotal??> and payTotal=:payTotal</#if>"
		+ " <#if startTimeStamp?? && endTimeStamp??> and theDay>=:startTimeStamp and theDay<=:endTimeStamp</#if>"
	    + " order by theDay asc";
    }
}
