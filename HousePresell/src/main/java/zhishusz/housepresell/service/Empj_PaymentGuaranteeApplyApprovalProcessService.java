package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import zhishusz.housepresell.util.MyProperties;

/*
 * Service 审批流提交 操作：退房退款-贷款已结清 
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_PaymentGuaranteeApplyApprovalProcessService
{
	
	@Autowired
	private Empj_PaymentGuaranteeDao empj_PaymentGuaranteeDao;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private Empj_PaymentGuaranteeChildDao empj_PaymentGuaranteeChildDao;
	@Autowired
	private Tgpj_BuildingAccountLogDao tgpj_BuildingAccountLogDao;
	@Autowired
	private Tgpj_BuildingAccountLogCalculateService calculateService;
	@Autowired
	private Tgpj_BuildingAccountLimitedUpdateService tgpj_BuildingAccountLimitedUpdateService;
	
	private static final String BUSI_CODE = "06120401";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	
	public Properties execute(Empj_PaymentGuaranteeForm model)
	{
		Properties properties = new MyProperties();
		model.setButtonType("2"); //1： 保存按钮  2：提交按钮
		String busiCode = BUSI_CODE;
		Long tableId = model.getTableId();
		
		List<Tgpj_BuildingAccountLog> buildingLogMap = new ArrayList<Tgpj_BuildingAccountLog>();
		
		Sm_User user = model.getUser();
		
		if(busiCode == null || busiCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'业务编码'不能为空");
		}

		if(tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "未查询到有效的支付保证申请信息！");
		}

		Empj_PaymentGuarantee empj_PaymentGuarantee = (Empj_PaymentGuarantee)empj_PaymentGuaranteeDao.findById(tableId);
		if(empj_PaymentGuarantee == null || S_TheState.Deleted.equals(empj_PaymentGuarantee.getTheState()))
		{
			return MyBackInfo.fail(properties, "保证申请有误！");
		}
		if( "1".equals(empj_PaymentGuarantee.getBusiState()))
		{
			return MyBackInfo.fail(properties, "申请中，请勿重复提交！");
		}
		if( "2".equals(empj_PaymentGuarantee.getBusiState()))
		{
			return MyBackInfo.fail(properties, "该申请已通过审核！");
		}
		if( "3".equals(empj_PaymentGuarantee.getBusiState()))
		{
			return MyBackInfo.fail(properties, "该撤销申请正在申请审核！");
		}
		if( "4".equals(empj_PaymentGuarantee.getBusiState()))
		{
			return MyBackInfo.fail(properties, "该撤销申请已通过审核！");
		}
		
		if (S_ApprovalState.Examining.equals(empj_PaymentGuarantee.getApprovalState()))
		{
			return MyBackInfo.fail(properties, "该协议已在审核中，不可重复提交");
		}
		else if (S_ApprovalState.Completed.equals(empj_PaymentGuarantee.getApprovalState()))
		{
			return MyBackInfo.fail(properties, "该协议已审批完成，不可重复提交");
		}
		
		// 查询保存的需要备案的楼幢
		Empj_PaymentGuaranteeChildForm empj_PaymentGuaranteeChildForm = new Empj_PaymentGuaranteeChildForm();
		empj_PaymentGuaranteeChildForm.setEmpj_PaymentGuarantee(empj_PaymentGuarantee);
		empj_PaymentGuaranteeChildForm.setTheState(S_TheState.Normal);
		
		Integer childCount = empj_PaymentGuaranteeChildDao.findByPage_Size(empj_PaymentGuaranteeChildDao.getQuery_Size(empj_PaymentGuaranteeChildDao.getBasicHQL(), empj_PaymentGuaranteeChildForm));
		
		List<Empj_PaymentGuaranteeChild> empj_PaymentGuaranteeChildList = new ArrayList<Empj_PaymentGuaranteeChild>();
		if(childCount > 0)
		{
			empj_PaymentGuaranteeChildList = empj_PaymentGuaranteeChildDao.findByPage(empj_PaymentGuaranteeChildDao.getQuery(empj_PaymentGuaranteeChildDao.getBasicHQL(), empj_PaymentGuaranteeChildForm));
			
			for(Empj_PaymentGuaranteeChild empj_PaymentGuaranteeChild : empj_PaymentGuaranteeChildList)
			{
				Empj_BuildingInfo empj_BuildingInfo = empj_PaymentGuaranteeChild.getEmpj_BuildingInfo();
				if(empj_BuildingInfo == null || S_TheState.Deleted.equals(empj_BuildingInfo.getTheState()))
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
				tgpj_BuildingAccountLog.setBldLimitAmountVerDtl(tgpj_BuildingAccount.getBldLimitAmountVerDtl());
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
//				tgpj_BuildingAccountLog.setPaymentLines(tgpj_BuildingAccount.getPaymentLines());
				tgpj_BuildingAccountLog.setRelatedBusiCode(BUSI_CODE);
				tgpj_BuildingAccountLog.setRelatedBusiTableId(empj_PaymentGuaranteeChild.getTableId());
				tgpj_BuildingAccountLog.setTgpj_BuildingAccount(tgpj_BuildingAccount);
				tgpj_BuildingAccountLog.setVersionNo(tgpj_BuildingAccount.getVersionNo());
				
				// 修改产生了变更的字段
				tgpj_BuildingAccountLog.setPaymentLines(empj_PaymentGuaranteeChild.getPaymentLines());
				tgpj_BuildingAccountLog.setPaymentProportion(empj_PaymentGuaranteeChild.getPaymentProportion());
				tgpj_BuildingAccountLog.setBuildAmountPaid(empj_PaymentGuaranteeChild.getBuildAmountPaid());
				tgpj_BuildingAccountLog.setBuildAmountPay(empj_PaymentGuaranteeChild.getBuildAmountPay());
				tgpj_BuildingAccountLog.setTotalAmountGuaranteed(empj_PaymentGuaranteeChild.getTotalAmountGuaranteed());
				tgpj_BuildingAccountLog.setCashLimitedAmount(empj_PaymentGuaranteeChild.getCashLimitedAmount());
				tgpj_BuildingAccountLog.setEffectiveLimitedAmount(empj_PaymentGuaranteeChild.getEffectiveLimitedAmount());
				tgpj_BuildingAccountLog.setSpilloverAmount(empj_PaymentGuaranteeChild.getSpilloverAmount());
				tgpj_BuildingAccountLog.setAllocableAmount(empj_PaymentGuaranteeChild.getReleaseTheAmount());
				
				calculateService.execute(tgpj_BuildingAccountLog);
				
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
			empj_PaymentGuarantee.setBusiState("2"); // 申请中
			empj_PaymentGuaranteeDao.save(empj_PaymentGuarantee);
		}
		else if ("fail".equals(properties.getProperty(S_NormalFlag.result)))
		{
			// 判断当前登录用户是否有权限发起审批
			return properties;
		}
		else
		{
			empj_PaymentGuarantee.setApprovalState(S_ApprovalState.Examining);
			empj_PaymentGuarantee.setBusiState("1"); // 申请中
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
