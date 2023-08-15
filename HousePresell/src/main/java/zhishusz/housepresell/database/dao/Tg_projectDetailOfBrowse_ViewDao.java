package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_projectDetailOfBrowse_View;

/*
 * Dao数据库操作：托管项目详情一览表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_projectDetailOfBrowse_ViewDao extends BaseDao<Tg_projectDetailOfBrowse_View>
{
	public String getBasicHQL()
    {
    	return "from Tg_projectDetailOfBrowse_View where 1=1"
    			+ "<#if keyword ??> and (projectName like :keyword or  eCodeFromConstruction like :keyword or forEcastArea like :keyword or cityRegion like :keyword ) </#if> "
				+ " <#if cityRegion??> and cityRegion=:cityRegion</#if>"
				+ " <#if projectName??> and projectName=:projectName</#if>"
				+ " <#if queryYear??> and queryYear=:queryYear</#if>"
				+ " <#if queryMonth??> and queryMonth=:queryMonth</#if>"
				+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (projectInfo.cityRegion.tableId in :cityRegionInfoIdArr)</#if>"
    			+" order by cityRegion desc, projectName desc";
    }
}
