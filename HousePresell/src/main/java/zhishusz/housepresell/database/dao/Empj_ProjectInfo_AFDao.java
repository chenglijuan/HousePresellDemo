package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Empj_ProjectInfo_AF;

/*
 * Dao数据库操作：申请表-项目信息变更(审批)
 * Company：ZhiShuSZ
 * */
@Repository
public class Empj_ProjectInfo_AFDao extends BaseDao<Empj_ProjectInfo_AF>
{
	public String getBasicHQL()
    {
    	return "from Empj_ProjectInfo_AF where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>";
    }
}
