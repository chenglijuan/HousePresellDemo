package zhishusz.housepresell.database.dao.extra;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.dao.BaseDao;
import zhishusz.housepresell.database.po.extra.Qs_EscrowBankFunds_View;

/*
 * Dao数据库操作：托管银行资金情况表
 * Company：ZhiShuSZ
 * 
 * timeStamp：日期
 * theNameOfDepositBank：开户行
 * keyword：关键字
 * */
@Repository
public class Qs_EscrowBankFunds_ViewDao extends BaseDao<Qs_EscrowBankFunds_View>
{
	public String getBasicHQL()
    {
    	return "from Qs_EscrowBankFunds_View where 1=1"
		+ " <#if theNameOfDepositBank??> and theNameOfDepositBank=:theNameOfDepositBank</#if>"
		+ " <#if timeStamp??> and timeStamp=:timeStamp</#if>"
		+ " <#if keyword??> and theNameOfDepositBank like :keyword</#if>";
    }
}
