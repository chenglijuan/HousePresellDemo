package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.controller.form.Tgpj_BankAccountSupervisedForm;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.state.S_TheState;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/*
 * Dao数据库操作：监管账户
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpj_BankAccountSupervisedDao extends BaseDao<Tgpj_BankAccountSupervised> {
    //	public String getSizeHQL(Boolean flag)
//	{
//		if(flag)
//		{
//			String condition = getBasicHQL(flag);
//			return "from Rel_Building_Bankaccountsup relBuildBankAccount"
//			+ " inner join relBuildBankAccount.empj_buildinginfo empj_buildinginfo where 1=1"
//			+ " inner join relBuildBankAccount.tgpj_bankaccountsupervised tgpj_bankaccountsupervised where 1=1"
//			+ condition;
//		}
//		else
//		{
//			return getBasicHQL(flag);
//		}
//
//	}
//	public String getListHQL(Boolean flag)
//    {
//		if(flag)
//		{
//			String condition = getBasicHQL(flag);
//			return "from Rel_Building_Bankaccountsup relBuildBankAccount"
//			+ " inner join fetch relBuildBankAccount.empj_buildinginfo empj_buildinginfo where 1=1"
//			+ " inner join relBuildBankAccount.tgpj_bankaccountsupervised tgpj_bankaccountsupervised where 1=1"
//			+ condition;
//		}
//		else
//		{
//			return getBasicHQL(flag);
//		}
//    }
//	public String getBasicHQL(Boolean flag)
    public String getBasicHQL() {
//		if(flag)
//		{
//			return	" <#if theState??> and theState=:theState</#if>"
//					+ " <#if busiState??> and busiState=:busiState</#if>"
//					+ " <#if eCode??> and eCode=:eCode</#if>"
//					+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
//					+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
//					+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
//					+ " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
//					+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
//					+ " <#if theNameOfBank??> and theNameOfBank=:theNameOfBank</#if>"
//					+ " <#if shortNameOfBank??> and shortNameOfBank=:shortNameOfBank</#if>"
////					+ " <#if theName??> and theName=:theName</#if>"
//					+ " <#if theAccount??> and theAccount=:theAccount</#if>"
//					+ " <#if remark??> and remark=:remark</#if>"
//					+ " <#if contactPerson??> and contactPerson=:contactPerson</#if>"
//					+ " <#if keyword??> and eCode like :keyword</#if>"
//					+ " <#if contactPhone??> and contactPhone=:contactPhone</#if>"
//					+ " <#if buildingId??> and empj_buildinginfo.tableId=:buildingId</#if>";
//		}
//		else
//		{
        return "from Tgpj_BankAccountSupervised where 1=1"
                + " <#if theState??> and theState=:theState</#if>"
                + " <#if busiState??> and busiState=:busiState</#if>"
                + " <#if eCode??> and eCode=:eCode</#if>"
                + " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
                + " <#if developCompany??> and developCompany=:developCompany</#if>"
                + " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
                + " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
                + " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
                + " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
                + " <#if theNameOfBank??> and theNameOfBank=:theNameOfBank</#if>"
                + " <#if shortNameOfBank??> and shortNameOfBank=:shortNameOfBank</#if>"
//					+ " <#if theName??> and theName=:theName</#if>"
                + " <#if theAccount??> and theAccount=:theAccount</#if>"
                + " <#if remark??> and remark=:remark</#if>"
                + " <#if contactPerson??> and contactPerson=:contactPerson</#if>"
                + " <#if keyword??> and theName like :keyword</#if>"
                + " <#if contactPhone??> and contactPhone=:contactPhone</#if>";
//		}
    }

    public String getExcelListHQL() {
        return "from Tgpj_BankAccountSupervised where 1=1"
                + " <#if idArr??> and tableId in :idArr</#if>";
    }

    //===================== 用 Hibernate 对象查询语句 解决：HQL多表关联查询，使用 fetch 抓取，有时候返回List<Object>有时候返回List<T>导致Rebuild重构数据异常 Start =====//
    public Criteria createCriteriaForList(Tgpj_BankAccountSupervisedForm model) {
        //开发企业和开户行
        String keyword = getKeyWord(model);
        Criteria criteria = createCriteria();
        criteria.createAlias("developCompany", "company");
        //criteria.createAlias("bankBranch", "bankBranch");
        criteria.add(Restrictions.and(Restrictions.eq("theState", S_TheState.Normal)));
        if (model.getIsUsing() != null) {
            criteria.add(Restrictions.and(Restrictions.eq("isUsing", model.getIsUsing())));
        }
        if (model.getDevelopCompanyId() != null) {
            criteria.add(Restrictions.and(Restrictions.eq("company.tableId", model.getDevelopCompanyId())));
        }
        ArrayList<String> needPinYinOrderList = new ArrayList<>();
        needPinYinOrderList.add("company.theName");
        /*needPinYinOrderList.add("bankBranch.shortName");*/
        addCriteriaListOrder(criteria, model, needPinYinOrderList);
        addCompanyLimitRange(criteria, model);
//        Emmp_CompanyInfo userCompany = model.getUser().getCompany();
//        if (userCompany != null) {
//            criteria.add(Restrictions.and(Restrictions.eq("company.tableId", userCompany.getTableId())));
//        }
        criteria.add(Restrictions.or(
                Restrictions.like("theNameOfBank", keyword)
                /*Restrictions.like("bankBranch.theName", keyword)*/
        ));
        return criteria;
    }
}
