package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;

/*
 * Dao数据库操作：对账列表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_BalanceOfAccountDao extends BaseDao<Tgpf_BalanceOfAccount>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_BalanceOfAccount where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if billTimeStamp??> and billTimeStamp=:billTimeStamp</#if>"
		+ " <#if bankName??> and bankName=:bankName</#if>"
		+ " <#if escrowedAccount??> and escrowedAccount=:escrowedAccount</#if>"
		+ " <#if escrowedAccountTheName??> and escrowedAccountTheName=:escrowedAccountTheName</#if>"
		+ " <#if centerTotalCount??> and centerTotalCount=:centerTotalCount</#if>"
		+ " <#if centerTotalAmount??> and centerTotalAmount=:centerTotalAmount</#if>"
		+ " <#if bankTotalCount??> and bankTotalCount=:bankTotalCount</#if>"
		+ " <#if bankTotalAmount??> and bankTotalAmount=:bankTotalAmount</#if>"
		+ " <#if cyberBankTotalCount??> and cyberBankTotalCount=:cyberBankTotalCount</#if>"
		+ " <#if cyberBankTotalAmount??> and cyberBankTotalAmount=:cyberBankTotalAmount</#if>"
		+ " <#if accountType??> and accountType=:accountType</#if>"
		+ " <#if reconciliationDate??> and reconciliationDate=:reconciliationDate</#if>"
		+ " <#if tgxy_BankAccountEscrowed??> and tgxy_BankAccountEscrowed=:tgxy_BankAccountEscrowed</#if>"
		+ " <#if reconciliationState??> and reconciliationState=:reconciliationState</#if>"
		+ "  order by NLSSORT(bankName, 'NLS_SORT = SCHINESE_PINYIN_M')" ;
    }
	
	public String getSpecialHQL()
    {
		return "from Tgpf_BalanceOfAccount where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if billTimeStamp??> and billTimeStamp=:billTimeStamp</#if>"
		+ " <#if reconciliationState??> and reconciliationState=:reconciliationState</#if>"
		+ " <#if theType??> and accountType=:theType</#if>"
		+ " <#if centerTotalCount??> and centerTotalCount>:centerTotalCount</#if>"
		+ " <#if keyword??> and (bankName like :keyword or escrowedAccount like :keyword or escrowedAccountTheName like :keyword ) </#if>"
//		+ " <#if reconciliationDate??> order by reconciliationState,centerTotalCount desc,bankName</#if>"
		+ " order by NLSSORT(bankName, 'NLS_SORT = SCHINESE_PINYIN_M')" ;
    }
}
