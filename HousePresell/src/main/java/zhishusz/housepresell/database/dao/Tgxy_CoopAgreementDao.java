package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgxy_CoopAgreement;

/*
 * Dao数据库操作：合作协议
 * Company：ZhiShuSZ
 */
@Repository
public class Tgxy_CoopAgreementDao extends BaseDao<Tgxy_CoopAgreement>
{
	public String getBasicHQL()
	{
		return "from Tgxy_CoopAgreement where 1=1" + " <#if theState??> and theState=:theState</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>" + " <#if eCode??> and eCode=:eCode</#if>"
				+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
				+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
				+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
				+ " <#if escrowCompany??> and escrowCompany=:escrowCompany</#if>"
				+ " <#if theVersion??> and theVersion=:theVersion</#if>"
				+ " <#if signTimeStamp??> and signTimeStamp=:signTimeStamp</#if>"
				+ " <#if theNameOfDevelopCompany??> and theNameOfDevelopCompany=:theNameOfDevelopCompany</#if>"
				+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
				+ " <#if eCodeOfBuilding??> and eCodeOfBuilding=:eCodeOfBuilding</#if>"
				+ " <#if eCodeFromConstruction??> and eCodeFromConstruction=:eCodeFromConstruction</#if>"
				+ " <#if eCodeFromPublicSecurity??> and eCodeFromPublicSecurity=:eCodeFromPublicSecurity</#if>"
				+ " <#if OtherAgreedMatters??> and OtherAgreedMatters=:OtherAgreedMatters</#if>"
				+ " <#if disputeResolution??> and disputeResolution=:disputeResolution</#if>"
				+ " <#if busiProcessState??> and busiProcessState=:busiProcessState</#if>"
				+ " <#if agreeState??> and agreeState=:agreeState</#if>";
	}

	/*
	 * xsz by time 2018-8-16 14:25:08
	 * 1.修改查询条件 使得根据关键字（keyword）即可查询
	 * （合作协议编号 银行名称 开户行名称）
	 * 2.添加排序规则（创建时间降序）
	 * 
	 * 调用service:
	 * Tgxy_CoopAgreementListService
	 * 
	 * @version 2.0
	 * 
	 * @return
	 */
	public String getBasicHQL2()
	{
		return "from Tgxy_CoopAgreement where 1=1"
				+ " <#if keyword??> and ( eCode like :keyword or theNameOfBank like :keyword or theNameOfDepositBank like :keyword ) </#if>"
				// + " <#if signDate??> and signDate=:signDate</#if>" + " and
				// theState = '0' order by createTimeStamp desc";
				
				/*
				 * 1.签署日期
				 * 2.合作协议编号、银行名称、开户行
				 * 3.签署日期↑、合作协议编号、银行名称、
				 */
				+ " <#if signDate??> and signDate=:signDate</#if>"
				+ " and theState = '0' order by signDate asc,eCode,theNameOfBank";
	}
}
