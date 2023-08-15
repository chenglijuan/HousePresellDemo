package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_RecordForm;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_WorkflowForm;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Record;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：审批流-审批流程
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_ApprovalProcess_ModalListService
{
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_approvalProcess_afDao;
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_ApprovalProcess_WorkflowDao;
	@Autowired
	private Sm_Permission_RoleUserDao sm_permission_roleUserDao;
	@Autowired
	private Sm_Permission_RoleDao sm_permission_roleDao;
	@Autowired
	private Sm_AttachmentDao sm_attachmentDao;
	@Autowired
	private Sm_ApprovalProcess_RecordDao sm_ApprovalProcess_RecordDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Sm_ApprovalProcess_WorkflowForm model)
	{
		Properties properties = new MyProperties();

		Long approvalProcess_AFId = model.getApprovalProcess_AFId(); //申请单Id
		if(approvalProcess_AFId == null  || approvalProcess_AFId  < 1)
		{
			return MyBackInfo.fail(properties, "'申请单'不能为空");
		}

		Sm_ApprovalProcess_AF sm_approvalProcess_af = sm_approvalProcess_afDao.findById(approvalProcess_AFId);
		if(sm_approvalProcess_af == null)
		{
			return MyBackInfo.fail(properties, "'申请单'不能为空");
		}

		if(sm_approvalProcess_af.getWorkFlowList() == null)
		{
			return MyBackInfo.fail(properties, "'审批流程'不能为空");
		}
		List<Sm_ApprovalProcess_Workflow> sm_ApprovalProcess_WorkflowList = sm_approvalProcess_af.getWorkFlowList();

		Long workflowIdArray [] = new Long[sm_ApprovalProcess_WorkflowList.size()];
		for(int i=0 ; i< sm_ApprovalProcess_WorkflowList.size();i++)
		{
			workflowIdArray[i] = sm_ApprovalProcess_WorkflowList.get(i).getTableId();
		}
		Sm_ApprovalProcess_RecordForm recordForm = new Sm_ApprovalProcess_RecordForm();
		recordForm.setTheState(S_TheState.Normal);
		recordForm.setWorkflowIdArray(workflowIdArray);

		List<Sm_ApprovalProcess_Record> sm_ApprovalProcess_RecordList =  sm_ApprovalProcess_RecordDao.findByPage(sm_ApprovalProcess_RecordDao.getQuery(sm_ApprovalProcess_RecordDao.getBasicHQL(), recordForm));

		
		properties.put("sm_ApprovalProcess_WorkflowList", sm_ApprovalProcess_WorkflowList);
		properties.put("sm_ApprovalProcess_RecordList", sm_ApprovalProcess_RecordList);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
