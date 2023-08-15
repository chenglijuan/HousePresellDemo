package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Sm_Permission_RoleData;

/*
 * Dao数据库操作：角色与数据权限对应关系
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_Permission_RoleDataDao extends BaseDao<Sm_Permission_RoleData>
{
	public String getBasicHQL()
    {
    	return "from Sm_Permission_RoleData where 1=1"
		+ " <#if theState??> and theState=:theState</#if>";
    }
}
