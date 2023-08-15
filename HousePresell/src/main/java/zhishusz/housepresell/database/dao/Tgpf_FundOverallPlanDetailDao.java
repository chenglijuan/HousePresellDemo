package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.database.po.Tgpf_FundOverallPlan;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlanDetail;
import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：资金统筹
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_FundOverallPlanDetailDao extends BaseDao<Tgpf_FundOverallPlanDetail>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_FundOverallPlanDetail where 1=1"
        +" <#if tableId??> and tableId=:tableId</#if>";
    }
}
