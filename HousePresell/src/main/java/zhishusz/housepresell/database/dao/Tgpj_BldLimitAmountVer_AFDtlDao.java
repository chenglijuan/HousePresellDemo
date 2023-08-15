package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFDtlForm;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：受限额度设置
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpj_BldLimitAmountVer_AFDtlDao extends BaseDao<Tgpj_BldLimitAmountVer_AFDtl>
{
	public String getBasicHQL()
    {
    	return "from Tgpj_BldLimitAmountVer_AFDtl where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if stageName??> and stageName=:stageName</#if>"
		+ " <#if bldLimitAmountVerMngId??> and bldLimitAmountVerMng.tableId=:bldLimitAmountVerMngId</#if>"
		+ " <#if limitedAmount??> and limitedAmount<:limitedAmount</#if>"
		+ " <#if orderBy??> order by ${orderBy}</#if>";
    }
	
	public Criteria createGetDtlList(Tgpj_BldLimitAmountVer_AFDtlForm model)
	{
		Criteria criteria = createCriteria();

		Integer theState = model.getTheState();
		if (theState != null) {
			criteria.add(Restrictions.eq("theState", theState));
		}
		Long bldLimitAmountVerMngId = model.getBldLimitAmountVerMngId();
		if (bldLimitAmountVerMngId != null) {
			criteria.add(Restrictions.eq("bldLimitAmountVerMng.tableId", bldLimitAmountVerMngId));
		}

		criteria.addOrder(Order.asc("limitedAmount"));

		return criteria;
	}
	
	public String getBasicDescHQL()
    {
        return "from Tgpj_BldLimitAmountVer_AFDtl where 1=1"
        + " <#if theState??> and theState=:theState</#if>"
        + " <#if bldLimitAmountVerMngId??> and bldLimitAmountVerMng.tableId=:bldLimitAmountVerMngId</#if>"
        + " <#if orderBy??> order by limitedAmount desc</#if>";
    }
}
