package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AFDtl;

/*
 * Dao数据库操作：特殊拨付-申请子表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_SpecialFundAppropriated_AFDtlDao extends BaseDao<Tgpf_SpecialFundAppropriated_AFDtl>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_SpecialFundAppropriated_AFDtl where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if specialAppropriated??> and specialAppropriated=:specialAppropriated</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theCodeOfAf??> and theCodeOfAf=:theCodeOfAf</#if>"
		+ " <#if accountOfEscrowed??> and accountOfEscrowed=:accountOfEscrowed</#if>"
		+ " <#if theNameOfEscrowed??> and theNameOfEscrowed=:theNameOfEscrowed</#if>"
		+ " <#if applyRefundPayoutAmount??> and applyRefundPayoutAmount=:applyRefundPayoutAmount</#if>"
		+ " <#if appliedAmount??> and appliedAmount=:appliedAmount</#if>"
		+ " <#if accountBalance??> and accountBalance=:accountBalance</#if>"
		+ " <#if billNumber??> and billNumber=:billNumber</#if>"
		+ " <#if payoutChannel??> and payoutChannel=:payoutChannel</#if>"
		+ " <#if payoutDate??> and payoutDate=:payoutDate</#if>"
		+ " <#if payoutState??> and payoutState=:payoutState</#if>"
		+ " order by createTimeStamp desc";
    }
}
