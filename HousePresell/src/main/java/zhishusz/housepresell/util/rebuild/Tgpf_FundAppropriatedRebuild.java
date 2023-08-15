package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgpf_FundAppropriated;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：资金拨付
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpf_FundAppropriatedRebuild extends RebuilderBase<Tgpf_FundAppropriated>
{
	private MyDatetime myDatetime = MyDatetime.getInstance();
	private MyDouble myDouble = MyDouble.getInstance();
	@Override
	public Properties getSimpleInfo(Tgpf_FundAppropriated object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		//列表页面
		properties.put("tableId", object.getTableId());
		properties.put("canPayAmount",myDouble.pointTOThousandths(object.getCanPayAmount()));//可拨付金额


		//托管账户信息
		if(object.getBankAccountEscrowed() != null)
		{
			Tgxy_BankAccountEscrowed tgxy_bankAccountEscrowed = object.getBankAccountEscrowed();
			properties.put("bankAccountEscrowedId",tgxy_bankAccountEscrowed.getTableId());
			properties.put("escroweBankAccount",tgxy_bankAccountEscrowed.getTheAccount());
			properties.put("theNameOfEscroweBankAccount",object.getBankAccountEscrowed().getTheName());
			if(tgxy_bankAccountEscrowed.getBankBranch()!=null)
			{
				properties.put("shortNameOfBankBranch",tgxy_bankAccountEscrowed.getBankBranch().getShortName()); //开户行简称
			}
		}
		
		//监管账户信息
		if(object.getBankAccountSupervised() != null)
		{
			properties.put("bankAccountSupervisedId",object.getBankAccountSupervised().getTableId());
			properties.put("supervisedBankAccount",object.getBankAccountSupervised().getTheAccount());
			properties.put("theNameOfSupervisedBankAccount",object.getBankAccountSupervised().getTheName());
		}

		if(object.getFundAppropriated_AF()!=null)
		{
			properties.put("fundAppropriated_AFId",object.getFundAppropriated_AF().getTableId()); //用款申请主表Id
		}
		
		properties.put("overallPlanPayoutAmount", myDouble.pointTOThousandths(object.getOverallPlanPayoutAmount())); //统筹拨付金额
		properties.put("currentPayoutAmount", myDouble.pointTOThousandths(object.getCurrentPayoutAmount())); //本次拨付金额
		properties.put("currentPayoutAmountOld", object.getCurrentPayoutAmount()); //本次拨付金额（未转换）
		properties.put("actualPayoutDate",object.getActualPayoutDate()); //拨付时间
		properties.put("eCodeFromPayoutBill", object.geteCodeFromPayoutBill()); //拨付单据号
		properties.put("colorState", object.getColorState());
		properties.put("approvalState",object.getApprovalState());

		
		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_FundAppropriated object)
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
		properties.put("fundOverallPlan", object.getFundOverallPlan());
		properties.put("fundOverallPlanId", object.getFundOverallPlan().getTableId());
		properties.put("fundAppropriated_AF", object.getFundAppropriated_AF());
		properties.put("fundAppropriated_AFId", object.getFundAppropriated_AF().getTableId());
		properties.put("overallPlanPayoutAmount", object.getOverallPlanPayoutAmount());
		properties.put("bankAccountEscrowed", object.getBankAccountEscrowed());
		properties.put("bankAccountEscrowedId", object.getBankAccountEscrowed().getTableId());
		properties.put("bankAccountSupervised", object.getBankAccountSupervised());
		properties.put("bankAccountSupervisedId", object.getBankAccountSupervised().getTableId());
		properties.put("actualPayoutDate", object.getActualPayoutDate());
		properties.put("eCodeFromPayoutBill", object.geteCodeFromPayoutBill());
		properties.put("currentPayoutAmount", object.getCurrentPayoutAmount());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpf_FundAppropriated> tgpf_FundAppropriatedList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpf_FundAppropriatedList != null)
		{
			for(Tgpf_FundAppropriated object:tgpf_FundAppropriatedList)
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
				properties.put("fundOverallPlan", object.getFundOverallPlan());
				properties.put("fundOverallPlanId", object.getFundOverallPlan().getTableId());
				properties.put("fundAppropriated_AF", object.getFundAppropriated_AF());
				properties.put("fundAppropriated_AFId", object.getFundAppropriated_AF().getTableId());
				properties.put("overallPlanPayoutAmount", object.getOverallPlanPayoutAmount());
				properties.put("bankAccountEscrowed", object.getBankAccountEscrowed());
				properties.put("bankAccountEscrowedId", object.getBankAccountEscrowed().getTableId());
				properties.put("bankAccountSupervised", object.getBankAccountSupervised());
				properties.put("bankAccountSupervisedId", object.getBankAccountSupervised().getTableId());
				properties.put("actualPayoutDate", object.getActualPayoutDate());
				properties.put("eCodeFromPayoutBill", object.geteCodeFromPayoutBill());
				properties.put("currentPayoutAmount", object.getCurrentPayoutAmount());
				
				list.add(properties);
			}
		}
		return list;
	}
}
