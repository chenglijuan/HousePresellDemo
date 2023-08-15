package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeChildForm;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFDtlForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeChildDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PaymentGuaranteeChild;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_EscrowState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_StoragePeriodCompanyType;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：楼幢-户室
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_PaymentGuaranteeApplyDetailListService
{
	@Autowired
	private Empj_PaymentGuaranteeDao empj_PaymentGuaranteeDao;
	@Autowired
	private Empj_PaymentGuaranteeChildDao empj_PaymentGuaranteeChildDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Tgpf_FundAppropriated_AFDtlDao tgpf_FundAppropriated_AFDtlDao;
	
	MyDouble myDouble = MyDouble.getInstance();
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_PaymentGuaranteeForm model)
	{
		Properties properties = new MyProperties();
	
		List<Empj_PaymentGuaranteeChild> empj_PaymentGuaranteeChildList = new ArrayList<Empj_PaymentGuaranteeChild>();
			
		Empj_ProjectInfo empj_ProjectInfo = new Empj_ProjectInfo();
		// 根据项目查询
		if(null !=  model.getProjectId() && model.getProjectId() > 0)
		{
			Long empj_ProjectInfoId = model.getProjectId();
			empj_ProjectInfo = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(empj_ProjectInfoId);
			if(empj_ProjectInfo == null || S_TheState.Deleted.equals(empj_ProjectInfo.getTheState()))
			{
				return MyBackInfo.fail(properties, "项目信息有误！");
			}
		}
		
		Empj_BuildingInfoForm empj_BuildingInfoForm = new Empj_BuildingInfoForm();
		empj_BuildingInfoForm.setProject(empj_ProjectInfo);
		empj_BuildingInfoForm.setTheState(S_TheState.Normal);
		empj_BuildingInfoForm.setBusiState(S_BusiState.HaveRecord);
		empj_BuildingInfoForm.setEscrowState(S_EscrowState.HasEscrowState);
		
//		Integer buildingCount = empj_BuildingInfoDao.findByPage_Size(empj_BuildingInfoDao.getQuery_Size(empj_BuildingInfoDao.getBasicHQL(), empj_BuildingInfoForm));
		Integer buildingCount = empj_BuildingInfoDao.findByPage_Size(empj_BuildingInfoDao.getCriteriaBasicHQL(empj_BuildingInfoForm));
		List<Empj_BuildingInfo> empj_BuildingInfoList;
		if(buildingCount > 0)
		{
//			empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), empj_BuildingInfoForm));
			empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getCriteriaBasicHQL(empj_BuildingInfoForm));
			
			for(Empj_BuildingInfo empj_BuildingInfo : empj_BuildingInfoList)
			{
				Empj_PaymentGuaranteeChildForm empj_PaymentGuaranteeChildForm = new Empj_PaymentGuaranteeChildForm();
				empj_PaymentGuaranteeChildForm.setEmpj_BuildingInfo(empj_BuildingInfo);
				empj_PaymentGuaranteeChildForm.setTheState(S_TheState.Normal);
				String condition = "('0','1','3')";
				empj_PaymentGuaranteeChildForm.setCondition(condition);
				
				Integer childCount = empj_PaymentGuaranteeChildDao.findByPage_Size(empj_PaymentGuaranteeChildDao.getQuery_Size(empj_PaymentGuaranteeChildDao.getSpeicalHQL(), empj_PaymentGuaranteeChildForm));
		
				if( childCount > 0)
				{
					continue;
				}
				
				Tgpf_FundAppropriated_AFDtlForm tgpf_FundAppropriated_AFDtlForm = new Tgpf_FundAppropriated_AFDtlForm();
				tgpf_FundAppropriated_AFDtlForm.setBuilding(empj_BuildingInfo);
				tgpf_FundAppropriated_AFDtlForm.setPayoutStatement("(1,2,3,4,5)");
				tgpf_FundAppropriated_AFDtlForm.setTheState(S_TheState.Normal);
							
				Integer payBuildingCount = tgpf_FundAppropriated_AFDtlDao.findByPage_Size(tgpf_FundAppropriated_AFDtlDao.getQuery_Size(tgpf_FundAppropriated_AFDtlDao.getSpecialHQL(), tgpf_FundAppropriated_AFDtlForm));

				if( payBuildingCount > 0 )
				{
					continue;
				}

			
				Empj_PaymentGuaranteeChild empj_PaymentGuaranteeChild = new Empj_PaymentGuaranteeChild();
    		
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
	    		
	    		Double refundAmount = 0.0;//已退款金额
	    		if( null != tgpj_BuildingAccount.getRefundAmount() && tgpj_BuildingAccount.getRefundAmount() > 0)
	    		{
	    			refundAmount = tgpj_BuildingAccount.getRefundAmount();
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
	    		
	    		//退房退款未拨付金额（元）
	    		Double applyRefundPayoutAmount = 0.0;// 可拨付金额
	    		if( null != tgpj_BuildingAccount.getApplyRefundPayoutAmount() && tgpj_BuildingAccount.getApplyRefundPayoutAmount() > 0)
	    		{
	    			applyRefundPayoutAmount = tgpj_BuildingAccount.getApplyRefundPayoutAmount();
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
	    		
	    		//释放金额（元）
	    		Double releaseTheAmount = 0.0;// 可拨付金额
	    		if( null != tgpj_BuildingAccount.getAllocableAmount() && tgpj_BuildingAccount.getAllocableAmount() > 0)
	    		{
	    			releaseTheAmount = tgpj_BuildingAccount.getAllocableAmount();
	    		}
	    			    		
	    		//现金受限额度
	    		Double cashLimitedAmount = 0.0;
	    			    		
	    		// 用户输入字段，展示是为 0.0 
	    		Double buildProjectPaid = 0.0;//楼幢项目建设已实际支付金额 	    		
	    		Double buildProjectPay = 0.0;//楼幢项目建设待支付承保金额（元）	    		
	    		Double amountGuaranteed = 0.0;//已落实支付保证金额（元）
	    			
	    		//支付保证封顶额度 = 初始受限额度*支付保证封顶百分比
	    		paymentProportion = myDouble.doubleMultiplyDouble(paymentLines, orgLimitedAmount)/100;
	    		
//				已落实支付保证金额（元）=楼幢项目建设已实际支付金额+楼幢项目建设待支付承保金额
//	    		amountGuaranteed = buildProjectPaid + buildProjectPay;
	    		
	    		//现金受限额度 = 初始受限额度-已落实支付保证累计金额-已落实支付保证金额
	    		cashLimitedAmount = myDouble.doubleSubtractDouble(orgLimitedAmount, myDouble.doubleAddDouble(amountGuaranteed, totalAmountGuaranteed));

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
	    		
	    		if( currentEscrowFund < 0 )
	    		{
	    			currentEscrowFund = 0.0;
	    			
	    			spilloverAmount = 0.0;
	    			
	    			releaseTheAmount = 0.0;
	    		}
	    		else
	    		{
	    			// 托管余额 > 有效受限额度 ，计算溢出金额和可拨付金额。否则 溢出金额和可拨付不变
	    			if (currentEscrowFund > effectiveLimitedAmount)
	    			{
	    				// 溢出金额 = 总入账金额 - 有效受限额度 - 已拨付金额 - 退房退款 = 托管余额 - 有效受限额度
	    				// 溢出金额 > = 0
	    				spilloverAmount = myDouble.doubleSubtractDouble( currentEscrowFund , effectiveLimitedAmount) ;
	    				
	    				if( spilloverAmount < 0 )
	    				{
	    					spilloverAmount = 0.0;
	    				}
	    				
	    				// 可拨付金额（元）= 总入账金额-有效受限额度-已拨付金额-拨付冻结金额-退款冻结金额 = 溢出金额 -拨付冻结金额-退款冻结金额 
	    				releaseTheAmount = myDouble.doubleSubtractDouble( spilloverAmount , myDouble.doubleAddDouble(applyRefundPayoutAmount, appliedNoPayoutAmount));
	    				
	    				if( releaseTheAmount < 0 )
	    				{
	    					releaseTheAmount = 0.0;
	    				}
	    			}
	    			else
	    			{
	    				spilloverAmount = 0.0;
	    				
	    				releaseTheAmount = 0.0;
	    			}
	    		}

	    		
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
	    		empj_PaymentGuaranteeChild.setBuildAmountPaid(buildAmountPaid);//楼幢项目建设已实际支付金额（元）
	    		empj_PaymentGuaranteeChild.setBuildAmountPay(buildAmountPay);//楼幢项目建设待支付承保累计金额（元）
	    		empj_PaymentGuaranteeChild.setTotalAmountGuaranteed(totalAmountGuaranteed);//已落实支付保证累计金额（元）
	    		empj_PaymentGuaranteeChild.setBuildProjectPaid(buildProjectPaid);//楼幢项目建设已实际支付金额 
	    		empj_PaymentGuaranteeChild.setBuildProjectPay(buildProjectPay);//楼幢项目建设待支付承保金额（元）
	    		empj_PaymentGuaranteeChild.setAmountGuaranteed(amountGuaranteed);//已落实支付保证金额（元）
	    		empj_PaymentGuaranteeChild.setCashLimitedAmount(cashLimitedAmount); //现金受限额度（元）
	    		empj_PaymentGuaranteeChild.setCurrentFigureProgress(currentFigureProgress);//当前形象进度	
	    		empj_PaymentGuaranteeChild.setCurrentLimitedRatio(currentLimitedRatio);//当前受限比例（%）
	    		empj_PaymentGuaranteeChild.setNodeLimitedAmount(nodeLimitedAmount);//当前节点受限额度（元）
	    		empj_PaymentGuaranteeChild.setEffectiveLimitedAmount(effectiveLimitedAmount);//有效受限额度（元）
	    		empj_PaymentGuaranteeChild.setTotalAccountAmount(totalAccountAmount);//总入账金额（元）
	    		empj_PaymentGuaranteeChild.setPayoutAmount(payoutAmount);//已拨付金额
	    		empj_PaymentGuaranteeChild.setSpilloverAmount(spilloverAmount);//溢出金额
	    		empj_PaymentGuaranteeChild.setAppropriateFrozenAmount(appropriateFrozenAmount);//拨付冻结金额
	    		empj_PaymentGuaranteeChild.setAppliedNoPayoutAmount(appliedNoPayoutAmount);//退款冻结金额
	    		empj_PaymentGuaranteeChild.setReleaseTheAmount(releaseTheAmount);// 释放金额
	    		
				empj_PaymentGuaranteeChildList.add(empj_PaymentGuaranteeChild);
			}		
		}
		else
		{
			empj_BuildingInfoList = new ArrayList<Empj_BuildingInfo>();
		}
			
		properties.put("empj_PaymentGuaranteeChildList", empj_PaymentGuaranteeChildList);	
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
