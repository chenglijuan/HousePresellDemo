package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_IncomeForecast;

/*
 * Dao数据库操作：收入预测
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_IncomeForecastDao extends BaseDao<Tg_IncomeForecast>
{
	public String getBasicHQL()
    {
    	return "from Tg_IncomeForecast where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theDay??> and theDay=:theDay</#if>"
		+ " <#if theWeek??> and theWeek=:theWeek</#if>"
		+ " <#if incomeTrendForecast??> and incomeTrendForecast=:incomeTrendForecast</#if>"
		+ " <#if fixedExpire??> and fixedExpire=:fixedExpire</#if>"
		+ " <#if bankLending??> and bankLending=:bankLending</#if>"
		+ " <#if incomeForecast1??> and incomeForecast1=:incomeForecast1</#if>"
		+ " <#if incomeForecast2??> and incomeForecast2=:incomeForecast2</#if>"
		+ " <#if incomeForecast3??> and incomeForecast3=:incomeForecast3</#if>"
		+ " <#if incomeTotal??> and incomeTotal=:incomeTotal</#if>"
	    + " <#if startTimeStamp?? && endTimeStamp??> and theDay>=:startTimeStamp and theDay<=:endTimeStamp</#if>"
	    + " order by theDay asc";

    }
}
