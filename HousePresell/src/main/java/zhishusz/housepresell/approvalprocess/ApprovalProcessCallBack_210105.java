package zhishusz.housepresell.approvalprocess;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Tg_DepositManagementForm;
import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.dao.Tg_DepositManagementDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Tg_DepositManagement;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_DepositPropertyType;
import zhishusz.housepresell.database.po.state.S_DepositState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.service.Sm_AttachmentBatchAddService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;

@Transactional
public class ApprovalProcessCallBack_210105 implements IApprovalProcessCallback
{
	@Autowired
	private Tg_DepositManagementDao tg_DepositManagementDao;

	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
	@Autowired
	private Gson gson;
	@Autowired
	private Tgxy_BankAccountEscrowedDao  tgxy_BankAccountEscrowedDao;

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
			if(depositManagementId == null || depositManagementId < 1)
			{
				return MyBackInfo.fail(properties, "审批的存单不存在");
			}
			Tg_DepositManagement tg_DepositManagement =  tg_DepositManagementDao.findById(depositManagementId);

			if(tg_DepositManagement == null)
			{
				return MyBackInfo.fail(properties, "审批的存单不存在");
			}

			switch (approvalProcessWork)
			{
			case "210105001_ZS":
				if(S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState()) && S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
				{
					String jsonStr = sm_ApprovalProcess_AF.getExpectObjJson();
					if (jsonStr != null && jsonStr.length() > 0)
					{

						/*Tg_DepositManagementForm tg_DepositManagementForm = gson.fromJson(jsonStr, Tg_DepositManagementForm.class);

						tg_DepositManagement.setRealInterest(tg_DepositManagementForm.getRealInterest());
						tg_DepositManagement.setRealInterestRate(tg_DepositManagementForm.getRealInterestRate());
						if(!tg_DepositManagementForm.getExtractDateStr().equals("")){
							tg_DepositManagement.setExtractDate(MyDatetime.getInstance().stringToLong(tg_DepositManagementForm.getExtractDateStr()));
						}*/
						tg_DepositManagement.setBusiState("已备案");
						tg_DepositManagement.setApprovalState("已完结");
						tg_DepositManagement.setDepositState(S_DepositState.TakeOut);

						tg_DepositManagementDao.save(tg_DepositManagement);
						
						/*
						 * xsz by time 2019-2-27 20:30:40
						 * 审批通过后更新托管账户字段：
						 * currentBalance  ： 活期余额减少
						 * 
						 * 
						 */
						
						
						Double principalAmount = tg_DepositManagement.getPrincipalAmount();// 本金金额
						String depositProperty = tg_DepositManagement.getDepositProperty();// 存款性质
						Tgxy_BankAccountEscrowed escrowAcount = tg_DepositManagement.getEscrowAcount();
						
						//zbp 20200909 11:33 审批通过后 判断是否销户 
						//增加转出托管账号的“转出金额”，增加转入托管账号的“托管余额”、“活期余额”、“转入金额”。
						Integer hasClosing =escrowAcount.getHasClosing()==null?0:escrowAcount.getHasClosing();
						if(1 == hasClosing){
							//更新原账户
							//原账户转出金额=转出金额+本金金额
							escrowAcount.setTransferOutAmount(escrowAcount.getTransferOutAmount()==null?0.00:escrowAcount.getTransferOutAmount()+principalAmount);
							//原账户可拨付金额（余额）=可拨付金额-本金金额
							escrowAcount.setCanPayAmount(escrowAcount.getCanPayAmount()==null?0.00:escrowAcount.getCanPayAmount()-principalAmount);
							//原账户存款金额=存款金额-本金金额
							if (S_DepositPropertyType.CertificateOfDeposit.equals(depositProperty))
							{// 大额存单
								escrowAcount.setCertOfDeposit((null==escrowAcount.getCertOfDeposit()?0:escrowAcount.getCertOfDeposit())-principalAmount);
							}
							else if (S_DepositPropertyType.StructuralDeposits.equals(depositProperty))
							{// 结构性存款
								escrowAcount.setStructuredDeposit((null==escrowAcount.getStructuredDeposit()?0:escrowAcount.getStructuredDeposit())-principalAmount);
							}
							else if (S_DepositPropertyType.GuaranteedFund.equals(depositProperty))
							{// 保本理财
								escrowAcount.setBreakEvenFinancial((null==escrowAcount.getBreakEvenFinancial()?0:escrowAcount.getBreakEvenFinancial())-principalAmount);
							}
							// 大额占比 = 大额存单/托管收入
							Double aa = MyDouble.getInstance().div(escrowAcount.getCertOfDeposit(), escrowAcount.getIncome(), 4);
							escrowAcount.setLargeRatio(aa);

							// 大额+活期占比 = (大额存单+活期)/托管收入
							Double aa1 = MyDouble.getInstance().doubleAddDouble(escrowAcount.getCertOfDeposit(), escrowAcount.getCurrentBalance());
							Double bb1 = MyDouble.getInstance().div(aa1, escrowAcount.getIncome(), 4);
							escrowAcount.setLargeAndCurrentRatio(bb1);

							// 理财占比 = (结构性存款+保本理财)/托管收入
							Double aa2 = MyDouble.getInstance().doubleAddDouble(escrowAcount.getStructuredDeposit(), escrowAcount.getBreakEvenFinancial());
							Double bb2 = MyDouble.getInstance().div(aa2, escrowAcount.getIncome(), 4);
							escrowAcount.setFinancialRatio(bb2);

							// 总资金沉淀占比 = (大额存单+活期+结构性存款+保本理财)/托管收入
							Double aa3 = MyDouble.getInstance().doubleAddDouble(aa1, aa2);
							Double bb3 = MyDouble.getInstance().div(aa3, escrowAcount.getIncome(), 4);
							escrowAcount.setTotalFundsRatio(bb3);
							tgxy_BankAccountEscrowedDao.update(escrowAcount);
							
							//更新新账户
							Tgxy_BankAccountEscrowedForm model1 = new Tgxy_BankAccountEscrowedForm();
							String eCode=escrowAcount.getToECode();
							model1.seteCode(eCode);
							model1.setTheState(0);
							List<Tgxy_BankAccountEscrowed> tgxy_BankAccountEscrowedList = tgxy_BankAccountEscrowedDao.findByPage(tgxy_BankAccountEscrowedDao.getQuery(tgxy_BankAccountEscrowedDao.getBasicHQL(), model1));
							if(tgxy_BankAccountEscrowedList.size()==0 || tgxy_BankAccountEscrowedList==null){
								 return MyBackInfo.fail(properties, "未查询到转入账户");
							}
							Long tableId = tgxy_BankAccountEscrowedList.get(0).getTableId();
							Tgxy_BankAccountEscrowed bankAccountEscrowed = tgxy_BankAccountEscrowedDao.findById(tableId);
							//转入=转入+本金
							bankAccountEscrowed.setTransferInAmount(bankAccountEscrowed.getTransferInAmount()==null?0.00:bankAccountEscrowed.getTransferInAmount()+principalAmount);
							//活期=活期+本金
							bankAccountEscrowed.setCurrentBalance(bankAccountEscrowed.getCurrentBalance()==null?0.00:bankAccountEscrowed.getCurrentBalance()+principalAmount);
							//可拨付金额（余额）=可拨付+本金
							bankAccountEscrowed.setCanPayAmount(bankAccountEscrowed.getCanPayAmount()==null?0.00:bankAccountEscrowed.getCanPayAmount()+principalAmount);
							
							/*if (S_DepositPropertyType.CertificateOfDeposit.equals(depositProperty))
							{// 大额存单
								bankAccountEscrowed.setCertOfDeposit((null==bankAccountEscrowed.getCertOfDeposit()?0:bankAccountEscrowed.getCertOfDeposit())-principalAmount);
							}
							else if (S_DepositPropertyType.StructuralDeposits.equals(depositProperty))
							{// 结构性存款
								bankAccountEscrowed.setStructuredDeposit((null==bankAccountEscrowed.getStructuredDeposit()?0:bankAccountEscrowed.getStructuredDeposit())-principalAmount);
							}
							else if (S_DepositPropertyType.GuaranteedFund.equals(depositProperty))
							{// 保本理财
								bankAccountEscrowed.setBreakEvenFinancial((null==bankAccountEscrowed.getBreakEvenFinancial()?0:bankAccountEscrowed.getBreakEvenFinancial())-principalAmount);
							}*/
							
							/*
							 * 大额占比 大额存单/托管收入
							 * 
							 * 大额+活期占比 （大额存单+活期余额）/托管收入
							 * 
							 * 理财占比 （结构性存款+保本理财）/托管收入
							 * 
							 * 总资金沉淀占比 （大额存单+结构性存款+保本理财+活期余额）/托管收入
							 */
							
							/*// 大额占比 = 大额存单/托管收入
							Double a = MyDouble.getInstance().div(bankAccountEscrowed.getCertOfDeposit(), bankAccountEscrowed.getIncome(), 4);
							bankAccountEscrowed.setLargeRatio(a);

							// 大额+活期占比 = (大额存单+活期)/托管收入
							Double a1 = MyDouble.getInstance().doubleAddDouble(bankAccountEscrowed.getCertOfDeposit(), bankAccountEscrowed.getCurrentBalance());
							Double b1 = MyDouble.getInstance().div(a1, bankAccountEscrowed.getIncome(), 4);
							bankAccountEscrowed.setLargeAndCurrentRatio(b1);

							// 理财占比 = (结构性存款+保本理财)/托管收入
							Double a2 = MyDouble.getInstance().doubleAddDouble(bankAccountEscrowed.getStructuredDeposit(), bankAccountEscrowed.getBreakEvenFinancial());
							Double b2 = MyDouble.getInstance().div(a2, bankAccountEscrowed.getIncome(), 4);
							bankAccountEscrowed.setFinancialRatio(b2);

							// 总资金沉淀占比 = (大额存单+活期+结构性存款+保本理财)/托管收入
							Double a3 = MyDouble.getInstance().doubleAddDouble(a1, a2);
							Double b3 = MyDouble.getInstance().div(a3, bankAccountEscrowed.getIncome(), 4);
							bankAccountEscrowed.setTotalFundsRatio(b3);
							*/
							
							tgxy_BankAccountEscrowedDao.update(bankAccountEscrowed);
						}else{
							if (S_DepositPropertyType.CertificateOfDeposit.equals(depositProperty))
							{// 大额存单
								escrowAcount.setCertOfDeposit((null==escrowAcount.getCertOfDeposit()?0:escrowAcount.getCertOfDeposit())-principalAmount);
							}
							else if (S_DepositPropertyType.StructuralDeposits.equals(depositProperty))
							{// 结构性存款
								escrowAcount.setStructuredDeposit((null==escrowAcount.getStructuredDeposit()?0:escrowAcount.getStructuredDeposit())-principalAmount);
							}
							else if (S_DepositPropertyType.GuaranteedFund.equals(depositProperty))
							{// 保本理财
								escrowAcount.setBreakEvenFinancial((null==escrowAcount.getBreakEvenFinancial()?0:escrowAcount.getBreakEvenFinancial())-principalAmount);
							}
							
							escrowAcount.setCurrentBalance((null==escrowAcount.getCurrentBalance()?0:escrowAcount.getCurrentBalance())+principalAmount);
							
							/*
							 * 大额占比 大额存单/托管收入
							 * 
							 * 大额+活期占比 （大额存单+活期余额）/托管收入
							 * 
							 * 理财占比 （结构性存款+保本理财）/托管收入
							 * 
							 * 总资金沉淀占比 （大额存单+结构性存款+保本理财+活期余额）/托管收入
							 */
							
							// 大额占比 = 大额存单/托管收入
							Double a = MyDouble.getInstance().div(escrowAcount.getCertOfDeposit(), escrowAcount.getIncome(), 4);
//							Double b = MyDouble.getInstance().doubleMultiplyDouble(a, 100.00);
//							Double c = MyDouble.getInstance().getShort(a, 2);
							escrowAcount.setLargeRatio(a);

							// 大额+活期占比 = (大额存单+活期)/托管收入
							Double a1 = MyDouble.getInstance().doubleAddDouble(escrowAcount.getCertOfDeposit(), escrowAcount.getCurrentBalance());
							Double b1 = MyDouble.getInstance().div(a1, escrowAcount.getIncome(), 4);
//							Double c1 = MyDouble.getInstance().doubleMultiplyDouble(b1, 100.00);
//							Double d1 = MyDouble.getInstance().getShort(b1, 2);
							escrowAcount.setLargeAndCurrentRatio(b1);

							// 理财占比 = (结构性存款+保本理财)/托管收入
							Double a2 = MyDouble.getInstance().doubleAddDouble(escrowAcount.getStructuredDeposit(), escrowAcount.getBreakEvenFinancial());
							Double b2 = MyDouble.getInstance().div(a2, escrowAcount.getIncome(), 4);
//							Double c2 = MyDouble.getInstance().doubleMultiplyDouble(b2, 100.00);
//							Double d2 = MyDouble.getInstance().getShort(b2, 2);
							escrowAcount.setFinancialRatio(b2);

							// 总资金沉淀占比 = (大额存单+活期+结构性存款+保本理财)/托管收入
							Double a3 = MyDouble.getInstance().doubleAddDouble(a1, a2);
							Double b3 = MyDouble.getInstance().div(a3, escrowAcount.getIncome(), 4);
//							Double c3 = MyDouble.getInstance().doubleMultiplyDouble(b3, 100.00);
//							Double d3 = MyDouble.getInstance().getShort(b3, 2);
							escrowAcount.setTotalFundsRatio(b3);
							
							tgxy_BankAccountEscrowedDao.update(escrowAcount);
						}
						/*@Getter @Setter @IFieldAnnotation(remark="大额存单")
						private Double certOfDeposit;
						
						@Getter @Setter @IFieldAnnotation(remark="结构性存款")
						private Double structuredDeposit;
						
						@Getter @Setter @IFieldAnnotation(remark="保本理财")
						private Double breakEvenFinancial;*/

						//更新附件信息
						/*tg_DepositManagementForm.setBusiType("210104");
						tg_DepositManagementForm.setUserId(sm_ApprovalProcess_AF.getUserStart().getTableId());
						sm_AttachmentBatchAddService.execute(tg_DepositManagementForm, depositManagementId);*/

					}
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
