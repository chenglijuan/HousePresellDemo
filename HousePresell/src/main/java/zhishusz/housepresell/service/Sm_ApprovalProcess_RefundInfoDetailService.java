package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Tgpf_RefundInfoDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：退房退款 审批流
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Sm_ApprovalProcess_RefundInfoDetailService
{

	@Autowired
	private Tgpf_RefundInfoDao tgpf_RefundInfoDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_approvalProcess_afDao;
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_ApprovalProcess_WorkflowDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_RefundInfoForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_RefundInfoId = model.getTableId();
		
		Long afId = model.getAfId();
		Long workflowId = model.getWorkflowId();//当前审批节点编码
		
		Tgpf_RefundInfo tgpf_RefundInfo = (Tgpf_RefundInfo) tgpf_RefundInfoDao.findById(tgpf_RefundInfoId);
		if (tgpf_RefundInfo == null || S_TheState.Deleted.equals(tgpf_RefundInfo.getTheState()))
		{
			return MyBackInfo.fail(properties, "未查询到有效的退房退款数据");
		}

		Sm_AttachmentForm attachmentForm = new Sm_AttachmentForm();

		String sourceId = String.valueOf(tgpf_RefundInfoId);
		attachmentForm.setBusiType(model.getBusiCode());
		attachmentForm.setSourceId(sourceId);
		attachmentForm.setTheState(S_TheState.Normal);

		// 查询附件
		List<Sm_Attachment> smAttachmentList = smAttachmentDao
				.findByPage(smAttachmentDao.getQuery(smAttachmentDao.getBasicHQL2(), attachmentForm));

		if (null == smAttachmentList || smAttachmentList.size() == 0)
		{
			smAttachmentList = new ArrayList<Sm_Attachment>();
		}

		// 查询附件类型
		List<Sm_Attachment> smList = null;
		Sm_AttachmentCfgForm attachmentCfgForm = new Sm_AttachmentCfgForm();
		attachmentCfgForm.setTheState(0);
		attachmentCfgForm.setBusiType(model.getBusiCode());
		List<Sm_AttachmentCfg> smAttachmentCfgList = sm_AttachmentCfgDao
				.findByPage(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), attachmentCfgForm));
		if (null == smAttachmentCfgList || smAttachmentCfgList.size() == 0)
		{
			smAttachmentCfgList = new ArrayList<Sm_AttachmentCfg>();
		}
		else
		{
			for (Sm_Attachment sm_Attachment : smAttachmentList)
			{
				Long cfgTableId = sm_Attachment.getAttachmentCfg().getTableId();
				
				for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList)
				{
					if (sm_AttachmentCfg.getTableId() == cfgTableId)
					{
						smList = sm_AttachmentCfg.getSmAttachmentList();
						if (null == smList || smList.size() == 0)
						{
							smList = new ArrayList<Sm_Attachment>();
						}
						smList.add(sm_Attachment);
						sm_AttachmentCfg.setSmAttachmentList(smList);
					}
				}
			}
		}
		
		//查询审批流信息
		Sm_ApprovalProcess_AF sm_approvalProcess_af = sm_approvalProcess_afDao.findById(afId);
		if(sm_approvalProcess_af == null || S_TheState.Deleted.equals(sm_approvalProcess_af.getTheState()))
		{
			return MyBackInfo.fail(properties, "'申请单'不存在");
		}

		if(sm_approvalProcess_af.getConfiguration() == null)
		{
			return MyBackInfo.fail(properties, "'流程配置'不存在");
		}
		Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = sm_approvalProcess_af.getConfiguration();

		String busiCode = sm_approvalProcess_af.getBusiCode();
		
		/**
		 * 判断当前节点进度
		 * 		根据当前节点Id 获取当前节点状态
		 * 		判断节点状态，如果是已办保存按钮不可使用，否则可以使用
		 */
		String isUseButten = "0";//可以使用
		Sm_ApprovalProcess_Workflow sm_ApprovalProcess_Workflow = sm_ApprovalProcess_WorkflowDao.findById(workflowId);
		if(null != sm_ApprovalProcess_Workflow && sm_ApprovalProcess_Workflow.getBusiState().equals(S_WorkflowBusiState.Completed)){
			isUseButten = "1";//不可使用
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgpf_RefundInfo", tgpf_RefundInfo);
		properties.put("smAttachmentCfgList", smAttachmentCfgList);
		properties.put("sm_approvalProcess_cfg", sm_approvalProcess_cfg);
		properties.put("sm_Permission_Role", new Sm_Permission_Role());
		properties.put("busiCode",busiCode);
		properties.put("isUseButten",isUseButten);
		return properties;
	}
}
