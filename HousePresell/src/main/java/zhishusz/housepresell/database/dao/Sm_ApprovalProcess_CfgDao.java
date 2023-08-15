package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;

/*
 * Dao数据库操作：审批流-流程配置
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_ApprovalProcess_CfgDao extends BaseDao<Sm_ApprovalProcess_Cfg>
{
	public String getBasicHQL()
    {
    	return "from Sm_ApprovalProcess_Cfg where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if companySet??> and companySet=:companySet</#if>"
		+ " <#if theName??> and theName=:theName</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if isNeedBackup??> and isNeedBackup=:isNeedBackup</#if>"
		+ " <#if nodeList??> and nodeList=:nodeList</#if>"
		+ " <#if keyword??> and  CONCAT(theName , eCode, busiCode) like :keyword </#if>"
		+ " <#if busiCode??> and busiCode =:busiCode</#if>"
		+ " <#if orderBy??> order by ${orderBy}</#if>";
    }
}
