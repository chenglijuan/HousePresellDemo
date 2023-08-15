package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_FundProjectInfo;

/*
 * Dao数据库操作：推送给财务系统-拨付凭证-项目信息
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_FundProjectInfoDao extends BaseDao<Tgpf_FundProjectInfo>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_FundProjectInfo where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if eCodeOfBuilding??> and eCodeOfBuilding=:eCodeOfBuilding</#if>"
		+ " <#if payoutAmount??> and payoutAmount=:payoutAmount</#if>";
    }
}
