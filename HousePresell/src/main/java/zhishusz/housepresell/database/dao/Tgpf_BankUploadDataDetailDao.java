package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_BankUploadDataDetail;

/*
 * Dao数据库操作：银行对账单数据
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_BankUploadDataDetailDao extends BaseDao<Tgpf_BankUploadDataDetail>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_BankUploadDataDetail where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theNameOfBank??> and theNameOfBank=:theNameOfBank</#if>"
		+ " <#if theNameOfBankBranch??> and theNameOfBankBranch=:theNameOfBankBranch</#if>"
		+ " <#if theNameOfBankAccountEscrowed??> and theNameOfBankAccountEscrowed=:theNameOfBankAccountEscrowed</#if>"
		+ " <#if theAccountBankAccountEscrowed??> and theAccountBankAccountEscrowed=:theAccountBankAccountEscrowed</#if>"
		+ " <#if theAccountOfBankAccountEscrowed??> and theAccountOfBankAccountEscrowed=:theAccountOfBankAccountEscrowed</#if>"
		+ " <#if tradeAmount??> and tradeAmount=:tradeAmount</#if>"
		+ " <#if enterTimeStamp??> and enterTimeStamp=:enterTimeStamp</#if>"
		+ " <#if recipientAccount??> and recipientAccount=:recipientAccount</#if>"
		+ " <#if recipientName??> and recipientName=:recipientName</#if>"
		+ " <#if lastCfgUser??> and lastCfgUser=:lastCfgUser</#if>"
		+ " <#if lastCfgTimeStamp??> and lastCfgTimeStamp=:lastCfgTimeStamp</#if>"
		+ " <#if bkpltNo??> and bkpltNo=:bkpltNo</#if>"
		+ " <#if eCodeOfTripleAgreement??> and eCodeOfTripleAgreement=:eCodeOfTripleAgreement</#if>"
		+ " <#if reconciliationState??> and reconciliationState=:reconciliationState</#if>"
		+ " <#if reconciliationStamp??> and reconciliationStamp=:reconciliationStamp</#if>"
		+ " <#if remark??> and remark=:remark</#if>"
		+ " <#if coreNo??> and coreNo=:coreNo</#if>"
		+ " <#if bankBranch??> and bankBranch=:bankBranch</#if>"		
		+ " <#if reconciliationUser??> and reconciliationUser=:reconciliationUser</#if>";
    }
}
