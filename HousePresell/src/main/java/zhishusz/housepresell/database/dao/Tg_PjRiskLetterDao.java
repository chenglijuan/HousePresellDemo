package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_PjRiskLetter;

/*
 * Dao数据库操作：项目风险函
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_PjRiskLetterDao extends BaseDao<Tg_PjRiskLetter>
{
	public String getBasicHQL()
    {
    	return "from Tg_PjRiskLetter where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theNameOfCityRegion??> and theNameOfCityRegion=:theNameOfCityRegion</#if>"
		+ " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
		+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
		+ " <#if releaseDate??> and releaseDate=:releaseDate</#if>"
		+ " <#if deliveryCompany??> and deliveryCompany=:deliveryCompany</#if>"
		+ " <#if riskNotification??> and riskNotification=:riskNotification</#if>"
		+ " <#if basicSituation??> and basicSituation=:basicSituation</#if>"
		+ " <#if letterDateBegin??> and releaseDate >= :letterDateBegin</#if>"
		+ " <#if letterDateEnd??> and releaseDate <= :letterDateEnd</#if>"
		+ " <#if keyword ??> and (eCode like :keyword or  theNameOfCityRegion like :keyword or theNameOfProject like :keyword or developCompany.theName like :keyword) </#if> "
		+ " <#if riskAssessment??> and riskAssessment=:riskAssessment</#if>";
    }
}
