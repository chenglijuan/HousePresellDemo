package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Sm_Permission_RoleCompanyType;

/*
 * Dao数据库操作：角色与机构类型对应关系
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_Permission_RoleCompanyTypeDao extends BaseDao<Sm_Permission_RoleCompanyType>
{
	public String getBasicHQL()
    {
    	return "from Sm_Permission_RoleCompanyType where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if forCompanyType??> and forCompanyType=:forCompanyType</#if>";
    }
}
