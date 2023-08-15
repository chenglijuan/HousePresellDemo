package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_ProInspectionSchedule_View;

/*
 * Dao数据库操作：项目巡查预测计划表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_ProInspectionSchedule_ViewDao extends BaseDao<Tg_ProInspectionSchedule_View>
{

	public String getBasicHQL()
    {
    	return "from Tg_ProInspectionSchedule_View where 1=1"
    			+ " <#if progressOfUpdateTime??> and progressOfUpdateTime=:progressOfUpdateTime</#if>"
    			+ " <#if cityRegion??> and cityRegion=:cityRegion</#if>"
    			+ " <#if projectName??> and projectName=:projectName</#if>"
    			;
    }
}
