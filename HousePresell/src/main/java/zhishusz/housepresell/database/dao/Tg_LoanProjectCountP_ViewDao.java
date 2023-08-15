package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_LoanProjectCountP_View;

/*
 * Dao数据库操作：托管项目统计表（项目部）
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_LoanProjectCountP_ViewDao extends BaseDao<Tg_LoanProjectCountP_View>
{

	public String getBasicHQL()
    {
    	return "from Tg_LoanProjectCountP_View where 1=1"
    			+ "<#if keyword ??> and (eCodeFromConstruction like :keyword or companyName like :keyword or projectName like :keyword or companyGroup like :companyGroup) </#if> "
				+ " <#if cityRegion??> and cityRegion=:cityRegion</#if>"
				+ " <#if companyName??> and companyName=:companyName</#if>"
				+ " <#if projectName??> and projectName=:projectName</#if>"
				+ " <#if companyGroup??> and companyGroup=:companyGroup</#if>"
				+ " <#if recordDateEnd??> and escrowAgRecordTime <= :recordDateEnd</#if>"
				+ " <#if recordDateStart??> and escrowAgRecordTime >= :recordDateStart</#if>"
				+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (projectInfo.cityRegion.tableId in :cityRegionInfoIdArr)</#if>"
				+ " order by companyGroup desc, companyName desc, cityRegion desc, projectName desc"
    	;
    }
}
