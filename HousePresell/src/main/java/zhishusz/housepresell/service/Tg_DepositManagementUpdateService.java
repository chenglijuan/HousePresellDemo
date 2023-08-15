package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentBatchForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_ButtonType;
import zhishusz.housepresell.database.po.state.S_DepositState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyString;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Tg_DepositManagementForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.util.ObjectCopier;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;

/*
 * Service更新操作：存单管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_DepositManagementUpdateService
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
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private Sm_PoCompareResult sm_PoCompareResult;

	//附件相关
	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
	@Autowired
	private AttachmentJudgeExistUtil attachmentJudgeExistUtil;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;//申请单
	

	public Properties execute(Tg_DepositManagementForm model)
	{
		Properties properties = new MyProperties();

		String buttonType = model.getButtonType(); //1： 保存按钮  2：提交按钮
		Long loginUserId = model.getUserId();
		if (loginUserId == null || loginUserId < 1)
		{
			return MyBackInfo.fail(properties, "'登录用户'不能为空");
		}

		Long tg_DepositManagementId = model.getTableId();
		Tg_DepositManagement tg_DepositManagement = tg_DepositManagementDao.findByIdWithClear(tg_DepositManagementId);

		if(tg_DepositManagement == null)
		{
			return MyBackInfo.fail(properties, "'Tg_DepositManagement(Id:" + tg_DepositManagementId + ")'不存在");
		}

		String depositState = model.getDepositState();
		String depositProperty = model.getDepositProperty();
		Long bankId = model.getBankId();
		Long bankOfDepositId = model.getBankOfDepositId();
		Long escrowAcountId = model.getEscrowAcountId();
//		String escrowAcountShortName = model.getEscrowAcountShortName();
		String agent = model.getAgent();
		Double principalAmount = model.getPrincipalAmount();
		Integer storagePeriod = model.getStoragePeriod();
		String storagePeriodCompany = model.getStoragePeriodCompany();
		Double annualRate = model.getAnnualRate();
		String openAccountCertificate = model.getOpenAccountCertificate();
		Double expectedInterest = model.getExpectedInterest();
		String floatAnnualRate = model.getFloatAnnualRate();

		String startDateStr = model.getStartDateStr();
		String stopDateStr = model.getStopDateStr();

		String extractDateStr = model.getExtractDateStr();
		Double realInterest = model.getRealInterest();
		Double realInterestRate = model.getRealInterestRate();

		Integer calculationRule = model.getCalculationRule();//计算规则
		
		String remark = model.getRemark();

		Emmp_BankInfo bank = emmp_BankInfoDao.findById(bankId);
		Emmp_BankBranch bankOfDeposit = emmp_BankBranchDao.findById(bankOfDepositId);
		Tgxy_BankAccountEscrowed escrowAcount = tgxy_bankAccountEscrowedDao.findById(escrowAcountId);

		if(depositState == null || depositState.length()< 1) //存单的类型，一定要传
		{
			return MyBackInfo.fail(properties, "保存失败");
		}

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
			calculationRule = tg_DepositManagement.getCalculationRule();
//			return MyBackInfo.fail(properties, "请选择计算规则");
		}

		if (!S_DepositState.InProgress.equals(depositState))
		{
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
			/**
			 * xsz by 2019-10-10 17:00:28
			 * BUG#5585 存单提取的时候 浮动年利率可为空
			 * start
			 */
			/*if(floatAnnualRate == null )
			{
				return MyBackInfo.fail(properties, "请输入浮动年利率");
			}*/
			
			/**
			 * xsz by 2019-10-10 17:00:28
			 * BUG#5585 存单提取的时候 浮动年利率可为空
			 * end
			 */
			if (S_DepositState.Deposit.equals(depositState) && S_BusiState.HaveRecord.equals(model.getBusiState()))
			{
				if(extractDateStr == null || extractDateStr.length() == 0)
				{
					return MyBackInfo.fail(properties, "请选择提取日期");
				}
				if(realInterest == null)
				{
					return MyBackInfo.fail(properties, "请输入实际利息");
				}
				if(realInterestRate == null)
				{
					return MyBackInfo.fail(properties, "请输入实际利率");
				}
			}
		}
		else
		{
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

		Long startDate = null;
		if (model.getStartDateStr() != null && model.getStartDateStr().length() > 0)
		{
			startDate = MyDatetime.getInstance().stringToLong(model.getStartDateStr());
		}
		Long stopDate = null;
		if (model.getStopDateStr() != null && model.getStopDateStr().length() > 0)
		{
			stopDate = MyDatetime.getInstance().stringToLong(model.getStopDateStr());
		}
		Long extractDate = null;
		if (model.getExtractDateStr() != null && model.getExtractDateStr().length() > 0)
		{
			extractDate = MyDatetime.getInstance().stringToLong(model.getExtractDateStr());
		}

		//先保存操作人
		tg_DepositManagement.setRemarkIn(remark);
		tg_DepositManagement.setRemarkOut(remark);
		tg_DepositManagement.setUserUpdate(model.getUser());
		tg_DepositManagement.setLastUpdateTimeStamp(System.currentTimeMillis());
		tg_DepositManagementDao.update(tg_DepositManagement);

		//取得需要审批的字段变更前的对象
		Tg_DepositManagement tg_DepositManagementOld = ObjectCopier.copy(tg_DepositManagement);

		//取得需要审批的字段变更后的对象
		tg_DepositManagement.setDepositProperty(depositProperty);
		tg_DepositManagement.setBank(bank);
		tg_DepositManagement.setBankOfDeposit(bankOfDeposit);
		tg_DepositManagement.setEscrowAcount(escrowAcount);
		tg_DepositManagement.setAgent(agent);
		tg_DepositManagement.setPrincipalAmount(principalAmount);
		tg_DepositManagement.setStoragePeriod(storagePeriod);
		tg_DepositManagement.setStoragePeriodCompany(storagePeriodCompany);
		tg_DepositManagement.setAnnualRate(annualRate);
		tg_DepositManagement.setStartDate(startDate);
		tg_DepositManagement.setStopDate(stopDate);
		tg_DepositManagement.setOpenAccountCertificate(openAccountCertificate);
		tg_DepositManagement.setExpectedInterest(expectedInterest);
		tg_DepositManagement.setFloatAnnualRate(floatAnnualRate);
		tg_DepositManagement.setExtractDate(extractDate);
		tg_DepositManagement.setRealInterest(realInterest);
		tg_DepositManagement.setRealInterestRate(realInterestRate);
		
		tg_DepositManagement.setCalculationRule(calculationRule);

		// 如果是存单正在办理，一定没有审批
		if (S_DepositState.InProgress.equals(model.getDepositState()))
		{
			//保存数据and返回
			tg_DepositManagementDao.update(tg_DepositManagement);

			Sm_AttachmentBatchForm sm_AttachmentBatchForm = new Sm_AttachmentBatchForm();
			sm_AttachmentBatchForm.setTheState(S_TheState.Normal);
			sm_AttachmentBatchForm.setBusiType(BUSI_CODE_INPROGRESS);
			sm_AttachmentBatchForm.setSourceId(MyString.getInstance().parse(model.getTableId()));
			sm_AttachmentBatchForm.setGeneralAttachmentList(model.getGeneralAttachmentList());
			sm_AttachmentBatchForm.setUserId(model.getUserId());
			sm_AttachmentBatchAddService.execute(sm_AttachmentBatchForm);

			tg_DepositManagement.setBusiState(S_BusiState.HaveRecord);
			tg_DepositManagement.setApprovalState(S_ApprovalState.Completed);

			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

			return properties;
		}

		if(S_DepositState.TakeOut.equals(model.getDepositState())){
			Sm_AttachmentBatchForm sm_AttachmentBatchForm = new Sm_AttachmentBatchForm();
			sm_AttachmentBatchForm.setTheState(S_TheState.Normal);
			sm_AttachmentBatchForm.setBusiType(BUSI_CODE);
			sm_AttachmentBatchForm.setSourceId(MyString.getInstance().parse(model.getTableId()));
			sm_AttachmentBatchForm.setGeneralAttachmentList(model.getGeneralAttachmentList());
			sm_AttachmentBatchForm.setUserId(model.getUserId());
			sm_AttachmentBatchAddService.execute(sm_AttachmentBatchForm);
		}
		
		Tg_DepositManagement tg_DepositManagementNew = ObjectCopier.copy(tg_DepositManagement);

		//新旧数据对比，看是都有字段修改
		Boolean flag = sm_PoCompareResult.execute(tg_DepositManagementOld, tg_DepositManagementNew);

		if (flag) //附件列表
		{
			for (Sm_AttachmentForm formOSS : model.getGeneralAttachmentList())
			{
				//如果有form没有tableId，说明有新增
				if (formOSS.getTableId() == null || formOSS.getTableId() == 0)
				{
					flag = false;
					break;
				}
			}
			if (!flag) //如果没有新增再看有没有删除
			{
				Integer totalCountNew = model.getGeneralAttachmentList().length;

				Sm_AttachmentForm theForm = new Sm_AttachmentForm();
				theForm.setTheState(S_TheState.Normal);

				if (S_DepositState.InProgress.equals(model.getDepositState()))
				{
					theForm.setBusiType(BUSI_CODE_INPROGRESS);
				}
				else //如果不是存单正在办理 附件统统用存单存入的业务编码
				{
					theForm.setBusiType(BUSI_CODE);
				}
				theForm.setSourceId(MyString.getInstance().parse(model.getTableId()));
				Integer totalCount = sm_AttachmentDao.findByPage_Size(sm_AttachmentDao.getQuery_Size(sm_AttachmentDao.getBasicHQL(), theForm));

				if (totalCountNew < totalCount)
				{
					flag = false;
				}
			}
		}


		//先判断是否是未备案
		//如果是未备案则先保存到数据库然后根据是提交按钮还是保存按钮判断是否走新增的审批流
		if(S_BusiState.NoRecord.equals(tg_DepositManagement.getBusiState()))
		{
			tg_DepositManagementDao.update(tg_DepositManagement);

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
			sm_AttachmentBatchForm.setSourceId(MyString.getInstance().parse(model.getTableId()));
			sm_AttachmentBatchForm.setGeneralAttachmentList(model.getGeneralAttachmentList());
			sm_AttachmentBatchForm.setUserId(model.getUserId());
			sm_AttachmentBatchAddService.execute(sm_AttachmentBatchForm);

			//如果是提交按钮则需要走新增的审批流
			if(S_ButtonType.Submit.equals(buttonType))
			{
				properties = sm_ApprovalProcessGetService.execute(BUSI_CODE, model.getUserId());
				if("fail".equals(properties.getProperty(S_NormalFlag.result)))
				{
					return properties;
				}

				//没有配置审批流程无需走审批流直接保存数据库
				if(!"noApproval".equals(properties.getProperty(S_NormalFlag.info)))
				{
					//有相应的审批流程配置才走审批流程
					Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");

					//审批操作
					sm_approvalProcessService.execute(tg_DepositManagement, model, sm_approvalProcess_cfg);
				}
				else
				{
					tg_DepositManagement.setBusiState(S_BusiState.HaveRecord);
					tg_DepositManagement.setApprovalState(S_ApprovalState.Completed);
					tg_DepositManagementDao.update(tg_DepositManagement);
				}
			}
		}

		else if(!flag)
		{
			properties = sm_ApprovalProcessGetService.execute(BUSI_CODE_TAKEOUT, model.getUserId());
			if("fail".equals(properties.getProperty(S_NormalFlag.result)))
			{
				return properties;
			}

			//没有配置审批流程无需走审批流直接保存数据库
			if("noApproval".equals(properties.getProperty(S_NormalFlag.info)))
			{
				tg_DepositManagement.setBusiState(S_BusiState.HaveRecord);
				tg_DepositManagement.setApprovalState(S_ApprovalState.Completed);
				tg_DepositManagement.setDepositState(S_DepositState.TakeOut);
				tg_DepositManagementDao.update(tg_DepositManagement);

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
				sm_AttachmentBatchForm.setSourceId(MyString.getInstance().parse(model.getTableId()));
				sm_AttachmentBatchForm.setGeneralAttachmentList(model.getGeneralAttachmentList());
				sm_AttachmentBatchForm.setUserId(model.getUserId());
				sm_AttachmentBatchAddService.execute(sm_AttachmentBatchForm);


				//日志，备案人，备案日期
				//TODO
			}
			else
			{
				//做一个还原操作
//				try {
//					PropertyUtils.copyProperties(tg_DepositManagement, tg_DepositManagementOld);
//				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//					e.printStackTrace();
//				}

				//有相应的审批流程配置才走审批流程
				Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");

				tg_DepositManagement.setUserUpdate(model.getUser());
				tg_DepositManagement.setLastUpdateTimeStamp(System.currentTimeMillis());

				//审批操作
				sm_approvalProcessService.execute(tg_DepositManagement, model, sm_approvalProcess_cfg);
			}
			
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}

}
