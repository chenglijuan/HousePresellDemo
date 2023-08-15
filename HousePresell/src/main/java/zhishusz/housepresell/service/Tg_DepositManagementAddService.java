package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.housepresell.controller.form.Sm_AttachmentBatchForm;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_DepositState;
import zhishusz.housepresell.util.MyString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_DepositManagementForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;

/*
 * Service添加操作：存单管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_DepositManagementAddService
{
	private static final String BUSI_CODE = "210104";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	private static final String BUSI_CODE_TAKEOUT = "210105"; //存单提取
	private static final String BUSI_CODE_INPROGRESS = "210106"; //正在办理

	@Autowired
	private Tg_DepositManagementDao tg_DepositManagementDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_BankInfoDao emmp_BankInfoDao;
	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_bankAccountEscrowedDao;

	//业务编码
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;

	//附件相关
	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
	@Autowired
	private AttachmentJudgeExistUtil attachmentJudgeExistUtil;
	
	public Properties execute(Tg_DepositManagementForm model)
	{
		Properties properties = new MyProperties();

		Long loginUserId = model.getUserId();
		if (loginUserId == null || loginUserId < 1)
		{
			return MyBackInfo.fail(properties, "'登录用户'不能为空");
		}

		String depositState = model.getDepositState();
		String depositProperty = model.getDepositProperty();
		Long bankId = model.getBankId();
		Long bankOfDepositId = model.getBankOfDepositId();
		Long escrowAcountId = model.getEscrowAcountId();
		String agent = model.getAgent();
		Double principalAmount = model.getPrincipalAmount();
		Integer storagePeriod = model.getStoragePeriod();
		String storagePeriodCompany = model.getStoragePeriodCompany();
		Double annualRate = model.getAnnualRate();

		String startDateStr = model.getStartDateStr();
		String stopDateStr = model.getStopDateStr();

		String openAccountCertificate = model.getOpenAccountCertificate();
		Double expectedInterest = model.getExpectedInterest();
		String floatAnnualRate = model.getFloatAnnualRate();
		
		Integer calculationRule = model.getCalculationRule();//计算规则
		
		String remark = model.getRemark();

		if(depositState == null || depositState.length()< 1) //存单的类型，一定要传
		{
			return MyBackInfo.fail(properties, "保存失败");
		}

		String eCode;

		Emmp_BankInfo bank = emmp_BankInfoDao.findById(bankId);
		Emmp_BankBranch bankOfDeposit = emmp_BankBranchDao.findById(bankOfDepositId);
		Tgxy_BankAccountEscrowed escrowAcount = tgxy_bankAccountEscrowedDao.findById(escrowAcountId);


		if(bank == null)
		{
			return MyBackInfo.fail(properties, "请选择银行名称");
		}
		if(bankOfDeposit == null)
		{
			return MyBackInfo.fail(properties, "请选择支行");
		}
		if(escrowAcount == null)
		{
			return MyBackInfo.fail(properties, "请选择托管账户");
		}

		if(depositProperty == null || depositProperty.length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择存款性质");
		}

		if(null == calculationRule || calculationRule <0)
		{
			calculationRule = 360;
		}
		
		if (!S_DepositState.InProgress.equals(depositState))
		{
			eCode = sm_BusinessCodeGetService.execute(BUSI_CODE);

			if(principalAmount == null)
			{
				return MyBackInfo.fail(properties, "请输入本金金额");
			}
			if(principalAmount <= 0)
			{
				return MyBackInfo.fail(properties, "本金金额必须大于0");
			}

			if(storagePeriod == null || storagePeriod < 1)
			{
				return MyBackInfo.fail(properties, "请输入存期");
			}
			if(storagePeriodCompany == null || storagePeriodCompany.length() == 0)
			{
				return MyBackInfo.fail(properties, "请选择存期单位");
			}
			if(annualRate == null )
			{
				return MyBackInfo.fail(properties, "请输入年利率");
			}
			if(startDateStr == null || startDateStr.length() == 0)
			{
				return MyBackInfo.fail(properties, "请选择起息日");
			}
			if(stopDateStr == null || stopDateStr.length() == 0)
			{
				return MyBackInfo.fail(properties, "请选择到期日");
			}
//			if(openAccountCertificate == null || openAccountCertificate.length() == 0)
//			{
//				return MyBackInfo.fail(properties, "请输入开户证实书");
//			}
			if(expectedInterest == null)
			{
				return MyBackInfo.fail(properties, "请输入预计利息");
			}
			if(floatAnnualRate == null )
			{
				return MyBackInfo.fail(properties, "请输入浮动年利率");
			}
		}
		else
		{
			eCode = sm_BusinessCodeGetService.execute(BUSI_CODE_INPROGRESS);

			if(principalAmount == null)
			{
				return MyBackInfo.fail(properties, "请输入正在办理金额");
			}
			if(principalAmount <= 0)
			{
				return MyBackInfo.fail(properties, "办理金额必须大于0");
			}
			if(startDateStr == null || startDateStr.length() == 0)
			{
				return MyBackInfo.fail(properties, "请选择办理日期");
			}
		}

		Sm_AttachmentBatchForm isExistForm = new Sm_AttachmentBatchForm();
		isExistForm.setTheState(S_TheState.Normal);
		if (S_DepositState.InProgress.equals(model.getDepositState()))
		{
			isExistForm.setBusiType(BUSI_CODE_INPROGRESS);
		}
		else //如果不是存单正在办理 附件统统用存单存入的业务编码
		{
			isExistForm.setBusiType(BUSI_CODE);
		}
		isExistForm.setGeneralAttachmentList(model.getGeneralAttachmentList());
		MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(isExistForm);
		if(!msgInfo.isSuccess())
		{
			return MyBackInfo.fail(properties, msgInfo.getInfo());
		}
	
		Tg_DepositManagement tg_DepositManagement = new Tg_DepositManagement();
		tg_DepositManagement.setTheState(S_TheState.Normal);
//		tg_DepositManagement.setBusiState(busiState);
		tg_DepositManagement.seteCode(eCode);
		tg_DepositManagement.setDepositState(depositState);
		tg_DepositManagement.setUserStart(model.getUser());
		tg_DepositManagement.setCreateTimeStamp(System.currentTimeMillis());
		tg_DepositManagement.setUserUpdate(model.getUser());
		tg_DepositManagement.setLastUpdateTimeStamp(System.currentTimeMillis());
		tg_DepositManagement.setDepositProperty(depositProperty);
		tg_DepositManagement.setBank(bank);
		tg_DepositManagement.setBankOfDeposit(bankOfDeposit);
		tg_DepositManagement.setEscrowAcount(escrowAcount);
//		tg_DepositManagement.setEscrowAcountShortName(escrowAcountShortName);
		tg_DepositManagement.setAgent(agent);
		tg_DepositManagement.setPrincipalAmount(principalAmount);
		tg_DepositManagement.setStoragePeriod(storagePeriod);
		tg_DepositManagement.setStoragePeriodCompany(storagePeriodCompany);
		tg_DepositManagement.setAnnualRate(annualRate);
		
		tg_DepositManagement.setRemarkIn(remark);
		tg_DepositManagement.setRemarkOut(remark);
		
		tg_DepositManagement.setStateIn("0");
		tg_DepositManagement.setStateOut("0");
		
		tg_DepositManagement.setCalculationRule(calculationRule);

		if (startDateStr != null && startDateStr.length() > 0)
		{
			tg_DepositManagement.setStartDate(MyDatetime.getInstance().stringToLong(startDateStr));
		}
		if (stopDateStr != null && stopDateStr.length() > 0)
		{
			tg_DepositManagement.setStopDate(MyDatetime.getInstance().stringToLong(stopDateStr));
		}

		tg_DepositManagement.setOpenAccountCertificate(openAccountCertificate);
		tg_DepositManagement.setExpectedInterest(expectedInterest);
		tg_DepositManagement.setFloatAnnualRate(floatAnnualRate);

		if (S_DepositState.Deposit.equals(depositState))  //如果是存单存入新增 走审批
		{
			// 发起人校验
			//1 如果该业务没有配置审批流程，直接保存
			//2 如果该业务配置了审批流程 ，判断用户能否与对应模块下的审批流程的发起人角色匹配
			properties = sm_ApprovalProcessGetService.execute(BUSI_CODE, loginUserId);
			if(! properties.getProperty("info").equals("noApproval") && properties.getProperty("result").equals("fail"))
			{
				return properties;
			}

			if(properties.getProperty("info").equals("noApproval"))
			{
				tg_DepositManagement.setBusiState(S_BusiState.HaveRecord);
				tg_DepositManagement.setApprovalState(S_ApprovalState.Completed);
				tg_DepositManagementDao.save(tg_DepositManagement);
			}
			else
			{
				Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");

				//业务状态
				tg_DepositManagement.setBusiState(S_BusiState.NoRecord);
				tg_DepositManagement.setApprovalState(S_ApprovalState.WaitSubmit);
				tg_DepositManagementDao.save(tg_DepositManagement);
				
				model.setIsSetApprovalState(false);
				sm_approvalProcessService.execute(tg_DepositManagement, model, sm_approvalProcess_cfg);
			}
		}
		else
		{
			//存单正在办理
			//开始保存数据
			tg_DepositManagement.setBusiState(S_BusiState.HaveRecord);
			tg_DepositManagement.setApprovalState(S_ApprovalState.Completed);

			tg_DepositManagementDao.save(tg_DepositManagement);
		}


		//附件材料
		Sm_AttachmentBatchForm sm_AttachmentBatchForm = new Sm_AttachmentBatchForm();
		sm_AttachmentBatchForm.setTheState(S_TheState.Normal);
		if (S_DepositState.InProgress.equals(model.getDepositState()))
		{
			sm_AttachmentBatchForm.setBusiType(BUSI_CODE_INPROGRESS);
		}
		else //如果不是存单正在办理 附件统统用存单存入的业务编码
		{
			sm_AttachmentBatchForm.setBusiType(BUSI_CODE);
		}
		sm_AttachmentBatchForm.setSourceId(MyString.getInstance().parse(tg_DepositManagement.getTableId()));
		sm_AttachmentBatchForm.setGeneralAttachmentList(model.getGeneralAttachmentList());
		sm_AttachmentBatchForm.setUserId(model.getUserId());
		properties = sm_AttachmentBatchAddService.execute(sm_AttachmentBatchForm);

		properties.put("tg_DepositManagement", tg_DepositManagement);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
