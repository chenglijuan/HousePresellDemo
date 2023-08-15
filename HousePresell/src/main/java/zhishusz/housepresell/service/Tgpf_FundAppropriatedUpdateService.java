package zhishusz.housepresell.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_BuildingRemainRightLogForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriatedForm;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriatedDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_ApplyState;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_ButtonType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_PayoutState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：资金拨付
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundAppropriatedUpdateService
{
	@Autowired
	private Tgpf_FundAppropriatedDao tgpf_FundAppropriatedDao;
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_buildingAccountDao;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_bankAccountEscrowedDao;
	@Autowired
	private Tgpf_BuildingRemainRightLogPublicAddService tgpf_BuildingRemainRightLogPublicAdd;// 留存权益计算方法

	MyDatetime myDatetime = MyDatetime.getInstance();
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_FundAppropriatedForm model)
	{
		Properties properties = new MyProperties();

		Map<Long,Double> buildingMap = new HashMap<Long,Double>();
		
		String busiCode = S_BusiCode.busiCode9; //业务编码
		String buttonType = model.getButtonType();
		Long loginUserId = model.getUserId();
		String remark = model.getRemark();
		if(loginUserId == null || loginUserId < 1)
		{
			return MyBackInfo.fail(properties, "'登录用户'不能为空");
		}
		Sm_User loginUser = model.getUser();

		// 发起人校验
		//1 如果该业务没有配置审批流程，直接保存
		//2 如果该业务配置了审批流程 ，判断用户能否与对应模块下的审批流程的发起人角色匹配
		properties = sm_ApprovalProcessGetService.execute(busiCode, loginUserId);
		if(! properties.getProperty("info").equals("noApproval") && properties.getProperty("result").equals("fail"))
		{
			return properties;
		}

		Long tgpf_FundAppropriated_AFId = model.getFundAppropriated_AFId();
		//用款申请主表信息
		Tgpf_FundAppropriated_AF tgpf_FundAppropriated_AF = (Tgpf_FundAppropriated_AF)tgpf_FundAppropriated_AFDao.findById(tgpf_FundAppropriated_AFId);
		if(tgpf_FundAppropriated_AF == null || S_TheState.Deleted.equals(tgpf_FundAppropriated_AF.getTheState()))
		{
			return MyBackInfo.fail(properties, "'用款申请主表(Id:" + tgpf_FundAppropriated_AFId + ")'不存在");
		}
		tgpf_FundAppropriated_AF.setRemark(remark);

		List<Tgpf_FundAppropriated> tgpf_fundAppropriatedList = new ArrayList<>();
		Tgpf_FundAppropriatedForm[] tgpf_FundAppropriatedList2 = model.getTgpf_FundAppropriatedList();
		if(null == tgpf_FundAppropriatedList2 || tgpf_FundAppropriatedList2.length == 0){
			for (Tgpf_FundAppropriated tgpf_fundAppropriatedForm : model.getFundAppropriatedList())
			{
				Long tableId = tgpf_fundAppropriatedForm.getTableId();
				String actualPayoutDate = tgpf_fundAppropriatedForm.getActualPayoutDate(); //实际拨款日期
				String eCodeFromPayoutBill = tgpf_fundAppropriatedForm.geteCodeFromPayoutBill(); // 拨付单据号
				if(tableId == null || tableId < 1)
				{
					return MyBackInfo.fail(properties, "'资金拨付信息'不能为空");
				}
				Tgpf_FundAppropriated tgpf_fundAppropriated = tgpf_FundAppropriatedDao.findById(tableId);
				if(tgpf_fundAppropriated == null)
				{
					return MyBackInfo.fail(properties, "'资金拨付信息'不能为空");
				}

				if(buttonType.equals(S_ButtonType.Save))
				{
					tgpf_fundAppropriated.setApprovalState(S_ApprovalState.WaitSubmit); // 资金拨付 =》 审批状态 ： 待提交
				}
				else if(buttonType.equals(S_ButtonType.Submit))
				{
					tgpf_fundAppropriated.setApprovalState(S_ApprovalState.Examining); // 资金拨付 =》 审批状态 ： 审核中
				}

				tgpf_fundAppropriated.setUserUpdate(loginUser); // 登录用户
				tgpf_fundAppropriated.setActualPayoutDate(actualPayoutDate);//实际拨款日期
				tgpf_fundAppropriated.seteCodeFromPayoutBill(eCodeFromPayoutBill);//打款单号
				tgpf_fundAppropriatedList.add(tgpf_fundAppropriated);
			}
		}else{
			for (Tgpf_FundAppropriatedForm tgpf_fundAppropriatedForm : model.getTgpf_FundAppropriatedList())
			{
				Long tableId = tgpf_fundAppropriatedForm.getTableId();
				String actualPayoutDate = tgpf_fundAppropriatedForm.getActualPayoutDate(); //实际拨款日期
				String eCodeFromPayoutBill = tgpf_fundAppropriatedForm.geteCodeFromPayoutBill(); // 拨付单据号
				if(tableId == null || tableId < 1)
				{
					return MyBackInfo.fail(properties, "'资金拨付信息'不能为空");
				}
				Tgpf_FundAppropriated tgpf_fundAppropriated = tgpf_FundAppropriatedDao.findById(tableId);
				if(tgpf_fundAppropriated == null)
				{
					return MyBackInfo.fail(properties, "'资金拨付信息'不能为空");
				}

				if(buttonType.equals(S_ButtonType.Save))
				{
					tgpf_fundAppropriated.setApprovalState(S_ApprovalState.WaitSubmit); // 资金拨付 =》 审批状态 ： 待提交
				}
				else if(buttonType.equals(S_ButtonType.Submit))
				{
					if(actualPayoutDate == null || actualPayoutDate.length() == 0)
					{
						return MyBackInfo.fail(properties, "'拨付时间'不能为空");
					}
					tgpf_fundAppropriated.setApprovalState(S_ApprovalState.Examining); // 资金拨付 =》 审批状态 ： 审核中
				}

				tgpf_fundAppropriated.setUserUpdate(loginUser); // 登录用户
				tgpf_fundAppropriated.setActualPayoutDate(actualPayoutDate);//实际拨款日期
				tgpf_fundAppropriated.seteCodeFromPayoutBill(eCodeFromPayoutBill);//打款单号
				tgpf_fundAppropriatedList.add(tgpf_fundAppropriated);
			}
		}
		
		
		if(properties.getProperty("info").equals("noApproval"))
		{
			if(S_ButtonType.Save.equals(buttonType))
			{
				tgpf_FundAppropriated_AF.setApplyState(S_ApplyState.Disbursement); // 申请单状态 ：拨付中
			}
			else if(S_ButtonType.Submit.equals(buttonType))
			{
				tgpf_FundAppropriated_AF.setApplyState(S_ApplyState.Alreadydisbursed); // 申请单状态 ： 已拨付

				/**
				 * 数据更新：
				 * 出纳完成一般拨付后，更新<托管账号资金情况表>-增加“托管支出”、减少“活期余额”。
				 * 出纳完成一般拨付后，更新<楼幢账户>-增加“已拨付金额（元）”、减少“已申请未拨付金额（元）”、减少“当前托管余额（元）”、减少“溢出资金（元）”。
				 */
				for (Tgpf_FundAppropriated tgpf_fundAppropriated : tgpf_fundAppropriatedList)
				{
					tgpf_fundAppropriated.setApprovalState(S_ApprovalState.Completed); // 审批状态 ： 已完结
					tgpf_fundAppropriated.setBusiState(String.valueOf(S_PayoutState.HaveAppropriated)); //业务状态 ： 已拨付
					tgpf_fundAppropriated.setUserRecord(loginUser);
					tgpf_fundAppropriated.setRecordTimeStamp(System.currentTimeMillis());

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
						
						/*
						 * 大额占比 大额存单/托管收入
						 * 
						 * 大额+活期占比 （大额存单+活期余额）/托管收入
						 * 
						 * 理财占比 （结构性存款+保本理财）/托管收入
						 * 
						 * 总资金沉淀占比 托管余额/托管收入
						 */
						
						if(null != bankAccountEscrowed.getIncome())
						{
							// 大额占比 = 大额存单/托管收入
							Double a = MyDouble.getInstance().div(null == bankAccountEscrowed.getCertOfDeposit()?0.00:bankAccountEscrowed.getCertOfDeposit(), bankAccountEscrowed.getIncome(), 4);
							bankAccountEscrowed.setLargeRatio(a);
		
							// 大额+活期占比 = (大额存单+活期)/托管收入
							Double a1 = MyDouble.getInstance().doubleAddDouble(null == bankAccountEscrowed.getCertOfDeposit()?0.00:bankAccountEscrowed.getCertOfDeposit(), null == bankAccountEscrowed.getCurrentBalance()?0.00:bankAccountEscrowed.getCurrentBalance());
							Double b1 = MyDouble.getInstance().div(a1, bankAccountEscrowed.getIncome(), 4);
							bankAccountEscrowed.setLargeAndCurrentRatio(b1);
		
							// 理财占比 = (结构性存款+保本理财)/托管收入
							Double a2 = MyDouble.getInstance().doubleAddDouble(null == bankAccountEscrowed.getStructuredDeposit()?0.00:bankAccountEscrowed.getStructuredDeposit(), null == bankAccountEscrowed.getBreakEvenFinancial()?0.00:bankAccountEscrowed.getBreakEvenFinancial());
							Double b2 = MyDouble.getInstance().div(a2, bankAccountEscrowed.getIncome(), 4);
							bankAccountEscrowed.setFinancialRatio(b2);
		
							// 总资金沉淀占比 = 托管余额/托管收入
		//						Double a3 = MyDouble.getInstance().doubleSubtractDouble(bankAccountEscrowed.getIncome(), payOut);
							Double b3 = MyDouble.getInstance().div(canPayAmount, bankAccountEscrowed.getIncome(), 4);
							bankAccountEscrowed.setTotalFundsRatio(b3);
						}
						
						tgxy_bankAccountEscrowedDao.save(bankAccountEscrowed);
					}
					tgpf_fundAppropriated.setApprovalState(S_ApprovalState.Completed); // 已完结
					tgpf_FundAppropriatedDao.save(tgpf_fundAppropriated);
				}

				/**
				 * 更新<楼幢账户> 更新<楼幢账户>-增加“已拨付金额（元）”、减少“已申请未拨付金额（元）”、减少“当前托管余额（元）”、减少“溢出资金（元）”。
				 */
				/**
				 * xsz by time 2019-7-6 11:34:09
				 * 更换更新方式为调用存储过程更新
				 * ==========start================
				 */
				/*for (Tgpf_FundAppropriated_AFDtl tgpf_fundAppropriated_afDtl : tgpf_FundAppropriated_AF.getFundAppropriated_AFDtlList())
				{
					*//**
					 * 状态更新：
					 * 资金拨付后需要更新的状态：
					 * 1)用款申请子表“拨付状态”：2-已拨付
					 *//*
					tgpf_fundAppropriated_afDtl.setPayoutState(S_PayoutState.HaveAppropriated);
					Double appliedAmount = tgpf_fundAppropriated_afDtl.getAppliedAmount(); //本次划款申请金额
					
					if(tgpf_fundAppropriated_afDtl.getBuilding() != null)
					{
						Empj_BuildingInfo buildingInfo = tgpf_fundAppropriated_afDtl.getBuilding();
						
						buildingMap.put(buildingInfo.getTableId(), 0.00);
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
				}*/
				
				try {
					Map<String, Object> map = tgpf_FundAppropriated_AFDao.update_FundAppropriated_Final(tgpf_FundAppropriated_AFId);
					
					String sign = (String) map.get("sign");
					if("fail".equals(sign))
					{
						return MyBackInfo.fail(properties, (String)map.get("info"));
					}
					
					buildingMap = (Map<Long, Double>) map.get("buildMap");
					
				} catch (SQLException e) {
					
					return MyBackInfo.fail(properties, e.getMessage());
					
				}
				
				
				/**
				 * xsz by time 2019-7-6 11:34:09
				 * 更换更新方式为调用存储过程更新
				 * ==========end================
				 */
				
				if(null != buildingMap)
				{
					// 更新楼幢信息
					Iterator<Long> buildingIt = buildingMap.keySet().iterator(); // map.keySet()得到的是set集合，可以使用迭代器遍历
					// 计算留存权益
					while (buildingIt.hasNext())
					{
						Long key = buildingIt.next();

						// 留存权益计算
						Tgpf_BuildingRemainRightLogForm tgpf_BuildingRemainRightLogForm = new Tgpf_BuildingRemainRightLogForm();
						tgpf_BuildingRemainRightLogForm.setBillTimeStamp(myDatetime.dateToSimpleString(System.currentTimeMillis()));
						tgpf_BuildingRemainRightLogForm.setBuildingId(key);
						tgpf_BuildingRemainRightLogForm.setSrcBusiType("业务触发");

						tgpf_BuildingRemainRightLogPublicAdd.execute(tgpf_BuildingRemainRightLogForm);
						
					}
				}
				
			}
		}
		else
		{
			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");
			//审批操作
			model.setIsSetApprovalState(false);//标识资金拨付的时候不再设置用户审批主表的审批状态
			sm_approvalProcessService.execute(tgpf_FundAppropriated_AF, model, sm_approvalProcess_cfg);
			tgpf_FundAppropriated_AF.setApplyState(S_ApplyState.Disbursement); // 申请单状态 ：拨付中
		}

		tgpf_FundAppropriated_AF.setFundAppropriatedList(tgpf_fundAppropriatedList);
		tgpf_FundAppropriated_AFDao.save(tgpf_FundAppropriated_AF);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
