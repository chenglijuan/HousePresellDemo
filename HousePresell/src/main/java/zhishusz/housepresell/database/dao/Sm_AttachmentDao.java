package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Sm_Attachment;

/*
 * Dao数据库操作：附件
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_AttachmentDao extends BaseDao<Sm_Attachment>
{
	public String getBasicHQL()
    {
    	return "from Sm_Attachment where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if sourceType??> and sourceType=:sourceType</#if>"
		+ " <#if sourceId??> and sourceId=:sourceId</#if>"
		+ " <#if fileType??> and fileType=:fileType</#if>"
		+ " <#if totalPage??> and totalPage=:totalPage</#if>"
		+ " <#if theLink??> and theLink=:theLink</#if>"
		+ " <#if theSize??> and theSize=:theSize</#if>"
		+ " <#if remark??> and remark=:remark</#if>"
		+ " <#if md5Info??> and md5Info=:md5Info</#if>"
		+ " <#if attachmentCfg??> and attachmentCfg=:attachmentCfg</#if>"
		+ " <#if busiType??> and busiType=:busiType</#if>";
    }

	public String getBasicHQL2()
	{
		return "from Sm_Attachment where 1=1"
				+ " <#if theState??> and theState=:theState</#if>"
				+ " <#if sourceId??> and sourceId=:sourceId</#if>"
				+ " <#if busiType??> and busiType=:busiType</#if>";
	}
	
	public String getHandlerPhotoHQL()
    {
        return "from Sm_Attachment where 1=1"
        + " <#if theState??> and theState=:theState</#if>"
        + " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
        + " <#if sourceType??> and sourceType=:sourceType</#if>"
        + " <#if sourceId??> and sourceId=:sourceId</#if>"
        + " <#if totalPage??> and totalPage=:totalPage</#if>"
        + " <#if theLink??> and theLink=:theLink</#if>"
        + " <#if theSize??> and theSize=:theSize</#if>"
        + " <#if remark??> and remark=:remark</#if>"
        + " <#if md5Info??> and md5Info=:md5Info</#if>"
        + " <#if attachmentCfg??> and attachmentCfg=:attachmentCfg</#if>"
        + " <#if busiType??> and busiType=:busiType</#if>"
        //+ " and (fileType like '%jpg' or fileType like '%png' or fileType like '%JPG' or fileType like '%PNG') " +
		+ " order by attachmentCfg";
    }
	
	public String getPushPhotoHQL()
    {
        return "from Sm_Attachment where 1=1"
        + " <#if theState??> and theState=:theState</#if>"
        + " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
        + " <#if sourceType??> and sourceType=:sourceType</#if>"
        + " <#if sourceId??> and sourceId=:sourceId</#if>"
        + " <#if totalPage??> and totalPage=:totalPage</#if>"
        + " <#if theLink??> and theLink=:theLink</#if>"
        + " <#if theSize??> and theSize=:theSize</#if>"
        + " <#if remark??> and remark=:remark</#if>"
        + " <#if md5Info??> and md5Info=:md5Info</#if>"
        + " <#if attachmentCfg??> and attachmentCfg=:attachmentCfg</#if>"
        + " <#if busiType??> and busiType=:busiType</#if>"
        + " and (fileType like '%jpg' or fileType like '%png' or fileType like '%JPG' or fileType like '%PNG') and md5Info is not null and sortNum is not null order by sortNum";
    }
}
