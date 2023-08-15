package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.database.po.Sm_UserState;

import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：用户登录记录
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_UserStateDao extends BaseDao<Sm_UserState>
{
	public String getBasicHQL()
    {
    	return "from Sm_UserState where 1=1"
		+ " <#if tableId??> and tableId=:tableId</#if>"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if loginUser??> and loginUser=:loginUser</#if>"
		+ " <#if loginSessionId??> and loginSessionId=:loginSessionId</#if>"
		+ " <#if loginUserName??> and loginUserName=:loginUserName</#if>"
		+ " <#if loginDate??> and loginDate=:loginDate</#if>";
    }
	
}
