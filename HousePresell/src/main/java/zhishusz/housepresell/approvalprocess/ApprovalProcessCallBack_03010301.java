package zhishusz.housepresell.approvalprocess;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountLogDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;
import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_EscrowStandardType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.service.Sm_BusinessCodeGetService;
import zhishusz.housepresell.service.Tgpj_BuildingAccountLimitedUpdateService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.BuildingAccountLogUtil;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

/**
 * 备案均价：
 * 审批过后-业务逻辑处理
 */
@Transactional
public class ApprovalProcessCallBack_03010301 implements IApprovalProcessCallback
{
	@Autowired
	private Tgpj_BuildingAvgPriceDao buildingAvgPriceDao;
	@Autowired
	private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Tgpj_BuildingAccountLogDao tgpj_BuildingAccountLogDao;
	@Autowired
	private Tgpj_BuildingAccountLimitedUpdateService tgpj_BuildingAccountLimitedUpdateService;//公共计算方法
	@Autowired
	private BuildingAccountLogUtil buildingAccountLogUtil;//初始化楼幢账户log

	@Autowired
	private Gson gson;
	private String busiCode = "03010301";
	private static final Double DELIVERYTYPE1 = 4000.00;// 毛坯房
	private static final Double DELIVERYTYPE2 = 6000.00;// 成品房

	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm)
	{
		Properties properties = new MyProperties();

		try
		{
			String workflowEcode = approvalProcessWorkflow.geteCode();

			// 获取正在处理的申请单
			Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();
			System.out.println("sm_ApprovalProcess_AF BusiState is " + sm_ApprovalProcess_AF.getBusiState()
					+ " approvalProcessWorkflow.getBusiState() is " + approvalProcessWorkflow.getBusiState());

			// 获取正在处理的申请单所属的流程配置
			Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = sm_ApprovalProcess_AF.getConfiguration();
			String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;

			// 获取正在审批的楼幢
			// Long empj_BuildingInfoId = sm_ApprovalProcess_AF.getSourceId();
			Long avgPriceId = sm_ApprovalProcess_AF.getSourceId();
			// Empj_BuildingInfo empj_BuildingInfo =
			// empj_BuildingInfoDao.findById(empj_BuildingInfoId);
			Tgpj_BuildingAvgPrice buildingAvgPrice = buildingAvgPriceDao.findById(avgPriceId);

			// empj_BuildingInfoDao.clear(empj_BuildingInfo);

			if (buildingAvgPrice == null)
			{
				return MyBackInfo.fail(properties, "审批的备案均价不存在");
			}

			switch (approvalProcessWork)
			{
			case "03010301001_ZS":// 最后一步的名称
				if (S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState())
						&& S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
				{
					String jsonStr = sm_ApprovalProcess_AF.getExpectObjJson();
					if (jsonStr != null && jsonStr.length() > 0)
					{
						buildingAvgPrice.setApprovalState(S_ApprovalState.Completed);
						buildingAvgPrice.setBusiState(S_BusiState.HaveRecord);
						buildingAvgPrice.setUserUpdate(sm_ApprovalProcess_AF.getUserStart());
						buildingAvgPrice.setLastUpdateTimeStamp(System.currentTimeMillis());
						buildingAvgPrice.setUserRecord(baseForm.getUser());
						buildingAvgPrice.setRecordTimeStamp(System.currentTimeMillis());
						Empj_BuildingInfo buildingInfo = buildingAvgPrice.getBuildingInfo();
						// if (buildingInfo != null) {
						// saveLimitAmount(buildingInfo, buildingAvgPrice);
						// } else {
						// System.out.println("buildingInfo is null");
						// }
						buildingAvgPriceDao.save(buildingAvgPrice);
						
						/**
						 * xsz by time 2019-7-24 17:05:18
						 * TASK# bug 4024 物价备案均价计算金额写入log表与日终结算金额写入冲突
						 */
						Tgpj_BuildingAccountLog initBuildingAccountLog = buildingAccountLogUtil.getInitBuildingAccountLog(buildingInfo, buildingAvgPrice.getTableId(), busiCode);
						if(null == initBuildingAccountLog.getCurrentLimitedRatio())
				    	{
							initBuildingAccountLog.setCurrentLimitedRatio(100d);
				    	}
						initBuildingAccountLog.setRecordAvgPriceOfBuilding(buildingAvgPrice.getRecordAveragePrice());
						initBuildingAccountLog.setRecordAvgPriceOfBuildingFromPresellSystem(buildingAvgPrice.getRecordAveragePriceFromPresellSystem());
						
						tgpj_BuildingAccountLimitedUpdateService.execute1(initBuildingAccountLog);
						
						/*buildingAccountLogUtil.callBackChange(
								buildingInfo, properties, buildingAvgPrice.getTableId(),busiCode, (tgpj_BuildingAccountLog, logForm) -> {tgpj_BuildingAccountLog.setRecordAvgPriceOfBuilding(logForm.getRecordAvgPriceOfBuilding());}
								);*/
					}
				}
				break;
			default:
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "没有需要处理的回调");
			}
		}
		catch (Exception e)
		{

			e.printStackTrace();

			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_BusiError);

		}

		return properties;
	}

	private void saveLimitAmount(Empj_BuildingInfo buildingInfo, Tgpj_BuildingAvgPrice buildingAvgPrice)
	{
		Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();
		if (buildingAccount != null)
		{
			// Empj_BldLimitAmount_AF empj_bldLimitAmount_af = new
			// Empj_BldLimitAmount_AF();
			// empj_bldLimitAmount_af.setTheState(S_TheState.Normal);
			// empj_bldLimitAmount_af.seteCode(sm_BusinessCodeGetService.execute("03030101"));
			// empj_bldLimitAmount_af.setBusiState(S_BusiState.HaveRecord);
			// empj_bldLimitAmount_af.setApprovalState(S_ApprovalState.Completed);
			// empj_bldLimitAmount_af.setUserStart(buildingAvgPrice.getUserStart());
			// empj_bldLimitAmount_af.setCreateTimeStamp(System.currentTimeMillis());
			// empj_bldLimitAmount_af.setUserUpdate(buildingAvgPrice.getUserStart());
			// empj_bldLimitAmount_af.setLastUpdateTimeStamp(System.currentTimeMillis());
			// empj_bldLimitAmount_af.setUserRecord(baseForm.getUser());
			// empj_bldLimitAmount_af.setRecordTimeStamp(System.currentTimeMillis());
			// empj_bldLimitAmount_af.setBuilding(buildingInfo);
			// Emmp_CompanyInfo developCompany =
			// buildingInfo.getDevelopCompany();
			// if (developCompany != null)
			// {
			// empj_bldLimitAmount_af.setDevelopCompany(developCompany);
			// empj_bldLimitAmount_af.seteCodeOfDevelopCompany(developCompany.geteCode());
			// }
			// Empj_ProjectInfo project = buildingInfo.getProject();
			// if (project != null)
			// {
			// empj_bldLimitAmount_af.setProject(project);
			// empj_bldLimitAmount_af.setTheNameOfProject(project.getTheName());
			// empj_bldLimitAmount_af.seteCodeOfProject(project.geteCode());
			// }
			// empj_bldLimitAmount_af.setUpfloorNumber(buildingInfo.getUpfloorNumber());
			// empj_bldLimitAmount_af.seteCodeFromConstruction(buildingInfo.geteCodeFromConstruction());
			// empj_bldLimitAmount_af.seteCodeFromPublicSecurity(buildingInfo.geteCodeFromPublicSecurity());
			// empj_bldLimitAmount_af.setRecordAveragePriceOfBuilding(buildingAvgPrice.getRecordAveragePrice());
			// empj_bldLimitAmount_af.setEscrowStandard(buildingInfo.getEscrowStandard());
			// empj_bldLimitAmount_af.setDeliveryType(buildingInfo.getDeliveryType());
			calculateOrgLimitAmount(buildingAvgPrice, buildingInfo, buildingAccount);
			// empj_bldLimitAmount_af.setCurrentFigureProgress(buildingAccount.getCurrentFigureProgress());
			// empj_bldLimitAmount_af.setCurrentLimitedRatio(buildingAccount.getCurrentLimitedRatio());
			// empj_bldLimitAmount_af.setNodeLimitedAmount(buildingAccount.getNodeLimitedAmount());
			// empj_bldLimitAmount_af.setTotalGuaranteeAmount(buildingAccount.getTotalGuaranteeAmount());
			// empj_bldLimitAmount_af.setCashLimitedAmount(buildingAccount.getCashLimitedAmount());
			// empj_bldLimitAmount_af.setEffectiveLimitenodeLimitedAmountdAmount(buildingAccount.getEffectiveLimitedAmount());
			// empj_bldLimitAmount_af.setExpectFigureProgress(null);
			// empj_BldLimitAmount_AFDao.save(empj_bldLimitAmount_af);
			empj_BuildingInfoDao.save(buildingInfo);
			System.out.println("save buildingInfo");

		}
		else
		{
			System.out.println("buildingAccount is null");
		}
	}

	private void calculateOrgLimitAmount(Tgpj_BuildingAvgPrice buildingAvgPrice, Empj_BuildingInfo buildingInfo,
			Tgpj_BuildingAccount buildingAccount)
	{
		
		/*
		 * Double result = null;
		 * Tgpj_EscrowStandardVerMng escrowStandardVerMng =
		 * buildingInfo.getEscrowStandardVerMng();
		 * if (escrowStandardVerMng != null && buildingInfo.getEscrowArea() !=
		 * null && buildingAvgPrice.getRecordAveragePrice() != null)
		 * {
		 * Double recordAveragePrice = buildingAvgPrice.getRecordAveragePrice();
		 * if (escrowStandardVerMng.getAmount() != null)
		 * {
		 * result = escrowStandardVerMng.getAmount() *
		 * buildingInfo.getEscrowArea();
		 * }
		 * else if (escrowStandardVerMng.getPercentage() != null)
		 * {
		 * result = recordAveragePrice * buildingInfo.getEscrowArea() *
		 * escrowStandardVerMng.getPercentage();
		 * }
		 * buildingAccount.setOrgLimitedAmount(result);
		 * }
		 */

		Tgpj_EscrowStandardVerMng escrowStandardVerMng = buildingInfo.getEscrowStandardVerMng();
		if (escrowStandardVerMng != null && buildingInfo.getEscrowArea() != null
				&& buildingAvgPrice.getRecordAveragePrice() != null)
		{
			// 托管面积
			Double escrowArea = 0.00;
			escrowArea = buildingInfo.getEscrowArea();
			// 楼幢住宅备案均价
			Double priceOfBuilding = 0.00;
			priceOfBuilding = buildingAccount.getRecordAvgPriceOfBuilding();
			if (null == priceOfBuilding)
			{
				priceOfBuilding = 0.00;
			}
			// 托管标准类型 (枚举选择:1-标准金额 2-标准比例)
			if (S_EscrowStandardType.StandardAmount.equals(escrowStandardVerMng.getTheType()))
			{
				if (null == escrowStandardVerMng.getAmount())
				{
					buildingAccount.setOrgLimitedAmount(0.00);
				}
				else
				{
					// 初始受限额度 = 托管面积 * 标准金额(如果其中有一个值为0，则保存为0)
					if (escrowArea <= 0.00 || escrowStandardVerMng.getAmount() <= 0.00)
						buildingAccount.setOrgLimitedAmount(0.00);

					Double orgLimitedAmount = MyDouble.getInstance().doubleMultiplyDouble(escrowArea,
							escrowStandardVerMng.getAmount());
					buildingAccount.setOrgLimitedAmount(orgLimitedAmount);

				}
			}
			else if (S_EscrowStandardType.StandardPercentage.equals(escrowStandardVerMng.getTheType()))
			{
				// 初始受限额度 = 托管面积 * 楼幢备案均价 * 受限比例
				if (null == escrowStandardVerMng.getPercentage())
				{
					buildingAccount.setOrgLimitedAmount(0.00);
				}
				else
				{
					// 初始受限额度 = 托管面积 * 楼幢备案均价 *
					// 受限比例(如果有一个值为0，则直接为0)
					if (priceOfBuilding <= 0 || escrowArea <= 0 || escrowStandardVerMng.getPercentage() <= 0)
						buildingAccount.setOrgLimitedAmount(0.00);

					/*
					 * 标准比例为30%时，计算 楼幢备案均价 * 受限比例
					 * 楼幢为成品房 6000 2 deliveryType
					 * 楼幢为毛坯房 4000 1 deliveryType
					 */
					Double percentage = escrowStandardVerMng.getPercentage();// 比例
					Double doubleTage = percentage * priceOfBuilding /100 ;// 楼幢备案均价 *
																		// 受限比例
					Double double2 = 0.00;// 初始受限额度
					if ((percentage - 30) == 0)
					{
						String theType = buildingInfo.getDeliveryType();
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

					buildingAccount.setOrgLimitedAmount(double2);
				}
			}
		}

	}
}
