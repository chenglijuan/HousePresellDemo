package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;
import zhishusz.housepresell.database.po.Tg_DepositHouseholdsDtl_View;

/*
 * Dao数据库操作：托管户信息明细表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_DepositHouseholdsDtl_ViewDao extends BaseDao<Tg_DepositHouseholdsDtl_View>
{
	public String getBasicHQL()
	{
		return "from Tg_DepositHouseholdsDtl_View where 1=1" + " <#if position??> and position=:position</#if>"
				+ " <#if buyerName??> and buyerName=:buyerName</#if>"
				+ " <#if eCodeOfcertificate??> and eCodeOfcertificate=:eCodeOfcertificate</#if>"
				+ " <#if contactPhone??> and contactPhone=:contactPhone</#if>"
				+ " <#if payMethod??> and payWay=:payMethod</#if>"
				+ " <#if cityRegionId??> and projectInfo.cityRegion.tableId=:cityRegionId</#if>"
				+ " <#if projectId??> and projectInfo.tableId=:projectId</#if>"
				+ " <#if buildingId??> and buildingInfo.tableId=:buildingId</#if>"
				+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (projectInfo.cityRegion.tableId in :cityRegionInfoIdArr)</#if>"
				+ " <#if keyword??> and (NVL(buyerName,'') like :keyword or eCodeOfContractRecord like :keyword )</#if>";
				/*+ "   order by eCodeOfContractRecord";*/
	}
}
