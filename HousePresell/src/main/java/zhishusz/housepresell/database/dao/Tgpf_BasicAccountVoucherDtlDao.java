package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_BasicAccountVoucherDtl;

/*
 * Dao数据库操作：基本户凭证-子表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_BasicAccountVoucherDtlDao extends BaseDao<Tgpf_BasicAccountVoucherDtl>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_BasicAccountVoucherDtl where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if billTimeStamp??> and billTimeStamp=:billTimeStamp</#if>"
		+ " <#if accountVoucher??> and accountVoucher=:accountVoucher</#if>"
		+ " ORDER BY billTimeStamp DESC , createTimeStamp DESC";
    }

}
