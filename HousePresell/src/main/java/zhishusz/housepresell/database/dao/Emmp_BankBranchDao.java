package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.database.po.Emmp_BankBranch;

import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：银行网点(开户行)
 * Company：ZhiShuSZ
 * */
@Repository
public class Emmp_BankBranchDao extends BaseDao<Emmp_BankBranch>
{
	public String getBasicHQL()
    {
    	return "from Emmp_BankBranch where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if isUsing??> and isUsing=:isUsing</#if>"
//		+ " <#if theName??> and theName=:theName</#if>"
		+ " <#if shortName??> and shortName=:shortName</#if>"
		+ " <#if address??> and address=:address</#if>"
		+ " <#if contactPerson??> and contactPerson=:contactPerson</#if>"
		+ " <#if contactPhone??> and contactPhone=:contactPhone</#if>"
		+ " <#if keyword??> and (theName like :keyword or shortName like :keyword or bank.theName like :keyword)</#if>"
		+ " <#if leader??> and leader=:leader</#if>"
		+ " <#if bankId??> and bank.tableId=:bankId</#if>"
//		+ " <#if orderBy??> order by ${orderBy}</#if>"
		+ " <#if orderBy??> order by NLSSORT(${orderBy},'NLS_SORT = SCHINESE_PINYIN_M') ${orderByType}</#if>"
		;
    }

	public String getExcelListHQL()
	{
		return "from Emmp_BankBranch where 1=1"
				+ " <#if idArr??> and tableId in :idArr</#if>";
	}
}
