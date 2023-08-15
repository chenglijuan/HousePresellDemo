package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_JournalCount_View;

/*
 * Dao数据库操作：日记账统计表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_JournalCount_ViewListDao extends BaseDao<Tg_JournalCount_View>
{
	public String getBasicHQL()
    {
    	return "from Tg_JournalCount_View where 1=1"
    			+ " <#if keyword??> and (escrowAcountName like :keyword )</#if>"
    			+ " <#if loanInDate??> and loanInDate >=:loanInDate</#if>"
    	    	+ " <#if endLoanInDate??> and loanInDate <=:endLoanInDate</#if>"
    			+ " <#if escrowAcountName??> and escrowAcountName =:escrowAcountName</#if>";
    			/*+ "  order by loanInDate desc, escrowAcountName asc";*/
    }
}
