package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_PjRiskRating;

/*
 * Dao数据库操作：项目风险评级
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_PjRiskRatingDao extends BaseDao<Tg_PjRiskRating>
{
	public String getBasicHQL()
    {
    	return "from Tg_PjRiskRating where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theNameOfCityRegion??> and theNameOfCityRegion=:theNameOfCityRegion</#if>"
		+ " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
		+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
		+ " <#if operateDate??> and operateDate=:operateDate</#if>"
		+ " <#if theLevel??> and theLevel=:theLevel</#if>"
		+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and cityRegion.tableId in :cityRegionInfoIdArr</#if>"
		+ " <#if developCompany??> and developCompany=:developCompany</#if>"
		+ " <#if project??> and project=:project</#if>"
		+ " <#if cityRegion??> and cityRegion=:cityRegion</#if>"
		+ " <#if cityRegionId??> and cityRegion.tableId=:cityRegionId</#if>"
		+ " <#if operateDateBegin??> and operateDate >= :operateDateBegin</#if>"
		+ " <#if operateDateEnd??> and operateDate <= :operateDateEnd</#if>"
		+ " <#if keyword ??> and (eCode like :keyword or  theNameOfCityRegion like :keyword or theNameOfProject like :keyword or developCompany.theName like :keyword) </#if> "
		+ " <#if riskNotification??> and riskNotification=:riskNotification</#if>";
    }
	
	/*
	 * xsz by time 2018-10-16 09:33:47
	 * 用于项目风险日志选择项目时加载评级
	 */
	public String getBasicDescoperateDateHQL()
    {
    	return "from Tg_PjRiskRating where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if projectId??> and project.tableId=:projectId</#if>"
		+ " <#if theNameOfCityRegion??> and theNameOfCityRegion=:theNameOfCityRegion</#if>"
		+ " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
		+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
		+ " <#if operateDate??> and operateDate=:operateDate</#if>"
		+ " <#if theLevel??> and theLevel=:theLevel</#if>"
		+ " <#if riskNotification??> and riskNotification=:riskNotification</#if>"
		+ " order by operateDate desc";
    }
}
