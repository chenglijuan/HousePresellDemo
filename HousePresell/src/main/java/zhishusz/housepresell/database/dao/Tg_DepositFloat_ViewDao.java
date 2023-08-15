package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_DepositFloat_View;

/*
 * Dao数据库操作：托协定存款统计表
 * Company：ZhiShuSZ
 */
@Repository
public class Tg_DepositFloat_ViewDao extends BaseDao<Tg_DepositFloat_View>
{
	public String getBasicHQL()
	{
		return "from Tg_DepositFloat_View where 1=1" + " <#if keyword??> and (remark like :keyword)</#if>"
				+ " <#if bankName??> and bankName=:bankName</#if>"
				+ " <#if signDateStart??> and signDate >=:signDateStart</#if>"
				+ " <#if signDateEnd??> and signDate <=:signDateEnd</#if>"
				+ " <#if endExpirationDateStart??> and endExpirationDate >=:endExpirationDateStart</#if>"
				+ " <#if endExpirationDateEnd??> and endExpirationDate <=:endExpirationDateEnd</#if>"
				+ " order by bankName desc, signDate asc, endExpirationDate desc";
	}
}
