package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_BasicAccountVoucher;

/*
 * Dao数据库操作：基本户凭证
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_BasicAccountVoucherDao extends BaseDao<Tgpf_BasicAccountVoucher>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_BasicAccountVoucher where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if billTimeStamp??> and billTimeStamp=:billTimeStamp</#if>"
		+ " <#if sendState??> and sendState=:sendState</#if>"
		+ " <#if beginDate??> and billTimeStamp>=:beginDate</#if>"
		+ " <#if endDate??> and billTimeStamp<=:endDate</#if>"
		+ " <#if keyWord??> and ( subCode like :keyWord) </#if>"
		+ " ORDER BY NVL(sendState,'0'), billTimeStamp DESC , createTimeStamp DESC";
    }

}
