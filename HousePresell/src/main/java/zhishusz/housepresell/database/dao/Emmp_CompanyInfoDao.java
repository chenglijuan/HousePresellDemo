package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_TheState;

/*
 * Dao鏁版嵁搴撴搷浣滐細鏈烘瀯淇℃伅
 * Company锛歓hiShuSZ
 * */
@Repository
public class Emmp_CompanyInfoDao extends BaseDao<Emmp_CompanyInfo>
{
	public String getBasicHQL()
    {
    	return "from Emmp_CompanyInfo where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if approvalState??> and approvalState=:approvalState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if recordState??> and recordState=:recordState</#if>"
		+ " <#if recordRejectReason??> and recordRejectReason=:recordRejectReason</#if>"
		+ " <#if theType?? && (theType? length gt 0)> and theType=:theType</#if>"
		+ " <#if exceptZhengTai?? && exceptZhengTai == true> and theType !="+S_CompanyType.Zhengtai+"</#if>"
		+ " <#if companyGroup??> and companyGroup=:companyGroup</#if>"
		+ " <#if theName??> and theName=:theName</#if>"
		+ " <#if shortName??> and shortName=:shortName</#if>"
		+ " <#if eCodeFromPresellSystem??> and eCodeFromPresellSystem=:eCodeFromPresellSystem</#if>"
		+ " <#if establishmentDate??> and establishmentDate=:establishmentDate</#if>"
		+ " <#if qualificationGrade??> and qualificationGrade=:qualificationGrade</#if>"
		+ " <#if unifiedSocialCreditCode??> and unifiedSocialCreditCode=:unifiedSocialCreditCode</#if>"
		+ " <#if registeredFund??> and registeredFund=:registeredFund</#if>"
		+ " <#if businessScope??> and businessScope=:businessScope</#if>"
		+ " <#if registeredDate??> and registeredDate=:registeredDate</#if>"
		+ " <#if expiredDate??> and expiredDate=:expiredDate</#if>"
		+ " <#if contactPerson??> and contactPerson=:contactPerson</#if>"
		+ " <#if contactPhone??> and contactPhone=:contactPhone</#if>"
		+ " <#if projectLeader??> and projectLeader=:projectLeader</#if>"
		+ " <#if qualificationInformationList??> and qualificationInformationList=:qualificationInformationList</#if>"
		+ " <#if theURL??> and theURL=:theURL</#if>"
		+ " <#if address??> and address=:address</#if>"
		+ " <#if email??> and email=:email</#if>"
		+ " <#if theFax??> and theFax=:theFax</#if>"
		+ " <#if eCodeOfPost??> and eCodeOfPost=:eCodeOfPost</#if>"
		+ " <#if introduction??> and introduction=:introduction</#if>"
		+ " <#if remark??> and remark=:remark</#if>"
		+ " <#if tableId??> and tableId=:tableId</#if>"
		+ " <#if logId??> and logId=:logId</#if>"
		+ " <#if isUsedState??> and nvl(isUsedState,'0')=:isUsedState</#if>"
		+ " <#if cityRegion??> and cityRegion=:cityRegion</#if>"
//		+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and cityRegion.tableId in :cityRegionInfoIdArr or userStart.tableId=:userId</#if>"
		+ " <#if orgMember??> and theType= 1 </#if>"
	    + " <#if keyword??> and (theName like :keyword or companyGroup like :keyword) </#if>"
//	    + " <#if orderBy??> order by ${orderBy}</#if>"
	    + " <#if orderBy??> order by NLSSORT(${orderBy},'NLS_SORT = SCHINESE_PINYIN_M') ${orderByType}</#if>"
	    ;
    }
	
	//寮�鍙戜紒涓氫笅鎷夐�夋煡璇�
	public String getBasicHQLForDevelop()
    {
    	return "from Emmp_CompanyInfo where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if approvalState??> and approvalState=:approvalState</#if>"
		+ " <#if theType??> and theType=:theType</#if>"
//		+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (cityRegion.tableId in :cityRegionInfoIdArr or userStart.tableId=:userId)</#if>"
		+ " <#if developCompanyInfoIdArr?? && (developCompanyInfoIdArr?size>0)> and (tableId in :developCompanyInfoIdArr or userStart.tableId=:userId)</#if>"
		;
    }
	
	public String getExcelListHQL()
    {
    	return "from Emmp_CompanyInfo where 1=1"
				+ " <#if theType??> and theType=:theType</#if>"
    			+ " <#if idArr??> and tableId in :idArr</#if>"
				+ " order by theName asc";
    }
	
	public String getBasicHQL2()
    {
    	return "from Emmp_CompanyInfo where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if theName??> and theName like :theName</#if>"
		+ " order by theName asc";
    }
	
	public String getBasicHQL3()
    {
    	return "from Emmp_CompanyInfo where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " order by theName asc";
    }

    // 妫�鏌ュ敮涓�鎬QL
	public String checkUniquenessHQL()
	{
		return "from Emmp_CompanyInfo where 1=1"
				+ " <#if exceptTableId??> and tableId<>:exceptTableId</#if>"
				+ " <#if theState??> and theState=:theState</#if>"
				+ " <#if theName??> and theName=:theName</#if>"
				+ " <#if unifiedSocialCreditCode??> and unifiedSocialCreditCode=:unifiedSocialCreditCode</#if>";
	}
	
	public String getSpecialCompany()
	{
		return "from Emmp_CompanyInfo where 1=1"
				+ " <#if theState??> and theState=:theState</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>"
				+ " <#if approvalState??> and approvalState=:approvalState</#if>"
				+ " <#if eCode??> and eCode=:eCode</#if>"
				+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
				+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
				+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
				+ " <#if recordState??> and recordState=:recordState</#if>"
				+ " <#if recordRejectReason??> and recordRejectReason=:recordRejectReason</#if>"
				+ " <#if theType?? && (theType? length gt 0)> and theType=:theType</#if>"
				+ " <#if exceptZhengTai??> and theType !="+S_CompanyType.Zhengtai+"</#if>"
				+ " <#if onlyZhengTai??> and theType ="+S_CompanyType.Zhengtai+"</#if>"
				+ " <#if companyGroup??> and companyGroup=:companyGroup</#if>"
				+ " <#if theName??> and theName=:theName</#if>"
				+ " <#if shortName??> and shortName=:shortName</#if>"
				+ " <#if eCodeFromPresellSystem??> and eCodeFromPresellSystem=:eCodeFromPresellSystem</#if>"
				+ " <#if establishmentDate??> and establishmentDate=:establishmentDate</#if>"
				+ " <#if qualificationGrade??> and qualificationGrade=:qualificationGrade</#if>"
				+ " <#if unifiedSocialCreditCode??> and unifiedSocialCreditCode=:unifiedSocialCreditCode</#if>"
				+ " <#if registeredFund??> and registeredFund=:registeredFund</#if>"
				+ " <#if businessScope??> and businessScope=:businessScope</#if>"
				+ " <#if registeredDate??> and registeredDate=:registeredDate</#if>"
				+ " <#if expiredDate??> and expiredDate=:expiredDate</#if>"
				+ " <#if contactPerson??> and contactPerson=:contactPerson</#if>"
				+ " <#if contactPhone??> and contactPhone=:contactPhone</#if>"
				+ " <#if projectLeader??> and projectLeader=:projectLeader</#if>"
				+ " <#if qualificationInformationList??> and qualificationInformationList=:qualificationInformationList</#if>"
				+ " <#if theURL??> and theURL=:theURL</#if>"
				+ " <#if address??> and address=:address</#if>"
				+ " <#if email??> and email=:email</#if>"
				+ " <#if theFax??> and theFax=:theFax</#if>"
				+ " <#if eCodeOfPost??> and eCodeOfPost=:eCodeOfPost</#if>"
				+ " <#if introduction??> and introduction=:introduction</#if>"
				+ " <#if remark??> and remark=:remark</#if>"
				+ " <#if logId??> and logId=:logId</#if>"
				+ " <#if cityRegion??> and cityRegion=:cityRegion</#if>"
				+ " <#if keyword??> and theName like :keyword</#if>"
				+ " <#if orgMember??> and theType<>0 and theType <> null </#if>"
				+ " order by createTimeStamp desc";
	}
	
	/*
	 * xsz by time 2018-11-23 13:52:05
	 * 鐢ㄤ簬寮�鍙戜紒涓氬鍏ユ椂鍒ゆ柇鍞竴鎬�
	 */
	public String getIsOneHQL()
    {
    	return "from Emmp_CompanyInfo where 1=1"
    			+ " <#if theState??> and theState=:theState</#if>"
    			+ " <#if externalId??> and externalId = :externalId</#if>";
    }
	
	/**
	 * 鏍规嵁鍖哄煙鏌ヨ寮�鍙戜紒涓�
	 * @return
	 */
	public String getListByIdArrHQLForDevelopCompany()
	{
		return "from Emmp_CompanyInfo where 1=1"
				+ " and theState = "+S_TheState.Normal
				+ " and theType = "+S_CompanyType.Development;
//				+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and cityRegion.tableId in :cityRegionInfoIdArr</#if>";
	}
	
	public String getIdByHql()
    {
    	return "from Emmp_CompanyInfo where 1=1"
    			+ " <#if tableId??> and tableId=:tableId</#if>";
    }
	
	/**
	 * 加载监理机构
	 * 进度见证和监理公司
	 * @return
	 */
	public String getBldLimitAmountCompanyListHQL()
    {
        return "from Emmp_CompanyInfo where 1=1"
        + "  and theState= 0 "
        + "  and busiState='已备案'"
        + "  and approvalState='已完结'"
        + "  and ( theType= '12'  or theType = '31' )"   
        + " <#if cityRegion??> and cityRegion=:cityRegion</#if>"
        + " <#if keyword??> and (theName like :keyword or companyGroup like :keyword) </#if>"
        + " <#if orderBy??> order by NLSSORT(${orderBy},'NLS_SORT = SCHINESE_PINYIN_M') ${orderByType}</#if>"
        ;
    }
}
