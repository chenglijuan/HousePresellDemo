package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_SocketMsg;

/*
 * Dao数据库操作：接口报文表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_SocketMsgDao extends BaseDao<Tgpf_SocketMsg>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_SocketMsg where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if msgLength??> and msgLength=:msgLength</#if>"
		+ " <#if locationCode??> and locationCode=:locationCode</#if>"
		+ " <#if msgBusinessCode??> and msgBusinessCode=:msgBusinessCode</#if>"
		+ " <#if bankCode??> and bankCode=:bankCode</#if>"
		+ " <#if returnCode??> and returnCode=:returnCode</#if>"
		+ " <#if remark??> and remark=:remark</#if>"
		+ " <#if msgDirection??> and msgDirection=:msgDirection</#if>"
		+ " <#if msgStatus??> and msgStatus=:msgStatus</#if>"
		+ " <#if md5Check??> and md5Check=:md5Check</#if>"
		+ " <#if msgTimeStamp??> and msgTimeStamp=:msgTimeStamp</#if>"
		+ " <#if eCodeOfTripleAgreement??> and eCodeOfTripleAgreement=:eCodeOfTripleAgreement</#if>"
		+ " <#if eCodeOfPermitRecord??> and eCodeOfPermitRecord=:eCodeOfPermitRecord</#if>"
		+ " <#if eCodeOfContractRecord??> and eCodeOfContractRecord=:eCodeOfContractRecord</#if>"
		+ " <#if msgSerialno??> and msgSerialno=:msgSerialno</#if>"
		+ " <#if bankPlatformSerialNo??> and bankPlatformSerialNo=:bankPlatformSerialNo</#if>"
		+ " <#if msgContent??> and msgContent=:msgContent</#if>";
    }
}
