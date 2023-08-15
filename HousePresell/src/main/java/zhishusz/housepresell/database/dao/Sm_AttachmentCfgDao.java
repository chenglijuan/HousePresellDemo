package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Sm_AttachmentCfg;

/*
 * Dao数据库操作：附件配置
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_AttachmentCfgDao extends BaseDao<Sm_AttachmentCfg>
{
	public String getBasicHQL()
    {
    	return "from Sm_AttachmentCfg where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if busiType??> and busiType=:busiType</#if>"
		+ " <#if theName??> and theName=:theName</#if>"
		+ " <#if acceptFileType??> and acceptFileType=:acceptFileType</#if>"
		+ " <#if acceptFileCount??> and acceptFileCount=:acceptFileCount</#if>"
		+ " <#if maxFileSize??> and maxFileSize=:maxFileSize</#if>"
		+ " <#if minFileSize??> and minFileSize=:minFileSize</#if>"
		+ " <#if remark??> and remark=:remark</#if>"
		+ " <#if isImage??> and isImage=:isImage</#if>"
		+ " <#if isNeeded??> and isNeeded=:isNeeded</#if>"
		+ " <#if listType??> and listType=:listType</#if>";
    }
	public String getBasicHQLBylike()
    {
    	return "from Sm_AttachmentCfg where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if busiType??> and busiType=:busiType</#if>"
		+ " <#if theName??> and theName=:theName</#if>"
		+ " <#if acceptFileType??> and acceptFileType=:acceptFileType</#if>"
		+ " <#if acceptFileCount??> and acceptFileCount=:acceptFileCount</#if>"
		+ " <#if maxFileSize??> and maxFileSize=:maxFileSize</#if>"
		+ " <#if minFileSize??> and minFileSize=:minFileSize</#if>"
		+ " <#if remark??> and remark=:remark</#if>"
		+ " <#if isImage??> and isImage=:isImage</#if>"
		+ " <#if isNeeded??> and isNeeded=:isNeeded</#if>"
		+ " <#if listType??> and listType=:listType</#if>"
		+ " <#if keyword??> and ( basetheName like:keyword )</#if>";
    }
}
