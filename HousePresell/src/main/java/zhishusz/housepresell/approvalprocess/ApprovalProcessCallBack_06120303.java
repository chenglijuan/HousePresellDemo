package zhishusz.housepresell.approvalprocess;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriatedDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_ApplyState;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_PayoutState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/**
 * 资金拨付：
 * 审批过后-业务逻辑处理
 */
@Transactional
public class ApprovalProcessCallBack_06120303 implements IApprovalProcessCallback
{
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_fundAppropriated_afDao;
	@Autowired
	private Tgpf_FundAppropriatedDao tgpf_FundAppropriatedDao;
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_buildingAccountDao;
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_bankAccountEscrowedDao;

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

			//不通过操作
			if(S_ApprovalState.NoPass.equals(sm_ApprovalProcess_AF.getBusiState()))
			{
				for (Tgpf_FundAppropriated tgpf_fundAppropriated : tgpf_fundAppropriated_af.getFundAppropriatedList())
				{
					tgpf_fundAppropriated.setApprovalState(S_ApprovalState.WaitSubmit);
				}
				tgpf_fundAppropriated_af.setApplyState(S_ApplyState.Alreadycoordinated); //已统筹
				tgpf_fundAppropriated_afDao.save(tgpf_fundAppropriated_af);
			}

			// 我发起的撤回 ， 驳回到发起人
			if(S_ApprovalState.WaitSubmit.equals(sm_ApprovalProcess_AF.getBusiState()))
			{
				for (Tgpf_FundAppropriated tgpf_fundAppropriated : tgpf_fundAppropriated_af.getFundAppropriatedList())
				{
					tgpf_fundAppropriated.setApprovalState(S_ApprovalState.WaitSubmit);
				}
				tgpf_fundAppropriated_afDao.save(tgpf_fundAppropriated_af);
			}

			switch (approvalProcessWork)
			{
				case "06120303001_ZS":
					if(S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState()) && S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
					{
						tgpf_fundAppropriated_af.setApplyState(S_ApplyState.Alreadydisbursed); // 申请单状态 ： 已拨付

						/**
						 * 数据更新：
						 * 出纳完成一般拨付后，更新<托管账号资金情况表>-增加“托管支出”、减少“活期余额”。
						 * 出纳完成一般拨付后，更新<楼幢账户>-增加“已拨付金额（元）”、减少“已申请未拨付金额（元）”、减少“当前托管余额（元）”、减少“溢出资金（元）”。
						 */
						for (Tgpf_FundAppropriated tgpf_fundAppropriated : tgpf_fundAppropriated_af.getFundAppropriatedList())
						{
							tgpf_fundAppropriated.setApprovalState(S_ApprovalState.Completed);
							tgpf_fundAppropriated.setBusiState(String.valueOf(S_PayoutState.HaveAppropriated)); //业务状态 ： 已拨付
							if(approvalProcessWorkflow.getUserUpdate()!=null)
							{
								tgpf_fundAppropriated.setUserRecord(approvalProcessWorkflow.getUserUpdate());
								tgpf_fundAppropriated.setRecordTimeStamp(System.currentTimeMillis());
							}

							Double overallPlanPayoutAmount = tgpf_fundAppropriated.getOverallPlanPayoutAmount(); //统筹拨付金额
							/**
							 * 更新<托管账号资金情况表> 增加“托管支出”、减少“活期余额”。减少“托管可拨付”
							 */
							if(tgpf_fundAppropriated.getBankAccountEscrowed() != null)
							{
								Tgxy_BankAccountEscrowed bankAccountEscrowed = tgpf_fundAppropriated.getBankAccountEscrowed(); // 托管账户
								Double payOut = bankAccountEscrowed.getPayout(); //托管支出
								Double currentBalance = bankAccountEscrowed.getCurrentBalance();//活期余额
								
								Double canPayAmount = bankAccountEscrowed.getCanPayAmount();//托管可拨付
								if(payOut==null)
								{
									payOut = 0.0;
								}
								if(currentBalance==null)
								{
									currentBalance = 0.0;
								}
								
								if(null == canPayAmount)
								{
									canPayAmount = 0.00;
								}
									
								payOut += overallPlanPayoutAmount;//托管支出
								currentBalance -=overallPlanPayoutAmount;//活期余额
								canPayAmount -=overallPlanPayoutAmount;//托管可拨付
								if(canPayAmount < 0)
								{
									canPayAmount = 0.00;
								}
								
								
								bankAccountEscrowed.setPayout(payOut); //增加“托管支出”
								bankAccountEscrowed.setCurrentBalance(currentBalance);//减少“活期余额”
								bankAccountEscrowed.setCanPayAmount(canPayAmount);//减少“托管可拨付”
								
								tgxy_bankAccountEscrowedDao.save(bankAccountEscrowed);
							}
							tgpf_FundAppropriatedDao.save(tgpf_fundAppropriated);
						}

						/**
						 * 更新<楼幢账户> 更新<楼幢账户>-增加“已拨付金额（元）”、减少“已申请未拨付金额（元）”、减少“当前托管余额（元）”、减少“溢出资金（元）”。
						 */
						for (Tgpf_FundAppropriated_AFDtl tgpf_fundAppropriated_afDtl : tgpf_fundAppropriated_af.getFundAppropriated_AFDtlList())
						{
							/**
							 * 状态更新：
							 * 资金拨付后需要更新的状态：
							 * 1)用款申请子表“拨付状态”：2-已拨付
							 */
							tgpf_fundAppropriated_afDtl.setPayoutState(S_PayoutState.HaveAppropriated);
							Double appliedAmount = tgpf_fundAppropriated_afDtl.getAppliedAmount(); //本次划款申请金额
							if(tgpf_fundAppropriated_afDtl.getBuilding() != null)
							{
								Empj_BuildingInfo buildingInfo = tgpf_fundAppropriated_afDtl.getBuilding();

								if(buildingInfo.getBuildingAccount() !=null)
								{
									Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();

									Double payoutAmount = buildingAccount.getPayoutAmount(); //已拨付金额
									Double appliedNoPayoutAmount = buildingAccount.getAppliedNoPayoutAmount();//已申请未拨付金额
									Double appropriateFrozenAmount = buildingAccount.getAppropriateFrozenAmount(); //拨付冻结金额
									Double currentEscrowFund = buildingAccount.getCurrentEscrowFund();//当前托管余额
									Double spilloverAmount = buildingAccount.getSpilloverAmount();//溢出金额
									if(payoutAmount == null)
									{
										payoutAmount = 0.0;
									}
									if(appliedNoPayoutAmount == null)
									{
										appliedNoPayoutAmount = 0.0;
									}
									if(appropriateFrozenAmount == null)
									{
										appropriateFrozenAmount = 0.0;
									}
									if(currentEscrowFund == null)
									{
										currentEscrowFund = 0.0;
									}
									if(spilloverAmount == null)
									{
										spilloverAmount = 0.0;
									}

									payoutAmount += appliedAmount;//增加已拨付金额（元）
									appliedNoPayoutAmount -= appliedAmount;//减少已申请未拨付金额（元）
									appropriateFrozenAmount -= appliedAmount;//减少拨付冻结金额
									currentEscrowFund -= appliedAmount;//减少当前托管余额（元）
									spilloverAmount -= appliedAmount;//减少溢出金额（元）

									buildingAccount.setPayoutAmount(payoutAmount);
									buildingAccount.setAppliedNoPayoutAmount(appliedNoPayoutAmount);
									buildingAccount.setAppropriateFrozenAmount(appropriateFrozenAmount);
									buildingAccount.setCurrentEscrowFund(currentEscrowFund);
									buildingAccount.setSpilloverAmount(spilloverAmount);
									tgpj_buildingAccountDao.save(buildingAccount);
								}
							}

						}
						tgpf_fundAppropriated_afDao.save(tgpf_fundAppropriated_af);
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
