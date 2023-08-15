package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.database.po.Tgpf_FundOverallPlanDetail;
import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：统筹计算
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_FundOverallPlanCalculateDao extends BaseDao<Tgpf_FundOverallPlanDetail>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_FundOverallPlanCalculate where 1=1"
        +" <#if tableId??> and tableId=:tableId</#if>";
    }
}
