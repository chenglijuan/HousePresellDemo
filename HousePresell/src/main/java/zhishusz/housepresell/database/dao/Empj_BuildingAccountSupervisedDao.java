package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.database.po.Empj_BuildingAccountSupervised;

import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：楼幢与楼幢监管账号关联表
 * Company：ZhiShuSZ
 * */
@Repository
public class Empj_BuildingAccountSupervisedDao extends BaseDao<Empj_BuildingAccountSupervised>
{
	public String getBasicHQL()
    {
    	return "from Empj_BuildingAccountSupervised where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if buildingInfoId??> and buildingInfo.tableId=:buildingInfoId</#if>"
		+ " <#if bankAccountSupervisedId??> and bankAccountSupervised.tableId=:bankAccountSupervisedId</#if>"
		+ " <#if beginTimeStamp??> and beginTimeStamp=:beginTimeStamp</#if>"
		+ " <#if endTimeStamp??> and endTimeStamp=:endTimeStamp</#if>" 
    	+ " <#if isUsing??> and isUsing=:isUsing</#if>";
    }
}
