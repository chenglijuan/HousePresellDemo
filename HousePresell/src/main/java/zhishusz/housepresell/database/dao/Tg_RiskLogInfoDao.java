package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_RiskLogInfo;

/*
 * Dao数据库操作：风险日志管理
 * Company：ZhiShuSZ
 */
@Repository
public class Tg_RiskLogInfoDao extends BaseDao<Tg_RiskLogInfo>
{
	public String getBasicHQL()
	{
		return "from Tg_RiskLogInfo where 1=1" + " <#if theState??> and theState=:theState</#if>"
//				+ " <#if busiState??> and busiState=:busiState</#if>" + " <#if eCode??> and eCode=:eCode</#if>"
//				+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
//				+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
//				+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
//				+ " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
//				+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
//				+ " <#if theNameOfCityRegion??> and theNameOfCityRegion=:theNameOfCityRegion</#if>"
//				+ " <#if riskRating??> and riskRating=:riskRating</#if>" + " <#if riskLog??> and riskLog=:riskLog</#if>"
				+ " <#if logDate??> and logDate=:logDate</#if>"
				// + " <#if keyword??> and ( eCode like :keyword or
				// developCompany.theName like :keyword or theNameOfProject like
				// :keyword or theNameOfCityRegion like :keyword or
				// eCodeOfPjRiskRating like :keyword ) </#if>"
				+ " <#if keyword??> and ( eCode like :keyword or theNameOfProject like :keyword or theNameOfCityRegion like :keyword or eCodeOfPjRiskRating like :keyword ) </#if>"
				+ " order by logDate desc ";
	}
}
