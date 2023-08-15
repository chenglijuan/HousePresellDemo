package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckRatioConfig;

/*
 * Dao数据库操作：风控例行抽查比例配置表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_RiskRoutineCheckRatioConfigDao extends BaseDao<Tg_RiskRoutineCheckRatioConfig>
{
	public String getBasicHQL()
    {
    	return "from Tg_RiskRoutineCheckRatioConfig where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theType??> and theType=:theType</#if>"
		+ " <#if department??> and department=:department</#if>"
		+ " <#if theRatio??> and theRatio=:theRatio</#if>"
		+ " <#if lastCfgUser??> and lastCfgUser=:lastCfgUser</#if>"
		+ " <#if lastCfgTimeStamp??> and lastCfgTimeStamp=:lastCfgTimeStamp</#if>"
		+ " <#if largeBusinessValue??> and largeBusinessValue=:largeBusinessValue</#if>"
		+ " <#if subBusinessValue??> and subBusinessValue=:subBusinessValue</#if>"
		+" and sort is not null"
		+ " order by sort asc";
    }
}
