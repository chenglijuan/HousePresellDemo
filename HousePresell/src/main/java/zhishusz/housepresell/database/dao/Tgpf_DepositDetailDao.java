package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_DepositDetail;

/*
 * Dao数据库操作：资金归集-明细表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_DepositDetailDao extends BaseDao<Tgpf_DepositDetail>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_DepositDetail where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if eCodeFromPayment??> and eCodeFromPayment=:eCodeFromPayment</#if>"
		+ " <#if fundProperty??> and fundProperty=:fundProperty</#if>"
		+ " <#if theNameOfBankAccountEscrowed??> and theNameOfBankAccountEscrowed=:theNameOfBankAccountEscrowed</#if>"
		+ " <#if theAccountOfBankAccountEscrowed??> and theAccountOfBankAccountEscrowed=:theAccountOfBankAccountEscrowed</#if>"
		+ " <#if theNameOfCreditor??> and theNameOfCreditor=:theNameOfCreditor</#if>"
		+ " <#if idType??> and idType=:idType</#if>"
		+ " <#if idNumber??> and idNumber=:idNumber</#if>"
		+ " <#if bankAccountForLoan??> and bankAccountForLoan=:bankAccountForLoan</#if>"
		+ " <#if loanAmountFromBank??> and loanAmountFromBank=:loanAmountFromBank</#if>"
		+ " <#if billTimeStamp??> and billTimeStamp=:billTimeStamp</#if>"
		+ " <#if eCodeFromBankCore??> and eCodeFromBankCore=:eCodeFromBankCore</#if>"
		+ " <#if eCodeFromBankPlatform??> and eCodeFromBankPlatform=:eCodeFromBankPlatform</#if>"
		+ " <#if remarkFromDepositBill??> and remarkFromDepositBill=:remarkFromDepositBill</#if>"
		+ " <#if eCodeFromBankWorker??> and eCodeFromBankWorker=:eCodeFromBankWorker</#if>"
		+ " <#if depositState??> and depositState=:depositState</#if>"
		+ " <#if bankBranch??> and bankBranch=:bankBranch</#if>"
		+ " <#if depositDatetime??> and depositDatetime=:depositDatetime</#if>"
		+ " <#if reconciliationTimeStampFromBusiness??> and reconciliationTimeStampFromBusiness=:reconciliationTimeStampFromBusiness</#if>"
		+ " <#if reconciliationStateFromBusiness??> and reconciliationStateFromBusiness=:reconciliationStateFromBusiness</#if>"
		+ " <#if reconciliationTimeStampFromCyberBank??> and reconciliationTimeStampFromCyberBank=:reconciliationTimeStampFromCyberBank</#if>"
		+ " <#if reconciliationStateFromCyberBank??> and reconciliationStateFromCyberBank=:reconciliationStateFromCyberBank</#if>"
		+ " <#if hasVoucher??> and hasVoucher=:hasVoucher</#if>"
		+ " <#if timestampFromReverse??> and timestampFromReverse=:timestampFromReverse</#if>"
		+ " <#if theStateFromReverse??> and theStateFromReverse=:theStateFromReverse</#if>"
		+ " <#if eCodeFromReverse??> and eCodeFromReverse=:eCodeFromReverse</#if>"
		+ " <#if tripleAgreement??> and tripleAgreement=:tripleAgreement</#if>"
		+ " <#if project??> and tripleAgreement.project=:project</#if>"
	    + " <#if startTimeStamp??> and billTimeStamp >=:startTimeStamp</#if>"
	    + " <#if endTimeStamp??> and billTimeStamp <:endTimeStamp</#if>"
		+ " <#if bankAccountEscrowedId??> and bankAccountEscrowed.tableId=:bankAccountEscrowedId</#if>"
		+ " <#if judgeStatement??> and eCodeFromPayment in (1,2,3) </#if>";
    }
	
	public String getBasicHQL2()
    {
    	return "from Tgpf_DepositDetail where 1=1 and theStateFromReverse = 0"
    	+ " <#if keyword??> and (theNameOfCreditor like :keyword or tripleAgreement.eCodeOfTripleAgreement like :keyword or tripleAgreement.eCodeOfContractRecord like :keyword )</#if>"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if bankBranchId??> and bankBranch.tableId =:bankBranchId</#if>"
		+ " <#if companyName??> and tripleAgreement.sellerName =:companyName</#if>"
		+ " <#if companyNames?? && (companyNames?size>0)> and tripleAgreement.sellerName in :companyNames</#if>"
		+ " <#if projectId??> and tripleAgreement.project.tableId =:projectId</#if>"
		+ " <#if billTimeStamp??> and depositDatetime >= :billTimeStamp</#if>"
    	+ " <#if endBillTimeStamp??> and depositDatetime <=:endBillTimeStamp</#if>"
    	+ " order by depositDatetime desc, tripleAgreement.sellerName asc, tripleAgreement.theNameOfProject asc, tripleAgreement.eCodeFromConstruction desc"
		;
    }
	
	/**
	 * xsz by time 2019-1-10 17:10:26
	 * 查询开发企业下的所有信息
	 * 
	 * 调用service : Sm_HomePageProjectAccountService
	 * @return
	 */
	public String getBasicHQL3()
    {
    	return "from Tgpf_DepositDetail where 1=1 and theStateFromReverse = 0"
    	+ " <#if theState??> and theState=:theState</#if>"
    	+ " <#if projectId??> and tripleAgreement.project.tableId =:projectId</#if>"
    	+ " <#if companyInfo??> and tripleAgreement.project.developCompany = :companyInfo</#if>";
    }
	
	public String getLoanAmountFromBank()
    {
    	return "from Tgpf_DepositDetail where 1=1 and theStateFromReverse = 0"
		+ " <#if billTimeStamp??> and (billTimeStamp like :billTimeStamp)</#if>"
		+ " <#if cityRegionId??> and tripleAgreement.project.cityRegion.tableId =:cityRegionId</#if>"
		;
    }
}
