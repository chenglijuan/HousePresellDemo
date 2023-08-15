package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_BasicAccount;

/*
 * Dao数据库操作：非基本户凭证
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_BasicAccountDao extends BaseDao<Tgpf_BasicAccount>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_BasicAccount where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if accountName??> and accountName=:accountName</#if>"
		+ " <#if month??> and billTimeStamp like :month</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if billTimeStamp??> and billTimeStamp=:billTimeStamp</#if>"
		+ " <#if voucherType??> and voucherType=:voucherType</#if>"
		+ " <#if keyWord??> and ( accountName like :keyWord or subCode like :keyWord ) </#if>"
		+ " ORDER BY NVL(sendState,'0') , billTimeStamp DESC , createTimeStamp DESC";
    }

}
