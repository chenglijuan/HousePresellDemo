package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_CyberBankStatementDtl;

/*
 * Dao数据库操作：网银对账-后台上传的账单原始Excel数据-明细表
 * Company：ZhiShuSZ
 */
@Repository
public class Tgpf_CyberBankStatementDtlDao extends BaseDao<Tgpf_CyberBankStatementDtl>
{
	public String getBasicHQL()
	{
		return "from Tgpf_CyberBankStatementDtl where 1=1" + " <#if theState??> and theState=:theState</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>" + " <#if eCode??> and eCode=:eCode</#if>"
				+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
				+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
				+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
				+ " <#if tradeTimeStamp??> and tradeTimeStamp=:tradeTimeStamp</#if>"
				+ " <#if recipientAccount??> and recipientAccount=:recipientAccount</#if>"
				+ " <#if recipientName??> and recipientName=:recipientName</#if>"
				+ " <#if remark??> and remark=:remark</#if>" + " <#if income??> and income=:income</#if>"
				+ " <#if payout??> and payout=:payout</#if>" + " <#if balance??> and balance=:balance</#if>"
				+ " <#if reconciliationState??> and reconciliationState=:reconciliationState</#if>"
				+ " <#if reconciliationUser??> and reconciliationUser=:reconciliationUser</#if>"
				+ " <#if tradeAmount??> and tradeAmount=:tradeAmount</#if>"
				+ " <#if reconciliationStamp??> and reconciliationStamp=:reconciliationStamp</#if>"
				+ " <#if mainTable??> and mainTable=:mainTable</#if>"
				+ " <#if coreNo??> and coreno=:coreNo</#if>"
				+ " order by nvl(tgpf_DepositDetailId,999999999)  ";
	}

	public String getBasicHQL2()
	{
		return "from Tgpf_CyberBankStatementDtl where 1=1" + " <#if theState??> and theState=:theState</#if>"
				+ " <#if tradeTimeStamp??> and tradeTimeStamp=:tradeTimeStamp</#if>"
				+ " <#if recipientAccount??> and recipientAccount=:recipientAccount</#if>"
				+ " <#if recipientName??> and recipientName=:recipientName</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>"
				 + " <#if income??> and income=:income</#if>"
				+ " <#if mainTable??> and mainTable=:mainTable</#if>";
	}
	
	public String getBasicHQL3()
	{
		return "from Tgpf_CyberBankStatementDtl where sourceType = '1' " + " <#if theState??> and theState=:theState</#if>"
				+ " <#if tradeTimeStamp??> and tradeTimeStamp=:tradeTimeStamp</#if>"
				+ " <#if recipientAccount??> and recipientAccount=:recipientAccount</#if>"
				+ " <#if recipientName??> and recipientName=:recipientName</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>"
				 + " <#if income??> and income=:income</#if>"
				+ " <#if mainTable??> and mainTable=:mainTable</#if>"
				 + " <#if detailedId??> and detailedId=:detailedId</#if>";
	}
}
