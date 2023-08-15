package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Tgpf_DayEndBalancing;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：业务对账-日终结算
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpf_DayEndBalancingRebuild extends RebuilderBase<Tgpf_DayEndBalancing>
{
	@Override
	public Properties getSimpleInfo(Tgpf_DayEndBalancing object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_DayEndBalancing object)
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
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpf_DayEndBalancing> tgpf_DayEndBalancingList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpf_DayEndBalancingList != null)
		{
			for(Tgpf_DayEndBalancing object:tgpf_DayEndBalancingList)
			{
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());
				properties.put("theState", object.getTheState());
//				properties.put("busiState", object.getBusiState());
//				properties.put("eCode", object.geteCode());
//				properties.put("userStart", object.getUserStart());
//				properties.put("userStartId", object.getUserStart().getTableId());
//				properties.put("createTimeStamp", object.getCreateTimeStamp());
//				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
//				properties.put("userRecord", object.getUserRecord());
//				properties.put("userRecordId", object.getUserRecord().getTableId());
//				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				Tgxy_BankAccountEscrowed bankAccountEscrowed = object.getTgxy_BankAccountEscrowed();
				
				Emmp_BankBranch bankBranch = bankAccountEscrowed.getBankBranch();
				
				properties.put("theAccount", bankAccountEscrowed.getTheAccount()); // 托管账号
				properties.put("theName", bankAccountEscrowed.getTheName());	// 托管账号名称
				properties.put("shortName", bankBranch.getShortName());	// 银行简称
				properties.put("theNameOfBank", bankBranch.getTheName()); // 开户行名称
				properties.put("totalCount", object.getTotalCount());
				properties.put("totalAmount", object.getTotalAmount());
				properties.put("billTimeStamp", object.getBillTimeStamp());
				properties.put("recordState", object.getRecordState());
				properties.put("settlementState", object.getSettlementState());
				
				list.add(properties);
			}
		}
		return list;
	}
}
