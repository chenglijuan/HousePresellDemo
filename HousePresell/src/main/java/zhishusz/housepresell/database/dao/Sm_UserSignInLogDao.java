package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;
import zhishusz.housepresell.database.po.Sm_UserSignInLog;

/*
 * Dao数据库操作：系统用户+机构用户
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_UserSignInLogDao extends BaseDao<Sm_UserSignInLog>
{
	public String getBasicHQL()
    {
    	return "from Sm_UserSignInLog where 1=1"
		+ " <#if tableId??> and tableId=:tableId</#if>";
    }

}
