package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Empj_PaymentGuaranteeChild;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;
import zhishusz.housepresell.database.po.state.S_EscrowStandardType;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：支付保证申请
 * Company：ZhiShuSZ
 * */
@Service
public class Empj_PaymentGuaranteeApplyChildRebuild extends RebuilderBase<Empj_PaymentGuaranteeChild>
{
	
	MyDatetime myDatetime = MyDatetime.getInstance();

	@Override
	public Properties getSimpleInfo(Empj_PaymentGuaranteeChild object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		properties.put("tableId", object.getTableId());
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("userStart", object.getUserStart());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("createTimeStampString", myDatetime.dateToString2(object.getCreateTimeStamp()));
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("lastUpdateTimeStampString", MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));
		properties.put("userRecord", object.getUserRecord());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("recordTimeStampString",myDatetime.dateToString2(object.getRecordTimeStamp()) );
		
		properties.put("empj_BuildingInfo", object.getEmpj_BuildingInfo());
		properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
		properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());
		properties.put("buildingArea", object.getBuildingArea());
		properties.put("escrowArea", object.getEscrowArea());
		properties.put("recordAvgPriceOfBuilding", object.getRecordAvgPriceOfBuilding());
		properties.put("escrowStandard", object.getEscrowStandard());
		properties.put("orgLimitedAmount", object.getOrgLimitedAmount());
		properties.put("paymentProportion", object.getPaymentProportion());
		properties.put("paymentLines", object.getPaymentLines());
		properties.put("buildAmountPaid", object.getBuildAmountPaid());
		properties.put("buildAmountPay", object.getBuildAmountPay());
		properties.put("totalAmountGuaranteed", object.getTotalAmountGuaranteed());
		properties.put("buildProjectPaid", object.getBuildProjectPaid());
		properties.put("buildProjectPay", object.getBuildProjectPay());
		properties.put("amountGuaranteed", object.getAmountGuaranteed());
		properties.put("cashLimitedAmount", object.getCashLimitedAmount());
		properties.put("currentFigureProgress", object.getCurrentFigureProgress());
		properties.put("currentLimitedRatio", object.getCurrentLimitedRatio());
		properties.put("nodeLimitedAmount", object.getNodeLimitedAmount());
		properties.put("effectiveLimitedAmount", object.getEffectiveLimitedAmount());
		properties.put("totalAccountAmount", object.getTotalAccountAmount());
		properties.put("payoutAmount", object.getPayoutAmount());
		properties.put("spilloverAmount", object.getSpilloverAmount());
		properties.put("appropriateFrozenAmount", object.getAppropriateFrozenAmount());
		properties.put("appliedNoPayoutAmount", object.getAppliedNoPayoutAmount());
		properties.put("releaseTheAmount", object.getReleaseTheAmount());
		properties.put("remark", object.getRemark());
		
		return properties;
	}

	@Override
	public Properties getDetail(Empj_PaymentGuaranteeChild object)
	{
		
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());


		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Empj_PaymentGuaranteeChild> Empj_PaymentGuaranteeChildList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(Empj_PaymentGuaranteeChildList != null)
		{
			for(Empj_PaymentGuaranteeChild object:Empj_PaymentGuaranteeChildList)
			{
				Properties properties = new MyProperties();
				properties.put("tableId", object.getTableId());
				list.add(properties);
			}
		}
		return list;
	}

	public List<Properties> getApplyList(List<Empj_PaymentGuaranteeChild> Empj_PaymentGuaranteeChildList){
		List<Properties> list = new ArrayList<Properties>();
		if(Empj_PaymentGuaranteeChildList != null)
		{
			for(Empj_PaymentGuaranteeChild object:Empj_PaymentGuaranteeChildList)
			{
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());

				Empj_BuildingInfo building = object.getEmpj_BuildingInfo();
				
				if(object.getEmpj_BuildingInfo()!=null){
					properties.put("empj_BuildingInfoId", building.getTableId());
					Tgpj_EscrowStandardVerMng escrowStandardVerMng = building.getEscrowStandardVerMng();
					if(null != escrowStandardVerMng && escrowStandardVerMng.getTheType().equals(S_EscrowStandardType.StandardAmount))
					{
						properties.put("escrowStandard", escrowStandardVerMng.getAmount()+"元/m²");
					}
					else if(null != escrowStandardVerMng && escrowStandardVerMng.getTheType().equals(S_EscrowStandardType.StandardPercentage))
					{
						properties.put("escrowStandard", escrowStandardVerMng.getPercentage()+"%");
					}			
				}
				
				properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
				properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());
				properties.put("buildingArea", object.getBuildingArea());
				properties.put("escrowArea", object.getEscrowArea());
				properties.put("recordAvgPriceOfBuilding", object.getRecordAvgPriceOfBuilding());
				properties.put("currentFigureProgress", object.getCurrentFigureProgress());
				properties.put("orgLimitedAmount", object.getOrgLimitedAmount());
				properties.put("paymentProportion", object.getPaymentProportion());
				properties.put("paymentLines", object.getPaymentLines());
				properties.put("buildAmountPaid", object.getBuildAmountPaid());
				properties.put("buildAmountPay", object.getBuildAmountPay());
				properties.put("totalAmountGuaranteed", object.getTotalAmountGuaranteed());
				properties.put("buildProjectPaid", object.getBuildProjectPaid());
				properties.put("buildProjectPay", object.getBuildProjectPay());
				properties.put("amountGuaranteed", object.getAmountGuaranteed());
				properties.put("cashLimitedAmount", object.getCashLimitedAmount());
				properties.put("currentLimitedRatio", object.getCurrentLimitedRatio());
				properties.put("nodeLimitedAmount", object.getNodeLimitedAmount());
				properties.put("effectiveLimitedAmount", object.getEffectiveLimitedAmount());
				properties.put("totalAccountAmount", object.getTotalAccountAmount());
				properties.put("payoutAmount", object.getPayoutAmount());
				properties.put("spilloverAmount", object.getSpilloverAmount());
				properties.put("appropriateFrozenAmount", object.getAppropriateFrozenAmount());
				properties.put("appliedNoPayoutAmount", object.getAppliedNoPayoutAmount());
				properties.put("releaseTheAmount", object.getReleaseTheAmount());
				properties.put("remark", object.getRemark());
				
				list.add(properties);
			}
		}
		return list;
	}
}
