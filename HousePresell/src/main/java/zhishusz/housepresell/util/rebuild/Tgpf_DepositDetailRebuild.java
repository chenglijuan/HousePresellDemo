package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：资金归集-明细表
 * Company：ZhiShuSZ
 */
@Service
public class Tgpf_DepositDetailRebuild extends RebuilderBase<Tgpf_DepositDetail>
{
	@Override
	public Properties getSimpleInfo(Tgpf_DepositDetail object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());

		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_DepositDetail object)
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
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("userRecord", object.getUserRecord());
		properties.put("userRecordId", object.getUserRecord().getTableId());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("eCodeFromPayment", object.geteCodeFromPayment());
		properties.put("fundProperty", object.getFundProperty());
		properties.put("bankAccountEscrowed", object.getBankAccountEscrowed());
		properties.put("bankAccountEscrowedId", object.getBankAccountEscrowed().getTableId());
		properties.put("bankBranch", object.getBankBranch());
		properties.put("bankBranchId", object.getBankBranch().getTableId());
		properties.put("theNameOfBankAccountEscrowed", object.getTheNameOfBankAccountEscrowed());
		properties.put("theAccountOfBankAccountEscrowed", object.getTheAccountOfBankAccountEscrowed());
		properties.put("theNameOfCreditor", object.getTheNameOfCreditor());
		properties.put("idType", object.getIdType());
		properties.put("idNumber", object.getIdNumber());
		properties.put("bankAccountForLoan", object.getBankAccountForLoan());
		properties.put("loanAmountFromBank", object.getLoanAmountFromBank());
		properties.put("billTimeStamp", object.getBillTimeStamp());
		properties.put("eCodeFromBankCore", object.geteCodeFromBankCore());
		properties.put("eCodeFromBankPlatform", object.geteCodeFromBankPlatform());
		properties.put("remarkFromDepositBill", object.getRemarkFromDepositBill());
		properties.put("theNameOfBankBranchFromDepositBill", object.getTheNameOfBankBranchFromDepositBill());
		properties.put("theNameOfBankBranchFromDepositBillId",
				object.getTheNameOfBankBranchFromDepositBill().getTableId());
		properties.put("eCodeFromBankWorker", object.geteCodeFromBankWorker());
		properties.put("depositState", object.getDepositState());
		properties.put("dayEndBalancing", object.getDayEndBalancing());
		properties.put("dayEndBalancingId", object.getDayEndBalancing().getTableId());
		properties.put("depositDatetime", object.getDepositDatetime());
		properties.put("reconciliationTimeStampFromBusiness", object.getReconciliationTimeStampFromBusiness());
		properties.put("reconciliationStateFromBusiness", object.getReconciliationStateFromBusiness());
		properties.put("reconciliationTimeStampFromCyberBank", object.getReconciliationTimeStampFromCyberBank());
		properties.put("reconciliationStateFromCyberBank", object.getReconciliationStateFromCyberBank());
		properties.put("hasVoucher", object.getHasVoucher());
		properties.put("timestampFromReverse", object.getTimestampFromReverse());
		properties.put("theStateFromReverse", object.getTheStateFromReverse());
		properties.put("eCodeFromReverse", object.geteCodeFromReverse());

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpf_DepositDetail> tgpf_DepositDetailList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (tgpf_DepositDetailList != null)
		{
			for (Tgpf_DepositDetail object : tgpf_DepositDetailList)
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
				properties.put("eCodeFromPayment", object.geteCodeFromPayment());
				properties.put("fundProperty", object.getFundProperty());
				properties.put("bankAccountEscrowed", object.getBankAccountEscrowed());
				properties.put("bankAccountEscrowedId", object.getBankAccountEscrowed().getTableId());
				properties.put("bankBranch", object.getBankBranch());
				properties.put("bankBranchId", object.getBankBranch().getTableId());
				properties.put("theNameOfBankAccountEscrowed", object.getTheNameOfBankAccountEscrowed());
				properties.put("theAccountOfBankAccountEscrowed", object.getTheAccountOfBankAccountEscrowed());
				properties.put("theNameOfCreditor", object.getTheNameOfCreditor());
				properties.put("idType", object.getIdType());
				properties.put("idNumber", object.getIdNumber());
				properties.put("bankAccountForLoan", object.getBankAccountForLoan());
				properties.put("loanAmountFromBank", object.getLoanAmountFromBank());
				properties.put("billTimeStamp", object.getBillTimeStamp());
				properties.put("eCodeFromBankCore", object.geteCodeFromBankCore());
				properties.put("eCodeFromBankPlatform", object.geteCodeFromBankPlatform());
				properties.put("remarkFromDepositBill", object.getRemarkFromDepositBill());
				properties.put("theNameOfBankBranchFromDepositBill", object.getTheNameOfBankBranchFromDepositBill());
				properties.put("theNameOfBankBranchFromDepositBillId",
						object.getTheNameOfBankBranchFromDepositBill().getTableId());
				properties.put("eCodeFromBankWorker", object.geteCodeFromBankWorker());
				properties.put("depositState", object.getDepositState());
				properties.put("dayEndBalancing", object.getDayEndBalancing());
				properties.put("dayEndBalancingId", object.getDayEndBalancing().getTableId());
				properties.put("depositDatetime", object.getDepositDatetime());
				properties.put("reconciliationTimeStampFromBusiness", object.getReconciliationTimeStampFromBusiness());
				properties.put("reconciliationStateFromBusiness", object.getReconciliationStateFromBusiness());
				properties.put("reconciliationTimeStampFromCyberBank",
						object.getReconciliationTimeStampFromCyberBank());
				properties.put("reconciliationStateFromCyberBank", object.getReconciliationStateFromCyberBank());
				properties.put("hasVoucher", object.getHasVoucher());
				properties.put("timestampFromReverse", object.getTimestampFromReverse());
				properties.put("theStateFromReverse", object.getTheStateFromReverse());
				properties.put("eCodeFromReverse", object.geteCodeFromReverse());

				list.add(properties);
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List getDetail(List<Tgpf_DepositDetail> tgpf_DepositDetailList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (tgpf_DepositDetailList != null)
		{
			for (Tgpf_DepositDetail object : tgpf_DepositDetailList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());
				properties.put("theState", object.getTheState());
				properties.put("tripleAgreementNum", object.getTripleAgreementNum());
				properties.put("tripleAgreementNumBank", object.getTripleAgreementNumBank());

				properties.put("billTimeStamp", object.getBillTimeStamp());
				properties.put("billTimeStampBank", object.getBankBillTimeStamp());

				properties.put("theNameOfCreditor", object.getTheNameOfCreditor());
				properties.put("theNameOfCreditorBank", object.getTheNameOfCreditorBank());

				properties.put("bankAccountForLoan", object.getBankAccountForLoan());
				properties.put("bankAccountForLoanBank", object.getBankAccountForLoanBank());

				properties.put("loanAmountFromBank", object.getLoanAmountFromBank());
				properties.put("bankAmount", object.getBankAmount());

				properties.put("reconciliationStateFromBusiness", object.getReconciliationStateFromBusiness());

				list.add(properties);
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin2(List<Tgpf_DepositDetail> tgpf_DepositDetailList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (tgpf_DepositDetailList != null)
		{
			for (Tgpf_DepositDetail object : tgpf_DepositDetailList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());// 主键
				properties.put("depositDatetime", object.getDepositDatetime());// 缴款记账日期

				if (object.getTripleAgreement() != null)
				{
					Tgxy_TripleAgreement tripleAgreement = object.getTripleAgreement();
					properties.put("eCodeOfTripleAgreement", tripleAgreement.geteCodeOfTripleAgreement());// 托管协议号
					properties.put("eCodeOfContractRecord", tripleAgreement.geteCodeOfContractRecord());// 合同备案号
					properties.put("sellerName", tripleAgreement.getSellerName());// 开发企业
					properties.put("theNameOfProject", tripleAgreement.getTheNameOfProject());// 开发项目

					Empj_BuildingInfo buildingInfo = tripleAgreement.getBuildingInfo();

					String isAdvanceSale = buildingInfo.getIsAdvanceSale();

					if (null != isAdvanceSale && "1".equals(isAdvanceSale.trim()))
					{
						properties.put("eCodeFromConstruction", tripleAgreement.geteCodeFromConstruction() + "（变更预售）");// 楼幢编号
					}
					else
					{
						properties.put("eCodeFromConstruction", tripleAgreement.geteCodeFromConstruction());// 楼幢编号
					}

					if (null == tripleAgreement.getUnitInfo())
					{
						properties.put("eCodeOfUnit", "");// 单元
					}
					else
					{
						properties.put("eCodeOfUnit", tripleAgreement.getUnitInfo().getTheName());// 单元
					}
					properties.put("unitRoom", tripleAgreement.getUnitRoom());// 室号

					properties.put("contractAmount", tripleAgreement.getContractAmount());// 合同金额
					properties.put("loanAmount", tripleAgreement.getLoanAmount());// 贷款金额
					properties.put("buyerName", tripleAgreement.getBuyerName());// 购房人
				}
				else
				{
					properties.put("eCodeOfTripleAgreement", "");
					properties.put("eCodeOfContractRecord", "");
					properties.put("sellerName", "");
					properties.put("theNameOfProject", "");
					properties.put("eCodeFromConstruction", "");
					properties.put("eCodeOfUnit", "");
					properties.put("unitRoom", "");
					properties.put("contractAmount", "");
					properties.put("loanAmount", "");
					properties.put("buyerName", "");
				}

				properties.put("theNameOfCreditor", object.getTheNameOfCreditor());// 借款人

				if (null == object.getIdType())
				{
					properties.put("idType", "1");// 证件类型 S_IDType
				}
				else
				{
					properties.put("idType", object.getIdType());// 证件类型
																	// S_IDType
				}

				properties.put("idNumber", object.getIdNumber());// 证件号码
				properties.put("bankAccountForLoan", object.getBankAccountForLoan());// 用于接收贷款的银行账号
																						// 银行账号（贷）
				properties.put("loanAmountFromBank", object.getLoanAmountFromBank());// 银行放款金额（元）
				properties.put("fundProperty", object.getFundProperty());// 资金性质
																			// 自有资金
																			// 商业贷款
																			// 公积金贷款
																			// 公转商贷款
																			// 公积金首付款

				if (object.getBankBranch() != null)
				{
					properties.put("bankBranchName", object.getBankBranch().getTheName());// 开户行
				}
				else
				{
					properties.put("bankBranchName", "");
				}

				properties.put("theAccountOfBAE", object.getTheAccountOfBankAccountEscrowed());// 托管账号
				properties.put("remarkFromDepositBill", object.getRemarkFromDepositBill());// 缴款记账备注

				list.add(properties);
			}
		}
		return list;
	}
}
