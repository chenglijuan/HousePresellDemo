package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Sm_CityRegionInfo;

/*
 * Dao数据库操作：基础数据-城市区域
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_CityRegionInfoDao extends BaseDao<Sm_CityRegionInfo>
{
	public String getBasicHQL()
    {
    	return "from Sm_CityRegionInfo where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and tableId in :cityRegionInfoIdArr</#if>"
		+ " <#if theName??> and theName=:theName</#if>";
    }
}
