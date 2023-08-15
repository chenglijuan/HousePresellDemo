package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Sm_ProjProgParameter;

/*
 * Dao数据库操作：参数定义
 * Company：ZhiShuSZ
 * 测算参数配置
 * */
@Repository
public class Sm_ProjProgParameterDao extends BaseDao<Sm_ProjProgParameter>
{
	public String getBasicHQL()
    {
    	return "from Sm_ProjProgParameter where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if project??> and project=:project</#if>"
		+ " <#if projectCode??> and projectCode=:projectCode</#if>"
		+ " <#if projectName??> and projectName=:projectName</#if>"
		+ " <#if area??> and area=:area</#if>"
		+ " <#if areaName??> and areaName=:areaName</#if>"
		+ " or hasAll= 1"
		+ " ORDER BY hasAll , areaName , projectName";
    }
	
}
