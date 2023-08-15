package zhishusz.housepresell.database.dao;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.controller.form.Tgpf_AccVoucherForm;
import zhishusz.housepresell.database.po.Tgpf_AccVoucher;

/*
 * Dao数据库操作：推送给财务系统-凭证
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_AccVoucherDao extends BaseDao<Tgpf_AccVoucher>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_AccVoucher where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if billTimeStamp??> and billTimeStamp=:billTimeStamp</#if>"
		+ " <#if theType??> and theType=:theType</#if>"
		+ " <#if tradeCount??> and tradeCount=:tradeCount</#if>"
		+ " <#if totalTradeAmount??> and totalTradeAmount=:totalTradeAmount</#if>"
		+ " <#if contentJson??> and contentJson=:contentJson</#if>"
		+ " <#if payoutTimeStamp??> and payoutTimeStamp=:payoutTimeStamp</#if>"
		+ " <#if theNameOfCompany??> and theNameOfCompany=:theNameOfCompany</#if>"
		+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
		+ " <#if theNameOfBank??> and theNameOfBank=:theNameOfBank</#if>"
		+ " <#if DayEndBalancingState??> and DayEndBalancingState=:DayEndBalancingState</#if>"
		+ " <#if theAccountOfBankAccountEscrowed??> and theAccountOfBankAccountEscrowed=:theAccountOfBankAccountEscrowed</#if>"
		+ " <#if payoutAmount??> and payoutAmount=:payoutAmount</#if>";
    }
	
	public String getSpeicalHQL()
    {
    	return "from Tgpf_AccVoucher where 1=1"
    	+ " <#if theState??> and theState=:theState</#if>"
    	+ " <#if theType??> and theType=:theType</#if>"
    	+ " <#if contentJson??> and contentJson=:contentJson</#if>"
    	+ " <#if busiState??> and busiState=:busiState</#if>"
    	+ " <#if tradeCount??> and tradeCount>:tradeCount</#if>"
    	+ " <#if tgpf_FundAppropriated_AF??> and tgpf_FundAppropriated_AF=:tgpf_FundAppropriated_AF</#if>"
    	+ " <#if keyword??> and (theNameOfBank like :keyword or eCode like :keyword or bankAccountEscrowed.theAccount like :keyword ) </#if>"		
    	+ " <#if billTimeStamp??> and billTimeStamp=:billTimeStamp</#if>"
    	+ " <#if relatedType??> and relatedType=:relatedType</#if>"
    	+ " <#if relatedTableId??> and relatedTableId=:relatedTableId</#if>"
    	+ " order by busiState,totalTradeAmount desc,tradeCount desc,payoutAmount desc";
    	
    }
	
	public String getPayHQL()
    {
    	return "from Tgpf_AccVoucher where 1=1"
    	+ " <#if theState??> and theState=:theState</#if>"
    	+ " <#if theType??> and theType=:theType</#if>"
    	+ " <#if busiState??> and busiState=:busiState</#if>"
    	+ " <#if keyword??> and (theNameOfBank like :keyword or eCode like :keyword) </#if>"		
    	+ " <#if payoutTimeStamp??> and payoutTimeStamp=:payoutTimeStamp</#if>"
    	+ " order by busiState,payoutAmount desc";
    	
    }
	
	public Criteria createNewCriteriaForList(Tgpf_AccVoucherForm model)
	{
		Criteria criteria = createCriteria();

		Integer theState = model.getTheState();
		if (theState != null) {
			criteria.add(Restrictions.eq("theState", theState));
		}
		String busiState = model.getBusiState();
		if (busiState != null) {
			criteria.add(Restrictions.eq("busiState", busiState));
		}
		String payoutTimeStamp = model.getPayoutTimeStamp();
		if (payoutTimeStamp != null) {
			criteria.add(Restrictions.eq("payoutTimeStamp", payoutTimeStamp));
		}
		String theType = model.getTheType();
		if (theType != null) {
			criteria.add(Restrictions.eq("theType", theType));
		}
		String keyword = getKeyWord(model);
		if (keyword != null && keyword.length() != 0)
		{
			criteria.add(Restrictions.like("theNameOfBank", keyword));
		}
		
		criteria.addOrder(Order.desc("busiState").desc("payoutAmount"));

		return criteria;
	}
}
