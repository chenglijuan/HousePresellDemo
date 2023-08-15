package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_SerialNumber;

/*
 * Dao数据库操作：流水号
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_SerialNumberDao extends BaseDao<Tgpf_SerialNumber>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_SerialNumber where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if businessType??> and businessType=:businessType</#if>"
		+ " <#if serialNumber??> and serialNumber=:serialNumber</#if>"
		+ " <#if serialDate??> and serialDate=:serialDate</#if>";
    }
}
