package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgpf_SocketMsg;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：接口报文表
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpf_SocketMsgRebuild extends RebuilderBase<Tgpf_SocketMsg>
{
	@Override
	public Properties getSimpleInfo(Tgpf_SocketMsg object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_SocketMsg object)
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
		properties.put("msgLength", object.getMsgLength());
		properties.put("locationCode", object.getLocationCode());
		properties.put("msgBusinessCode", object.getMsgBusinessCode());
		properties.put("bankCode", object.getBankCode());
		properties.put("returnCode", object.getReturnCode());
		properties.put("remark", object.getRemark());
		properties.put("msgDirection", object.getMsgDirection());
		properties.put("msgStatus", object.getMsgStatus());
		properties.put("md5Check", object.getMd5Check());
		properties.put("msgTimeStamp", object.getMsgTimeStamp());
		properties.put("eCodeOfTripleAgreement", object.geteCodeOfTripleAgreement());
		properties.put("eCodeOfPermitRecord", object.geteCodeOfPermitRecord());
		properties.put("eCodeOfContractRecord", object.geteCodeOfContractRecord());
		properties.put("msgSerialno", object.getMsgSerialno());
		properties.put("msgContent", object.getMsgContent());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpf_SocketMsg> tgpf_SocketMsgList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpf_SocketMsgList != null)
		{
			for(Tgpf_SocketMsg object:tgpf_SocketMsgList)
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
				properties.put("msgLength", object.getMsgLength());
				properties.put("locationCode", object.getLocationCode());
				properties.put("msgBusinessCode", object.getMsgBusinessCode());
				properties.put("bankCode", object.getBankCode());
				properties.put("returnCode", object.getReturnCode());
				properties.put("remark", object.getRemark());
				properties.put("msgDirection", object.getMsgDirection());
				properties.put("msgStatus", object.getMsgStatus());
				properties.put("md5Check", object.getMd5Check());
				properties.put("msgTimeStamp", object.getMsgTimeStamp());
				properties.put("eCodeOfTripleAgreement", object.geteCodeOfTripleAgreement());
				properties.put("eCodeOfPermitRecord", object.geteCodeOfPermitRecord());
				properties.put("eCodeOfContractRecord", object.geteCodeOfContractRecord());
				properties.put("msgSerialno", object.getMsgSerialno());
				properties.put("msgContent", object.getMsgContent());
				
				list.add(properties);
			}
		}
		return list;
	}
}
