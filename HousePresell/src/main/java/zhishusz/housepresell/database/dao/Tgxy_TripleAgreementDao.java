package zhishusz.housepresell.database.dao;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_ApprovalState;

/*
 * Dao数据库操作：三方协议
 * Company：ZhiShuSZ
 */
@Repository
public class Tgxy_TripleAgreementDao extends BaseDao<Tgxy_TripleAgreement>
{
	public String getBasicHQL()
	{
		return "from Tgxy_TripleAgreement where 1=1" + " <#if theState??> and theState=:theState</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>" + " <#if eCode??> and eCode=:eCode</#if>"

				// 风控抽查条件
				+ " <#if approvalState??> and approvalState=:approvalState</#if>"
				+ " <#if checkStartTimeStamp??> and createTimeStamp>=:checkStartTimeStamp</#if>"
				+ " <#if checkEndTimeStamp??> and createTimeStamp<:checkEndTimeStamp</#if>"
				+ " <#if codeFroTripleOrContract??> and (eCodeOfTripleAgreement=:codeFroTripleOrContract or eCodeOfContractRecord=:codeFroTripleOrContract )</#if>"
				+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
				+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
				+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
				+ " <#if eCodeOfTripleAgreement??> and eCodeOfTripleAgreement=:eCodeOfTripleAgreement</#if>"
				+ " <#if tripleAgreementTimeStamp??> and tripleAgreementTimeStamp=:tripleAgreementTimeStamp</#if>"
				+ " <#if eCodeOfContractRecord??> and eCodeOfContractRecord=:eCodeOfContractRecord</#if>"
				+ " <#if sellerName??> and sellerName=:sellerName</#if>"
				+ " <#if escrowCompany??> and escrowCompany=:escrowCompany</#if>"
				+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
				+ " <#if eCodeOfBuilding??> and eCodeOfBuilding=:eCodeOfBuilding</#if>"
				+ " <#if eCodeFromConstruction??> and eCodeFromConstruction=:eCodeFromConstruction</#if>"
				+ " <#if eCodeOfUnit??> and eCodeOfUnit=:eCodeOfUnit</#if>"
				+ " <#if unitRoom??> and unitRoom=:unitRoom</#if>"
				+ " <#if buildingArea??> and buildingArea=:buildingArea</#if>"
				+ " <#if contractAmount??> and contractAmount=:contractAmount</#if>"
				+ " <#if firstPayment??> and firstPayment=:firstPayment</#if>"
				+ " <#if loanAmount??> and loanAmount=:loanAmount</#if>"
				+ " <#if buyerInfoSet??> and buyerInfoSet=:buyerInfoSet</#if>"
				+ " <#if theStateOfTripleAgreement??> and theStateOfTripleAgreement=:theStateOfTripleAgreement</#if>"
				+ " <#if theStateOfTripleAgreementFiling??> and theStateOfTripleAgreementFiling=:theStateOfTripleAgreementFiling</#if>"
				+ " <#if theStateOfTripleAgreementEffect??> and theStateOfTripleAgreementEffect in :theStateOfTripleAgreementEffect</#if>"
				+ " <#if printMethod??> and printMethod=:printMethod</#if>"
				+ " <#if theAmountOfRetainedEquity??> and theAmountOfRetainedEquity=:theAmountOfRetainedEquity</#if>"
				+ " <#if theAmountOfInterestRetained??> and theAmountOfInterestRetained=:theAmountOfInterestRetained</#if>"
				+ " <#if theAmountOfInterestUnRetained??> and theAmountOfInterestUnRetained=:theAmountOfInterestUnRetained</#if>"
				+ " <#if totalAmountOfHouse??> and totalAmountOfHouse=:totalAmountOfHouse</#if>"
				+ " <#if house??> and house=:house</#if>"
				+ " <#if tgxy_BankAccountEscrowed??> and tgxy_BankAccountEscrowed=:tgxy_BankAccountEscrowed</#if>"
				+ " <#if projectId??> and project.tableId =:projectId</#if>"
				+ " <#if companyId??> and project.developCompany.tableId =:companyId</#if>"
				+ " <#if isAgency??> and project.developCompany.tableId =:companyId</#if>"
				
				+ " <#if startDate??> and tripleAgreementTimeStamp >=:startDate</#if>"
				+ " <#if endDate??> and tripleAgreementTimeStamp <=:endDate</#if>"
				+ " <#if cityRegionId??> and project.cityRegion.tableId =:cityRegionId</#if>"
				// + " <#if keyword??> and ( eCodeOfTripleAgreement like
				// :keyword or eCodeOfContractRecord like :keyword or buyerName
				// like :keyword or sellerName like :keyword or theNameOfProject
				// like :keyword ) </#if>"
				// + " order by createTimeStamp desc";

				/*
				 * 1.协议状态
				 * 2.三方协议号、合同备案号、买受人名称
				 * 3.开发企业名称、项目名称、三方协议号
				 */
				+ " <#if keyword??> and ( eCodeOfTripleAgreement like :keyword or eCodeOfContractRecord like :keyword or buyerName like :keyword or sellerName like :keyword ) </#if>"

				/*
				 * xsz by time 2018-11-13 09:42:24
				 * 数据权限过滤
				 * 
				 */

//				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> and (buildingInfo.tableId in :buildingInfoIdIdArr or (userStart.tableId=:userId) )</#if>"
//				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (project.tableId in :projectInfoIdArr or (userStart.tableId=:userId) )</#if>"
				
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>"
					+ " <#list projectInfoIdArr as projectInfoId>"
						+ " <#if projectInfoId_index == 0> (project.tableId = ${projectInfoId?c} )</#if>"
						+ " <#if projectInfoId_index != 0> or (project.tableId = ${projectInfoId?c} )</#if>"
					+ " </#list>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> or (userStart.tableId=:userId) )</#if>"
				+ " </#if>"
				
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> and (</#if>"
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)>"
					+ " <#list buildingInfoIdIdArr as buildingInfoId>"
						+ " <#if buildingInfoId_index == 0> (buildingInfo.tableId = ${buildingInfoId?c} )</#if>"
						+ " <#if buildingInfoId_index != 0> or (buildingInfo.tableId = ${buildingInfoId?c} )</#if>"
						+ " </#list>"
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> or (userStart.tableId=:userId) )</#if>"
				+ " </#if>"
				/*+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (project.cityRegion.tableId in :cityRegionInfoIdArr or (userStart.tableId=:userId) )</#if>"*/
				
				+ " order by theStateOfTripleAgreement ,DECODE(busiState ,'待提交',-2,'审核中',-1,'已完结',0 ),sellerName,theNameOfProject,eCodeOfTripleAgreement";
	}

	/*
	 * jian.wang by time 2018-8-16
	 * 1.修改查询条件 使得根据关键字（keyword）即可查询
	 * （三方协议号 合同备案号 房屋坐落 买受人名称 公司名称 项目名称）
	 * 2.添加排序规则（创建时间降序）
	 * 
	 * 调用service:
	 * Tgxy_TripleAgreementListService
	 * 
	 * @version 2.0
	 * 
	 * @return
	 */
	public String getBasicHQLM()
	{
		return "from Tgxy_TripleAgreement where 1=1"
				+ " <#if keyword??> and ( eCode like :keyword or eCodeOfContractRecord like :keyword or buyerInfoSet like :keyword or theNameOfProject like :keyword ) </#if>"
				+ " <#if theState??> and theState=:theState</#if>" + " order by createTimeStamp desc";
	}

	public String getSpecialHQL() // settlementSOfTA
	{
		return "from Tgxy_TripleAgreement where 1=1" 
				/*+ " <#if beginTime??> and tripleAgreementTimeStamp >= :beginTime</#if>"
				+ " <#if endTime??> and tripleAgreementTimeStamp <= :endTime</#if>"
				+ " <#if company??> and userStart.company = :company</#if>"*/
				+ " <#if startDate??> and tripleAgreementTimeStamp >=:startDate</#if>"
				+ " <#if endDate??> and tripleAgreementTimeStamp <:endDate</#if>"
				+ " <#if busiState??> and nvl(house.settlementStateOfTripleAgreement,0) not in (1,2) </#if>"
				+ " <#if isAgency??> and project.developCompany.tableId =:companyId</#if>"
				+ " <#if approvalState??> and approvalState = :approvalState</#if>"
				
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>"
					+ " <#list projectInfoIdArr as projectInfoId>"
						+ " <#if projectInfoId_index == 0> (project.tableId = ${projectInfoId?c} )</#if>"
						+ " <#if projectInfoId_index != 0> or (project.tableId = ${projectInfoId?c} )</#if>"
					+ " </#list>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> or (userStart.tableId=:userId) )</#if>"
				+ " </#if>"
				
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> and (</#if>"
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)>"
					+ " <#list buildingInfoIdIdArr as buildingInfoId>"
						+ " <#if buildingInfoId_index == 0> (buildingInfo.tableId = ${buildingInfoId?c} )</#if>"
						+ " <#if buildingInfoId_index != 0> or (buildingInfo.tableId = ${buildingInfoId?c} )</#if>"
						+ " </#list>"
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> or (userStart.tableId=:userId) )</#if>"
				+ " </#if>"
				+ " <#if theState??> and theState=:theState order by tripleAgreementTimeStamp desc</#if>";
	}

	/**
	 * xsz by time 2018-11-28 11:50:21
	 * 根据合同备案号查询是否存在有效的三方协议
	 * 
	 * 调用service:
	 * Tb_b_contractDetailDetailSplitService
	 * 
	 * @return 查询语句
	 */
	public String getTripleAgreementContractHQL()
	{
		return "from Tgxy_TripleAgreement where 1=1" + " <#if theState??> and theState=:theState</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>"
				+ " <#if eCodeOfContractRecord??> and eCodeOfContractRecord=:eCodeOfContractRecord</#if>"
				+ " and theStateOfTripleAgreementEffect in ('0','1','2')";
	}
	
	/**
	 * xsz by time 2019-1-10 16:58:40
	 * 根据开发企业或项目加载统计协议量
	 * 
	 * 调用service:
	 * Sm_HomePageProjectAccountService
	 * 
	 * @return 查询语句
	 */
	public String getTripleAgreementAcount()
	{
		return "from Tgxy_TripleAgreement where 1=1" + " <#if theState??> and theState=:theState</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>"
				+ " <#if projectId??> and project.tableId=:projectId</#if>"
				+ " <#if company??> and project.developCompany=:company</#if>";
	}
}
