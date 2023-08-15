package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_OtherRiskInfo;

/*
 * Dao数据库操作：其他风险信息
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_OtherRiskInfoDao extends BaseDao<Tg_OtherRiskInfo>
{
	public String getBasicHQL()
    {
    	return "from Tg_OtherRiskInfo where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theNameOfCityRegion??> and theNameOfCityRegion=:theNameOfCityRegion</#if>"
		+ " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
		+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
		+ " <#if riskInputDate??> and riskInputDate=:riskInputDate</#if>"
		+ " <#if riskInfo??> and riskInfo=:riskInfo</#if>"
		+ " <#if isUsed??> and isUsed=:isUsed</#if>"
		+ " <#if keyword ??> and (eCode like :keyword or  theNameOfCityRegion like :keyword or theNameOfProject like :keyword or developCompany.theName like :keyword) </#if> "
		+ " <#if remark??> and remark=:remark</#if>";
    }
	
	public String getSpecialHQL()
    {
    	return "from Tg_OtherRiskInfo where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if project??> and project=:project</#if>"
		+ " <#if isUsed??> and isUsed=:isUsed</#if>"
		+ " order by riskInputDate desc";
    }
}
