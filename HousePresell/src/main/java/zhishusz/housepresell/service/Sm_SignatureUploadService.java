package zhishusz.housepresell.service;import zhishusz.housepresell.controller.form.Sm_SignatureForm;import zhishusz.housepresell.database.dao.Sm_AttachmentDao;import zhishusz.housepresell.database.po.Sm_Attachment;import zhishusz.housepresell.database.po.Sm_User;import zhishusz.housepresell.util.MyBackInfo;import zhishusz.housepresell.util.MyProperties;import zhishusz.housepresell.util.fileupload.OssServerUtil;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;import com.xiaominfo.oss.sdk.ReceiveMessage;import com.xiaominfo.oss.sdk.client.FileBytesResponse;import java.util.Properties;import javax.transaction.Transactional;/* * Service添加操作：签章文件上传service * Company：ZhiShuSZ * */@Service@Transactionalpublic class Sm_SignatureUploadService{	@Autowired	private Sm_AttachmentDao sm_AttachmentDao;//附件	@Autowired	private OssServerUtil ossUtil;//本地上传OSS			public Properties execute(Sm_SignatureForm model)	{		Properties properties = new MyProperties();		//当前操作用户		Sm_User sm_User = model.getSm_User();		//签章后的文件路径		String signaturePath = model.getSignaturePath();		//附件主键		Long signatureAttachmentTableId = model.getSignatureAttachmentTableId();				//根据附件主键查找对应附件		Sm_Attachment sm_Attachment = sm_AttachmentDao.findById(signatureAttachmentTableId);		if(null==sm_Attachment)		{			return MyBackInfo.fail(properties, "未查找到对应附件，请确认后重试");		}				// 上传Oss-Server		ReceiveMessage upload = ossUtil.upload(signaturePath);		if (null == upload)		{			return MyBackInfo.fail(properties, "文件上传失败");		}		FileBytesResponse ossMessage = upload.getData().get(0);		if (null == ossMessage)		{			return MyBackInfo.fail(properties, "文件上传失败");		}		String httpUrl = ossMessage.getUrl();		//切换文件资源地址并保存		sm_Attachment.setTheLink(httpUrl);		sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());		sm_Attachment.setUserUpdate(sm_User);				sm_AttachmentDao.save(sm_Attachment);				properties.put("pdfUrl", httpUrl);				return properties;			}}