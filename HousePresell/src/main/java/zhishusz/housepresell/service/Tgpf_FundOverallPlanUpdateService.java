package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriatedForm;
import zhishusz.housepresell.controller.form.Tgpf_OverallPlanAccoutForm;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.*;
import zhishusz.housepresell.util.MyDatetime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Tgpf_FundOverallPlanForm;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：资金统筹
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundOverallPlanUpdateService
{
	@Autowired
	private Tgpf_FundOverallPlanDao tgpf_FundOverallPlanDao;
	@Autowired
	private Tgpf_FundAppropriatedAddService fundAppropriatedAddService;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private Tgpf_OverallPlanAccoutDao tgpf_OverallPlanAccoutDao;
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;
	@Autowired
	private Tgpf_FundAppropriatedDao tgpf_fundAppropriatedDao;
	@Autowired
	private Tgpf_CyberBankStatementDao tgpf_CyberBankStatementDao;
	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;
	
	private MyDatetime myDatetime = MyDatetime.getInstance();
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_FundOverallPlanForm model)
	{
		Properties properties = new MyProperties();

		String busiCode = S_BusiCode.busiCode8; //业务编码
		Long loginUserId = model.getUserId(); //登录用户
		String fundOverallPlanDate = model.getFundOverallPlanDate(); //统筹日期
		Long tgpf_FundOverallPlanId = model.getTableId();
		Boolean isFirstFundAppropriated = model.getIsFirstFundAppropriated();

		if(loginUserId == null || loginUserId < 1)
		{
			return MyBackInfo.fail(properties, "'登录用户'不能为空");
		}
		Sm_User loginUser = model.getUser();
		if(loginUser == null)
		{
			return MyBackInfo.fail(properties,"登录用户不能为空");
		}
		
		boolean isPush = true;

		// 发起人校验
		//1 如果该业务没有配置审批流程，直接保存
		//2 如果该业务配置了审批流程 ，判断用户能否与对应模块下的审批流程的发起人角色匹配
		properties = sm_ApprovalProcessGetService.execute(busiCode, model.getUserId());
		if(! properties.getProperty("info").equals("noApproval") && properties.getProperty("result").equals("fail"))
		{
			return properties;
		}

		if(fundOverallPlanDate == null || fundOverallPlanDate.length()==0)
		{
			return MyBackInfo.fail(properties, "'统筹日期'不能为空");
		}

		Tgpf_FundOverallPlan tgpf_FundOverallPlan = (Tgpf_FundOverallPlan)tgpf_FundOverallPlanDao.findById(tgpf_FundOverallPlanId);
		if(tgpf_FundOverallPlan == null)
		{
			return MyBackInfo.fail(properties, "'统筹单'不存在");
		}

		//是否是首次统筹
		//1.首次统筹，新增拨付信息
		//2.修改统筹信息，更新拨付信息
		if(isFirstFundAppropriated)
		{
			//统筹页面 - 新增资金拨付信息
			List<Tgpf_FundAppropriated> tgpf_fundAppropriatedList = new ArrayList<Tgpf_FundAppropriated>();
			if(model.getTgpf_FundAppropriatedList() == null || model.getTgpf_FundAppropriatedList().length == 0)
			{
				return MyBackInfo.fail(properties, "请选择'用款申请楼幢信息'");
			}
			for (Tgpf_FundAppropriatedForm tgpf_fundAppropriatedForm : model.getTgpf_FundAppropriatedList())
			{
				Properties propertiesSaveDetail = fundAppropriatedAddService.execute(tgpf_fundAppropriatedForm,loginUser);
				if (S_NormalFlag.success.equals(propertiesSaveDetail.getProperty(S_NormalFlag.result)))
				{
					tgpf_fundAppropriatedList.add((Tgpf_FundAppropriated) propertiesSaveDetail.get("tgpf_FundAppropriated"));
				}
				else
				{
					return propertiesSaveDetail;
				}
			}
			/**
			 * 设置用款申请主表与资金拨付一对多关联关系
			 */
			if(tgpf_FundOverallPlan.getFundAppropriated_AFList() != null)//用款申请列表信息
			{
				for(Tgpf_FundAppropriated_AF tgpf_fundAppropriated_af : tgpf_FundOverallPlan.getFundAppropriated_AFList())
				{
					List<Tgpf_FundAppropriated> tgpf_fundAppropriateds = new ArrayList<>();//资金拨付列表信息
					for(Tgpf_FundAppropriated tgpf_fundAppropriated : tgpf_fundAppropriatedList)
					{
						if(tgpf_fundAppropriated_af.getTableId().equals(tgpf_fundAppropriated.getFundAppropriated_AF().getTableId()))
						{
							tgpf_fundAppropriateds.add(tgpf_fundAppropriated);
						}
					}
					tgpf_fundAppropriated_af.setFundAppropriatedList(tgpf_fundAppropriateds);
					tgpf_FundAppropriated_AFDao.save(tgpf_fundAppropriated_af);
				}
			}
			/**
			 * 设置资金统筹与资金拨付一对多关联关系
			 */
			tgpf_FundOverallPlan.setFundAppropriatedList(tgpf_fundAppropriatedList);
		}
		else
		{
			//统筹页面 - 修改资金拨付金额
			for (Tgpf_FundAppropriatedForm tgpf_fundAppropriatedForm : model.getTgpf_FundAppropriatedList())
			{
				Long fundAppropriated_AFId = tgpf_fundAppropriatedForm.getFundAppropriated_AFId();//同款申请主表Id
				Long bankAccountEscrowedId = tgpf_fundAppropriatedForm.getBankAccountEscrowedId(); // 托管账号id
				Long bankAccountSupervisedId = tgpf_fundAppropriatedForm.getBankAccountSupervisedId();//监管账号id
				Double overallPlanPayoutAmount = tgpf_fundAppropriatedForm.getOverallPlanPayoutAmount(); //统筹拨付金额


				if(bankAccountEscrowedId == null || bankAccountEscrowedId < 1)
				{
					return MyBackInfo.fail(properties, "'托管账户'不能为空");
				}
				if(bankAccountSupervisedId == null || bankAccountSupervisedId < 1)
				{
					return MyBackInfo.fail(properties, "'监管账户'不能为空");
				}
				if(overallPlanPayoutAmount == null)
				{
					overallPlanPayoutAmount = 0.0;
				}
				else if(overallPlanPayoutAmount < 0)
				{
					return MyBackInfo.fail(properties, "'统筹拨付金额'不能小于0");
				}

				if(tgpf_FundOverallPlan.getFundAppropriatedList() == null)
				{
					return MyBackInfo.fail(properties, "'拨付计划'不能为空");
				}
				for(Tgpf_FundAppropriated tgpf_fundAppropriated : tgpf_FundOverallPlan.getFundAppropriatedList())
				{
					if(tgpf_fundAppropriated.getBankAccountEscrowed() == null)
					{
						return MyBackInfo.fail(properties, "'托管账户'不能为空");
					}
					if(tgpf_fundAppropriated.getBankAccountSupervised() == null)
					{
						return MyBackInfo.fail(properties, "'监管账户'不能为空");
					}
					if(tgpf_fundAppropriated.getFundAppropriated_AF() == null)
					{
						return MyBackInfo.fail(properties, "'用款申请主表'不能为空");
					}

					Tgxy_BankAccountEscrowed tgxy_bankAccountEscrowed = tgpf_fundAppropriated.getBankAccountEscrowed();//托管账户
					Tgpj_BankAccountSupervised tgpj_bankAccountSupervised = tgpf_fundAppropriated.getBankAccountSupervised();//监管账户
					Tgpf_FundAppropriated_AF tgpf_fundAppropriated_af = tgpf_fundAppropriated.getFundAppropriated_AF();//用款申请主表
					if(bankAccountEscrowedId.equals(tgxy_bankAccountEscrowed.getTableId())
							&& bankAccountSupervisedId.equals(tgpj_bankAccountSupervised.getTableId())
							&& fundAppropriated_AFId.equals(tgpf_fundAppropriated_af.getTableId()))
					{
						
//						Double cyberBankTotalAmount = 0.00;
						
//						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//						Tgpf_CyberBankStatementForm tgpf_CyberBankStatementForm = new Tgpf_CyberBankStatementForm();
//						tgpf_CyberBankStatementForm.setTheState(S_TheState.Normal);
//						tgpf_CyberBankStatementForm.setTheAccountOfBankAccountEscrowed(tgxy_bankAccountEscrowed.getTheAccount());
//						tgpf_CyberBankStatementForm.setReconciliationState(0);
//						tgpf_CyberBankStatementForm.setUploadTimeStamp(sdf.format(new Date()));
//						Tgpf_CyberBankStatement cyberBankStatement = tgpf_CyberBankStatementDao.findOneByQuery_T(tgpf_CyberBankStatementDao.getQuery(tgpf_CyberBankStatementDao.getBasicHQL2(), tgpf_CyberBankStatementForm));
						
//						List<Tgpf_CyberBankStatement> cyberBankStatementList;
//						cyberBankStatementList = tgpf_CyberBankStatementDao.findByPage(tgpf_CyberBankStatementDao.getQuery(tgpf_CyberBankStatementDao.getBasicHQL(), tgpf_CyberBankStatementForm));
//
//						Tgpf_CyberBankStatementDtlForm tgpf_CyberBankStatementDtlForm;
//						for (Tgpf_CyberBankStatement cyberBankStatement : cyberBankStatementList) {
//							tgpf_CyberBankStatementDtlForm = new Tgpf_CyberBankStatementDtlForm();
//							tgpf_CyberBankStatementDtlForm.setTheState(S_TheState.Normal);
//							tgpf_CyberBankStatementDtlForm.setMainTable(cyberBankStatement);
//							tgpf_CyberBankStatementDtlForm.setMainTableId(cyberBankStatement.getTableId());
//							cyberBankTotalAmount += (Double) tgpf_CyberBankStatementDtlDao.findOneByQuery(tgpf_CyberBankStatementDtlDao.getSpecialQuery(tgpf_CyberBankStatementDtlDao.getBasicHQL(),tgpf_CyberBankStatementDtlForm, " nvl(sum(income),0) "));
//						}
						
						/*if(null != cyberBankStatement){
							Tgpf_CyberBankStatementDtlForm tgpf_CyberBankStatementDtlForm = new Tgpf_CyberBankStatementDtlForm();
							tgpf_CyberBankStatementDtlForm.setTheState(S_TheState.Normal);
							tgpf_CyberBankStatementDtlForm.setMainTable(cyberBankStatement);
							tgpf_CyberBankStatementDtlForm.setMainTableId(cyberBankStatement.getTableId());
							cyberBankTotalAmount = (Double) tgpf_CyberBankStatementDtlDao.findOneByQuery(tgpf_CyberBankStatementDtlDao.getSpecialQuery(tgpf_CyberBankStatementDtlDao.getBasicHQL(),tgpf_CyberBankStatementDtlForm, " nvl(sum(income),0) "));
							
						}*/
						
						//活期余额+当天网银入账金额
//						Double countAmount = (null == cyberBankTotalAmount?0.00 : cyberBankTotalAmount) + (null == tgxy_bankAccountEscrowed.getCurrentBalance()?0.00:tgxy_bankAccountEscrowed.getCurrentBalance());
//						if(countAmount < 0 ){
//							countAmount = 0.00;
//						}
//						if(overallPlanPayoutAmount - countAmount > 0){
//							return MyBackInfo.fail(properties, "开户行："+tgxy_bankAccountEscrowed.getTheNameOfBank()+" 托管余额不足！");
//						}
						
						tgpf_fundAppropriated.setOverallPlanPayoutAmount(overallPlanPayoutAmount);//统筹拨付金额
						tgpf_fundAppropriated.setCurrentPayoutAmount(overallPlanPayoutAmount);//本次实际拨付金额
						tgpf_fundAppropriated.setUserUpdate(loginUser);
						tgpf_fundAppropriated.setLastUpdateTimeStamp(System.currentTimeMillis());
						break;
					}
				}
			}

		}
		//更新统筹 账户状况信息表
		for(Tgpf_OverallPlanAccoutForm tgpf_overallPlanAccoutForm : model.getTgpf_OverallPlanAccountList())
		{
			if(tgpf_overallPlanAccoutForm == null)
			{
				return MyBackInfo.fail(properties, "'统筹账户状况信息'不能为空");
			}

			Double overallPlanAmount = tgpf_overallPlanAccoutForm.getOverallPlanAmount();
			Long tgpf_OverallPlanAccoutId = tgpf_overallPlanAccoutForm.getTableId();
			Double accountAmountTrim = tgpf_overallPlanAccoutForm.getAccountAmountTrim();//当天入账金额调整项
			Tgpf_OverallPlanAccout tgpf_OverallPlanAccout = (Tgpf_OverallPlanAccout)tgpf_OverallPlanAccoutDao.findById(tgpf_OverallPlanAccoutId);
			if(tgpf_OverallPlanAccout == null)
			{
				return MyBackInfo.fail(properties, "'统筹账户状况信息(Id:" + tgpf_OverallPlanAccoutId + ")'不存在");
			}

			tgpf_OverallPlanAccout.setLastUpdateTimeStamp(myDatetime.stringToLong(fundOverallPlanDate));
			tgpf_OverallPlanAccout.setUserUpdate(loginUser);
			tgpf_OverallPlanAccout.setOverallPlanAmount(overallPlanAmount);
			tgpf_OverallPlanAccout.setAccountAmountTrim(accountAmountTrim);
			tgpf_OverallPlanAccoutDao.save(tgpf_OverallPlanAccout);
		}

		//更新统筹单
		tgpf_FundOverallPlan.setUserUpdate(loginUser);
		tgpf_FundOverallPlan.setFundOverallPlanDate(fundOverallPlanDate);

		if(properties.getProperty("info").equals("noApproval"))
		{
			//不需要走审批流程 变更字段直接保存到数据库中
			tgpf_FundOverallPlan.setBusiState(S_CoordinateState.Alreadycoordinated); //统筹状态 ： 已统筹
			tgpf_FundOverallPlan.setApprovalState(S_ApprovalState.Completed);//流程状态： 已完结

			//更新用款申请主表：申请单状态
			for (Tgpf_FundAppropriated_AF tgpf_fundAppropriated_af : tgpf_FundOverallPlan.getFundAppropriated_AFList())
			{
				tgpf_fundAppropriated_af.setApplyState(S_ApplyState.Alreadycoordinated); //申请单状态 : 已统筹

				tgpf_FundAppropriated_AFDao.save(tgpf_fundAppropriated_af);
			}
		}
		else
		{
			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");

			//审批操作
			sm_approvalProcessService.execute(tgpf_FundOverallPlan, model, sm_approvalProcess_cfg);
			List<Tgpf_FundAppropriated> fundAppropriatedList;
			Tgpf_FundAppropriatedForm fundAppropriatedForm;
			Tgxy_BankAccountEscrowed bankAccountEscrowed;
			for (Tgpf_FundAppropriated_AF tgpf_fundAppropriated_af : tgpf_FundOverallPlan.getFundAppropriated_AFList())
			{
				fundAppropriatedForm = new Tgpf_FundAppropriatedForm();
				fundAppropriatedForm.setTheState(S_TheState.Normal);
				fundAppropriatedForm.setFundAppropriated_AFId(tgpf_fundAppropriated_af.getTableId());
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

		}
		tgpf_FundOverallPlanDao.save(tgpf_FundOverallPlan);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		if(!isPush){
			properties.put(S_NormalFlag.info, "存在资金系统不支持的开户行，请在系统内完成拨付流程！");
		}
		
		return properties;
	}
}
