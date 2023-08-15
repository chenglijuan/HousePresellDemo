package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgpf_BankUploadDataDetail;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：银行对账单数据
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpf_BankUploadDataDetailRebuild extends RebuilderBase<Tgpf_BankUploadDataDetail>
{
	@Override
	public Properties getSimpleInfo(Tgpf_BankUploadDataDetail object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_BankUploadDataDetail object)
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
		properties.put("bank", object.getBank());
		properties.put("bankId", object.getBank().getTableId());
		properties.put("theNameOfBank", object.getTheNameOfBank());
		properties.put("bankBranch", object.getBankBranch());
		properties.put("bankBranchId", object.getBankBranch().getTableId());
		properties.put("theNameOfBankBranch", object.getTheNameOfBankBranch());
		properties.put("bankAccountEscrowed", object.getBankAccountEscrowed());
		properties.put("bankAccountEscrowedId", object.getBankAccountEscrowed().getTableId());
		properties.put("theNameOfBankAccountEscrowed", object.getTheNameOfBankAccountEscrowed());
		properties.put("theAccountBankAccountEscrowed", object.getTheAccountBankAccountEscrowed());
		properties.put("theAccountOfBankAccountEscrowed", object.getTheAccountOfBankAccountEscrowed());
		properties.put("tradeAmount", object.getTradeAmount());
		properties.put("enterTimeStamp", object.getEnterTimeStamp());
		properties.put("recipientAccount", object.getRecipientAccount());
		properties.put("recipientName", object.getRecipientName());
		properties.put("lastCfgUser", object.getLastCfgUser());
		properties.put("lastCfgTimeStamp", object.getLastCfgTimeStamp());
		properties.put("bkpltNo", object.getBkpltNo());
		properties.put("eCodeOfTripleAgreement", object.geteCodeOfTripleAgreement());
		properties.put("reconciliationState", object.getReconciliationState());
		properties.put("reconciliationStamp", object.getReconciliationStamp());
		properties.put("remark", object.getRemark());
		properties.put("coreNo", object.getCoreNo());
		properties.put("reconciliationUser", object.getReconciliationUser());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpf_BankUploadDataDetail> tgpf_BankUploadDataDetailList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpf_BankUploadDataDetailList != null)
		{
			for(Tgpf_BankUploadDataDetail object:tgpf_BankUploadDataDetailList)
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
				properties.put("bank", object.getBank());
				properties.put("bankId", object.getBank().getTableId());
				properties.put("theNameOfBank", object.getTheNameOfBank());
				properties.put("bankBranch", object.getBankBranch());
				properties.put("bankBranchId", object.getBankBranch().getTableId());
				properties.put("theNameOfBankBranch", object.getTheNameOfBankBranch());
				properties.put("bankAccountEscrowed", object.getBankAccountEscrowed());
				properties.put("bankAccountEscrowedId", object.getBankAccountEscrowed().getTableId());
				properties.put("theNameOfBankAccountEscrowed", object.getTheNameOfBankAccountEscrowed());
				properties.put("theAccountBankAccountEscrowed", object.getTheAccountBankAccountEscrowed());
				properties.put("theAccountOfBankAccountEscrowed", object.getTheAccountOfBankAccountEscrowed());
				properties.put("tradeAmount", object.getTradeAmount());
				properties.put("enterTimeStamp", object.getEnterTimeStamp());
				properties.put("recipientAccount", object.getRecipientAccount());
				properties.put("recipientName", object.getRecipientName());
				properties.put("lastCfgUser", object.getLastCfgUser());
				properties.put("lastCfgTimeStamp", object.getLastCfgTimeStamp());
				properties.put("bkpltNo", object.getBkpltNo());
				properties.put("eCodeOfTripleAgreement", object.geteCodeOfTripleAgreement());
				properties.put("reconciliationState", object.getReconciliationState());
				properties.put("reconciliationStamp", object.getReconciliationStamp());
				properties.put("remark", object.getRemark());
				properties.put("coreNo", object.getCoreNo());
				properties.put("reconciliationUser", object.getReconciliationUser());
				
				list.add(properties);
			}
		}
		return list;
	}
}
