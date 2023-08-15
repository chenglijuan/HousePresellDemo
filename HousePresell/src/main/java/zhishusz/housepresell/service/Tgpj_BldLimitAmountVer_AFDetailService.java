package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.IsNeedBackupUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service详情：版本管理-受限节点设置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BldLimitAmountVer_AFDetailService
{
	@Autowired
	private Tgpj_BldLimitAmountVer_AFDao tgpj_BldLimitAmountVer_AFDao;
	@Autowired
	private Sm_BaseParameterGetService sm_BaseParameterGetService;
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;
	@Autowired
	private IsNeedBackupUtil isNeedBackupUtil;

	public Properties execute(Tgpj_BldLimitAmountVer_AFForm model)
	{
		Properties properties = new MyProperties();

		Long tgpj_BldLimitAmountVer_AFId = model.getTableId();
		Tgpj_BldLimitAmountVer_AF tgpj_BldLimitAmountVer_AF = (Tgpj_BldLimitAmountVer_AF)tgpj_BldLimitAmountVer_AFDao.findById(tgpj_BldLimitAmountVer_AFId);
//		if(tgpj_BldLimitAmountVer_AF == null || S_TheState.Deleted.equals(tgpj_BldLimitAmountVer_AF.getTheState()))
		if(tgpj_BldLimitAmountVer_AF == null)
		{
			return MyBackInfo.fail(properties, "'Tgpj_BldLimitAmountVer_AF(Id:" + tgpj_BldLimitAmountVer_AFId + ")'不存在");
		}
		Sm_BaseParameter sm_BaseParameter = sm_BaseParameterGetService.getParameter("5", tgpj_BldLimitAmountVer_AF.getTheType());
		String parameterName = "";
		if(sm_BaseParameter != null)parameterName = sm_BaseParameter.getTheName();

//		Sm_ApprovalProcess_AFForm sm_ApprovalProcess_AFForm = new Sm_ApprovalProcess_AFForm();
//		sm_ApprovalProcess_AFForm.setTheState(S_TheState.Normal);
////		sm_ApprovalProcess_AFForm.setBusiState("待提交");
//		sm_ApprovalProcess_AFForm.setBusiCode(S_BusiCode.busiCode_06010102);
//		sm_ApprovalProcess_AFForm.setSourceId(tgpj_BldLimitAmountVer_AF.getTableId());
//		Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));
//		boolean isHasApproveProcess = true;
//		if (sm_ApprovalProcess_AF == null) {
//			isHasApproveProcess = false;
//		}else{
//			isHasApproveProcess=true;
//		}
//		if (isHasApproveProcess) {
//			Long currentNode = sm_ApprovalProcess_AF.getCurrentIndex();
//			Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = sm_approvalProcess_workflowDao
//					.findById(currentNode);
//			Boolean isNeedBackup = null;
//			if (sm_approvalProcess_workflow.getNextWorkFlow() == null) {
//				if (S_IsNeedBackup.Yes.equals(sm_ApprovalProcess_AF.getIsNeedBackup())) {
//					isNeedBackup = true;
//				} else {
//					isNeedBackup = false;
//				}
//			} else {
//				isNeedBackup = false;
//			}
//			properties.put("isNeedBackup", isNeedBackup);//是否显示备案按钮
//		}
		if(model.getAfId()!=null){
			Sm_ApprovalProcess_AF af = sm_ApprovalProcess_AFDao.findById(model.getAfId());
			isNeedBackupUtil.setIsNeedBackup(properties,af);

		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgpj_BldLimitAmountVer_AF", tgpj_BldLimitAmountVer_AF);
		properties.put("parameterName", parameterName);

		return properties;
	}
}
