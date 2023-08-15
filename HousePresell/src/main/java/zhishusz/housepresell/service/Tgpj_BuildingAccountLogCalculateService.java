package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountLogForm;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountLogDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;
import zhishusz.housepresell.database.po.state.S_EscrowStandardType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service 楼幢账户日志表计算公共方法
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BuildingAccountLogCalculateService
{
	private static final Double DELIVERYTYPE1 = 4000.00;// 毛坯房
	private static final Double DELIVERYTYPE2 = 6000.00;// 成品房
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;
	@Autowired
	private Tgpj_BuildingAccountLogDao tgpj_BuildingAccountLogDao;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;
	
	MyDouble myDouble = MyDouble.getInstance();

	public Properties execute(Tgpj_BuildingAccountLog tgpj_BuildingAccountLog)
	{
		Properties properties = new MyProperties();
		
		Double escrowArea = 0.0 ; //托管面积
		if( null != tgpj_BuildingAccountLog.getEscrowArea() && tgpj_BuildingAccountLog.getEscrowArea() > 0)
		{
			escrowArea = tgpj_BuildingAccountLog.getEscrowArea();
		}
		
		Double recordAvgPriceOfBuilding = 0.0 ; //备案均价
		if( null != tgpj_BuildingAccountLog.getRecordAvgPriceOfBuilding() && tgpj_BuildingAccountLog.getRecordAvgPriceOfBuilding() > 0)
		{
			recordAvgPriceOfBuilding = tgpj_BuildingAccountLog.getRecordAvgPriceOfBuilding();
		}
		
		
		Double appliedNoPayoutAmount = 0.0;//退款冻结金额
		if( null != tgpj_BuildingAccountLog.getAppliedNoPayoutAmount() && tgpj_BuildingAccountLog.getAppliedNoPayoutAmount() > 0)
		{
			appliedNoPayoutAmount = tgpj_BuildingAccountLog.getAppliedNoPayoutAmount();
		}
		
		Double refundAmount = 0.0;//已退款金额
		if( null != tgpj_BuildingAccountLog.getRefundAmount() && tgpj_BuildingAccountLog.getRefundAmount() > 0)
		{
			refundAmount = tgpj_BuildingAccountLog.getRefundAmount();
		}
		
		Double orgLimitedAmount = 0.0;//初始受限额度（元）
		if( null != tgpj_BuildingAccountLog.getOrgLimitedAmount() && tgpj_BuildingAccountLog.getOrgLimitedAmount() > 0)
		{
			orgLimitedAmount = tgpj_BuildingAccountLog.getOrgLimitedAmount();
		}
		
		Double paymentLines = 0.0;//支付保证封顶额度
		
		
		if( null != tgpj_BuildingAccountLog.getPaymentLines() && tgpj_BuildingAccountLog.getPaymentLines() > 0)
		{
			paymentLines = tgpj_BuildingAccountLog.getPaymentLines();
		}
		
		Double paymentProportion = 0.0;// 支付保证封顶比例
		if( null != tgpj_BuildingAccountLog.getPaymentProportion() && tgpj_BuildingAccountLog.getPaymentProportion() > 0)
		{
			paymentProportion = tgpj_BuildingAccountLog.getPaymentProportion();
		}	    		
		
		Double buildAmountPaid = 0.0;//楼幢项目建设已实际支付金额（元）
		if( null != tgpj_BuildingAccountLog.getBuildAmountPaid() && tgpj_BuildingAccountLog.getBuildAmountPaid() > 0)
		{
			buildAmountPaid = tgpj_BuildingAccountLog.getBuildAmountPaid();
		}
		
		Double buildAmountPay = 0.0;//楼幢项目建设待支付承保累计金额（元）
		if( null != tgpj_BuildingAccountLog.getBuildAmountPay() && tgpj_BuildingAccountLog.getBuildAmountPay() > 0)
		{
			buildAmountPay = tgpj_BuildingAccountLog.getBuildAmountPay();
		}
	
		Double totalAmountGuaranteed = 0.0;//已落实支付保证累计金额（元）
		if( null != tgpj_BuildingAccountLog.getTotalAmountGuaranteed() && tgpj_BuildingAccountLog.getTotalAmountGuaranteed() > 0)
		{
			totalAmountGuaranteed = tgpj_BuildingAccountLog.getTotalAmountGuaranteed();
		}
		
		Double effectiveLimitedAmount = 0.0;//有效受限额度（元）
		if( null != tgpj_BuildingAccountLog.getEffectiveLimitedAmount() && tgpj_BuildingAccountLog.getEffectiveLimitedAmount() > 0)
		{
			effectiveLimitedAmount = tgpj_BuildingAccountLog.getEffectiveLimitedAmount();
		}
		
		Double totalAccountAmount = 0.0;//总入账金额（元)
		if( null != tgpj_BuildingAccountLog.getTotalAccountAmount() && tgpj_BuildingAccountLog.getTotalAccountAmount() > 0)
		{
			totalAccountAmount = tgpj_BuildingAccountLog.getTotalAccountAmount();
		}
			    		
		String currentFigureProgress = tgpj_BuildingAccountLog.getCurrentFigureProgress();//当前形象进度
		
		Double currentLimitedRatio = 1.0;//当前受限比例（%）
		if( null != tgpj_BuildingAccountLog.getCurrentLimitedRatio())
		{
			currentLimitedRatio = tgpj_BuildingAccountLog.getCurrentLimitedRatio();
		}
		
		Double nodeLimitedAmount = 0.0;////当前节点受限额度（元）
		if( null != tgpj_BuildingAccountLog.getNodeLimitedAmount() && tgpj_BuildingAccountLog.getNodeLimitedAmount() > 0)
		{
			nodeLimitedAmount = tgpj_BuildingAccountLog.getNodeLimitedAmount();
		}
		
		Double payoutAmount = 0.0;//已拨付金额
		if( null != tgpj_BuildingAccountLog.getPayoutAmount() && tgpj_BuildingAccountLog.getPayoutAmount() > 0)
		{
			payoutAmount = tgpj_BuildingAccountLog.getPayoutAmount();
		}
		
		Double appropriateFrozenAmount = 0.0;//拨付冻结金额（元）
		if( null != tgpj_BuildingAccountLog.getAppropriateFrozenAmount() && tgpj_BuildingAccountLog.getAppropriateFrozenAmount() > 0)
		{
			appropriateFrozenAmount = tgpj_BuildingAccountLog.getAppropriateFrozenAmount();
		}
			    		
		Double spilloverAmount = 0.0;// 溢出金额
		if( null != tgpj_BuildingAccountLog.getSpilloverAmount() && tgpj_BuildingAccountLog.getSpilloverAmount() > 0)
		{
			spilloverAmount = tgpj_BuildingAccountLog.getSpilloverAmount();
		}
		
		//释放金额（元）
		Double releaseTheAmount = 0.0;// 可拨付金额
		if( null != tgpj_BuildingAccountLog.getAllocableAmount() && tgpj_BuildingAccountLog.getAllocableAmount() > 0)
		{
			releaseTheAmount = tgpj_BuildingAccountLog.getAllocableAmount();
		}
		
		//退房退款未拨付金额（元）
		Double applyRefundPayoutAmount = 0.0;// 可拨付金额
		if( null != tgpj_BuildingAccountLog.getApplyRefundPayoutAmount() && tgpj_BuildingAccountLog.getApplyRefundPayoutAmount() > 0)
		{
			applyRefundPayoutAmount = tgpj_BuildingAccountLog.getApplyRefundPayoutAmount();
		}
		
		/*Empj_BuildingInfo building = tgpj_BuildingAccountLog.getBuilding();
				
		String deliveryType = building.getDeliveryType();
		
		String standardMoney = "";
		
		if( null != deliveryType && "1".equals(deliveryType) )
		{
			standardMoney = getParameter("毛坯房托管标准","66");
		}
		else if( null != deliveryType && "2".equals(deliveryType) )
		{
			standardMoney = getParameter("成品房托管标准","66");
		}
		
		if( null != building)
		{
			Tgpj_EscrowStandardVerMng escrowStandardVerMng = building.getEscrowStandardVerMng();
			
			// 托管标准类型 (枚举选择:1-标准金额 2-标准比例)
			if(S_EscrowStandardType.StandardAmount.equals(escrowStandardVerMng.getTheType()))
			{
				if(null == escrowStandardVerMng.getAmount())
				{
					
				}
				else
				{
					// 初始受限额度 = 托管面积 * 标准金额
					orgLimitedAmount = 	escrowArea * escrowStandardVerMng.getAmount();
				}
			}
			else if(S_EscrowStandardType.StandardPercentage.equals(escrowStandardVerMng.getTheType()))
			{
				// 初始受限额度 = 托管面积 * 楼幢备案均价 * 受限比例
				if(null == escrowStandardVerMng.getPercentage())
				{
					// 初始受限额度 = 托管面积 * 标准金额
					orgLimitedAmount = escrowArea * escrowStandardVerMng.getPercentage() * recordAvgPriceOfBuilding / 100;
				}
				else
				{
					if("物价备案均价30%".equals(escrowStandardVerMng.getTheName()))
					{
						Long money = Long.parseLong(standardMoney);
						
						if( money < escrowStandardVerMng.getPercentage() * recordAvgPriceOfBuilding / 100 )
						{
							orgLimitedAmount = money * escrowArea;
						}
						else
						{
							// 初始受限额度 = 托管面积 * 标准金额
							orgLimitedAmount = escrowArea * escrowStandardVerMng.getPercentage() * recordAvgPriceOfBuilding / 100;
						}
					}
					else
					{
						// 初始受限额度 = 托管面积 * 标准金额
						orgLimitedAmount = escrowArea * escrowStandardVerMng.getPercentage() * recordAvgPriceOfBuilding / 100;
					}
				}
				
			}
		}*/
		
		Empj_BuildingInfo building = tgpj_BuildingAccountLog.getBuilding();
		
		String deliveryType = building.getDeliveryType();
		
		String standardMoney = "";
		
		if( null != deliveryType && "1".equals(deliveryType) )
		{
			standardMoney = getParameter("毛坯房托管标准","66");
		}
		else if( null != deliveryType && "2".equals(deliveryType) )
		{
			standardMoney = getParameter("成品房托管标准","66");
		}
		
		if( null != building)
		{
			Tgpj_EscrowStandardVerMng escrowStandardVerMng = building.getEscrowStandardVerMng();
			
			// 托管标准类型 (枚举选择:1-标准金额 2-标准比例)
			if(S_EscrowStandardType.StandardAmount.equals(escrowStandardVerMng.getTheType()))
			{
				if(null == escrowStandardVerMng.getAmount())
				{
					orgLimitedAmount = 0.00;
				}
				else
				{
					// 初始受限额度 = 托管面积 * 标准金额
					if (escrowArea <= 0.00 || escrowStandardVerMng.getAmount() <= 0.00)
					{
						orgLimitedAmount = 0.00;
					}
					else
					{
						orgLimitedAmount = 	escrowArea * escrowStandardVerMng.getAmount();
					}
						
				}
			}
			else if(S_EscrowStandardType.StandardPercentage.equals(escrowStandardVerMng.getTheType()))
			{
				// 初始受限额度 = 托管面积 * 楼幢备案均价 * 受限比例
				if(null == escrowStandardVerMng.getPercentage())
				{
					orgLimitedAmount = 0.00;
					// 初始受限额度 = 托管面积 * 标准金额
//					orgLimitedAmount = escrowArea * escrowStandardVerMng.getPercentage() * recordAvgPriceOfBuilding / 100;
				}
				else
				{
//					if("物价备案均价30%".equals(escrowStandardVerMng.getTheName()))
					if(30 - escrowStandardVerMng.getPercentage() == 0)
					{
						Double percentage = escrowStandardVerMng.getPercentage();// 比例
						Double doubleTage = percentage * recordAvgPriceOfBuilding / 100;// 楼幢备案均价 * 受限比例
						if ("1".equals(deliveryType))
						{// 毛坯房
							if (DELIVERYTYPE1 - doubleTage < 0)
							{
								orgLimitedAmount = escrowArea * DELIVERYTYPE1;
							}
							else
							{
								orgLimitedAmount = escrowArea * doubleTage;
							}
						}
						else
						{// 成品房
							if (DELIVERYTYPE2 - doubleTage < 0)
							{
								orgLimitedAmount = escrowArea * DELIVERYTYPE2;
							}
							else
							{
								orgLimitedAmount = escrowArea * doubleTage;
							}
						}
						
						/*Long money = Long.parseLong(standardMoney);
						
						if( money < escrowStandardVerMng.getPercentage() * recordAvgPriceOfBuilding / 100 )
						{
							orgLimitedAmount = money * escrowArea;
						}
						else
						{
							// 初始受限额度 = 托管面积 * 标准金额
							orgLimitedAmount = escrowArea * escrowStandardVerMng.getPercentage() * recordAvgPriceOfBuilding / 100;
						}*/
					}
					else
					{
						// 初始受限额度 = 托管面积 * 标准金额
						orgLimitedAmount = escrowArea * escrowStandardVerMng.getPercentage() * recordAvgPriceOfBuilding / 100;
					}
				}
				
			}
		}
			
		// 当前受限额度 = 初始受限额度  * 当前受限比例 
		nodeLimitedAmount = myDouble.doubleMultiplyDouble(currentLimitedRatio, orgLimitedAmount) / 100;
		
		//现金受限额度
		Double cashLimitedAmount = 0.0;			    		
			
		//支付保证封顶额度 = 初始受限额度*支付保证封顶百分比
		paymentProportion = myDouble.doubleMultiplyDouble(paymentLines, orgLimitedAmount) / 100;
				
		//现金受限额度 = 初始受限额度-已落实支付保证累计金额
		cashLimitedAmount = myDouble.doubleSubtractDouble(orgLimitedAmount, totalAmountGuaranteed);
		

		// 修改
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
	
	
		// 更新楼幢账户log表中的数据(需要重新计算的)
		tgpj_BuildingAccountLog.setPaymentProportion(paymentProportion);
		tgpj_BuildingAccountLog.setOrgLimitedAmount(orgLimitedAmount);
		tgpj_BuildingAccountLog.setNodeLimitedAmount(nodeLimitedAmount);
		tgpj_BuildingAccountLog.setCashLimitedAmount(cashLimitedAmount);//现金受限额度
		tgpj_BuildingAccountLog.setEffectiveLimitedAmount(effectiveLimitedAmount); // 有效受限额度
		tgpj_BuildingAccountLog.setSpilloverAmount(spilloverAmount);// 溢出金额
		tgpj_BuildingAccountLog.setCurrentEscrowFund(currentEscrowFund);// 托管余额
		tgpj_BuildingAccountLog.setAllocableAmount(releaseTheAmount);// 可拨付金额
		

		if( null == tgpj_BuildingAccountLog.getTableId() || tgpj_BuildingAccountLog.getTableId() < 0 )
		{
			// 根据楼幢账户查询版本号最大的楼幢账户log表
			// 查询条件：1.业务编码 2.楼幢账户 3.关联主键 4.根据版本号大小排序
			Tgpj_BuildingAccountLogForm tgpj_BuildingAccountLogForm = new Tgpj_BuildingAccountLogForm();
			tgpj_BuildingAccountLogForm.setTheState(S_TheState.Normal);
			tgpj_BuildingAccountLogForm.setRelatedBusiCode(tgpj_BuildingAccountLog.getRelatedBusiCode());
			tgpj_BuildingAccountLogForm.setTgpj_BuildingAccount(tgpj_BuildingAccountLog.getTgpj_BuildingAccount());
			tgpj_BuildingAccountLogForm.setRelatedBusiTableId(tgpj_BuildingAccountLog.getRelatedBusiTableId());
			tgpj_BuildingAccountLogForm.setVersionNo(tgpj_BuildingAccountLog.getVersionNo());
		
			Integer logCount = tgpj_BuildingAccountLogDao.findByPage_Size(tgpj_BuildingAccountLogDao.getQuery_Size(tgpj_BuildingAccountLogDao.getSpecialHQL(), tgpj_BuildingAccountLogForm));
			
			List<Tgpj_BuildingAccountLog> tgpj_BuildingAccountLogList;
			if(logCount > 0)
			{
				tgpj_BuildingAccountLogList = tgpj_BuildingAccountLogDao.findByPage(tgpj_BuildingAccountLogDao.getQuery(tgpj_BuildingAccountLogDao.getBasicHQL(), tgpj_BuildingAccountLogForm));
				
				Tgpj_BuildingAccountLog buildingAccountLog = tgpj_BuildingAccountLogList.get(0);
				
				tgpj_BuildingAccountLog.setTableId(buildingAccountLog.getTableId());
			}
		}
				
		tgpj_BuildingAccountLogDao.save(tgpj_BuildingAccountLog);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
	
	public String getParameter(String theName,String parametertype)
	{		
		String retParam = "";
		
		Sm_BaseParameterForm sm_BaseParameterForm = new Sm_BaseParameterForm();
		sm_BaseParameterForm.setTheState(0);
		sm_BaseParameterForm.setTheName(theName);
		sm_BaseParameterForm.setParametertype(parametertype);
		
		Integer totalCount = sm_BaseParameterDao.findByPage_Size(sm_BaseParameterDao.getQuery_Size(sm_BaseParameterDao.getBasicHQL(), sm_BaseParameterForm));
	
		List<Sm_BaseParameter> sm_BaseParameterList;
		if(totalCount > 0)
		{
			sm_BaseParameterList = sm_BaseParameterDao.findByPage(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), sm_BaseParameterForm));
			retParam = sm_BaseParameterList.get(0).getTheValue();
		}
		
		return retParam;
	}
}
