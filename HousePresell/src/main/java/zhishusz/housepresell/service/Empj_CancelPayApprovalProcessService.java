package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeChildForm;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeForm;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeChildDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountLogDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Empj_PaymentGuaranteeChild;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * 
 * Service 审批流提交操作:支付申请撤销
 */
@Service
@Transactional
public class Empj_CancelPayApprovalProcessService
{
	@Autowired
	private Empj_PaymentGuaranteeDao empj_PaymentGuaranteeDao;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private Tgpj_BuildingAccountLogDao tgpj_BuildingAccountLogDao;
	@Autowired
	private Empj_PaymentGuaranteeChildDao empj_PaymentGuaranteeChildDao;
	@Autowired
	private Tgpj_BuildingAccountLimitedUpdateService tgpj_BuildingAccountLimitedUpdateService;
	
	MyDouble myDouble = MyDouble.getInstance();
	
	private static final String BUSI_CODE = "06120403";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	
	public Properties execute(Empj_PaymentGuaranteeForm model)
	{
		Properties properties = new MyProperties();
		
		model.setButtonType("2");//1:保存按钮 2:提交按钮
		String busiCode = model.getBusiCode();	
		Long tableId = model.getTableId();
		Long userStartId = model.getUserId(); //登录用户id
		
		List<Tgpj_BuildingAccountLog> buildingLogMap = new ArrayList<Tgpj_BuildingAccountLog>();
		
		Sm_User user = model.getUser();
		
		if(busiCode == null || busiCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'业务编码'不能为空");
		}

		if(tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "未查询到有效的信息!");
		}
		
		//查询支付申请撤销
		Empj_PaymentGuarantee empj_PaymentGuarantee =  empj_PaymentGuaranteeDao.findById(tableId);  
		if(empj_PaymentGuarantee == null || S_TheState.Deleted.equals(empj_PaymentGuarantee.getTheState()))
		{
			return MyBackInfo.fail(properties, "保证申请有误！");
		}
		if( "0".equals(empj_PaymentGuarantee.getBusiState()))
		{
			return MyBackInfo.fail(properties, "请提交支付申请审核！");
		}
		if( "1".equals(empj_PaymentGuarantee.getBusiState()))
		{
			return MyBackInfo.fail(properties, "该申请正再通过审核，不能进行撤销操作！");
		}
		if( "3".equals(empj_PaymentGuarantee.getBusiState()))
		{
			return MyBackInfo.fail(properties, "该撤销申请正在申请审核！");
		}
		if( "4".equals(empj_PaymentGuarantee.getBusiState()))
		{
			return MyBackInfo.fail(properties, "该撤销申请已通过审核！");
		}
		
//		if (S_ApprovalState.Examining.equals(empj_PaymentGuarantee.getApprovalState()))
//		{
//			return MyBackInfo.fail(properties, "该支付申请已在审核中，不可重复提交");
//		}
//		else if (S_ApprovalState.Completed.equals(empj_PaymentGuarantee.getApprovalState()))
//		{
//			return MyBackInfo.fail(properties, "该支付申请已审批完成，不可重复提交");
//		}
		
		
		empj_PaymentGuarantee.setBusiState("3"); // 申请撤销中
		empj_PaymentGuaranteeDao.save(empj_PaymentGuarantee);
		
		// 查询保存的需要备案的楼幢
		Empj_PaymentGuaranteeChildForm empj_PaymentGuaranteeChildForm = new Empj_PaymentGuaranteeChildForm();
		empj_PaymentGuaranteeChildForm.setEmpj_PaymentGuarantee(empj_PaymentGuarantee);
		empj_PaymentGuaranteeChildForm.setTheState(S_TheState.Normal);

		Integer childCount = empj_PaymentGuaranteeChildDao.findByPage_Size(empj_PaymentGuaranteeChildDao
				.getQuery_Size(empj_PaymentGuaranteeChildDao.getBasicHQL(), empj_PaymentGuaranteeChildForm));

		List<Empj_PaymentGuaranteeChild> empj_PaymentGuaranteeChildList = new ArrayList<Empj_PaymentGuaranteeChild>();
		if (childCount > 0)
		{
			empj_PaymentGuaranteeChildList = empj_PaymentGuaranteeChildDao.findByPage(empj_PaymentGuaranteeChildDao
					.getQuery(empj_PaymentGuaranteeChildDao.getBasicHQL(), empj_PaymentGuaranteeChildForm));

			for (Empj_PaymentGuaranteeChild empj_PaymentGuaranteeChild : empj_PaymentGuaranteeChildList)
			{
				Empj_BuildingInfo empj_BuildingInfo = empj_PaymentGuaranteeChild.getEmpj_BuildingInfo();
				if (empj_BuildingInfo == null || S_TheState.Deleted.equals(empj_BuildingInfo.getTheState()))
				{
					continue;
				}

				empj_PaymentGuaranteeChild.setBusiState("1");
				empj_PaymentGuaranteeChild.setEmpj_BuildingInfo(empj_BuildingInfo);
				empj_PaymentGuaranteeChildDao.save(empj_PaymentGuaranteeChild);

				Tgpj_BuildingAccount tgpj_BuildingAccount = empj_BuildingInfo.getBuildingAccount();

				// 不发生修改的字段
				Tgpj_BuildingAccountLog tgpj_BuildingAccountLog = new Tgpj_BuildingAccountLog();
				tgpj_BuildingAccountLog.setTheState(S_TheState.Normal);
				tgpj_BuildingAccountLog.setBusiState(tgpj_BuildingAccount.getBusiState());
				tgpj_BuildingAccountLog.seteCode(tgpj_BuildingAccount.geteCode());
				tgpj_BuildingAccountLog.setUserStart(tgpj_BuildingAccount.getUserStart());
				tgpj_BuildingAccountLog.setCreateTimeStamp(tgpj_BuildingAccount.getCreateTimeStamp());
				tgpj_BuildingAccountLog.setUserUpdate(user);
				tgpj_BuildingAccountLog.setLastUpdateTimeStamp(System.currentTimeMillis());
				tgpj_BuildingAccountLog.setUserRecord(tgpj_BuildingAccount.getUserRecord());
				tgpj_BuildingAccountLog.setRecordTimeStamp(tgpj_BuildingAccount.getRecordTimeStamp());
				tgpj_BuildingAccountLog.setDevelopCompany(tgpj_BuildingAccount.getDevelopCompany());
				tgpj_BuildingAccountLog.seteCodeOfDevelopCompany(tgpj_BuildingAccount.geteCodeOfDevelopCompany());
				tgpj_BuildingAccountLog.setProject(tgpj_BuildingAccount.getProject());
				tgpj_BuildingAccountLog.setTheNameOfProject(tgpj_BuildingAccount.getTheNameOfProject());
				tgpj_BuildingAccountLog.setBuilding(tgpj_BuildingAccount.getBuilding());
				tgpj_BuildingAccountLog.setBldLimitAmountVerDtl(tgpj_BuildingAccount.getBldLimitAmountVerDtl());
				tgpj_BuildingAccountLog.setPayment(tgpj_BuildingAccount.getPayment());
				tgpj_BuildingAccountLog.seteCodeOfBuilding(tgpj_BuildingAccount.geteCodeOfBuilding());
				tgpj_BuildingAccountLog.setEscrowStandard(tgpj_BuildingAccount.getEscrowStandard());
				tgpj_BuildingAccountLog.setEscrowArea(tgpj_BuildingAccount.getEscrowArea());
				tgpj_BuildingAccountLog.setBuildingArea(tgpj_BuildingAccount.getBuildingArea());
				tgpj_BuildingAccountLog.setOrgLimitedAmount(tgpj_BuildingAccount.getOrgLimitedAmount());
				tgpj_BuildingAccountLog.setCurrentFigureProgress(tgpj_BuildingAccount.getCurrentFigureProgress());
				tgpj_BuildingAccountLog.setCurrentLimitedRatio(tgpj_BuildingAccount.getCurrentLimitedRatio());
				tgpj_BuildingAccountLog.setNodeLimitedAmount(tgpj_BuildingAccount.getNodeLimitedAmount());
				tgpj_BuildingAccountLog.setTotalGuaranteeAmount(tgpj_BuildingAccount.getTotalGuaranteeAmount());
				tgpj_BuildingAccountLog.setCashLimitedAmount(tgpj_BuildingAccount.getCashLimitedAmount());
				tgpj_BuildingAccountLog.setTotalAccountAmount(tgpj_BuildingAccount.getTotalAccountAmount());
				tgpj_BuildingAccountLog.setPayoutAmount(tgpj_BuildingAccount.getPayoutAmount());
				tgpj_BuildingAccountLog.setAppliedNoPayoutAmount(tgpj_BuildingAccount.getAppliedNoPayoutAmount());
				tgpj_BuildingAccountLog.setApplyRefundPayoutAmount(tgpj_BuildingAccount.getApplyRefundPayoutAmount());
				tgpj_BuildingAccountLog.setRefundAmount(tgpj_BuildingAccount.getRefundAmount());
				tgpj_BuildingAccountLog.setCurrentEscrowFund(tgpj_BuildingAccount.getCurrentEscrowFund());
				tgpj_BuildingAccountLog.setAppropriateFrozenAmount(tgpj_BuildingAccount.getAppropriateFrozenAmount());
				tgpj_BuildingAccountLog.setRecordAvgPriceOfBuildingFromPresellSystem(tgpj_BuildingAccount.getRecordAvgPriceOfBuildingFromPresellSystem());
				tgpj_BuildingAccountLog.setRecordAvgPriceOfBuilding(tgpj_BuildingAccount.getRecordAvgPriceOfBuilding());
				tgpj_BuildingAccountLog.setLogId(tgpj_BuildingAccount.getLogId());
				tgpj_BuildingAccountLog.setActualAmount(tgpj_BuildingAccount.getActualAmount());
				tgpj_BuildingAccountLog.setPaymentLines(tgpj_BuildingAccount.getPaymentLines());
				tgpj_BuildingAccountLog.setRelatedBusiCode(BUSI_CODE);
				tgpj_BuildingAccountLog.setRelatedBusiTableId(empj_PaymentGuaranteeChild.getTableId());
				tgpj_BuildingAccountLog.setTgpj_BuildingAccount(tgpj_BuildingAccount);
				tgpj_BuildingAccountLog.setVersionNo(tgpj_BuildingAccount.getVersionNo());
				
				
	    		Double appliedNoPayoutAmount = 0.0;//退款冻结金额
	    		if( null != tgpj_BuildingAccount.getAppliedNoPayoutAmount() && tgpj_BuildingAccount.getAppliedNoPayoutAmount() > 0)
	    		{
	    			appliedNoPayoutAmount = tgpj_BuildingAccount.getAppliedNoPayoutAmount();
	    		}
	    		
	    		Double payoutAmount = 0.0;//已拨付金额
	    		if( null != tgpj_BuildingAccount.getPayoutAmount() && tgpj_BuildingAccount.getPayoutAmount() > 0)
	    		{
	    			payoutAmount = tgpj_BuildingAccount.getPayoutAmount();
	    		}
	    		
	    		Double appropriateFrozenAmount = 0.0;//拨付冻结金额（元）
	    		if( null != tgpj_BuildingAccount.getAppropriateFrozenAmount() && tgpj_BuildingAccount.getAppropriateFrozenAmount() > 0)
	    		{
	    			appropriateFrozenAmount = tgpj_BuildingAccount.getAppropriateFrozenAmount();
	    		}
	    		
	    		Double buildAmountPaid = 0.0;//楼幢项目建设已实际支付金额（元）
	    		if( null != tgpj_BuildingAccount.getBuildAmountPaid() && tgpj_BuildingAccount.getBuildAmountPaid() > 0)
	    		{
	    			buildAmountPaid = tgpj_BuildingAccount.getBuildAmountPaid();
	    		}
	    		//楼幢项目建设已实际支付金额（元）
	    		buildAmountPaid = myDouble.doubleSubtractDouble(buildAmountPaid,empj_PaymentGuaranteeChild.getBuildProjectPaid());
	    		
	    		Double buildAmountPay = 0.0;//楼幢项目建设待支付承保累计金额（元）
	    		if( null != tgpj_BuildingAccount.getBuildAmountPay() && tgpj_BuildingAccount.getBuildAmountPay() > 0)
	    		{
	    			buildAmountPay = tgpj_BuildingAccount.getBuildAmountPay();
	    		}
	    		//楼幢项目建设待支付承保累计金额（元）
	    		buildAmountPay = myDouble.doubleSubtractDouble( buildAmountPay , empj_PaymentGuaranteeChild.getBuildProjectPay());
	    		
	    		//已落实支付保证累计金额（元） = 楼幢项目建设已实际支付金额（元） + 楼幢项目建设待支付承保累计金额（元）
	    		Double totalAmountGuaranteed = myDouble.doubleAddDouble(buildAmountPaid, buildAmountPay);
	    			    		
	    		Double effectiveLimitedAmount = 0.0;//有效受限额度（元）
	    		if( null != tgpj_BuildingAccount.getEffectiveLimitedAmount() && tgpj_BuildingAccount.getEffectiveLimitedAmount() > 0)
	    		{
	    			effectiveLimitedAmount = tgpj_BuildingAccount.getEffectiveLimitedAmount();
	    		}
	    		
	    		Double orgLimitedAmount = 0.0;//初始受限额度（元）
	    		if( null != tgpj_BuildingAccount.getOrgLimitedAmount() && tgpj_BuildingAccount.getOrgLimitedAmount() > 0)
	    		{
	    			orgLimitedAmount = tgpj_BuildingAccount.getOrgLimitedAmount();
	    		}
	    		
	    		//现金受限额度 = 初始受限额度-已落实支付保证累计金额
	    		Double cashLimitedAmount = myDouble.doubleSubtractDouble(orgLimitedAmount, totalAmountGuaranteed);
	    		
	    		Double nodeLimitedAmount = 0.0;////当前节点受限额度（元）
	    		if( null != tgpj_BuildingAccount.getNodeLimitedAmount() && tgpj_BuildingAccount.getNodeLimitedAmount() > 0)
	    		{
	    			nodeLimitedAmount = tgpj_BuildingAccount.getNodeLimitedAmount();
	    		}

	    		// 有效受限额度（元）= 现金受限额度与当前节点受限额度的最小值
	    		if( cashLimitedAmount < nodeLimitedAmount)
	    		{
	    			effectiveLimitedAmount = cashLimitedAmount;
	    		}
	    		else
	    		{
	    			effectiveLimitedAmount = nodeLimitedAmount;
	    		}
	    		
	    		Double totalAccountAmount = 0.0;//总入账金额（元)
	    		if( null != tgpj_BuildingAccount.getTotalAccountAmount() && tgpj_BuildingAccount.getTotalAccountAmount() > 0)
	    		{
	    			totalAccountAmount = tgpj_BuildingAccount.getTotalAccountAmount();
	    		}
	    		
	    		//溢出金额= 总入账金额 - 有效受限额度 - 已拨付金额
	    		Double spilloverAmount = myDouble.doubleSubtractDouble( totalAccountAmount , myDouble.doubleAddDouble(effectiveLimitedAmount, payoutAmount)) ;
	    		
	    		//释放金额（元）=总入账金额-有效受限额度-已拨付金额-拨付冻结金额-退款冻结金额
	    		Double releaseTheAmount = myDouble.doubleSubtractDouble( totalAccountAmount , myDouble.doubleAddDouble(myDouble.doubleAddDouble(appropriateFrozenAmount, appliedNoPayoutAmount),myDouble.doubleAddDouble(effectiveLimitedAmount, payoutAmount)));
	    		
				// 修改产生了变更的字段
				tgpj_BuildingAccountLog.setPaymentProportion(empj_PaymentGuaranteeChild.getPaymentProportion());
				tgpj_BuildingAccountLog.setBuildAmountPaid(buildAmountPaid);
				tgpj_BuildingAccountLog.setBuildAmountPay(buildAmountPay);
				tgpj_BuildingAccountLog.setTotalAmountGuaranteed(totalAmountGuaranteed);
				tgpj_BuildingAccountLog.setCashLimitedAmount(cashLimitedAmount);
				tgpj_BuildingAccountLog.setEffectiveLimitedAmount(effectiveLimitedAmount);
				tgpj_BuildingAccountLog.setSpilloverAmount(spilloverAmount);
				tgpj_BuildingAccountLog.setAllocableAmount(releaseTheAmount);
				
				tgpj_BuildingAccountLogDao.save(tgpj_BuildingAccountLog);	
				
				buildingLogMap.add(tgpj_BuildingAccountLog);
			}
		}
				
		properties = sm_ApprovalProcessGetService.execute(busiCode, model.getUserId());
		if (properties.getProperty("info").equals("noApproval"))
		{
			for(int i = 0 ;i < buildingLogMap.size() ; i++ )
			{
				tgpj_BuildingAccountLimitedUpdateService.execute(buildingLogMap.get(i));
			}	
			empj_PaymentGuarantee.setApprovalState(S_ApprovalState.Completed);
			empj_PaymentGuarantee.setUserRecord(user);
			empj_PaymentGuarantee.setRecordTimeStamp(System.currentTimeMillis());
			empj_PaymentGuarantee.setBusiState("4"); // 申请中
			empj_PaymentGuaranteeDao.save(empj_PaymentGuarantee);
		}
		else
		{
			empj_PaymentGuarantee.setApprovalState(S_ApprovalState.Examining);
			empj_PaymentGuarantee.setBusiState("3"); // 申请中
			empj_PaymentGuaranteeDao.save(empj_PaymentGuarantee);
			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties
					.get("sm_approvalProcess_cfg");
			// 审批操作
			sm_approvalProcessService.execute(empj_PaymentGuarantee, model, sm_approvalProcess_cfg);
		}			
		//审批操作
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
			
	}
}









