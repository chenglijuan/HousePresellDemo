package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentBatchForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyLong;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;
	
/*
 * Service添加操作：附件
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_AttachmentBatchAddService
{
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;

	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;

	@Autowired
	private Sm_UserDao sm_userDao;

	public <T extends BaseForm> Properties execute(T baseForm,Long tableId)
	{
		Sm_AttachmentBatchForm sm_AttachmentBatchForm = new Sm_AttachmentBatchForm();
		sm_AttachmentBatchForm.setTheState(S_TheState.Normal);
		sm_AttachmentBatchForm.setBusiType(baseForm.getBusiType());
		sm_AttachmentBatchForm.setSourceId(MyString.getInstance().parse(tableId));
		sm_AttachmentBatchForm.setGeneralAttachmentList(baseForm.getGeneralAttachmentList());
		sm_AttachmentBatchForm.setUserId(baseForm.getUserId());
		Properties execute = execute(sm_AttachmentBatchForm);
		return execute;
	}

	@SuppressWarnings("unchecked")
	public Properties execute(Sm_AttachmentBatchForm model)
	{
		Properties properties = new MyProperties();

		Sm_AttachmentForm theModel = new Sm_AttachmentForm();
		theModel.setBusiType(model.getBusiType());
		String attachmentSourceId = model.getSourceId();
		theModel.setSourceId(attachmentSourceId);
		Long loginUserId = model.getUserId();//登录用户
		Sm_User loginUser = sm_userDao.findById(loginUserId); //登录用户

		if(loginUser == null)
		{
			return MyBackInfo.fail(properties, "请登录");
		}

		if(model.getBusiType() == null || model.getBusiType().length() < 1)
		{
			return MyBackInfo.fail(properties, "请选择资源类型");
		}
		if(attachmentSourceId == null || attachmentSourceId.length() < 1 || MyLong.getInstance().parse(attachmentSourceId) == null)
		{
			return MyBackInfo.fail(properties, "请选择资源Id");
		}
		
		List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), theModel), null, null);
		for (Sm_Attachment sm_Attachment : sm_AttachmentList)
		{
			Long cfg = 52L;
			//过滤受限额度变更 pdf打印附件被删除问题
			if(cfg.equals(sm_Attachment.getAttachmentCfg().getTableId()))
			{
				continue;
			}
			sm_Attachment.setTheState(S_TheState.Deleted);
			sm_AttachmentDao.save(sm_Attachment);
		}
		
		Sm_AttachmentForm[] attachmentList = model.getGeneralAttachmentList();

		if (attachmentList != null && attachmentList.length > 0)
		{
			for (Sm_AttachmentForm attachmentForm : attachmentList)
			{
				attachmentForm.setBusiType(model.getBusiType());

				String sourceId = attachmentSourceId;
				String busiType = model.getBusiType();
				String sourceType = attachmentForm.getSourceType();
				String fileType = attachmentForm.getFileType();
				String theLink = attachmentForm.getTheLink();
				String theSize = attachmentForm.getTheSize();
				String remark = attachmentForm.getRemark();

				Sm_Attachment sm_Attachment = new Sm_Attachment();
				sm_Attachment.setTheState(S_TheState.Normal);
				sm_Attachment.setBusiType(busiType);

				Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
				form.seteCode(sourceType);
				Sm_AttachmentCfg sm_AttachmentCfg = sm_AttachmentCfgDao.findOneByQuery_T(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), form));

				sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);
				sm_Attachment.setSourceId(sourceId);
				sm_Attachment.setFileType(fileType);
				sm_Attachment.setTheLink(theLink);
				sm_Attachment.setTheSize(theSize);
				sm_Attachment.setRemark(remark);
				sm_Attachment.setUserStart(loginUser);
				sm_Attachment.setUserUpdate(loginUser);
				sm_Attachment.setCreateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setSourceType(sourceType);
				sm_AttachmentDao.save(sm_Attachment);
			}
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
