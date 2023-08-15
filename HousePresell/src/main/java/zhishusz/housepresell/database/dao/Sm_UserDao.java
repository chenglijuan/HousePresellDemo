package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.database.po.Sm_User;

import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：系统用户+机构用户
 * Company：ZhiShuSZ
 * */
//@Repository
public class Sm_UserDao extends BaseDao<Sm_User>
{
	public String getBasicHQL()
    {
    	return "from Sm_User where 1=1"
		+ " <#if tableId??> and tableId=:tableId</#if>"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if eCodeOfCompany??> and eCodeOfCompany=:eCodeOfCompany</#if>"
		+ " <#if theNameOfCompany??> and theNameOfCompany=:theNameOfCompany</#if>"
		+ " <#if eCodeOfDepartment??> and eCodeOfDepartment=:eCodeOfDepartment</#if>"
		+ " <#if theNameOfDepartment??> and theNameOfDepartment=:theNameOfDepartment</#if>"
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
		+ " <#if loginPassword??> and loginPassword=:loginPassword</#if>"
		+ " <#if errPwdCount??> and errPwdCount=:errPwdCount</#if>"
		+ " <#if heardImagePath??> and heardImagePath=:heardImagePath</#if>"
		+ " <#if lastLoginTimeStamp??> and lastLoginTimeStamp=:lastLoginTimeStamp</#if>"
		+ " <#if loginMode??> and loginMode=:loginMode</#if>"
		+ " <#if ukeyNumber??> and ukeyNumber=:ukeyNumber</#if>"
		+ " <#if hasQC??> and hasQC=:hasQC</#if>"
		+ " <#if menuPermissionHtmlPath??> and menuPermissionHtmlPath=:menuPermissionHtmlPath</#if>"
		+ " <#if buttonPermissionJsonPath??> and buttonPermissionJsonPath=:buttonPermissionJsonPath</#if>"
		+ " <#if dataPermssion??> and dataPermssion=:dataPermssion</#if>"
		+ " <#if keyword??> and (theName like :keyword or theAccount like :keyword)</#if>"
		+ " <#if isEncrypt??> and isEncrypt=:isEncrypt</#if>"
		+ " <#if theAccount??> and theAccount=:theAccount</#if>"
		+ " <#if company??> and company =:company</#if>"
		+ " <#if companyId??> and company.tableId=:companyId</#if>"
		+ " <#if applyState?? && applyState == 1> and lockUntil>:lockUntil</#if>"
		+ " <#if applyState?? && applyState == 2> and lockUntil<:lockUntil</#if>"
		+ " <#if orderBy?? && orderBy != \"\" && orderBy == 'theName'> order by NLSSORT(${orderBy},'NLS_SORT = SCHINESE_PINYIN_M') ${orderByType}</#if>"
		+ " <#if orderBy?? && orderBy != \"\" && orderBy == 'theNameOfCompany'> order by company.theType ${orderByType}</#if>"
				;
    }
	
	public String getExcelListHQL()
    {
    	return "from Sm_User where 1=1"
    	+ " <#if idArr??> and tableId in :idArr</#if>";
    }
	
	
	public String getExceptType0HQL()
    {
    	return "from Sm_User where 1=1"
		+ " <#if tableId??> and tableId=:tableId</#if>"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if eCodeOfCompany??> and eCodeOfCompany=:eCodeOfCompany</#if>"
		+ " <#if theNameOfCompany??> and theNameOfCompany=:theNameOfCompany</#if>"
		+ " <#if eCodeOfDepartment??> and eCodeOfDepartment=:eCodeOfDepartment</#if>"
		+ " <#if theNameOfDepartment??> and theNameOfDepartment=:theNameOfDepartment</#if>"
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
		+ " <#if loginPassword??> and loginPassword=:loginPassword</#if>"
		+ " <#if errPwdCount??> and errPwdCount=:errPwdCount</#if>"
		+ " <#if heardImagePath??> and heardImagePath=:heardImagePath</#if>"
		+ " <#if lastLoginTimeStamp??> and lastLoginTimeStamp=:lastLoginTimeStamp</#if>"
		+ " <#if loginMode??> and loginMode=:loginMode</#if>"
		+ " <#if ukeyNumber??> and ukeyNumber=:ukeyNumber</#if>"
		+ " <#if hasQC??> and hasQC=:hasQC</#if>"
		+ " <#if menuPermissionHtmlPath??> and menuPermissionHtmlPath=:menuPermissionHtmlPath</#if>"
		+ " <#if buttonPermissionJsonPath??> and buttonPermissionJsonPath=:buttonPermissionJsonPath</#if>"
		+ " <#if dataPermssion??> and dataPermssion=:dataPermssion</#if>"
		+ " <#if keyword??> and  (theName like :keyword or theAccount like :keyword)</#if>"
		+ " <#if isEncrypt??> and isEncrypt=:isEncrypt</#if>"
		+ " <#if companyId??> and company.tableId=:companyId</#if>"
		+ "  and theType <> 1 "
		+ " <#if applyState?? && applyState == 1> and lockUntil>:lockUntil</#if>"
		+ " <#if applyState?? && applyState == 2> and lockUntil<:lockUntil</#if>";
    }
}
