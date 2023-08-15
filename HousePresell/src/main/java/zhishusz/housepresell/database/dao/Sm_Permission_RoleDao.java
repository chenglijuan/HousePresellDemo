package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.database.po.Sm_Permission_Role;

import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：管理角色
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_Permission_RoleDao extends BaseDao<Sm_Permission_Role>
{
	public String getBasicHQL()
    {
    	return "from Sm_Permission_Role where 1=1"
		+ " <#if tableId??> and tableId=:tableId</#if>"
		+ " <#if exceptTableId??> and tableId!=:exceptTableId</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if theName??> and theName=:theName</#if>"
		+ " <#if remark??> and remark=:remark</#if>"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiType?? && busiType != \"\"> and busiType=:busiType</#if>"
		+ " <#if uiPermissionJson??> and uiPermissionJson=:uiPermissionJson</#if>"
		+ " <#if enableTimeStampStart??> and enableTimeStamp>=:enableTimeStampStart</#if>"
		+ " <#if enableTimeStampEnd??> and enableTimeStamp<=:enableTimeStampEnd</#if>"
		+ " <#if keyword??> and  CONCAT(eCode,theName) like :keyword </#if>"
		+ " <#if companyType?? && companyType != \"\"> and companyType=:companyType</#if>"
		+ " <#if enableTimeStamp??> and enableTimeStamp>=:enableTimeStamp</#if>"
		+ " <#if downTimeStamp??> and downTimeStamp<=:downTimeStamp</#if>"
		+ " <#if orderBy?? && orderBy != \"\" && orderBy == 'theName'> order by NLSSORT(${orderBy},'NLS_SORT = SCHINESE_PINYIN_M') ${orderByType}</#if>"
		+ " <#if orderBy?? && orderBy != \"\" && orderBy != 'theName'> order by ${orderBy}</#if>";
    }
	
	//获取法务角色
	public String getLawWorks()
    {
    	return "from Sm_Permission_Role where 1=1"
		+ " <#if theName??> and theName like :theName</#if>"
		+ " <#if theState??> and theState=:theState</#if>"
		;
    }
	
	public String getExcelListHQL()
    {
    	return "from Sm_Permission_Role where 1=1"
    	+ " <#if idArr??> and tableId in :idArr</#if>";
    }
}
