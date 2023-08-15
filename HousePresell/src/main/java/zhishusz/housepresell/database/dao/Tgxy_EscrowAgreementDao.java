package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;

/*
 * Dao数据库操作：托管合作协议
 * Company：ZhiShuSZ
 */
@Repository
public class Tgxy_EscrowAgreementDao extends BaseDao<Tgxy_EscrowAgreement>
{
	public String getBasicHQL()
	{
		return "from Tgxy_EscrowAgreement where 1=1" + " <#if theState??> and theState=:theState</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>" + " <#if eCode??> and eCode=:eCode</#if>"
				//风控抽查条件
				+ " <#if approvalState??> and approvalState=:approvalState</#if>"
				+ " <#if checkStartTimeStamp??> and createTimeStamp>=:checkStartTimeStamp</#if>"
				+ " <#if checkEndTimeStamp??> and createTimeStamp<:checkEndTimeStamp</#if>"
				
				+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
				+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
				+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
				+ " <#if escrowCompany??> and escrowCompany=:escrowCompany</#if>"
				+ " <#if agreementVersion??> and agreementVersion=:agreementVersion</#if>"
				+ " <#if eCodeOfAgreement??> and eCodeOfAgreement=:eCodeOfAgreement</#if>"
				+ " <#if contractApplicationDate??> and contractApplicationDate=:contractApplicationDate</#if>"
				+ " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
				+ " <#if theNameOfDevelopCompany??> and theNameOfDevelopCompany=:theNameOfDevelopCompany</#if>"
				+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
				+ " <#if buildingInfoList??> and buildingInfoList=:buildingInfoList</#if>"
				+ " <#if OtherAgreedMatters??> and OtherAgreedMatters=:OtherAgreedMatters</#if>"
				+ " <#if disputeResolution??> and disputeResolution=:disputeResolution</#if>"
				+ " <#if businessProcessState??> and businessProcessState in :businessProcessState</#if>"
				+ " <#if agreementState??> and agreementState=:agreementState</#if>"
				+ " <#if projectId??> and project.tableId=:projectId</#if>"
				+ " <#if eCodeOfContractRecord??> and eCodeOfContractRecord=:eCodeOfContractRecord</#if>"
				+ " <#if buildingInfoCodeList??> and buildingInfoCodeList like :buildingInfoCodeList</#if>"
				+ " <#if startDate??> and contractApplicationDate >=:startDate</#if>"
				+ " <#if endDate??> and contractApplicationDate <=:endDate</#if>"
				+ " <#if cityRegionId??> and cityRegion.tableId =:cityRegionId</#if>"
				// + " <#if keyword??> and ( agreementVersion like :keyword or
				// eCodeOfAgreement like :keyword or theNameOfDevelopCompany
				// like :keyword or theNameOfProject like :keyword or
				// buildingInfoCodeList like :keyword ) </#if>"
				// + " order by createTimeStamp desc";

				/*
				 * 1.开发企业名称、项目名称签约申请日期查询
				 * 2.协议版本
				 * 3.协议版本、开发企业名称、项目名称、施工编号↓
				 * 
				 */
				+ " <#if keyword??> and ( agreementVersion like :keyword or theNameOfDevelopCompany like :keyword or theNameOfProject like :keyword or eCodeOfAgreement like :keyword ) </#if>"
				
				/*
				 * xsz by time 2018-11-12 15:08:02
				 * 加数据权限控制
				 * 开发企业、区域、项目
				 */
				
				+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (cityRegion.tableId in :cityRegionInfoIdArr or userStart.tableId=:userId)</#if>"
//				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (project.tableId in :projectInfoIdArr or userStart.tableId=:userId)</#if>"
				
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>"
					+ " <#list projectInfoIdArr as projectInfoId>"
						+ " <#if projectInfoId_index == 0> (project.tableId = ${projectInfoId?c} )</#if>"
						+ " <#if projectInfoId_index != 0> or (project.tableId = ${projectInfoId?c} )</#if>"
					+ " </#list>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> )</#if>"
				+ " </#if>"
				
				+ " order by businessProcessState ,contractApplicationDate desc,agreementVersion,theNameOfDevelopCompany,theNameOfProject,buildingInfoCodeList desc";
	}

	/**
	 * jian.wang
	 * 1.修改查询条件 使得根据关键字（keyword）即可查询
	 * （协议编号 公司名称 项目名称）
	 * 
	 * 
	 * 调用service:
	 * Tgxy_EscrowAgreementListService
	 * 
	 * @version 2.0
	 * @return
	 */
	public String getBasicHQLM()
	{
		return "from Tgxy_EscrowAgreement where 1=1"
				+ "<#if keyword??>and (eCode like :keyword or theNameOfDevelopCompany like :keyword or theNameOfProject like :keyword or theNameOfProject like :keyword)</#if>"
				+ "<#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>" + " and theState = '0'"
				+ "<#if buildingInfoCodeList??> and buildingInfoCodeList like :BuildingInfoCodeList</#if>";
	}

	public String getBasicHQL2()
	{
		return "from Tgxy_EscrowAgreement where 1=1" + " <#if theState??> and theState=:theState</#if>"
				+ "and businessProcessState in (5,6,7)"
				+ " <#if agreementState??> and agreementState=:agreementState</#if>"
				+ " <#if projectId??> and project.tableId=:projectId</#if>"
				+ " <#if buildingInfoCodeList??> and buildingInfoCodeList like :buildingInfoCodeList</#if>"
				+ " <#if keyword??> and ( agreementVersion like :keyword or eCodeOfAgreement like :keyword or theNameOfDevelopCompany like :keyword or theNameOfProject like :keyword or buildingInfoCodeList like :keyword ) </#if>"
				+ " order by createTimeStamp desc";
	}

	//查询托管协议
	public String getBasicHQL3()
	{
		return "from Tgxy_EscrowAgreement where 1=1" + " <#if theState??> and theState=:theState</#if>"
				+ " <#if theState??> and theState=:theState</#if>"
				+ " <#if agreementState??> and agreementState=:agreementState</#if>"
				+ " <#if theNameOfProject??> and theNameOfProject =:theNameOfProject</#if>"
//				+ " <#if buildingInfoCodeList??> and isContained(BUILDINGINFOCODELIST "+":buildingInfoCodeList"+ ", 0, ',', ',') > 0 </#if>"
				+ " <#if buildingInfoCodeList??> and isContained(BUILDINGINFOCODELIST,:buildingInfoCodeList, 0, ',', ',') > 0 </#if>"
				+ " and rownum<=1 order by createTimeStamp desc ";
	}
	
	public String getRecordTime()
	{
		return "from Tgxy_EscrowAgreement where 1=1"
				+ " <#if project??> and project=:project</#if>"
				+ " <#if theState??> and theState=:theState</#if>"
				+ "<#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
				+ "<#if developCompany??> and developCompany like :developCompany</#if>"
				+ "<#if cityRegionId??> and cityRegion.tableId = :cityRegionId</#if>"
				+ "<#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (cityRegion.tableId in :cityRegionInfoIdArr)</#if>"
				+ " order by recordTimeStamp desc";
	}

	public String getRecordTime2()
	{
		return "from Tgxy_EscrowAgreement where 1=1"
				+ " <#if project??> and project=:project</#if>"
				+ " <#if theState??> and theState=:theState</#if>"
				+ "<#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
				+ "<#if developCompany??> and developCompany like :developCompany</#if>"
				+ "<#if cityRegionId??> and cityRegion.tableId = :cityRegionId</#if>"
				+ "<#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (cityRegion.tableId in :cityRegionInfoIdArr)</#if>"
				+ " and approvalState = '已完结'"
				+ " order by recordTimeStamp desc";
	}
}
