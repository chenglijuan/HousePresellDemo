package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：楼幢账户
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpj_BuildingAccountRebuild extends RebuilderBase<Tgpj_BuildingAccount>
{
	@Override
	public Properties getSimpleInfo(Tgpj_BuildingAccount object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tgpj_BuildingAccount object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
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
		properties.put("recordAvgPriceOfBuildingFromPresellSystem", object.getRecordAvgPriceOfBuildingFromPresellSystem());
		properties.put("recordAvgPriceOfBuilding", object.getRecordAvgPriceOfBuilding());
		properties.put("appropriateFrozenAmount", object.getAppropriateFrozenAmount());
		properties.put("logId", object.getLogId());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpj_BuildingAccount> tgpj_BuildingAccountList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpj_BuildingAccountList != null)
		{
			for(Tgpj_BuildingAccount object:tgpj_BuildingAccountList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
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
				properties.put("recordAvgPriceOfBuildingFromPresellSystem", object.getRecordAvgPriceOfBuildingFromPresellSystem());
				properties.put("recordAvgPriceOfBuilding", object.getRecordAvgPriceOfBuilding());
				properties.put("logId", object.getLogId());
				
				list.add(properties);
			}
		}
		return list;
	}
	
	/*
	 * xsz by time 2018-11-29 15:43:13
	 * 
	 */
	public Properties getDetailForSpecialFund(Tgpj_BuildingAccount object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		//托管标准
		properties.put("escrowStandard", object.getEscrowStandard());
		//初始受限额度
		properties.put("orgLimitedAmount", object.getOrgLimitedAmount());
		//当前形象进度
		properties.put("currentFigureProgress", object.getCurrentFigureProgress());
		//当前受限比例
		properties.put("currentLimitedRatio", object.getCurrentLimitedRatio());
		//支付保证累计金额
		properties.put("totalAmountGuaranteed", object.getTotalAmountGuaranteed());
		//节点受限额度
		properties.put("nodeLimitedAmount", object.getNodeLimitedAmount());
		//现金受限额度
		properties.put("cashLimitedAmount", object.getCashLimitedAmount());
		//有效受限额度
		properties.put("effectiveLimitedAmount", object.getEffectiveLimitedAmount());
		//总入账金额
		properties.put("totalAccountAmount", object.getTotalAccountAmount());
		//已拨付金额
		properties.put("payoutAmount", object.getPayoutAmount());
		properties.put("appropriateFrozenAmount", object.getAppropriateFrozenAmount());
		//已申请未拨付金额
		properties.put("appliedNoPayoutAmount", object.getAppliedNoPayoutAmount());
		//可拨付金额
		properties.put("allocableAmount", object.getAllocableAmount());
		
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("escrowArea", object.getEscrowArea());
		properties.put("buildingArea", object.getBuildingArea());
		properties.put("currentLimitedRatio", object.getCurrentLimitedRatio());
		properties.put("totalGuaranteeAmount", object.getTotalGuaranteeAmount());
		//溢出金额
		properties.put("spilloverAmount", object.getSpilloverAmount());
		//已退款申请未拨付金额
		properties.put("applyRefundPayoutAmount", object.getApplyRefundPayoutAmount());
		//已退款金额
		properties.put("refundAmount", object.getRefundAmount());
		//当前托管余额
		properties.put("currentEscrowFund", object.getCurrentEscrowFund());
		properties.put("recordAvgPriceOfBuildingFromPresellSystem", object.getRecordAvgPriceOfBuildingFromPresellSystem());
		properties.put("recordAvgPriceOfBuilding", object.getRecordAvgPriceOfBuilding());
		
		properties.put("logId", object.getLogId());
		
		return properties;
	}
}
