package zhishusz.housepresell.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeChildForm;
import zhishusz.housepresell.controller.form.Tgpf_BuildingRemainRightLogForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFForm;
import zhishusz.housepresell.controller.form.Tgpf_FundOverPlanDetaillForm;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeChildDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.dao.Tgpj_BankAccountSupervisedDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PaymentGuaranteeChild;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlanDetail;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_ApplyState;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_ButtonType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_PayoutState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.messagequeue.producer.MQKey_EventType;
import zhishusz.housepresell.messagequeue.producer.MQKey_OrgType;
import zhishusz.housepresell.util.MQConnectionUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：申请-用款-主表
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_FundAppropriated_AFAddService
{
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Tgpf_FundAppropriated_AFDtlAddService tgpf_FundAppropriated_AFDtlAddService;
	@Autowired
	private Tgpf_FundOverPlanDetailAddService tgpf_fundOverPlanDetailAddService;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired 
	private MQConnectionUtil mqConnectionUtil;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;//楼幢
	@Autowired
	private Tgpf_FundAppropriated_AFDtlDao tgpf_FundAppropriated_AFDtlDao;//用款申请
	@Autowired
	private Empj_PaymentGuaranteeChildDao empj_PaymentGuaranteeChildDao;// 支付保证
	@Autowired
	private Tgpj_BankAccountSupervisedDao tgpj_BankAccountSupervisedDao;//监管账户
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_buildingAccountDao;//楼幢账户
	@Autowired
	private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;//受限额度变更
	@Autowired
	private CheckMutexService checkMutexService;
	
	private MyDatetime myDatetime = MyDatetime.getInstance();
	
	public Properties execute(Tgpf_FundAppropriated_AFForm model)
	{

		Properties properties = new MyProperties();

		String busiCode = S_BusiCode.busiCode7; //业务编码
		Long loginUserId = model.getUserId();
		String buttonType = model.getButtonType();
		Long projectId = model.getProjectId();
		String applyDate = model.getApplyDate();
		Double totalApplyAmount = model.getTotalApplyAmount(); // 本次申请总金额
		Boolean isNeedApproval;

		if (loginUserId == null || loginUserId < 1)
		{
			return MyBackInfo.fail(properties, "'登录用户'不能为空");
		}
		Sm_User userStart = model.getUser();
		if (userStart == null)
		{
			return MyBackInfo.fail(properties, "'登录用户'不能为空");
		}

		// 发起人校验
		//1 如果该业务没有配置审批流程，直接保存
		//2 如果该业务配置了审批流程 ，判断用户能否与对应模块下的审批流程的发起人角色匹配
		properties = sm_ApprovalProcessGetService.execute(busiCode, loginUserId);
		if(! properties.getProperty("info").equals("noApproval") && properties.getProperty("result").equals("fail"))
		{
			return properties;
		}
		else
		{
			isNeedApproval = true;
		}

		if(userStart.getCompany() == null)
		{
			return MyBackInfo.fail(properties, "'用户所属企业'不能为空");
		}
		Emmp_CompanyInfo developCompany = userStart.getCompany();

		if (projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "'请选择项目'");
		}

		Empj_ProjectInfo project = (Empj_ProjectInfo) empj_ProjectInfoDao.findById(projectId);

		if (project == null)
		{
			return MyBackInfo.fail(properties, "'请选择项目'");
		}

		String theNameOfDevelopCompany = developCompany.getTheName();
		String eCodeOfDevelopCompany = developCompany.geteCode();
		String theNameOfProject = project.getTheName();
		String eCodeOfProject = project.geteCode();
		if (theNameOfDevelopCompany == null || theNameOfDevelopCompany.length() == 0)
		{
			return MyBackInfo.fail(properties, "'机构名称'不能为空");
		}
		if (eCodeOfDevelopCompany == null || eCodeOfDevelopCompany.length() == 0)
		{
			return MyBackInfo.fail(properties, "'机构编号'不能为空");
		}
		if (theNameOfProject == null || theNameOfProject.length() == 0)
		{
			return MyBackInfo.fail(properties, "'项目名称'不能为空");
		}
		if (eCodeOfProject == null || eCodeOfProject.length() == 0)
		{
			return MyBackInfo.fail(properties, "'项目编号'不能为空");
		}

		//用款申请明细
		List<Tgpf_FundAppropriated_AFDtl> tgpf_FundAppropriated_AFDtls = new ArrayList<Tgpf_FundAppropriated_AFDtl>();

		if(model.getTgpf_FundAppropriated_AFAddtab() == null || model.getTgpf_FundAppropriated_AFAddtab().length == 0)
		{
			return MyBackInfo.fail(properties, "请选择'用款申请楼幢信息'");
		}
		for (Tgpf_FundAppropriated_AFDtlForm tgpf_FundAppropriated_AFDtlForm : model.getTgpf_FundAppropriated_AFAddtab())
		{
			/*
			 * xsz by time 2019-3-11 16:10:35
			 * 先校验楼幢数据有没有问题
			 */
			Properties propertiesSaveDetail = checkBuildingComplete(tgpf_FundAppropriated_AFDtlForm,buttonType,userStart,applyDate,isNeedApproval);
//			Properties propertiesSaveDetail = tgpf_FundAppropriated_AFDtlAddService.execute(tgpf_FundAppropriated_AFDtlForm,buttonType,userStart,applyDate,isNeedApproval);
			if (!S_NormalFlag.success.equals(propertiesSaveDetail.getProperty(S_NormalFlag.result)))
			{
				return propertiesSaveDetail;
//				tgpf_FundAppropriated_AFDtls.add((Tgpf_FundAppropriated_AFDtl) propertiesSaveDetail.get("tgpf_FundAppropriated_AFDtl"));
			}
			
			/**
			 * BUG#4080 互斥业务
			 */
			/*Empj_BuildingInfoForm buildingInfoForm = new Empj_BuildingInfoForm();
			buildingInfoForm.setTableId(tgpf_FundAppropriated_AFDtlForm.getBuildingId());
			Properties properties1 = checkMutexService.checkPaymentGuaranteeApply(buildingInfoForm);
			if(!S_NormalFlag.success.equals(properties1.get(S_NormalFlag.result)))
			{
				return properties1;
			}*/
			/**
			 * BUG#4080 互斥业务
			 */
			
		}
		
		//用款申请汇总信息
		List<Tgpf_FundOverallPlanDetail> tgpf_FundOverallPlanDetails = new ArrayList<Tgpf_FundOverallPlanDetail>();
		if(model.getTgpf_FundOverPlanDetailltab() == null || model.getTgpf_FundOverPlanDetailltab().length == 0)
		{
			return MyBackInfo.fail(properties, "'用款申请汇总信息'不能为空");
		}
		for (Tgpf_FundOverPlanDetaillForm tgpf_FundOverallPlanDetailForm : model.getTgpf_FundOverPlanDetailltab())
		{
			Properties propertiesSaveDetail = tgpf_fundOverPlanDetailAddService.execute(tgpf_FundOverallPlanDetailForm);
			if (S_NormalFlag.success.equals(propertiesSaveDetail.getProperty(S_NormalFlag.result)))
			{
				tgpf_FundOverallPlanDetails.add((Tgpf_FundOverallPlanDetail) propertiesSaveDetail.get("tgpf_FundOverallPlanDetail"));
			}
			else
			{
				return propertiesSaveDetail;
			}
		}
		
		//楼幢校验通过，更新楼幢数据
		for (Tgpf_FundAppropriated_AFDtlForm tgpf_FundAppropriated_AFDtlForm : model.getTgpf_FundAppropriated_AFAddtab())
		{
			/*
			 * xsz by time 2019-3-11 16:10:35
			 * 先校验楼幢数据有没有问题
			 */
			Properties propertiesSaveDetail = commitCheck(tgpf_FundAppropriated_AFDtlForm,buttonType,userStart,applyDate,isNeedApproval);
				
			tgpf_FundAppropriated_AFDtls.add((Tgpf_FundAppropriated_AFDtl) propertiesSaveDetail.get("tgpf_FundAppropriated_AFDtl"));
			
		}

		//用款申请主表
		Tgpf_FundAppropriated_AF tgpf_FundAppropriated_AF = new Tgpf_FundAppropriated_AF();
		tgpf_FundAppropriated_AF.setTheState(S_TheState.Normal);
		tgpf_FundAppropriated_AF.setApplyDate(applyDate);
		tgpf_FundAppropriated_AF.seteCode(sm_BusinessCodeGetService.execute(busiCode));
		tgpf_FundAppropriated_AF.setDevelopCompany(developCompany);
		tgpf_FundAppropriated_AF.setTheNameOfDevelopCompany(theNameOfDevelopCompany);
		tgpf_FundAppropriated_AF.setProject(project);
		tgpf_FundAppropriated_AF.setTheNameOfProject(theNameOfProject);
		tgpf_FundAppropriated_AF.seteCodeOfProject(eCodeOfProject);
		tgpf_FundAppropriated_AF.setTotalApplyAmount(totalApplyAmount);
		tgpf_FundAppropriated_AF.setUserStart(userStart);
		tgpf_FundAppropriated_AF.setCreateTimeStamp(System.currentTimeMillis());
		tgpf_FundAppropriated_AF.setUserUpdate(userStart);
		tgpf_FundAppropriated_AF.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgpf_FundAppropriated_AF.setFundAppropriated_AFDtlList(tgpf_FundAppropriated_AFDtls);
		tgpf_FundAppropriated_AF.setFundOverallPlanDetailList(tgpf_FundOverallPlanDetails);

		if(properties.getProperty("info").equals("noApproval"))
		{
			//不需要走审批流程 变更字段直接保存到数据库中
			tgpf_FundAppropriated_AF.setApplyState(S_ApplyState.Admissible);	  //申请单状态：已受理
			tgpf_FundAppropriated_AF.setApprovalState(S_ApprovalState.Completed); //审批状态：已完结
			tgpf_FundAppropriated_AFDao.save(tgpf_FundAppropriated_AF);
			
			for(Tgpf_FundAppropriated_AFDtl tgpf_FundAppropriated_AFDtl : tgpf_FundAppropriated_AFDtls)
			{
				//反写到Tgpf_RetainedRightsView拨付留存权益
				Tgpf_BuildingRemainRightLogForm tgpf_BuildingRemainRightLogForm = new Tgpf_BuildingRemainRightLogForm();
				tgpf_BuildingRemainRightLogForm.setBuildingId(tgpf_FundAppropriated_AFDtl.getBuilding().getTableId());
				tgpf_BuildingRemainRightLogForm.setSrcBusiType("业务触发");
				tgpf_BuildingRemainRightLogForm.setBillTimeStamp(MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));

				//生成楼幢留存权益，同时生成每个楼幢下的户留存权益
				mqConnectionUtil.sendMessage(MQKey_EventType.APPROPRIATION_RETAINED, MQKey_OrgType.SYSTEM, tgpf_BuildingRemainRightLogForm);
			}
		}
		else
		{
			tgpf_FundAppropriated_AF.setApplyState(S_ApplyState.Application); // 申请单状态： 申请中

			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");

			tgpf_FundAppropriated_AFDao.save(tgpf_FundAppropriated_AF);

			//审批操作
			sm_approvalProcessService.execute(tgpf_FundAppropriated_AF, model, sm_approvalProcess_cfg);
		}

		properties.put("tableId", tgpf_FundAppropriated_AF.getTableId());
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
	
	/*
	 * 校验楼幢信息的完整性
	 */
	@SuppressWarnings("unchecked")
	public Properties checkBuildingComplete(Tgpf_FundAppropriated_AFDtlForm model, String buttonType, Sm_User userStart, String applyDate, Boolean isNeedApproval)
	{
		Properties properties = new MyProperties();
		Long buildingId = model.getTableId(); //楼幢id
		Long bankAccountSupervisedId = model.getBankAccountSupervisedId(); // 监管账户Id
		Double appliedAmount = model.getAppliedAmount(); //本次划款申请金额

		if(buildingId == null || buildingId < 1)
		{
			return MyBackInfo.fail(properties, "'该楼幢信息'不存在");
		}
		Empj_BuildingInfo building = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(buildingId);
		if(building == null)
		{
			return MyBackInfo.fail(properties, "'该楼幢信息'不存在");
		}

		//------------------------------------判断该楼幢是否正在用款申请中-------------------------------------------------//
		Tgpf_FundAppropriated_AFDtlForm fundAppropriated_afDtlForm = new Tgpf_FundAppropriated_AFDtlForm();
		fundAppropriated_afDtlForm.setTheState(S_TheState.Normal);
		fundAppropriated_afDtlForm.setBuildingId(buildingId);
		fundAppropriated_afDtlForm.setPayoutState(S_PayoutState.NotAppropriated);
		List<Tgpf_FundAppropriated_AFDtl> tgpf_fundAppropriated_afDtls = tgpf_FundAppropriated_AFDtlDao.findByPage(tgpf_FundAppropriated_AFDtlDao.getQuery(tgpf_FundAppropriated_AFDtlDao.getJumpBasicHQL(), fundAppropriated_afDtlForm));
		for (Tgpf_FundAppropriated_AFDtl tgpf_fundAppropriated_afDtl : tgpf_fundAppropriated_afDtls)
		{
			String eCodeFromConstruction = building.geteCodeFromConstruction();
			return MyBackInfo.fail(properties, "‘"+eCodeFromConstruction+"’正在用款申请中");
		}
		//------------------------------------判断该楼幢是否正在用款申请中-------------------------------------------------//
		
		/**
		 * BUG#4080 互斥业务
		 */
		Empj_BuildingInfoForm buildingInfoForm = new Empj_BuildingInfoForm();
		buildingInfoForm.setTableId(buildingId);
		Properties properties1 = checkMutexService.checkFundAppropriated_AFDtl(buildingInfoForm);
		if(!S_NormalFlag.success.equals(properties1.get(S_NormalFlag.result)))
		{
			return properties1;
		}
		/**
		 * BUG#4080 互斥业务
		 */
		
		/**
		 * xsz by time 2019-7-15 11:46:48
		 * 判断是否发起节点变更
		 */
		Empj_BldLimitAmount_AFForm AFModel = new Empj_BldLimitAmount_AFForm();
		AFModel.setTheState(S_TheState.Normal);
		AFModel.setBuilding(building);
		AFModel.setBuildingId(building.getTableId());
		List<Empj_BldLimitAmount_AF> afList = new ArrayList<>();
		afList = empj_BldLimitAmount_AFDao.findByPage(empj_BldLimitAmount_AFDao.getQuery(empj_BldLimitAmount_AFDao.getListHQL(), AFModel));
		
		if(!afList.isEmpty())
		{
			Empj_BldLimitAmount_AF empj_BldLimitAmount_AF = afList.get(0);
			if(!S_ApprovalState.Completed.equals(empj_BldLimitAmount_AF.getApprovalState()))
			{
				return MyBackInfo.fail(properties, "楼幢："+building.geteCodeFromConstruction()+"已发起节点变更流程，请待流程结束后重新申请！");
			}
		}
		
		//------------------------------------判断该楼幢是否正在支付保证撤销流程中-------------------------------------------------//
		/*
		 * xsz by time 2018-12-13 15:18:31
		 * 查询楼幢是否处于支付保证或支付保证撤销中
		 */
		Empj_PaymentGuaranteeChildForm paymentGuaranteeChildModel = new Empj_PaymentGuaranteeChildForm();
		paymentGuaranteeChildModel.setTheState(S_TheState.Normal);
		paymentGuaranteeChildModel.setEmpj_BuildingInfo(building);

		List<Empj_PaymentGuaranteeChild> paymentGuaranteeChildList;
		paymentGuaranteeChildList = empj_PaymentGuaranteeChildDao.findByPage(empj_PaymentGuaranteeChildDao
				.getQuery(empj_PaymentGuaranteeChildDao.getBasicHQL(), paymentGuaranteeChildModel));

		/**
		 * xsz by time 2018-12-13 16:43:46
		 * 和wuyu确认同一时期一个楼幢只会存在一条有效信息
		 * 
		 * 主表busiState 为1 或3 说明该楼幢处于支付保证申请或支付保证撤销中
		 */
		if (null != paymentGuaranteeChildList && paymentGuaranteeChildList.size() > 0)
		{
			
			Empj_PaymentGuaranteeChild paymentGuaranteeChild = paymentGuaranteeChildList.get(0);
			String busiState = paymentGuaranteeChild.getEmpj_PaymentGuarantee().getBusiState();
			if ("1".equals(busiState))
			{
				return MyBackInfo.fail(properties, "该楼幢已发起支付保证申请，请检查后重试！");
			}

			if ("3".equals(busiState))
			{
				return MyBackInfo.fail(properties, "该楼幢已发起支付保证撤销申请，请检查后重试！");
			}
			
		}
		//------------------------------------判断该楼幢是否正在支付保证撤销流程中-------------------------------------------------//


		if(bankAccountSupervisedId == null || bankAccountSupervisedId < 1)
		{
			return MyBackInfo.fail(properties, "请维护监管账户！");
		}
		Tgpj_BankAccountSupervised bankAccountSupervised = (Tgpj_BankAccountSupervised)tgpj_BankAccountSupervisedDao.findById(bankAccountSupervisedId);

		if(bankAccountSupervised == null)
		{
			return MyBackInfo.fail(properties, "请维护监管账户！");
		}
		if(appliedAmount == null || appliedAmount < 0)
		{
			return MyBackInfo.fail(properties, "'本次申请总金额'不能为空");
		}
		
		String supervisedBankAccount = bankAccountSupervised.getTheAccount();
		if(supervisedBankAccount == null || supervisedBankAccount.length() == 0)
		{
			return MyBackInfo.fail(properties, "'监管账号'不能为空");
		}
		//楼幢虚拟账户
		if( building.getBuildingAccount() == null)
		{
			return MyBackInfo.fail(properties, "'请维护楼幢账户'");
		}

		Tgpj_BuildingAccount tgpj_buildingAccount  = building.getBuildingAccount();
		/**
		 * 	操作检查：
		 * 	1、对应的楼幢没有维护监管账号和备案价格，
		 * 	不可以做拨付，限制客户操作，如果没有维护监管账户，
		 * 	则提示信息“请维护监管账户！”，如果没有维护备案价格，
		 * 	则提示信息“请维护物价备案价格！”。
		 */
		
		/*
		 * xsz by time 2019-4-4 17:54:55
		 * ATSK#1566 用款计划申请的时候，现在会校验备案价格 改成校验初始受限额度>0时可以提交（orglimitedamount）
		 */
		/*Double recordAvgPriceOfBuilding = tgpj_buildingAccount.getRecordAvgPriceOfBuilding();//楼幢住宅备案均价
		if(recordAvgPriceOfBuilding == null || recordAvgPriceOfBuilding <= 0)
		{
			return MyBackInfo.fail(properties, "请维护物价备案价格!");
		}*/
		
		Double orgLimitedAmount = tgpj_buildingAccount.getOrgLimitedAmount();//初始受限额度
		if(orgLimitedAmount == null || orgLimitedAmount <= 0)
		{
//			return MyBackInfo.fail(properties, "请维护物价备案价格!");
			return MyBackInfo.fail(properties, "请先维护托管标准或备案价格!");
		}

		Double allocableAmount = tgpj_buildingAccount.getAllocableAmount(); // 当前可划拨金额
		System.out.println("当前可拨付金额="+allocableAmount);
		if(allocableAmount == null || allocableAmount < 0)
		{
			return MyBackInfo.fail(properties, "'当前可划拨金额'不能为空");
		}

		if(null == appliedAmount || appliedAmount == 0){
			return MyBackInfo.fail(properties, "请输入有效的本次划款申请金额");
		}
		
		BigDecimal d = new BigDecimal(allocableAmount);
		allocableAmount= d.setScale(3,RoundingMode.HALF_UP).doubleValue();
		
		if(allocableAmount < appliedAmount)
		{
			return MyBackInfo.fail(properties, "本次划款申请金额不得大于当前可划拨金额，请确认！");
		}
	
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		return properties;
	}
	
	/*
	 * 楼幢信息校验无误，更新数据
	 */
	public Properties commitCheck(Tgpf_FundAppropriated_AFDtlForm model, String buttonType, Sm_User userStart, String applyDate, Boolean isNeedApproval)
	{
		Properties properties = new MyProperties();
		Long buildingId = model.getTableId(); //楼幢id
		Long bankAccountSupervisedId = model.getBankAccountSupervisedId(); // 监管账户Id
		Double appliedAmount = model.getAppliedAmount(); //本次划款申请金额
		String escrowStandard = model.getEscrowStandard(); //托管标准
		Empj_BuildingInfo building = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(buildingId);

		Tgpj_BankAccountSupervised bankAccountSupervised = (Tgpj_BankAccountSupervised)tgpj_BankAccountSupervisedDao.findById(bankAccountSupervisedId);

		String eCodeOfBuilding = building.geteCode(); //楼幢编号
		String supervisedBankAccount = bankAccountSupervised.getTheAccount();

		Tgpj_BuildingAccount tgpj_buildingAccount  = building.getBuildingAccount();

		/**
		 * 	操作检查：
		 * 	1、对应的楼幢没有维护监管账号和备案价格，
		 * 	不可以做拨付，限制客户操作，如果没有维护监管账户，
		 * 	则提示信息“请维护监管账户！”，如果没有维护备案价格，
		 * 	则提示信息“请维护物价备案价格！”。
		 */
		Double allocableAmount = tgpj_buildingAccount.getAllocableAmount(); // 当前可划拨金额
		Double orgLimitedAmount = tgpj_buildingAccount.getOrgLimitedAmount(); //初始受限额度
		String currentFigureProgress = tgpj_buildingAccount.getCurrentFigureProgress();    //当前形象进度
		Double currentLimitedRatio = tgpj_buildingAccount.getCurrentLimitedRatio(); //当前受限比例
		Double currentLimitedAmount = tgpj_buildingAccount.getNodeLimitedAmount(); //当前受限额度
		Double totalAccountAmount = tgpj_buildingAccount.getTotalAccountAmount(); //总入账金额
		Double appliedPayoutAmount = tgpj_buildingAccount.getPayoutAmount(); //已申请拨付金额
		Double currentEscrowFund = tgpj_buildingAccount.getCurrentEscrowFund(); //当前托管余额
		Double refundAmount = tgpj_buildingAccount.getRefundAmount(); //退房退款金额
		
		Double cashLimitedAmount = tgpj_buildingAccount.getCashLimitedAmount();//现金受限额度
		Double effectiveLimitedAmount = tgpj_buildingAccount.getEffectiveLimitedAmount();//有效受限额度

		Tgpf_FundAppropriated_AFDtl tgpf_FundAppropriated_AFDtl = new Tgpf_FundAppropriated_AFDtl();
		tgpf_FundAppropriated_AFDtl.setTheState(S_TheState.Normal);
		tgpf_FundAppropriated_AFDtl.setBuilding(building);
		tgpf_FundAppropriated_AFDtl.seteCodeOfBuilding(eCodeOfBuilding);
		tgpf_FundAppropriated_AFDtl.setBankAccountSupervised(bankAccountSupervised);
		tgpf_FundAppropriated_AFDtl.setSupervisedBankAccount(supervisedBankAccount);
		tgpf_FundAppropriated_AFDtl.setPayoutState(S_PayoutState.NotAppropriated); //拨付状态  1: 未拨付
		tgpf_FundAppropriated_AFDtl.setAppliedAmount(appliedAmount);
		tgpf_FundAppropriated_AFDtl.setAllocableAmount(allocableAmount);
		tgpf_FundAppropriated_AFDtl.setEscrowStandard(escrowStandard);
		tgpf_FundAppropriated_AFDtl.setOrgLimitedAmount(orgLimitedAmount);
		tgpf_FundAppropriated_AFDtl.setCurrentFigureProgress(currentFigureProgress);
		tgpf_FundAppropriated_AFDtl.setCurrentLimitedRatio(currentLimitedRatio);
		tgpf_FundAppropriated_AFDtl.setCurrentLimitedAmount(currentLimitedAmount);
		tgpf_FundAppropriated_AFDtl.setTotalAccountAmount(totalAccountAmount);
		tgpf_FundAppropriated_AFDtl.setAppliedPayoutAmount(appliedPayoutAmount);
		tgpf_FundAppropriated_AFDtl.setCurrentEscrowFund(currentEscrowFund);
		tgpf_FundAppropriated_AFDtl.setRefundAmount(refundAmount);
		tgpf_FundAppropriated_AFDtl.setUserStart(userStart);
		tgpf_FundAppropriated_AFDtl.setCreateTimeStamp(myDatetime.stringToLong(applyDate));
		tgpf_FundAppropriated_AFDtl.setUserUpdate(userStart);
		tgpf_FundAppropriated_AFDtl.setLastUpdateTimeStamp(myDatetime.stringToLong(applyDate));

		tgpf_FundAppropriated_AFDtl.setCashLimitedAmount(cashLimitedAmount);
		tgpf_FundAppropriated_AFDtl.setEffectiveLimitedAmount(effectiveLimitedAmount);
		/**
		 * 数据更新：
		 * 	用款申请“提交”后更新金额：
		 * 	1)更新楼幢账户：增加“已申请未拨付金额（元）appliedNoPayoutAmount”、减少“可划拨金额（元）allocableAmount”。
		 *  增加"拨付冻结金额"
		 */
		if(buttonType.equals(S_ButtonType.Submit) || isNeedApproval == false)
		{
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

			allocableAmount -=appliedAmount; //减少可划拨金额
			appliedNoPayoutAmount += appliedAmount;//增加已申请未拨付金额
			appropriateFrozenAmount += appliedAmount;//增加拨付冻结金额

			tgpj_buildingAccount.setAllocableAmount(allocableAmount);//减少可划拨金额
			tgpj_buildingAccount.setAppliedNoPayoutAmount(appliedNoPayoutAmount);// 增加已申请未拨付金额
			tgpj_buildingAccount.setAppropriateFrozenAmount(appropriateFrozenAmount);//增加拨付冻结金额

			tgpj_buildingAccountDao.save(tgpj_buildingAccount);
		}

		properties.put("tgpf_FundAppropriated_AFDtl",tgpf_FundAppropriated_AFDtl);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		return properties;
	}
}
