package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Empj_PresellDocumentInfo;

/*
 * Dao数据库操作：预售证信息
 * Company：ZhiShuSZ
 * */
@Repository
public class Empj_PresellDocumentInfoDao extends BaseDao<Empj_PresellDocumentInfo>
{
	public String getBasicHQL()
    {
    	return "from Empj_PresellDocumentInfo where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
		+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
		+ " <#if addressOfProject??> and addressOfProject=:addressOfProject</#if>"
		+ " <#if project??> and project=:project</#if>"
		+ " <#if buildingInfoSet??> and buildingInfoSet=:buildingInfoSet</#if>";
    }
}
