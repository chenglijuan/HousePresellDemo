package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_DepositProjectAnalysis_View;


/*
 * Dao数据库操作：托管项目情况分析表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_DepositProjectAnalysis_ViewDao extends BaseDao<Tg_DepositProjectAnalysis_View>
{

	public String getBasicHQL()
    {
    	return "from Tg_DepositProjectAnalysis_View where 1=1"
    			+ " <#if cityRegion??> and cityRegion=:cityRegion</#if>"
    			+ " <#if depositTime??> and depositTime=:depositTime</#if>"
    			+ " <#if queryYear??> and queryYear=:queryYear</#if>"
    			+ " <#if queryQuarter??> and queryQuarter=:queryQuarter</#if>"
    			+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (cityRegionInfo.tableId in :cityRegionInfoIdArr)</#if>"
    			+ " <#if queryMonth??> and queryMonth=:queryMonth</#if>"
    			;
    }
}
