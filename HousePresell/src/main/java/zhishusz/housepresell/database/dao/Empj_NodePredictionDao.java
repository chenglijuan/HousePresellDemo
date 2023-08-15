package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Empj_NodePrediction;

/*
 * Dao数据库操作：楼幢预测节点
 * Company：ZhiShuSZ
 * */
@Repository
public class Empj_NodePredictionDao extends BaseDao<Empj_NodePrediction>
{
	public String getBasicHQL()
    {
    	return "from Empj_NodePrediction where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if buildingId??> and building.tableId=:buildingId</#if>"
		+ " <#if expectFigureProgressId??> and expectFigureProgress.tableId=:expectFigureProgressId</#if>"
		
		+ " order by expectLimitedRatio";
    }
	
	public String getBasicDescHQL()
    {
        return "from Empj_NodePrediction where 1=1"
        + " <#if theState??> and theState=:theState</#if>"
        + " <#if busiState??> and busiState=:busiState</#if>"
        + " <#if eCode??> and eCode=:eCode</#if>"
        + " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
        + " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
        + " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
        + " <#if buildingId??> and building.tableId=:buildingId</#if>"
        
        + " order by expectLimitedRatio desc";
    }

}
