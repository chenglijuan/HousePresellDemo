package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.database.po.Tgpf_FundOverallPlanRecord;
import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：资金统筹
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_FundOverallPlanRecordDao extends BaseDao<Tgpf_FundOverallPlanRecord>
{
	public String getBasicHQL()
    {
    	return "from  Tgpf_FundOverallPlanRecord where 1=1"
		+ " <#if theState??> and theState=:theState</#if>";
    }
}
