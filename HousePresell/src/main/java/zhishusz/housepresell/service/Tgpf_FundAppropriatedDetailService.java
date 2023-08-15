package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_FundAppropriatedForm;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriatedDao;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：资金拨付
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundAppropriatedDetailService
{
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;
	@Autowired
	private Tgpf_FundAppropriatedDao tgpf_FundAppropriatedDao;
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_approvalProcess_afDao;
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;


	public Properties execute(Tgpf_FundAppropriatedForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_FundAppropriated_AFId = model.getFundAppropriated_AFId();

		//用款申请主表信息
		Tgpf_FundAppropriated_AF tgpf_FundAppropriated_AF = (Tgpf_FundAppropriated_AF)tgpf_FundAppropriated_AFDao.findById(tgpf_FundAppropriated_AFId);
		if(tgpf_FundAppropriated_AF == null || S_TheState.Deleted.equals(tgpf_FundAppropriated_AF.getTheState()))
		{
			return MyBackInfo.fail(properties, "'用款申请主表(Id:" + tgpf_FundAppropriated_AFId + ")'不存在");
		}

		if(tgpf_FundAppropriated_AF.getFundOverallPlan()==null)
		{
			return MyBackInfo.fail(properties, "'统筹单'不存在");
		}

		Tgpf_FundOverallPlan tgpf_fundOverallPlan = tgpf_FundAppropriated_AF.getFundOverallPlan();

		//用款申请明细表信息
		List<Tgpf_FundAppropriated_AFDtl> tgpf_fundAppropriated_afDtlList ;
		if(tgpf_FundAppropriated_AF.getFundAppropriated_AFDtlList() != null)
		{
			tgpf_fundAppropriated_afDtlList = tgpf_FundAppropriated_AF.getFundAppropriated_AFDtlList();
		}
		else
		{
			tgpf_fundAppropriated_afDtlList = new ArrayList<>();
		}

		Tgpf_FundAppropriatedForm fundAppropriatedForm = new Tgpf_FundAppropriatedForm();
		fundAppropriatedForm.setTheState(S_TheState.Normal);
		fundAppropriatedForm.setFundAppropriated_AFId(tgpf_FundAppropriated_AFId);
		fundAppropriatedForm.setFundOverallPlanId(tgpf_fundOverallPlan.getTableId());
		fundAppropriatedForm.setOverallPlanPayoutAmount(0D);
		List<Tgpf_FundAppropriated> tgpf_FundAppropriatedList = tgpf_FundAppropriatedDao.findByPage(tgpf_FundAppropriatedDao.getQuery(tgpf_FundAppropriatedDao.getBasicHQL(), fundAppropriatedForm));

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
		//--------------------审批---------------------------------------//

		properties.put("tgpf_FundAppropriated_AF", tgpf_FundAppropriated_AF);
		properties.put("tgpf_fundAppropriated_afDtlList", tgpf_fundAppropriated_afDtlList);
		properties.put("tgpf_fundAppropriatedList", tgpf_FundAppropriatedList);
		properties.put("isNeedBackup",isNeedBackup);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
