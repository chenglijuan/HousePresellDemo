package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：审批流-审批记录
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_ApprovalProcess_RecordRebuild extends RebuilderBase<Sm_ApprovalProcess_Record>
{

	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao sm_attachmentCfgDao;

	private MyDatetime myDatetime = MyDatetime.getInstance();

	@Override
	public Properties getSimpleInfo(Sm_ApprovalProcess_Record object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Sm_ApprovalProcess_Record object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("approvalProcess", object.getApprovalProcess());
		properties.put("approvalProcessId", object.getApprovalProcess().getTableId());
		properties.put("configuration", object.getConfiguration());
		properties.put("configurationId", object.getConfiguration().getTableId());
		properties.put("userOperate", object.getUserOperate());
		properties.put("userOperateId", object.getUserOperate().getTableId());
		properties.put("theContent", object.getTheContent());
		properties.put("theAction", object.getTheAction());
		properties.put("operateTimeStamp", object.getOperateTimeStamp());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_ApprovalProcess_Record> sm_ApprovalProcess_RecordList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_ApprovalProcess_RecordList != null)
		{
			for(Sm_ApprovalProcess_Record object:sm_ApprovalProcess_RecordList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());
				
				list.add(properties);
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List getRecordModalList(List<Sm_ApprovalProcess_Record> sm_ApprovalProcess_RecordList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_ApprovalProcess_RecordList != null)
		{
			for(Sm_ApprovalProcess_Record object:sm_ApprovalProcess_RecordList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());
				if(object.getApprovalProcess()!= null)
				{
					Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = object.getApprovalProcess();
					properties.put("approvalProcessId", sm_approvalProcess_workflow.getTableId());
					properties.put("theName",sm_approvalProcess_workflow.getTheName());//步骤名称
					properties.put("approvalModel",sm_approvalProcess_workflow.getApprovalModel());//审批模式
				}
				if(object.getUserOperate()!=null)
				{
					Sm_User userOperate = object.getUserOperate();
					properties.put("userOperateId", userOperate.getTableId());//处理人Id
					properties.put("userOperate", userOperate.getTheName()); //处理人姓名
				}
				properties.put("operateTimeStamp",myDatetime.dateToString2(object.getOperateTimeStamp())); //处理时间
				properties.put("theAction",object.getTheAction());//审批结果
				properties.put("theContent", object.getTheContent()); //审批评语

				//附件配置
				Sm_AttachmentCfgForm attachmentCfgForm = new Sm_AttachmentCfgForm();
				attachmentCfgForm.setTheState(S_TheState.Normal);
				attachmentCfgForm.setBusiType("01030101");
				List<Sm_AttachmentCfg> sm_attachmentCfgList = sm_attachmentCfgDao.findByPage(sm_attachmentCfgDao.getQuery(sm_attachmentCfgDao.getBasicHQL(), attachmentCfgForm));

				//附件
				Sm_AttachmentForm attachmentForm = new Sm_AttachmentForm();
				attachmentForm.setTheState(S_TheState.Normal);
				attachmentForm.setSourceId(String.valueOf(object.getTableId()));
				attachmentForm.setBusiType("01030101");
				List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), attachmentForm));

				List<Properties> attchment_Pro = new ArrayList<Properties>();
				for (Sm_Attachment sm_attachment : sm_AttachmentList)
				{
					Properties approvalAttachment_Pro = new MyProperties();

					approvalAttachment_Pro.put("tableId",sm_attachment.getTableId());
					if(sm_attachment.getUserUpdate() !=null)
					{
						approvalAttachment_Pro.put("uploadUserId",sm_attachment.getUserUpdate().getTableId());
					}
					approvalAttachment_Pro.put("name",sm_attachment.getRemark());
					approvalAttachment_Pro.put("url",sm_attachment.getTheLink());

					approvalAttachment_Pro.put("sourceType",sm_attachment.getSourceType());
					approvalAttachment_Pro.put("theLink",sm_attachment.getTheLink());
					approvalAttachment_Pro.put("theSize",sm_attachment.getTheSize());
					approvalAttachment_Pro.put("remark",sm_attachment.getRemark());
					approvalAttachment_Pro.put("fileType",sm_attachment.getFileType());
					approvalAttachment_Pro.put("busiType",sm_attachment.getBusiType());

					if(sm_attachment.getUserUpdate() !=null)
					{
						approvalAttachment_Pro.put("uploadUserId",sm_attachment.getUserUpdate().getTableId());
					}
					attchment_Pro.add(approvalAttachment_Pro);
				}

				List<Properties> attchmentCfg_Pro = new ArrayList<Properties>();
				for (Sm_AttachmentCfg sm_attachmentCfg : sm_attachmentCfgList)
				{
					Properties approvalAttachmentCfg_Pro = new MyProperties();
					approvalAttachmentCfg_Pro.put("listType",sm_attachmentCfg.getListType());
					approvalAttachmentCfg_Pro.put("acceptFileType",sm_attachmentCfg.getAcceptFileType());
					approvalAttachmentCfg_Pro.put("attchmentList",attchment_Pro);

					attchmentCfg_Pro.add(approvalAttachmentCfg_Pro);
				}
				properties.put("sm_attachmentCfgList", attchmentCfg_Pro);//附件配置
				list.add(properties);
			}
		}
		return list;
	}
}
