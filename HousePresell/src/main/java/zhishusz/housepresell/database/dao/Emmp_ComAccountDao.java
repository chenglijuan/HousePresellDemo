package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Emmp_ComAccount;

/*
 * Dao数据库操作：机构-财务账号信息
 * Company：ZhiShuSZ
 * */
@Repository
public class Emmp_ComAccountDao extends BaseDao<Emmp_ComAccount>
{
	public String getBasicHQL()
    {
    	return "from Emmp_ComAccount where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if officeAddress??> and officeAddress=:officeAddress</#if>"
		+ " <#if officePhone??> and officePhone=:officePhone</#if>"
		+ " <#if theNameOfBank??> and theNameOfBank=:theNameOfBank</#if>"
		+ " <#if bankAccount??> and bankAccount=:bankAccount</#if>"
		+ " <#if theNameOfBankAccount??> and theNameOfBankAccount=:theNameOfBankAccount</#if>";
    }
}
