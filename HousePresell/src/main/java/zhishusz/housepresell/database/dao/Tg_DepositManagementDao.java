package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_DepositManagement;

/*
 * Dao数据库操作：存单管理
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_DepositManagementDao extends BaseDao<Tg_DepositManagement>
{
	public String getBasicHQL()
    {
    	return "from Tg_DepositManagement where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if depositState??> and depositState=:depositState</#if>"
		+ " <#if exceptDepositState??> and depositState!=:exceptDepositState</#if>"
		
		//风控抽查条件
		+ " <#if approvalState??> and approvalState=:approvalState</#if>"
		+ " <#if checkStartTimeStamp??> and createTimeStamp>=:checkStartTimeStamp</#if>"
		+ " <#if checkEndTimeStamp??> and createTimeStamp<:checkEndTimeStamp</#if>"
		
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if depositProperty??> and depositProperty=:depositProperty</#if>"
		+ " <#if bankOfDepositId??> and bankOfDeposit.tableId=:bankOfDepositId</#if>"
		+ " <#if escrowAcount??> and escrowAcount=:escrowAcount</#if>"
		+ " <#if escrowAcountShortName??> and escrowAcountShortName=:escrowAcountShortName</#if>"
		+ " <#if agent??> and agent=:agent</#if>"
		+ " <#if principalAmount??> and principalAmount=:principalAmount</#if>"
		+ " <#if storagePeriod??> and storagePeriod=:storagePeriod</#if>"
		+ " <#if storagePeriodCompany??> and storagePeriodCompany=:storagePeriodCompany</#if>"
		+ " <#if annualRate??> and annualRate=:annualRate</#if>"
		+ " <#if startDate??> and startDate =:startDate</#if>"
		+ " <#if stopDate??> and stopDate >=:stopDate</#if>"
		+ " <#if listDateStrEndLon??> and stopDate <=:listDateStrEndLon</#if>"
		
		+ " <#if openAccountCertificate??> and openAccountCertificate=:openAccountCertificate</#if>"
		+ " <#if expectedInterest??> and expectedInterest=:expectedInterest</#if>"
		+ " <#if floatAnnualRate??> and floatAnnualRate=:floatAnnualRate</#if>"
		+ " <#if extractDate??> and extractDate=:extractDate</#if>"
		+ " <#if realInterest??> and realInterest=:realInterest</#if>"
		+ " <#if realInterestRate??> and realInterestRate=:realInterestRate</#if>"
	    + " <#if keyword??> and (eCode like :keyword or escrowAcount.theAccount like :keyword)</#if>"
	    + " <#if orderBy??> order by ${orderBy} ${orderByType}</#if>"
	    ;
    }


	public String getBasicHQLInProgress()
	{
		return "from Tg_DepositManagement where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if depositState??> and depositState=:depositState</#if>"
		+ " <#if exceptDepositState??> and depositState!=:exceptDepositState</#if>"

		//风控抽查条件
		+ " <#if approvalState??> and approvalState=:approvalState</#if>"
		+ " <#if checkStartTimeStamp??> and createTimeStamp>=:checkStartTimeStamp</#if>"
		+ " <#if checkEndTimeStamp??> and createTimeStamp<:checkEndTimeStamp</#if>"

		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if depositProperty??> and depositProperty=:depositProperty</#if>"
		+ " <#if bankOfDepositId??> and bankOfDeposit.tableId=:bankOfDepositId</#if>"
		+ " <#if escrowAcount??> and escrowAcount=:escrowAcount</#if>"
		+ " <#if escrowAcountShortName??> and escrowAcountShortName=:escrowAcountShortName</#if>"
		+ " <#if agent??> and agent=:agent</#if>"
		+ " <#if principalAmount??> and principalAmount=:principalAmount</#if>"
		+ " <#if storagePeriod??> and storagePeriod=:storagePeriod</#if>"
		+ " <#if storagePeriodCompany??> and storagePeriodCompany=:storagePeriodCompany</#if>"
		+ " <#if annualRate??> and annualRate=:annualRate</#if>"
		+ " <#if startDate??> and startDate>=:startDate</#if>"
		+ " <#if stopDate??> and startDate<=:stopDate</#if>"
		+ " <#if openAccountCertificate??> and openAccountCertificate=:openAccountCertificate</#if>"
		+ " <#if expectedInterest??> and expectedInterest=:expectedInterest</#if>"
		+ " <#if floatAnnualRate??> and floatAnnualRate=:floatAnnualRate</#if>"
		+ " <#if extractDate??> and extractDate=:extractDate</#if>"
		+ " <#if realInterest??> and realInterest=:realInterest</#if>"
		+ " <#if realInterestRate??> and realInterestRate=:realInterestRate</#if>"
		+ " <#if keyword??> and (eCode like :keyword or escrowAcount.theAccount like :keyword or bankOfDeposit.theName like :keyword)</#if>"
		+ " <#if orderBy??> order by ${orderBy} ${orderByType}</#if>"
		;
	}

	public String getExcelListHQL()
	{
		return "from Tg_DepositManagement where 1=1"
				+ " <#if idArr??> and tableId in :idArr</#if>";
	}

	public String getBasicHQL2()
	{
		return "from Tg_DepositManagement where 1=1"
				+ " <#if theState??> and theState=:theState</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>"
				+ " <#if approvalState??> and approvalState=:approvalState</#if>"
				+ " <#if escrowAcountId??> and escrowAcount.tableId =:escrowAcountId</#if>"
				+ " <#if depositState??> and depositState =:depositState</#if>";
	}

	public String getSumHQL()
	{
		return "from Tg_DepositManagement where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if approvalState??> and approvalState=:approvalState</#if>"
		+ " <#if depositState??> and depositState=:depositState</#if>"
		+ " <#if depositProperty??> and depositProperty=:depositProperty</#if>"
		+ " <#if stopDate??> and stopDate=:stopDate</#if>"
		+ " <#if startDate??> and startDate=:startDate</#if>";
	}

	public String getSumHQL2()
	{
		return "from Tg_DepositManagement where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if approvalState??> and approvalState=:approvalState</#if>"
		+ " <#if depositState??> and depositState=:depositState</#if>"
		+ " <#if depositProperty??> and depositProperty=:depositProperty</#if>"
		+ " <#if stopDate??> and stopDate<=:stopDate</#if>";
	}
	
	public String getSumHQL3()
	{
		return "from Tg_DepositManagement where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if approvalState??> and approvalState=:approvalState</#if>"
		+ " <#if depositState??> and depositState=:depositState</#if>"
		+ " <#if depositProperty??> and depositProperty=:depositProperty</#if>"
		+ " <#if startDate??> and stopDate>=:startDate</#if>"
		+ " <#if stopDate??> and stopDate<=:stopDate</#if>";
	}
}
