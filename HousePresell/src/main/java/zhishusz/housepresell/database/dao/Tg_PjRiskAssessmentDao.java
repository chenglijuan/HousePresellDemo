package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_PjRiskAssessment;

/*
 * Dao数据库操作：项目风险评估
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_PjRiskAssessmentDao extends BaseDao<Tg_PjRiskAssessment>
{
	public String getBasicHQL()
    {
    	return "from Tg_PjRiskAssessment where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theNameOfCityRegion??> and theNameOfCityRegion=:theNameOfCityRegion</#if>"
		+ " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
		+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
		
		+ " <#if developCompany??> and developCompany=:developCompany</#if>"
		+ " <#if project??> and project=:project</#if>"
		+ " <#if cityRegion??> and cityRegion=:cityRegion</#if>"
		+ " <#if assessDate??> and assessDate >= :assessDate</#if>"
		+ " <#if assessDateEnd??> and assessDate <= :assessDateEnd</#if>"
		+" <#if keyword ??> and (eCode like :keyword or  theNameOfCityRegion like :keyword or theNameOfProject like :keyword ) </#if> "
		+ " <#if riskAssessment??> and riskAssessment=:riskAssessment</#if>";
    	
    }
	
	public String getSpecialHQL()
    {
    	return "from Tg_PjRiskAssessment where 1=1"		
		+ " <#if project??> and project=:project</#if>"
		+ " <#if assessDate??> and assessDate >= :assessDate</#if>"
		+ " <#if riskAssessment??> and riskAssessment=:riskAssessment</#if>"
		+ " order by assessDate desc";	
		
    }
}
