package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgxy_BuyerInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：买受人信息
 * Company：ZhiShuSZ
 * */
@Service
public class Tgxy_BuyerInfoRebuild extends RebuilderBase<Tgxy_BuyerInfo>
{
	@Override
	public Properties getSimpleInfo(Tgxy_BuyerInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("buyerName", object.getBuyerName());
		properties.put("contactPhone", object.getContactPhone());
		properties.put("eCodeOfcertificate", object.geteCodeOfcertificate());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tgxy_BuyerInfo object)
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
		properties.put("buyerName", object.getBuyerName());
		properties.put("buyerType", object.getBuyerType());
		properties.put("certificateType", object.getCertificateType());
		properties.put("eCodeOfcertificate", object.geteCodeOfcertificate());
		properties.put("contactPhone", object.getContactPhone());
		properties.put("contactAdress", object.getContactAdress());
		properties.put("agentName", object.getAgentName());
		properties.put("agentCertType", object.getAgentCertType());
		properties.put("agentCertNumber", object.getAgentCertNumber());
		properties.put("agentPhone", object.getAgentPhone());
		properties.put("agentAddress", object.getAgentAddress());
		properties.put("eCodeOfContract", object.geteCodeOfContract());
		properties.put("userUpdate", object.getUserUpdate());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgxy_BuyerInfo> tgxy_BuyerInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgxy_BuyerInfoList != null)
		{
			for(Tgxy_BuyerInfo object:tgxy_BuyerInfoList)
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
				properties.put("buyerName", object.getBuyerName());
				properties.put("buyerType", object.getBuyerType());
				properties.put("certificateType", object.getCertificateType());
				properties.put("eCodeOfcertificate", object.geteCodeOfcertificate());
				properties.put("contactPhone", object.getContactPhone());
				properties.put("contactAdress", object.getContactAdress());
				properties.put("agentName", object.getAgentName());
				properties.put("agentCertType", object.getAgentCertType());
				properties.put("agentCertNumber", object.getAgentCertNumber());
				properties.put("agentPhone", object.getAgentPhone());
				properties.put("agentAddress", object.getAgentAddress());
				properties.put("eCodeOfContract", object.geteCodeOfContract());
				
				list.add(properties);
			}
		}
		return list;
	}
}
