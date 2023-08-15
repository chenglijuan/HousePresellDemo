package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_PjRiskLetterReceiver;

/*
 * Dao数据库操作：项目风险函收件人
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_PjRiskLetterReceiverDao extends BaseDao<Tg_PjRiskLetterReceiver>
{
	public String getBasicHQL()
    {
    	return "from Tg_PjRiskLetterReceiver where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if tg_PjRiskLetter??> and tg_PjRiskLetter=:tg_PjRiskLetter</#if>"
		+ " <#if emmp_OrgMember??> and emmp_OrgMember=:emmp_OrgMember</#if>"
		+ " <#if sendWay??> and sendWay=:sendWay</#if>"
		+ " <#if emmp_CompanyInfo??> and emmp_CompanyInfo=:emmp_CompanyInfo</#if>";
    }
}
