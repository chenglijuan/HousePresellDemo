package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_DepositManagementForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.dao.Tg_DepositManagementDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Tg_DepositManagement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：存单管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_DepositManagementDetailService
{
	@Autowired
	private Tg_DepositManagementDao tg_DepositManagementDao;

	@Autowired
	private Sm_ApprovalProcess_AFDao sm_approvalProcess_afDao;

	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;

	public Properties execute(Tg_DepositManagementForm model)
	{
		Properties properties = new MyProperties();

		Long tg_DepositManagementId = model.getTableId();
		Tg_DepositManagement tg_DepositManagement = (Tg_DepositManagement)tg_DepositManagementDao.findById(tg_DepositManagementId);
		if(tg_DepositManagement == null || S_TheState.Deleted.equals(tg_DepositManagement.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Tg_DepositManagement(Id:" + tg_DepositManagementId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tg_DepositManagement", tg_DepositManagement);


		//--------------------审批---------------------------------------//
		Long afId = model.getAfId();//申请单Id
		Sm_ApprovalProcess_AF sm_approvalProcess_af;
		Boolean isNeedBackup = null;
		if(afId!=null && afId > 0)
		{
			sm_approvalProcess_af = sm_approvalProcess_afDao.findById(afId);

			if(sm_approvalProcess_af == null || S_TheState.Deleted.equals(sm_approvalProcess_af.getTheState()))
			{
				return MyBackInfo.fail(properties, "'申请单'不存在");
			}
			Long currentNode = sm_approvalProcess_af.getCurrentIndex();
			Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = sm_approvalProcess_workflowDao.findById(currentNode);
			if(sm_approvalProcess_workflow.getNextWorkFlow() == null)
			{
				if(sm_approvalProcess_af.getIsNeedBackup().equals(1))
				{
					isNeedBackup = true;
				}
			}
			else
			{
				isNeedBackup = false;
			}
		}
		properties.put("isNeedBackup",isNeedBackup);
		//--------------------审批---------------------------------------//

		return properties;
	}
}
