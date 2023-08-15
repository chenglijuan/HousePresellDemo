package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
	
/*
 * Service添加操作：附件
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_AttachmentAddService
{
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Sm_AttachmentForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = model.getTheState();
		Long userStartId = model.getUserStartId();
		Long createTimeStamp = model.getCreateTimeStamp();
		String sourceType = model.getSourceType();
		String sourceId = model.getSourceId();
		String fileType = model.getFileType();
		Integer totalPage = model.getTotalPage();
		String theLink = model.getTheLink();
		String theSize = model.getTheSize();
		String remark = model.getRemark();
		String md5Info = model.getMd5Info();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(createTimeStamp == null || createTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'createTimeStamp'不能为空");
		}
		if(sourceType == null || sourceType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'sourceType'不能为空");
		}
		if(sourceId == null || sourceId.length() == 0)
		{
			return MyBackInfo.fail(properties, "'sourceId'不能为空");
		}
		if(fileType == null || fileType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'fileType'不能为空");
		}
		if(totalPage == null || totalPage < 1)
		{
			return MyBackInfo.fail(properties, "'totalPage'不能为空");
		}
		if(theLink == null || theLink.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theLink'不能为空");
		}
		if(theSize == null ||  theSize.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theSize'不能为空");
		}
		if(remark == null || remark.length() == 0)
		{
			return MyBackInfo.fail(properties, "'remark'不能为空");
		}
		if(md5Info == null || md5Info.length() == 0)
		{
			return MyBackInfo.fail(properties, "'md5Info'不能为空");
		}

		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
	
		Sm_Attachment sm_Attachment = new Sm_Attachment();
		sm_Attachment.setTheState(theState);
		sm_Attachment.setUserStart(userStart);
		sm_Attachment.setCreateTimeStamp(createTimeStamp);
		sm_Attachment.setSourceType(sourceType);
		sm_Attachment.setSourceId(sourceId);
		sm_Attachment.setFileType(fileType);
		sm_Attachment.setTotalPage(totalPage);
		sm_Attachment.setTheLink(theLink);
		sm_Attachment.setTheSize(theSize);
		sm_Attachment.setRemark(remark);
		sm_Attachment.setMd5Info(md5Info);
		sm_AttachmentDao.save(sm_Attachment);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
