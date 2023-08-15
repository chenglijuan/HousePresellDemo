package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Sm_BaseParameter;

/*
 * Dao数据库操作：参数定义
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_BaseParameterDao extends BaseDao<Sm_BaseParameter>
{
	public String getBasicHQL()
    {
    	return "from Sm_BaseParameter where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if theName??> and theName=:theName</#if>"
		+ " <#if theValue??> and theValue=:theValue</#if>"
		+ " <#if validDateFrom??> and validDateFrom=:validDateFrom</#if>"
		+ " <#if validDateTo??> and validDateTo=:validDateTo</#if>"
		+ " <#if theVersion??> and theVersion=:theVersion</#if>"
		+ " <#if exceptZhengTai??> and theValue <> 0</#if>"
		+ " <#if parametertype??> and parametertype=:parametertype</#if>";
    }
	
	//根据theName获取thevalue
	public String getTheValueHQL(){	
		return "FROM Sm_BaseParameter WHERE 1=1"
				+ "<#if theName??> and theName=:theName</#if>"
				+ "<#if keyword??> and theName like :keyword</#if>"
				+ "<#if parametertype??> and parametertype=:parametertype</#if>";
	}
}
