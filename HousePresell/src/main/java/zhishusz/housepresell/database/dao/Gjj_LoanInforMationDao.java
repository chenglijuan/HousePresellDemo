package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.util.gjj.GJJ_LoanInforMation;
@Repository
public class Gjj_LoanInforMationDao extends BaseDao<GJJ_LoanInforMation> {

    public String getBasicHQL() {
        return "from Empj_BuildingInfo where 1=1"
                + " <#if loanApplicationEcide??> and loanApplicationEcide=:loanApplicationEcide</#if>"
                + " <#if buyerName??> and buyerName=:buyerName</#if>"
                + " <#if buyerCardType??> and buyerCardType=:buyerCardType</#if>"
                + " <#if buyerCardNum??> and buyerCardNum=:buyerCardNum</#if>"
                + " <#if buyerAddress??> and buyerAddress=:buyerAddress</#if>"
                + " <#if ecodeofcontractrecord??> and ecodeofcontractrecord=:ecodeofcontractrecord</#if>"
                + " <#if theComposeStete??> and theComposeStete=:theComposeStete</#if>"
                + " <#if loanBank??> and loanBank=:loanBank</#if>"
                + " <#if loanMoney??> and loanMoney=:loanMoney</#if>"
                + " <#if theState??> and theState=:theState</#if>";

    }

}
