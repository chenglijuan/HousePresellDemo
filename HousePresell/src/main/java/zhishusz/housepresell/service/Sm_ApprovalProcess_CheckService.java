package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_CfgForm;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_RecordForm;
import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 审批人校验
 * 业务审批流流程中时，业务提交人和审批流中的初审人员，
 * 后续审批流审核人和前一个审核人（和后一个审核人）不得为同一个人。
 * 例如， 审批流配置：T1(提交)->T2->T3->T4，T1配置角色R1，R1中有用户A，T2配置角色R2，R2中有用户A、B，T3配置角色R3，
 * R3中有用户B、C，T4配置角色R4，R4中有用户B、D; A提交后，T2环节，A不可审核；T2环节B审核，
 * 则T3环节时B不可审核；T4环节时，B和D都可审核；
 */
@Service
public class Sm_ApprovalProcess_CheckService
{
	@Autowired
	private Sm_ApprovalProcess_CfgDao sm_ApprovalProcess_CfgDao;
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;
	@Autowired
	private Sm_Permission_RoleUserDao sm_permission_roleUserDao;
	@Autowired
	private Sm_ApprovalProcess_RecordDao sm_ApprovalProcess_RecordDao;
	@Autowired
	private Sm_UserDao sm_userDao;

	@SuppressWarnings({"unchecked"})
	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcess_workflow,Long userStartId)
	{
		Properties properties = new MyProperties();

		if(approvalProcess_workflow.getApprovalProcess_AF() == null)
		{
			return MyBackInfo.fail(properties, "申请单不能为空");
		}

		Sm_ApprovalProcess_AF sm_approvalProcess_af = approvalProcess_workflow.getApprovalProcess_AF();

		if(sm_approvalProcess_af.getUserStart() == null)
		{
			return MyBackInfo.fail(properties, "发起人不能为空");
		}

		Long userId = sm_approvalProcess_af.getUserStart().getTableId();

		if(approvalProcess_workflow.getLastWorkFlow() !=null)
		{
			if(approvalProcess_workflow.getLastWorkFlow().getNodeType().equals(0))
			{
				if(userStartId.equals(userId))
				{
					return MyBackInfo.fail(properties, "业务提交人和初审人员不能为同一个人");
				}
			}
			else
			{
				Long sourceId = approvalProcess_workflow.getSourceId(); //来源结点
				if(sourceId == null || sourceId < 0)
				{
					return MyBackInfo.fail(properties, "来源结点不能为空");
				}

				Sm_ApprovalProcess_Workflow sourceWorkflow = sm_approvalProcess_workflowDao.findById(sourceId);//来源结点
				if(sourceWorkflow == null)
				{
					return MyBackInfo.fail(properties, "来源结点不能为空");
				}

				Sm_ApprovalProcess_RecordForm recordForm = new Sm_ApprovalProcess_RecordForm();
				recordForm.setTheState(S_TheState.Normal);
				recordForm.setApprovalProcessId(sourceId);
				if(sourceWorkflow.getLastUpdateTimeStamp()!=null)
				{
					recordForm.setWorkflowTime(approvalProcess_workflow.getLastUpdateTimeStamp());
				}
				List<Sm_ApprovalProcess_Record>	sm_ApprovalProcess_RecordList = sm_ApprovalProcess_RecordDao.findByPage(sm_ApprovalProcess_RecordDao.getQuery(sm_ApprovalProcess_RecordDao.getBasicHQL(), recordForm));
				for(Sm_ApprovalProcess_Record sm_approvalProcess_record : sm_ApprovalProcess_RecordList)
				{
					if(sm_approvalProcess_record.getUserOperate() == null)
					{
						return MyBackInfo.fail(properties, "操作人不能为空");
					}
					if(sm_approvalProcess_record.getUserOperate().getTableId().equals(userStartId))
					{
						return MyBackInfo.fail(properties, "后续审批流审核人和前一个审核人不得为同一个人");
					}
				}
			}
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
