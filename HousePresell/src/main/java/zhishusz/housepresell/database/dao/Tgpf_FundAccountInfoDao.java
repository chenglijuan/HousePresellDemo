package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_FundAccountInfo;

/*
 * Dao数据库操作：推送给财务系统-设置
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_FundAccountInfoDao extends BaseDao<Tgpf_FundAccountInfo>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_FundAccountInfo where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theNameOfCompany??> and theNameOfCompany=:theNameOfCompany</#if>"
		+ " <#if eCodeOfCompany??> and eCodeOfCompany=:eCodeOfCompany</#if>"
		+ " <#if fullNameOfCompanyFromFinanceSystem??> and fullNameOfCompanyFromFinanceSystem=:fullNameOfCompanyFromFinanceSystem</#if>"
		+ " <#if shortNameOfCompanyFromFinanceSystem??> and shortNameOfCompanyFromFinanceSystem=:shortNameOfCompanyFromFinanceSystem</#if>"
		+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
		+ " <#if eCodeOfProject??> and eCodeOfProject=:eCodeOfProject</#if>"
		+ " <#if fullNameOfProjectFromFinanceSystem??> and fullNameOfProjectFromFinanceSystem=:fullNameOfProjectFromFinanceSystem</#if>"
		+ " <#if shortNameOfProjectFromFinanceSystem??> and shortNameOfProjectFromFinanceSystem=:shortNameOfProjectFromFinanceSystem</#if>"
		+ " <#if eCodeFromConstruction??> and eCodeFromConstruction=:eCodeFromConstruction</#if>"
		+ " <#if eCodeOfBuilding??> and eCodeOfBuilding=:eCodeOfBuilding</#if>"
		+ " <#if fullNameOfBuildingFromFinanceSystem??> and fullNameOfBuildingFromFinanceSystem=:fullNameOfBuildingFromFinanceSystem</#if>"
		+ " <#if shortNameOfBuildingFromFinanceSystem??> and shortNameOfBuildingFromFinanceSystem=:shortNameOfBuildingFromFinanceSystem</#if>"
		+ " <#if building??> and building=:building</#if>"
		+ " <#if operateType??> and operateType=:operateType</#if>";
    }
	
	public String getSpecialHQL()
    {
    	return "from Tgpf_FundAccountInfo where 1=1"
    	+ " <#if theState??> and theState=:theState</#if>"
//    	+ " <#if keyword??> and (theNameOfCompany like :keyword or fullNameOfCompanyFromFinanceSystem like :keyword or configureTime like :keyword"
//    	+ " or project.theName like :keyword or fullNameOfProjectFromFinanceSystem like :keyword or eCodeFromConstruction like :keyword"
//    	+ " or fullNameOfBuildingFromFinanceSystem like :keyword or shortNameOfBuildingFromFinanceSystem like :keyword or configureUser like :keyword)</#if>"
		+ " <#if keyword??> and (eCodeFromConstruction like :keyword or eCodeOfBuilding like :keyword or fullNameOfBuildingFromFinanceSystem like :keyword"
		+ " or fullNameOfCompanyFromFinanceSystem like :keyword or fullNameOfProjectFromFinanceSystem like :keyword"
		+ " or shortNameOfBuildingFromFinanceSystem like :keyword or shortNameOfCompanyFromFinanceSystem like :keyword or shortNameOfProjectFromFinanceSystem like :keyword"
		+ " or theNameOfCompany like :keyword or theNameOfProject like :keyword)</#if>"
    	+ " order by createTimeStamp desc,building.recordTimeStamp desc";
    }
}
