package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：对账列表
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpf_BalanceOfAccountRebuild extends RebuilderBase<Tgpf_BalanceOfAccount>
{
	@Override
	public Properties getSimpleInfo(Tgpf_BalanceOfAccount object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_BalanceOfAccount object)
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
		properties.put("billTimeStamp", object.getBillTimeStamp());
		properties.put("bankName", object.getBankName());
		properties.put("escrowedAccount", object.getEscrowedAccount());
		properties.put("escrowedAccountTheName", object.getEscrowedAccountTheName());
		properties.put("centerTotalCount", object.getCenterTotalCount());
		properties.put("centerTotalAmount", object.getCenterTotalAmount());
		properties.put("bankTotalCount", object.getBankTotalCount());
		properties.put("bankTotalAmount", object.getBankTotalAmount());
		properties.put("cyberBankTotalCount", object.getCyberBankTotalCount());
		properties.put("cyberBankTotalAmount", object.getCyberBankTotalAmount());
		properties.put("accountType", object.getAccountType());
		properties.put("reconciliationDate", object.getReconciliationDate());
		properties.put("reconciliationState", object.getReconciliationState());
		properties.put("bankBranch", object.getBankBranch());
		properties.put("bankBranchId", object.getBankBranch().getTableId());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpf_BalanceOfAccount> tgpf_BalanceOfAccountList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpf_BalanceOfAccountList != null)
		{
			for(Tgpf_BalanceOfAccount object:tgpf_BalanceOfAccountList)
			{
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
//				properties.put("eCode", object.geteCode());
//				properties.put("userStart", object.getUserStart());
//				properties.put("userStartId", object.getUserStart().getTableId());
//				properties.put("createTimeStamp", object.getCreateTimeStamp());
//				properties.put("userUpdate", object.getUserUpdate());
//				properties.put("userUpdateId", object.getUserUpdate().getTableId());
//				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
//				properties.put("userRecord", object.getUserRecord());
//				properties.put("userRecordId", object.getUserRecord().getTableId());
				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				properties.put("billTimeStamp", object.getBillTimeStamp());
				properties.put("bankName", object.getBankName());
				properties.put("escrowedAccount", object.getEscrowedAccount());
//				properties.put("escrowedAccountTheName", object.getEscrowedAccountTheName());
				properties.put("escrowedAccountTheName", object.getBankBranch().getTheName());
				properties.put("centerTotalCount", object.getCenterTotalCount());
				properties.put("centerTotalAmount", object.getCenterTotalAmount());
				properties.put("bankTotalCount", object.getBankTotalCount());
				properties.put("bankTotalAmount", object.getBankTotalAmount());
				properties.put("cyberBankTotalCount", object.getCyberBankTotalCount());
				properties.put("cyberBankTotalAmount", object.getCyberBankTotalAmount());
				properties.put("accountType", object.getAccountType());
//				properties.put("reconciliationDate", object.getReconciliationDate());
				properties.put("reconciliationState", object.getReconciliationState());
//				properties.put("bankBranch", object.getBankBranch());
//				properties.put("bankBranchId", object.getBankBranch().getTableId());
				
				list.add(properties);
			}
		}
		return list;
	}
}
