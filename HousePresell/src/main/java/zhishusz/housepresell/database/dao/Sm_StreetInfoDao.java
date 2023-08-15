package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Sm_StreetInfo;

/*
 * Dao数据库操作：Sm_StreetInfo
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_StreetInfoDao extends BaseDao<Sm_StreetInfo>
{
	public String getBasicHQL()
    {
    	return "from Sm_StreetInfo where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theName??> and theName=:theName</#if>"
		+ " <#if cityRegion??> and cityRegion=:cityRegion</#if>";
    }
}
