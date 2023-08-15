package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.database.po.Tg_Holiday;

import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：假期
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_HolidayDao extends BaseDao<Tg_Holiday>
{
	public String getBasicHQL()
    {
    	return "from Tg_Holiday where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if tableId??> and tableId=:tableId</#if>"
		+ " <#if dateTime??> and dateTime=:dateTime</#if>"
	    + " <#if startDateTime??> and dateTime>=:startDateTime</#if>"
	    + " <#if endDateTime??> and dateTime<=:endDateTime</#if>"
		;
    }

    public String getNextWorkDay(){
		String hqlFreemark="from Tg_Holiday where 1=1"
				+ " <#if theState??> and theState=:theState</#if>"
				+ " <#if type??> and type=:type</#if>"
				+ " <#if dateTime??> and dateTime>:dateTime</#if>"
				+" order by dateTime asc"
		;
		System.out.println("Tg_HolidayDao hqlFreemark is "+hqlFreemark);
		return hqlFreemark;
	}

	public String getNowWorkDay(){
		String hqlFreemark="from Tg_Holiday where 1=1"
				+ " <#if theState??> and theState=:theState</#if>"
				+ " <#if type??> and type=:type</#if>"
				+ " <#if dateTime??> and dateTime=:dateTime</#if>"
				;
		System.out.println("Tg_HolidayDao hqlFreemark is "+hqlFreemark);
		return hqlFreemark;
	}
}
