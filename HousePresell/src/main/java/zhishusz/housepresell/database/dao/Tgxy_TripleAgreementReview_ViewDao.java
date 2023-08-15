package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_JournalCount_View;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreementReview_View;

/*
 * Dao数据库操作：日记账统计表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgxy_TripleAgreementReview_ViewDao extends BaseDao<Tgxy_TripleAgreementReview_View>
{
	public String getBasicHQL()
    {
    	return "from Tgxy_TripleAgreementReview_View where 1=1"
    	+ " <#if proxyCompany??> and nvl(proxyCompany,'')=:proxyCompany</#if>"
    	+ " <#if rejectReason??> and nvl(rejectReason,'')=:rejectReason</#if>"
    	+ " <#if keyword??> and (nvl(rejectReason,' ') like :keyword or nvl(proxyCompany,' ') like :keyword )</#if>"
    	+ " <#if beginTime??> and nvl(rejectTimeStamp,'') >= :beginTime</#if>"
    	+ " <#if endTime??> and nvl(rejectTimeStamp,'') <= :endTime</#if>";
    }
}
