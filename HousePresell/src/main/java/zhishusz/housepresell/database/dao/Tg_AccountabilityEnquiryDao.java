package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Tg_AccountabilityEnquiry;

/*
 * Dao : 按权责发生制查询利息情况统计表
 * */
@Repository
public class Tg_AccountabilityEnquiryDao extends BaseDao<Tg_AccountabilityEnquiry>
{
	public String getBasicHQL(){
		
		return "FROM Tg_AccountabilityEnquiry WHERE 1=1"
				+"<#if loadTimeStart??> and loadTime >=:loadTimeStart</#if>"		
				+"<#if expirationTimeEnd??> and expirationTime <=:expirationTimeEnd</#if>"
				+"<#if bank??> and bank=:bank</#if>" 
				+"<#if keyword??> and bank like :keyword </#if>";
	}
}
