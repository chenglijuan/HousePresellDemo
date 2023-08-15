package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Properties;

/*
 * Service删除：审批流-申请单
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_DeleteService
{
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;

	@SuppressWarnings("unchecked")
    public Properties execute(Long sourceId,String busiCode)
	{
		Properties properties = new MyProperties();

		//查找申请单
		Sm_ApprovalProcess_AFForm sm_approvalProcess_afForm = new Sm_ApprovalProcess_AFForm();
		sm_approvalProcess_afForm.setTheState(S_TheState.Normal);
		sm_approvalProcess_afForm.setBusiCode(busiCode);
		sm_approvalProcess_afForm.setSourceId(sourceId);
		sm_approvalProcess_afForm.setBusiState(S_ApprovalState.WaitSubmit);

//		Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_approvalProcess_afForm));

		List<Sm_ApprovalProcess_AF> sm_ApprovalProcess_AFs = sm_ApprovalProcess_AFDao.findByPage(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_approvalProcess_afForm));
		
		if(sm_ApprovalProcess_AFs !=null && sm_ApprovalProcess_AFs.size() > 0)
		{
		    for (Sm_ApprovalProcess_AF sm_ApprovalProcess_AF : sm_ApprovalProcess_AFs) {
		        for (Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow : sm_ApprovalProcess_AF.getWorkFlowList())
	            {
	                sm_approvalProcess_workflow.setTheState(S_TheState.Deleted);
	                sm_approvalProcess_workflow.getWorkflowConditionList().clear();
	                sm_approvalProcess_workflow.getSm_messageTemplate_cfgList().clear();
	            }
	            sm_ApprovalProcess_AF.getWorkFlowList().clear();
	            sm_ApprovalProcess_AF.setTheState(S_TheState.Deleted);
	            sm_ApprovalProcess_AFDao.save(sm_ApprovalProcess_AF);
	        }
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
