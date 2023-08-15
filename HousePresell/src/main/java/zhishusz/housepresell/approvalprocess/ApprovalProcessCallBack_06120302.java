package zhishusz.housepresell.approvalprocess;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriatedForm;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriatedDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_FundOverallPlanDao;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.*;
import zhishusz.housepresell.external.service.BatchBankTransfersService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.log.Log;

import java.util.List;
import java.util.Properties;

/**
 * 资金统筹与复核：
 * 审批过后-业务逻辑处理
 */
@Transactional
public class ApprovalProcessCallBack_06120302 implements IApprovalProcessCallback
{
	@Autowired
	private Tgpf_FundOverallPlanDao tgpf_fundOverallPlanDao;
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_fundAppropriated_afDao;
	@Autowired
	private Tgpf_FundAppropriatedDao tgpf_fundAppropriatedDao;
	
	@Autowired
	private BatchBankTransfersService batchBankTransfersService;

	@SuppressWarnings("unchecked")
	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm)
	{
		Properties properties = new MyProperties();

		try
		{
			String workflowEcode = approvalProcessWorkflow.geteCode();

			// 获取正在处理的申请单
			Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();
			
			// 获取正在处理的申请单所属的流程配置
			Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = sm_ApprovalProcess_AF.getConfiguration();
			String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;
			
			// 获取正在审批的统筹单
			Long tgpf_FundOverallPlanId = sm_ApprovalProcess_AF.getSourceId();
			Tgpf_FundOverallPlan tgpf_fundOverallPlan = tgpf_fundOverallPlanDao.findById(tgpf_FundOverallPlanId);
			
			if(tgpf_fundOverallPlan == null)
			{
				return MyBackInfo.fail(properties, "审批的统筹单不存在");
			}

			// 不通过
			if(S_ApprovalState.NoPass.equals(sm_ApprovalProcess_AF.getBusiState()))
			{
				tgpf_fundOverallPlan.setApprovalState(S_ApprovalState.NoPass);
				tgpf_fundOverallPlan.setTheState(S_TheState.Deleted);
				for (Tgpf_FundAppropriated_AF tgpf_fundAppropriated_af : tgpf_fundOverallPlan.getFundAppropriated_AFList())
				{
					tgpf_fundAppropriated_af.setApplyState(S_ApplyState.Admissible); // 申请单状态置为 ：已受理
				}

				//删除资金拨付
				for (Tgpf_FundAppropriated tgpf_fundAppropriated : tgpf_fundOverallPlan.getFundAppropriatedList())
				{
					tgpf_fundAppropriated.setTheState(S_TheState.Deleted);
				}
				tgpf_fundOverallPlanDao.save(tgpf_fundOverallPlan);
			}

			switch (approvalProcessWork)
			{
				case "06120302001_ZS":
					if(S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState()) && S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
					{
						for(Tgpf_FundAppropriated_AF tgpf_fundAppropriated_af : tgpf_fundOverallPlan.getFundAppropriated_AFList())
						{
							tgpf_fundAppropriated_af.setApplyState(S_ApplyState.Alreadycoordinated); // 统筹审批通过：用款申请单状态置为"已统筹"

							tgpf_fundAppropriated_afDao.save(tgpf_fundAppropriated_af);
						}
						
						tgpf_fundOverallPlan.setBusiState(S_CoordinateState.Alreadycoordinated);
						tgpf_fundOverallPlan.setApprovalState(S_ApprovalState.Completed);

						if(approvalProcessWorkflow.getUserUpdate()!=null)
						{
							tgpf_fundOverallPlan.setUserRecord(approvalProcessWorkflow.getUserUpdate());
							tgpf_fundOverallPlan.setRecordTimeStamp(System.currentTimeMillis());
						}
						tgpf_fundOverallPlanDao.save(tgpf_fundOverallPlan);
						//是否推送
						try {
							boolean isPush = true;
							List<Tgpf_FundAppropriated> fundAppropriatedList;
							Tgpf_FundAppropriatedForm fundAppropriatedForm;
							Tgxy_BankAccountEscrowed bankAccountEscrowed;
							for(Tgpf_FundAppropriated_AF tgpf_fundAppropriated_af : tgpf_fundOverallPlan.getFundAppropriated_AFList())
							{
								
								fundAppropriatedForm = new Tgpf_FundAppropriatedForm();
								fundAppropriatedForm.setTheState(S_TheState.Normal);
								fundAppropriatedForm.setFundAppropriated_AFId(tgpf_fundAppropriated_af.getTableId());
								fundAppropriatedForm.setFundOverallPlanId(tgpf_fundOverallPlan.getTableId());
								fundAppropriatedForm.setOverallPlanPayoutAmount(0D);
								fundAppropriatedList = tgpf_fundAppropriatedDao.findByPage(tgpf_fundAppropriatedDao.getQuery(tgpf_fundAppropriatedDao.getBasicHQL(), fundAppropriatedForm));
	
								for (Tgpf_FundAppropriated tgpf_FundAppropriated : fundAppropriatedList) {
									
									bankAccountEscrowed = tgpf_FundAppropriated.getBankAccountEscrowed();
									if(!(1 == bankAccountEscrowed.getBankBranch().getIsDocking())){
										isPush = false;
										break;
									}
								}
								
								if(!isPush){
									break;
								}
							}
	
							//进行推送
						
							if (isPush) {
								for(Tgpf_FundAppropriated_AF tgpf_fundAppropriated_af : tgpf_fundOverallPlan.getFundAppropriated_AFList())
								{
									batchBankTransfersService.execute("06120302", tgpf_fundAppropriated_af.getTableId(), sm_ApprovalProcess_AF.getCurrentIndex(), baseForm);
								}
							}
						} catch (Exception e) {
							System.out.println("统筹拨付异常！"+e.getMessage());
						}
						
					}
					break;
				default:
					properties.put(S_NormalFlag.result, S_NormalFlag.success);
					properties.put(S_NormalFlag.info, "没有需要处理的回调");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_BusiError);
		}

		return properties;
	}
}
