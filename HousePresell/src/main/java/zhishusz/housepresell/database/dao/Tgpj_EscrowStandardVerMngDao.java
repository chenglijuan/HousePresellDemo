package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;

/*
 * Dao数据库操作：版本管理-托管标准
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpj_EscrowStandardVerMngDao extends BaseDao<Tgpj_EscrowStandardVerMng>
{
	//默认排序：托管标准协议版本号↓、启用日期↑
	public String getBasicHQL()
    {
    	return "from Tgpj_EscrowStandardVerMng where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if approvalState??> and approvalState=:approvalState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theName??> and theName=:theName</#if>"
		+ " <#if theVersion??> and theVersion=:theVersion</#if>"
//		+ " <#if hasEnable??> and hasEnable=:hasEnable</#if>" //已弃用
		+ " <#if theType??> and theType=:theType</#if>"
		+ " <#if theContent??> and theContent=:theContent</#if>"
		+ " <#if amount??> and amount=:amount</#if>"
		+ " <#if percentage??> and percentage=:percentage</#if>"
		+ " <#if extendParameter1??> and extendParameter1=:extendParameter1</#if>"
		+ " <#if extendParameter2??> and extendParameter2=:extendParameter2</#if>"
		+ " <#if expirationDate??> and beginExpirationDate<=:expirationDate</#if>"
		+ " <#if expirationDate??> and endExpirationDate>=:expirationDate</#if>"
    	+ " <#if keyword??> and CONCAT(theName, theVersion) like :keyword</#if>"
		+ " <#if orderBy??> order by ${orderBy}</#if>"
    	+ " <#if !orderBy??> ORDER BY THENAME ASC, BEGINEXPIRATIONDATE DESC</#if>";
    }

	public String getEexistenceHQL()
	{
		return "from Tgpj_EscrowStandardVerMng where 1=1"
				+ " <#if tableId??> and tableId!=:tableId</#if>"
				+ " <#if theState??> and theState=:theState</#if>"
				+ " <#if theName??> and theName=:theName</#if>"
				+ " <#if beginExpirationDate??> and ((beginExpirationDate<=:beginExpirationDate and" +
				" endExpirationDate>=:beginExpirationDate)</#if>"
				+ " <#if endExpirationDate??> or (beginExpirationDate<=:endExpirationDate and" +
				" endExpirationDate>=:endExpirationDate)</#if>"
				+ " <#if beginExpirationDate?? && endExpirationDate??> or (beginExpirationDate>=:beginExpirationDate" +
				" and endExpirationDate<=:endExpirationDate))</#if>";
	}

	public String getExcelListHQL()
	{
		return "from Tgpj_EscrowStandardVerMng where 1=1"
				+ " <#if idArr??> and tableId in :idArr</#if>";
	}
}
