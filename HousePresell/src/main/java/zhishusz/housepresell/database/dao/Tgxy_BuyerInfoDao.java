package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgxy_BuyerInfo;

/*
 * Dao数据库操作：买受人信息
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgxy_BuyerInfoDao extends BaseDao<Tgxy_BuyerInfo>
{
	public String getBasicHQL()
    {
    	return "from Tgxy_BuyerInfo where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if buyerName??> and buyerName=:buyerName</#if>"
		+ " <#if buyerType??> and buyerType=:buyerType</#if>"
		+ " <#if certificateType??> and certificateType=:certificateType</#if>"
		+ " <#if eCodeOfcertificate??> and eCodeOfcertificate=:eCodeOfcertificate</#if>"
		+ " <#if contactPhone??> and contactPhone=:contactPhone</#if>"
		+ " <#if contactAdress??> and contactAdress=:contactAdress</#if>"
		+ " <#if agentName??> and agentName=:agentName</#if>"
		+ " <#if agentCertType??> and agentCertType=:agentCertType</#if>"
		+ " <#if agentCertNumber??> and agentCertNumber=:agentCertNumber</#if>"
		+ " <#if agentPhone??> and agentPhone=:agentPhone</#if>"
		+ " <#if agentAddress??> and agentAddress=:agentAddress</#if>"
		+ " <#if eCodeOfContract??> and eCodeOfContract=:eCodeOfContract</#if>";
    }
	
	public String getHouseHQL()
    {
    	return "from Tgxy_BuyerInfo where 1=1 and theState=0"
		+ " <#if houseInfoId??> and houseInfo.tableId=:houseInfoId</#if>";
    }
}
