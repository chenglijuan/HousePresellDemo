package zhishusz.housepresell.approvalprocess;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.database.dao.Tg_DepositManagementDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Tg_DepositManagement;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_DepositPropertyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;

@Transactional
public class ApprovalProcessCallBack_210104 implements IApprovalProcessCallback
{
	@Autowired
	private Tg_DepositManagementDao tg_DepositManagementDao;
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;

	@Override
	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm model)
	{
		Properties properties = new Properties();

		try
		{
			String workflowEcode = approvalProcessWorkflow.geteCode();

			// 获取正在处理的申请单
			Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();

			// 获取正在处理的申请单所属的流程配置
			Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = sm_ApprovalProcess_AF.getConfiguration();
			String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;

			// 获取正在审批的存单
			Long depositManagementId = sm_ApprovalProcess_AF.getSourceId();
			if (depositManagementId == null || depositManagementId < 1)
			{
				return MyBackInfo.fail(properties, "审批的存单不存在");
			}
			Tg_DepositManagement tg_DepositManagement = tg_DepositManagementDao.findById(depositManagementId);

			if (tg_DepositManagement == null)
			{
				return MyBackInfo.fail(properties, "审批的存单不存在");
			}

			switch (approvalProcessWork)
			{
			case "210104001_ZS":
				if (S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState())
						&& S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
				{
					String jsonStr = sm_ApprovalProcess_AF.getExpectObjJson();
					if (jsonStr != null && jsonStr.length() > 0)
					{

						/*
						 * xsz by time 2019-2-27 20:30:40
						 * 审批通过后更新托管账户字段：
						 * currentBalance ： 活期余额减少
						 * 
						 * 
						 */
						Double principalAmount = tg_DepositManagement.getPrincipalAmount();// 本金金额
						String depositProperty = tg_DepositManagement.getDepositProperty();// 存款性质
						Tgxy_BankAccountEscrowed escrowAcount = tg_DepositManagement.getEscrowAcount();
						if (S_DepositPropertyType.CertificateOfDeposit.equals(depositProperty))
						{// 大额存单
							escrowAcount.setCertOfDeposit(
									(null == escrowAcount.getCertOfDeposit() ? 0 : escrowAcount.getCertOfDeposit())
											+ principalAmount);
						}
						else if (S_DepositPropertyType.StructuralDeposits.equals(depositProperty))
						{// 结构性存款
							escrowAcount.setStructuredDeposit((null == escrowAcount.getStructuredDeposit() ? 0
									: escrowAcount.getStructuredDeposit()) + principalAmount);
						}
						else if (S_DepositPropertyType.GuaranteedFund.equals(depositProperty))
						{// 保本理财
							escrowAcount.setBreakEvenFinancial((null == escrowAcount.getBreakEvenFinancial() ? 0
									: escrowAcount.getBreakEvenFinancial()) + principalAmount);
						}

						if(escrowAcount.getHasClosing().equals(0)){
							escrowAcount.setCurrentBalance(
									(null == escrowAcount.getCurrentBalance() ? 0 : escrowAcount.getCurrentBalance())
											- principalAmount);
						}
						
						

						/*
						 * 大额占比 大额存单/托管收入
						 * 
						 * 大额+活期占比 （大额存单+活期余额）/托管收入
						 * 
						 * 理财占比 （结构性存款+保本理财）/托管收入
						 * 
						 * 总资金沉淀占比 （大额存单+结构性存款+保本理财+活期余额）/托管收入
						 */
						if(escrowAcount.getIncome()==null){
							return MyBackInfo.fail(properties, "托管收入为0");
						}
						
						// 大额占比 = 大额存单/托管收入
						Double a = MyDouble.getInstance().div(escrowAcount.getCertOfDeposit(),escrowAcount.getIncome(), 4);
//						Double b = MyDouble.getInstance().doubleMultiplyDouble(a, 100.00);
//						Double c = MyDouble.getInstance().getShort(a, 4);
						escrowAcount.setLargeRatio(a);

						// 大额+活期占比 = (大额存单+活期)/托管收入
						Double a1 = MyDouble.getInstance().doubleAddDouble(escrowAcount.getCertOfDeposit(), escrowAcount.getCurrentBalance());
						Double b1 = MyDouble.getInstance().div(a1, escrowAcount.getIncome(), 4);
//						Double c1 = MyDouble.getInstance().doubleMultiplyDouble(b1, 100.00);
//						Double d1 = MyDouble.getInstance().getShort(b1, 4);
						escrowAcount.setLargeAndCurrentRatio(b1);

						// 理财占比 = (结构性存款+保本理财)/托管收入
						Double a2 = MyDouble.getInstance().doubleAddDouble(escrowAcount.getStructuredDeposit(), escrowAcount.getBreakEvenFinancial());
						Double b2 = MyDouble.getInstance().div(a2, escrowAcount.getIncome(), 4);
//						Double c2 = MyDouble.getInstance().doubleMultiplyDouble(b2, 100.00);
//						Double d2 = MyDouble.getInstance().getShort(b2, 4);
						escrowAcount.setFinancialRatio(b2);

						// 总资金沉淀占比 = (大额存单+活期+结构性存款+保本理财)/托管收入
						Double a3 = MyDouble.getInstance().doubleAddDouble(a1, a2);
						Double b3 = MyDouble.getInstance().div(a3, escrowAcount.getIncome(), 4);
//						Double c3 = MyDouble.getInstance().doubleMultiplyDouble(b3, 100.00);
//						Double d3 = MyDouble.getInstance().getShort(b3, 4);
						escrowAcount.setTotalFundsRatio(b3);
						
						tgxy_BankAccountEscrowedDao.save(escrowAcount);
						
						/*@Getter @Setter @IFieldAnnotation(remark="大额占比")
						private Double largeRatio;
						
						@Getter @Setter @IFieldAnnotation(remark="大额+活期占比")
						private Double largeAndCurrentRatio;
						
						@Getter @Setter @IFieldAnnotation(remark="理财占比")
						private Double financialRatio;
						
						@Getter @Setter @IFieldAnnotation(remark="总资金沉淀占比")
						private Double totalFundsRatio;*/

						/*
						 * @Getter @Setter @IFieldAnnotation(remark="大额存单")
						 * private Double certOfDeposit;
						 * 
						 * @Getter @Setter @IFieldAnnotation(remark="结构性存款")
						 * private Double structuredDeposit;
						 * 
						 * @Getter @Setter @IFieldAnnotation(remark="保本理财")
						 * private Double breakEvenFinancial;
						 */

						tg_DepositManagement.setBusiState("已备案");
						tg_DepositManagement.setApprovalState("已完结");
						tg_DepositManagementDao.save(tg_DepositManagement);
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
