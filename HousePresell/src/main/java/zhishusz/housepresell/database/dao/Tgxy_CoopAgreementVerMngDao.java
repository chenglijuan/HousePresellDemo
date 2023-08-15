package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgxy_CoopAgreementVerMng;

/*
 * Dao数据库操作：合作协议版本管理
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgxy_CoopAgreementVerMngDao extends BaseDao<Tgxy_CoopAgreementVerMng>
{
	public String getBasicHQL()
    {
    	return "from Tgxy_CoopAgreementVerMng where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theName??> and theName=:theName</#if>"
		+ " <#if theVersion??> and theVersion=:theVersion</#if>"
		+ " <#if theType??> and theType=:theType</#if>"
		+ " <#if enableTimeStamp??> and enableTimeStamp=:enableTimeStamp</#if>"
		+ " <#if downTimeStamp??> and downTimeStamp=:downTimeStamp</#if>"
		+ " <#if templateFilePath??> and templateFilePath=:templateFilePath</#if>"
		+ " <#if contractApplicationDate??> and (enableTimeStamp<=:contractApplicationDate and nvl(downTimeStamp,'9999-12-30')>=:contractApplicationDate )</#if>";
    }
	
	public String getBasicHQLBylike()
    {
    	return "from Tgxy_CoopAgreementVerMng where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theName??> and theName=:theName</#if>"
		+ " <#if theVersion??> and theVersion=:theVersion</#if>"
		+ " <#if theType??> and theType=:theType</#if>"
		+ " <#if enableTimeStamp??> and enableTimeStamp=:enableTimeStamp</#if>"
		+ " <#if downTimeStamp??> and downTimeStamp=:downTimeStamp</#if>"
		+ " <#if templateFilePath??> and templateFilePath=:templateFilePath</#if>"
		+ " <#if contractApplicationDate??> and (enableTimeStamp<=:contractApplicationDate and downTimeStamp>=:contractApplicationDate )</#if>"
		+ " <#if keyword??> and  theName like:keyword</#if>"
		+ " order by enableTimeStamp desc";   	
    }
	public String getBasicHQL1()
    {
    	return "from Tgxy_CoopAgreementVerMng where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"		
		+ " and (downTimeStamp is null or downtimestamp >= TO_CHAR(SYSDATE,'YYYY-MM-DD'))";   	
    }
	
	public String getBasicH2()
    {
    	return "from Tgxy_CoopAgreementVerMng where 1=1"
    	+ " <#if theType??> and theType=:theType</#if>"
		+ " and theState=0  order by  createTimeStamp desc";		
    }
	
	public String getBasicH3()
    {
    	return "from Tgxy_CoopAgreementVerMng where 1=1"
		+ " and theState=1 and enabletimestamp < = TO_CHAR(SYSDATE,'YYYY-MM-DD') and (downtimestamp >= TO_CHAR(SYSDATE,'YYYY-MM-DD') or downTimeStamp is null )";		
    }
}
