package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Emmp_QualificationInfo;

/*
 * Dao数据库操作：资质认证信息
 * Company：ZhiShuSZ
 * */
@Repository
public class Emmp_QualificationInfoDao extends BaseDao<Emmp_QualificationInfo>
{
	public String getBasicHQL()
    {
    	return "from Emmp_QualificationInfo where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theType??> and theType=:theType</#if>"
		+ " <#if theLevel??> and theLevel=:theLevel</#if>"
		+ " <#if issuanceDate??> and issuanceDate=:issuanceDate</#if>"
		+ " <#if expiryDate??> and expiryDate=:expiryDate</#if>";
    }
}
