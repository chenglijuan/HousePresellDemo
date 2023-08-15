package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettle;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：三方协议结算-主表
 * Company：ZhiShuSZ
 * */
@Service
public class Tgxy_CoopAgreementSettleRebuild extends RebuilderBase<Tgxy_CoopAgreementSettle>
{
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	@Override
	public Properties getSimpleInfo(Tgxy_CoopAgreementSettle object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tgxy_CoopAgreementSettle object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("theState", object.getTheState());
//		properties.put("busiState", object.getBusiState());
//		properties.put("userStart", object.getUserStart());
//		properties.put("userStartId", object.getUserStart().getTableId());
//		properties.put("createTimeStamp", object.getCreateTimeStamp());
		if(null == object.getUserUpdate()){
			properties.put("userUpdate", "-");
		}else{
			properties.put("userUpdate", object.getUserUpdate().getTheName());
		}
		properties.put("lastUpdateTimeStamp", myDatetime.dateToString2(object.getLastUpdateTimeStamp()));
//		properties.put("userRecord", object.getUserRecord().getTheName());
//		properties.put("userRecordId", object.getUserRecord().getTableId());
//		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("eCode", object.geteCode());
		properties.put("signTimeStamp", object.getSignTimeStamp());
//		properties.put("agentCompany", object.getAgentCompany());
//		properties.put("agentCompanyId", object.getAgentCompany().getTableId());
		properties.put("applySettlementDate", object.getApplySettlementDate());
		properties.put("startSettlementDate", object.getStartSettlementDate());
		properties.put("endSettlementDate", object.getEndSettlementDate());
		properties.put("protocolNumbers", object.getProtocolNumbers());
		properties.put("beforeNumbers", null == object.getBeforeNumbers()?0:object.getBeforeNumbers());
		properties.put("settlementState", object.getSettlementState());
		properties.put("companyName", object.getCompanyName()); // 企业名称
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgxy_CoopAgreementSettle> tgxy_CoopAgreementSettleList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgxy_CoopAgreementSettleList != null)
		{
			for(Tgxy_CoopAgreementSettle object:tgxy_CoopAgreementSettleList)
			{
				Properties properties = new MyProperties();
				properties.put("tableId", object.getTableId());
				properties.put("theState", object.getTheState());
//				properties.put("busiState", object.getBusiState());
//				properties.put("userStart", object.getUserStart());
//				properties.put("userStartId", object.getUserStart().getTableId());
//				properties.put("createTimeStamp", object.getCreateTimeStamp());
//				properties.put("userUpdate", object.getUserUpdate().getTheName());
//				properties.put("lastUpdateTimeStamp", myDatetime.dateToSimpleString(object.getLastUpdateTimeStamp()));
				properties.put("userRecord", object.getRecordName());
//				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				properties.put("eCode", object.geteCode());
				properties.put("signTimeStamp", object.getSignTimeStamp());
//				properties.put("agentCompany", object.getAgentCompany());
//				properties.put("agentCompanyId", object.getAgentCompany().getTableId());
				properties.put("applySettlementDate", object.getApplySettlementDate());
				properties.put("startSettlementDate", object.getStartSettlementDate());
				properties.put("endSettlementDate", object.getEndSettlementDate());
				properties.put("protocolNumbers", object.getProtocolNumbers());
				properties.put("settlementState", object.getSettlementState());
				properties.put("companyName", object.getAgentCompany().getTheName()); // 企业名称
				
				list.add(properties);
			}
		}
		return list;
	}
}
