package zhishusz.housepresell.approvalprocess;

import java.util.Properties;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;

//审批流回调
public interface IApprovalProcessCallback
{
	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm);
}
