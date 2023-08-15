package zhishusz.housepresell.database.dao.extra;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.dao.BaseDao;
import zhishusz.housepresell.database.po.extra.Qs_TripleAgreement_View;

/*
 * Dao数据库操作：三方协议视图
 * Company：ZhiShuSZ
 * 
 * 
 */
@Repository
public class Qs_TripleAgreement_ViewDao extends BaseDao<Qs_TripleAgreement_View>
{
	public String getBasicHQL()
    {
    	return "from Qs_TripleAgreement_View where 1=1"
    			+ " <#if bankAccountEscrowedId??> and bankAccountEscrowedId=:bankAccountEscrowedId</#if>"
    			+ " <#if projectId??> and projectId=:projectId</#if>";
    }
}
