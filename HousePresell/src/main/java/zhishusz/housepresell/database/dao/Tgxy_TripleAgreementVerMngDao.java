package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgxy_TripleAgreementVerMng;

/*
 * Dao数据库操作：三方协议版本管理
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgxy_TripleAgreementVerMngDao extends BaseDao<Tgxy_TripleAgreementVerMng>
{
	public String getBasicHQL()
    {
    	return "from Tgxy_TripleAgreementVerMng where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theName??> and theName=:theName</#if>"
		+ " <#if theVersion??> and theVersion=:theVersion</#if>"
		+ " <#if theType??> and theType=:theType</#if>"
		+ " <#if eCodeOfCooperationAgreement??> and eCodeOfCooperationAgreement=:eCodeOfCooperationAgreement</#if>"
		+ " <#if theNameOfCooperationAgreement??> and theNameOfCooperationAgreement=:theNameOfCooperationAgreement</#if>"
		+ " <#if enableTimeStamp??> and enableTimeStamp=:enableTimeStamp</#if>"
		+ " <#if downTimeStamp??> and downTimeStamp=:downTimeStamp</#if>"
		+ " <#if templateContentStyle??> and templateContentStyle=:templateContentStyle</#if>";
    }
	public String getBasicHQLBylike()
    {
    	return "from Tgxy_TripleAgreementVerMng where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theName??> and theName=:theName</#if>"
		+ " <#if theVersion??> and theVersion=:theVersion</#if>"
		+ " <#if theType??> and theType=:theType</#if>"
		+ " <#if eCodeOfCooperationAgreement??> and eCodeOfCooperationAgreement=:eCodeOfCooperationAgreement</#if>"
		+ " <#if theNameOfCooperationAgreement??> and theNameOfCooperationAgreement=:theNameOfCooperationAgreement</#if>"
		+ " <#if enableTimeStamp??> and enableTimeStamp=:enableTimeStamp</#if>"
		+ " <#if downTimeStamp??> and downTimeStamp=:downTimeStamp</#if>"
		+ " <#if templateContentStyle??> and templateContentStyle=:templateContentStyle</#if>"
		+ " <#if keyword??> and ( theName like:keyword or theVersion like:keyword )</#if>"
		+ " order by enableTimeStamp desc";
    }
	public String getBasicH1()
    {
    	return "from Tgxy_TripleAgreementVerMng where 1=1"
    	+ " <#if theType??> and theType=:theType</#if>"
		+ " and theState=0  order by  createTimeStamp desc";		
    }
	
	public String getBasicHQLByTiem(){
		return "from Tgxy_TripleAgreementVerMng where 1=1"
		    	+ " <#if theState??> and theState=:theState</#if>"
		    	+ " <#if enableTimeStamp??> and enableTimeStamp <=:enableTimeStamp</#if>"
		    	;		
	}
	
	public String getTheVersion(){
		return "from Tgxy_TripleAgreementVerMng where 1=1"
		    	+ " <#if theState??> and theState=:theState</#if>"
		    	+ " <#if busiState??> and busiState=:busiState</#if>"
		    	+ " order by enableTimeStamp desc";	
	}
}
