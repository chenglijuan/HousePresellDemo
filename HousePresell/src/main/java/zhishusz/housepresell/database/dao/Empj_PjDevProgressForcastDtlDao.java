package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Empj_PjDevProgressForcastDtl;

/*
 * Dao数据库操作：项目-工程进度预测 -明细表 
 * Company：ZhiShuSZ
 * */
@Repository
public class Empj_PjDevProgressForcastDtlDao extends BaseDao<Empj_PjDevProgressForcastDtl>
{
	public String getBasicHQL()
    {
    	return "from Empj_PjDevProgressForcastDtl where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if patrolTimestamp??> and patrolTimestamp=:patrolTimestamp</#if>"
		+ " <#if currentProgressNode??> and currentProgressNode=:currentProgressNode</#if>"
		+ " <#if predictedFigureProgress??> and predictedFigureProgress=:predictedFigureProgress</#if>"
		+ " <#if predictedFinishDatetime??> and predictedFinishDatetime=:predictedFinishDatetime</#if>"
		+ " <#if progressJudgement??> and progressJudgement=:progressJudgement</#if>"
		+ " <#if causeDescriptionForDelay??> and causeDescriptionForDelay=:causeDescriptionForDelay</#if>"
		+ " <#if remark??> and remark=:remark</#if>"
		+ " <#if userStartId??> and userStart.tableId=:userStartId</#if>"
		+ " <#if userUpdateId??> and userUpdate.tableId=:userUpdateId</#if>";
    }

	public String getExcelListHQL()
	{
		////编辑主表时，删除明细表信息，如果点击保存按钮后才更新主表与明细表关联数据，可释放此处
		return "from Empj_PjDevProgressForcastDtl where 1=1"
//				+ " <#if theState??> and theState=:theState</#if>"
				+ " <#if idArr??> and tableId in :idArr</#if>";
	}
}
