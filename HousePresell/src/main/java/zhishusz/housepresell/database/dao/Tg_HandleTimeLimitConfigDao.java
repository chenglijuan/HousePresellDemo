package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_HandleTimeLimitConfig;

/*
 * Dao数据库操作：办理时限配置表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_HandleTimeLimitConfigDao extends BaseDao<Tg_HandleTimeLimitConfig>
{
	public String getBasicHQL()
    {
    	return "from Tg_HandleTimeLimitConfig where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theType??> and theType=:theType</#if>"
		+ " <#if completionStandard??> and completionStandard=:completionStandard</#if>"
		+ " <#if limitDayNumber??> and limitDayNumber=:limitDayNumber</#if>"
		+ " <#if counterpartDepartment??> and counterpartDepartment=:counterpartDepartment</#if>"
		+ " <#if lastCfgUser??> and lastCfgUser=:lastCfgUser</#if>"
		+ " <#if lastCfgTimeStamp??> and lastCfgTimeStamp=:lastCfgTimeStamp</#if>";
    }
}
