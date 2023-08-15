package zhishusz.housepresell.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeChildForm;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeChildDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Empj_PaymentGuaranteeChild;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/**
 * 支付保证申请添加service
 * @author wang
 *create by 2018/09/25
 */
@Service
@Transactional
public class Empj_PaymentGuaranteeApplyUpdateService{
	@Autowired
	private Empj_PaymentGuaranteeDao empj_PaymentGuaranteeDao;
	@Autowired
	private Empj_PaymentGuaranteeChildDao empj_PaymentGuaranteeChildDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Empj_BuildingInfoDao buildInfoDao;
	@Autowired
	private Tgpj_BuildingAccountDao accountDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	MyDouble myDouble = MyDouble.getInstance();
	
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;
	private static final String BUSI_CODE = "06120401";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_PaymentGuaranteeForm model)
	{
		Properties properties = new MyProperties();

		Sm_User user = model.getUser();
		


		Empj_PaymentGuaranteeForm empj_PaymentGuaranteeForm =JSON.parseObject(model.getEmpj_PaymentGuarantee(), Empj_PaymentGuaranteeForm.class);

		Long empj_PaymentGuaranteetableId = empj_PaymentGuaranteeForm.getTableId();
		
		Empj_PaymentGuarantee empj_PaymentGuarantee = (Empj_PaymentGuarantee)empj_PaymentGuaranteeDao.findById(empj_PaymentGuaranteetableId);
		if(empj_PaymentGuarantee == null || S_TheState.Deleted.equals(empj_PaymentGuarantee.getTheState()))
		{
			return MyBackInfo.fail(properties, "保证申请有误！");
		}
		
		if( null != empj_PaymentGuarantee.getBusiState() && (empj_PaymentGuarantee.getBusiState().equals("1") || empj_PaymentGuarantee.getBusiState().equals("2")))
		{
			return MyBackInfo.fail(properties, "保证申请已提交审核或审核完成，禁止修改！");
		}
		
		Emmp_CompanyInfo emmp_CompanyInfo = new Emmp_CompanyInfo();//开发企业 
		Empj_ProjectInfo empj_ProjectInfo = new Empj_ProjectInfo();//项目名称
		
		// 根据开发企业查询
		if (null != empj_PaymentGuaranteeForm.getCompanyId() && empj_PaymentGuaranteeForm.getCompanyId() > 0)
		{
			Long companyId = empj_PaymentGuaranteeForm.getCompanyId();

			emmp_CompanyInfo = (Emmp_CompanyInfo) emmp_CompanyInfoDao.findById(companyId);
			if (emmp_CompanyInfo == null || S_TheState.Deleted.equals(emmp_CompanyInfo.getTheState()))
			{
				return MyBackInfo.fail(properties, "企业信息有误！");
			}
			empj_PaymentGuarantee.setCompanyName(emmp_CompanyInfo.getTheName());
			empj_PaymentGuarantee.setCompany(emmp_CompanyInfo);
		}

		// 根据项目查询
		if (null != empj_PaymentGuaranteeForm.getProjectId() && empj_PaymentGuaranteeForm.getProjectId() > 0)
		{
			Long empj_ProjectInfoId = empj_PaymentGuaranteeForm.getProjectId();
			empj_ProjectInfo = (Empj_ProjectInfo) empj_ProjectInfoDao.findById(empj_ProjectInfoId);
			if (empj_ProjectInfo == null || S_TheState.Deleted.equals(empj_ProjectInfo.getTheState()))
			{
				return MyBackInfo.fail(properties, "项目信息有误！");
			}

			empj_PaymentGuarantee.setProjectName(empj_ProjectInfo.getTheName());
			empj_PaymentGuarantee.setProject(empj_ProjectInfo);

		}



		String applyDate = empj_PaymentGuaranteeForm.getApplyDate();//申请日期
		String guaranteeNo = empj_PaymentGuaranteeForm.getGuaranteeNo();//支付保证单号
		String guaranteeType = empj_PaymentGuaranteeForm.getGuaranteeType();//支付保证类型(1-银行支付保证 ,2- 支付保险 ,3- 支付担保 )
		Double alreadyActualAmount = empj_PaymentGuaranteeForm.getAlreadyActualAmount();// 项目建设工程已实际支付金额 
		Double actualAmount = empj_PaymentGuaranteeForm.getActualAmount(); //项目建设工程待支付承保金额 
		Double guaranteedAmount = empj_PaymentGuaranteeForm.getGuaranteedAmount(); //已落实支付保证金额
		String remark = empj_PaymentGuaranteeForm.getRemark(); //备注
		String guaranteeCompany = empj_PaymentGuaranteeForm.getGuaranteeCompany();//支付保证出具单位

		if(guaranteeNo == null || guaranteeNo.length()< 1)
		{
			return MyBackInfo.fail(properties, "请输入支付保证单号");
		}
		if(guaranteeType == null || guaranteeType.length()< 1)
		{
			return MyBackInfo.fail(properties, "请选择支付保证类型");
		}
		if(guaranteeCompany == null || guaranteeCompany.length()< 1)
		{
			return MyBackInfo.fail(properties, "请选择支付保证出具单位");
		}
		
		empj_PaymentGuarantee.setTheState(S_TheState.Normal);
//		empj_PaymentGuarantee.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		empj_PaymentGuarantee.setUserUpdate(user);
		empj_PaymentGuarantee.setLastUpdateTimeStamp(System.currentTimeMillis());
		
		empj_PaymentGuarantee.setApplyDate(applyDate);
		empj_PaymentGuarantee.setGuaranteeNo(guaranteeNo);
		empj_PaymentGuarantee.setGuaranteeType(guaranteeType);
		empj_PaymentGuarantee.setAlreadyActualAmount(alreadyActualAmount);
		empj_PaymentGuarantee.setActualAmount(actualAmount);
		empj_PaymentGuarantee.setGuaranteedAmount(guaranteedAmount);
		empj_PaymentGuarantee.setRemark(remark);
		empj_PaymentGuarantee.setGuaranteeCompany(guaranteeCompany);
	
		// 删除所有的子表记录，重新新增
		Empj_PaymentGuaranteeChildForm paymentGuaranteeChildForm = new Empj_PaymentGuaranteeChildForm();
		paymentGuaranteeChildForm.setEmpj_PaymentGuarantee(empj_PaymentGuarantee);
		paymentGuaranteeChildForm.setTheState(S_TheState.Normal);
		
		Integer paymentChildCount = empj_PaymentGuaranteeChildDao.findByPage_Size(empj_PaymentGuaranteeChildDao.getQuery_Size(empj_PaymentGuaranteeChildDao.getBasicHQL(), paymentGuaranteeChildForm));
		
		List<Empj_PaymentGuaranteeChild> guaranteeChildList;
		if(paymentChildCount > 0)
		{
			guaranteeChildList = empj_PaymentGuaranteeChildDao.findByPage(empj_PaymentGuaranteeChildDao.getQuery(empj_PaymentGuaranteeChildDao.getBasicHQL(), paymentGuaranteeChildForm));
		
			for(Empj_PaymentGuaranteeChild empj_PaymentGuaranteeChild : guaranteeChildList)
			{
				
				empj_PaymentGuaranteeChild.setTheState(S_TheState.Deleted);
				empj_PaymentGuaranteeChildDao.save(empj_PaymentGuaranteeChild);
			}		
		}

		List<Empj_PaymentGuaranteeChildForm> empj_PaymentGuaranteeChildList = JSON.parseArray(model.getEmpj_PaymentGuaranteeChildList(), Empj_PaymentGuaranteeChildForm.class);

		if( null == empj_PaymentGuaranteeChildList || empj_PaymentGuaranteeChildList.size() == 0)
		{
			return MyBackInfo.fail(properties, "请勾选需要支付保证申请的楼幢");
		}
		
		for(Empj_PaymentGuaranteeChildForm guaranteeChildForm : empj_PaymentGuaranteeChildList)
		{
	
			Empj_PaymentGuaranteeChild empj_PaymentGuaranteeChild = new Empj_PaymentGuaranteeChild();
			
			Long childId = guaranteeChildForm.getTableId();
			
			Long empj_BuildingInfoId = guaranteeChildForm.getEmpj_BuildingInfoId();
			
			Empj_BuildingInfo empj_BuildingInfo = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(empj_BuildingInfoId);
			
			if(empj_PaymentGuaranteeChildDao == null || S_TheState.Deleted.equals(empj_BuildingInfo.getTheState()))
			{
				return MyBackInfo.fail(properties, "楼幢信息有误");
			}
			
			Empj_PaymentGuaranteeChildForm empj_PaymentGuaranteeChildForm = new Empj_PaymentGuaranteeChildForm();
			empj_PaymentGuaranteeChildForm.setEmpj_BuildingInfo(empj_BuildingInfo);
			empj_PaymentGuaranteeChildForm.setTheState(S_TheState.Normal);
			String condition = "('0','1','3')";
			empj_PaymentGuaranteeChildForm.setCondition(condition);
			
			/*
			Integer childCount = empj_PaymentGuaranteeChildDao.findByPage_Size(empj_PaymentGuaranteeChildDao.getQuery_Size(empj_PaymentGuaranteeChildDao.getSpeicalHQL(), empj_PaymentGuaranteeChildForm));

			if( childCount > 0)
			{
				return MyBackInfo.fail(properties, "存在未审核通过但已申请的楼幢！");
			}*/
					
    		Tgpj_BuildingAccount tgpj_BuildingAccount = empj_BuildingInfo.getBuildingAccount();	    				    			
    		/*
    		 * 获取楼幢账户、楼幢基本信息中信息
    		 */
    		String eCodeFromConstruction1 = empj_BuildingInfo.geteCodeFromConstruction();//施工编号
    		String eCodeFromPublicSecurity = empj_BuildingInfo.geteCodeFromPublicSecurity();//公安编号
    		String escrowStandard = empj_BuildingInfo.getEscrowStandard();//托管标准

    		Double recordAvgPriceOfBuilding = 0.0;//楼幢住宅备案均价
    		if( null != tgpj_BuildingAccount.getRecordAvgPriceOfBuilding() && tgpj_BuildingAccount.getRecordAvgPriceOfBuilding() > 0)
    		{
    			recordAvgPriceOfBuilding = tgpj_BuildingAccount.getRecordAvgPriceOfBuilding();
    		}
    		
    		Double appliedNoPayoutAmount = 0.0;//退款冻结金额
    		if( null != tgpj_BuildingAccount.getAppliedNoPayoutAmount() && tgpj_BuildingAccount.getAppliedNoPayoutAmount() > 0)
    		{
    			appliedNoPayoutAmount = tgpj_BuildingAccount.getAppliedNoPayoutAmount();
    		}
    		
    		Double orgLimitedAmount = 0.0;//初始受限额度（元）
    		if( null != tgpj_BuildingAccount.getOrgLimitedAmount() && tgpj_BuildingAccount.getOrgLimitedAmount() > 0)
    		{
    			orgLimitedAmount = tgpj_BuildingAccount.getOrgLimitedAmount();
    		}
    		
    		Double buildingArea = 0.0;//建筑面积（㎡）
    		if( null != tgpj_BuildingAccount.getBuildingArea() && tgpj_BuildingAccount.getBuildingArea() > 0)
    		{
    			buildingArea = tgpj_BuildingAccount.getBuildingArea();
    		}
    		
    		Double refundAmount = 0.0;//已退款金额
    		if( null != tgpj_BuildingAccount.getRefundAmount() && tgpj_BuildingAccount.getRefundAmount() > 0)
    		{
    			refundAmount = tgpj_BuildingAccount.getRefundAmount();
    		}
    		
    		Double escrowArea = 0.0;//托管面积（㎡）
    		if( null != tgpj_BuildingAccount.getEscrowArea() && tgpj_BuildingAccount.getEscrowArea() > 0)
    		{
    			escrowArea = tgpj_BuildingAccount.getEscrowArea();
    		}
    		
    		Double paymentLines = 0.0;//支付保证封顶额度
    		if( null != tgpj_BuildingAccount.getPaymentLines() && tgpj_BuildingAccount.getPaymentLines() > 0)
    		{
    			paymentLines = tgpj_BuildingAccount.getPaymentLines();
    		}
    		
    		Double paymentProportion = 0.0;// 支付保证封顶比例
    		if( null != tgpj_BuildingAccount.getPaymentProportion() && tgpj_BuildingAccount.getPaymentProportion() > 0)
    		{
    			paymentProportion = tgpj_BuildingAccount.getPaymentProportion();
    		}	    		
    		
    		Double buildAmountPaid = 0.0;//楼幢项目建设已实际支付金额（元）
    		if( null != tgpj_BuildingAccount.getBuildAmountPaid() && tgpj_BuildingAccount.getBuildAmountPaid() > 0)
    		{
    			buildAmountPaid = tgpj_BuildingAccount.getBuildAmountPaid();
    		}
    		
    		Double buildAmountPay = 0.0;//楼幢项目建设待支付承保累计金额（元）
    		if( null != tgpj_BuildingAccount.getBuildAmountPay() && tgpj_BuildingAccount.getBuildAmountPay() > 0)
    		{
    			buildAmountPay = tgpj_BuildingAccount.getBuildAmountPay();
    		}
		
    		Double totalAmountGuaranteed = 0.0;//已落实支付保证累计金额（元）
    		if( null != tgpj_BuildingAccount.getTotalAmountGuaranteed() && tgpj_BuildingAccount.getTotalAmountGuaranteed() > 0)
    		{
    			totalAmountGuaranteed = tgpj_BuildingAccount.getTotalAmountGuaranteed();
    		}
    		
    		Double effectiveLimitedAmount = 0.0;//有效受限额度（元）
    		if( null != tgpj_BuildingAccount.getEffectiveLimitedAmount() && tgpj_BuildingAccount.getEffectiveLimitedAmount() > 0)
    		{
    			effectiveLimitedAmount = tgpj_BuildingAccount.getEffectiveLimitedAmount();
    		}
    		
    		Double totalAccountAmount = 0.0;//总入账金额（元)
    		if( null != tgpj_BuildingAccount.getTotalAccountAmount() && tgpj_BuildingAccount.getTotalAccountAmount() > 0)
    		{
    			totalAccountAmount = tgpj_BuildingAccount.getTotalAccountAmount();
    		}
    			    		
    		String currentFigureProgress = tgpj_BuildingAccount.getCurrentFigureProgress();//当前形象进度
    		
    		Double currentLimitedRatio = 0.0;//当前受限比例（%）
    		if( null != tgpj_BuildingAccount.getCurrentLimitedRatio() && tgpj_BuildingAccount.getCurrentLimitedRatio() > 0)
    		{
    			currentLimitedRatio = tgpj_BuildingAccount.getCurrentLimitedRatio();
    		}
    		
    		Double nodeLimitedAmount = 0.0;////当前节点受限额度（元）
    		if( null != tgpj_BuildingAccount.getNodeLimitedAmount() && tgpj_BuildingAccount.getNodeLimitedAmount() > 0)
    		{
    			nodeLimitedAmount = tgpj_BuildingAccount.getNodeLimitedAmount();
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
    			    		
    		Double spilloverAmount = 0.0;// 溢出金额
    		if( null != tgpj_BuildingAccount.getSpilloverAmount() && tgpj_BuildingAccount.getSpilloverAmount() > 0)
    		{
    			spilloverAmount = tgpj_BuildingAccount.getSpilloverAmount();
    		}
    		
    		/**
			 * 支付保证上限比例及支付保证上限额度
			 */
			paymentLines = null == guaranteeChildForm.getPaymentLines() ? 0.00 : guaranteeChildForm.getPaymentLines();// 支付保证上限比例
			paymentProportion = null == guaranteeChildForm.getPaymentProportion() ? 0.00
					: guaranteeChildForm.getPaymentProportion();// 支付保证封顶额度
			
			if(paymentLines == 0.00){
				return MyBackInfo.fail(properties, "请选择支付保证上限比例！");
			}
    		
    		//释放金额（元）
    		Double releaseTheAmount = 0.0;// 可拨付金额
    		if( null != tgpj_BuildingAccount.getAllocableAmount() && tgpj_BuildingAccount.getAllocableAmount() > 0)
    		{
    			releaseTheAmount = tgpj_BuildingAccount.getAllocableAmount();
    		}
    		
    		Double buildProjectPaid2 = guaranteeChildForm.getBuildProjectPaid();
    		Double buildProjectPay2 = guaranteeChildForm.getBuildProjectPay();
    		if(null == buildProjectPaid2)
    		{
    			buildProjectPaid2 = 0.00;
    		}
    		if(null == buildProjectPay2)
    		{
    			buildProjectPay2 = 0.00;
    		}
    		
    		if((buildProjectPaid2 + buildProjectPay2) <= 0.00)
    		{
    			return MyBackInfo.fail(properties, "请输入楼幢建设项目已实际支付金额或楼幢项目建设待支付承保金额");
    		}
    			    		
    		//现金受限额度
    		Double cashLimitedAmount = 0.0;
    		Double buildProjectPaid = 0.0;
    		Double buildProjectPay = 0.0;
    		
    		// 用户输入字段，展示是为 0.0 
    		if( null == guaranteeChildForm.getBuildProjectPaid())
    		{
    			buildProjectPaid = 0.0;
    		}
    		else
    		{
    			buildProjectPaid = guaranteeChildForm.getBuildProjectPaid();//楼幢项目建设已实际支付金额   			
    		}
    		
    		if( null == guaranteeChildForm.getBuildProjectPay())
    		{
    			buildProjectPay = 0.0;
    		}
    		else
    		{
    			buildProjectPay = guaranteeChildForm.getBuildProjectPay();//楼幢项目建设待支付承保金额（元）
    		}
    		
 
    		BigDecimal data1 = new BigDecimal(buildProjectPaid); 
    		BigDecimal data2 = new BigDecimal(buildProjectPaid);
    		BigDecimal data3 = new BigDecimal(0.0); 
    		int result = data1.compareTo(data2);
    		int result2 = data1.compareTo(data3);
    		/*if  (result == 0 && result2 == 0) //  为0
    		{
    			return MyBackInfo.fail(properties, "请输入楼幢项目建设已实际支付金额或楼幢项目建设待支付承保金额！");
    		}*/
    		
    		Double amountGuaranteed = 0.0;//已落实支付保证金额（元）
    			
    		//支付保证封顶额度 = 初始受限额度*支付保证封顶百分比
    		paymentProportion = myDouble.doubleMultiplyDouble(paymentLines, orgLimitedAmount) / 100;
    		
//			已落实支付保证金额（元）=楼幢项目建设已实际支付金额+楼幢项目建设待支付承保金额
    		amountGuaranteed = myDouble.doubleAddDouble(buildProjectPaid, buildProjectPay);
    		
    		//已落实支付保证累计金额（元） = 已落实支付保证累计金额（元） + 已落实支付保证金额（元）
    		totalAmountGuaranteed = myDouble.doubleAddDouble(totalAmountGuaranteed, amountGuaranteed);
    		
    		//现金受限额度 = 初始受限额度-已落实支付保证累计金额-已落实支付保证金额
    		cashLimitedAmount = myDouble.doubleSubtractDouble(orgLimitedAmount, totalAmountGuaranteed);
    		
    		//楼幢项目建设已实际支付金额（元）
    		buildAmountPaid = myDouble.doubleAddDouble(buildProjectPaid, buildAmountPaid);
    		
    		//楼幢项目建设待支付承保累计金额（元）
    		buildAmountPay = myDouble.doubleAddDouble(buildProjectPay, buildAmountPay);;
    		
    		//有效受限额度（元）= 现金受限额度与当前节点受限额度的最小值
    		if( cashLimitedAmount < nodeLimitedAmount)
    		{
    			effectiveLimitedAmount = cashLimitedAmount;
    		}
    		else
    		{
    			effectiveLimitedAmount = nodeLimitedAmount;
    		}
    		
    		// 托管余额 = 总入账金额  - 已拨付 - 退房退款
    		Double currentEscrowFund = myDouble.doubleSubtractDouble( totalAccountAmount , myDouble.doubleAddDouble(payoutAmount , refundAmount));
    		

    		// 托管余额 > 有效受限额度 ，计算溢出金额和可拨付金额。否则 溢出金额和可拨付不变
    		if (currentEscrowFund > effectiveLimitedAmount)
    		{
    			// 溢出金额 = 总入账金额 - 有效受限额度 - 已拨付金额 - 退房退款 = 托管余额 - 有效受限额度
    			// 溢出金额 > = 0
    			spilloverAmount = myDouble.doubleSubtractDouble( currentEscrowFund , effectiveLimitedAmount) ;
    			
    			// 可拨付金额（元）= 总入账金额-有效受限额度-已拨付金额-拨付冻结金额-退款冻结金额 = 溢出金额 -拨付冻结金额-退款冻结金额 
    			releaseTheAmount = myDouble.doubleSubtractDouble( spilloverAmount , myDouble.doubleAddDouble(appropriateFrozenAmount, appliedNoPayoutAmount));
    		}
    		
    		//已落实支付保证金额需要小于等于初始受限额度*支付保证封顶比例
    		/*if( amountGuaranteed > paymentProportion)
    		{
    			return MyBackInfo.fail(properties, "已落实支付保证金额需要小于等于初始受限额度*支付保证封顶比例！");
    		}*/
    		
    		empj_PaymentGuaranteeDao.save(empj_PaymentGuarantee);
			
			// 存在记录就修改
			if( null != childId && childId > 0)
			{
				empj_PaymentGuaranteeChild = (Empj_PaymentGuaranteeChild)empj_PaymentGuaranteeChildDao.findById(childId);
								
			
			}
			else
			{
				
	    		empj_PaymentGuaranteeChild.setUserStart(user);
	    		empj_PaymentGuaranteeChild.setCreateTimeStamp(System.currentTimeMillis());
	    		
	    		empj_PaymentGuaranteeChild.setEmpj_BuildingInfo(empj_BuildingInfo);	
	    		empj_PaymentGuaranteeChild.seteCodeFromConstruction(eCodeFromConstruction1); // 施工编号
	    		empj_PaymentGuaranteeChild.seteCodeFromPublicSecurity(eCodeFromPublicSecurity);// 公安
	    		empj_PaymentGuaranteeChild.setBuildingArea(buildingArea);// 建筑面积
	    		empj_PaymentGuaranteeChild.setEscrowArea(escrowArea);//托管面积（㎡）
	    		empj_PaymentGuaranteeChild.setRecordAvgPriceOfBuilding(recordAvgPriceOfBuilding);//楼幢住宅备案均价
	    		empj_PaymentGuaranteeChild.setEscrowStandard(escrowStandard);//托管标准
	    		empj_PaymentGuaranteeChild.setOrgLimitedAmount(orgLimitedAmount);//初始受限额度
	    		empj_PaymentGuaranteeChild.setPaymentProportion(paymentProportion);// 支付保证封顶比例
	    		empj_PaymentGuaranteeChild.setPaymentLines(paymentLines);//支付保证封顶额度
	    		empj_PaymentGuaranteeChild.setCurrentFigureProgress(currentFigureProgress);//当前形象进度	
	    		empj_PaymentGuaranteeChild.setCurrentLimitedRatio(currentLimitedRatio);//当前受限比例（%）
	    		empj_PaymentGuaranteeChild.setNodeLimitedAmount(nodeLimitedAmount);//当前节点受限额度（元）
	    		empj_PaymentGuaranteeChild.setTotalAccountAmount(totalAccountAmount);//总入账金额（元）
	    		empj_PaymentGuaranteeChild.setPayoutAmount(payoutAmount);//已拨付金额
	    		empj_PaymentGuaranteeChild.setAppropriateFrozenAmount(appropriateFrozenAmount);//拨付冻结金额
	    		empj_PaymentGuaranteeChild.setAppliedNoPayoutAmount(appliedNoPayoutAmount);//退款冻结金额
	    		empj_PaymentGuaranteeChild.setEmpj_PaymentGuarantee(empj_PaymentGuarantee);
	    			    		
			}
			
    		empj_PaymentGuaranteeChild.setTheState(S_TheState.Normal);
    		empj_PaymentGuaranteeChild.setUserUpdate(user);
    		empj_PaymentGuaranteeChild.setLastUpdateTimeStamp(System.currentTimeMillis());
    		
    		empj_PaymentGuaranteeChild.setBuildAmountPaid(buildAmountPaid);//楼幢项目建设已实际支付金额（元）
    		empj_PaymentGuaranteeChild.setBuildAmountPay(buildAmountPay);//楼幢项目建设待支付承保累计金额（元）
    		empj_PaymentGuaranteeChild.setTotalAmountGuaranteed(totalAmountGuaranteed);//已落实支付保证累计金额（元）
    		empj_PaymentGuaranteeChild.setBuildProjectPaid(buildProjectPaid);//楼幢项目建设已实际支付金额 
    		empj_PaymentGuaranteeChild.setBuildProjectPay(buildProjectPay);//楼幢项目建设待支付承保金额（元）
    		empj_PaymentGuaranteeChild.setAmountGuaranteed(amountGuaranteed);//已落实支付保证金额（元）
    		empj_PaymentGuaranteeChild.setCashLimitedAmount(cashLimitedAmount); //现金受限额度（元）
    		empj_PaymentGuaranteeChild.setEffectiveLimitedAmount(effectiveLimitedAmount);//有效受限额度（元）
    		empj_PaymentGuaranteeChild.setSpilloverAmount(spilloverAmount);//溢出金额
    		empj_PaymentGuaranteeChild.setReleaseTheAmount(releaseTheAmount);// 释放金额
    		empj_PaymentGuaranteeChild.setEmpj_PaymentGuarantee(empj_PaymentGuarantee);
    		
    		if (amountGuaranteed > paymentProportion) {
				empj_PaymentGuaranteeChild.setAmountGuaranteed(paymentProportion);// 已落实支付保证金额（元）
			} else {
				empj_PaymentGuaranteeChild.setAmountGuaranteed(amountGuaranteed);// 已落实支付保证金额（元）
			}
    		  		
    		empj_PaymentGuaranteeChildDao.save(empj_PaymentGuaranteeChild);
    		
    		
    	}
		

		/*
		 * 修改附件
		 * 附件需要先进行删除操作，然后进行重新上传保存功能
		 */
		// 附件信息
		String smAttachmentJson = null;
		if (null != model.getSmAttachmentList() && model.getSmAttachmentList().length() > 0)
		{
			// 根据退房退款ID进行查询附件功能
			Sm_AttachmentForm from = new Sm_AttachmentForm();

			String sourceId = String.valueOf(empj_PaymentGuaranteetableId);
			from.setTheState(S_TheState.Normal);
			from.setSourceId(sourceId);

			// 查询附件
			@SuppressWarnings("unchecked")
			List<Sm_Attachment> smAttachmentList = smAttachmentDao
					.findByPage(smAttachmentDao.getQuery(smAttachmentDao.getBasicHQL2(), from));
			// 删除附件
			if (null != smAttachmentList && smAttachmentList.size() > 0)
			{
				for (Sm_Attachment sm_Attachment : smAttachmentList)
				{
					sm_Attachment.setTheState(S_TheState.Deleted);
					smAttachmentDao.save(sm_Attachment);
				}
			}

			// 重新保存附件
			smAttachmentJson = model.getSmAttachmentList().toString();
			List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentJson, Sm_Attachment.class);

			if (null != gasList && gasList.size() > 0)
			{
				for (Sm_Attachment sm_Attachment : gasList)
				{
					//查询附件配置表
					Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
					form.seteCode(sm_Attachment.getSourceType());
					Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));
					
					sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);
					sm_Attachment.setSourceId(empj_PaymentGuaranteetableId.toString());
					sm_Attachment.setTheState(S_TheState.Normal);
					smAttachmentDao.save(sm_Attachment);
				}
			}
		}		
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
	
		return properties;
		
	}


}
