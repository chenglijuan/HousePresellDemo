package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Tgpf_AccVoucher;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：推送给财务系统-凭证
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpf_AccVoucherRebuild extends RebuilderBase<Tgpf_AccVoucher>
{
	@Override
	public Properties getSimpleInfo(Tgpf_AccVoucher object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_AccVoucher object)
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
		properties.put("billTimeStamp", object.getBillTimeStamp());
		properties.put("theType", object.getTheType());
		properties.put("tradeCount", object.getTradeCount());
		properties.put("totalTradeAmount", object.getTotalTradeAmount());
		properties.put("contentJson", object.getContentJson());
		properties.put("payoutTimeStamp", object.getPayoutTimeStamp());
		properties.put("company", object.getCompany());
		properties.put("companyId", object.getCompany().getTableId());
		properties.put("theNameOfCompany", object.getTheNameOfCompany());
		properties.put("project", object.getProject());
		properties.put("projectId", object.getProject().getTableId());
		properties.put("theNameOfProject", object.getTheNameOfProject());
		properties.put("bank", object.getBank());
		properties.put("bankId", object.getBank().getTableId());
		properties.put("theNameOfBank", object.getTheNameOfBank());
		properties.put("DayEndBalancingState", object.getDayEndBalancingState());
		properties.put("bankAccountEscrowed", object.getBankAccountEscrowed());
		properties.put("bankAccountEscrowedId", object.getBankAccountEscrowed().getTableId());
		properties.put("theAccountOfBankAccountEscrowed", object.getTheAccountOfBankAccountEscrowed());
		properties.put("payoutAmount", object.getPayoutAmount());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpf_AccVoucher> tgpf_AccVoucherList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpf_AccVoucherList != null)
		{
			for(Tgpf_AccVoucher object:tgpf_AccVoucherList)
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
				properties.put("billTimeStamp", object.getBillTimeStamp());
				properties.put("theType", object.getTheType());
				properties.put("tradeCount", object.getTradeCount());
				properties.put("totalTradeAmount", object.getTotalTradeAmount());
				properties.put("contentJson", object.getContentJson());
				properties.put("payoutTimeStamp", object.getPayoutTimeStamp());
				properties.put("company", object.getCompany());
				properties.put("companyId", object.getCompany().getTableId());
				properties.put("theNameOfCompany", object.getTheNameOfCompany());
				properties.put("project", object.getProject());
				properties.put("projectId", object.getProject().getTableId());
				properties.put("theNameOfProject", object.getTheNameOfProject());
				properties.put("bank", object.getBank());
				properties.put("bankId", object.getBank().getTableId());
				properties.put("theNameOfBank", object.getTheNameOfBank());
				properties.put("DayEndBalancingState", object.getDayEndBalancingState());
				properties.put("bankAccountEscrowed", object.getBankAccountEscrowed());
				properties.put("bankAccountEscrowedId", object.getBankAccountEscrowed().getTableId());
				properties.put("theAccountOfBankAccountEscrowed", object.getTheAccountOfBankAccountEscrowed());
				properties.put("payoutAmount", object.getPayoutAmount());
				
				list.add(properties);
			}
		}
		return list;
	}
	
	
	@SuppressWarnings("rawtypes")
	public List getList(List<Tgpf_AccVoucher> tgpf_AccVoucherList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpf_AccVoucherList != null)
		{
			for(Tgpf_AccVoucher object:tgpf_AccVoucherList)
			{
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
//				properties.put("userStart", object.getUserStart());
//				properties.put("userStartId", object.getUserStart().getTableId());
//				properties.put("createTimeStamp", object.getCreateTimeStamp());
//				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
//				properties.put("userRecord", object.getUserRecord());
//				properties.put("userRecordId", object.getUserRecord().getTableId());
//				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				properties.put("billTimeStamp", object.getBillTimeStamp());
//				properties.put("theType", object.getTheType());
				properties.put("tradeCount", object.getTradeCount());
				properties.put("totalTradeAmount", object.getTotalTradeAmount());
				properties.put("contentJson", object.getContentJson());
				properties.put("payoutTimeStamp", object.getPayoutTimeStamp());
//				properties.put("company", object.getCompany());
//				properties.put("companyId", object.getCompany().getTableId());
//				properties.put("theNameOfCompany", object.getTheNameOfCompany());
//				properties.put("project", object.getProject());
//				properties.put("projectId", object.getProject().getTableId());
//				properties.put("theNameOfProject", object.getTheNameOfProject());
//				properties.put("bank", object.getBank());
//				properties.put("bankId", object.getBank().getTableId());
				
				Tgxy_BankAccountEscrowed bankAccountEscrowed = object.getBankAccountEscrowed();
				
				Emmp_BankBranch bankBranch = bankAccountEscrowed.getBankBranch();
				
				properties.put("theAccount", bankAccountEscrowed.getTheAccount()); // 托管账号
				properties.put("theName", bankAccountEscrowed.getTheName());	// 托管账号名称
				properties.put("shortName", bankBranch.getShortName());	// 银行简称
				properties.put("theNameOfBank", bankBranch.getTheName()); // 开户行名称
				properties.put("dayEndBalancingState", object.getDayEndBalancingState());
//				properties.put("bankAccountEscrowed", object.getBankAccountEscrowed());
//				properties.put("bankAccountEscrowedId", object.getBankAccountEscrowed().getTableId());
//				properties.put("theAccountOfBankAccountEscrowed", object.getTheAccountOfBankAccountEscrowed());
//				properties.put("payoutAmount", object.getPayoutAmount());
				
				list.add(properties);
			}
		}
		return list;
	}
	
	
	@SuppressWarnings("rawtypes")
	public List getDisbursementList(List<Tgpf_AccVoucher> tgpf_AccVoucherList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpf_AccVoucherList != null)
		{
			for(Tgpf_AccVoucher object:tgpf_AccVoucherList)
			{
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
//				properties.put("userStart", object.getUserStart());
//				properties.put("userStartId", object.getUserStart().getTableId());
//				properties.put("createTimeStamp", object.getCreateTimeStamp());
//				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
//				properties.put("userRecord", object.getUserRecord());
//				properties.put("userRecordId", object.getUserRecord().getTableId());
//				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				properties.put("billTimeStamp", object.getBillTimeStamp());
//				properties.put("theType", object.getTheType());
//				properties.put("tradeCount", object.getTradeCount());
				properties.put("totalTradeAmount", object.getTotalTradeAmount());
				properties.put("bankTradeAmount", object.getTotalTradeAmount());
				properties.put("contentJson", object.getContentJson());
				properties.put("payoutTimeStamp", object.getPayoutTimeStamp());
				properties.put("companyName", object.getCompany().getTheName());
//				properties.put("companyId", object.getCompany().getTableId());
//				properties.put("theNameOfCompany", object.getTheNameOfCompany());
				properties.put("projectName", object.getProject().getTheName());
//				properties.put("eCodeFromConstruction", object.getBuilding().geteCodeFromConstruction());
//				properties.put("eCodeFromPublicSecurity", object.getBuilding().geteCodeFromPublicSecurity());
//				properties.put("projectId", object.getProject().getTableId());
//				properties.put("theNameOfProject", object.getTheNameOfProject());
//				properties.put("bank", object.getBank());
//				properties.put("bankId", object.getBank().getTableId());
				properties.put("theNameOfBank", object.getBankAccountEscrowed().getBankBranch().getTheName());
				properties.put("dayEndBalancingState", object.getDayEndBalancingState());
//				properties.put("bankAccountEscrowed", object.getBankAccountEscrowed());
//				properties.put("bankAccountEscrowedId", object.getBankAccountEscrowed().getTableId());
//				properties.put("theAccountOfBankAccountEscrowed", object.getTheAccountOfBankAccountEscrowed());
//				properties.put("payoutAmount", object.getPayoutAmount());
				
				Tgpf_FundAppropriated_AF tgpf_FundAppropriated_AF = object.getTgpf_FundAppropriated_AF();
				if(null != tgpf_FundAppropriated_AF)
				{
					properties.put("fundAppropriated_AFId", tgpf_FundAppropriated_AF.getTableId());
				}				
				
				list.add(properties);
			}
		}
		return list;
	}
}
