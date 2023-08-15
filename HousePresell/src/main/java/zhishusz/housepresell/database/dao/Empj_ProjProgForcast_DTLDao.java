package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Empj_ProjProgForcast_DTL;

/*
 * Dao数据库操作：工程进度巡查-子
 * Company：ZhiShuSZ
 */
@Repository
public class Empj_ProjProgForcast_DTLDao extends BaseDao<Empj_ProjProgForcast_DTL>
{
	public String getBasicHQL()
	{
		return "from Empj_ProjProgForcast_DTL where 1=1" + " <#if theState??> and theState=:theState</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>" + " <#if eCode??> and eCode=:eCode</#if>"
				+ " <#if approvalState??> and approvalState=:approvalState</#if>"
				+ " <#if afCode??> and afCode=:afCode</#if>"
				+ " <#if afEntity??> and afEntity=:afEntity</#if>"
				+ " <#if hasAchieve??> and hasAchieve=:hasAchieve</#if>"
				
				+ " <#if keyword??> and ( agreementVersion like :keyword or theNameOfDevelopCompany like :keyword or theNameOfProject like :keyword or eCodeOfAgreement like :keyword ) </#if>"
				+ " ORDER BY to_number(regexp_substr(buildCode,'[0-9]*[0-9]',1))";
	}
	
	public String getHandlerPicHQL()
	{
		return "from Empj_ProjProgForcast_DTL where 1=1" + " <#if theState??> and theState=:theState</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>" + " <#if eCode??> and eCode=:eCode</#if>"
				+ " <#if approvalState??> and approvalState=:approvalState</#if>"
				+ " <#if afCode??> and afCode=:afCode</#if>"
				+ " <#if afEntity??> and afEntity=:afEntity</#if>"
				+ " <#if handleState??> and NVL(handleState,'0') = '0'</#if>"
				
				+ " <#if keyword??> and ( agreementVersion like :keyword or theNameOfDevelopCompany like :keyword or theNameOfProject like :keyword or eCodeOfAgreement like :keyword ) </#if>"
				+ " ORDER BY to_number(regexp_substr(buildCode,'[0-9]*[0-9]',1))";
	}

	
}
