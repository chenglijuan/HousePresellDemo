package zhishusz.housepresell.database.dao.extra;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.dao.BaseDao;
import zhishusz.housepresell.database.po.extra.Qs_BigAmountCompare_View;

/*
 * Dao数据库操作：大额到期对比表
 * Company：ZhiShuSZ
 * 
 * keyword：关键字
 * theNameOfDepositBank：开户行名称
 * 
 * depositDateStart：存入日期起始时间
 * depositDateEnd：存入日期结束时间
 * dueDateStart：到期日期起始时间
 * dueDateEnd：到期日期结束时间
 * 
 * */
@Repository
public class Qs_BigAmountCompare_ViewDao extends BaseDao<Qs_BigAmountCompare_View>
{
	public String getBasicHQL()
    {
    	return "from Qs_BigAmountCompare_View where 1=1"
		+ " <#if theNameOfDepositBank??> and theNameOfDepositBank=:theNameOfDepositBank</#if>"
		+ " <#if depositDateStart??> and depositDate >= :depositDateStart</#if>"
		+ " <#if depositDateEnd??> and depositDate < :depositDateEnd</#if>"
		+ " <#if dueDateStart??> and dueDate >= :dueDateStart</#if>"
		+ " <#if dueDateEnd??> and dueDate < :dueDateEnd</#if>"
		+ " <#if keyword??> and theNameOfDepositBank like :keyword</#if>";
    }
}
