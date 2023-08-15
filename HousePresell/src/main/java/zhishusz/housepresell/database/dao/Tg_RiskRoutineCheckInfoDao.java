package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckInfo;

/*
 * Dao数据库操作：风控例行抽查表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_RiskRoutineCheckInfoDao extends BaseDao<Tg_RiskRoutineCheckInfo>
{
	public String getBasicHQL()
    {
    	return "from Tg_RiskRoutineCheckInfo where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if spotTimeStamp??> and spotTimeStamp=:spotTimeStamp</#if>"
		+ " <#if bigBusiType??> and bigBusiType=:bigBusiType</#if>"
		+ " <#if smallBusiType??> and smallBusiType=:smallBusiType</#if>"
		+ " <#if eCodeOfBill??> and eCodeOfBill=:eCodeOfBill</#if>"
		+ " <#if checkResult??> and checkResult=:checkResult</#if>"
		+ " <#if unqualifiedReasons??> and unqualifiedReasons=:unqualifiedReasons</#if>"
		+ " <#if isDoPush??> and isDoPush=:isDoPush</#if>"
		+ " <#if modifyFeedback??> and modifyFeedback=:modifyFeedback</#if>"
		+ " <#if forensicConfirmation??> and forensicConfirmation=:forensicConfirmation</#if>"
	    + " <#if startDateTimeStamp??> and spotTimeStamp>=:startDateTimeStamp</#if>"
	    + " <#if endDateTimeStamp??> and spotTimeStamp<:endDateTimeStamp</#if>";
    }
}
