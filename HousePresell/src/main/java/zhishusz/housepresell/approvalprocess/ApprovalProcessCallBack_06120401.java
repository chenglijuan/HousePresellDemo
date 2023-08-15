package zhishusz.housepresell.approvalprocess;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeChildForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountLogForm;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeChildDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountLogDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Empj_PaymentGuaranteeChild;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.external.service.Empj_PaymentGuaranteeInsertInterfaceService;
import zhishusz.housepresell.service.Tgpj_BuildingAccountLimitedUpdateService;
import zhishusz.housepresell.util.MyBackInfo;

/*
 * Service 审批流提交 操作：退房退款-贷款已结清 
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class ApprovalProcessCallBack_06120401 implements IApprovalProcessCallback
{
	private static final Double DELIVERYTYPE1 = 4000.00;// 毛坯房
	private static final Double DELIVERYTYPE2 = 6000.00;// 成品房
	@Autowired
	private Empj_PaymentGuaranteeDao empj_PaymentGuaranteeDao;
	@Autowired
	private Empj_PaymentGuaranteeChildDao empj_PaymentGuaranteeChildDao;
	@Autowired
	private Tgpj_BuildingAccountLogDao tgpj_BuildingAccountLogDao;
	@Autowired
	private Tgpj_BuildingAccountLimitedUpdateService tgpj_BuildingAccountLimitedUpdateService;
	@Autowired
	private Empj_PaymentGuaranteeInsertInterfaceService insertInterfaceService;

	private static final String BUSI_CODE = "06120401";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	
	@Override
	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm)
	{
		Properties properties = new Properties();

		String workflowEcode = approvalProcessWorkflow.geteCode();
		// String workflowName = approvalProcessWorkflow.getTheName();
		
		//获取当前操作人（备案人）
		Sm_User user = baseForm.getUser();

		// 获取正在处理的申请单
		Sm_ApprovalProcess_AF approvalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();

		// 获取正在处理的申请单所属的流程配置
		Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = approvalProcess_AF.getConfiguration();
		String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;
		
		// 获取正在审批的三方协议
		Long sourceId = approvalProcess_AF.getSourceId();

		if (null == sourceId || sourceId < 1)
		{
			return MyBackInfo.fail(properties, "获取的申请单主键为空");
		}
		
		Empj_PaymentGuarantee empj_PaymentGuarantee = (Empj_PaymentGuarantee)empj_PaymentGuaranteeDao.findById(sourceId);
		if(empj_PaymentGuarantee == null || S_TheState.Deleted.equals(empj_PaymentGuarantee.getTheState()))
		{
			return MyBackInfo.fail(properties, "保证申请有误！");
		}
		
		// 驳回到发起人，发起人撤回
		if (S_ApprovalState.WaitSubmit.equals(approvalProcess_AF.getBusiState()))
		{
			empj_PaymentGuarantee.setBusiState("0");
			empj_PaymentGuarantee.setApprovalState(S_ApprovalState.WaitSubmit);

			empj_PaymentGuaranteeDao.save(empj_PaymentGuarantee);
			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		}

		// 不通过
		if (S_ApprovalState.NoPass.equals(approvalProcess_AF.getBusiState()))
		{
			empj_PaymentGuarantee.setBusiState("0");
			empj_PaymentGuarantee.setApprovalState(S_ApprovalState.NoPass);

			empj_PaymentGuaranteeDao.save(empj_PaymentGuarantee);
			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		}

//		if (approvalProcessWork.equals("06120401_4"))
		if (approvalProcessWork.equals("06120401001_ZS"))
		{
			if (S_ApprovalState.Completed.equals(approvalProcess_AF.getBusiState())
					&& S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
			{
				empj_PaymentGuarantee.setBusiState("2");
				empj_PaymentGuarantee.setUserRecord(user);
				empj_PaymentGuarantee.setRecordTimeStamp(System.currentTimeMillis());
				empj_PaymentGuarantee.setUserRecord(approvalProcess_AF.getUserStart());
				
				empj_PaymentGuaranteeDao.save(empj_PaymentGuarantee);
				
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
						
						empj_PaymentGuaranteeChild.setRecordTimeStamp(System.currentTimeMillis());
						empj_PaymentGuaranteeChild.setUserRecord(approvalProcess_AF.getUserStart());
						empj_PaymentGuaranteeChild.setEmpj_PaymentGuarantee(empj_PaymentGuarantee);
						empj_PaymentGuaranteeChild.setBusiState("2");
						
						empj_PaymentGuaranteeChildDao.save(empj_PaymentGuaranteeChild);
						
						// 查询需要修改的楼幢账户
						Tgpj_BuildingAccount tgpj_BuildingAccount = empj_BuildingInfo.getBuildingAccount();
						/*//TODO begin
						String theType = empj_BuildingInfo.getDeliveryType();
						if (theType == null)
						{
							return MyBackInfo.fail(properties, "楼幢类型未选择，操作失败");
						}
						// 托管面积
						Double escrowArea = 0.00;
						escrowArea = empj_BuildingInfo.getEscrowArea();
						// 楼幢住宅备案均价
						Double priceOfBuilding = 0.00;
						priceOfBuilding = tgpj_BuildingAccount.getRecordAvgPriceOfBuilding();
						if (null == priceOfBuilding)
						{
							priceOfBuilding = 0.00;
						}
						Tgpj_EscrowStandardVerMng escrowStandardVerMng = empj_BuildingInfo.getEscrowStandardVerMng();
						// 托管标准类型 (枚举选择:1-标准金额 2-标准比例)
						if (S_EscrowStandardType.StandardAmount.equals(escrowStandardVerMng.getTheType()))
						{
							if (null == escrowStandardVerMng.getAmount())
							{
								tgpj_BuildingAccount.setOrgLimitedAmount(0.00);
							}
							else
							{
								// 初始受限额度 = 托管面积 * 标准金额(如果其中有一个值为0，则保存为0)
								if (escrowArea <= 0.00 || escrowStandardVerMng.getAmount() <= 0.00)
									tgpj_BuildingAccount.setOrgLimitedAmount(0.00);

								Double orgLimitedAmount = MyDouble.getInstance().doubleMultiplyDouble(escrowArea,
										escrowStandardVerMng.getAmount());
								tgpj_BuildingAccount.setOrgLimitedAmount(orgLimitedAmount);

							}
						}
						else if (S_EscrowStandardType.StandardPercentage.equals(escrowStandardVerMng.getTheType()))
						{
							// 初始受限额度 = 托管面积 * 楼幢备案均价 * 受限比例
							if (null == escrowStandardVerMng.getPercentage())
							{
								tgpj_BuildingAccount.setOrgLimitedAmount(0.00);
							}
							else
							{
								// 初始受限额度 = 托管面积 * 楼幢备案均价 *
								// 受限比例(如果有一个值为0，则直接为0)
								if (priceOfBuilding <= 0 || escrowArea <= 0
										|| escrowStandardVerMng.getPercentage() <= 0)
									tgpj_BuildingAccount.setOrgLimitedAmount(0.00);

								
								 * 标准比例为30%时，计算 楼幢备案均价 * 受限比例
								 * 楼幢为成品房 6000 2 deliveryType
								 * 楼幢为毛坯房 4000 1 deliveryType
								 
								Double percentage = escrowStandardVerMng.getPercentage();// 比例
								Double doubleTage = percentage * priceOfBuilding / 100;// 楼幢备案均价
																						// *
																						// 受限比例
								Double double2 = 0.00;// 初始受限额度
								if ((percentage - 30) == 0)
								{
									theType = empj_BuildingInfo.getDeliveryType();
									if ("1".equals(theType))
									{// 毛坯房
										if (DELIVERYTYPE1 - doubleTage < 0)
										{
											double2 = escrowArea * DELIVERYTYPE1;
										}
										else
										{
											double2 = escrowArea * doubleTage;
										}
									}
									else
									{// 成品房
										if (DELIVERYTYPE2 - doubleTage < 0)
										{
											double2 = escrowArea * DELIVERYTYPE2;
										}
										else
										{
											double2 = escrowArea * doubleTage;
										}
									}
								}
								else
								{
									double2 = escrowArea * percentage * priceOfBuilding / 100;
								}

								tgpj_BuildingAccount.setOrgLimitedAmount(double2);

							}

						}
						//TODO end
*/						
						Long accountVersion = tgpj_BuildingAccount.getVersionNo();
						if( null == tgpj_BuildingAccount.getVersionNo() || tgpj_BuildingAccount.getVersionNo() < 0)
						{
							accountVersion = 1l;
						}
						
						// 根据楼幢账户查询版本号最大的楼幢账户log表
						// 查询条件：1.业务编码 2.楼幢账户 3.关联主键 4.根据版本号大小排序
						Tgpj_BuildingAccountLogForm tgpj_BuildingAccountLogForm = new Tgpj_BuildingAccountLogForm();
						tgpj_BuildingAccountLogForm.setTheState(S_TheState.Normal);
						tgpj_BuildingAccountLogForm.setRelatedBusiCode(BUSI_CODE);
						tgpj_BuildingAccountLogForm.setTgpj_BuildingAccount(tgpj_BuildingAccount);
						tgpj_BuildingAccountLogForm.setRelatedBusiTableId(empj_PaymentGuaranteeChild.getTableId());
					
						Integer logCount = tgpj_BuildingAccountLogDao.findByPage_Size(tgpj_BuildingAccountLogDao.getQuery_Size(tgpj_BuildingAccountLogDao.getSpecialHQL(), tgpj_BuildingAccountLogForm));
						
						List<Tgpj_BuildingAccountLog> tgpj_BuildingAccountLogList;
						if(logCount > 0)
						{
							tgpj_BuildingAccountLogList = tgpj_BuildingAccountLogDao.findByPage(tgpj_BuildingAccountLogDao.getQuery(tgpj_BuildingAccountLogDao.getBasicHQL(), tgpj_BuildingAccountLogForm));
							
							Tgpj_BuildingAccountLog buildingAccountLog = tgpj_BuildingAccountLogList.get(0);
							// 获取日志表的版本号
							Long logVersionNo = buildingAccountLog.getVersionNo();
							if( null == buildingAccountLog.getVersionNo() || buildingAccountLog.getVersionNo() < 0)
							{
								logVersionNo = 1l;
							}
							
							if(logVersionNo == accountVersion)
							{
								tgpj_BuildingAccountLimitedUpdateService.execute(buildingAccountLog);
							}
							else if(logVersionNo < accountVersion)
							{						
								// 不发生修改的字段
								Tgpj_BuildingAccountLog tgpj_BuildingAccountLog = new Tgpj_BuildingAccountLog();
								tgpj_BuildingAccountLog.setBldLimitAmountVerDtl(tgpj_BuildingAccount.getBldLimitAmountVerDtl());
								tgpj_BuildingAccountLog.setTheState(S_TheState.Normal);
								tgpj_BuildingAccountLog.setBusiState(tgpj_BuildingAccount.getBusiState());
								tgpj_BuildingAccountLog.seteCode(tgpj_BuildingAccount.geteCode());
								tgpj_BuildingAccountLog.setUserStart(tgpj_BuildingAccount.getUserStart());
								tgpj_BuildingAccountLog.setCreateTimeStamp(tgpj_BuildingAccount.getCreateTimeStamp());
								tgpj_BuildingAccountLog.setUserUpdate(tgpj_BuildingAccount.getUserUpdate());
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
								tgpj_BuildingAccountLog.setPaymentLines(tgpj_BuildingAccount.getPaymentLines());
								tgpj_BuildingAccountLog.setRelatedBusiCode("06120401");
								tgpj_BuildingAccountLog.setRelatedBusiTableId(empj_PaymentGuaranteeChild.getTableId());
								tgpj_BuildingAccountLog.setTgpj_BuildingAccount(tgpj_BuildingAccount);
								tgpj_BuildingAccountLog.setVersionNo(tgpj_BuildingAccount.getVersionNo());
								
								// 修改产生了变更的字段
								tgpj_BuildingAccountLog.setPaymentProportion(empj_PaymentGuaranteeChild.getPaymentProportion());
								tgpj_BuildingAccountLog.setBuildAmountPaid(empj_PaymentGuaranteeChild.getBuildAmountPaid());
								tgpj_BuildingAccountLog.setBuildAmountPay(empj_PaymentGuaranteeChild.getBuildAmountPay());
								tgpj_BuildingAccountLog.setTotalAmountGuaranteed(empj_PaymentGuaranteeChild.getTotalAmountGuaranteed());
								tgpj_BuildingAccountLog.setCashLimitedAmount(empj_PaymentGuaranteeChild.getCashLimitedAmount());
								tgpj_BuildingAccountLog.setEffectiveLimitedAmount(empj_PaymentGuaranteeChild.getEffectiveLimitedAmount());
								tgpj_BuildingAccountLog.setSpilloverAmount(empj_PaymentGuaranteeChild.getSpilloverAmount());
								tgpj_BuildingAccountLog.setAllocableAmount(empj_PaymentGuaranteeChild.getReleaseTheAmount());
															
								tgpj_BuildingAccountLimitedUpdateService.execute(tgpj_BuildingAccountLog);
							}
							else if(logVersionNo > accountVersion)
							{
								return MyBackInfo.fail(properties, "备案版本存在回档，请核实后重新发起！");
							}
							
							/**
							 * xsz by time 2019-7-8 17:17:43
							 * 与档案系统接口对接
							 * ====================start==================
							 */
							
							Properties execute = insertInterfaceService.execute(empj_PaymentGuarantee, baseForm);
							if(execute.isEmpty()|| S_NormalFlag.fail.equals(execute.get(S_NormalFlag.result)))
							{
								return MyBackInfo.fail(properties, "与档案系统对接失败，请稍后重试！");
							}
							
							/**
							 * xsz by time 2019-7-8 17:17:43
							 * 与档案系统接口对接
							 * ====================end==================
							 */
												
						}
						else
						{
							return MyBackInfo.fail(properties, "存在未申请备案的楼幢，请核实后重新发起！");
						}	
					}
				}
			}
			
			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		}
		else
		{
			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, "没有需要处理的回调");
		}

		return properties;
	}
}
