package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement;

/*
 * Dao数据库操作：网银对账-后台上传的账单原始Excel数据-主表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_CyberBankStatementDao extends BaseDao<Tgpf_CyberBankStatement>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_CyberBankStatement where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theNameOfBank??> and theNameOfBank=:theNameOfBank</#if>"
		+ " <#if theAccountOfBankAccountEscrowed??> and theAccountOfBankAccountEscrowed=:theAccountOfBankAccountEscrowed</#if>"
		+ " <#if theNameOfBankAccountEscrowed??> and theNameOfBankAccountEscrowed=:theNameOfBankAccountEscrowed</#if>"
		+ " <#if theNameOfBankBranch??> and theNameOfBankBranch=:theNameOfBankBranch</#if>"
		+ " <#if reconciliationState??> and reconciliationState=:reconciliationState</#if>"
		+ " <#if reconciliationUser??> and reconciliationUser=:reconciliationUser</#if>"
		+ " <#if billTimeStamp??> and billTimeStamp=:billTimeStamp</#if>"
		+ " <#if orgFilePath??> and orgFilePath=:orgFilePath</#if>";
    }
	
	public String getBasicHQL2()
    {
    	return "from Tgpf_CyberBankStatement where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if keyword??> and (theNameOfBank like :keyword or accountOfBankAccountEscrowed like :keyword )</#if>"
		+ " <#if uploadTimeStamp??> and uploadTimeStamp=:uploadTimeStamp</#if>"
		+ " <#if billTimeStamp??> and billTimeStamp=:billTimeStamp</#if>"
		+ " <#if theAccountOfBankAccountEscrowed??> and theAccountOfBankAccountEscrowed=:theAccountOfBankAccountEscrowed</#if>"
		+ " order by billTimeStamp desc, NLSSORT(theNameOfBank, 'NLS_SORT = SCHINESE_PINYIN_M')";
    }
}
