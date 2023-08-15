package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

import lombok.Getter;
import lombok.Setter;

/*
 * Rebuilder：退房退款-贷款已结清
 * Company：ZhiShuSZ
 */
@Service
public class Tgpf_RefundInfoRebuild extends RebuilderBase<Tgpf_RefundInfo>
{
	@Override
	public Properties getSimpleInfo(Tgpf_RefundInfo object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("eCodeOfContractRecord", object.geteCodeOfContractRecord());
		properties.put("eCodeOfTripleAgreement", object.geteCodeOfTripleAgreement());
		properties.put("theNameOfProject", object.getTheNameOfProject());
		properties.put("theNameOfBuyer", object.getTheNameOfBuyer());
		properties.put("theNameOfCreditor", object.getTheNameOfCreditor());
		properties.put("eCode", object.geteCode());

		Double refundAmount = MyDouble.getInstance().getShort(object.getRefundAmount(), 3);
		properties.put("refundAmount", refundAmount);
		Double actualRefundAmount = MyDouble.getInstance().getShort(object.getActualRefundAmount(), 3);
		properties.put("actualRefundAmount", actualRefundAmount);

		properties.put("refundTimeStamp", object.getRefundTimeStamp());
		properties.put("busiState", object.getBusiState());

		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_RefundInfo object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("tripleAgreement", object.getTripleAgreement());
		properties.put("tripleAgreementId", object.getTripleAgreement().getTableId());
		properties.put("eCodeOfTripleAgreement", object.geteCodeOfTripleAgreement());
		properties.put("eCodeOfContractRecord", object.geteCodeOfContractRecord());
		properties.put("project", object.getProject());
		properties.put("projectId", object.getProject().getTableId());
		properties.put("theNameOfProject", object.getTheNameOfProject());
		properties.put("building", object.getBuilding());
		properties.put("companyName", object.getBuilding().getDevelopCompany().getTheName());
		properties.put("buildingId", object.getBuilding().getTableId());
		properties.put("positionOfBuilding", object.getPositionOfBuilding());
		// properties.put("buyerId", object.getBuyer().getTableId());
		properties.put("theNameOfBuyer", object.getTheNameOfBuyer());
		properties.put("certificateNumberOfBuyer", object.getCertificateNumberOfBuyer());
		properties.put("contactPhoneOfBuyer", object.getContactPhoneOfBuyer());
		properties.put("theNameOfCreditor", object.getTheNameOfCreditor());
		Double fundOfTripleAgreement = MyDouble.getInstance().getShort(object.getFundOfTripleAgreement(), 3);
		properties.put("fundOfTripleAgreement", fundOfTripleAgreement);
		Double fundFromLoan = MyDouble.getInstance().getShort(object.getFundFromLoan(), 3);
		properties.put("fundFromLoan", fundFromLoan);
		Double retainRightAmount = MyDouble.getInstance().getShort(object.getRetainRightAmount(), 3);
		properties.put("retainRightAmount", retainRightAmount);
		Double expiredAmount = MyDouble.getInstance().getShort(object.getExpiredAmount(), 3);
		properties.put("expiredAmount", expiredAmount);
		Double unexpiredAmount = MyDouble.getInstance().getShort(object.getUnexpiredAmount(), 3);
		properties.put("unexpiredAmount", unexpiredAmount);
		Double refundAmount = MyDouble.getInstance().getShort(object.getRefundAmount(), 3);
		properties.put("refundAmount", refundAmount);
		
		properties.put("receiverType", object.getReceiverType());
		if(null != object.getReceiverType() && 1 == object.getReceiverType()){
			properties.put("receiverName", object.getReceiverName());
			properties.put("receiverBankName", object.getReceiverBankName());
			properties.put("receiverBankAccount", object.getReceiverBankAccount());
		}
		else
		{
			properties.put("developCompanyName", object.getReceiverName());
			properties.put("bBankName", object.getReceiverBankName());
			properties.put("bAccountSupervised", object.getReceiverBankAccount());
		}
		
		properties.put("refundType", object.getRefundType());
		Double actualRefundAmount = MyDouble.getInstance().getShort(object.getActualRefundAmount(), 3);
		properties.put("actualRefundAmount", actualRefundAmount);
		properties.put("refundBankName", object.getRefundBankName());
		properties.put("refundBankAccount", object.getRefundBankAccount());

		properties.put("refundTimeStamp", object.getRefundTimeStamp());

		if(object.getTheBankAccountEscrowed() != null){
			properties.put("theBankAccountEscrowed", object.getTheBankAccountEscrowed());
			properties.put("theBankAccountEscrowedId", object.getTheBankAccountEscrowed().getTableId());
		}
		
		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpf_RefundInfo> tgpf_RefundInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (tgpf_RefundInfoList != null)
		{
			for (Tgpf_RefundInfo object : tgpf_RefundInfoList)
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
				properties.put("tripleAgreement", object.getTripleAgreement());
				properties.put("tripleAgreementId", object.getTripleAgreement().getTableId());
				properties.put("eCodeOfTripleAgreement", object.geteCodeOfTripleAgreement());
				properties.put("eCodeOfContractRecord", object.geteCodeOfContractRecord());
				properties.put("project", object.getProject());
				properties.put("projectId", object.getProject().getTableId());
				properties.put("theNameOfProject", object.getTheNameOfProject());
				properties.put("building", object.getBuilding());
				properties.put("buildingId", object.getBuilding().getTableId());
				properties.put("positionOfBuilding", object.getPositionOfBuilding());
				// properties.put("buyerId", object.getBuyer().getTableId());
				properties.put("theNameOfBuyer", object.getTheNameOfBuyer());
				properties.put("certificateNumberOfBuyer", object.getCertificateNumberOfBuyer());
				properties.put("contactPhoneOfBuyer", object.getContactPhoneOfBuyer());
				properties.put("theNameOfCreditor", object.getTheNameOfCreditor());
				Double fundOfTripleAgreement = MyDouble.getInstance().getShort(object.getFundOfTripleAgreement(), 3);
				properties.put("fundOfTripleAgreement", fundOfTripleAgreement);
				Double fundFromLoan = MyDouble.getInstance().getShort(object.getFundFromLoan(), 3);
				properties.put("fundFromLoan", fundFromLoan);
				Double retainRightAmount = MyDouble.getInstance().getShort(object.getRetainRightAmount(), 3);
				properties.put("retainRightAmount", retainRightAmount);
				Double expiredAmount = MyDouble.getInstance().getShort(object.getExpiredAmount(), 3);
				properties.put("expiredAmount", expiredAmount);
				Double unexpiredAmount = MyDouble.getInstance().getShort(object.getUnexpiredAmount(), 3);
				properties.put("unexpiredAmount", unexpiredAmount);
				properties.put("refundAmount", object.getRefundAmount());
				properties.put("receiverType", object.getReceiverType());
				properties.put("receiverName", object.getReceiverName());
				properties.put("receiverBankName", object.getReceiverBankName());
				properties.put("refundType", object.getRefundType());
				properties.put("receiverBankAccount", object.getReceiverBankAccount());
				Double actualRefundAmount = MyDouble.getInstance().getShort(object.getActualRefundAmount(), 3);
				properties.put("actualRefundAmount", actualRefundAmount);
				properties.put("refundBankName", object.getRefundBankName());
				properties.put("refundBankAccount", object.getRefundBankAccount());
				properties.put("refundTimeStamp", object.getRefundTimeStamp());

				list.add(properties);
			}
		}
		return list;
	}
}
