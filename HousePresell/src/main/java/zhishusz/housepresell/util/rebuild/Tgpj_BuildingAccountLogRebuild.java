package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：楼幢账户Log表
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpj_BuildingAccountLogRebuild extends RebuilderBase<Tgpj_BuildingAccountLog>
{
	@Override
	public Properties getSimpleInfo(Tgpj_BuildingAccountLog object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tgpj_BuildingAccountLog object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("userUpdate", object.getUserUpdate());
		properties.put("userUpdateId", object.getUserUpdate().getTableId());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("userRecord", object.getUserRecord());
		properties.put("userRecordId", object.getUserRecord().getTableId());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("developCompany", object.getDevelopCompany());
		properties.put("developCompanyId", object.getDevelopCompany().getTableId());
		properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
		properties.put("project", object.getProject());
		properties.put("projectId", object.getProject().getTableId());
		properties.put("theNameOfProject", object.getTheNameOfProject());
		properties.put("building", object.getBuilding());
		properties.put("buildingId", object.getBuilding().getTableId());
		properties.put("payment", object.getPayment());
		properties.put("paymentId", object.getPayment().getTableId());
		properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
		properties.put("escrowStandard", object.getEscrowStandard());
		properties.put("escrowArea", object.getEscrowArea());
		properties.put("buildingArea", object.getBuildingArea());
		properties.put("orgLimitedAmount", object.getOrgLimitedAmount());
		properties.put("currentFigureProgress", object.getCurrentFigureProgress());
		properties.put("currentLimitedRatio", object.getCurrentLimitedRatio());
		properties.put("nodeLimitedAmount", object.getNodeLimitedAmount());
		properties.put("totalGuaranteeAmount", object.getTotalGuaranteeAmount());
		properties.put("cashLimitedAmount", object.getCashLimitedAmount());
		properties.put("effectiveLimitedAmount", object.getEffectiveLimitedAmount());
		properties.put("totalAccountAmount", object.getTotalAccountAmount());
		properties.put("spilloverAmount", object.getSpilloverAmount());
		properties.put("payoutAmount", object.getPayoutAmount());
		properties.put("appliedNoPayoutAmount", object.getAppliedNoPayoutAmount());
		properties.put("applyRefundPayoutAmount", object.getApplyRefundPayoutAmount());
		properties.put("refundAmount", object.getRefundAmount());
		properties.put("currentEscrowFund", object.getCurrentEscrowFund());
		properties.put("allocableAmount", object.getAllocableAmount());
		properties.put("appropriateFrozenAmount", object.getAppropriateFrozenAmount());
		properties.put("recordAvgPriceOfBuildingFromPresellSystem", object.getRecordAvgPriceOfBuildingFromPresellSystem());
		properties.put("recordAvgPriceOfBuilding", object.getRecordAvgPriceOfBuilding());
		properties.put("logId", object.getLogId());
		properties.put("actualAmount", object.getActualAmount());
		properties.put("paymentLines", object.getPaymentLines());
		properties.put("paymentProportion", object.getPaymentProportion());
		properties.put("buildAmountPaid", object.getBuildAmountPaid());
		properties.put("buildAmountPay", object.getBuildAmountPay());
		properties.put("totalAmountGuaranteed", object.getTotalAmountGuaranteed());
		properties.put("relatedBusiCode", object.getRelatedBusiCode());
		properties.put("relatedBusiTableId", object.getRelatedBusiTableId());
		properties.put("tgpj_BuildingAccount", object.getTgpj_BuildingAccount());
		properties.put("tgpj_BuildingAccountId", object.getTgpj_BuildingAccount().getTableId());
		properties.put("versionNo", object.getVersionNo());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpj_BuildingAccountLog> tgpj_BuildingAccountLogList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpj_BuildingAccountLogList != null)
		{
			for(Tgpj_BuildingAccountLog object:tgpj_BuildingAccountLogList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("userUpdate", object.getUserUpdate());
				properties.put("userUpdateId", object.getUserUpdate().getTableId());
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				properties.put("userRecord", object.getUserRecord());
				properties.put("userRecordId", object.getUserRecord().getTableId());
				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				properties.put("developCompany", object.getDevelopCompany());
				properties.put("developCompanyId", object.getDevelopCompany().getTableId());
				properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
				properties.put("project", object.getProject());
				properties.put("projectId", object.getProject().getTableId());
				properties.put("theNameOfProject", object.getTheNameOfProject());
				properties.put("building", object.getBuilding());
				properties.put("buildingId", object.getBuilding().getTableId());
				properties.put("payment", object.getPayment());
				properties.put("paymentId", object.getPayment().getTableId());
				properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
				properties.put("escrowStandard", object.getEscrowStandard());
				properties.put("escrowArea", object.getEscrowArea());
				properties.put("buildingArea", object.getBuildingArea());
				properties.put("orgLimitedAmount", object.getOrgLimitedAmount());
				properties.put("currentFigureProgress", object.getCurrentFigureProgress());
				properties.put("currentLimitedRatio", object.getCurrentLimitedRatio());
				properties.put("nodeLimitedAmount", object.getNodeLimitedAmount());
				properties.put("totalGuaranteeAmount", object.getTotalGuaranteeAmount());
				properties.put("cashLimitedAmount", object.getCashLimitedAmount());
				properties.put("effectiveLimitedAmount", object.getEffectiveLimitedAmount());
				properties.put("totalAccountAmount", object.getTotalAccountAmount());
				properties.put("spilloverAmount", object.getSpilloverAmount());
				properties.put("payoutAmount", object.getPayoutAmount());
				properties.put("appliedNoPayoutAmount", object.getAppliedNoPayoutAmount());
				properties.put("applyRefundPayoutAmount", object.getApplyRefundPayoutAmount());
				properties.put("refundAmount", object.getRefundAmount());
				properties.put("currentEscrowFund", object.getCurrentEscrowFund());
				properties.put("allocableAmount", object.getAllocableAmount());
				properties.put("appropriateFrozenAmount", object.getAppropriateFrozenAmount());
				properties.put("recordAvgPriceOfBuildingFromPresellSystem", object.getRecordAvgPriceOfBuildingFromPresellSystem());
				properties.put("recordAvgPriceOfBuilding", object.getRecordAvgPriceOfBuilding());
				properties.put("logId", object.getLogId());
				properties.put("actualAmount", object.getActualAmount());
				properties.put("paymentLines", object.getPaymentLines());
				properties.put("paymentProportion", object.getPaymentProportion());
				properties.put("buildAmountPaid", object.getBuildAmountPaid());
				properties.put("buildAmountPay", object.getBuildAmountPay());
				properties.put("totalAmountGuaranteed", object.getTotalAmountGuaranteed());
				properties.put("relatedBusiCode", object.getRelatedBusiCode());
				properties.put("relatedBusiTableId", object.getRelatedBusiTableId());
				properties.put("tgpj_BuildingAccount", object.getTgpj_BuildingAccount());
				properties.put("tgpj_BuildingAccountId", object.getTgpj_BuildingAccount().getTableId());
				properties.put("versionNo", object.getVersionNo());
				
				list.add(properties);
			}
		}
		return list;
	}
}
