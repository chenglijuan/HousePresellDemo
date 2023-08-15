package zhishusz.housepresell.database.dao.extra;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.dao.BaseDao;
import zhishusz.housepresell.database.po.extra.Qs_BuildingAccount_View;

/*
 * Dao数据库操作：入账金额核对表
 * Company：ZhiShuSZ
 * 
 * keyword：关键字(开户行或账号)
 * recordDateStart:起始日期
 * recordDateEnd：终止日期
 * theNameOfBank：银行名称
 * theNameOfEscrow：托管账户名称
 * 
 * 默认入账日期降序排序
 * 
 */
@Repository
public class Qs_RecordAmount_ViewDao extends BaseDao<Qs_BuildingAccount_View>
{
	public String getBasicHQL()
    {
    	return "from Qs_RecordAmount_View where 1=1"
    			+ " <#if recordDateStart??> and recordDate >= :recordDateStart </#if>"
    			+ " <#if recordDateEnd??> and recordDate <= :recordDateEnd </#if>"
    			+ " <#if theNameOfBank??> and theNameOfBank = :theNameOfBank </#if>"
    			+ " <#if theNameOfEscrow??> and theNameOfEscrow like :theNameOfEscrow </#if>"
    			+ " <#if keyword??> and ( theNameOfBank like :keyword or theAccountOfEscrow like :keyword)</#if>"
    			+ " order by recordDate desc";
    }
}
