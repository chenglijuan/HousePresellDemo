package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;

/*
 * Dao数据库操作：特殊拨付-申请主表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_SpecialFundAppropriated_AFDao extends BaseDao<Tgpf_SpecialFundAppropriated_AF>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_SpecialFundAppropriated_AF where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if building??> and building=:building</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if approvalState??> and approvalState=:approvalState</#if>"
		+ " <#if theNameOfDevelopCompany??> and theNameOfDevelopCompany=:theNameOfDevelopCompany</#if>"
		+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
		+ " <#if eCodeFromConstruction??> and eCodeFromConstruction=:eCodeFromConstruction</#if>"
		+ " <#if eCodeFromPublicSecurity??> and eCodeFromPublicSecurity=:eCodeFromPublicSecurity</#if>"
		+ " <#if theAccountOfBankAccount??> and theAccountOfBankAccount=:theAccountOfBankAccount</#if>"
		+ " <#if theNameOfBankAccount??> and theNameOfBankAccount=:theNameOfBankAccount</#if>"
		+ " <#if theBankOfBankAccount??> and theBankOfBankAccount=:theBankOfBankAccount</#if>"
		+ " <#if appropriatedType??> and appropriatedType=:appropriatedType</#if>"
		+ " <#if appropriatedRemark??> and appropriatedRemark=:appropriatedRemark</#if>"
		+ " <#if totalApplyAmount??> and totalApplyAmount=:totalApplyAmount</#if>"
		+ " <#if applyDate??> and applyDate=:applyDate</#if>"
		+ " <#if applyState??> and applyState=:applyState</#if>"
		+ " <#if keyword??> and (eCodeFromConstruction like :keyword or theNameOfDevelopCompany like :keyword or theNameOfProject like :keyword or eCode like :keyword)</#if>"
		+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (project.cityRegion.tableId in :cityRegionInfoIdArr or userStart.tableId=:userId)</#if>"
//		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (project.tableId in :projectInfoIdArr or userStart.tableId=:userId)</#if>"
//		+ " <#if timeStampStart??> and APPLYDATE >=:timeStampStart</#if>"
//		+ " <#if timeStampEnd??> and APPLYDATE <=:timeStampEnd</#if>"
		+ " <#if timeStampStart??> and AFPAYOUTDATE >=:timeStampStart</#if>"
		+ " <#if timeStampEnd??> and AFPAYOUTDATE <=:timeStampEnd</#if>"

		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>"
			+ " <#list projectInfoIdArr as projectInfoId>"
				+ " <#if projectInfoId_index == 0> (project.tableId = ${projectInfoId?c} )</#if>"
				+ " <#if projectInfoId_index != 0> or (project.tableId = ${projectInfoId?c} )</#if>"
			+ " </#list>"
		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> )</#if>"
		+ " </#if>"
		+ " order by approvalstate,RECORDTIMESTAMP desc";
		//+ " order by createTimeStamp desc";
    }
	
	public String getCheckHQL()
    {
    	return "from Tgpf_SpecialFundAppropriated_AF where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if buildingId??> and building.tableId=:buildingId</#if>"
		+ " and (approvalState is null or approvalState <> '已完结')";
    }
}
