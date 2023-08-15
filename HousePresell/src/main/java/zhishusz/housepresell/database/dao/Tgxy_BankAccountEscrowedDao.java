package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_TheState;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/*
 * Dao数据库操作：托管账户
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgxy_BankAccountEscrowedDao extends BaseDao<Tgxy_BankAccountEscrowed> {
    public String getBasicHQL() {
        return "from Tgxy_BankAccountEscrowed where 1=1"
                + " <#if theState??> and theState=:theState</#if>"
                + " <#if busiState??> and busiState=:busiState</#if>"
                + " <#if eCode??> and eCode=:eCode</#if>"
                + " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
                + " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
                + " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
                + " <#if theNameOfBank??> and theNameOfBank=:theNameOfBank</#if>"
                + " <#if shortNameOfBank??> and shortNameOfBank=:shortNameOfBank</#if>"
                + " <#if theName??> and theName=:theName</#if>"
                + " <#if theAccount??> and theAccount=:theAccount</#if>"
                + " <#if remark??> and remark=:remark</#if>"
                + " <#if contactPerson??> and contactPerson=:contactPerson</#if>"
                + " <#if contactPhone??> and contactPhone=:contactPhone</#if>"
                + " <#if updatedStamp??> and updatedStamp=:updatedStamp</#if>"
                + " <#if income??> and income=:income</#if>"
                + " <#if payout??> and payout=:payout</#if>"
                + " <#if certOfDeposit??> and certOfDeposit=:certOfDeposit</#if>"
                + " <#if structuredDeposit??> and structuredDeposit=:structuredDeposit</#if>"
                + " <#if breakEvenFinancial??> and breakEvenFinancial=:breakEvenFinancial</#if>"
                + " <#if currentBalance??> and currentBalance=:currentBalance</#if>"
                + " <#if largeRatio??> and largeRatio=:largeRatio</#if>"
                + " <#if largeAndCurrentRatio??> and largeAndCurrentRatio=:largeAndCurrentRatio</#if>"
                + " <#if financialRatio??> and financialRatio=:financialRatio</#if>"
                + " <#if keyword??> and theName like :keyword</#if>"
                + " <#if totalFundsRatio??> and totalFundsRatio=:totalFundsRatio</#if>"
                + " <#if bank??> and bank=:bank</#if>"
                + " <#if bankBranchId??> and bankBranch.tableId=:bankBranchId</#if>"
                + " <#if closingTime??> and closingTime=:closingTime</#if>"
                + " <#if hasClosing??> and hasClosing=:hasClosing</#if>"
                + " <#if isUsing??> and isUsing=:isUsing</#if>"
                + " <#if closingPerson??> and closingPerson=:closingPerson</#if>"
                + " <#if transferOutAmount??> and isUsing=:transferOutAmount</#if>"
                + " <#if transferInAmount??> and isUsing=:transferInAmount</#if>";  	
    }

    public String getExcelListHQL() {
        return "from Tgxy_BankAccountEscrowed where 1=1"
                + " <#if idArr??> and tableId in :idArr</#if>";
    }
    
    
    public String loadingListExecute() {
        return "from Tgxy_BankAccountEscrowed where 1=1"
                + " <#if theState??> and theState=:theState</#if>"
                + " <#if busiState??> and busiState=:busiState</#if>"   
                + " <#if hasClosing??> and hasClosing=:hasClosing</#if>"
                + " <#if isUsing??> and isUsing=:isUsing</#if>"
                + " <#if tableId??> and tableId!=:tableId</#if>"
                + " <#if keyword?? && keyword !=''> and (theName like :keyword or theAccount like :keyword)</#if>";  	
    }

    //===================== 用 Hibernate 对象查询语句 解决：HQL多表关联查询，使用 fetch 抓取，有时候返回List<Object>有时候返回List<T>导致Rebuild重构数据异常 Start =====//
    public Criteria createCriteriaForList(Tgxy_BankAccountEscrowedForm model) {
        //托管账号、开户行
        String keyword = getKeyWord(model);
        Criteria criteria = createCriteria();
        criteria.createAlias("bankBranch", "bankBranch");
        criteria.createAlias("bankBranch.bank", "bank");
        criteria.add(Restrictions.and(Restrictions.in("theState", S_TheState.Cache,S_TheState.Normal)));
        if (model.getIsUsing() != null) {
            criteria.add(Restrictions.and(Restrictions.eq("isUsing", model.getIsUsing())));
        }
        Emmp_BankInfo bank = model.getBank();
        if (bank != null) {
            criteria.add(Restrictions.eq("bank", bank));
        }
        if (model.getBankBranchId() != null) {
            criteria.add(Restrictions.and(Restrictions.eq("bankBranch.tableId", model.getBankBranchId())));
        }
        ArrayList<String> needPinYinOrderList = new ArrayList<>();
        needPinYinOrderList.add("bankBranch.theName");
        needPinYinOrderList.add("bankBranch.shortName");
        addCriteriaListOrder(criteria,model,needPinYinOrderList);
        if (!keyword.equals("%%")) {
            criteria.add(Restrictions.or(
                    Restrictions.like("bankBranch.shortName", keyword),
//                    Restrictions.like("theName", keyword),
                    Restrictions.like("theAccount", keyword)
            ));
        }
        return criteria;
    }


}
