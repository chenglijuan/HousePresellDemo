package zhishusz.housepresell.approvalprocess;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Tgpf_BuildingRemainRightLogForm;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_ApplyState;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.messagequeue.producer.MQKey_EventType;
import zhishusz.housepresell.messagequeue.producer.MQKey_OrgType;
import zhishusz.housepresell.service.Tgpf_BuildingRemainRightLogPublicAddService;
import zhishusz.housepresell.util.MQConnectionUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/**
 * 用款申请和复核：
 * 审批过后-业务逻辑处理
 */
@Transactional
public class ApprovalProcessCallBack_06120301 implements IApprovalProcessCallback
{
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_fundAppropriated_afDao;
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_buildingAccountDao;
	@Autowired 
	private MQConnectionUtil mqConnectionUtil;
	@Autowired 
	private Tgpf_BuildingRemainRightLogPublicAddService tgpf_BuildingRemainRightLogPublicAddService;

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
			
			// 获取正在审批的用款申请
			Long fundAppropriated_AFId = sm_ApprovalProcess_AF.getSourceId();
			Tgpf_FundAppropriated_AF tgpf_fundAppropriated_af = tgpf_fundAppropriated_afDao.findById(fundAppropriated_AFId);
			
			if(tgpf_fundAppropriated_af == null)
			{
				return MyBackInfo.fail(properties, "审批的用款申请不存在");
			}

			//驳回到发起人 ，发起人撤回 ，不通过  - 回写楼幢账户金额
			if(S_ApprovalState.WaitSubmit.equals(sm_ApprovalProcess_AF.getBusiState()) || S_ApprovalState.NoPass.equals(sm_ApprovalProcess_AF.getBusiState()))
			{
				for (Tgpf_FundAppropriated_AFDtl tgpf_fundAppropriated_afDtl : tgpf_fundAppropriated_af.getFundAppropriated_AFDtlList())
				{
					Double appliedAmount = tgpf_fundAppropriated_afDtl.getAppliedAmount(); // 本次划款申请金额（元）
					if(tgpf_fundAppropriated_afDtl.getBuilding()!=null)
					{
						Empj_BuildingInfo empj_buildingInfo = tgpf_fundAppropriated_afDtl.getBuilding();
						if(empj_buildingInfo.getBuildingAccount()!=null)
						{
							Tgpj_BuildingAccount tgpj_buildingAccount = empj_buildingInfo.getBuildingAccount();

							Double allocableAmount = tgpj_buildingAccount.getAllocableAmount(); // 当前可划拨金额
							Double appliedNoPayoutAmount = tgpj_buildingAccount.getAppliedNoPayoutAmount();// 已申请未拨付金额
							Double appropriateFrozenAmount = tgpj_buildingAccount.getAppropriateFrozenAmount(); //拨付冻结金额
							if(appliedNoPayoutAmount == null)
							{
								appliedNoPayoutAmount = 0.0;
							}
							if(appropriateFrozenAmount == null)
							{
								appropriateFrozenAmount = 0.0;
							}
							allocableAmount +=appliedAmount; //恢复可划拨金额
							appliedNoPayoutAmount -= appliedAmount;//恢复已申请未拨付金额
							appropriateFrozenAmount -= appliedAmount;//恢复拨付冻结金额

							tgpj_buildingAccount.setAllocableAmount(allocableAmount);
							tgpj_buildingAccount.setAppliedNoPayoutAmount(appliedNoPayoutAmount);
							tgpj_buildingAccount.setAppropriateFrozenAmount(appropriateFrozenAmount);
							tgpj_buildingAccountDao.save(tgpj_buildingAccount);
						}
					}
				}
			}

			// 不通过操作
			if(S_ApprovalState.NoPass.equals(sm_ApprovalProcess_AF.getBusiState()))
			{
				tgpf_fundAppropriated_af.setApplyState(S_ApplyState.Rescinded);  // 已撤销
				tgpf_fundAppropriated_af.setApprovalState(S_ApprovalState.NoPass); // 不通过
				tgpf_fundAppropriated_af.setTheState(S_TheState.Deleted); // 逻辑删除
				for (Tgpf_FundAppropriated_AFDtl tgpf_fundAppropriated_afDtl : tgpf_fundAppropriated_af.getFundAppropriated_AFDtlList())
				{
					tgpf_fundAppropriated_afDtl.setTheState(S_TheState.Deleted);
				}
			}

			switch (approvalProcessWork)
			{
				case "06120301001_ZS":
					if(S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState()) && S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
					{
						//新增支付限制业务  20210914
						for (Tgpf_FundAppropriated_AFDtl tgpf_fundAppropriated_afDtl : tgpf_fundAppropriated_af.getFundAppropriated_AFDtlList()) {
							if (tgpf_fundAppropriated_afDtl.getBuilding() != null) {
								Empj_BuildingInfo empj_buildingInfo = tgpf_fundAppropriated_afDtl.getBuilding();
//								System.out.println("paystate="+empj_buildingInfo.getPayState());
								if ("1".equals(empj_buildingInfo.getPayState())) {
//									System.out.println("----------------"+empj_buildingInfo.getTheNameFromPresellSystem());
//									System.out.println("----"+empj_buildingInfo.geteCodeFromConstruction()+"该审批单中有不允许支付的楼栋");
									return MyBackInfo.fail(properties, empj_buildingInfo.getTheNameFromPresellSystem()
											+ empj_buildingInfo.geteCodeFromConstruction()+"为不允许支付的楼栋");
//									return MyBackInfo.fail(properties, "请维护预约时间A！");
								}
							}
						}

						tgpf_fundAppropriated_af.setApplyState(S_ApplyState.Admissible); //用款申请单状态
						tgpf_fundAppropriated_af.setApprovalState(S_ApprovalState.Completed);//流程状态
						if(approvalProcessWorkflow.getUserUpdate()!=null)
						{
							tgpf_fundAppropriated_af.setUserRecord(approvalProcessWorkflow.getUserUpdate());//备案人
							tgpf_fundAppropriated_af.setRecordTimeStamp(System.currentTimeMillis());//备案日期
						}
						tgpf_fundAppropriated_afDao.save(tgpf_fundAppropriated_af);
						
						
						/*for(Tgpf_FundAppropriated_AFDtl tgpf_FundAppropriated_AFDtl : tgpf_fundAppropriated_af.getFundAppropriated_AFDtlList())
						{
							//反写到Tgpf_RetainedRightsView拨付留存权益
							Tgpf_BuildingRemainRightLogForm tgpf_BuildingRemainRightLogForm = new Tgpf_BuildingRemainRightLogForm();
							tgpf_BuildingRemainRightLogForm.setBuildingId(tgpf_FundAppropriated_AFDtl.getBuilding().getTableId());
							tgpf_BuildingRemainRightLogForm.setSrcBusiType("业务触发");
							tgpf_BuildingRemainRightLogForm.setBillTimeStamp(MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));

							//生成楼幢留存权益，同时生成每个楼幢下的户留存权益
							mqConnectionUtil.sendMessage(MQKey_EventType.APPROPRIATION_RETAINED, MQKey_OrgType.SYSTEM, tgpf_BuildingRemainRightLogForm);
//							tgpf_BuildingRemainRightLogPublicAddService.execute(tgpf_BuildingRemainRightLogForm);
						}*/
						
					}break;
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
