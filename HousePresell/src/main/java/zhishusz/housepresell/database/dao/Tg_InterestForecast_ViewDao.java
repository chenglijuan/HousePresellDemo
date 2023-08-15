package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_InterestForecast_View;

/*
 * Dao数据库操作：托管楼幢明细
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_InterestForecast_ViewDao extends BaseDao<Tg_InterestForecast_View>
{

	public String getBasicHQL()
    {
    	return "from Tg_InterestForecast_View where 1=1"
    			+ " <#if keyword??> and  (bankName like :keyword)</#if>"
    			+ " <#if startDate??> and  startDate =:startDate</#if>"
    			+ " <#if stopDate??> and  stopDate =:stopDate</#if>"
    	;
    }
}
