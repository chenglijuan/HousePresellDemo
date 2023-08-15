package zhishusz.housepresell.database.dao.extra;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.dao.BaseDao;
import zhishusz.housepresell.database.po.extra.Qs_BuildingAccount_View;

/*
 * Dao数据库操作：楼幢账户表
 * Company：ZhiShuSZ
 * 
 * keyword：关键字
 */
@Repository
public class Qs_BuildingAccount_ViewDao extends BaseDao<Qs_BuildingAccount_View>
{
	public String getBasicHQL()
    {
    	return "from Qs_BuildingAccount_View where 1=1"
    			+ " <#if theNameOfCompany??> and theNameOfCompany=:theNameOfCompany</#if>"
    			+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
    			+ " <#if eCodeFromConstruction??> and eCodeFromConstruction=:eCodeFromConstruction</#if>"
    			+ " <#if keyword??> and ( theNameOfCompany like :keyword or theNameOfProject like :keyword or eCodeFromConstruction like :keyword )</#if>"
    			+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (projectInfo.cityRegion.tableId in :cityRegionInfoIdArr)</#if>"
    			+ " order by theNameOfProject , to_number(regexp_substr(eCodeFromConstruction,'[0-9]*[0-9]',1)) , tableId";
    }
}
