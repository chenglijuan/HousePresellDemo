package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.database.po.Tg_IncomeExpDepositForecast;

import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：收入预测
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_IncomeExpDepositForecastDao extends BaseDao<Tg_IncomeExpDepositForecast>
{
	public String getBasicHQL()
    {
    	return "from Tg_IncomeExpDepositForecast where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theDay??> and theDay=:theDay</#if>"
		+ " <#if theWeek??> and theWeek=:theWeek</#if>"
	    + " <#if startTimeStamp?? && endTimeStamp??> and theDay>=:startTimeStamp and theDay<=:endTimeStamp</#if>"
		+ " order by theDay asc";

    }
}
