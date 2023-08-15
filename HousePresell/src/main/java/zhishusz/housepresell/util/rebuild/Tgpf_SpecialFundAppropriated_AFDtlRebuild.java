package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AFDtl;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：特殊拨付-申请子表
 * Company：ZhiShuSZ
 */
@Service
public class Tgpf_SpecialFundAppropriated_AFDtlRebuild extends RebuilderBase<Tgpf_SpecialFundAppropriated_AFDtl>
{
	@Override
	public Properties getSimpleInfo(Tgpf_SpecialFundAppropriated_AFDtl object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());

		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_SpecialFundAppropriated_AFDtl object)
	{
		if (object == null)
			return null;
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
		// properties.put("userRecord", object.getUserRecord());
		// properties.put("userRecordId", object.getUserRecord().getTableId());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("specialAppropriated", object.getSpecialAppropriated());
		properties.put("specialAppropriatedId", object.getSpecialAppropriated().getTableId());
		properties.put("theCodeOfAf", object.getTheCodeOfAf());
		if (null == object.getBankAccountEscrowed())
		{
			properties.put("bankAccountEscrowed", "");
			properties.put("bankAccountEscrowedId", "");
			properties.put("theNameOfBankBranch", "");
		}
		else
		{
			properties.put("bankAccountEscrowed", object.getBankAccountEscrowed());
			properties.put("bankAccountEscrowedId", object.getBankAccountEscrowed().getTableId());
			properties.put("theNameOfBankBranch", object.getBankAccountEscrowed().getBankBranch().getTheName());
		}
		properties.put("accountOfEscrowed", object.getAccountOfEscrowed());
		properties.put("theNameOfEscrowed", object.getTheNameOfEscrowed());
		properties.put("applyRefundPayoutAmount", object.getApplyRefundPayoutAmount());
		properties.put("appliedAmount", object.getAppliedAmount());
		properties.put("accountBalance", object.getAccountBalance());
		properties.put("billNumber", object.getBillNumber());
		properties.put("payoutChannel", object.getPayoutChannel());
		properties.put("payoutDate", object.getPayoutDate());
		properties.put("payoutState", object.getPayoutState());

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpf_SpecialFundAppropriated_AFDtl> tgpf_SpecialFundAppropriated_AFDtlList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (tgpf_SpecialFundAppropriated_AFDtlList != null)
		{
			for (Tgpf_SpecialFundAppropriated_AFDtl object : tgpf_SpecialFundAppropriated_AFDtlList)
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
				properties.put("specialAppropriated", object.getSpecialAppropriated());
				properties.put("specialAppropriatedId", object.getSpecialAppropriated().getTableId());
				properties.put("theCodeOfAf", object.getTheCodeOfAf());
				properties.put("bankAccountEscrowed", object.getBankAccountEscrowed());
				properties.put("bankAccountEscrowedId", object.getBankAccountEscrowed().getTableId());
				properties.put("accountOfEscrowed", object.getAccountOfEscrowed());
				properties.put("theNameOfEscrowed", object.getTheNameOfEscrowed());
				properties.put("applyRefundPayoutAmount", object.getApplyRefundPayoutAmount());
				properties.put("appliedAmount", object.getAppliedAmount());
				properties.put("accountBalance", object.getAccountBalance());
				properties.put("billNumber", object.getBillNumber());
				properties.put("payoutChannel", object.getPayoutChannel());
				properties.put("payoutDate", object.getPayoutDate());
				properties.put("payoutState", object.getPayoutState());

				list.add(properties);
			}
		}
		return list;
	}
}
