package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.database.po.Sm_MessageTemplate_Cfg;
import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：审批流-消息模板配置
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_MessageTemplate_CfgDao extends BaseDao<Sm_MessageTemplate_Cfg>
{
	public String getBasicHQL()
    {
    	return "from Sm_MessageTemplate_Cfg where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if theName??> and theName=:theName</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if theDescribe??> and theDescribe=:theDescribe</#if>"
		+ " <#if theTitle??> and theTitle=:theTitle</#if>"
		+ " <#if theContent??> and theContent=:theContent</#if>"
		+ " <#if keyword??> and CONCAT(eCode,theName,busiCode) like :keyword</#if>"
		+ " <#if busiCode??> and busiCode=:busiCode</#if>"
		+ " <#if orderBy??> order by ${orderBy}</#if>";
    }
}
