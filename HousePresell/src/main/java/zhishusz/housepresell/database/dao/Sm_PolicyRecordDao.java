package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.database.po.Sm_PolicyRecord;

import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：政策公告
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_PolicyRecordDao extends BaseDao<Sm_PolicyRecord>
{
	public String getBasicHQL()
    {
    	return "from Sm_PolicyRecord where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if policyIsbrow??> and policyIsbrow=:policyIsbrow</#if>"
		+ " <#if policyIstop??> and policyIstop=:policyIstop</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if policyDate??> and policyDate=:policyDate</#if>"
		+ " <#if policyType??> and policyType=:policyType</#if>"
		+ " <#if policyTypeCode??> and policyTypeCode=:policyTypeCode</#if>"
		+ " <#if policyTitle??> and policyTitle=:policyTitle</#if>"
		+ " <#if policyContent??> and policyContent=:policyContent</#if>"
		+ " <#if keyword??> and (policyType like :keyword or policyTitle like :keyword) </#if>"
		+ " <#if policyState??> and policyState=:policyState</#if>"
		+ " order by policyDate desc"
		;
    }

}
