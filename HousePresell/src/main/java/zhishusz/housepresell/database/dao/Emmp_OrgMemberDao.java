package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.database.po.Emmp_OrgMember;

import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：机构成员
 * Company：ZhiShuSZ
 * */
@Repository
public class Emmp_OrgMemberDao extends BaseDao<Emmp_OrgMember>
{
	public String getBasicHQL()
    {
    	return "from Emmp_OrgMember where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if eCodeOfDepartment??> and eCodeOfDepartment=:eCodeOfDepartment</#if>"
		+ " <#if theNameOfDepartment??> and theNameOfDepartment=:theNameOfDepartment</#if>"
		+ " <#if companyId??> and company.tableId=:companyId</#if>"
		+ " <#if bankId??> and bank.tableId=:bankId</#if>"
		+ " <#if theType??> and theType=:theType</#if>"
		+ " <#if theName??> and theName=:theName</#if>"
		+ " <#if realName??> and realName=:realName</#if>"
		+ " <#if idType??> and idType=:idType</#if>"
		+ " <#if idNumber??> and idNumber=:idNumber</#if>"
		+ " <#if phoneNumber??> and phoneNumber=:phoneNumber</#if>"
		+ " <#if email??> and email=:email</#if>"
		+ " <#if weixin??> and weixin=:weixin</#if>"
		+ " <#if qq??> and qq=:qq</#if>"
		+ " <#if address??> and address=:address</#if>"
		+ " <#if heardImagePath??> and heardImagePath=:heardImagePath</#if>"
		+ " <#if hasQC??> and hasQC=:hasQC</#if>"
		+ " <#if remark??> and remark=:remark</#if>"
		+ " <#if logId??> and logId=:logId</#if>";
    }
}
