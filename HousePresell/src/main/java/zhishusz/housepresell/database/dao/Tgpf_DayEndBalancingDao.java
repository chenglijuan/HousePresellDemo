package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_DayEndBalancing;

/*
 * Dao数据库操作：业务对账-日终结算
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_DayEndBalancingDao extends BaseDao<Tgpf_DayEndBalancing>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_DayEndBalancing where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if bankName??> and bankName=:bankName</#if>"
		+ " <#if escrowedAccount??> and escrowedAccount=:escrowedAccount</#if>"
		+ " <#if escrowedAccountTheName??> and escrowedAccountTheName=:escrowedAccountTheName</#if>"
//		+ " <#if totalCount??> and totalCount=:totalCount</#if>"
		+ " <#if totalAmount??> and totalAmount=:totalAmount</#if>"
		+ " <#if billTimeStamp??> and billTimeStamp=:billTimeStamp</#if>"
		+ " <#if recordState??> and recordState=:recordState</#if>"
		+ " <#if settlementState??> and settlementState=:settlementState</#if>"
		+ " <#if judgeState??> and settlementState > :judgeState</#if>"
		+ " <#if settlementTime??> and settlementTime=:settlementTime</#if>"
		+ " <#if tgxy_BankAccountEscrowed??> and tgxy_BankAccountEscrowed=:tgxy_BankAccountEscrowed</#if>";
    }
	
	public String getSpecialHQL()
    {
		return "from Tgpf_DayEndBalancing where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if totalCount??> and totalCount>:totalCount</#if>"
		+ " <#if billTimeStamp??> and billTimeStamp=:billTimeStamp</#if>"
		//+ " order by totalAmount desc,totalCount desc,recordState desc,settlementTime desc";	
		+ " order by NLSSORT(bankName, 'NLS_SORT = SCHINESE_PINYIN_M')";
    }
	
	public String getQueryHQL()
    {
		return "from Tgpf_DayEndBalancing where 1=1"
		+ " <#if tgxy_BankAccountEscrowed??> and tgxy_BankAccountEscrowed=:tgxy_BankAccountEscrowed</#if>"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if recordState??> and recordState=:recordState</#if>"
		+ " <#if settlementState??> and settlementState=:settlementState</#if>"	
		+ " <#if billTimeStamp??> and billTimeStamp < :billTimeStamp</#if>"
		+ " and totalCount > 0 ";		
    }
	
	public String getBalancingHQL()
    {
		return "from Tgpf_DayEndBalancing where 1=1"
		+ " <#if tgxy_BankAccountEscrowed??> and tgxy_BankAccountEscrowed=:tgxy_BankAccountEscrowed</#if>"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if recordState??> and recordState=:recordState</#if>"
		+ " <#if settlementState??> and settlementState=:settlementState</#if>"	
		+ " <#if billTimeStamp??> and billTimeStamp = :billTimeStamp</#if>"
		+ " and totalCount > 0 ";		
    }

    public String getSumHQL()
    {
	    return "from Tgpf_DayEndBalancing where 1=1"
	    + " <#if theState??> and theState=:theState</#if>"
	    + " <#if settlementState??> and settlementState=:settlementState</#if>"
//	    + " <#if countPerPage??> and rownum<=:countPerPage</#if>"
	    + " order by billTimeStamp desc";
    }
	
}
