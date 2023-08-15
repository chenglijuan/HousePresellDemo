package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tg_WitnessStatistics;

/*
 * Dao数据库操作：见证报告统计表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_WitnessStatisticsDao extends BaseDao<Tg_WitnessStatistics>
{
	public String getBasicHQL()
    {
    	return "from Tg_WitnessStatistics where 1=1"
		+ " <#if projectArea??> and projectArea=:projectArea</#if>"
    	+ " <#if billTimeStamp??> and reportUploadTime>=:billTimeStamp</#if>"	
    	+ " <#if endBillTimeStamp??> and reportUploadTime<=:endBillTimeStamp</#if>"		
		+ " <#if supervisionCompany??> and supervisionCompany=:supervisionCompany</#if>"
    	+ " <#if projectName??> and projectName=:projectName</#if>";		
    }	
}